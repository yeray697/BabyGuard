package com.ncatz.babyguard.model;

import com.google.firebase.database.DataSnapshot;
import com.ncatz.yeray.calendarview.DiaryCalendarEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Nursery School POJO class
 */

public class NurserySchool {
    private String id;
    private String name;
    private String address;
    private String email;
    private String web;
    private ArrayList<String> telephone;
    private HashMap<String, NurseryClass> nurseryClasses;

    public NurserySchool(String id, String name, String address, String email, String web, ArrayList<String> telephone, HashMap<String, NurseryClass> nurseryClasses) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.web = web;
        this.telephone = telephone;
        this.nurseryClasses = nurseryClasses;
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

    public HashMap<String, NurseryClass> getNurseryClasses() {
        return nurseryClasses;
    }

    public ArrayList<NurseryClass> getNurseryClassesList() {
        ArrayList<NurseryClass> classes = new ArrayList<>(nurseryClasses.values());
        Collections.sort(classes, NurseryClass.comparatorName);
        return classes;
    }

    public void setNurseryClasses(HashMap<String, NurseryClass> nurseryClasses) {
        this.nurseryClasses = nurseryClasses;
    }

    public static NurserySchool parseFromDataSnapshot(DataSnapshot dataSnapshot) {
        NurserySchool nurserySchool = null;
        if (dataSnapshot.exists()) {
            HashMap<String, Object> value = ((HashMap<String, Object>) dataSnapshot.getValue());
            HashMap<String, NurseryClass> nurseryClass = new HashMap<>();
            nurserySchool = new NurserySchool(dataSnapshot.getKey(),
                    value.get("name").toString(),
                    value.get("address").toString(),
                    value.get("email").toString(),
                    value.get("web").toString(),
                    ((ArrayList) ((HashMap<String, Object>) dataSnapshot.getValue()).get("telephone")),
                    nurseryClass);
        }
        return nurserySchool;
    }

    public void addNurseryClasses(String nursery_class, NurseryClass nurseryClass) {
        if (nurseryClasses == null)
            nurseryClasses = new HashMap<>();
        nurseryClasses.put(nursery_class, nurseryClass);
    }

    public void addNurseryClass(String nurseryClassId, NurseryClass nurseryClass) {
        if (nurseryClasses == null)
            nurseryClasses = new HashMap<>();
        nurseryClasses.put(nurseryClassId, nurseryClass);
    }

    public DiaryCalendarEvent removeEvent(String classId, String eventId) {
        DiaryCalendarEvent result = null;

        int count;
        ArrayList<DiaryCalendarEvent> events;
        for (Map.Entry<String, NurseryClass> classAux : nurseryClasses.entrySet()) {
            if (classAux.getKey().equals(classId)) {
                count = 0;
                events = classAux.getValue().getCalendar();
                for (DiaryCalendarEvent eventAux : classAux.getValue().getCalendar()) {
                    if (eventAux.getId().equals(eventId)) {
                        result = eventAux;
                        events.remove(count);
                        break;
                    }
                    count++;
                }
                break;
            }
        }
        return result;
    }
}
