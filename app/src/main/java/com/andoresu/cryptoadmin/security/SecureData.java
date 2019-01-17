package com.andoresu.cryptoadmin.security;

import android.annotation.SuppressLint;

import de.adorsys.android.securestoragelibrary.SecurePreferences;

@SuppressLint("LogNotTimber")
public class SecureData {

    private final static String TAG = "CRYPTO_" + SecureData.class.getSimpleName();

    private final static String TOKEN_KEY = "SECURE_DATA_TOKEN_KEY";

    public static void saveToken(String token) {
        if (SecurePreferences.contains(TOKEN_KEY)) {
            SecurePreferences.removeValue(TOKEN_KEY);
        }
        SecurePreferences.setValue(TOKEN_KEY, token);
    }


    public static String getToken() {
        if (!SecurePreferences.contains(TOKEN_KEY)) {
            return null;
        }
        return SecurePreferences.getStringValue(TOKEN_KEY, null);
    }

    public static void removeToken() {
        SecurePreferences.removeValue(TOKEN_KEY);
    }
}