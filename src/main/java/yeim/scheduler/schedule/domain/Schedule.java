package yeim.scheduler.schedule.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Schedule {

	@Setter
	private Long id;
	private String author;
	private String content;
	private String password;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static Schedule from(ScheduleCreateRequest request) {
		return new Schedule(
			null,
			request.getAuthor(),
			request.getContent(),
			request.getPassword(),
			LocalDateTime.now(),
			LocalDateTime.now()
		);
	}

	public Schedule update(ScheduleUpdateRequest request) {
		return new Schedule(
			id,
			author,
			request.getContent(),
			request.getPassword(),
			createdAt,
			LocalDateTime.now()
		);
	}

	public boolean verifyPassword(String password) {
		return this.password.equals(password);
	}
}
