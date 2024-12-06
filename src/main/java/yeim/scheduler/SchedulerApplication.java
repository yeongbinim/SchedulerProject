package yeim.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import yeim.scheduler.member.domain.Member;
import yeim.scheduler.member.infrastructure.MemberRepository;

@SpringBootApplication
public class SchedulerApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SchedulerApplication.class, args);
		MemberRepository memberRepository = ctx.getBean(MemberRepository.class);
		memberRepository.create(new Member(null, "yeim", "aaa@aaa.aaa", "1234", null, null));
		memberRepository.create(new Member(null, "hello", "bbb@bbb.bbb", "1234", null, null));
	}
}
