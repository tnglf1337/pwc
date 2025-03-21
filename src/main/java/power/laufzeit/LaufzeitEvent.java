package power.laufzeit;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record LaufzeitEvent(
		@Id Integer id,
		Integer laufzeit,
		LocalDateTime stampedAt,
		double tarifKosten,
		double kwh
) {
}
