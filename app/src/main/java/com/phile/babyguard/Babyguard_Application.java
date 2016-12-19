package com.phile.babyguard;

import android.app.Application;
import android.content.SharedPreferences;

import com.phile.babyguard.model.User;

/**
 * Created by usuario on 19/12/16.
 */

public class Babyguard_Application extends Application {
    User user;
    SharedPreferences pref;

    @Override
    public void onCreate() {
        super.onCreate();

        pref = getApplicationContext().getSharedPreferences("credentials", MODE_PRIVATE);
    }

    public void setUser(User user){
        this.user = user;
        if (user != null) {
            pref.edit().putString("user", user.getUser()).apply();
            pref.edit().putString("pass", user.getPass()).apply();
        } else {
            pref.edit().clear().apply();
        }
    }
    public User getUser(){
        return this.user;
    }

    public String getUserIfExists() {
        return pref.getString("user",null);
    }

    public String getPassIfExists() {
        return pref.getString("pass",null);
    }
}
