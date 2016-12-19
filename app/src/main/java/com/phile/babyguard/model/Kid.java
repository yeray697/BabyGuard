package com.phile.babyguard.model;

/**
 * Created by usuario on 19/12/16.
 */

public class Kid {
    private String id_kid;
    private String id_parent;
    private String name;
    private String photo;

    public Kid(String id_kid,String id_parent, String name, String photo) {
        this.id_kid = id_kid;
        this.id_parent = id_parent;
        this.name = name;
        this.photo = photo;
    }

    public String getIdKid() {
        return id_kid;
    }

    public String getIdParent() {
        return id_parent;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }
}
