package com.phile.babyguard.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Comparator;

/**
 * Created by yeray697 on 19/12/16.
 */

public class InfoKid {
    private String image;
    private String title;
    private String date;
    private int type;
    private String description;

    public static final Comparator<? super InfoKid> CATEGORY = new Comparator<InfoKid>() {
        @Override
        public int compare(InfoKid o1, InfoKid o2) {
            int result = o1.getType() - o2.getType();
            if (result == 0)
                result = o2.getDate().compareTo(o1.getDate());
            return result;
        }
    };
    public static final Comparator<? super InfoKid> CHRONOLOGIC = new Comparator<InfoKid>() {
        @Override
        public int compare(InfoKid o1, InfoKid o2) {
            int result = o2.getDate().compareTo(o1.getDate());
            return result;
        }
    };

    @IntDef({Type.POOP, Type.FOOD, Type.SLEEP, Type.OTHER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
        int POOP = 1;
        int FOOD = 2;
        int SLEEP = 3;
        int OTHER = 4;
    }

    public InfoKid(String image, String title, String date, @Type int type, String description) {
        this.image = image;
        this.title = title;
        this.date = date;
        this.type = type;
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
