package com.ncatz.babyguard;

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

import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.repository.Repository;

public class Home_Parent_Activity extends AppCompatActivity {

    public static final String KID_KEY = "kid";
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
        setContentView(R.layout.activity_home);
        kid = getIntent().getExtras().getParcelable(KidList_Activity.KID_EXTRA);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            prepareDrawer(navigationView);
            if (getIntent().getExtras().getString(ACTION,"").equals(ACTION_OPEN_CALENDAR)){
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        String tag = "";
        Bundle args = null;
        switch (itemDrawer.getItemId()) {
            case R.id.item_tracing:
                tag = "tracing";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                    args.putString(KID_KEY, kid.getId());
                    fragment = Tracking_Fragment.newInstance(args);
                }
                selected = 0;
                break;
            case R.id.item_calendar:
                tag = "calendar";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                    args.putString(KID_KEY, kid.getId());
                    fragment = Calendar_Fragment.newInstance(args);
                }
                selected = 1;
                break;
            case R.id.item_chat:
                tag = "conversations";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                    args.putString(KID_KEY, kid.getId());
                    fragment = Conversations_Fragment.newInstance(args);
                }
                navigationView.getMenu().getItem(selected).setChecked(true);
                navigationView.getMenu().getItem(2).setChecked(false);
                //selected = 2;
                break;
            case R.id.item_profile:
                //selected = 4;
                /*args = new Bundle();
                args.putString(KID_NURSERY_KEY, kid.getId());
                Intent intent = new Intent(Home_Parent_Activity.this,AboutNursery_Activity.class);
                intent.putExtra(KID_NURSERY_KEY,kid.getId_nursery());
                startActivity(intent);*/
                navigationView.getMenu().getItem(3).setChecked(false);
                break;
            case R.id.item_contact:
                //selected = 4;
                args = new Bundle();
                args.putString(KID_NURSERY_KEY, kid.getId());
                Intent intent = new Intent(Home_Parent_Activity.this,AboutNursery_Activity.class);
                intent.putExtra(KID_NURSERY_KEY,kid.getId_nursery());
                startActivity(intent);
                navigationView.getMenu().getItem(4).setChecked(false);
                break;
            case R.id.item_settings:
                //TODO
                //selected = 5;
                navigationView.getMenu().getItem(5).setChecked(false);
                break;
        }
        if (fragment != null) {
            /*if (tag.equals("conversations")){
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container_home, fragment,tag)
                        .addToBackStack(null)
                        .commit();
            } else {*/
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.container_home, fragment,tag)
                        .commit();
            //}
        }
    }

    @Override
    public void onBackPressed() {
        if ((onBackPressedTracingListener != null && !onBackPressedTracingListener.doBack()) || onBackPressedTracingListener == null)
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
        ((Babyguard_Application)getApplicationContext()).signOff();
        Intent intent = new Intent(Home_Parent_Activity.this,Login_Activity.class);
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

    public void openChat(){
        selectItemDrawer(navigationView.getMenu().getItem(2));
    }

    public void closeChatFragment(){
        onBackPressed();
    }

    public void enableNavigationDrawer(boolean enable){
        if (drawerLayout != null) {
            if (enable){
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        }
    }
}
