package com.ncatz.babyguard.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by yeray697 on 12/04/17.
 */

public abstract class FirebaseListeners {
    public abstract void onUserModified(DataSnapshot dataSnapshot);

    public abstract void onUserCancelled(DatabaseError databaseError);


    public abstract void onKidModified(DataSnapshot dataSnapshot);

    public abstract void onKidCancelled(DatabaseError databaseError);


    public abstract void onNurseryModified(DataSnapshot dataSnapshot);

    public abstract void onNurseryCancelled(DatabaseError databaseError);

    public abstract void onNurseryClassModified(DataSnapshot dataSnapshot);

    public abstract void onNurseryClassCancelled(DatabaseError databaseError);

    public abstract void onTrackingModified(DataSnapshot dataSnapshot);

    public abstract void onTrackingCancelled(DatabaseError databaseError);
}
