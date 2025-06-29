package power.tarif;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TarifUpdateService {

	private final Logger log = LoggerFactory.getLogger(TarifUpdateService.class);
	private final TarifService tarifService;
	private final ProviderService providerService;

	public TarifUpdateService(TarifService tarifService, ProviderService providerService) {
		this.tarifService = tarifService;
		this.providerService = providerService;
	}

	public boolean compareTarifs() {
		try {
			double currentTarif = tarifService.getCurrentTarif().getTarifKosten();
			double providerTarif = providerService.fetchRecentTarif();

			if(providerTarif > currentTarif) {
				tarifService.save(new Tarif(providerTarif));
				return true;
			} else {
				return false;
			}
		} catch (NoSuchElementException | InterruptedException | StaleElementReferenceException e) {
			log.error(e.getMessage());
			return false;
		}
	}
}