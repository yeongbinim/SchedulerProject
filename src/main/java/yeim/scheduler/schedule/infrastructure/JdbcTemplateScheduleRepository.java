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
import yeim.scheduler.schedule.domain.Schedule;

@Repository
@Primary
public class JdbcTemplateScheduleRepository implements ScheduleRepository {

	private final JdbcTemplate template;

	public JdbcTemplateScheduleRepository(DataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}

	@Override
	public Schedule create(Schedule schedule) {
		String sql = """
				INSERT INTO task (author, password, content)
				VALUES (?, ?, ?)
			""";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		template.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
			ps.setString(1, schedule.getAuthor());
			ps.setString(2, schedule.getPassword());
			ps.setString(3, schedule.getContent());
			return ps;
		}, keyHolder);

		long key = keyHolder.getKey().longValue();
		schedule.setId(key);
		return schedule;
	}

	@Override
	public List<Schedule> findAll() {
		String sql = """
			SELECT id, author, content, password, created_at, updated_at
			FROM task
			""";
		return template.query(sql, scheduleRowMapper());
	}

	@Override
	public Optional<Schedule> findById(Long id) {
		String sql = """
			SELECT id, author, content, password, created_at, updated_at
			FROM task
			WHERE id = ?
			""";
		Schedule schedule = template.queryForObject(sql, scheduleRowMapper(), id);
		return Optional.ofNullable(schedule);
	}

	@Override
	public Schedule update(Long id, Schedule schedule) {
		String sql = """
			UPDATE task
			SET author = ?, password = ?, content = ?
			WHERE id = ?
			""";

		template.update(
			sql,
			schedule.getAuthor(),
			schedule.getPassword(),
			schedule.getContent(),
			id
		);
		return schedule;
	}

	@Override
	public void delete(Long id) {
		String sql = """
			    DELETE FROM task
			    WHERE id = ?
			""";
		template.update(sql, id);
	}

	private RowMapper<Schedule> scheduleRowMapper() {
		return ((rs, rowNum) -> new Schedule(
			rs.getLong("id"),
			rs.getString("author"),
			rs.getString("content"),
			rs.getString("password"),
			rs.getTimestamp("created_at").toLocalDateTime(),
			rs.getTimestamp("updated_at").toLocalDateTime()
		));
	}
}
