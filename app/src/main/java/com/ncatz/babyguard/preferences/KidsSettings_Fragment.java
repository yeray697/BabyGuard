package com.ncatz.babyguard.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.ncatz.babyguard.firebase.FirebaseManager;
import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.repository.Repository;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;

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

    private Kid kidActivityResult;
    private static final int PICK_IMAGE_REQUEST_CODE = 1;

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
            category.setKey("category_" + kid.getId());
            screen.addPreference(category);
            image = getPreferenceManager().createPreferenceScreen(context);
            image.setTitle("Change the photo");
            image.setKey(kid.getId());
            loadImage(image,kid.getImg());
            image.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    kidActivityResult = kid;
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_CODE);
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
            if (requestCode == PICK_IMAGE_REQUEST_CODE) {
                final PreferenceScreen imgPref = (PreferenceScreen) findPreference(kidActivityResult.getId());
                Uri selectedImage = imageReturnedIntent.getData();
                FirebaseManager.getInstance().uploadImageToFirebase(selectedImage, true, kidActivityResult.getId(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error while loading the image", Toast.LENGTH_SHORT).show();
                    }
                }, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests") String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                        kidActivityResult.setImg(downloadUrl);
                        loadImage(imgPref,downloadUrl);
                    }
                });
            }
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

    private void loadImage(final PreferenceScreen pref, final String url) {
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(context).asBitmap().load(url)
                            .submit(200, 200).get(); // Width and height;

                } catch (final ExecutionException e) {
                    e.getMessage();
                } catch (final InterruptedException e) {
                    e.getMessage();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (null != bitmap) {
                    Drawable drawable = new BitmapDrawable(getResources(),bitmap);
                    pref.setIcon(drawable);
                }
            }
        }.execute();
    }
}