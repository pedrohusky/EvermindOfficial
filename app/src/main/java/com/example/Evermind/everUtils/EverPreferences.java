package com.example.Evermind.everUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class EverPreferences {

    private final Context context;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public EverPreferences(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("DeleteNoteID", MODE_PRIVATE);
        editor = prefs.edit();
    }

    public int getInt(String name, int defaultValue) {
       return prefs.getInt(name, defaultValue);
    }

    public boolean getBoolean(String name, boolean defaultValue) {
        return prefs.getBoolean(name, defaultValue);
    }

    public String getString(String name, String defaultValue) {
        return prefs.getString(name, defaultValue);
    }

    public void putString(String name, String value) {
        editor.putString(name, value);
        editor.apply();
    }

    public void putInt(String name, int value) {
        editor.putInt(name, value);
        editor.apply();
    }

    public void putBoolean(String name, boolean value) {
        editor.putBoolean(name, value);
        editor.apply();
    }

}
