package com.phile.babyguard.model;

import java.util.ArrayList;

/**
 * Created by yeray697 on 26/12/16.
 */

public class NurserySchool {
    private String name;
    private String address;
    private String email;
    private String web;
    private ArrayList<String> telephone;

    public NurserySchool(String name, String address, String email, String web, ArrayList<String> telephone) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.web = web;
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public ArrayList<String> getTelephone() {
        return telephone;
    }

    public void setTelephone(ArrayList<String> telephone) {
        this.telephone = telephone;
    }
}
