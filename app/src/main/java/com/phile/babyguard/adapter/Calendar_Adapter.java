package com.phile.babyguard.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.phile.babyguard.R;
import com.phile.babyguard.model.CalendarEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yeray697 on 29/12/16.
 */

public class Calendar_Adapter extends ExpandableRecyclerAdapter<Calendar_Adapter.ParentHolder,Calendar_Adapter.ChildHolder> {
    private int selectedEvent = -1;
    private int currentEvent = -1;

    public Calendar_Adapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
    }

    @Override
    public ParentHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.calendar_not_expanded_list_item, viewGroup, false);
        return new ParentHolder(view);
    }

    @Override
    public ChildHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.calendar_expanded_list_item, viewGroup, false);
        return new ChildHolder(view);
    }

    @Override
    public void onBindParentViewHolder(ParentHolder parentHolder, int i, Object parentObject) {
        parentHolder.tvDate.setText(((CalendarEvent) parentObject).getDate());
        parentHolder.tvTitle.setText(((CalendarEvent) parentObject).getName());
        if (checkDates(((CalendarEvent) parentObject).getDate())) {
            this.currentEvent = i;
            parentHolder.itemView.setBackgroundColor(ContextCompat.getColor(parentHolder.itemView.getContext(),R.color.colorAccent));
        } else {
            if (i == selectedEvent)
                parentHolder.itemView.setBackgroundColor(ContextCompat.getColor(parentHolder.itemView.getContext(),R.color.selectedEventColor));
            else
                parentHolder.itemView.setBackgroundColor(ContextCompat.getColor(parentHolder.itemView.getContext(),R.color.colorPrimaryLight));
        }
    }
    private boolean checkDates(String date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");

        boolean result = false;
        try {
            if (calendar.getTime().equals(dfDate.parse(date))) {
                result = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return result;
    }
    @Override
    public void onBindChildViewHolder(ChildHolder childHolder, int i, Object childObject) {
        final CalendarEvent.EventInfo description = ((CalendarEvent.EventInfo)childObject);
        childHolder.tvDescription.setText(description.getInfo());
    }

    class ParentHolder extends ParentViewHolder {
        TextView tvTitle;
        TextView tvDate;

        ParentHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvCalendarTitle_item);
            tvDate = (TextView) itemView.findViewById(R.id.tvCalendarDate_item);
        }
    }

    class ChildHolder extends ChildViewHolder {
        TextView tvDescription;

        ChildHolder(View itemView) {
            super(itemView);
            tvDescription = (TextView) itemView.findViewById(R.id.tvCalendarDescription_item);
        }
    }

    public int getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(int lastPosition, View lastCurrentView, int selectedEvent, View  currentView) {
        if (lastCurrentView != null && this.selectedEvent != this.currentEvent)
            lastCurrentView.setBackgroundColor(ContextCompat.getColor(lastCurrentView.getContext(),R.color.colorPrimaryLight));
        this.selectedEvent = selectedEvent;
        if (currentView != null && selectedEvent != this.currentEvent)
          currentView.setBackgroundColor(ContextCompat.getColor(currentView.getContext(),R.color.selectedEventColor));
        notifyItemChanged(lastPosition);
        notifyItemChanged(selectedEvent);
    }

    public int getCurrentEvent() {
        return currentEvent;
    }

}
