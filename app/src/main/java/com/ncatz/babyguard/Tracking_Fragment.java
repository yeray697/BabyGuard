package com.ncatz.babyguard;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ncatz.babyguard.adapter.InfoKid_Adapter;
import com.ncatz.babyguard.interfaces.Home_View;
import com.ncatz.babyguard.model.TrackingKid;
import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.repository.Repository;
import com.ncatz.babyguard.utils.Utils;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yeray697.dotLineRecyclerView.DotLineRecyclerView;
import com.yeray697.dotLineRecyclerView.Message_View;
import com.yeray697.dotLineRecyclerView.RecyclerData;

import java.util.ArrayList;

public class Tracking_Fragment extends Fragment implements Home_View{

    private static final String MULTIPLE_LISTENER = "multipleListener";
    private InfoKid_Adapter adapter;
    private DotLineRecyclerView rvInfoKid;
    private ArrayList<? extends RecyclerData> dataKid;
    private boolean orderedByCategory;
    private ImageView ivExpandedImage;
    private FloatingActionButton fabMessage;

    private Kid kid;
    private boolean zoom;
    private ImageView ivKid;

    public static Tracking_Fragment newInstance(Bundle args) {
        Tracking_Fragment fragment = new Tracking_Fragment();
        if (args != null)
            fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tracing,container,false);
        kid = Repository.getInstance().getKidById(getArguments().getString(Home_Activity.KID_KEY));

        setToolbar(view);
        setHasOptionsMenu(true);
        ((Home_Activity)getActivity()).setOnBackPressedTracingListener(new Home_Activity.OnBackPressedTracingListener() {
            @Override
            public boolean doBack() {
                boolean result = true;
                if (!zoom)
                    result = false;
                else {
                    Utils.cancelZoomedImage(ivExpandedImage);
                    fabMessage.setVisibility(View.VISIBLE);
                }
                return result;
            }
        });
        orderedByCategory= true;
        final CollapsingToolbarLayout collapser =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsingHome);
        collapser.setTitle(kid.getName());
        fabMessage = (FloatingActionButton) view.findViewById(R.id.fabMessage_Home);
        ivKid = (ImageView) view.findViewById(R.id.ivKid_Home);
        //Picasso.with(getContext()).load(kid.getImg())
        //        .into(ivKid);
        ivKid.setBackgroundColor(Color.WHITE);
        ivExpandedImage = (ImageView) view.findViewById(R.id.ivExpanded_Home);
        ivKid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMessage.setVisibility(View.GONE);
                Utils.zoomImageFromThumb(getContext(), R.id.clHome, view, ivExpandedImage, ivKid.getDrawable(), new Utils.OnAnimationEnded() {
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
        rvInfoKid = (DotLineRecyclerView) view.findViewById(R.id.rvHome);
        order = Repository.Sort.CHRONOLOGIC;
        dataKid = Repository.getInstance().getOrderedInfoKid(kid.getTracking(), order);
        adapter = new InfoKid_Adapter(getContext(), (ArrayList<RecyclerData>) dataKid,45);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvInfoKid.setLayoutManager(mLayoutManager);
        rvInfoKid.setLineColor(ContextCompat.getColor(getContext(), R.color.colorLineColor));
        rvInfoKid.setAdapter(adapter);
        rvInfoKid.setLineWidth(3);
        adapter.setOnMessageClickListener(new Message_View.OnMessageClickListener() {
            @Override
            public void onClick(View view, int i) {
                    TrackingKid aux = (TrackingKid) adapter.getItemAtPosition(i);
                    new LovelyInfoDialog(getContext())
                            .setTopColorRes(R.color.colorPrimaryLightDark)
                            .setIcon(R.mipmap.ic_info_outline_white_36dp)
                            .setTitle(aux.getTitle())
                            .setMessage(aux.getDescription())
                            .show();
            }
        });
        ((Babyguard_Application)getContext().getApplicationContext()).addTrackingListener(new Babyguard_Application.ActionEndListener() {
            @Override
            public void onEnd() {
                kid = Repository.getInstance().getKidById(kid.getId());
                dataKid = Repository.getInstance().getOrderedInfoKid(kid.getTracking(), order);
                adapter = new InfoKid_Adapter(getContext(), (ArrayList<RecyclerData>) dataKid,45);
                rvInfoKid.setAdapter(adapter);
                collapser.setTitle(kid.getName());
                Picasso.with(getContext()).load(kid.getImg())
                        .into(ivKid);
            }
        });
        return view;
    }
    private int order;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_home:
                if (orderedByCategory){
                    order = Repository.Sort.CATEGORY;
                    item.setIcon(R.drawable.ic_sort_by_category);
                }else {
                    order = Repository.Sort.CHRONOLOGIC;
                    item.setIcon(R.mipmap.ic_sort_chronologically);
                }
                dataKid = Repository.getInstance().getOrderedInfoKid(kid.getTracking(), order);
                adapter.clear();
                adapter.addAll((ArrayList<RecyclerData>)dataKid);
                orderedByCategory = !orderedByCategory;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setToolbar(View rootView) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbarHome);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        final ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (ab != null) {
            /*ab.setHomeAsUpIndicator(R.drawable.ic_navigation_drawer);
            ab.setDisplayHomeAsUpEnabled(true);*/
        }
        Utils.colorizeToolbar(toolbar, getResources().getColor(R.color.toolbar_text_color), getActivity());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((Babyguard_Application)getContext().getApplicationContext()).removeTrackingListener();
    }
}
