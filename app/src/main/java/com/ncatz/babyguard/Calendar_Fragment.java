package com.ncatz.babyguard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.repository.Repository;
import com.ncatz.babyguard.utils.Utils;
import com.yeray697.calendarview.DiaryCalendarEvent;
import com.yeray697.calendarview.DiaryCalendarView;

import java.util.ArrayList;


public class Calendar_Fragment extends Fragment {
    private Kid kid;
    private DiaryCalendarView calendar;
    private ArrayList<DiaryCalendarEvent> calendarDates;


    public static Calendar_Fragment newInstance(Bundle args) {
        Calendar_Fragment fragment = new Calendar_Fragment();
        if (args != null)
            fragment.setArguments(args);
        return fragment;
    }

    public Calendar_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendar = (DiaryCalendarView) view.findViewById(R.id.calendar);
        setToolbar();
        setHasOptionsMenu(true);
        kid = Repository.getInstance().getKidById(getArguments().getString(Home_Activity.KID_KEY));
        calendarDates = Repository.getInstance().getCalendarByUser(kid);
        calendar.addEvent(calendarDates);
        return view;
    }

    private void setToolbar( ) {
        Toolbar toolbar = calendar.getToolbar();
        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        final ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_navigation_drawer);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }
        Utils.colorizeToolbar(toolbar, getResources().getColor(R.color.toolbar_text_color), getActivity());
        ((Babyguard_Application)getContext().getApplicationContext()).addCalendarListener(new Babyguard_Application.ActionEndListener() {
            @Override
            public void onEnd() {
                kid = Repository.getInstance().getKidById(kid.getId());
                calendarDates = Repository.getInstance().getCalendarByUser(kid);
                calendar.clearEvents();
                calendar.addEvent(calendarDates);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        ((Babyguard_Application)getContext().getApplicationContext()).removeCalendarListener();
    }
}
