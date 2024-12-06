package yeim.scheduler.schedule.controller;

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
import org.springframework.web.bind.annotation.RestController;
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

	@GetMapping
	public ResponseEntity<List<ScheduleResponse>> getAllSchedules() {
		List<Schedule> schedules = scheduleService.getAllSchedules();

		return ResponseEntity
			.ok()
			.body(schedules.stream().map(ScheduleResponse::from).toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ScheduleResponse> getScheduleById(@PathVariable Long id) {
		Schedule schedule = scheduleService.getScheduleById(id);

		return ResponseEntity
			.ok()
			.body(ScheduleResponse.from(schedule));
	}

	@PostMapping
	public ResponseEntity<Void> createSchedule(@RequestBody ScheduleCreateRequest request) {
		Schedule createdSchedule = scheduleService.createSchedule(request);

		return ResponseEntity
			.created(URI.create("/api/schedules/" + createdSchedule.getId()))
			.build();
	}

	@PutMapping("/{id}")
	public ResponseEntity<ScheduleResponse> updateSchedule(@PathVariable Long id,
		@RequestBody ScheduleUpdateRequest request) {
		Schedule schedule = scheduleService.updateSchedule(id, request);
		return ResponseEntity
			.ok()
			.body(ScheduleResponse.from(schedule));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSchedule(@PathVariable Long id,
		@RequestBody ScheduleDeleteRequest request) {
		scheduleService.deleteSchedule(id, request);

		return ResponseEntity
			.noContent()
			.build();
	}
}
