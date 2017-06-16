package com.ncatz.babyguard.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.ncatz.yeray.calendarview.DiaryCalendarEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by yeray697 on 15/06/17.
 */

public class Notification {

    public static final int TYPE_CALENDAR_ADD = 1;
    public static final int TYPE_CALENDAR_EDIT = 2;
    public static final int TYPE_CALENDAR_REMOVE = 3;
    public static final int TYPE_TRACKING_ADD = 4;
    public static final int TYPE_TRACKING_EDIT = 5;
    public static final int TYPE_TRACKING_REMOVE = 6;
    public static final int TYPE_MESSAGE = 7;

    private int type;
    private String from;
    private String to;
    private MessageNotif message;
    private TrackingNotif tracking;
    private CalendarNotif calendar;

    public class MessageNotif {
        private String teacher,
                kid,
                message,
                datetime;

        public String getTeacher() {
            return teacher;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }

        public String getKid() {
            return kid;
        }

        public void setKid(String kid) {
            this.kid = kid;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public ChatMessage getMessage() {
            return new ChatMessage(teacher,kid,message,datetime);
        }
    }

    public class TrackingNotif {
        private String id,
                title,
                datetime,
                description;
        private int type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public TrackingKid getTracking() {
            return new TrackingKid(id,"",title,datetime,type,description);
        }
    }

    public class CalendarNotif {
        private String id,
                title,
                datetime,
                description;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public DiaryCalendarEvent getEvent() {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(Long.parseLong(datetime));
            int year = c.get(Calendar.YEAR),
                    month = c.get(Calendar.MONTH),
                    day = c.get(Calendar.DAY_OF_MONTH);

            return new DiaryCalendarEvent(id,title,year,month,day,description);
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public MessageNotif getMessage() {
        return message;
    }

    public void setMessage(MessageNotif message) {
        this.message = message;
    }

    public TrackingNotif getTracking() {
        return tracking;
    }

    public void setTracking(TrackingNotif tracking) {
        this.tracking = tracking;
    }

    public CalendarNotif getCalendar() {
        return calendar;
    }

    public void setCalendar(CalendarNotif calendar) {
        this.calendar = calendar;
    }

    public static Notification parseReceivedNotif(Map<String, String> data) {

        Gson gson = new Gson();
        Notification notification = new Notification();
        notification.setType(Integer.valueOf(data.get("type")));

        String calendar = data.get("calendar");
        if (calendar != null) {
            try {
                JSONObject calendarJSON = new JSONObject(calendar);
                Notification.CalendarNotif calendarNotif = gson.fromJson(calendarJSON.toString(), Notification.CalendarNotif.class);
                notification.setCalendar(calendarNotif);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String tracking = data.get("tracking");
        if (tracking != null) {
            try {
                JSONObject trackingJSON = new JSONObject(tracking);
                Notification.TrackingNotif trackingNotif = gson.fromJson(trackingJSON.toString(), Notification.TrackingNotif.class);
                notification.setTracking(trackingNotif);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String message = data.get("message");
        if (message != null) {
            try {
                JSONObject messageJSON = new JSONObject(message);
                Notification.MessageNotif messageNotif = gson.fromJson(messageJSON.toString(), Notification.MessageNotif.class);
                notification.setMessage(messageNotif);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return notification;
    }
}