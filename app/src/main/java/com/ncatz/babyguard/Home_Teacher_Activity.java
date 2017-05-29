package com.ncatz.babyguard;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ncatz.babyguard.adapter.SpinnerKidsTeacher_Adapter;
import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.model.NurseryClass;
import com.ncatz.babyguard.repository.Repository;

import java.util.Collections;

public class Home_Teacher_Activity extends AppCompatActivity {

    private int selected;
    private String selectedClassId;

    private OnSelectedClassIdChangedListener selectedClassIdChangedListener;

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
                    args.putString(Conversations_Fragment.ID_KEY, Repository.getInstance().getUser().getId());
                    fragment = Conversations_Fragment.newInstance(args);
                }
                selected = 1;
                break;
            case R.id.item_calendar:
                tag = "calendar";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                    args.putString(Calendar_Fragment.ID_KEY,selectedClassId);
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
                        //.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        getMenuInflater().inflate(R.menu.menu_teacher, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        final Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        SpinnerKidsTeacher_Adapter adapter = new SpinnerKidsTeacher_Adapter(this,Repository.getInstance().getNurserySchool().getNurseryClassesList());
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSelectedClassId(((NurseryClass) spinner.getSelectedItem()).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setSelectedClassId(((NurseryClass) spinner.getSelectedItem()).getId());
        return true;
    }

    public void setSelectedClassId(String selectedClassId) {
        this.selectedClassId = selectedClassId;
        if (selectedClassIdChangedListener != null)
            selectedClassIdChangedListener.selectedClassIdChanged(selectedClassId);

    }

    public void setSelectedClassIdChangedListener (OnSelectedClassIdChangedListener selectedClassIdChangedListener) {
        this.selectedClassIdChangedListener = selectedClassIdChangedListener;
    }

    public interface OnSelectedClassIdChangedListener {
        void selectedClassIdChanged(String newId);
    }
}
