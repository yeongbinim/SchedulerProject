package yeim.scheduler.schedule.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yeim.scheduler.common.PageResponse;
import yeim.scheduler.schedule.domain.Schedule;
import yeim.scheduler.schedule.domain.ScheduleCreateRequest;
import yeim.scheduler.schedule.domain.ScheduleDeleteRequest;
import yeim.scheduler.schedule.domain.ScheduleResponse;
import yeim.scheduler.schedule.domain.ScheduleUpdateRequest;
import yeim.scheduler.schedule.service.ScheduleService;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ApiScheduleController {

	private final ScheduleService scheduleService;

	@PostMapping
	public ResponseEntity<Void> createSchedule(@RequestParam Long memberId,
		@Valid @RequestBody ScheduleCreateRequest request) {
		Schedule createdSchedule = scheduleService.createSchedule(memberId, request);

		return ResponseEntity
			.created(URI.create("/api/schedules/" + createdSchedule.getId()))
			.build();
	}

	@GetMapping
	public ResponseEntity<PageResponse<ScheduleResponse>> getAllSchedules(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {

		PageResponse<Schedule> schedules = scheduleService.getAllSchedules(page, size);

		List<ScheduleResponse> scheduleResponses = schedules.getContent().stream()
			.map(ScheduleResponse::from)
			.toList();

		PageResponse<ScheduleResponse> response = new PageResponse<>(
			scheduleResponses,
			schedules.getCurrentPage(),
			schedules.getPageSize(),
			schedules.getTotalElements());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ScheduleResponse> getScheduleById(@PathVariable Long id) {
		Schedule schedule = scheduleService.getScheduleById(id);

		return ResponseEntity
			.ok()
			.body(ScheduleResponse.from(schedule));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ScheduleResponse> updateSchedule(@PathVariable Long id,
		@Valid @RequestBody ScheduleUpdateRequest request) {
		Schedule schedule = scheduleService.updateSchedule(id, request);
		return ResponseEntity
			.ok()
			.body(ScheduleResponse.from(schedule));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSchedule(@PathVariable Long id,
		@Valid @RequestBody ScheduleDeleteRequest request) {
		scheduleService.deleteSchedule(id, request);

		return ResponseEntity
			.noContent()
			.build();
	}
}
