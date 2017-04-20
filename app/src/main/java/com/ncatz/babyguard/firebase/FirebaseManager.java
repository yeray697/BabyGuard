package com.ncatz.babyguard.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by yeray697 on 12/04/17.
 */

public class FirebaseManager {
    private static final String KID_REFERENCE = "kid";
    private static final String NURSERY_REFERENCE = "nursery";
    private static final String USER_REFERENCE = "user";
    private static final String TRACKING_REFERENCE = "tracking";
    private static final String NURSERY_CLASS_REFERENCE = "nursery_class";
    private static FirebaseManager instance;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private Query userReference;
    private Query kidReference;
    private Query nurseryReference;
    private Query nurseryClassReference;
    private ValueEventListener userListener;
    private ValueEventListener kidListener;
    private ValueEventListener nurseryListener;
    private ValueEventListener nurseryClassListener;
    private FirebaseListeners listeners;

    public void setUserIdTracking(String userId) {
        database.getReference().child(TRACKING_REFERENCE).orderByKey().equalTo(userId).addValueEventListener(new ValueEventListener() {
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
        });
    }
    public void setUserMail(String user_mail){
        userReference = database.getReference().child(USER_REFERENCE).orderByChild("mail").equalTo(user_mail);
        userReference.addValueEventListener(userListener);
    }
    public void setUserId(String userId){
        kidReference = database.getReference().child(KID_REFERENCE).orderByChild("id_parent").equalTo(userId);
        kidReference.addValueEventListener(kidListener);
    }
    public void setNursery(String nurseryId){
        nurseryReference = database.getReference().child(NURSERY_REFERENCE).child(nurseryId);
        nurseryReference.addValueEventListener(new ValueEventListener() {
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
        });
    }
    public void setNurseryClass(String nurseryId,String nurseryClass){
        nurseryClassReference = database.getReference().child(NURSERY_CLASS_REFERENCE).child(nurseryId).child(nurseryClass);
        nurseryClassReference.addValueEventListener(new ValueEventListener() {
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
        });
    }
    private FirebaseManager (){
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        nurseryClassListener = new ValueEventListener() {
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
        userListener = new ValueEventListener() {
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
        nurseryListener = new ValueEventListener() {
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
        kidListener = new ValueEventListener() {
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
}
