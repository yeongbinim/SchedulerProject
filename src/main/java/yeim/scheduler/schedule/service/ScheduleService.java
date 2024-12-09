package yeim.scheduler.schedule.service;

import static yeim.scheduler.common.exception.enums.MemberExceptionType.MEMBER_NOT_FOUND;
import static yeim.scheduler.common.exception.enums.MemberExceptionType.PASSWORD_NOT_MATCH;
import static yeim.scheduler.common.exception.enums.ScheduleExceptionType.SCHEDULE_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yeim.scheduler.common.PageResponse;
import yeim.scheduler.common.exception.CustomException;
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
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
		Schedule schedule = Schedule.from(member, scheduleCreateRequest);
		return scheduleRepository.create(schedule);
	}

	public PageResponse<Schedule> getAllSchedules(int page, int size) {
		return scheduleRepository.findAll(page, size);
	}

	public Schedule getScheduleById(Long id) {
		return scheduleRepository.findById(id)
			.orElseThrow(() -> new CustomException(SCHEDULE_NOT_FOUND));
	}

	public Schedule updateSchedule(Long id, ScheduleUpdateRequest scheduleUpdateRequest) {
		Schedule schedule = getScheduleById(id);

		// 비밀번호 검증
		if (!schedule.getMember().verifyPassword(scheduleUpdateRequest.getPassword())) {
			throw new CustomException(PASSWORD_NOT_MATCH);
		}

		return scheduleRepository.update(id, schedule.update(scheduleUpdateRequest));
	}

	public void deleteSchedule(Long id, ScheduleDeleteRequest scheduleDeleteRequest) {
		Schedule schedule = getScheduleById(id);

		// 비밀번호 검증
		if (!schedule.getMember().verifyPassword(scheduleDeleteRequest.getPassword())) {
			throw new CustomException(PASSWORD_NOT_MATCH);
		}

		scheduleRepository.delete(id);
	}
}
