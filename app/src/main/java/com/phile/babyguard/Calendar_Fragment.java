package com.phile.babyguard;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phile.babyguard.model.Kid;
import com.phile.babyguard.utils.Utils;
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
        kid = getArguments().getParcelable(Home_Activity.KID_KEY);
        calendarDates = kid.getCalendarEvents();
        calendar.addEvent(calendarDates);
        return view;
    }

    private void setToolbar( ) {
        Toolbar toolbar = calendar.getToolbar();
        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        final ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.mipmap.ic_navigation_drawer);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }
        Utils.colorizeToolbar(toolbar, getResources().getColor(R.color.toolbar_color), getActivity());
    }
}
