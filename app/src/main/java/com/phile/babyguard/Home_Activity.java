package com.phile.babyguard;

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
import com.phile.babyguard.repository.Repository;

public class Home_Activity extends AppCompatActivity {

    public static final String KID_KEY = "kid";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Kid kid;
    private OnBackPressedTracingListener onBackPressedTracingListener;

    public interface OnBackPressedTracingListener {
        public boolean doBack();
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
                break;
            case R.id.item_calendar:
                tag = "calendar";
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null) {
                    args = new Bundle();
                    args.putParcelable(KID_KEY, kid);
                    fragment = Calendar_Fragment.newInstance(args);
                }
                break;
            case R.id.item_chat:
                fragment = new Chat_Fragment();
                break;
            case R.id.item_profile:
                fragment = new Profile_Fragment();
                break;
            case R.id.item_webcam:
                fragment = new WebCam_Fragment();
                break;
            case R.id.item_contact:
                fragment = new Contact_Fragment();
                break;
            case R.id.item_settings:
                //TODO
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
        }
        return super.onOptionsItemSelected(item);
    }
}
