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
    private String time;
    private int type;
    private String description;

    public static final Comparator<? super InfoKid> CATEGORY = new Comparator<InfoKid>() {
        @Override
        public int compare(InfoKid o1, InfoKid o2) {
            int result = o1.getType() - o2.getType();
            if (result == 0)
                result = o2.getTime().compareTo(o1.getTime());
            return result;
        }
    };
    public static final Comparator<? super InfoKid> CHRONOLOGIC = new Comparator<InfoKid>() {
        @Override
        public int compare(InfoKid o1, InfoKid o2) {
            return o2.getTime().compareTo(o1.getTime());
        }
    };

    protected InfoKid(Parcel in) {
        super(in.readString(), in.readString(), in.readString());
        image = super.getImageUrl();
        title = super.getTitle();
        time = super.getSubtitle();
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
        dest.writeString(time);
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

    public InfoKid(String image, String title, String time, @Type int type, String description) {
        super(image, title, time);
        this.image = image;
        this.title = title;
        this.time = time;
        this.type = type;
        this.description = description;
        this.data = new RecyclerData(this.image,this.title,this.time);
    }

    public String getImageURL() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
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

    public static @InfoKid.Type int parseIntToType(int type){
        @Type int result = Type.OTHER;
        switch (type){
            case 1:
                result = Type.POOP;
                break;
            case 2:
                result = Type.FOOD;
                break;
            case 3:
                result = Type.SLEEP;
                break;
            case 4:
                result = Type.OTHER;
                break;
        }
        return result;
    }
}
