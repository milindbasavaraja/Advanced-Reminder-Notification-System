package com.nyu.java.reminderui.config;

import java.util.prefs.Preferences;

public class TokenStorage {
    private static final String JWT_KEY = "jwtToken";

    private static Preferences prefs = Preferences.userNodeForPackage(TokenStorage.class);

    public static void storeToken(String token) {
        prefs.put(JWT_KEY, token);
    }

    public static String getToken() {
        return prefs.get(JWT_KEY, "");
    }

    public static void clearToken() {
        prefs.remove(JWT_KEY);
    }
}
