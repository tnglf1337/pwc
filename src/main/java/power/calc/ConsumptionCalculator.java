package power.calc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConsumptionCalculator {

	private final double EPSILON = 0.6;

	@Value("${verbrauch.cpu.min}")
	private int cpu_min;

	@Value("${verbrauch.cpu.max}")
	private int cpu_max;

	@Value("${verbrauch.gpu.min}")
	private int gpu_min;

	@Value("${verbrauch.gpu.max}")
	private int gpu_max;

	@Value("${verbrauch.aio}")
	private int aio;

	@Value("${verbrauch.monitor}")
	private int monitor;

	private double getMinWattConsumption() {
		return cpu_min + gpu_min + aio + monitor;
	}

	private double getMaxWattConsumption() {
		return cpu_max + gpu_max + aio + monitor;
	}

	public double getWattConsumption() {
		return EPSILON * getMinWattConsumption() + (1-EPSILON) * getMaxWattConsumption();
	}

	public double kiloWattProStunde(double stunde) {
		return getWattConsumption() * stunde / 1000;
	}
}