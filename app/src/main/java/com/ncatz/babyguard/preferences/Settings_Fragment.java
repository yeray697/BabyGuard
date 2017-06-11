package com.ncatz.babyguard.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ncatz.babyguard.R;

import static com.ncatz.babyguard.preferences.SettingsManager.getBooleanPreference;
import static com.ncatz.babyguard.preferences.SettingsManager.getIntegerPreference;
import static com.ncatz.babyguard.preferences.SettingsManager.getKeyPreferenceByResourceId;
import static com.ncatz.babyguard.preferences.SettingsManager.getStringPreference;

/**
 * Created by yeray697 on 11/06/17.
 */

public class Settings_Fragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    private SharedPreferences sharedPreferences;

    private EditTextPreference nameProfilePref;
    private EditTextPreference phoneProfilePref;
    private PreferenceScreen passProfilePref;

    private ListPreference vibrationNotifPref;
    private SwitchPreference previewNotificationPref;


    private String nameKey, passKey, phoneKey, notifVibrationKey, previewKey;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        nameKey = getKeyPreferenceByResourceId(R.string.profile_name_pref);
        passKey = getKeyPreferenceByResourceId(R.string.profile_password_pref);
        phoneKey = getKeyPreferenceByResourceId(R.string.profile_phone_pref);
        notifVibrationKey = getKeyPreferenceByResourceId(R.string.notifications_vibration_pref);
        previewKey = getKeyPreferenceByResourceId(R.string.notifications_preview_pref);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        nameProfilePref = (EditTextPreference) findPreference(nameKey);
        passProfilePref = (PreferenceScreen) findPreference(passKey);
        phoneProfilePref = (EditTextPreference) findPreference(phoneKey);

        vibrationNotifPref = (ListPreference) findPreference(notifVibrationKey);
        previewNotificationPref = (SwitchPreference) findPreference(previewKey);

        nameProfilePref.setSummary(getStringPreference(nameKey,""));
        phoneProfilePref.setSummary(getStringPreference(phoneKey,""));
        try {
            vibrationNotifPref.setSummary(getResources().getStringArray(R.array.notifications_vibration_pref_entries)[getIntegerPreference(notifVibrationKey, 0)]);
        } catch (Exception e) {
            Log.d("","");
        }
        previewNotificationPref.setChecked(getBooleanPreference(previewKey,true));

        passProfilePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                return true;
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        //unregister the preferenceChange listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        /*Preference preference = findPreference(key);
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(sharedPreferences.getString(key, ""));
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            preference.setSummary(sharedPreferences.getString(key, ""));

        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregister the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
