package power;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import power.laufzeit.LaufzeitFileManager;

@EnableScheduling
@SpringBootApplication
public class PowerconsumptionApplication {
	public static void main(String[] args) {
		SpringApplication.run(PowerconsumptionApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(LaufzeitFileManager laufzeitFileManager) {
		return args -> {
			int seconds = laufzeitFileManager.readNumber();
			while (true) {
				seconds++;
				laufzeitFileManager.updateNumber(seconds);
				Thread.sleep(1000);
			}
		};
	}
}