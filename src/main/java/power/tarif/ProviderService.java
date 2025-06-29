package power.tarif;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class ProviderService {

	@Value("${provider.url}")
	private String PROVIDER_URL;

	public double fetchRecentTarif() throws InterruptedException, NoSuchElementException, StaleElementReferenceException {
		WebDriver driver = new FirefoxDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.get(PROVIDER_URL);

		// Cookies akzeptieren
		driver.findElement(By.xpath("//button[contains(text(),'Alle akzeptieren')]")).click();

		// Formular ausfüllen
		driver.findElement(By.cssSelector("label[for='privatkunde_Strom']")).click();
		WebElement verbrauchFeld = driver.findElement(By.id("Verbrauch_Strom"));
		verbrauchFeld.clear();
		verbrauchFeld.sendKeys("1500"); // Einzelperson mean-kwh pro Jahr
		driver.findElement(By.id("PLZ_Eingabe_Strom")).sendKeys("41541");
		Thread.sleep(1000);
		driver.findElement(By.className("tr-submit")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("+ Preisübersicht")));

		// Preisübersicht öffnet sich
		driver.findElement(By.linkText("+ Preisübersicht")).click();
		List<WebElement> preis = driver.findElements(By.className("arbeitspreis"));
		double preisParsed = parsePreis(preis.get(0).getText());
		driver.close();

		return preisParsed;
	}

	public double parsePreis(String raw) {
		String numberOnly = raw.split(" ")[0];
		String normalized = numberOnly.replace(',', '.');
		return Double.parseDouble(normalized);
	}
}
