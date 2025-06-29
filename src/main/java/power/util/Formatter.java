package power.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.Month;
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

	public static String formatSeconds(int seconds) {
		long days = seconds / 86400;
		seconds %= 86400;
		long hours = seconds / 3600;
		seconds %= 3600;
		long minutes = seconds / 60;
		seconds %= 60;

		return String.format("%dd %dh %dm %ds", days, hours, minutes, seconds);
	}

	public static String getCurrentMonthAsString() {
		Month month = LocalDate.now().getMonth();
		String monthName = month.getDisplayName(java.time.format.TextStyle.FULL, Locale.GERMAN);
		return monthName.substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase();
	}

	public static String getMonthAsString(Month month) {
		String monthName = month.getDisplayName(java.time.format.TextStyle.FULL, Locale.GERMAN);
		return monthName.substring(0, 1).toUpperCase() + monthName.substring(1).toLowerCase();
	}
}
