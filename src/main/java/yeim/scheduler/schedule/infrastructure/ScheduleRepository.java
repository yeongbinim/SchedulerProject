package yeim.scheduler.schedule.infrastructure;

import java.util.Optional;
import yeim.scheduler.common.PageResponse;
import yeim.scheduler.schedule.domain.Schedule;

public interface ScheduleRepository {

	Schedule create(Schedule schedule);

	PageResponse<Schedule> findAll(int page, int size);

	Optional<Schedule> findById(Long id);

	Schedule update(Long id, Schedule schedule);

	void delete(Long id);
}
