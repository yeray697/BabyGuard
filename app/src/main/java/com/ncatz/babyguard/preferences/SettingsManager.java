package com.ncatz.babyguard.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ncatz.babyguard.Babyguard_Application;
import com.ncatz.babyguard.R;
import com.ncatz.babyguard.model.UserCredentials;

/**
 * Created by yeray697 on 11/06/17.
 */

public class SettingsManager {
    private static Context context = Babyguard_Application.getContext();
    private static SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

    public static void setDefaultValues() {
        String notifVibrationKey = getKeyPreferenceByResourceId(R.string.notifications_vibration_pref);
        String previewKey = getKeyPreferenceByResourceId(R.string.notifications_preview_pref);
        setStringPreference(notifVibrationKey, "0");
        setBooleanPreference(previewKey, true);
    }


    public static void setProfileInfo(UserCredentials userCredentials) {
        String lastUser = getStringPreference(context.getString(R.string.profile_username_pref), "");
        if (lastUser.equals("") || !lastUser.equals(userCredentials.getUser())) { //It's first time logged or session was signed off
            setDefaultValues();
            setStringPreference(getKeyPreferenceByResourceId(R.string.profile_username_pref), userCredentials.getUser());
        }
        setStringPreference(getKeyPreferenceByResourceId(R.string.profile_password_pref), userCredentials.getPass());
        setIntegerPreference(getKeyPreferenceByResourceId(R.string.profile_usertype_pref), userCredentials.getType());
    }

    public static String getKeyPreferenceByResourceId(int resourceId) {
        return context.getString(resourceId);
    }

    public static String getStringPreference(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public static void setStringPreference(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public static int getIntegerPreference(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    public static void setIntegerPreference(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public static boolean getBooleanPreference(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public static void setBooleanPreference(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public static void signOff() {
        preferences.edit().clear().apply();
    }
}
