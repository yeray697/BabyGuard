package com.ncatz.babyguard;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.repository.Repository;
import com.ncatz.yeray.calendarview.DiaryCalendarEvent;
import com.ncatz.yeray.calendarview.DiaryCalendarView;

import java.util.ArrayList;


public class Calendar_Fragment extends Fragment {
    public static final String ID_KEY = "id";
    private Kid kid;
    private DiaryCalendarView calendar;
    private ArrayList<DiaryCalendarEvent> calendarDates;
    private LinearLayout expandableDate;
    private Home_Teacher_Activity.OnSelectedClassIdChangedListener listener;


    public static Calendar_Fragment newInstance(Bundle args) {
        Calendar_Fragment fragment = new Calendar_Fragment();
        if (args != null)
            fragment.setArguments(args);
        return fragment;
    }

    public Calendar_Fragment() {
        listener = new Home_Teacher_Activity.OnSelectedClassIdChangedListener() {
            @Override
            public void selectedClassIdChanged(String newId) {
                classId = newId;
                refreshCalendar();
            }
        };
    }

    private String classId;

    private void refreshCalendar() {
        if(Babyguard_Application.isTeacher()) {
            calendarDates = Repository.getInstance().getCalendarByNursery(Repository.getInstance().getUser().getId_nursery(),classId);
        } else {
            calendarDates = Repository.getInstance().getCalendarByNursery(kid.getId_nursery(), kid.getId_nursery_class());
        }
        calendar.clearEvents();
        calendar.addEvent(calendarDates);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendar = (DiaryCalendarView) view.findViewById(R.id.calendar);
        setToolbar();
        setHasOptionsMenu(true);
        if(Babyguard_Application.isTeacher()) {
            classId = getArguments().getString(ID_KEY);
        } else {
            kid = Repository.getInstance().getKidById(getArguments().getString(Home_Parent_Activity.KID_KEY));
        }
        refreshCalendar();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((Home_Teacher_Activity)getActivity()).setSelectedClassIdChangedListener(listener);
    }

    private void setToolbar() {
        Toolbar toolbar = calendar.getToolbar();
        if (!Babyguard_Application.isTeacher()) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            final ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
            if (ab != null) {
                ab.setHomeAsUpIndicator(R.drawable.ic_navigation_drawer);
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setHomeButtonEnabled(true);
            }
            ((Babyguard_Application)getContext().getApplicationContext()).addCalendarListener(new Babyguard_Application.ActionEndListener() {
                @Override
                public void onEnd() {
                    refreshCalendar();
                }
            });
        } else {
            expandableDate = calendar.customToolbar();
            toolbar.removeView(expandableDate);
            ((Home_Teacher_Activity)getActivity()).prepareCalendarToolbar(expandableDate);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((Babyguard_Application)getContext().getApplicationContext()).removeCalendarListener();
        ((ViewGroup)expandableDate.getParent()).removeView(expandableDate);
    }
}
