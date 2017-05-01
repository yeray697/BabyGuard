package com.ncatz.babyguard.model;

import java.util.ArrayList;

/**
 * Created by yeray697 on 1/05/17.
 */

public class Chat {
    private String name;
    private String photo;
    private ArrayList<ChatMessage> messages;

    public Chat() {
    }

    public Chat(String name, String photo, ArrayList<ChatMessage> messages) {
        this.name = name;
        this.photo = photo;
        this.messages = messages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public ArrayList<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<ChatMessage> messages) {
        this.messages = messages;
    }
}
