package com.ncatz.babyguard.firebase;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ncatz.babyguard.Babyguard_Application;
import com.ncatz.babyguard.model.ChatKeyMap;
import com.ncatz.babyguard.model.ChatMessage;
import com.ncatz.babyguard.model.Kid;
import com.ncatz.babyguard.model.TrackingKid;
import com.ncatz.babyguard.model.User;
import com.ncatz.babyguard.repository.Repository;
import com.ncatz.babyguard.utils.Utils;
import com.ncatz.yeray.calendarview.DiaryCalendarEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.ncatz.babyguard.preferences.SettingsManager.setStringPreference;

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
    private static final String ROOT_STORAGE_REFERENCE = "gs://babyguard-4536a.appspot.com";
    private static final String PROFILE_IMG_STORAGE_REFERENCE = "profile_img";

    private static FirebaseManager instance;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private FirebaseMessaging mFCMInteractor;
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
        Calendar c = Utils.getTodayDateCalendar();
        String todayUnix = String.valueOf(c.getTime().getTime());

        Query reference = database.getReference().child(TRACKING_REFERENCE).child(userId).orderByChild("datetime").startAt(todayUnix);
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

    public String chatIdTeacher;
    public void getChat(String kid_id){
        boolean isTeacher = Babyguard_Application.isTeacher();
        if (isTeacher && chatIdTeacher.equals("")) {
            if (isTeacher)
                chatIdTeacher = kid_id;
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
    }

    private FirebaseManager (){
        activeValueListeners = new HashMap<>();
        activeChildListeners = new HashMap<>();
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        mFCMInteractor = FirebaseMessaging.getInstance();
        chatIdTeacher = "";
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
        chatIdTeacher = "";
        removeListeners();
    }

    public boolean removeEvent(String nurseryId, String classId, String eventId) {
        boolean result = Repository.getInstance().removeEvent(nurseryId,classId,eventId);
        database.getReference().child(NURSERY_CLASS_REFERENCE).child(nurseryId).child(classId).child("calendar").child(eventId).removeValue();
        return result;
    }

    public void updateEvent(String nurseryId, String classId, DiaryCalendarEvent event) {
        HashMap<String,String> eventPush = new HashMap<>();
        String datetime = Utils.parseDateToUnix(event.getDate());
        eventPush.put("datetime",datetime);
        eventPush.put("description",event.getDescription());
        eventPush.put("title",event.getTitle());
        database.getReference().child(NURSERY_CLASS_REFERENCE).child(nurseryId).child(classId).child("calendar").child(event.getId()).setValue(eventPush);
    }

    public DiaryCalendarEvent addEvent(String nurseryId, String classId, DiaryCalendarEvent event) {

        HashMap<String,String> eventPush = new HashMap<>();
        String datetime = Utils.parseDateToUnix(event.getDate());
        eventPush.put("datetime",datetime);
        eventPush.put("description",event.getDescription());
        eventPush.put("title",event.getTitle());
        DatabaseReference ref = database.getReference().child(NURSERY_CLASS_REFERENCE).child(nurseryId).child(classId).child("calendar").push();
        event.setId(ref.getKey());
        ref.setValue(eventPush);
        return event;
    }


    public boolean removeTracking(String kidId, String trackingId) {
        boolean result = Repository.getInstance().removeTrackingItem(kidId,trackingId);
        database.getReference().child(TRACKING_REFERENCE).child(kidId).child(trackingId).removeValue();
        return result;
    }

    public void updateTracking(String kidId, TrackingKid trackingKid) {
        HashMap<String,String> trackingPush = new HashMap<>();
        trackingPush.put("datetime",trackingKid.getDatetime());
        trackingPush.put("description",trackingKid.getDescription());
        trackingPush.put("title",trackingKid.getTitle());
        trackingPush.put("type", String.valueOf(trackingKid.getType()));
        database.getReference().child(TRACKING_REFERENCE).child(kidId).child(trackingKid.getId()).setValue(trackingPush);
    }

    public TrackingKid addTracking(String kidId, TrackingKid trackingKid) {
        HashMap<String,String> trackingPush = new HashMap<>();
        trackingPush.put("datetime",trackingKid.getDatetime());
        trackingPush.put("description",trackingKid.getDescription());
        trackingPush.put("title",trackingKid.getTitle());
        trackingPush.put("type", String.valueOf(trackingKid.getType()));
        DatabaseReference ref = database.getReference().child(TRACKING_REFERENCE).child(kidId).push();
        trackingKid.setId(ref.getKey());
        ref.setValue(trackingPush);
        return trackingKid;
    }

    public void changeUserPassword(final String newPass, final String passKey) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential =
                EmailAuthProvider.getCredential(
                        Repository.getInstance().getUser().getMail(),
                        newPass);

        user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    setStringPreference(passKey,newPass);
                } else {
                }
            }
        });
    }

    public void changeUserName() {
        User user = Repository.getInstance().getUser();
        database.getReference(USER_REFERENCE).child(user.getId()).child("name").setValue(user.getName());
    }

    public void changeUserPhone() {
        User user = Repository.getInstance().getUser();
        database.getReference(USER_REFERENCE).child(user.getId()).child("phone_number").setValue(user.getPhone_number());
    }

    public void changeKidName(String id, String name) {
        database.getReference(KID_REFERENCE).child(id).child("name").setValue(name);
    }

    public void changeKidInfo(String id, String info) {
        database.getReference(KID_REFERENCE).child(id).child("info").setValue(info);
    }

    public void uploadImageToFirebase(Uri uri, final boolean isKid, final String id, OnFailureListener onFailureListener, OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener) {
        StorageReference ref;
        if (isKid) {
            ref = storage.getReferenceFromUrl(ROOT_STORAGE_REFERENCE).child(PROFILE_IMG_STORAGE_REFERENCE + "/" + KID_REFERENCE + "/" + id + ".jpg");
        } else {
            ref = storage.getReferenceFromUrl(ROOT_STORAGE_REFERENCE).child(PROFILE_IMG_STORAGE_REFERENCE + "/" + USER_REFERENCE + "/" + id + ".jpg");
        }
        byte[] imageBytes = Utils.prepareImageToUpload(uri,3840);

        UploadTask uploadTask = ref.putBytes(imageBytes);

        uploadTask.addOnFailureListener(onFailureListener)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                String urlImage = taskSnapshot.getDownloadUrl().toString();
                if (isKid) {
                    database.getReference(KID_REFERENCE).child(id).child("img").setValue(urlImage);
                } else {
                    if (!Babyguard_Application.isTeacher()){
                        ArrayList<Kid> kids = (ArrayList<Kid>) Repository.getInstance().getKids();
                        for (Kid aux : kids)
                            database.getReference(KID_REFERENCE).child(aux.getId()).child("img_profile").setValue(urlImage);
                    }
                    database.getReference(USER_REFERENCE).child(id).child("img_profile").setValue(urlImage);
                }
            }
        })
                .addOnSuccessListener(onSuccessListener);
    }

    public void setDeviceId() {
        User user = Repository.getInstance().getUser();
        if (user != null) {
            String token = FirebaseInstanceId.getInstance().getToken();
            user.setFcmID(token);
            database.getReference(USER_REFERENCE).child(user.getId()).child("fcmToken").setValue(token);
            if (!Babyguard_Application.isTeacher()) {
                for (Kid aux : Repository.getInstance().getKids()) {
                    aux.setFcmID(token);
                    database.getReference(KID_REFERENCE).child(aux.getId()).child("fcmToken").setValue(token);
                }
            }
        }
    }
}
