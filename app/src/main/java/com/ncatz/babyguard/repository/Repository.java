package com.ncatz.babyguard.repository;

import android.support.annotation.IntDef;

import com.ncatz.babyguard.Home_Teacher_Activity;
import com.ncatz.babyguard.database.DatabaseHelper;
import com.ncatz.babyguard.model.Chat;
import com.ncatz.babyguard.model.ChatMessage;
import com.ncatz.babyguard.model.NurseryClass;
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
    private ArrayList<Chat> chats;

    public ArrayList<DiaryCalendarEvent> getCalendarByNursery(String nurseryId, String nurseryClassId) {
        ArrayList<DiaryCalendarEvent> calendar = new ArrayList<>();
        for (NurserySchool nursAux : nurserySchools) {
            if (nursAux.getId().equals(nurseryId)){
                for (Map.Entry<String,NurseryClass> aux: nursAux.getNurseryClasses().entrySet()){
                    if (aux.getKey().equals(nurseryClassId)) {
                        calendar = aux.getValue().getCalendar();
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

    public void setNurseryClass(String nursery_id, String nurseryClassId, NurseryClass nurseryClass) {
        if (nurserySchools == null)
            nurserySchools = new ArrayList<>();
        for (NurserySchool nursery:nurserySchools){
            if (nursery.getId().equals(nursery_id)){
                nursery.addNurseryClass(nurseryClassId, nurseryClass);
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

    public boolean addMessage(ChatMessage chatMessage, String idTo) {
        boolean result = false;
        if (chats == null)
            chats = new ArrayList<>();
        for (Chat chat : chats) {
            if (idTo.equals(chat.getId())){
                try {
                    DatabaseHelper.getInstance().addMessage(chatMessage);
                    chat.addMessage(chatMessage);
                    result = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return result;
    }

    public void addChat(Chat chat) {
        if (chats == null)
            chats = new ArrayList<>();
        chats.add(chat);
    }

    public String getTeacherChat(Kid kid) {
        String id = "";
        if (chats != null) {
            for (Chat chat : chats) {
                if (chat.getNursery().equals(kid.getId_nursery()) && chat.getNurseryClass().equals(kid.getId_nursery_class())){
                    id = chat.getId();
                    break;
                }
            }
        }
        return id;
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public void signOff() {
        user = null;
        nurserySchools = null;
        chats = null;
    }

    public Chat getChat(String userId) {
        Chat chat = null;
        for (Chat chatAux : chats){
            if (chatAux.getId().equals(userId)) {
                chat = chatAux;
                break;
            }
        }
        return chat;
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
        return (user==null || user.getKids() == null)?new ArrayList<Kid>():new ArrayList<>(user.getKids().values());
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

    public NurserySchool getNurserySchool(){
        return (nurserySchools != null && nurserySchools.size() > 0)?nurserySchools.get(0):null;
    }

    public void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
    }
}
