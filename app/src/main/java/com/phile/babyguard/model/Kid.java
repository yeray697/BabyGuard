package com.phile.babyguard.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Kid credentials
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class Kid implements Parcelable{
    private String id_kid;
    private String id_parent;
    private String name;
    private String photo;
    private ArrayList<InfoKid> infoKids;
    private ArrayList<CalendarEvent> calendarEvents;

    public Kid(String id_kid,String id_parent, String name, String photo, ArrayList<InfoKid> infoKids, ArrayList<CalendarEvent> calendarEvents) {
        this.id_kid = id_kid;
        this.id_parent = id_parent;
        this.name = name;
        this.photo = photo;
        this.infoKids = infoKids;
        this.calendarEvents = calendarEvents;
    }

    protected Kid(Parcel in) {
        id_kid = in.readString();
        id_parent = in.readString();
        name = in.readString();
        photo = in.readString();
        calendarEvents = new ArrayList<>();
        infoKids = new ArrayList<>();
        in.readTypedList(calendarEvents,CalendarEvent.CREATOR);
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

    public String getIdParent() {
        return id_parent;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public ArrayList<InfoKid> getInfoKids() {
        return infoKids;
    }

    public ArrayList<CalendarEvent> getCalendarEvents() {
        return calendarEvents;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_kid);
        dest.writeString(id_parent);
        dest.writeString(name);
        dest.writeString(photo);
        dest.writeTypedList(calendarEvents);
        dest.writeTypedList(infoKids);
    }
}
