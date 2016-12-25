package com.phile.babyguard;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.phile.babyguard.adapter.InfoKid_Adapter;
import com.phile.babyguard.interfaces.Home_View;
import com.phile.babyguard.repository.Repository;
import com.yeray697.dotLineRecyclerView.DotLineRecyclerView;
import com.yeray697.dotLineRecyclerView.Message_View;
import com.yeray697.dotLineRecyclerView.RecyclerData;

import java.util.ArrayList;

public class Tracing_Fragment extends Fragment implements Home_View{



    private InfoKid_Adapter adapter;
    private DotLineRecyclerView rvInfoKid;
    private ArrayList<? extends RecyclerData> dataKid;
    private boolean orderedByCategory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracing,container,false);

        setHasOptionsMenu(true);

        orderedByCategory= true;

        rvInfoKid = (DotLineRecyclerView) view.findViewById(R.id.rvHome);
        dataKid = Repository.getInstance().getInfoKidById( Repository.Sort.CHRONOLOGIC);
        adapter = new InfoKid_Adapter(getContext(), (ArrayList<RecyclerData>) dataKid,45);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvInfoKid.setLayoutManager(mLayoutManager);
        rvInfoKid.setLineColor(ContextCompat.getColor(getContext(), R.color.colorLineColor));
        rvInfoKid.setAdapter(adapter);
        rvInfoKid.setLineWidth(5);
        adapter.setOnMessageClickListener(new Message_View.OnMessageClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu,menu);
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
