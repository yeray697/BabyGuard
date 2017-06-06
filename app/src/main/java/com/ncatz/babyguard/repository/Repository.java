package com.ncatz.babyguard.repository;

import android.support.annotation.IntDef;
import android.util.Log;

import com.ncatz.babyguard.Babyguard_Application;
import com.ncatz.babyguard.database.DatabaseHelper;
import com.ncatz.babyguard.model.Chat;
import com.ncatz.babyguard.model.ChatKeyMap;
import com.ncatz.babyguard.model.ChatMessage;
import com.ncatz.babyguard.model.NurseryClass;
import com.ncatz.babyguard.model.TrackingKid;
import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.model.NurserySchool;
import com.ncatz.babyguard.model.User;
import com.ncatz.yeray.calendarview.DiaryCalendarEvent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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
    private HashMap<ChatKeyMap,Chat> chats;
    private int parentChats;

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

    public boolean addMessage(ChatMessage chatMessage) {
        boolean result = false;
        String idFrom;
        String idTo;
        if ((Babyguard_Application.isTeacher())) {
            idFrom = chatMessage.getTeacher();
            idTo = chatMessage.getKid();
        }  else {
            idTo = chatMessage.getTeacher();
            idFrom = chatMessage.getKid();
        }
        if (chats == null)
            chats = new HashMap<>();
        for (Map.Entry<ChatKeyMap, Chat> chat : chats.entrySet()) {
            if (idTo.equals(chat.getKey().getKidId()) && idFrom.equals(chat.getKey().getTeacherId()) ||
                    idFrom.equals(chat.getKey().getKidId()) && idTo.equals(chat.getKey().getTeacherId())){
                try {
                    DatabaseHelper.getInstance().addMessage(chatMessage);
                    chat.getValue().addMessage(chatMessage);
                    result = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if (!result) {
            Chat aux = new Chat();
            aux.setId(idTo);
            aux.addMessage(chatMessage);
            addChat(new ChatKeyMap(chatMessage.getTeacher(),chatMessage.getKey()), Chat.duplicate(aux));
            result = true;
        }
        return result;
    }

    //Is teacher account
    public void addChat(ChatKeyMap key, Chat chat) {
        if (chats == null)
            chats = new HashMap<>();
        chats.put(key,chat);
    }

    //This method is called only if user is a parent
    //ChatKeyMap entry was added previously, but with chat = null
    //So, if a teacher gives class to x>1 kids from the same parent, this method fill with the info: name, img, ...
    public void addChat(Chat chat) {
        if (chats == null)
            chats = new HashMap<>();
        String idTeacher = chat.getId();
        Chat tmp;
        for (Map.Entry<ChatKeyMap, Chat> aux :
                chats.entrySet()) {
            if (aux.getKey().getTeacherId().equals(idTeacher)) {
                tmp = chats.get(aux.getKey());
                if (tmp != null && tmp.getMessages() != null && tmp.getMessages().size() > 0){
                    chat.addMessage(tmp.getMessages());
                }
                chats.put(aux.getKey(),Chat.duplicate(chat));
            }
        }
    }

    public ArrayList<Chat> getChatByKidId(String kidId) {
        ArrayList<Chat> aux = new ArrayList<>();
        for (Map.Entry<ChatKeyMap, Chat> chatAux: chats.entrySet()) {
            if (chatAux.getKey().getKidId().equals(kidId))
                aux.add(chatAux.getValue());
        }
        return aux;
    }

    public HashMap<ChatKeyMap, Chat> getChats() {
        return chats;
    }

    public ArrayList<Chat> getChats(String classId) {
        ArrayList<Chat> aux = new ArrayList<>();
        for (Map.Entry<ChatKeyMap, Chat> chatAux: chats.entrySet()) {
            try {
                if (chatAux.getValue().getNurseryClass().equals(classId))
                    aux.add(chatAux.getValue());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return aux;
    }

    public void signOff() {
        user = null;
        nurserySchools = null;
        chats = null;
        parentChats = 0;
    }

    public Chat getChat(String kidId,String teacherId) {
        Chat chat = null;
        for (Map.Entry<ChatKeyMap, Chat> chatAux : chats.entrySet()){
            if (chatAux.getKey().getKidId().equals(kidId) && chatAux.getKey().getTeacherId().equals(teacherId)) {
                chat = chatAux.getValue();
                break;
            }
        }
        return chat;
    }

    public void addKeyChat(ChatKeyMap key) {
        if (chats == null)
            chats = new HashMap<>();
        chats.put(key,null);
    }

    /*public void addMessage(ChatMessage chatMessage) {
        boolean result = false;
        if (chats == null)
            chats = new HashMap<>();
        boolean isKid = false;
        boolean isTeacher = false;
        for (Map.Entry<ChatKeyMap, Chat> chat : chats.entrySet()) {
            isTeacher = chatMessage.getTeacher().equals(chat.getValue().getId());
            isKid = chatMessage.getKid().equals(chat.getValue().getId());
            if (isTeacher || isKid){
                try {
                    DatabaseHelper.getInstance().addMessage(chatMessage);
                    chat.getValue().addMessage(chatMessage);
                    result = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if (!result) {
            Chat aux = new Chat();
            String idTo;
            if (isTeacher) {
                idTo = chatMessage.getTeacher();
            } else { //isKid
                idTo = chatMessage.getKid();
            }
            aux.setId(idTo);
            aux.addMessage(chatMessage);
            addChat(new ChatKeyMap(chatMessage.getTeacher(),chatMessage.getKid()), Chat.duplicate(aux));
            result = true;
        }
    }*/

    public void setParentChats(int parentChats) {
        this.parentChats = parentChats;
    }
    public int getParentChats() {
        return parentChats;
    }
    public int decreaseParentChats() {
        parentChats -= 1;
        return parentChats;
    }

    public ArrayList<Kid> getKidsByClass(String classId) {
        ArrayList<Kid> kidArrayList = new ArrayList<>();
        for (Map.Entry<String, Kid> aux: user.getKids().entrySet()) {
            if(aux.getValue().getId_nursery_class().equals(classId)) {
                kidArrayList.add(aux.getValue());
            }
        }
        return kidArrayList;
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
}
