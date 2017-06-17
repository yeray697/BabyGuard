package com.ncatz.babyguard.preferences;

import android.os.Bundle;
import android.preference.ListPreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.ncatz.babyguard.R;
import com.ncatz.babyguard.components.CustomToolbar;

/**
 * Activity were fragment settings are displayed
 */

public class Settings_Activity extends AppCompatActivity {
    private ListPreference mListPreference;
    private CustomToolbar toolbar;
    private boolean hasBeenModifedSomething;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasBeenModifedSomething = false;
        setContentView(R.layout.activity_settings);
        toolbar = (CustomToolbar) findViewById(R.id.toolbar_settings);
        setToolbar();
        getFragmentManager().beginTransaction().replace(R.id.settings_container,
                new Settings_Fragment()).commit();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean hasBeenModifedSomething() {
        return hasBeenModifedSomething;
    }

    public void setHasBeenModifedSomething(boolean hasBeenModifedSomething) {
        this.hasBeenModifedSomething = hasBeenModifedSomething;
    }
}
