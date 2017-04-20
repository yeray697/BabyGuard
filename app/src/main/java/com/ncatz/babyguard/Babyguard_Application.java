package com.ncatz.babyguard;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.ncatz.babyguard.firebase.FirebaseListeners;
import com.ncatz.babyguard.firebase.FirebaseManager;
import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.model.NurserySchool;
import com.ncatz.babyguard.model.TrackingKid;
import com.ncatz.babyguard.model.User;
import com.ncatz.babyguard.model.UserCredentials;
import com.ncatz.babyguard.presenter.KidListPresenterImpl;
import com.ncatz.babyguard.repository.Repository;
import com.yeray697.calendarview.DiaryCalendarEvent;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private static String token;

    private ActionEndListener kidListListener;
    private ActionEndListener trackingListener;
    private ActionEndListener calendarListener;

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

    public static String getToken() {
        return token;
    }

    public static String getTokenAuth() {
        return "Bearer " + token;
    }

    public static void setToken(String token) {
        Babyguard_Application.token = token;
    }

    public boolean isTeacher() {
        return userCredentials.getType() == UserCredentials.TYPE_TEACHER;
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
                Repository.getInstance().setUser(user);
                FirebaseManager.getInstance().setUserId(user.getId());
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
                if (kids.size() > 0) {
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
                            FirebaseManager.getInstance().setNursery(entry.getKey());
                            for (String classId: entry.getValue()){
                                FirebaseManager.getInstance().setNurseryClass(entry.getKey(),classId);
                            }
                        }
                    } else {
                        Kid kidAux = kids.get(0);
                        FirebaseManager.getInstance().setNursery(kidAux.getId_nursery());
                        FirebaseManager.getInstance().setNurseryClass(kidAux.getId_nursery(),kidAux.getId_nursery_class());

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
        }

        @Override
        public void onKidCancelled(DatabaseError databaseError) {

        }

        @Override
        public void onNurseryModified(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                Repository.getInstance().setNursery(NurserySchool.parseFromDataSnapshot(dataSnapshot));
                if (kidListListener != null)
                    kidListListener.onEnd();
                if (trackingListener != null)
                    trackingListener.onEnd();
                if (calendarListener != null)
                    calendarListener.onEnd();
            }
        }

        @Override
        public void onNurseryCancelled(DatabaseError databaseError) {

        }

        @Override
        public void onNurseryClassModified(DataSnapshot dataSnapshot) {
            ArrayList<DiaryCalendarEvent> calendarListAux = new ArrayList<>();
            String nursery_id = (String) ((HashMap<String, Object>)dataSnapshot.getValue()).get("nursery_id"),
                    nursery_class = dataSnapshot.getKey();
            ArrayList<HashMap<String,String>> calendar = (ArrayList<HashMap<String, String>>) ((HashMap<String, Object>)dataSnapshot.getValue()).get("calendar");
            DiaryCalendarEvent calendarAux;
            for (HashMap<String,String> entry:calendar){
                calendarAux = new DiaryCalendarEvent(entry.get("title"),
                        Integer.valueOf(entry.get("year")),
                        Integer.valueOf(entry.get("month")),
                        Integer.valueOf(entry.get("day")),
                        entry.get("description"));
                calendarListAux.add(calendarAux);
            }
            Repository.getInstance().setCalendar(nursery_id,nursery_class,calendarListAux);
        }

        @Override
        public void onNurseryClassCancelled(DatabaseError databaseError) {

        }

        @Override
        public void onTrackingModified(DataSnapshot dataSnapshot) {

            ArrayList<TrackingKid> trackingList = new ArrayList<>();
            TrackingKid trackingAux;
            HashMap<String,ArrayList<HashMap<String,String>>> kids = (HashMap<String, ArrayList<HashMap<String, String>>>) dataSnapshot.getValue();
            String idKid = "";
            for (HashMap.Entry<String,ArrayList<HashMap<String,String>>> kid:kids.entrySet()){
                idKid = kid.getKey();
                for (HashMap<String, String> trackingListEntry: kid.getValue()) {
                    trackingAux = new TrackingKid("",trackingListEntry.get("title"),
                            trackingListEntry.get("time"),
                            TrackingKid.parseIntToType(Integer.parseInt(trackingListEntry.get("type"))),
                            trackingListEntry.get("description"));
                    trackingList.add(trackingAux);
                }
            }
            Repository.getInstance().addTracking(idKid,trackingList);
        }

        @Override
        public void onTrackingCancelled(DatabaseError databaseError) {

        }
    };
}
