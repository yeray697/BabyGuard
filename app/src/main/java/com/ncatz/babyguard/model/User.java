package com.ncatz.babyguard.model;

import com.google.firebase.database.DataSnapshot;
import com.ncatz.babyguard.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yeray697 on 13/04/17.
 */

public class User {
    private String id;
    private boolean deleted;
    private String mail;
    private String id_nursery;
    private ArrayList<String> id_nursery_classes_teacher;
    private String img;
    private String phone_number;
    private String dbPass;
    private int user_type;
    private HashMap<String,Kid> kids;

    public User(){
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

    public HashMap<String,Kid> getKids() {
        return kids;
    }

    public void setKids(HashMap<String,Kid> kids) {
        if (this.kids == null)
            this.kids = new HashMap<>();
        for (Map.Entry<String, Kid> aux : kids.entrySet()) {
            this.kids.put(aux.getKey(),aux.getValue());
        }
    }

    public String getId_nursery() {
        return id_nursery;
    }

    public void setId_nursery(String id_nursery) {
        this.id_nursery = id_nursery;
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
            //userAux.setDeleted((Boolean) aux.get("deleted"));
            userAux.setImg(String.valueOf(aux.get("img")));
            userAux.setPhone_number(String.valueOf(aux.get("phone_number")));
            userAux.setDbPass(String.valueOf(aux.get("db_pass")));
            if (userAux.getUser_type()==UserCredentials.TYPE_TEACHER) {
                userAux.setId_nursery(String.valueOf(aux.get("id_nursery")));
                userAux.setIdClassTeacher(new ArrayList<>(((HashMap<String,String>) aux.get("classes")).values()));
            }
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
}
