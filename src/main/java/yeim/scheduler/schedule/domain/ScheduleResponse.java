package yeim.scheduler.schedule.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleResponse {

	private Long id;
	private String author;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static ScheduleResponse from(Schedule schedule) {
		return new ScheduleResponse(
			schedule.getId(),
			schedule.getMember().getName(),
			schedule.getContent(),
			schedule.getCreatedAt(),
			schedule.getUpdatedAt()
		);
	}
}
