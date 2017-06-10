package com.ncatz.babyguard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ncatz.babyguard.adapter.TrackingKid_Adapter;
import com.ncatz.babyguard.firebase.FirebaseManager;
import com.ncatz.babyguard.interfaces.Home_View;
import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.model.TrackingKid;
import com.ncatz.babyguard.repository.Repository;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;
import com.yeray697.dotLineRecyclerView.DotLineRecyclerView;
import com.yeray697.dotLineRecyclerView.Message_View;
import com.yeray697.dotLineRecyclerView.RecyclerData;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Tracking_Teacher_Fragment extends Fragment implements Home_View{

    public static final String KID_KEY = "kid";
    private TextView tvName;
    private TextView tvInfo;
    private ImageView backButton;
    private CircleImageView ivProfile;
    private DotLineRecyclerView rvInfoKid;
    private TrackingKid_Adapter adapter;
    private FloatingActionButton fabAdd;

    private Kid kid;
    private ArrayList<? extends RecyclerData> dataKid;
    private TrackingKid selectedItemContextMenu;

    public static Tracking_Teacher_Fragment newInstance(Bundle args) {
        Tracking_Teacher_Fragment fragment = new Tracking_Teacher_Fragment();
        if (args != null)
            fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        final View view = inflater.inflate(R.layout.fragment_tracking_teacher,container,false);
        kid = Repository.getInstance().getKidById(getArguments().getString(KID_KEY));
        setHasOptionsMenu(true);
        tvName = (TextView) view.findViewById(R.id.tvName_tracking);
        backButton = (ImageView) view.findViewById(R.id.backButtonTeacher);
        ivProfile = (CircleImageView) view.findViewById(R.id.ivProfile_tracking);
        tvName = (TextView) view.findViewById(R.id.tvName_tracking);
        tvInfo = (TextView) view.findViewById(R.id.tvInfoKid_tracking);
        fabAdd = (FloatingActionButton) view.findViewById(R.id.fabAdd_tracking);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddTrackingFragment(null);
            }
        });
        rvInfoKid = (DotLineRecyclerView) view.findViewById(R.id.rvTrackingTeacher);
        tvName.setText(kid.getName());
        tvInfo.setSelected(true);
        tvInfo.setText(kid.getInfo());
        Picasso.with(getContext()).load(kid.getImg()).into(ivProfile);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvInfoKid.setLayoutManager(mLayoutManager);
        rvInfoKid.setLineColor(ContextCompat.getColor(getContext(), R.color.colorLineColor));
        rvInfoKid.setLineWidth(3);
        refreshList();
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
                refreshList();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }
    private void openAddTrackingFragment(Bundle args) {
        Bundle args2 = null;
        if (args == null) {
            args2 = new Bundle();
            args2.putString(AddTracking_Fragment.KID_ID,kid.getId());
        } else
            args.putString(AddTracking_Fragment.KID_ID,kid.getId());
        Fragment fragment = AddTracking_Fragment.newInstance( (args == null) ? args2 : args);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_home, fragment,"addTracking")
                .addToBackStack("addTracking")
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolbar();
    }

    private void refreshList() {
        dataKid = Repository.getInstance().getOrderedInfoKid(kid.getTracking(), Repository.Sort.CHRONOLOGIC);
        adapter = new TrackingKid_Adapter(getContext(), (ArrayList<RecyclerData>) dataKid,45);
        rvInfoKid.setAdapter(adapter);
    }

    private void setToolbar() {
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((Babyguard_Application)getContext().getApplicationContext()).removeTrackingListener();
        ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        ab.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        selectedItemContextMenu = (TrackingKid) v.getTag();
        menu.setHeaderTitle("Select The Action");
        menu.add(1, v.getId(), 0, "Edit");
        menu.add(2, v.getId(), 0, "Remove");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() == 1 && selectedItemContextMenu != null) { //Edit
            Bundle args = new Bundle();
            args.putParcelable(AddTracking_Fragment.TRACKING_KEY,selectedItemContextMenu);
            openAddTrackingFragment(args);
            selectedItemContextMenu = null;
        } else if (item.getGroupId() == 2) { //Remove
            final String idTracking = selectedItemContextMenu.getId();
            selectedItemContextMenu = null;
            AlertDialog.Builder dialog  = new AlertDialog.Builder(getContext());
            dialog.setTitle("¿Deseas borrar éste seguimiento?");
            dialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseManager.getInstance().removeTracking(kid.getId(),idTracking);
                }
            });
            dialog.setNegativeButton(android.R.string.cancel,null);
            dialog.show();
            selectedItemContextMenu = null;
        }
        return super.onContextItemSelected(item);
    }
}