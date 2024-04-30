package org.me.gcu.otuoma_skywatch_weatherapp_s2110913.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import org.me.gcu.otuoma_skywatch_weatherapp_s2110913.R;

public class NightModeUtil {
    private static final String TAG = "NightModeUtil";

    public static void setNightMode(Context context, boolean isDarkMode) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            Log.d(TAG, "Night mode set to Dark Mode");
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Log.d(TAG, "Night mode set to Light Mode");
        }
        Log.d(TAG, "Dark mode settings successful");
        logCurrentNightMode(context); // Log the current night mode
    }

    public static void logCurrentNightMode(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightMode) {
            case Configuration.UI_MODE_NIGHT_YES:
                Log.d(TAG, "Current night mode: Dark Mode");
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                Log.d(TAG, "Current night mode: Light Mode");
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
            default:
                Log.d(TAG, "Current night mode: Undefined");
                break;
        }
    }

    public static boolean isDarkModeEnabled(Context context) {
        int nightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    public static void applyTheme(Activity activity) {
        if (isDarkModeEnabled(activity)) {
            activity.setTheme(R.style.Theme_Otuoma_SkyWatch_WeatherApp_S2110913_Dark);
        } else {
            activity.setTheme(R.style.Base_Theme_Otuoma_SkyWatch_WeatherApp_S2110913);
        }
    }
}
