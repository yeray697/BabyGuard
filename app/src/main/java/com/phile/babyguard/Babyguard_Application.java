package com.phile.babyguard;

import android.app.Application;
import android.content.SharedPreferences;

import com.phile.babyguard.model.User;

/**
 * Context application
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class Babyguard_Application extends Application {
    User user;
    SharedPreferences pref;

    @Override
    public void onCreate() {
        super.onCreate();

        pref = getApplicationContext().getSharedPreferences("credentials", MODE_PRIVATE);
    }

    /**
     * Set user credentials.
     * If user is null, it cleans preferences
     * @param user
     */
    public void setUser(User user){
        this.user = user;
        if (user != null) {
            pref.edit().putString("user", user.getUser()).apply();
            pref.edit().putString("pass", user.getPass()).apply();
        } else {
            pref.edit().clear().apply();
        }
    }

    /**
     * Get user's credentials
     * @return Return the user
     */
    public User getUser(){
        if (this.user == null) {
            String user = getUserIfExists();
            if (user != null) {
                String pass = getPassIfExists();
                this.user = new User(user, pass);
            }
        }
        return this.user;
    }

    /**
     * Get the user's name is exists
     * @return Return the user's name
     */
    private String getUserIfExists() {
        return pref.getString("user",null);
    }

    /**
     * Get the user's pass is exists
     * @return Return the user's pass
     */
    private String getPassIfExists() {
        return pref.getString("pass",null);
    }
}
