package com.ncatz.babyguard.model;

import com.google.firebase.database.DataSnapshot;
import com.ncatz.babyguard.firebase.FirebaseManager;
import com.ncatz.babyguard.repository.Repository;
import com.yeray697.calendarview.DiaryCalendarEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yeray697 on 13/04/17.
 */

public class User {
    private String id;
    private boolean deleted;
    private String mail;
    private String id_nursery;
    private String id_nursery_class;
    private String img;
    private String phone_number;
    private String dbPass;
    private int user_type;
    private List<Kid> kids;

    public User(){

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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public List<Kid> getKids() {
        return kids;
    }

    public void setKids(List<Kid> kids) {
        this.kids = kids;
    }

    public String getId_nursery() {
        return id_nursery;
    }

    public void setId_nursery(String id_nursery) {
        this.id_nursery = id_nursery;
    }

    public String getId_nursery_class() {
        return id_nursery_class;
    }

    public void setId_nursery_class(String id_nursery_class) {
        this.id_nursery_class = id_nursery_class;
    }

    public static User parseFromDataSnapshot(DataSnapshot dataSnapshot) {

        User userAux = Repository.getInstance().getUser();
        if (userAux == null)
            userAux = new User();
        HashMap<String,Object> value = (HashMap<String, Object>) dataSnapshot.getValue();

        HashMap<String, Object> aux;
        for (HashMap.Entry<String,Object> entry : value.entrySet()) {
            aux = (HashMap<String, Object>) entry.getValue();
            userAux.setId(String.valueOf(aux.get("id")));
            userAux.setUser_type(Integer.parseInt(String.valueOf(aux.get("user_type"))));
            userAux.setMail(String.valueOf(aux.get("mail")));
            userAux.setDeleted((Boolean) aux.get("deleted"));
            userAux.setImg(String.valueOf(aux.get("img")));
            userAux.setPhone_number(String.valueOf(aux.get("phone_number")));
            userAux.setDbPass(String.valueOf(aux.get("db_pass")));
            if (userAux.getUser_type()==UserCredentials.TYPE_TEACHER) {
                userAux.setId_nursery(String.valueOf(aux.get("id_nursery")));
                userAux.setId_nursery_class(String.valueOf(aux.get("id_nursery_class")));
            }
            break;
        }

        return userAux;
    }
}
