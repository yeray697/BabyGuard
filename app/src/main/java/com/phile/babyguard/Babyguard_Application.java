package com.phile.babyguard;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.phile.babyguard.model.User;

/**
 * Context application
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class Babyguard_Application extends Application {
    private User user;
    private SharedPreferences pref;

    private final static String FILE_PREFERENCE = "credentials";
    private final static String USER_PREFERENCE = "user";
    private final static String PASS_PREFERENCE = "pass";
    private final static String TYPE_PREFERENCE = "type";

    @Override
    public void onCreate() {
        super.onCreate();

        pref = getApplicationContext().getSharedPreferences(FILE_PREFERENCE, MODE_PRIVATE);
    }

    /**
     * Set user credentials.
     * If user is null, it cleans preferences
     * @param user
     */
    public void setUser(User user){
        this.user = user;
        if (user != null) {
            pref.edit().putString(USER_PREFERENCE, user.getUser()).apply();
            pref.edit().putString(PASS_PREFERENCE, user.getPass()).apply();
            pref.edit().putInt(TYPE_PREFERENCE, user.getType()).apply();
        } else {
            pref.edit().clear().apply();
        }
    }

    /**
     * Get user's credentials
     * @return Return the user
     */
    public User getUser(){
        String name;
        String pass;
        int type;
        if (this.user == null) {
            name = getUserIfExists();
            if (!TextUtils.isEmpty(name)) {
                pass = getPassIfExists();
                if (!TextUtils.isEmpty(pass)) {
                    type = getTypeIfExists();
                    this.user = new User(name, pass, type);
                }
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

    /**
     * Get the user's pass is exists
     * @return Return the user's pass
     */
    private int getTypeIfExists() {
        return pref.getInt("type",-1);
    }

}
