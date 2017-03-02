package com.phile.babyguard;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.phile.babyguard.model.Kid;

public class Home_Activity extends AppCompatActivity {

    public static final String KID_KEY = "kid";
    private int selected;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Kid kid;
    private OnBackPressedTracingListener onBackPressedTracingListener;

    public interface OnBackPressedTracingListener {
        boolean doBack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        kid = getIntent().getExtras().getParcelable(KidList_Activity.KID_EXTRA);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            prepareDrawer(navigationView);
            selectItemDrawer(navigationView.getMenu().getItem(0));
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

    }
    private void prepareDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        selectItemDrawer(menuItem);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void selectItemDrawer(MenuItem itemDrawer) {
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
                    args.putParcelable(KID_KEY, kid);
                    fragment = Tracing_Fragment.newInstance(args);
                }
                selected = 0;
                break;
            case R.id.item_calendar:
                tag = "calendar";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                    args.putParcelable(KID_KEY, kid);
                    fragment = Calendar_Fragment.newInstance(args);
                }
                selected = 1;
                break;
            case R.id.item_chat:
                tag = "chat";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                    args.putParcelable(KID_KEY, kid);
                    fragment = Chat_Fragment.newInstance(args);
                }
                selected = 2;
                break;
            case R.id.item_webcam:
                tag = "webcam";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                    args.putParcelable(KID_KEY, kid);
                    fragment = WebCam_Fragment.newInstance(args);
                }
                selected = 3;
                break;
            case R.id.item_contact:
                tag = "contact";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                    args.putParcelable(KID_KEY, kid);
                    fragment = Contact_Fragment.newInstance(args);
                }
                selected = 4;
                break;
            case R.id.item_settings:
                //TODO
                selected = 5;
                break;
        }
        if (fragment != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container_home, fragment,tag)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedTracingListener != null)
            if(!onBackPressedTracingListener.doBack())
                super.onBackPressed();
    }

    public void setOnBackPressedTracingListener(OnBackPressedTracingListener onBackPressedTracingListener) {
        this.onBackPressedTracingListener = onBackPressedTracingListener;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_sign_off:
                signOff();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void signOff() {
        ((Babyguard_Application)getApplicationContext()).setUser(null);
        Intent intent = new Intent(Home_Activity.this,Login_Activity.class);
        startActivity(intent);
        finishAffinity();

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
        selectItemDrawer(navigationView.getMenu().getItem(selected));
    }
}
