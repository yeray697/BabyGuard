package com.ncatz.babyguard.firebase;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ncatz.babyguard.Babyguard_Application;
import com.ncatz.babyguard.model.ChatKeyMap;
import com.ncatz.babyguard.model.ChatMessage;
import com.ncatz.babyguard.model.User;
import com.ncatz.babyguard.repository.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yeray697 on 12/04/17.
 */

public class FirebaseManager {
    private static final String KID_REFERENCE = "kid";
    private static final String NURSERY_REFERENCE = "nursery";
    private static final String USER_REFERENCE = "user";
    private static final String TRACKING_REFERENCE = "tracking";
    private static final String NURSERY_CLASS_REFERENCE = "nursery_class";
    private static final String TEACHER_REFERENCE = "teacher";
    private static final String CHAT_REFERENCE = "chat";
    private static final String CHAT_PARENT_REFERENCE = "chat_room_parent";

    private static FirebaseManager instance;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private FirebaseListeners listeners;
    private HashMap<Query,ArrayList<ValueEventListener>> activeValueListeners;
    private HashMap<Query,ArrayList<ChildEventListener>> activeChildListeners;

    private void addListener(Query reference, ValueEventListener listener){
        reference.addValueEventListener(listener);
        ArrayList<ValueEventListener> aux = activeValueListeners.get(reference);
        if (aux == null)
            aux = new ArrayList<>();
        aux.add(listener);
        activeValueListeners.put(reference,aux);
    }
    private void addListener(Query reference, ChildEventListener listener){
        reference.addChildEventListener(listener);
        ArrayList<ChildEventListener> aux = activeChildListeners.get(reference);
        if (aux == null)
            aux = new ArrayList<>();
        aux.add(listener);
        activeChildListeners.put(reference,aux);
    }

    public void getTrackingKid(String userId) {
        Query reference = database.getReference().child(TRACKING_REFERENCE).child(userId);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (listeners != null) {
                    listeners.onTrackingModified(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (listeners != null) {
                    listeners.onTrackingCancelled(databaseError);
                }
            }
        };
        addListener(reference,listener);
    }

    public void getUserInfo(String user_mail){
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (listeners != null) {
                    listeners.onUserModified(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (listeners != null) {
                    listeners.onUserCancelled(databaseError);
                }
            }
        };
        Query reference = database.getReference().child(USER_REFERENCE).orderByChild("mail").equalTo(user_mail);
        addListener(reference, listener);
    }

    public void getKidsInfo(User user){
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (listeners != null) {
                    listeners.onKidModified(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (listeners != null) {
                    listeners.onKidCancelled(databaseError);
                }
            }
        };
        Query reference;
        if (Babyguard_Application.isTeacher()){
            ArrayList<String> classIds = user.getIdNurseryClasses();
            for (String id:classIds) {
                reference = database.getReference().child(KID_REFERENCE).orderByChild("id_nursery_class").equalTo(id);
                addListener(reference, listener);
            }
        } else {
            reference = database.getReference().child(KID_REFERENCE).orderByChild("id_parent").equalTo(user.getId());
            addListener(reference, listener);
        }
    }

    public void getNursery(String nurseryId){
        Query reference = database.getReference().child(NURSERY_REFERENCE).child(nurseryId);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (listeners != null) {
                    listeners.onNurseryModified(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (listeners != null) {
                    listeners.onNurseryCancelled(databaseError);
                }
            }
        };
        addListener(reference,listener);
    }

    public void getNurseryClass(String nurseryId, String nurseryClass){
        Query reference = database.getReference().child(NURSERY_CLASS_REFERENCE).child(nurseryId).child(nurseryClass);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (listeners != null) {
                    listeners.onNurseryClassModified(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (listeners != null) {
                    listeners.onNurseryClassCancelled(databaseError);
                }
            }
        };
        addListener(reference,listener);
    }

    public void getChatInfoParent(String idNursery, String idNurseryClass, final String idKid) {
        Query reference = database.getReference().child(TEACHER_REFERENCE).child(idNursery).child(idNurseryClass);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Query referenceChat;
                    ValueEventListener listenerChat;
                    int count = 0;
                    for (DataSnapshot aux:dataSnapshot.getChildren()) {
                        final String idTeacher = aux.getKey();
                        referenceChat = database.getReference().child(USER_REFERENCE).child(idTeacher);
                        listenerChat = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ChatKeyMap key = new ChatKeyMap(idTeacher,idKid);
                                Repository.getInstance().addKeyChat(key);
                                if (listeners != null) {
                                    listeners.onChatNameChanged(dataSnapshot);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };
                        referenceChat.addListenerForSingleValueEvent(listenerChat);
                        count++;
                    }
                    Repository.getInstance().setParentChats(count);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }
    public void getChatInfoTeacher(String nurseryClass) {
        final ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (listeners != null) {
                    listeners.onChatNameChanged(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (listeners != null) {
                    listeners.onChatNameError(databaseError);
                }
            }
        };
        Query reference = database.getReference().child(KID_REFERENCE).orderByChild("id_nursery_class").equalTo(nurseryClass);
        addListener(reference,listener);
    }

    public void getChat(String kid_id){
        Query reference = database.getReference().child(CHAT_REFERENCE).child(kid_id);
        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (listeners != null) {
                    listeners.onChatAdded(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (listeners != null) {
                    listeners.onChatCancelled(databaseError);
                }
            }
        };
        addListener(reference,listener);
    }

    private FirebaseManager (){
        activeValueListeners = new HashMap<>();
        activeChildListeners = new HashMap<>();
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseManager getInstance() {
        if (instance==null)
            instance = new FirebaseManager();
        return instance;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void setListeners(FirebaseListeners listeners) {
        this.listeners = listeners;
    }

    public void removeListeners(){
        for (Map.Entry<Query, ArrayList<ChildEventListener>> listenerEntry: activeChildListeners.entrySet()) {
            for (ChildEventListener singleListener : listenerEntry.getValue()){
                listenerEntry.getKey().removeEventListener(singleListener);
            }
        }
        for (Map.Entry<Query, ArrayList<ValueEventListener>> listenerEntry: activeValueListeners.entrySet()) {
            for (ValueEventListener singleListener : listenerEntry.getValue()){
                listenerEntry.getKey().removeEventListener(singleListener);
            }
        }
    }

    public void sendMessage(String id, ChatMessage message) {
        database.getReference().child(CHAT_REFERENCE).child(id).push().setValue(message);
    }

    public void deleteMessage(String id, String key) {
        database.getReference().child(CHAT_REFERENCE).child(id).child(key).removeValue();
    }

    public void close() {
        firebaseAuth.signOut();
        removeListeners();
    }

}
