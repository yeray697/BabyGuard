package com.phile.babyguard;

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

import com.phile.babyguard.adapter.KidList_Adapter;
import com.phile.babyguard.interfaces.KidList_Presenter;
import com.phile.babyguard.interfaces.KidList_View;
import com.phile.babyguard.model.Kid;
import com.phile.babyguard.presenter.KidListPresenterImpl;
import com.phile.babyguard.utils.Utils;

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
        zoom = false;
        clicked = (savedInstanceState != null) && savedInstanceState.getBoolean(CLICKED, false);
        ivExpandedImage = (ImageView) findViewById(R.id.ivExpanded_KidList);
        presenter = new KidListPresenterImpl(this);
        lvKids = (ListView) findViewById(R.id.lvKidList);
        lvKids.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!clicked) {
                    clicked = true;
                    Intent intent = new Intent(KidList_Activity.this, Home_Activity.class);
                    intent.putExtra(KID_EXTRA,(Kid)lvKids.getItemAtPosition(i));
                    startActivity(intent);
                    clicked = false;
                }
            }
        });
        toolbar = (Toolbar) findViewById(R.id.toolbar_kidlist);
        setSupportActionBar(toolbar);
        Utils.colorizeToolbar(toolbar, getResources().getColor(R.color.toolbar_color), this);
    }

    @Override
    public void setKids() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                adapter = new KidList_Adapter(KidList_Activity.this, new KidList_Adapter.OnImageClickListener() {
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
                lvKids.setAdapter(adapter);
            }
        });
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
        ((Babyguard_Application)getApplicationContext()).setUser(null);
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
}
