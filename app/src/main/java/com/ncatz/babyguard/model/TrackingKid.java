package com.ncatz.babyguard.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import com.ncatz.babyguard.R;
import com.yeray697.dotLineRecyclerView.RecyclerData;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Comparator;

/**
 * Created by yeray697 on 19/12/16.
 */

public class TrackingKid extends RecyclerData implements Parcelable{

    private RecyclerData data;
    private String image;
    private String title;
    private String time;
    private int type;
    private String description;

    public static final Comparator<? super TrackingKid> CATEGORY = new Comparator<TrackingKid>() {
        @Override
        public int compare(TrackingKid o1, TrackingKid o2) {
            int result = o1.getType() - o2.getType();
            if (result == 0)
                result = o2.getTime().compareTo(o1.getTime());
            return result;
        }
    };
    public static final Comparator<? super TrackingKid> CHRONOLOGIC = new Comparator<TrackingKid>() {
        @Override
        public int compare(TrackingKid o1, TrackingKid o2) {
            return o2.getTime().compareTo(o1.getTime());
        }
    };

    public TrackingKid(String image, String title, String time, @Type int type, String description) {
        super(image, title, time);
        this.image = image;
        this.title = title;
        this.time = time;
        this.type = type;
        this.description = description;
        this.data = new RecyclerData(this.image,this.title,this.time);
    }

    protected TrackingKid(Parcel in) {
        super(in.readString(), in.readString(), in.readString());
        image = super.getImageUrl();
        title = super.getTitle();
        time = super.getSubtitle();
        type = in.readInt();
        description = in.readString();
    }

    public static final Creator<TrackingKid> CREATOR = new Creator<TrackingKid>() {
        @Override
        public TrackingKid createFromParcel(Parcel in) {
            return new TrackingKid(in);
        }

        @Override
        public TrackingKid[] newArray(int size) {
            return new TrackingKid[size];
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

    public static @TrackingKid.Type int parseIntToType(int type){
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

    public RecyclerData getData() {
        return data;
    }

    public void setData(RecyclerData data) {
        this.data = data;
    }

    @Override
    public String getImageUrl() {
        return image;
    }

    public void setImageUrl(String image) {
        this.image = image;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        super.setSubtitle(time);
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
