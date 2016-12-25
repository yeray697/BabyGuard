package com.phile.babyguard;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.phile.babyguard.adapter.InfoKid_Adapter;
import com.phile.babyguard.interfaces.Home_View;
import com.phile.babyguard.model.Kid;
import com.phile.babyguard.repository.Repository;
import com.phile.babyguard.utils.Utils;
import com.squareup.picasso.Picasso;
import com.yeray697.dotLineRecyclerView.DotLineRecyclerAdapter;
import com.yeray697.dotLineRecyclerView.DotLineRecyclerView;
import com.yeray697.dotLineRecyclerView.Message_View;
import com.yeray697.dotLineRecyclerView.RecyclerData;

import java.util.ArrayList;

public class Home_Activity extends AppCompatActivity implements Home_View{

    private Kid kid;
    private InfoKid_Adapter adapter;
    private DotLineRecyclerView rvInfoKid;
    private ArrayList<? extends RecyclerData> dataKid;
    private ImageView ivKid;
    private FloatingActionButton fabMessage;
    private boolean orderedByCategory;
    private boolean zoom;
    private ImageView ivExpandedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        String id_kid = getIntent().getExtras().getString(KidList_Activity.KID_EXTRA);
        kid = Repository.getInstance().getKidById(id_kid);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapser =
                (CollapsingToolbarLayout) findViewById(R.id.collapsingHome);
        collapser.setTitle(kid.getName());
        orderedByCategory= true;
        fabMessage = (FloatingActionButton) findViewById(R.id.fabMessage_Home);
        ivKid = (ImageView) findViewById(R.id.ivKid_Home);
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
        rvInfoKid = (DotLineRecyclerView) findViewById(R.id.rvHome);
        Picasso.with(this).load(kid.getPhoto())
                .into(ivKid);
        dataKid = Repository.getInstance().getInfoKidById( Repository.Sort.CHRONOLOGIC);
        adapter = new InfoKid_Adapter((ArrayList<RecyclerData>) dataKid,this);
        rvInfoKid.setLineColor(ContextCompat.getColor(this, R.color.colorLineColor));
        rvInfoKid.setLineWidth(3);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvInfoKid.setLayoutManager(mLayoutManager);
        rvInfoKid.setAdapter(adapter);
        adapter.setOnMessageClickListener(new Message_View.OnMessageClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_home:
                if (orderedByCategory){
                    dataKid = Repository.getInstance().getInfoKidById( Repository.Sort.CATEGORY);
                    item.setIcon(R.mipmap.ic_sort_by_category);
                }else {
                    dataKid = Repository.getInstance().getInfoKidById( Repository.Sort.CHRONOLOGIC);
                    item.setIcon(R.mipmap.ic_sort_chronologically);
                }
                adapter.clear();
                adapter.addAll((ArrayList<RecyclerData>)dataKid);
                orderedByCategory = !orderedByCategory;
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
