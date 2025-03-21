package power.calc;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConsumptionCalculator {

	@Value("${verbrauch.watt.normal}")
	private int verbrauchWattNormal;

	@Value("${verbrauch.gewicht.normal}")
	private double gewichtNormal;

	@Value("${verbrauch.watt.last}")
	private int verbrauchWattLast;

	@Value("${verbrauch.gewicht.last}")
	private double gewichtLast;

	public ConsumptionCalculator() {
	}

	public double averageWattConsumption() {
		return ((verbrauchWattNormal * gewichtNormal)
				+ (verbrauchWattLast * gewichtLast))
				/ (gewichtNormal + gewichtLast);
	}

	public double kiloWattProStunde(double stunde) {
		double averageWatt = averageWattConsumption();
		return averageWatt * stunde / 1000;
	}
}
