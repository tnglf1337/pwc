package power.tarif;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public class Tarif {
	@Id
	private Integer id;
	private Double tarifKosten;
	private LocalDate stand;

	public Tarif(Double tarifKosten) {
		this.tarifKosten = tarifKosten;
		this.stand = LocalDate.now();
	}

	public Double getTarifKosten() {
		return tarifKosten;
	}

	public void setTarifKosten(Double tarifKosten) {
		this.tarifKosten = tarifKosten;
	}

	public LocalDate getStand() {
		return stand;
	}

	public void setStand(LocalDate stand) {
		this.stand = stand;
	}

	public double calculateKwhCost(double kwh) {
		return kwh * tarifKosten / 100;
	}

	@Override
	public String toString() {
		return "Tarif{" +
				"id=" + id +
				", tarifKosten=" + tarifKosten +
				", stand=" + stand +
				'}';
	}
}
