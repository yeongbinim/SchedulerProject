package yeim.scheduler.schedule.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import yeim.scheduler.member.domain.Member;

@Getter
@AllArgsConstructor
public class Schedule {

	@Setter
	private Long id;
	private Member member;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static Schedule from(Member member, ScheduleCreateRequest request) {
		return new Schedule(
			null,
			member,
			request.getContent(),
			LocalDateTime.now(),
			LocalDateTime.now()
		);
	}

	public Schedule update(ScheduleUpdateRequest request) {
		return new Schedule(
			id,
			member,
			request.getContent(),
			createdAt,
			LocalDateTime.now()
		);
	}
}
