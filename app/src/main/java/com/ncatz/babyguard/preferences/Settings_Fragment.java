package com.ncatz.babyguard.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
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
import com.ncatz.babyguard.repository.Repository;

import static com.ncatz.babyguard.preferences.SettingsManager.getBooleanPreference;
import static com.ncatz.babyguard.preferences.SettingsManager.getKeyPreferenceByResourceId;
import static com.ncatz.babyguard.preferences.SettingsManager.getStringPreference;

/**
 * Created by yeray697 on 11/06/17.
 */

public class Settings_Fragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private PreferenceScreen imgPref;
    private EditTextPreference nameProfilePref;
    private EditTextPreference phoneProfilePref;
    private PreferenceScreen kidsPref;

    private ListPreference vibrationNotifPref;
    private SwitchPreference previewNotificationPref;

    private Context context;

    private String imgKey, nameKey, passKey, kidKey, phoneKey, notifVibrationKey, previewKey;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ((Settings_Activity)getActivity()).getSupportActionBar().setTitle("Settings");
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
        imgKey = getKeyPreferenceByResourceId(R.string.profile_img_pref);
        nameKey = getKeyPreferenceByResourceId(R.string.profile_name_pref);
        passKey = getKeyPreferenceByResourceId(R.string.profile_password_pref);
        phoneKey = getKeyPreferenceByResourceId(R.string.profile_phone_pref);
        kidKey = getKeyPreferenceByResourceId(R.string.profile_kids_pref);
        notifVibrationKey = getKeyPreferenceByResourceId(R.string.notifications_vibration_pref);
        previewKey = getKeyPreferenceByResourceId(R.string.notifications_preview_pref);

        addPreferencesFromResource(R.xml.settings);

        imgPref = (PreferenceScreen) findPreference(imgKey);
        nameProfilePref = (EditTextPreference) findPreference(nameKey);
        phoneProfilePref = (EditTextPreference) findPreference(phoneKey);
        kidsPref = (PreferenceScreen) findPreference(kidKey);

        vibrationNotifPref = (ListPreference) findPreference(notifVibrationKey);
        previewNotificationPref = (SwitchPreference) findPreference(previewKey);

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

        imgPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                int PICK_IMAGE = 1;
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                return true;
            }
        });

        if (Babyguard_Application.isTeacher()) {
            PreferenceCategory profileCategory = (PreferenceCategory) findPreference("profileCategoryKey");
            profileCategory.removePreference(kidsPref);
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
                Repository.getInstance().getUser().setName(sharedPreferences.getString(key, ""));
                FirebaseManager.getInstance().changeUserName();
                preference.setSummary(sharedPreferences.getString(key, ""));
            } else if (key.equals(phoneKey)) {
                Repository.getInstance().getUser().setPhone_number(sharedPreferences.getString(key, ""));
                FirebaseManager.getInstance().changeUserPhone();
                preference.setSummary(sharedPreferences.getString(key, ""));
            } else {
                preference.setSummary(sharedPreferences.getString(key, ""));
            }
        } else if (preference instanceof SwitchPreference) {

        } else {
            preference.setSummary(sharedPreferences.getString(key, ""));
        }
        ((Settings_Activity)getActivity()).setHasBeenModifedSomething(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}