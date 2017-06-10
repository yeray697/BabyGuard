package com.phile.yrj.testlinedotrecyclerview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yeray697.dotLineRecyclerView.DotLineRecyclerView;
import com.yeray697.dotLineRecyclerView.RecyclerData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<RecyclerData> data = new ArrayList<>();
        data.add(new RecyclerData("1","1"));
        data.add(new RecyclerData("2","2"));
        data.add(new RecyclerData("3","3"));
        data.add(new RecyclerData("4","4"));
        data.add(new RecyclerData("5","5"));
        DotLineRecyclerView recyclerView = (DotLineRecyclerView) findViewById(R.id.recyclerView);
        CustomAdapter adapter = new CustomAdapter(data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLineColor(Color.BLUE);
        recyclerView.setLineWidth(10);
    }
}
