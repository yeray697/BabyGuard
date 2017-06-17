package com.ncatz.babyguard.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yeray697 on 1/05/17.
 */

public class Chat {
    private String id;
    private String name;
    private String photo;
    private String nursery;
    private String nurseryClass;
    private String fcmToken;
    private ArrayList<ChatMessage> messages;

    public Chat() {
    }

    public Chat(String id, String name, String photo, String nursery, String nurseryClass, String fcmToken, ArrayList<ChatMessage> messages) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.nursery = nursery;
        this.nurseryClass = nurseryClass;
        this.fcmToken = fcmToken;
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

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void addMessage(ChatMessage chatMessage) {
        if (messages == null)
            messages = new ArrayList<>();
        if (chatMessage != null)
            messages.add(chatMessage);
    }

    public void addMessage(List<ChatMessage> chatMessages) {
        if (messages == null)
            messages = new ArrayList<>();
        if (chatMessages != null) {
            for (ChatMessage aux : chatMessages)
                messages.add(aux);
        }
    }

    public ChatMessage getLastMessage() {
        return (messages != null && messages.size() > 0) ? messages.get(messages.size() - 1) : null;
    }

    public static Comparator<Chat> comparator = new Comparator<Chat>() {
        @Override
        public int compare(Chat c1, Chat c2) {
            int result = 0;
            ChatMessage message1 = c1.getLastMessage();
            ChatMessage message2 = c2.getLastMessage();
            if (message1 == null && message2 == null) {
                //Order alphabetical
                result = c1.name.compareToIgnoreCase(c2.name);
            } else if (message1 == null) {
                result = 1;
            } else if (message2 == null) {
                result = -1;
            } else { //Both are !null
                //Order by date
                result = (int) (Long.valueOf(message2.getDatetime()) - Long.valueOf(message1.getDatetime()));
                if (result == 0) {
                    result = c1.name.compareToIgnoreCase(c2.name);
                }
            }
            return result;
        }
    };


    public static Chat parseFromDataSnapshot(HashMap<String, Object> dataSnapshot) {
        Chat chat = null;
        chat = new Chat((String) dataSnapshot.get("id"),
                (String) dataSnapshot.get("name"),
                (String) dataSnapshot.get("img_profile"),
                (String) dataSnapshot.get("id_nursery"),
                (String) dataSnapshot.get("id_nursery_class"),
                (String) dataSnapshot.get("fcmToken"),
                new ArrayList<ChatMessage>());

        return chat;
    }

    public static Chat duplicate(Chat chat) {
        return new Chat(chat.id, chat.name, chat.photo, chat.nursery, chat.nurseryClass, chat.fcmToken, new ArrayList<ChatMessage>());
    }
}
