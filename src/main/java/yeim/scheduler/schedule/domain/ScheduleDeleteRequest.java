package yeim.scheduler.schedule.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleDeleteRequest {

	@NotNull
	private String password;
}
