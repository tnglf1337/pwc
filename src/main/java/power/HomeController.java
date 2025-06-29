package power;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import power.calc.ConsumptionCalculator;
import power.laufzeit.LaufzeitEventService;
import power.laufzeit.LaufzeitFileManager;
import power.tarif.Tarif;
import power.tarif.TarifForm;
import power.tarif.TarifService;
import power.util.Formatter;
import java.io.IOException;
import java.time.LocalDate;
import static power.util.Formatter.*;

@Controller
public class HomeController {

	private final LaufzeitEventService laufzeitEventService;
	private final TarifService tarifService;
	private final LaufzeitFileManager laufzeitFileManager;
	private final ConsumptionCalculator calculator;

	public HomeController(LaufzeitEventService laufzeitEventService, TarifService tarifService, LaufzeitFileManager laufzeitFileManager, ConsumptionCalculator calculator) {
		this.laufzeitEventService = laufzeitEventService;
		this.tarifService = tarifService;
		this.laufzeitFileManager = laufzeitFileManager;
		this.calculator = calculator;
	}

	@GetMapping("/")
	public String home(TarifForm tarifForm, Model model) throws IOException {
		LocalDate firstEventDate = laufzeitEventService.getFirstEvent();
		Tarif currentTarif = tarifService.getCurrentTarif();
		double currentKwh = calculator.kiloWattProStunde((double) laufzeitFileManager.readNumber() / (60*60));
		int averageWatt = (int) calculator.averageWattConsumption();
		double currentCost = currentTarif.calculateKwhCost(currentKwh);

		model.addAttribute("currentTarif", currentTarif.getTarifKosten());
		model.addAttribute("firstEventDate", convertLocalDateDE(firstEventDate));
		model.addAttribute("currentKwh", format(currentKwh));
		model.addAttribute("averageWatt", averageWatt);
		model.addAttribute("currentCost",format(currentCost));

		return "home";
	}

	@GetMapping("/details")
	public String detail(Model model) {
		model.addAttribute("thisMonth", Formatter.getCurrentMonthAsString());
		model.addAttribute("monthDetails",laufzeitEventService.computeTableDetailsThisMonth(LocalDate.now().getMonthValue()));
		model.addAttribute("monthsDetails",laufzeitEventService.computeTableDetailsMonthly());
		return "details";
	}
}
