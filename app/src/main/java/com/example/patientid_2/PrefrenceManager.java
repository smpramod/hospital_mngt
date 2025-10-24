package com.example.patientid_2;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefrenceManager {
    SharedPreferences.Editor prefEditor;

    // Shared preferences file name
    public static final String PREF_NAME = "MISCOS_DQM";

    SharedPreferences pref;
    //Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;
    public static int PRIVATE_MODE = 0;
    private String contactPersonNo;
    private String agencyCreatedOn;

    public PrefrenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        // getSharedPreferences();
    }

    SharedPreferences.Editor getEditor() {
        return pref.edit();
    }

    public void setPreference(String key, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(key, value);
        editor.apply();

    }


    public void setUsername(String firstName) {
        editor.putString("firstName", firstName);
        editor.commit();
    }

    public String getUsername(String key) {

            return pref.getString(key, "firstName");

    }

    public void setPassword(String password) {
        editor.putString("firstName", password);
        editor.commit();
    }

    public String getPassword(String key) {

        return pref.getString(key, "password");

    }
}
