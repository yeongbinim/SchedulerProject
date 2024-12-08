package yeim.scheduler.schedule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yeim.scheduler.common.PageResponse;
import yeim.scheduler.common.exception.InvalidPasswordException;
import yeim.scheduler.common.exception.ResourceNotFoundException;
import yeim.scheduler.member.domain.Member;
import yeim.scheduler.member.infrastructure.MemberRepository;
import yeim.scheduler.schedule.domain.Schedule;
import yeim.scheduler.schedule.domain.ScheduleCreateRequest;
import yeim.scheduler.schedule.domain.ScheduleDeleteRequest;
import yeim.scheduler.schedule.domain.ScheduleUpdateRequest;
import yeim.scheduler.schedule.infrastructure.ScheduleRepository;

@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final ScheduleRepository scheduleRepository;
	private final MemberRepository memberRepository;

	public Schedule createSchedule(Long memberId, ScheduleCreateRequest scheduleCreateRequest) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new ResourceNotFoundException("Member", memberId));
		Schedule schedule = Schedule.from(member, scheduleCreateRequest);
		return scheduleRepository.create(schedule);
	}

	public PageResponse<Schedule> getAllSchedules(int page, int size) {
		return scheduleRepository.findAll(page, size);
	}

	public Schedule getScheduleById(Long id) {
		return scheduleRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Schedules", id));
	}

	public Schedule updateSchedule(Long id, ScheduleUpdateRequest scheduleUpdateRequest) {
		Schedule schedule = getScheduleById(id);

		// 비밀번호 검증
		if (!schedule.getMember().verifyPassword(scheduleUpdateRequest.getPassword())) {
			throw new InvalidPasswordException();
		}

		return scheduleRepository.update(id, schedule.update(scheduleUpdateRequest));
	}

	public void deleteSchedule(Long id, ScheduleDeleteRequest scheduleDeleteRequest) {
		Schedule schedule = getScheduleById(id);

		// 비밀번호 검증
		if (!schedule.getMember().verifyPassword(scheduleDeleteRequest.getPassword())) {
			throw new InvalidPasswordException();
		}

		scheduleRepository.delete(id);
	}
}
