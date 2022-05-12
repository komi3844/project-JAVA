package com.mmtr.finance.parser.utils;

public interface ConvertIndicator {
    static Float convertToFloat(String string) {
        if (string.equals("-")) {
            return null;
        } else if (string.contains("B")) {
            return Float.parseFloat(string
                    .replace("B", "")) * 1_000;
        } else {
            return Float.parseFloat(string
                    .replace("M", "")
                    .replace("%", ""));
        }
    }
    static Integer convertToInteger(String string) {
        return string.equals("-")? 0 : Integer.parseInt(string);
    }
}