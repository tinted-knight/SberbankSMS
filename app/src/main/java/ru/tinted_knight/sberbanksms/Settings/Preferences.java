package ru.tinted_knight.sberbanksms.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    private static String FIRST_RUN = "first_run";

    public static boolean isFirstRun(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(FIRST_RUN, true);
    }

    public static void setFirstRun(Context context, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putBoolean(FIRST_RUN, value)
                .apply();
    }
}
