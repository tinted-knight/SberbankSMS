package ru.tinted_knight.sberbanksms.Settings;

import android.content.Context;
import android.content.SharedPreferences;

import ru.tinted_knight.sberbanksms.Tools.Constants;

import static ru.tinted_knight.sberbanksms.Tools.Constants.AppPreferences;

public class Preferences {

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

}
