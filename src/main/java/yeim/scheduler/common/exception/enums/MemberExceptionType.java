package yeim.scheduler.common.exception.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import yeim.scheduler.common.exception.CustomExceptionType;

@Getter
@RequiredArgsConstructor
public enum MemberExceptionType implements CustomExceptionType {

	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M01", "일치하는 멤버를 찾을 수 없습니다."),
	PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "M02", "비밀번호가 일치하지 않습니다."),
	EMAIL_DUPLICATE(HttpStatus.BAD_REQUEST, "M03", "이메일이 중복됩니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
