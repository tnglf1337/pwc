package power.tarif;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TarifService {
	@Autowired
	private TarifRepository tarifRepository;
	private final Logger log = LoggerFactory.getLogger(TarifService.class);

	public void save(Tarif tarif) {
		tarifRepository.save(tarif);
		log.info("Tarif saved: " + tarif);
	}

	public Tarif getCurrentTarif() {
		return tarifRepository.findAktuellertatrif();
	}
}
