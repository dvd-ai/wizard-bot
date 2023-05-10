package com.learngerman.wizardbot.util;

public class NumberUtil {

    private NumberUtil() {
    }

    public static boolean isPositiveRealNumber(String str) {

        if (
                str != null && !str.isEmpty() &&
                str.matches("^(?:[1-9]\\d*(?:\\.\\d+)?|0?\\.\\d*[1-9]\\d*)$")
        ) {
            try {
                float number = Float.parseFloat(str);
                return number > 0;
            } catch (NumberFormatException e) {
                // Ignore the exception, as it will be treated as not a positive number
            }
        }
        return false;
    }
}
