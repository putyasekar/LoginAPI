package com.diki.idn.loginapi;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
    public static final String SP_STUDENT = "spStudent";
    public static final String SP_EMAIL = "spEmail";
    public static final String SP_SIGNED = "signed";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;

    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_STUDENT, Context.MODE_PRIVATE);
        spEditor = sharedPreferences.edit();
    }

    public void saveBoolean(String keySp, Boolean valueSp) {
        spEditor.putBoolean(keySp, valueSp);
        spEditor.commit();
    }

    public Boolean getSpSigned() {
        return sharedPreferences.getBoolean(SP_SIGNED, false);
    }
}
