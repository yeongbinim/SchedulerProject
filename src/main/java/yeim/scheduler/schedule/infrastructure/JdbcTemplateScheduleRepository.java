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
			SELECT id, member_id, content, created_at, updated_at
			FROM schedule
			ORDER BY updated_at DESC
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
			SELECT id, member_id, content, created_at, updated_at
			FROM schedule
			WHERE id = ?
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
			Member member = memberRepository.findById(rs.getLong("member_id")).orElse(null);
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
