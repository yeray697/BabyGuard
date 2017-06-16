package com.ncatz.babyguard.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ncatz.babyguard.firebase.FirebaseManager;

/**
 * Created by yeray697 on 14/06/17.
 */

public class FCMInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        FirebaseManager.getInstance().setDeviceId(); //If user is null (is not logged) it doesn't update it until he logs in

    }
}
