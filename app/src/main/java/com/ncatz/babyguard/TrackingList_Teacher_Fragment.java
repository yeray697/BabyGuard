package com.ncatz.babyguard;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ncatz.babyguard.adapter.KidList_Adapter;
import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.repository.Repository;

import java.util.ArrayList;

/**
 * Created by yeray697 on 3/06/17.
 */

public class TrackingList_Teacher_Fragment extends Fragment{

    private KidList_Adapter adapter;
    private ListView lvKids;
    private Home_Teacher_Activity.OnSelectedClassIdChangedListener listener;
    private String classId;
    private boolean clicked;
    public TrackingList_Teacher_Fragment() {
        // Required empty public constructor
        listener = new Home_Teacher_Activity.OnSelectedClassIdChangedListener() {
            @Override
            public void selectedClassIdChanged(String newId) {
                classId = newId;
                refreshKidList();
            }
        };
    }

    public static TrackingList_Teacher_Fragment newInstance(Bundle args) {
        TrackingList_Teacher_Fragment fragment = new TrackingList_Teacher_Fragment();
        if (args != null)
            fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        clicked = false;
        View view = inflater.inflate(R.layout.fragment_trackinglist_teacher, container, false);
        lvKids = (ListView) view.findViewById(R.id.lvKidList);
        lvKids.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!clicked) {
                    clicked = true;
                    openKid((Kid)lvKids.getItemAtPosition(i));
                    clicked = false;
                }
            }
        });
        setToolbar();
        classId = ((Home_Teacher_Activity) getActivity()).getSelectedClassId();
        if (classId != null && !classId.equals(""))
            refreshKidList();
        return view;
    }

    private void openKid(Kid kid) {
        Bundle args = new Bundle();
        args.putString(Tracking_Parent_Fragment.KID_KEY, kid.getId());
        Fragment fragment = Tracking_Teacher_Fragment.newInstance(args);
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.container_home, fragment,"tracking")
                .addToBackStack("tracking")
                .commit();
    }

    private void setToolbar() {
        ((Home_Teacher_Activity) getActivity()).getSupportActionBar().setTitle("Babyguard");
    }

    private void refreshKidList() {
        ArrayList<Kid> kids = Repository.getInstance().getKidsByClass(classId);
        adapter = new KidList_Adapter(lvKids.getContext(), kids, new KidList_Adapter.OnImageClickListener() {
            @Override
            public void clicked(View view, Drawable drawable) {

            }
        });
        lvKids.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((Home_Teacher_Activity)getActivity()).setSelectedClassIdChangedListener(listener);
    }
}
