package power.laufzeit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import power.calc.ConsumptionCalculator;
import power.tarif.Tarif;
import power.tarif.TarifRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LaufzeitEventService {

	private final LaufzeitFileManager laufzeitFileManager;
	private final LaufzeitEventRepository laufzeitEventRepository;
	private final TarifRepository tarifRepository;
	private final Logger log = LoggerFactory.getLogger(LaufzeitEventService.class);
	private final ConsumptionCalculator calculator;

	public LaufzeitEventService(LaufzeitFileManager laufzeitFileManager, LaufzeitEventRepository laufzeitEventRepository, TarifRepository tarifRepository, ConsumptionCalculator calculator) {
		this.laufzeitFileManager = laufzeitFileManager;
		this.laufzeitEventRepository = laufzeitEventRepository;
		this.tarifRepository = tarifRepository;
		this.calculator = calculator;
	}

	@Scheduled(fixedRate = 600000)
	public void stampEvent() throws IOException {
		Tarif aktuellerTarif = tarifRepository.findAktuellertatrif();
		int laufzeit = laufzeitFileManager.readNumber();
		double kwh = calculator.kiloWattProStunde(toStunden(laufzeit));
		LaufzeitEvent event = new LaufzeitEvent(null, laufzeit, LocalDateTime.now(), aktuellerTarif.getTarifKosten(), kwh);
		LaufzeitEvent savedEvent = laufzeitEventRepository.save(event);
		log.info("LaufzeitEvent saved: " + savedEvent);
	}

	public double toStunden(int seconds) {
		return ((double) seconds / (60 * 60));
	}

	public LocalDate getFirstEvent() {
		return laufzeitEventRepository.getFirstLaufzeit();
	}

	public List<LaufzeitEvent> findByStampedAt(LocalDate date) {
		return laufzeitEventRepository.findByStampedAt(date);
	}

	public int getLaufzeitSekundenbyDate(LocalDate date) {
		List<LaufzeitEvent> events = laufzeitEventRepository.findByStampedAt(date);

		int min = events.get(0).laufzeit();
		int max = events.get(0).laufzeit();

		for (LaufzeitEvent event : events) {
			int currentLaufzeit = event.laufzeit();
			if (currentLaufzeit < min) min = event.laufzeit();
			if (currentLaufzeit > max) max = currentLaufzeit;

		}

		System.out.println("min: " + min);
		System.out.println("max: " + max);

		return max - min;

	}
}
