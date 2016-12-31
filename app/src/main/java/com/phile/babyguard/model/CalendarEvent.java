package com.phile.babyguard.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by yeray697 on 26/12/16.
 */

public class CalendarEvent implements ParentObject, Parcelable {
    private String name;
    private String date;
    private int color;
    private EventInfo description;

    public CalendarEvent(String name, String date, EventInfo description, int color) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.color = color;
    }


    protected CalendarEvent(Parcel in) {
        name = in.readString();
        description = in.readParcelable(EventInfo.class.getClassLoader());
        date = in.readString();
        color = in.readInt();
    }

    public static final Creator<CalendarEvent> CREATOR = new Creator<CalendarEvent>() {
        @Override
        public CalendarEvent createFromParcel(Parcel in) {
            return new CalendarEvent(in);
        }

        @Override
        public CalendarEvent[] newArray(int size) {
            return new CalendarEvent[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventInfo getDescription() {
        return description;
    }

    public void setDescription(EventInfo description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeParcelable(description, flags);
        dest.writeString(date);
        dest.writeInt(color);
    }

    @Override
    public List<Object> getChildObjectList() {
        return Arrays.asList((Object)description);
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        if (list != null && list.size() >= 1)
            description = (EventInfo) list.get(0);
    }


    public static class EventInfo implements Parcelable {
        private String info;

        public EventInfo(String info) {
            this.info = info;
        }

        protected EventInfo(Parcel in) {
            info = in.readString();
        }

        public static final Creator<EventInfo> CREATOR = new Creator<EventInfo>() {
            @Override
            public EventInfo createFromParcel(Parcel in) {
                return new EventInfo(in);
            }

            @Override
            public EventInfo[] newArray(int size) {
                return new EventInfo[size];
            }
        };


        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(info);
        }
    }
}
