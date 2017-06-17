package com.ncatz.babyguard.model;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ncatz.babyguard.Babyguard_Application;
import com.ncatz.babyguard.utils.RestClient;
import com.ncatz.yeray.calendarview.DiaryCalendarEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by yeray697 on 15/06/17.
 */

public class PushNotification {

    public static final int TYPE_CALENDAR_ADD = 1;
    public static final int TYPE_CALENDAR_EDIT = 2;
    public static final int TYPE_CALENDAR_REMOVE = 3;
    public static final int TYPE_TRACKING_ADD = 4;
    public static final int TYPE_TRACKING_EDIT = 5;
    public static final int TYPE_TRACKING_REMOVE = 6;
    public static final int TYPE_MESSAGE = 7;

    public static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    public static final String FCM_APIKEY = "AAAAyEDg-zI:APA91bG6YvY7BpWGphY70EMuJVktx9f77STPBbatuqL19eZCS2IK9AJkWJ9SiJwFCo8E9-XQBOKzJwcbW6lFpv9QSHrr9r6AsAWYMaXgtOr7a-g64p8XEoPnfJC4TrWB48O4w7A_Ji6v";

    private int type;
    private String fromUser;
    private String toUser;
    private MessageNotif message;
    private TrackingNotif tracking;
    private CalendarNotif calendar;

    public void setChatMessage(ChatMessage chatMessage) {
        message = new MessageNotif();
        message.teacher = chatMessage.getTeacher();
        message.kid = chatMessage.getKid();
        message.message = chatMessage.getMessage();
        message.datetime = chatMessage.getDatetime();
    }

    public void setTrackingKid(TrackingKid trackingKid) {
        tracking = new TrackingNotif();
        tracking.datetime = trackingKid.getDatetime();
        tracking.description = trackingKid.getDescription();
        tracking.title = trackingKid.getTitle();
        tracking.type = trackingKid.getType();
    }

    public void setDiaryCalendarEvent(DiaryCalendarEvent event) {
        calendar = new CalendarNotif();
        calendar.datetime = event.getDate();
        calendar.description = event.getDescription();
        calendar.title = event.getTitle();
    }

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
            int id = Babyguard_Application.isTeacher() ? 0 : 1;
            return new ChatMessage(id, teacher,kid,message,datetime);
        }
    }

    public class TrackingNotif {
        private String title,
                datetime,
                description;
        private int type;

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
            return new TrackingKid("",title,datetime,type,description);
        }
    }

    public class CalendarNotif {
        private String title,
                datetime,
                description;

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
            int year,month,day;
            boolean error = false;
            Calendar c = Calendar.getInstance();
            try {
                c.setTimeInMillis(Long.parseLong(datetime));
            } catch (Exception e) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                try {

                    c.setTime(sdf.parse(datetime));
                } catch (ParseException e1) {
                    error = true;
                }
            }
            DiaryCalendarEvent event = null;
            if (!error) {

                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                event = new DiaryCalendarEvent(title,year,month,day,description);
            }
            return event;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
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

    public static PushNotification parseReceivedNotif(Map<String, String> data) {

        Gson gson = new Gson();
        PushNotification pushNotification = new PushNotification();
        pushNotification.type = Integer.valueOf(data.get("type"));
        pushNotification.fromUser = data.get("fromUser");
        pushNotification.toUser = data.get("toUser");

        String calendar = data.get("calendar");
        if (calendar != null) {
            try {
                JSONObject calendarJSON = new JSONObject(calendar);
                PushNotification.CalendarNotif calendarNotif = gson.fromJson(calendarJSON.toString(), PushNotification.CalendarNotif.class);
                pushNotification.calendar = calendarNotif;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String tracking = data.get("tracking");
        if (tracking != null) {
            try {
                JSONObject trackingJSON = new JSONObject(tracking);
                PushNotification.TrackingNotif trackingNotif = gson.fromJson(trackingJSON.toString(), PushNotification.TrackingNotif.class);
                pushNotification.tracking  = trackingNotif;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String message = data.get("message");
        if (message != null) {
            try {
                JSONObject messageJSON = new JSONObject(message);
                PushNotification.MessageNotif messageNotif = gson.fromJson(messageJSON.toString(), PushNotification.MessageNotif.class);
                pushNotification.message = messageNotif;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return pushNotification;
    }

    public void pushNotification(String deviceId) {
        try {
            JSONObject root = new JSONObject();
           // JSONArray j = this.toJSON();
            try {
                JSONObject j = toJSON();
                root.put("data",j);
                root.put("to",deviceId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StringEntity entity = new StringEntity(root.toString());
            RestClient.post(FCM_URL,FCM_APIKEY,entity, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private JSONObject toJSON() {
        JSONObject jsonArray = null;
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        try {
            jsonArray = new JSONObject(jsonString);
            //jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonArray;
    }
}