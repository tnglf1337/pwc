package power.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Formatter {


	public static String format(double value) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
		DecimalFormat df = new DecimalFormat("0.00", symbols);
		return df.format(value);
	}

	public static String convertLocalDateDE(LocalDate localDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy", Locale.GERMAN);
		return localDate.format(formatter);
	}

}
