package yeim.scheduler.member.infrastructure;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import yeim.scheduler.member.domain.Member;

@Repository
@Primary
public class JdbcTemplateMemberRepository implements MemberRepository {

	private final JdbcTemplate template;

	public JdbcTemplateMemberRepository(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}

	@Override
	public Member create(Member member) {
		String sql = """
				INSERT INTO member (name, email, password, created_at, updated_at)
				VALUES (?, ?, ?, ?, ?)
			""";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		template.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
			ps.setString(1, member.getName());
			ps.setString(2, member.getEmail());
			ps.setString(3, member.getPassword());
			ps.setString(4, member.getCreatedAt().toString());
			ps.setString(5, member.getUpdatedAt().toString());
			return ps;
		}, keyHolder);

		long key = keyHolder.getKey().longValue();
		member.setId(key);
		return member;
	}

	@Override
	public List<Member> findAll() {
		String sql = """
			SELECT id, name, email, password, created_at, updated_at
			FROM member
			""";
		return template.query(sql, memberRowMapper());
	}

	@Override
	public Optional<Member> findById(Long id) {
		String sql = """
			SELECT id, name, email, password, created_at, updated_at
			FROM member
			WHERE id = ?
			""";
		Member member = template.queryForObject(sql, memberRowMapper(), id);
		return Optional.ofNullable(member);
	}

	private RowMapper<Member> memberRowMapper() {
		return ((rs, rowNum) -> new Member(
			rs.getLong("id"),
			rs.getString("name"),
			rs.getString("email"),
			rs.getString("password"),
			rs.getTimestamp("created_at").toLocalDateTime(),
			rs.getTimestamp("updated_at").toLocalDateTime()
		));
	}
}
