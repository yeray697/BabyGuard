package com.ncatz.babyguard.model;

import android.support.annotation.NonNull;

import com.ncatz.yeray.calendarview.DiaryCalendarEvent;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by yeray697 on 27/05/17.
 */

public class NurseryClass implements Comparable<NurseryClass> {
    public static Comparator<? super NurseryClass> comparatorName = new Comparator<NurseryClass>() {
        @Override
        public int compare(NurseryClass o1, NurseryClass o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };
    private String id;
    private String name;
    private ArrayList<Kid> kids;
    private ArrayList<DiaryCalendarEvent> calendarEvents;

    public NurseryClass() {
    }

    public NurseryClass(String id, String name, ArrayList<Kid> kids, ArrayList<DiaryCalendarEvent> calendarEvents) {
        this.id = id;
        this.name = name;
        this.kids = kids;
        this.calendarEvents = calendarEvents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Kid> getKids() {
        return kids;
    }

    public void setKids(ArrayList<Kid> kids) {
        this.kids = kids;
    }

    public ArrayList<DiaryCalendarEvent> getCalendar() {
        return calendarEvents;
    }

    public void setCalendar(ArrayList<DiaryCalendarEvent> calendarEvents) {
        this.calendarEvents = calendarEvents;
    }

    @Override
    public int compareTo(@NonNull NurseryClass o) {
        return 0;
    }

    public void addEvent(DiaryCalendarEvent event) {
        calendarEvents.add(event);
    }

    public void updateEvent(DiaryCalendarEvent event) {
        for (DiaryCalendarEvent aux : calendarEvents) {
            if (aux.getId().equals(event.getId())) {
                aux.setTitle(event.getTitle());
                aux.setDescription(event.getDescription());
                aux.setYear(event.getYear());
                aux.setMonth(event.getMonth());
                aux.setDay(event.getDay());
                break;
            }
        }
    }
}
