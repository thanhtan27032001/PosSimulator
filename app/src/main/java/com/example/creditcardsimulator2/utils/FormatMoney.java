package com.example.creditcardsimulator2.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatMoney {
    public static String format(double amount){
        Locale usLocale = new Locale("en", "US");
        NumberFormat usFormat = NumberFormat.getCurrencyInstance(usLocale);
        return usFormat.format(amount);
    }
}
