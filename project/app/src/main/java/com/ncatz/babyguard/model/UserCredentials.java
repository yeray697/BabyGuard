package com.ncatz.babyguard.model;

/**
 * User credentials POJO class
 *
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class UserCredentials {
    private String user;
    private String pass;
    private int type;
    public static final int TYPE_TEACHER = 3;
    public static final int TYPE_PARENT = 4;

    public UserCredentials(String user, String pass) {
        this.pass = pass;
        this.user = user;
    }

    public UserCredentials(String user, String pass, int type) {
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
