package yeim.scheduler.schedule.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yeim.scheduler.common.exception.InvalidPasswordException;
import yeim.scheduler.common.exception.ResourceNotFoundException;
import yeim.scheduler.schedule.domain.Schedule;
import yeim.scheduler.schedule.domain.ScheduleCreateRequest;
import yeim.scheduler.schedule.domain.ScheduleDeleteRequest;
import yeim.scheduler.schedule.domain.ScheduleUpdateRequest;
import yeim.scheduler.schedule.infrastructure.ScheduleRepository;

@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final ScheduleRepository scheduleRepository;

	public Schedule createSchedule(ScheduleCreateRequest scheduleCreateRequest) {
		Schedule schedule = Schedule.from(scheduleCreateRequest);
		return scheduleRepository.create(schedule);
	}

	public List<Schedule> getAllSchedules() {
		return scheduleRepository.findAll();
	}

	public Schedule getScheduleById(Long id) {
		return scheduleRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Schedules", id));
	}

	public Schedule updateSchedule(Long id, ScheduleUpdateRequest scheduleUpdateRequest) {
		Schedule schedule = getScheduleById(id);

		// 비밀번호 검증
		if (!schedule.verifyPassword(scheduleUpdateRequest.getPassword())) {
			throw new InvalidPasswordException();
		}

		return scheduleRepository.update(id, schedule.update(scheduleUpdateRequest));
	}

	public void deleteSchedule(Long id, ScheduleDeleteRequest scheduleDeleteRequest) {
		Schedule schedule = getScheduleById(id);

		// 비밀번호 검증
		if (!schedule.verifyPassword(scheduleDeleteRequest.getPassword())) {
			throw new InvalidPasswordException();
		}

		scheduleRepository.delete(id);
	}
}
