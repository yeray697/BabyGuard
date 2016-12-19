package com.phile.babyguard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.phile.babyguard.model.Kid;
import com.phile.babyguard.repository.Repository;

public class Home_Activity extends AppCompatActivity {

    Kid kid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        kid = Repository.getInstance().getKidById(getIntent().getExtras().getString(KidList_Activity.KID_EXTRA));
    }
}
