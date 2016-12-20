package com.phile.babyguard;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.phile.babyguard.adapter.KidList_Adapter;
import com.phile.babyguard.interfaces.KidList_Presenter;
import com.phile.babyguard.interfaces.KidList_View;
import com.phile.babyguard.model.Kid;
import com.phile.babyguard.presenter.KidListPresenterImpl;
import com.phile.babyguard.repository.Repository;

import java.util.List;

/**
 * KidList view
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class KidList_Activity extends ListActivity implements KidList_View{

    public static final String KID_EXTRA = "kid_id";
    KidList_Presenter presenter;
    KidList_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_list);
        presenter = new KidListPresenterImpl(this);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(KidList_Activity.this, Home_Activity.class);
                intent.putExtra(KID_EXTRA,((Kid)getListView().getItemAtPosition(i)).getIdKid());
                startActivity(intent);
            }
        });
    }

    @Override
    public void setKids() {
        adapter = new KidList_Adapter(this);
        getListView().setAdapter(adapter);
    }
}
