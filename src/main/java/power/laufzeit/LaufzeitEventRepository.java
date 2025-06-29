package power.laufzeit;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import java.time.LocalDate;
import java.util.List;

public interface LaufzeitEventRepository extends CrudRepository<LaufzeitEvent, Integer> {

	@NonNull
	List<LaufzeitEvent> findAll();

	@Query("select stamped_at from laufzeit_event order by stamped_at limit 1")
	LocalDate getFirstLaufzeit();

	@Query("SELECT * FROM laufzeit_event WHERE stamped_at::date = :date")
	List<LaufzeitEvent> findByStampedAt(@Param("date") LocalDate date);
}
