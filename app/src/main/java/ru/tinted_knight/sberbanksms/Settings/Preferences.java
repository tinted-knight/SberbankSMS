package ru.tinted_knight.sberbanksms.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ru.tinted_knight.sberbanksms.Tools.Constants;

import static ru.tinted_knight.sberbanksms.Tools.Constants.AppPreferences;

public class Preferences {

    private static String FIRST_RUN = "first_run";

    public static boolean isFirstRun(Context context, String name) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getBoolean(Constants.AppPreferences.FirstRun, true);
    }

    public static void setFirtsRun(Context context, String name, boolean value) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(AppPreferences.FirstRun, value)
                .apply();
    }

    public static boolean isFirstRun2(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(FIRST_RUN, true);
    }

    public static void setFirstRun2(Context context, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putBoolean(FIRST_RUN, value)
                .apply();
    }
}
