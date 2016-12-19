package com.phile.babyguard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.phile.babyguard.adapter.InfoKid_Adapter;
import com.phile.babyguard.model.InfoKid;
import com.phile.babyguard.model.Kid;
import com.phile.babyguard.repository.Repository;

import java.util.ArrayList;

public class Home_Activity extends AppCompatActivity {

    Kid kid;
    InfoKid_Adapter adapter;
    RecyclerView rvInfoKid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String id_kid = getIntent().getExtras().getString(KidList_Activity.KID_EXTRA);
        kid = Repository.getInstance().getKidById(id_kid);
        rvInfoKid = (RecyclerView) findViewById(R.id.rvInfoKid);
        ArrayList<InfoKid> info = Repository.getInstance().getInfoKidById( Repository.Sort.CATEGORY);
        adapter = new InfoKid_Adapter(info, id_kid);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvInfoKid.setLayoutManager(mLayoutManager);
        rvInfoKid.setAdapter(adapter);
    }
}
