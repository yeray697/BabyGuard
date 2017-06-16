package com.ncatz.babyguard.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import com.google.firebase.database.Exclude;
import com.yeray697.dotLineRecyclerView.RecyclerData;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Comparator;

/**
 * Created by yeray697 on 19/12/16.
 */

public class TrackingKid extends RecyclerData implements Parcelable{

    @Exclude
    private RecyclerData data;
    @Exclude
    private String image;
    private String id;
    private String description;
    private String title;
    private String datetime;
    private int type;

    public static final Comparator<? super TrackingKid> CATEGORY = new Comparator<TrackingKid>() {
        @Override
        public int compare(TrackingKid o1, TrackingKid o2) {
            int result = o1.getType() - o2.getType();
            if (result == 0)
                result = o2.datetime.compareTo(o1.datetime);
            return result;
        }
    };
    public static final Comparator<? super TrackingKid> CHRONOLOGIC = new Comparator<TrackingKid>() {
        @Override
        public int compare(TrackingKid o1, TrackingKid o2) {
            return o2.datetime.compareTo(o1.datetime);
        }
    };

    public TrackingKid(String image, String title, String datetime, @Type int type, String description) {
        super(image, title, datetime);
        this.image = image;
        this.title = title;
        this.datetime = datetime;
        this.type = type;
        this.description = description;
        this.data = new RecyclerData(this.image,this.title,this.datetime);
    }

    public TrackingKid(String id, String image, String title, String datetime, @Type int type, String description) {
        super(image, title, datetime);
        this.id = id;
        this.image = image;
        this.title = title;
        this.datetime = datetime;
        this.type = type;
        this.description = description;
        this.data = new RecyclerData(this.image,this.title,this.datetime);
    }

    protected TrackingKid(Parcel in) {
        super(in.readString(), in.readString(), in.readString());
        image = super.getImageUrl();
        title = super.getTitle();
        datetime = super.getSubtitle();
        type = in.readInt();
        description = in.readString();
        id = in.readString();
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
        dest.writeString(datetime);
        dest.writeInt(type);
        dest.writeString(description);
        dest.writeString(id);
    }

    public String getTypeString() {
        String result;
        switch (type) {
            case 1:
                result = "Poop";
                break;
            case 2:
                result = "Food";
                break;
            case 3:
                result = "Sleep";
                break;
            case 4:
                result = "Other";
                break;
            default:
                result = "";
                break;
        }
        return result;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        super.setSubtitle(datetime);
        this.datetime = datetime;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
