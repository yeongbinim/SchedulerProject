package yeim.scheduler.member.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Member {

	@Setter
	private Long id;
	private String name;
	private String email;
	private String password;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public boolean verifyPassword(String password) {
		return this.password.equals(password);
	}
}
