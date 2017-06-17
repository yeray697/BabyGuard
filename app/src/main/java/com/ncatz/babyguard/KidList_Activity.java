package com.ncatz.babyguard;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.ncatz.babyguard.adapter.KidList_Adapter;
import com.ncatz.babyguard.interfaces.KidList_Presenter;
import com.ncatz.babyguard.interfaces.KidList_View;
import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.presenter.KidListPresenterImpl;
import com.ncatz.babyguard.repository.Repository;
import com.ncatz.babyguard.utils.Utils;

import java.util.ArrayList;

/**
 * KidList view
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class KidList_Activity extends AppCompatActivity implements KidList_View{

    private static final String CLICKED = "clicked";
    private ListView lvKids;
    public static final String KID_EXTRA = "kid_id";
    private KidList_Presenter presenter;
    private KidList_Adapter adapter;
    private ImageView ivExpandedImage;
    private Toolbar toolbar;
    private boolean zoom;
    private boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_list);
        lvKids = (ListView) findViewById(R.id.lvKidList);
        zoom = false;
        clicked = (savedInstanceState != null) && savedInstanceState.getBoolean(CLICKED, false);
        ivExpandedImage = (ImageView) findViewById(R.id.ivExpanded_KidList);
        presenter = new KidListPresenterImpl(this);
        presenter.addListener();
        //presenter.startLoading();
        lvKids.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!clicked) {
                    clicked = true;
                    Intent intent = new Intent(KidList_Activity.this, Home_Parent_Activity.class);
                    intent.putExtra(KID_EXTRA,(Kid)lvKids.getItemAtPosition(i));
                    startActivity(intent);
                    clicked = false;
                }
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar_kidlist);
        setSupportActionBar(toolbar);
    }

    @Override
    public void setKids() {
        adapter = new KidList_Adapter(KidList_Activity.this, new ArrayList<>(Repository.getInstance().getKids()), new KidList_Adapter.OnImageClickListener() {
                @Override
                public void clicked(final View view, Drawable drawable) {
                zoom = true;
                Utils.zoomImageFromThumb(view.getContext(),R.id.activity_kid_list, view, ivExpandedImage, drawable, new Utils.OnAnimationEnded() {
                    @Override
                    public void finishing() {
                        zoom = false;
                    }

                    @Override
                    public void finished() {
                    }
                });
            }
        });
        if (lvKids != null)
            lvKids.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setKids();
        Babyguard_Application.setCurrentActivity("KidList_Activity");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_sign_off:
                signOff();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void signOff() {
        ((Babyguard_Application)getApplicationContext()).signOff();
        Intent intent = new Intent(KidList_Activity.this,Login_Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (!zoom)
            super.onBackPressed();
        else
            Utils.cancelZoomedImage(ivExpandedImage);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(CLICKED,clicked);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.removeListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Babyguard_Application.setCurrentActivity("");
    }
}
