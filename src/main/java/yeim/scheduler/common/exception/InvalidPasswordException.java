package yeim.scheduler.common.exception;

public class InvalidPasswordException extends RuntimeException {

	public InvalidPasswordException() {
		super("제공된 비밀번호가 올바르지 않습니다.");
	}
}
