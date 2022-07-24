package com.crypto.cryptopricechecker.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValidator {

    private static final String tickerRegex = "[A-Z0-9]{0,5}(?:List)?";

    public static boolean isValidTicker(String ticker) {
        Pattern pattern = Pattern.compile(tickerRegex);

        Matcher matcher = pattern.matcher(ticker);
        return matcher.matches();
    }

    // TODO: add regex for password and username validations

}
