package com.ncatz.babyguard.model;

/**
 * Created by yeray697 on 31/05/17.
 */

public class ChatKeyMap {
    private String teacherId;
    private String kidId;

    public ChatKeyMap() {
    }

    public ChatKeyMap(String teacherId, String kidId) {
        this.teacherId = teacherId;
        this.kidId = kidId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getKidId() {
        return kidId;
    }

    public void setKidId(String kidId) {
        this.kidId = kidId;
    }

    @Override
    public boolean equals(Object obj) {
        return teacherId.equals(((ChatKeyMap) obj).teacherId) && kidId.equals(((ChatKeyMap) obj).kidId);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + kidId.hashCode();
        result = 31 * result + teacherId.hashCode();
        return result;
    }
}
