package power.laufzeit;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;

public interface LaufzeitEventRepository extends CrudRepository<LaufzeitEvent, Integer> {
	@Query("select stamped_at from laufzeit_event order by stamped_at desc limit 1")
	LocalDate getFirstLaufzeit();
}
