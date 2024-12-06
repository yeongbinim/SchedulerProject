package yeim.scheduler.schedule.infrastructure;

import java.util.List;
import java.util.Optional;
import yeim.scheduler.schedule.domain.Schedule;

public interface ScheduleRepository {

	Schedule create(Schedule schedule);

	List<Schedule> findAll();

	Optional<Schedule> findById(Long id);

	Schedule update(Long id, Schedule schedule);

	void delete(Long id);
}
