package com.ncatz.babyguard.model;

import com.google.firebase.database.DataSnapshot;
import com.ncatz.babyguard.R;
import com.ncatz.babyguard.preferences.SettingsManager;
import com.ncatz.babyguard.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yeray697 on 13/04/17.
 */

public class User {
    private String id;
    private String mail;
    private String id_nursery;
    private ArrayList<String> id_nursery_classes_teacher;
    private String img;
    private String phone_number;
    private String dbPass;
    private int user_type;
    private String name;
    private String fcmID;
    private HashMap<String, Kid> kids;

    public User() {
        id_nursery_classes_teacher = new ArrayList<>();
    }

    public String getDbPass() {
        return dbPass;
    }

    public void setDbPass(String dbPass) {
        this.dbPass = dbPass;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getUser_type() {
        return user_type;
    }

    public void setUser_type(int user_type) {
        this.user_type = user_type;
    }

    public HashMap<String, Kid> getKids() {
        return kids;
    }

    public void setKids(HashMap<String, Kid> kids) {
        if (this.kids == null)
            this.kids = new HashMap<>();
        for (Map.Entry<String, Kid> aux : kids.entrySet()) {
            this.kids.put(aux.getKey(), aux.getValue());
        }
    }

    public String getId_nursery() {
        return id_nursery;
    }

    public void setId_nursery(String id_nursery) {
        this.id_nursery = id_nursery;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static User parseFromDataSnapshot(DataSnapshot dataSnapshot) {

        User userAux = Repository.getInstance().getUser();
        if (userAux == null)
            userAux = new User();
        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();

        HashMap<String, Object> aux;
        for (HashMap.Entry<String, Object> entry : value.entrySet()) {
            aux = (HashMap<String, Object>) entry.getValue();
            userAux.id = String.valueOf(aux.get("id"));
            userAux.user_type = Integer.parseInt(String.valueOf(aux.get("user_type")));
            userAux.mail = String.valueOf(aux.get("mail"));
            userAux.img = String.valueOf(aux.get("img_profile"));
            userAux.phone_number = String.valueOf(aux.get("phone_number"));
            userAux.dbPass = String.valueOf(aux.get("db_pass"));
            userAux.name = String.valueOf(aux.get("name"));
            userAux.fcmID = String.valueOf(aux.get("fcmToken"));
            if (userAux.getUser_type() == UserCredentials.TYPE_TEACHER) {
                userAux.id_nursery = String.valueOf(aux.get("id_nursery"));
                userAux.id_nursery_classes_teacher = new ArrayList<>(((HashMap<String, String>) aux.get("classes")).values());
            }
            String phoneKey = SettingsManager.getKeyPreferenceByResourceId(R.string.profile_phone_pref);
            String nameKey = SettingsManager.getKeyPreferenceByResourceId(R.string.profile_name_pref);
            SettingsManager.setStringPreference(phoneKey, userAux.phone_number);
            SettingsManager.setStringPreference(nameKey, userAux.name);
            break;
        }

        return userAux;
    }

    public ArrayList<String> getIdNurseryClasses() {
        return id_nursery_classes_teacher;
    }

    public void setIdClassTeacher(ArrayList<String> ids) {
        id_nursery_classes_teacher = ids;
    }

    public String getFcmID() {
        return fcmID;
    }

    public void setFcmID(String fcmID) {
        this.fcmID = fcmID;
    }
}
