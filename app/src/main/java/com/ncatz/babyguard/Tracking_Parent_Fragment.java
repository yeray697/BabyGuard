package com.ncatz.babyguard;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ncatz.babyguard.adapter.TrackingKid_Adapter;
import com.ncatz.babyguard.interfaces.Home_View;
import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.model.TrackingKid;
import com.ncatz.babyguard.repository.Repository;
import com.ncatz.babyguard.utils.Utils;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yeray697.dotLineRecyclerView.DotLineRecyclerView;
import com.yeray697.dotLineRecyclerView.Message_View;
import com.yeray697.dotLineRecyclerView.RecyclerData;

import java.util.ArrayList;

public class Tracking_Parent_Fragment extends Fragment implements Home_View{

    public static final String KID_KEY = "kid";
    private static final String MULTIPLE_LISTENER = "multipleListener";
    private TrackingKid_Adapter adapter;
    private DotLineRecyclerView rvInfoKid;
    private ArrayList<? extends RecyclerData> dataKid;
    private boolean orderedByCategory;
    private ImageView ivExpandedImage;
    private FloatingActionButton fabMessage;
    private CollapsingToolbarLayout collapser;

    private Kid kid;
    private boolean zoom;
    private ImageView ivKid;
    private int order;
    private Context context;

    public static Tracking_Parent_Fragment newInstance(Bundle args) {
        Tracking_Parent_Fragment fragment = new Tracking_Parent_Fragment();
        if (args != null)
            fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((Home_Parent_Activity)getActivity()).enableNavigationDrawer(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context = getContext();
        } else {
            context = getActivity();
        }
        final View view = inflater.inflate(R.layout.fragment_tracking_parent,container,false);
        kid = Repository.getInstance().getKidById(getArguments().getString(KID_KEY));

        setToolbar(view);
        setHasOptionsMenu(true);
        ((Home_Parent_Activity)getActivity()).setOnBackPressedTracingListener(new Home_Parent_Activity.OnBackPressedTracingListener() {
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
        collapser = (CollapsingToolbarLayout) view.findViewById(R.id.collapsingHome);
        fabMessage = (FloatingActionButton) view.findViewById(R.id.fabMessage_Home);
        ivKid = (ImageView) view.findViewById(R.id.ivKid_Home);
        ivExpandedImage = (ImageView) view.findViewById(R.id.ivExpanded_Home);
        ivKid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoom = true;
                fabMessage.setVisibility(View.GONE);
                Utils.zoomImageFromThumb(context, R.id.clHome, view, ivExpandedImage, ivKid.getDrawable(), new Utils.OnAnimationEnded() {
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
                ((Home_Parent_Activity)getActivity()).openChat();
            }
        });
        rvInfoKid = (DotLineRecyclerView) view.findViewById(R.id.rvHome);
        order = Repository.Sort.CHRONOLOGIC;

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        rvInfoKid.setLayoutManager(mLayoutManager);
        rvInfoKid.setLineColor(ContextCompat.getColor(context, R.color.colorLineColor));
        rvInfoKid.setLineWidth(3);
        refreshTrackingList();
        ((Babyguard_Application)context.getApplicationContext()).addTrackingListener(new Babyguard_Application.ActionEndListener() {
            @Override
            public void onEnd() {
                refreshTrackingList();
            }
        });
        return view;
    }

    private void refreshTrackingList() {
        kid = Repository.getInstance().getKidById(kid.getId()); //Refreshing
        dataKid = Repository.getInstance().getOrderedInfoKid(kid.getTracking(), order);
        adapter = new TrackingKid_Adapter(context, (ArrayList<RecyclerData>) dataKid,45);
        rvInfoKid.setAdapter(adapter);
        collapser.setTitle(kid.getName());

        ivKid.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Glide.with(context)
                .load(kid.getImg())
                .apply(RequestOptions.placeholderOf(R.mipmap.ic_placeholder_profile_img))
                .apply(RequestOptions.errorOf(R.mipmap.ic_placeholder_profile_img))
                .into(ivKid);
        ivKid.setScaleType(ImageView.ScaleType.CENTER_CROP);
        adapter.setOnMessageClickListener(new Message_View.OnMessageClickListener() {
            @Override
            public void onClick(View view, int i) {
                TrackingKid aux = (TrackingKid) adapter.getItemAtPosition(i);
                new LovelyInfoDialog(context)
                        .setTopColorRes(R.color.colorPrimaryLightDark)
                        .setIcon(R.mipmap.ic_info_outline_white_36dp)
                        .setTitle(aux.getTitle())
                        .setMessage(aux.getDescription())
                        .show();
            }
        });
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
        toolbar.setVisibility(View.VISIBLE);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        final ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_navigation_drawer);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((Babyguard_Application)context.getApplicationContext()).removeTrackingListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        Babyguard_Application.setCurrentActivity("Tracking_Parent_Fragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        Babyguard_Application.setCurrentActivity("");
    }
}