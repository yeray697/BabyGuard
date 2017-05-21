package com.ncatz.babyguard.model;

import com.google.firebase.database.DataSnapshot;
import com.yeray697.calendarview.DiaryCalendarEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yeray697 on 26/12/16.
 */

public class NurserySchool {
    private String id;
    private String name;
    private String address;
    private String email;
    private String web;
    private ArrayList<String> telephone;
    private HashMap<String,ArrayList<DiaryCalendarEvent>> classCalendars;

    public NurserySchool(String id,String name, String address, String email, String web, ArrayList<String> telephone,HashMap<String,ArrayList<DiaryCalendarEvent>> classCalendars) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.web = web;
        this.telephone = telephone;
        this.classCalendars = classCalendars;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public ArrayList<String> getTelephone() {
        return telephone;
    }

    public void setTelephone(ArrayList<String> telephone) {
        this.telephone = telephone;
    }

    public HashMap<String,ArrayList<DiaryCalendarEvent>> getClassCalendars() {
        return classCalendars;
    }

    public void setClassCalendars(HashMap<String,ArrayList<DiaryCalendarEvent>> classCalendars) {
        this.classCalendars = classCalendars;
    }

    public static NurserySchool parseFromDataSnapshot(DataSnapshot dataSnapshot) {
        NurserySchool nurserySchool = null;
        if (dataSnapshot.exists()) {
            HashMap<String,Object> value = ((HashMap<String,Object>)dataSnapshot.getValue());
            HashMap<String,ArrayList<DiaryCalendarEvent>> classCalendars = new HashMap<>();
            nurserySchool = new NurserySchool(dataSnapshot.getKey(),
                    value.get("name").toString(),
                    value.get("address").toString(),
                    value.get("email").toString(),
                    value.get("web").toString(),
                    ((ArrayList)((HashMap<String,Object>)dataSnapshot.getValue()).get("telephone")),
                    classCalendars);
        }
        return nurserySchool;
    }

    public void addCalendar(String nursery_class, ArrayList<DiaryCalendarEvent> calendar) {
        if (classCalendars == null)
            classCalendars = new HashMap<>();
        classCalendars.put(nursery_class,calendar);
    }
}
