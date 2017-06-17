package com.ncatz.babyguard;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ncatz.babyguard.components.CustomToolbar;
import com.ncatz.babyguard.firebase.FirebaseManager;
import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.model.PushNotification;
import com.ncatz.babyguard.model.User;
import com.ncatz.babyguard.repository.Repository;
import com.ncatz.babyguard.utils.OneClickListener;
import com.ncatz.yeray.calendarview.DiaryCalendarEvent;
import com.ncatz.yeray.calendarview.DiaryCalendarView;

import java.util.ArrayList;


public class Calendar_Fragment extends Fragment implements View.OnCreateContextMenuListener {
    public static final String KID_KEY = "kid";
    public static final String ID_KEY = "id";
    private Kid kid;
    private DiaryCalendarView calendar;
    private ArrayList<DiaryCalendarEvent> calendarDates;
    private LinearLayout expandableDate;
    private FloatingActionButton fabAddEvent;
    private Home_Teacher_Activity.OnSelectedClassIdChangedListener listener;
    private String classId;
    private DiaryCalendarEvent selectedItemContextMenu;
    private Context context;

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
        setRetainInstance(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context = getContext();
        } else {
            context = getActivity();
        }
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        fabAddEvent = (FloatingActionButton) view.findViewById(R.id.fabAdd_calendar);
        calendar = (DiaryCalendarView) view.findViewById(R.id.calendar);
        calendar.setSaveEnabled(true);
        setToolbar(view);
        if(Babyguard_Application.isTeacher()) {
            fabAddEvent.setVisibility(View.VISIBLE);
            if (savedInstanceState != null) {
                classId = savedInstanceState.getString(ID_KEY,"");
            }
            if (TextUtils.isEmpty(classId))
                classId = getArguments().getString(ID_KEY);
            calendar.setOnCreateContextMenuListenerItem(this);
        } else {
            fabAddEvent.setVisibility(View.GONE);
            kid = Repository.getInstance().getKidById(getArguments().getString(KID_KEY));
        }
        fabAddEvent.setOnClickListener(new OneClickListener() {
            @Override
            protected void onOneClick() {
                openAddEventFragment(null);
            }
        });
        return view;
    }

    private void openAddEventFragment(Bundle args) {
        calendar.setSaveEnabled(false);
        Fragment fragment = AddEvent_Fragment.newInstance(args);
        getActivity().getFragmentManager()
                .beginTransaction()
                .replace(R.id.container_home, fragment,"addEvent")
                .addToBackStack("addEvent")
                .commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (Babyguard_Application.isTeacher())
            ((Home_Teacher_Activity)getActivity()).setSelectedClassIdChangedListener(listener);
    }

    private void setToolbar(View view) {
        Toolbar toolbar = calendar.getToolbar();
        CustomToolbar customToolbar = (CustomToolbar) view.findViewById(R.id.toolbarCalendarParent);
        expandableDate = calendar.customToolbar();
        calendar.setDateArrowColor(getResources().getColor(R.color.toolbar_text_color));
        toolbar.removeView(expandableDate);
        if (!Babyguard_Application.isTeacher()) {
            customToolbar.setVisibility(View.VISIBLE);
            customToolbar.setTitle("");
            customToolbar.addView(expandableDate);
            ((AppCompatActivity)getActivity()).setSupportActionBar(customToolbar);
            final ActionBar ab = ((AppCompatActivity)getActivity()).getSupportActionBar();
            if (ab != null) {
                ab.setHomeAsUpIndicator(R.drawable.ic_navigation_drawer);
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setHomeButtonEnabled(true);
            }
            ((Babyguard_Application)context.getApplicationContext()).addCalendarListener(new Babyguard_Application.ActionEndListener() {
                @Override
                public void onEnd() {
                    refreshCalendar();
                }
            });
        } else {
            customToolbar.setVisibility(View.GONE);
            ((Home_Teacher_Activity)getActivity()).prepareCalendarToolbar(expandableDate);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshCalendar();
        Babyguard_Application.setCurrentActivity("Calendar_Fragment");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (Babyguard_Application.isTeacher()) {
            ((Babyguard_Application) context.getApplicationContext()).removeCalendarListener();
            ((ViewGroup) expandableDate.getParent()).removeView(expandableDate);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Babyguard_Application.setCurrentActivity("");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        selectedItemContextMenu = (DiaryCalendarEvent) v.getTag();
        menu.setHeaderTitle("Select The Action");
        menu.add(1, v.getId(), 0, "Edit");
        menu.add(2, v.getId(), 0, "Remove");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() == 1 && selectedItemContextMenu != null) { //Edit
            Bundle args = new Bundle();
            args.putParcelable(AddEvent_Fragment.EVENT_KEY,selectedItemContextMenu);
            args.putString(AddEvent_Fragment.CLASS_ID_KEY,classId);
            openAddEventFragment(args);
            selectedItemContextMenu = null;
        } else if (item.getGroupId() == 2) { //Remove
            final String idEvent = selectedItemContextMenu.getId();
            selectedItemContextMenu = null;
            AlertDialog.Builder dialog  = new AlertDialog.Builder(context);
            dialog.setTitle("¿Deseas borrar el evento?");
            dialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (FirebaseManager.getInstance().removeEvent(Repository.getInstance().getUser().getId_nursery(),classId,idEvent)) {
                        PushNotification notification = new PushNotification();
                        notification.setFromUser(Repository.getInstance().getUser().getId());
                        notification.setDiaryCalendarEvent(Repository.getInstance().getEventById(Repository.getInstance().getUser().getId_nursery(),classId, idEvent));
                        notification.setType(PushNotification.TYPE_CALENDAR_REMOVE);

                        for (Kid aux : Repository.getInstance().getKids()) {
                            if (aux.getId_nursery_class().equals(classId)) {
                                notification.setToUser(aux.getId());
                                notification.pushNotification(aux.getFcmID());
                            }
                        }
                        refreshCalendar();
                    }
                }
            });
            dialog.setNegativeButton(android.R.string.cancel,null);
            dialog.show();
            selectedItemContextMenu = null;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(classId,ID_KEY);
    }
}
