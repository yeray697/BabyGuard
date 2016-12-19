package com.phile.babyguard.model;

/**
 * Created by usuario on 19/12/16.
 */

public class User {
    private String user;
    private String pass;

    public User(String user, String pass) {
        this.pass = pass;
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
