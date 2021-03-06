package com.ncatz.babyguard;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.preferences.Settings_Activity;

/**
 * Activity shown to a parent with actions to trace their kids
 */
public class Home_Parent_Activity extends AppCompatActivity {

    public static final String ACTION = "action";
    public static final String ACTION_OPEN_CALENDAR = "open_calendar";
    public static final String KID_NURSERY_KEY = "nursery";
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
        setContentView(R.layout.activity_home_parent);
        kid = getIntent().getExtras().getParcelable(KidList_Activity.KID_EXTRA);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            prepareDrawer(navigationView);
            if (getIntent().getExtras().getString(ACTION, "").equals(ACTION_OPEN_CALENDAR)) {
                selectItemDrawer(navigationView.getMenu().getItem(1));
            } else {
                selectItemDrawer(navigationView.getMenu().getItem(0));
            }
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
        Intent intent = null;
        FragmentManager fragmentManager = getFragmentManager();
        String tag = "";
        Bundle args = null;
        switch (itemDrawer.getItemId()) {
            case R.id.item_tracing:
                tag = "tracking";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                    args.putString(Tracking_Parent_Fragment.KID_KEY, kid.getId());
                    fragment = Tracking_Parent_Fragment.newInstance(args);
                }
                selected = 0;
                break;
            case R.id.item_calendar:
                tag = "calendar";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                    args.putString(Tracking_Parent_Fragment.KID_KEY, kid.getId());
                    fragment = Calendar_Fragment.newInstance(args);
                }
                selected = 1;
                break;
            case R.id.item_chat:
                tag = "conversations";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                    args.putString(Conversations_Fragment.ID_KEY, kid.getId());
                    fragment = Conversations_Fragment.newInstance(args);
                }
                navigationView.getMenu().getItem(selected).setChecked(true);
                navigationView.getMenu().getItem(2).setChecked(false);
                //selected = 2;
                break;
            case R.id.item_contact:
                //selected = 4;
                args = new Bundle();
                args.putString(KID_NURSERY_KEY, kid.getId());
                intent = new Intent(Home_Parent_Activity.this, AboutNursery_Activity.class);
                intent.putExtra(KID_NURSERY_KEY, kid.getId_nursery());
                startActivity(intent);
                navigationView.getMenu().getItem(3).setChecked(false);
                break;
            case R.id.item_settings:
                intent = new Intent(this, Settings_Activity.class);
                startActivity(intent);
                navigationView.getMenu().getItem(4).setChecked(false);
                break;
        }
        if (fragment != null) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction()
                    .replace(R.id.container_home, fragment, tag)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if ((onBackPressedTracingListener != null && !onBackPressedTracingListener.doBack()) || onBackPressedTracingListener == null) {
            super.onBackPressed();
            ((Babyguard_Application) getApplicationContext()).setDatabaseLoaded(false);
        }
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
        ((Babyguard_Application) getApplicationContext()).signOff();
        Intent intent = new Intent(Home_Parent_Activity.this, Login_Activity.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selected", selected);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selected = savedInstanceState.getInt("selected", 0);
        selectItemDrawer(navigationView.getMenu().getItem(selected));
    }

    public void openChat() {
        selectItemDrawer(navigationView.getMenu().getItem(2));
    }

    public void enableNavigationDrawer(boolean enable) {
        if (drawerLayout != null) {
            if (enable) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Babyguard_Application.setCurrentActivity("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Babyguard_Application.setCurrentActivity("Home_Parent_Activity");
    }
}
