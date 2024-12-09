package yeim.scheduler.schedule.infrastructure;

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
import yeim.scheduler.common.PageResponse;
import yeim.scheduler.member.domain.Member;
import yeim.scheduler.member.infrastructure.MemberRepository;
import yeim.scheduler.schedule.domain.Schedule;

@Repository
@Primary
public class JdbcTemplateScheduleRepository implements ScheduleRepository {

	private final JdbcTemplate template;
	private final MemberRepository memberRepository;

	public JdbcTemplateScheduleRepository(DataSource dataSource,
		MemberRepository memberRepository) {
		this.template = new JdbcTemplate(dataSource);
		this.memberRepository = memberRepository;
	}

	@Override
	public Schedule create(Schedule schedule) {
		String sql = """
			    INSERT INTO schedule (member_id, content, created_at, updated_at)
			    VALUES (?, ?, ?, ?)
			""";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		template.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
			ps.setLong(1, schedule.getMember().getId());
			ps.setString(2, schedule.getContent());
			ps.setString(3, schedule.getCreatedAt().toString());
			ps.setString(4, schedule.getUpdatedAt().toString());
			return ps;
		}, keyHolder);

		long key = keyHolder.getKey().longValue();
		schedule.setId(key);
		return schedule;
	}

	@Override
	public PageResponse<Schedule> findAll(int page, int size) {
		String rowCountSql = "SELECT count(*) FROM schedule";
		int total = template.queryForObject(rowCountSql, Integer.class);

		int offset = page * size;
		String sql = """
			SELECT s.id, s.member_id, s.content, s.created_at, s.updated_at,
			       m.name as member_name, m.email as member_email, m.password as member_password, m.created_at as member_created_at, m.updated_at as member_updated_at
			FROM schedule s
			JOIN member m ON s.member_id = m.id
			ORDER BY s.updated_at DESC
			LIMIT ? OFFSET ?
			""";
		List<Schedule> schedules = template.query(
			sql,
			new Object[]{size, offset},
			scheduleRowMapper()
		);

		return new PageResponse<>(schedules, page, size, total);
	}

	@Override
	public Optional<Schedule> findById(Long id) {
		String sql = """
			SELECT s.id, s.member_id, s.content, s.created_at, s.updated_at,
			       m.name as member_name, m.email as member_email, m.password as member_password, m.created_at as member_created_at, m.updated_at as member_updated_at
			FROM schedule s
			JOIN member m ON s.member_id = m.id
			WHERE s.id = ?
			""";
		Schedule schedule = template.queryForObject(sql, scheduleRowMapper(), id);
		return Optional.ofNullable(schedule);
	}

	@Override
	public Schedule update(Long id, Schedule schedule) {
		String sql = """
			UPDATE schedule
			SET member_id = ?, content = ?, created_at = ?, updated_at = ?
			WHERE id = ?
			""";

		template.update(
			sql,
			schedule.getMember().getId(),
			schedule.getContent(),
			schedule.getCreatedAt().toString(),
			schedule.getUpdatedAt().toString(),
			id
		);
		return schedule;
	}

	@Override
	public void delete(Long id) {
		String sql = """
			DELETE FROM schedule
			WHERE id = ?
			""";
		template.update(sql, id);
	}

	private RowMapper<Schedule> scheduleRowMapper() {
		return (rs, rowNum) -> {
			Member member = new Member(
				rs.getLong("member_id"),
				rs.getString("member_name"),
				rs.getString("member_email"),
				rs.getString("member_password"),
				rs.getTimestamp("member_created_at").toLocalDateTime(),
				rs.getTimestamp("member_updated_at").toLocalDateTime()
			);
			return new Schedule(
				rs.getLong("id"),
				member,
				rs.getString("content"),
				rs.getTimestamp("created_at").toLocalDateTime(),
				rs.getTimestamp("updated_at").toLocalDateTime()
			);
		};
	}

}
