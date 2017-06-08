package com.ncatz.babyguard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ncatz.babyguard.repository.Repository;
import com.ncatz.yeray.calendarview.DiaryCalendarEvent;

/**
 * Created by yeray697 on 8/06/17.
 */

public class AddEvent_Fragment extends Fragment {

    public static final String EVENT_KEY = "event";

    private ImageView backButton;

    private boolean editMode;
    private DiaryCalendarEvent event;

    public static AddEvent_Fragment newInstance(Bundle args) {
        AddEvent_Fragment fragment = new AddEvent_Fragment();
        if (args != null)
            fragment.setArguments(args);
        return fragment;
    }

    public AddEvent_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            editMode = true;
            event = args.getParcelable(EVENT_KEY);
        } else {
            editMode = false;
        }
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        backButton = (ImageView) view.findViewById(R.id.backButton_AddEvent);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolbar();
    }

    private void setToolbar() {
        ((Home_Teacher_Activity)getActivity()).getSupportActionBar().hide();
        ((Home_Teacher_Activity)getActivity()).setNavigationBottomBarHide(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        ((Babyguard_Application)(getContext()).getApplicationContext()).removeChatListener();
        ((Home_Teacher_Activity) getActivity()).getSupportActionBar().show();
        ((Home_Teacher_Activity)getActivity()).setNavigationBottomBarHide(false);
    }
}
