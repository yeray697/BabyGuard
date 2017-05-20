package com.ncatz.babyguard;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class Home_Teacher_Activity extends AppCompatActivity {

    private int selected;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_teacher);
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectItemMenu(item);
                        return true;
                    }
                });
        selectItemMenu(bottomNavigationView.getMenu().getItem(0));
    }
    private void selectItemMenu(MenuItem itemDrawer) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        String tag = "";
        Bundle args;
        switch (itemDrawer.getItemId()) {
            case R.id.item_tracing:
                tag = "tracing";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                }
                selected = 0;
                break;
            case R.id.item_chat:
                tag = "chat";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                    //fragment = Calendar_Fragment.newInstance(args);
                }
                selected = 1;
                break;
            case R.id.item_calendar:
                tag = "calendar";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                    fragment = Calendar_Fragment.newInstance(args);
                }
                selected = 2;
                break;
            case R.id.item_profile:
                tag = "profile";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                    //fragment = Calendar_Fragment.newInstance(args);
                }
                selected = 3;
                break;
            case R.id.item_settings:
                tag = "settings";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                    //fragment = Calendar_Fragment.newInstance(args);
                }
                selected = 4;
                break;
        }
        if (fragment != null) {
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in,
                        R.anim.fade_out)
                        .replace(R.id.frame_layout, fragment,tag)
                        .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selected",selected);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selected = savedInstanceState.getInt("selected",0);
        selectItemMenu(bottomNavigationView.getMenu().getItem(selected));
    }
}
