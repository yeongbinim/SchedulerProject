package yeim.scheduler.member.infrastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;
import yeim.scheduler.member.domain.Member;

@Repository
public class MemoryMemberRepository implements MemberRepository {

	private final ConcurrentHashMap<Long, Member> store = new ConcurrentHashMap<>();
	private final AtomicLong sequence = new AtomicLong(0);

	@Override
	public Member create(Member member) {
		member.setId(sequence.incrementAndGet());
		store.put(member.getId(), member);
		return member;
	}

	@Override
	public List<Member> findAll() {
		return new ArrayList<>(store.values());
	}

	@Override
	public Optional<Member> findById(Long id) {
		return Optional.ofNullable(store.get(id));
	}
}
