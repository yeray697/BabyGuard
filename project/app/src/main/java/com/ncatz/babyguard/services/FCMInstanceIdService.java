package com.ncatz.babyguard.services;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ncatz.babyguard.firebase.FirebaseManager;

/**
 * Firebase service that update firebase token when it changes
 */

public class FCMInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        FirebaseManager.getInstance().setDeviceId(); //If user is null (is not logged) it doesn't update it until he logs in

    }
}
