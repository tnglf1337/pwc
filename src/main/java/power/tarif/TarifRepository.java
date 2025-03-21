package power.tarif;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface TarifRepository extends CrudRepository<Tarif, Integer> {

	@Query("select * from tarif order by stand desc limit 1")
	Tarif findAktuellertatrif();
}
