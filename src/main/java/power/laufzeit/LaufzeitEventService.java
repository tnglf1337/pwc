package power.laufzeit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import power.DetailsDto;
import power.calc.ConsumptionCalculator;
import power.tarif.Tarif;
import power.tarif.TarifRepository;
import power.util.Formatter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import static power.util.Formatter.*;

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

	public int getLaufzeitSekundenByDate(LocalDate date) {
		List<LaufzeitEvent> events = laufzeitEventRepository.findByStampedAt(date);
		if (events.isEmpty()) return 0;
		return getLaufzeitSekunden(events);
	}

	public int getLaufzeitSekundenByMonth(Month month) {
		List<LaufzeitEvent> events = laufzeitEventRepository.findAll()
				.stream()
				.filter( e -> e.stampedAt().getMonth() == month)
				.toList();
		if (events.isEmpty()) return 0;
		return getLaufzeitSekunden(events);
	}

	private int getLaufzeitSekunden(List<LaufzeitEvent> events) {
		if(events.size() == 1) return events.get(0).laufzeit();

		int min = events.get(0).laufzeit();
		int max = events.get(0).laufzeit();

		for (LaufzeitEvent event : events) {
			int currentLaufzeit = event.laufzeit();
			if (currentLaufzeit < min) min = event.laufzeit();
			if (currentLaufzeit > max) max = currentLaufzeit;
		}
		return max - min;
	}

	public List<DetailsDto> computeTableDetailsThisMonth(int monthValue) {
		List<DetailsDto> l = new LinkedList<>();
		Tarif currTarif = tarifRepository.findAktuellertatrif();

		YearMonth yearMonth = YearMonth.of(2025, monthValue);
		int daysInMonth = yearMonth.lengthOfMonth();

		for (int i = 1; i <= daysInMonth; i++) {
			LocalDate currentDate = LocalDate.of(2025, monthValue, i);

			int laufzeit = getLaufzeitSekundenByDate(currentDate);
			if (laufzeit == 0) continue;

			double kwh = calculator.kiloWattProStunde((double) laufzeit / 3600);
			double kosten = currTarif.calculateKwhCost(kwh);

			DetailsDto detail = new DetailsDto(
					convertLocalDateDE(currentDate),
					formatSeconds(laufzeit),
					format(kwh),
					format(kosten)
			);

			l.add(detail);
		}

		Collections.reverse(l);

		return l;
	}


	public List<DetailsDto> computeTableDetailsMonthly() {
		List<DetailsDto> l = new LinkedList<>();
		Tarif currTarif = tarifRepository.findAktuellertatrif();
		Month[] months = Month.values();

		for(int i = 1; i <= 12; i++) {
			Month month = months[i-1];
			String currentMonth = Formatter.getMonthAsString(month);
			int laufzeit = getLaufzeitSekundenByMonth(month);
			if(laufzeit == 0) continue;
			double kwh = calculator.kiloWattProStunde((double) laufzeit / 3600);
			double kosten = currTarif.calculateKwhCost(kwh);

			DetailsDto detail = new DetailsDto(currentMonth, formatSeconds(laufzeit), format(kwh), format(kosten));
			l.add(detail);
		}

		Collections.reverse(l);

		return l;
	}
}
