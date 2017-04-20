package com.ncatz.babyguard.repository;

import android.support.annotation.IntDef;

import com.ncatz.babyguard.model.TrackingKid;
import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.model.NurserySchool;
import com.ncatz.babyguard.model.User;
import com.yeray697.calendarview.DiaryCalendarEvent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Repository's singleton
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class Repository {
    private User user;
    private static Repository repository;
    private ArrayList<NurserySchool> nurserySchools;

    public ArrayList<DiaryCalendarEvent> getCalendarByUser(Kid kid) {
        ArrayList<DiaryCalendarEvent> calendar = new ArrayList<>();
        for (NurserySchool nursAux :
                nurserySchools) {
            if (nursAux.getId().equals(kid.getId_nursery())){
                for (Map.Entry<String,ArrayList<DiaryCalendarEvent>> aux: nursAux.getClassCalendars().entrySet()){
                    if (aux.getKey().equals(kid.getId_nursery_class())) {
                        calendar = aux.getValue();
                        break;
                    }
                }
                break;
            }
        }

        return calendar;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Kid getKidById(String id) {
        Kid kid = null;
        for (Kid kidAux :
                getKids()) {
            if (kidAux.getId().equals(id)){
                kid = kidAux;
                break;
            }
        }
        return kid;
    }

    public void setNursery(NurserySchool nursery) {
        boolean modified = false;
        if (nurserySchools != null) {
            for (NurserySchool nursAux :
                    nurserySchools) {
                if (nursAux.getId().equals(nursery.getId())) {
                    nursAux = nursery;
                    modified = true;
                    break;
                }
            }
        }
        if (!modified){
            if (nurserySchools == null)
                nurserySchools = new ArrayList<>();
            nurserySchools.add(nursery);
        }
    }

    public void setCalendar(String nursery_id, String nursery_class, ArrayList<DiaryCalendarEvent> calendar) {
        if (nurserySchools == null)
            nurserySchools = new ArrayList<>();
        for (NurserySchool nursery:nurserySchools){
            if (nursery.getId().equals(nursery_id)){
                nursery.addCalendar(nursery_class,calendar);
                break;
            }
        }
    }

    public void addTracking(String idKid, ArrayList<TrackingKid> trackingList) {
        for (Kid kid:getKids()){
            if (kid.getId().equals(idKid)){
                kid.setTracking(trackingList);
                break;
            }
        }
    }

    @IntDef({Sort.CHRONOLOGIC, Sort.CATEGORY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Sort {
        int CHRONOLOGIC = 1;
        int CATEGORY = 2;
    }

    private Repository() {

    }

    public static Repository getInstance(){
        if (repository == null) {
            repository = new Repository();
        }
        return repository;
    }

    /**
     * Get user's kids
     * @return UserCredentials's kids
     */
    public List<Kid> getKids() {
        return (user==null || user.getKids() == null)?new ArrayList<Kid>():user.getKids();
    }

    public ArrayList<TrackingKid> getOrderedInfoKid(ArrayList<TrackingKid> trackingKid, @Sort int sortType) {
        ArrayList<TrackingKid> newTrackingKid = new ArrayList<>(trackingKid);
        if (sortType == Sort.CATEGORY) {
            Collections.sort(newTrackingKid, TrackingKid.CATEGORY);
        } else {
            Collections.sort(newTrackingKid, TrackingKid.CHRONOLOGIC);
        }
        return newTrackingKid;
    }

    public NurserySchool getNurserySchoolById(String id){
        NurserySchool aux = null;
        for (NurserySchool nursAux :
                nurserySchools) {
            if (nursAux.getId().equals(id)){
                aux = nursAux;
                break;
            }
        }
        return aux;
    }

}
