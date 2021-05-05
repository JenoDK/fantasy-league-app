package com.jeno.fantasyleague.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class DecimalUtil {

	public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

	public static String getTwoDecimalsThousandSeperator(BigDecimal value) {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

		symbols.setGroupingSeparator('.');
		formatter.setDecimalFormatSymbols(symbols);
		return formatter.format(value);
	}

	public static BigDecimal percentage(BigDecimal base, BigDecimal total){
		return base.multiply(ONE_HUNDRED).divide(total, 0, RoundingMode.HALF_UP);
	}

	public static double round(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}

}
