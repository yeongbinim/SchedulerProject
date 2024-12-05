package yeim.scheduler.schedule.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleCreateRequest {

	private String author;
	private String content;
	private String password;
}