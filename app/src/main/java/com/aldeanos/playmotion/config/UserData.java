package com.aldeanos.playmotion.config;

import android.content.SharedPreferences;

public final class UserData {

    public static final String TOKEN_KEY = "LOGIN_TOKEN";
    private static UserData instance;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private UserData(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.editor = this.sharedPreferences.edit();
    }

    public static UserData getInstance(SharedPreferences sp) {

        if (instance == null)
            instance = new UserData(sp);
        return instance;
    }

    public boolean saveString(String key, String value) {

        editor.putString(key, value);
        return editor.commit();
    }

    public String getString(String key){
        return sharedPreferences.getString(key, null);
    }
}
