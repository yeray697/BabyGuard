package com.ncatz.babyguard;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.ncatz.babyguard.database.DatabaseHelper;
import com.ncatz.babyguard.firebase.FirebaseListeners;
import com.ncatz.babyguard.firebase.FirebaseManager;
import com.ncatz.babyguard.model.Chat;
import com.ncatz.babyguard.model.ChatMessage;
import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.model.NurserySchool;
import com.ncatz.babyguard.model.TrackingKid;
import com.ncatz.babyguard.model.User;
import com.ncatz.babyguard.model.UserCredentials;
import com.ncatz.babyguard.repository.Repository;
import com.yeray697.calendarview.DiaryCalendarEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Context application
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class Babyguard_Application extends Application {

    private final static String FILE_PREFERENCE = "credentials";
    private final static String USER_PREFERENCE = "userCredentials";
    private final static String PASS_PREFERENCE = "pass";
    private final static String TYPE_PREFERENCE = "type";

    private UserCredentials userCredentials;
    private SharedPreferences pref;
    private static Context context;

    private ActionEndListener nurseryListener;
    private ActionEndListener kidListListener;
    private ActionEndListener trackingListener;
    private ActionEndListener calendarListener;
    private ActionEndListener chatListener;

    boolean nurseryAndChatsLoadedFirstTime;
    boolean kidsInfoLoadedFirstTime;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        pref = getApplicationContext().getSharedPreferences(FILE_PREFERENCE, MODE_PRIVATE);
        FirebaseManager.getInstance().setListeners(firebaseListeners);
    }

    public static Context getContext(){
        return context;
    }

    /**
     * Set userCredentials credentials.
     * If userCredentials is null, it cleans preferences
     * @param userCredentials
     */
    public void setUserCredentials(UserCredentials userCredentials){
        this.userCredentials = userCredentials;
        if (userCredentials != null) {
            pref.edit().putString(USER_PREFERENCE, userCredentials.getUser()).apply();
            pref.edit().putString(PASS_PREFERENCE, userCredentials.getPass()).apply();
            pref.edit().putInt(TYPE_PREFERENCE, userCredentials.getType()).apply();
        } else {
            pref.edit().clear().apply();
        }
    }

    public void signOff() {
        FirebaseManager.getInstance().close();
        Repository.getInstance().signOff();
        setUserCredentials(null);
    }

    /**
     * Get userCredentials's credentials
     * @return Return the userCredentials
     */
    public UserCredentials getUserCredentials(){
        String name;
        String pass;
        int type;
        if (this.userCredentials == null) {
            name = getUserIfExists();
            if (!TextUtils.isEmpty(name)) {
                pass = getPassIfExists();
                if (!TextUtils.isEmpty(pass)) {
                    type = getTypeIfExists();
                    this.userCredentials = new UserCredentials(name, pass, type);
                }
            }
        }
        return this.userCredentials;
    }

    /**
     * Get the userCredentials's name is exists
     * @return Return the userCredentials's name
     */
    private String getUserIfExists() {
        return pref.getString("userCredentials",null);
    }

    /**
     * Get the userCredentials's pass is exists
     * @return Return the userCredentials's pass
     */
    private String getPassIfExists() {
        return pref.getString("pass",null);
    }

    /**
     * Get the userCredentials's pass is exists
     * @return Return the userCredentials's pass
     */
    private int getTypeIfExists() {
        return pref.getInt("type",-1);
    }

    public boolean isTeacher() {
        return userCredentials.getType() == UserCredentials.TYPE_TEACHER;
    }

    public void addNurseryListener(ActionEndListener nurseryListener) {
        this.nurseryListener = nurseryListener;
    }

    public void removeNurseryListener() {
        nurseryListener = null;
    }

    public void addChatListener(ActionEndListener chatListener) {
        this.chatListener = chatListener;
    }

    public void removeChatListener() {
        chatListener = null;
    }

    public void addKidListListener(ActionEndListener kidListListener) {
        this.kidListListener = kidListListener;
    }

    public void removeKidListListener() {
        kidListListener = null;
    }

    public interface ActionEndListener {
        void onEnd();
    }

    public void addTrackingListener(ActionEndListener trackingListener) {
        this.trackingListener = trackingListener;
    }

    public void removeTrackingListener() {
        trackingListener = null;
    }

    public void addCalendarListener(ActionEndListener calendarListener) {
        this.calendarListener = calendarListener;
    }

    public void removeCalendarListener() {
        calendarListener = null;
    }

    private FirebaseListeners firebaseListeners = new FirebaseListeners() {

        @Override
        public void onUserModified(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                User user = User.parseFromDataSnapshot(dataSnapshot);
                DatabaseHelper.getInstance(user.getId()).setPassword(user.getDbPass());
                Repository.getInstance().setUser(user);
                //if (!kidsInfoLoadedFirstTime) {
                    FirebaseManager.getInstance().getKidsInfo(user.getId());
                    kidsInfoLoadedFirstTime = true;
                //}
                if (kidListListener != null)
                    kidListListener.onEnd();
                if (trackingListener != null)
                    trackingListener.onEnd();
                if (calendarListener != null)
                    calendarListener.onEnd();
            }
        }

        @Override
        public void onUserCancelled(DatabaseError databaseError) {

        }

        @Override
        public void onKidModified(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                HashMap<String, HashMap<String, Object>> value = (HashMap<String, HashMap<String, Object>>) dataSnapshot.getValue();
                List<Kid> kids = Kid.parseFromDataSnapshot(value);
                List<String> aux;
                if (!nurseryAndChatsLoadedFirstTime && kids.size() > 0) {
                    if (kids.size() > 1){
                        HashMap<String,List<String>> nurseryClasses = new HashMap<>();
                        String id_nursery;
                        String id_class;
                        for (int i = 0; i < kids.size(); i++) {
                            id_nursery = kids.get(i).getId_nursery();
                            id_class = kids.get(i).getId_nursery_class();
                            if (nurseryClasses.containsKey(id_nursery)){
                                aux = nurseryClasses.get(id_nursery);
                                if (!aux.contains(id_class)){
                                    aux.add(id_class);
                                    nurseryClasses.put(id_nursery,aux);
                                }
                            } else {
                                aux = new ArrayList<>();
                                aux.add(id_class);
                                nurseryClasses.put(id_nursery,aux);
                            }
                        }
                        for (HashMap.Entry<String,List<String>> entry:nurseryClasses.entrySet()){
                            FirebaseManager.getInstance().getNursery(entry.getKey());
                            for (String classId: entry.getValue()){
                                FirebaseManager.getInstance().getNurseryClass(entry.getKey(),classId);
                                FirebaseManager.getInstance().getChatNames(entry.getKey(),classId);
                            }
                        }
                        for (int i = 0; i < kids.size(); i++)
                                FirebaseManager.getInstance().getChat(kids.get(i).getId());
                    } else {
                        Kid kidAux = kids.get(0);
                        FirebaseManager.getInstance().getNursery(kidAux.getId_nursery());
                        FirebaseManager.getInstance().getNurseryClass(kidAux.getId_nursery(),kidAux.getId_nursery_class());
                        FirebaseManager.getInstance().getChatNames(kidAux.getId_nursery(),kidAux.getId_nursery_class());
                        FirebaseManager.getInstance().getChat(kidAux.getId());
                    }
                    nurseryAndChatsLoadedFirstTime = true;
                }
                Repository.getInstance().getUser().setKids(kids);
                if (kidListListener != null)
                    kidListListener.onEnd();
                if (trackingListener != null)
                    trackingListener.onEnd();
                if (calendarListener != null)
                    calendarListener.onEnd();
            }
        }

        @Override
        public void onKidCancelled(DatabaseError databaseError) {

        }

        @Override
        public void onNurseryModified(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                Repository.getInstance().setNursery(NurserySchool.parseFromDataSnapshot(dataSnapshot));
                if (nurseryListener != null)
                    nurseryListener.onEnd();
            }
        }

        @Override
        public void onNurseryCancelled(DatabaseError databaseError) {

        }

        @Override
        public void onNurseryClassModified(DataSnapshot dataSnapshot) {
            ArrayList<DiaryCalendarEvent> calendarListAux = new ArrayList<>();
            String nursery_id = (String) ((HashMap<String, Object>)dataSnapshot.getValue()).get("id_nursery"),
            nursery_class = dataSnapshot.getKey();
            HashMap<String,HashMap<String,String>> calendar = (HashMap<String,HashMap<String, String>>) ((HashMap<String, Object>)dataSnapshot.getValue()).get("calendar");
            DiaryCalendarEvent calendarAux = null;
            for (Map.Entry<String, HashMap<String, String>> entry:calendar.entrySet()){
                calendarAux = new DiaryCalendarEvent(entry.getValue().get("title"),
                        Integer.valueOf(entry.getValue().get("year")),
                        Integer.valueOf(entry.getValue().get("month")),
                        Integer.valueOf(entry.getValue().get("day")),
                        entry.getValue().get("description"));
                calendarListAux.add(calendarAux);
            }
            Repository.getInstance().setCalendar(nursery_id,nursery_class,calendarListAux);
            if (calendarListener != null)
                calendarListener.onEnd();
        }

        @Override
        public void onNurseryClassCancelled(DatabaseError databaseError) {

        }

        @Override
        public void onTrackingModified(DataSnapshot dataSnapshot) {

            ArrayList<TrackingKid> trackingList = new ArrayList<>();
            TrackingKid trackingAux;
            if (dataSnapshot.getValue() != null) {
                ArrayList<HashMap<String, String>> kids = (ArrayList<HashMap<String, String>>) dataSnapshot.getValue();
                String idKid = dataSnapshot.getKey();
                for (HashMap<String, String> kid:kids){
                        trackingAux = new TrackingKid("",kid.get("title"),
                                kid.get("time"),
                                TrackingKid.parseIntToType(Integer.parseInt(kid.get("type"))),
                                kid.get("description"));
                        trackingList.add(trackingAux);
                }
                Repository.getInstance().addTracking(idKid,trackingList);
                if (trackingListener != null)
                    trackingListener.onEnd();
            }
        }

        @Override
        public void onTrackingCancelled(DatabaseError databaseError) {

        }

        @Override
        public void onChatAdded(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                try {
                    String id = dataSnapshot.getRef().getParent().getKey();
                    ChatMessage chatMessage = ChatMessage.parseFromDataSnapshot(id,dataSnapshot);
                    if (Repository.getInstance().addMessage(chatMessage,chatMessage.getSender())) {
                        FirebaseManager.getInstance().deleteMessage(id,chatMessage.getKey());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (chatListener != null)
                    chatListener.onEnd();
            }
        }

        @Override
        public void onChatCancelled(DatabaseError databaseError) {

        }

        @Override
        public void onChatNameChanged(DataSnapshot dataSnapshot) {
            Chat chat;
            for (HashMap.Entry<String,HashMap<String ,Object>> chatData : ((HashMap<String,HashMap<String ,Object>>)dataSnapshot.getValue()).entrySet()) {
                chat = Chat.parseFromDataSnapshot(chatData);
                Repository.getInstance().addChat(chat);
            }
            try {
                DatabaseHelper.getInstance().loadChatMessages("124");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (chatListener != null)
                chatListener.onEnd();
        }

        @Override
        public void onChatNameError(DatabaseError databaseError) {

        }
    };
}
