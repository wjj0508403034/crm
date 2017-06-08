package com.huoyun.core.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {

	public static BigDecimal subtract(BigDecimal num1, BigDecimal num2) {
		if (num1 == null) {
			if (num2 == null) {
				return null;
			}

			return num2.multiply(new BigDecimal(-1));
		}

		if (num2 == null) {
			return num1.multiply(new BigDecimal(1));
		}

		return num1.subtract(num2);
	}
}
