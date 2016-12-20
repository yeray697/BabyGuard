package com.phile.babyguard.model;

/**
 * User's credentials
 * @author Yeray Ruiz Juárez
 * @version 1.0
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
