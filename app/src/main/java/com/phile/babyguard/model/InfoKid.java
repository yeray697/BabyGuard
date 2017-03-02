package com.phile.babyguard.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import com.yeray697.dotLineRecyclerView.RecyclerData;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Comparator;

/**
 * Created by yeray697 on 19/12/16.
 */

public class InfoKid extends RecyclerData implements Parcelable{

    private RecyclerData data;
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
            return o2.getDate().compareTo(o1.getDate());
        }
    };

    protected InfoKid(Parcel in) {
        super(in.readString(), in.readString(), in.readString());
        image = super.getImageUrl();
        title = super.getTitle();
        date = super.getSubtitle();
        type = in.readInt();
        description = in.readString();
    }

    public static final Creator<InfoKid> CREATOR = new Creator<InfoKid>() {
        @Override
        public InfoKid createFromParcel(Parcel in) {
            return new InfoKid(in);
        }

        @Override
        public InfoKid[] newArray(int size) {
            return new InfoKid[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeInt(type);
        dest.writeString(description);
    }

    @IntDef({Type.POOP, Type.FOOD, Type.SLEEP, Type.OTHER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
        int POOP = 1;
        int FOOD = 2;
        int SLEEP = 3;
        int OTHER = 4;
    }

    public InfoKid(String image, String title, String date, @Type int type, String description) {
        super(image, title, date);
        this.image = image;
        this.title = title;
        this.date = date;
        this.type = type;
        this.description = description;
        this.data = new RecyclerData(this.image,this.title,this.date);
    }

    public String getImageURL() {
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

    public RecyclerData getData() {
        return data;
    }
}
