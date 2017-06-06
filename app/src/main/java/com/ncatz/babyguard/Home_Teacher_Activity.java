package com.ncatz.babyguard;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.ncatz.babyguard.adapter.SpinnerKidsTeacher_Adapter;
import com.ncatz.babyguard.components.CustomToolbar;
import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.model.NurseryClass;
import com.ncatz.babyguard.model.NurserySchool;
import com.ncatz.babyguard.repository.Repository;

import java.util.Collections;

public class Home_Teacher_Activity extends AppCompatActivity {

    private int selected;
    private String selectedClassId;
    private Toolbar toolbar;
    private Spinner spinner;
    private SpinnerKidsTeacher_Adapter adapter;

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
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                //Empty to prevent double click on tab item
            }
        });
        selectItemMenu(bottomNavigationView.getMenu().getItem(0));
        setToolbar();
        ((Babyguard_Application)getApplicationContext()).addHomeTeacherListener(new Babyguard_Application.ActionEndListener() {
            @Override
            public void onEnd() {
                refreshSpinner();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        ((Babyguard_Application)getApplicationContext()).removeHomeTeacherListener();
    }

    public void refreshSpinner() {
        NurserySchool school = Repository.getInstance().getNurserySchool();
        if (school != null) {
            adapter = new SpinnerKidsTeacher_Adapter(this,school.getNurseryClassesList());
            if (spinner != null) {
                spinner.setAdapter(adapter);
                NurseryClass aux = (NurseryClass) spinner.getSelectedItem();
                if (aux != null)
                    setSelectedClassId(aux.getId());
            }
        }
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
                    fragment = TrackingList_Teacher_Fragment.newInstance(args);
                }
                selected = 0;
                break;
            case R.id.item_chat:
                tag = "conversations";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                    args.putString(Conversations_Fragment.ID_KEY, selectedClassId);
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
            case R.id.item_settings:
                tag = "settings";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                    //fragment = Calendar_Fragment.newInstance(args);
                }
                selected = 3;
                break;
        }
        if (fragment != null) {
                fragmentManager
                        .beginTransaction()
                        //.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.container_home, fragment)
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
        spinner = (Spinner) MenuItemCompat.getActionView(item);
        spinner.getBackground().setColorFilter(getResources().getColor(R.color.toolbar_text_color), PorterDuff.Mode.SRC_ATOP);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSelectedClassId(((NurseryClass) spinner.getSelectedItem()).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        refreshSpinner();
        return true;
    }

    public void setSelectedClassId(String selectedClassId) {
        this.selectedClassId = selectedClassId;
        if (selectedClassIdChangedListener != null)
            selectedClassIdChangedListener.selectedClassIdChanged(selectedClassId);

    }

    public String getSelectedClassId() {
        return selectedClassId;
    }

    public void setSelectedClassIdChangedListener (OnSelectedClassIdChangedListener selectedClassIdChangedListener) {
        this.selectedClassIdChangedListener = selectedClassIdChangedListener;
    }

    public void prepareCalendarToolbar(LinearLayout dates) {
        toolbar.setTitle("");
        toolbar.addView(dates);
    }

    public void prepareChatToolbar(Toolbar toolbar) {
        RelativeLayout container = (RelativeLayout) findViewById(R.id.activity_home_teacher);
        container.removeView(this.toolbar);
        ((ViewGroup) toolbar.getParent()).removeView(toolbar);
        container.addView(toolbar);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case R.id.action_sign_off:
                signOff();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void signOff() {
        ((Babyguard_Application)getApplicationContext()).signOff();
        Intent intent = new Intent(Home_Teacher_Activity.this,Login_Activity.class);
        startActivity(intent);
        finishAffinity();
    }

    public void setNavigationBottomBarHide(boolean navigationBottomBarHide) {
        bottomNavigationView.setVisibility( navigationBottomBarHide ? View.GONE : View.VISIBLE);
    }

    public interface OnSelectedClassIdChangedListener {
        void selectedClassIdChanged(String newId);
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ((Babyguard_Application)getApplicationContext()).setDatabaseLoaded(false);
    }
}
