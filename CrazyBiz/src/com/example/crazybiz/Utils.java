package com.example.crazybiz;

import java.math.BigDecimal;

public class Utils {

	public static BigDecimal parsePrice(String string) {
		string = string.replaceAll(",", ".");
		return BigDecimal.valueOf(Double.parseDouble(string));
	}

}
