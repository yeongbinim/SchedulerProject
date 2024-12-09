package yeim.scheduler.common.exception.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import yeim.scheduler.common.exception.CustomExceptionType;

@Getter
@RequiredArgsConstructor
public enum ScheduleExceptionType implements CustomExceptionType {
	SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "S01", "일치하는 일정을 찾을 수 없습니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
