package power.tarif;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public class TarifForm {
	@DecimalMin(value = "1.0", message = "Wert muss größer als 1 sein")
	@DecimalMax(value = "100.0", message = "Wert darf maximal 100.0 sein")
	 private Double tarifKosten;

	public TarifForm(Double tarifKosten) {
		this.tarifKosten = tarifKosten;
	}

	public Double getTarifKosten() {
		return tarifKosten;
	}
}
