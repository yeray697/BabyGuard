package com.ncatz.babyguard.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yeray697 on 1/05/17.
 */

public class ChatMessage {
    @Exclude
    private boolean isMessageType;
    @Exclude
    private String key;
    private String teacher;
    private String kid;
    private String message;
    private String datetime;

    public ChatMessage() {
    }

    public ChatMessage(String teacher, String kid, String message, String datetime) {
        this.kid = kid;
        this.teacher = teacher;
        this.message = message;
        this.datetime = datetime;
        this.isMessageType = true;
    }

    @Exclude
    public boolean isMessage() {
        return isMessageType;
    }

    @Exclude
    public void setIfIsMessage(boolean isMessage) {
        this.isMessageType = isMessage;
    }

    public String getKey() {
        return key;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        this.isMessageType = true;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public static ChatMessage parseFromDataSnapshot(DataSnapshot dataSnapshot) {
        ChatMessage aux = new ChatMessage();
        for (Map.Entry<String, Object> entry:((HashMap<String,Object>) dataSnapshot.getValue()).entrySet()) {

            switch (entry.getKey()){
                case "datetime": //String (Long) Unix time
                    aux.setDatetime((String)entry.getValue());
                    break;
                case "message": //String message
                    aux.setMessage((String) entry.getValue());
                    break;
                case "kid": //String kid
                    aux.setKid((String) entry.getValue());
                    break;
                case "teacher": //String teacher
                    aux.setTeacher((String) entry.getValue());
                    break;
            }
        }
        aux.setKey(dataSnapshot.getKey());

        return aux;
    }

    public static Comparator<ChatMessage> comparator = new Comparator<ChatMessage>() {
        @Override
        public int compare(ChatMessage o1, ChatMessage o2) {
            return (int) (Long.parseLong(o1.getDatetime()) - Long.parseLong(o2.getDatetime()));
        }
    };
}
