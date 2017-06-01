package com.ncatz.babyguard.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yeray697 on 1/05/17.
 */

public class ChatMessage {
    @Exclude
    private String key;
    private String sender;
    @Exclude
    private String receiver;
    private String message;
    private String datetime;

    public ChatMessage() {
    }

    public ChatMessage(String sender, String receiver, String message, String datetime) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.datetime = datetime;
    }

    public String getKey() {
        return key;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public static ChatMessage parseFromDataSnapshot(String idReceiver, DataSnapshot dataSnapshot) {
        ChatMessage aux = new ChatMessage();
        for (Map.Entry<String, Object> entry:((HashMap<String,Object>) dataSnapshot.getValue()).entrySet()) {

            switch (entry.getKey()){
                case "datetime": //String (Long) Unix time
                    aux.setDatetime((String)entry.getValue());
                    break;
                case "message": //String message
                    aux.setMessage((String) entry.getValue());
                    break;
                case "sender": //String id
                    aux.setSender((String) entry.getValue());
                    break;
            }
        }
        aux.setKey(dataSnapshot.getKey());
        aux.setReceiver(idReceiver);

        return aux;
    }
}
