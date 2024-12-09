package yeim.scheduler.schedule.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleUpdateRequest {

	@NotNull
	@Size(max = 200, message = "내용은 200자 이내여야 합니다")
	private String content;

	@NotNull
	private String password;
}
