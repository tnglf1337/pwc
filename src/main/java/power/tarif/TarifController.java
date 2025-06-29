package power.tarif;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;

@Controller
public class TarifController {
	 private final TarifRepository tarifRepository;
	 private final TarifService tarifService;
	 private final TarifUpdateService tarifUpdateService;

	public TarifController(TarifRepository tarifRepository, TarifService tarifService, TarifUpdateService tarifUpdateService) {
		this.tarifRepository = tarifRepository;
		this.tarifService = tarifService;
		this.tarifUpdateService = tarifUpdateService;
	}

	@GetMapping("/tarif")
	public String home(TarifForm tarifForm) throws IOException {
		return "tarif";
	}

	@PostMapping("/new-tarif")
	public String newTarif(@Valid TarifForm tarifForm,
	                       BindingResult bindingResult,
	                       RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) return "tarif";
		redirectAttributes.addFlashAttribute("tarifForm", tarifForm);
		tarifService.save(new Tarif(tarifForm.getTarifKosten()));
		return "redirect:/";
	}

	@GetMapping("/check-tarif")
	public String checkTarif(RedirectAttributes redirectAttributes) {
		boolean updated = tarifUpdateService.compareTarifs();

		if(updated) {
			redirectAttributes.addFlashAttribute("fetchResponse", "Tarif wurde aktualisiert.");
			redirectAttributes.addFlashAttribute("newTarif", true);
		} else {
			redirectAttributes.addFlashAttribute("fetchResponse", "Kein neuer Tarif vorhanden.");
			redirectAttributes.addFlashAttribute("newTarif", false);
		}
		return "redirect:/";
	}
}
