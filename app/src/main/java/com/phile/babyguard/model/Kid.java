package com.phile.babyguard.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.yeray697.calendarview.DiaryCalendarEvent;

import java.util.ArrayList;

/**
 * Kid credentials
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class Kid implements Parcelable{
    private String id_kid;
    private String name;
    private String photo;
    private String info;
    private ArrayList<InfoKid> infoKids;
    private ArrayList<DiaryCalendarEvent> calendarEvents;

    public Kid(String id_kid, String name, String photo, String info, ArrayList<InfoKid> infoKids, ArrayList<DiaryCalendarEvent> calendarEvents) {
        this.id_kid = id_kid;
        this.name = name;
        this.photo = photo;
        this.info = info;
        this.infoKids = infoKids;
        this.calendarEvents = calendarEvents;
    }

    protected Kid(Parcel in) {
        id_kid = in.readString();
        name = in.readString();
        photo = in.readString();
        info = in.readString();
        calendarEvents = new ArrayList<>();
        infoKids = new ArrayList<>();
        in.readTypedList(calendarEvents,DiaryCalendarEvent.CREATOR);
        in.readTypedList(infoKids,InfoKid.CREATOR);
    }

    public static final Creator<Kid> CREATOR = new Creator<Kid>() {
        @Override
        public Kid createFromParcel(Parcel in) {
            return new Kid(in);
        }

        @Override
        public Kid[] newArray(int size) {
            return new Kid[size];
        }
    };

    public String getIdKid() {
        return id_kid;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public String getInfo() {
        return info;
    }

    public ArrayList<InfoKid> getInfoKids() {
        return infoKids;
    }

    public ArrayList<DiaryCalendarEvent> getCalendarEvents() {
        return calendarEvents;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_kid);
        dest.writeString(name);
        dest.writeString(photo);
        dest.writeString(info);
        dest.writeTypedList(calendarEvents);
        dest.writeTypedList(infoKids);
    }
}
