package com.phile.babyguard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.phile.babyguard.adapter.Calendar_Adapter;
import com.phile.babyguard.model.CalendarEvent;
import com.phile.babyguard.model.Kid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Calendar_Fragment extends Fragment {

    private static final String CURRENT_DATE_KEY = "current_date";
    private static final java.lang.String CURRENT_MONTH_KEY = "current_month";
    private static final String SCROLL_KEY = "scroll";
    private static final java.lang.String SELECTED_EVENT_KEY = "selected_event";
    private Kid kid;
    private TextView tvMonth;
    private CompactCalendarView calendar;
    private ArrayList<? extends ParentObject> calendarDates;
    private RecyclerView recyclerView;
    private Calendar_Adapter adapter;

    private Date currentDate;
    private String currentMonth;

    private final SimpleDateFormat month_date = new SimpleDateFormat("MMMM - yyyy");

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        setToolbar(view);
        setHasOptionsMenu(true);
        kid = getArguments().getParcelable(Home_Activity.KID_KEY);
        calendarDates = kid.getCalendarEvents();
        recyclerView = (RecyclerView) view.findViewById(R.id.rvCalendar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Calendar_Adapter(getContext(), (List<ParentObject>) calendarDates);
        adapter.setCustomParentAnimationViewId(R.id.btCalendarExpand_item);
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);
        recyclerView.setAdapter(adapter);
        tvMonth = (TextView) view.findViewById(R.id.tvMonth_Calendar);
        calendar = (CompactCalendarView) view.findViewById(R.id.calendar);
        currentMonth = month_date.format(calendar.getFirstDayOfCurrentMonth());
        tvMonth.setText(currentMonth);
        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                int position = checkDates(dateClicked);
                if (position != -1)
                    recyclerView.getLayoutManager().scrollToPosition(position);
                setSelectedEvent(position);
                currentDate = dateClicked;
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                currentMonth = month_date.format(firstDayOfNewMonth);
                tvMonth.setText(currentMonth);
                int position = checkDates(firstDayOfNewMonth);
                if (position != -1)
                    recyclerView.getLayoutManager().scrollToPosition(position);
                setSelectedEvent(position);
                currentDate = firstDayOfNewMonth;
            }
        });

        setEvents();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null){
            currentDate = new Date(savedInstanceState.getLong(CURRENT_DATE_KEY));
            currentMonth = savedInstanceState.getString(CURRENT_MONTH_KEY);
            if (currentDate != null)
                calendar.setCurrentDate(currentDate);
            if (currentMonth != null)
                tvMonth.setText(currentMonth);
            recyclerView.scrollToPosition(savedInstanceState.getInt(SCROLL_KEY));
            setSelectedEvent(savedInstanceState.getInt(SELECTED_EVENT_KEY));
        }
    }

    private int checkDates(Date dateClicked) {
        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");

        int result = -1;
        int position = 0;
        for (CalendarEvent event: kid.getCalendarEvents()) {
            try {
                if (dateClicked.equals(dfDate.parse(event.getDate()))) {
                    result = position;  // If start date is before end date.
                    break;
                } else {
                    position ++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return result;
    }

    private void setEvents() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        List<Event> listEvents = new ArrayList<>();
        for (CalendarEvent event: kid.getCalendarEvents()) {
            try {
                date = sdf.parse(event.getDate());
                listEvents.add(new Event(event.getColor(),date.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        calendar.addEvents(listEvents);
    }

    private void setToolbar(View rootView) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbarCalendar);
        toolbar.setTitle("");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        final ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (ab != null) {
            //TODO set a smaller icon
            ab.setHomeAsUpIndicator(R.mipmap.ic_navigation_drawer);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeButtonEnabled(true);
        }
    }

    private void setSelectedEvent(int position){
        int lastPosition = adapter.getSelectedEvent();
        RecyclerView.ViewHolder lastViewHolder = recyclerView.findViewHolderForAdapterPosition(lastPosition);
        View lastView = null;
        if (lastViewHolder != null)
            lastView = lastViewHolder.itemView;

        RecyclerView.ViewHolder currentViewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        View currentView = null;
        if (currentViewHolder != null)
            currentView = currentViewHolder.itemView;
        adapter.setSelectedEvent(lastPosition, lastView,position,currentView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentDate != null)
            outState.putLong(CURRENT_DATE_KEY,currentDate.getTime());
        outState.putString(CURRENT_MONTH_KEY,currentMonth);
        outState.putInt(SCROLL_KEY, recyclerView.computeVerticalScrollOffset());
        outState.putInt(SELECTED_EVENT_KEY, adapter.getSelectedEvent());
    }

}
