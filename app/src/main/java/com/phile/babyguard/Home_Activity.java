package com.phile.babyguard;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.phile.babyguard.model.Kid;
import com.phile.babyguard.repository.Repository;
import com.phile.babyguard.utils.Utils;
import com.squareup.picasso.Picasso;

public class Home_Activity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Kid kid;
    private ImageView ivExpandedImage;
    private FloatingActionButton fabMessage;

    private boolean zoom;
    private ImageView ivKid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        agregarToolbar();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            prepararDrawer(navigationView);
            // Seleccionar item por defecto
            seleccionarItem(navigationView.getMenu().getItem(0));
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        String id_kid = getIntent().getExtras().getString(KidList_Activity.KID_EXTRA);

        kid = Repository.getInstance().getKidById(id_kid);
        CollapsingToolbarLayout collapser =
                (CollapsingToolbarLayout) findViewById(R.id.collapsingHome);
        collapser.setTitle(kid.getName());
        fabMessage = (FloatingActionButton) findViewById(R.id.fabMessage_Home);
        ivKid = (ImageView) findViewById(R.id.ivKid_Home);
        Picasso.with(this).load(kid.getPhoto())
                .into(ivKid);
        ivExpandedImage = (ImageView) findViewById(R.id.ivExpanded_Home);
        ivKid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoom = true;
                fabMessage.setVisibility(View.GONE);
                Utils.zoomImageFromThumb(Home_Activity.this, R.id.clHome, view, ivExpandedImage, ivKid.getDrawable(), new Utils.OnAnimationEnded() {
                    @Override
                    public void finishing() {
                        zoom = false;
                    }

                    @Override
                    public void finished() {
                        fabMessage.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        fabMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void prepararDrawer(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        seleccionarItem(menuItem);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

    }

    private void seleccionarItem(MenuItem itemDrawer) {
        Fragment fragmentoGenerico = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (itemDrawer.getItemId()) {
            case R.id.item_tracing:
                fragmentoGenerico = new Tracing_Fragment();
                break;
            case R.id.item_calendar:
                fragmentoGenerico = new Calendar_Fragment();
                break;
            case R.id.item_chat:
                fragmentoGenerico = new Chat_Fragment();
                break;
            case R.id.item_profile:
                fragmentoGenerico = new Profile_Fragment();
                break;
            case R.id.item_webcam:
                fragmentoGenerico = new WebCam_Fragment();
                break;
            case R.id.item_contact:
                fragmentoGenerico = new Contact_Fragment();
                break;
            case R.id.item_settings:
                //TODO
                break;
        }
        if (fragmentoGenerico != null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container_home, fragmentoGenerico)
                    .commit();
        }
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.mipmap.ic_navigation_drawer);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }


    @Override
    public void onBackPressed() {
        if (!zoom)
            super.onBackPressed();
        else {
            Utils.cancelZoomedImage(ivExpandedImage);
            fabMessage.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_actividad_principal, menu);
        return true;
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
