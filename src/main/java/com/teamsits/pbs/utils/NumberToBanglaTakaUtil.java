package com.teamsits.pbs.utils;

import java.text.DecimalFormat;

public class NumberToBanglaTakaUtil {
    public static final String[] units = new String[]{"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
    public static final String[] tens = new String[]{"", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};

    public static String convert(double n) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String numberAsString = decimalFormat.format(n);
        String[] convert = numberAsString.split("\\.");
        long taka = Long.parseLong(convert[0]);
        int paisa = Integer.parseInt(convert[1]);
        NumberToBanglaTakaUtil numberToBanglaTaka = new NumberToBanglaTakaUtil();
        String totalTaka = "";
        if (taka != 0L) {
            totalTaka = numberToBanglaTaka.convertTaka(taka) + " Taka ";
        }

        if (taka == 0L && n < 0.0D) {
            totalTaka = "Minus ";
        }

        if (paisa > 0) {
            totalTaka = totalTaka + numberToBanglaTaka.convertPaisa(paisa) + " Paisa ";
        }

        totalTaka = totalTaka + "Only";
        return totalTaka;
    }

    public String convertTaka(Long n) {
        if (n < 0L) {
            return "Minus " + this.convertTaka(Math.abs(n));
        } else {
            int a;
            if (n < 20L) {
                a = Integer.parseInt(n.toString());
                return units[a];
            } else if (n < 100L) {
                a = Integer.parseInt(n.toString());
                return tens[a / 10] + (n % 10L != 0L ? " " : "") + units[a % 10];
            } else if (n < 1000L) {
                a = Integer.parseInt(n.toString());
                return units[a / 100] + " Hundred" + (a % 100 != 0 ? " " : "") + this.convertTaka(n % 100L);
            } else if (n < 100000L) {
                return this.convertTaka(n / 1000L) + " Thousand" + (n % 10000L != 0L ? " " : "") + this.convertTaka(n % 1000L);
            } else {
                return n < 10000000L ? this.convertTaka(n / 100000L) + " Lakh" + (n % 100000L != 0L ? " " : "") + this.convertTaka(n % 100000L) : this.convertTaka(n / 10000000L) + " Crore" + (n % 10000000L != 0L ? " " : "") + this.convertTaka(n % 10000000L);
            }
        }
    }

    public String convertPaisa(int n) {
        if (n < 20) {
            return units[n];
        } else {
            return n < 100 ? tens[n / 10] + (n % 10 != 0 ? " " : "") + units[n % 10] : "";
        }
    }

}
