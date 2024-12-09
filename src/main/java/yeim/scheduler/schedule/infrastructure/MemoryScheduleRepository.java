package yeim.scheduler.schedule.infrastructure;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;
import yeim.scheduler.common.PageResponse;
import yeim.scheduler.schedule.domain.Schedule;

@Repository
public class MemoryScheduleRepository implements ScheduleRepository {

	private final ConcurrentHashMap<Long, Schedule> store = new ConcurrentHashMap<>();
	private final AtomicLong sequence = new AtomicLong(0);

	@Override
	public Schedule create(Schedule schedule) {
		schedule.setId(sequence.incrementAndGet());
		store.put(schedule.getId(), schedule);

		return schedule;
	}

	@Override
	public PageResponse<Schedule> findAll(int page, int size) {
		int start = page * size;
		List<Schedule> schedules = store.values().stream()
			.sorted(Comparator.comparing(Schedule::getUpdatedAt).reversed())
			.skip(start)
			.limit(size)
			.toList();
		return new PageResponse<>(schedules, page, size, store.size());
	}

	@Override
	public Optional<Schedule> findById(Long id) {
		return Optional.ofNullable(store.get(id));
	}

	@Override
	public Schedule update(Long id, Schedule schedule) {
		schedule.setId(id);
		store.put(id, schedule);
		return schedule;
	}

	@Override
	public void delete(Long id) {
		store.remove(id);
	}
}
