package yeim.scheduler.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

	private final HttpStatus httpStatus;
	private final String code;

	public CustomException(CustomExceptionType customExceptionType) {
		super(customExceptionType.getMessage());
		this.httpStatus = customExceptionType.getHttpStatus();
		this.code = customExceptionType.getCode();
	}
}
