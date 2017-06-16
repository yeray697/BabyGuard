package com.ncatz.babyguard.model;

import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ncatz.babyguard.Babyguard_Application;
import com.ncatz.babyguard.utils.RestClient;
import com.ncatz.yeray.calendarview.DiaryCalendarEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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
        pushNotification.setType(Integer.valueOf(data.get("type")));

        String calendar = data.get("calendar");
        if (calendar != null) {
            try {
                JSONObject calendarJSON = new JSONObject(calendar);
                PushNotification.CalendarNotif calendarNotif = gson.fromJson(calendarJSON.toString(), PushNotification.CalendarNotif.class);
                pushNotification.setCalendar(calendarNotif);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String tracking = data.get("tracking");
        if (tracking != null) {
            try {
                JSONObject trackingJSON = new JSONObject(tracking);
                PushNotification.TrackingNotif trackingNotif = gson.fromJson(trackingJSON.toString(), PushNotification.TrackingNotif.class);
                pushNotification.setTracking(trackingNotif);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String message = data.get("message");
        if (message != null) {
            try {
                JSONObject messageJSON = new JSONObject(message);
                PushNotification.MessageNotif messageNotif = gson.fromJson(messageJSON.toString(), PushNotification.MessageNotif.class);
                pushNotification.setMessage(messageNotif);
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
                    Toast.makeText(Babyguard_Application.getContext(), "statusCode: " + statusCode, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(Babyguard_Application.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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