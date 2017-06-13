package com.ncatz.babyguard.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ncatz.babyguard.R;
import com.ncatz.babyguard.firebase.FirebaseManager;
import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.repository.Repository;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.ncatz.babyguard.preferences.SettingsManager.getBooleanPreference;
import static com.ncatz.babyguard.preferences.SettingsManager.getKeyPreferenceByResourceId;
import static com.ncatz.babyguard.preferences.SettingsManager.getStringPreference;

/**
 * Created by yeray697 on 11/06/17.
 */

public class KidsSettings_Fragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SharedPreferences sharedPreferences;

    private EditTextPreference nameProfilePref;
    private EditTextPreference phoneProfilePref;

    private ListPreference vibrationNotifPref;
    private SwitchPreference previewNotificationPref;

    private PreferenceScreen kidsPref;

    private Context context;

    private String nameKey, passKey, kidKey, phoneKey, notifVibrationKey, previewKey;

    private ArrayList<Kid> kids;

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

        ((Settings_Activity)getActivity()).getSupportActionBar().setTitle("Kids");
        kids = (ArrayList<Kid>) Repository.getInstance().getKids();
        PreferenceScreen screen;
        PreferenceCategory category;
        PreferenceScreen image;
        EditTextPreference etpName;
        EditTextPreference etpInfo;

        screen = getPreferenceManager().createPreferenceScreen(context);

        for (final Kid kid : kids) {
            category = new PreferenceCategory(context);
            category.setTitle(kid.getName());
            screen.addPreference(category);
            image = getPreferenceManager().createPreferenceScreen(context);
            image.setTitle("Change the photo");
            image.setIcon(R.drawable.food);
            image.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    int PICK_IMAGE = 1;
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                    return true;
                }
            });
            category.addPreference(image);

            etpName = new EditTextPreference(context);
            etpName.setTitle("Change the name");
            final PreferenceCategory finalCategory = category;
            etpName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    finalCategory.setTitle(newValue.toString());
                    kid.setName(newValue.toString());
                    FirebaseManager.getInstance().changeKidName(kid.getId(),newValue.toString());
                    return false;
                }
            });
            category.addPreference(etpName);

            etpInfo = new EditTextPreference(context);
            etpInfo.setTitle("Change the information");
            etpInfo.setSummary(kid.getInfo());
            etpInfo.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    preference.setSummary(newValue.toString());
                    kid.setInfo(newValue.toString());
                    FirebaseManager.getInstance().changeKidInfo(kid.getId(),newValue.toString());
                    return false;
                }
            });
            category.addPreference(etpInfo);
        }

        setPreferenceScreen(screen);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (resultCode == RESULT_OK) {
            Uri selectedImage = imageReturnedIntent.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            /*Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();


            Log.d(LOG_TAG, "Data Recieved! " + filePath);*/

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        ((Settings_Activity)getActivity()).setHasBeenModifedSomething(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}