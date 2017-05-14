package com.ncatz.babyguard.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yeray697 on 1/05/17.
 */

public class ChatMessage {
    private String sender;
    private String message;
    private String date;
    private String time;

    public ChatMessage() {
    }

    public ChatMessage(String sender, String message, String date, String time) {
        this.sender = sender;
        this.message = message;
        this.date = date;
        this.time = time;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static ChatMessage parseFromDataSnapshot(DataSnapshot dataSnapshot) {
        ChatMessage aux = new ChatMessage();
        for (Map.Entry<String, Object> entry:((HashMap<String,Object>) dataSnapshot.getValue()).entrySet()) {
            Log.d("","");
            switch (entry.getKey()){
                case "datetime": //String (Long) Unix time
                    Long timeUnix = Long.parseLong((String) entry.getValue());
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(timeUnix);
                    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
                    SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
                    String date = sdfDate.format(c.getTime());
                    String time = sdfTime.format(c.getTime());
                    aux.setDate(date);
                    aux.setTime(time);
                    break;
                case "message": //String message
                    aux.setMessage((String) entry.getValue());
                    break;
                case "sender": //String id
                    aux.setSender((String) entry.getValue());
                    break;
                case "received": //boolean
                    break;
            }
        }

        return aux;
    }
}
