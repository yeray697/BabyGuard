package com.ncatz.babyguard.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ncatz.babyguard.Babyguard_Application;
import com.ncatz.babyguard.R;
import com.ncatz.babyguard.firebase.FirebaseManager;

import static com.ncatz.babyguard.preferences.SettingsManager.getBooleanPreference;
import static com.ncatz.babyguard.preferences.SettingsManager.getKeyPreferenceByResourceId;
import static com.ncatz.babyguard.preferences.SettingsManager.getStringPreference;

/**
 * Created by yeray697 on 11/06/17.
 */

public class Settings_Fragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private EditTextPreference nameProfilePref;
    private EditTextPreference phoneProfilePref;

    private ListPreference vibrationNotifPref;
    private SwitchPreference previewNotificationPref;

    private PreferenceScreen kidsPref;

    private Context context;

    private String nameKey, passKey, kidKey, phoneKey, notifVibrationKey, previewKey;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        //view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context = getContext();
        } else {
            context = getActivity();
        }
        nameKey = getKeyPreferenceByResourceId(R.string.profile_name_pref);
        passKey = getKeyPreferenceByResourceId(R.string.profile_password_pref);
        phoneKey = getKeyPreferenceByResourceId(R.string.profile_phone_pref);
        kidKey = getKeyPreferenceByResourceId(R.string.profile_kids_pref);
        notifVibrationKey = getKeyPreferenceByResourceId(R.string.notifications_vibration_pref);
        previewKey = getKeyPreferenceByResourceId(R.string.notifications_preview_pref);

        addPreferencesFromResource(R.xml.settings);

        nameProfilePref = (EditTextPreference) findPreference(nameKey);
        phoneProfilePref = (EditTextPreference) findPreference(phoneKey);

        vibrationNotifPref = (ListPreference) findPreference(notifVibrationKey);
        previewNotificationPref = (SwitchPreference) findPreference(previewKey);

        kidsPref = (PreferenceScreen) findPreference(kidKey);

        kidsPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                getFragmentManager().beginTransaction()
                        .replace(R.id.settings_container, new KidsSettings_Fragment())
                        .addToBackStack("kidsSettingsFragment")
                        .commit();
                return true;
            }
        });
        nameProfilePref.setSummary(getStringPreference(nameKey, ""));
        phoneProfilePref.setSummary(getStringPreference(phoneKey, ""));
        vibrationNotifPref.setSummary(getResources().getStringArray(R.array.notifications_vibration_pref_entries)[Integer.parseInt(getStringPreference(notifVibrationKey, "0"))]);
        previewNotificationPref.setChecked(getBooleanPreference(previewKey, true));

        if (Babyguard_Application.isTeacher()) {
            getPreferenceScreen().removePreference(kidsPref);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            if (key.equals(notifVibrationKey)) {
                int prefIndex = listPreference.findIndexOfValue(getStringPreference(key, ""));
                if (prefIndex >= 0) {
                    preference.setSummary(listPreference.getEntries()[prefIndex]);
                }
            } else {
                preference.setSummary(sharedPreferences.getString(key, ""));
            }
        } else if (preference instanceof EditTextPreference) {
            if (key.equals(nameKey)) {
                FirebaseManager.getInstance().changeUserName();
                preference.setSummary(sharedPreferences.getString(key, ""));
            } else if (key.equals(phoneKey)) {
                FirebaseManager.getInstance().changeUserPhone();
                preference.setSummary(sharedPreferences.getString(key, ""));
            } else {
                preference.setSummary(sharedPreferences.getString(key, ""));
            }
        } else if (preference instanceof SwitchPreference) {

        } else {
            preference.setSummary(sharedPreferences.getString(key, ""));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}