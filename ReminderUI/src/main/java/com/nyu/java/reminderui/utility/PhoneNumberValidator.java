package com.nyu.java.reminderui.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator {
    private static final String PHONE_REGEX = "\\+1[2-9]\\d{2}[2-9]\\d{2}\\d{4}";

    public static boolean isValidUSPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_REGEX);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
