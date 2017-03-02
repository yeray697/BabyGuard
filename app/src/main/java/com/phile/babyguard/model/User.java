package com.phile.babyguard.model;

/**
 * User's credentials
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class User {
    private String user;
    private String pass;
    private int type;
    public static final int TYPE_TEACHER = 1;
    public static final int TYPE_BABY = 2;

    public User(String user, String pass, int type) {
        this.pass = pass;
        this.user = user;
        this.type = type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
