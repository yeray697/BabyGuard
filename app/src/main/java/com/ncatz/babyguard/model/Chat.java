package com.ncatz.babyguard.model;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yeray697 on 1/05/17.
 */

public class Chat {
    private String id;
    private String name;
    private String photo;
    private String nursery;
    private String nurseryClass;
    private ArrayList<ChatMessage> messages;

    public Chat() {
    }

    public Chat(String id, String name, String photo, String nursery, String nurseryClass, ArrayList<ChatMessage> messages) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.nursery = nursery;
        this.nurseryClass = nurseryClass;
        this.messages = messages;
    }

    public String getNurseryClass() {
        return nurseryClass;
    }

    public void setNurseryClass(String nurseryClass) {
        this.nurseryClass = nurseryClass;
    }

    public String getNursery() {
        return nursery;
    }

    public void setNursery(String nursery) {
        this.nursery = nursery;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void addMessage(ChatMessage chatMessage) {
        if (messages == null)
            messages = new ArrayList<>();
        if (chatMessage != null)
            messages.add(chatMessage);
    }

    public static Chat parseFromDataSnapshot(Map.Entry<String, HashMap<String, Object>> dataSnapshot) {
        HashMap<String,Object> chatData = dataSnapshot.getValue();
        Chat chat = null;
        if (!((boolean)chatData.get("deleted"))) {
            chat = new Chat((String) chatData.get("id"),
                    (String) chatData.get("name"),
                    (String) chatData.get("img"),
                    (String) chatData.get("id_nursery"),
                    (String) chatData.get("id_nursery_class"),
                    new ArrayList<ChatMessage>());
        }
        return chat;
    }
}
