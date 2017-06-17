package com.ncatz.babyguard.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ncatz.babyguard.Babyguard_Application;
import com.ncatz.babyguard.Login_Activity;
import com.ncatz.babyguard.R;
import com.ncatz.babyguard.model.ChatMessage;
import com.ncatz.babyguard.model.PushNotification;
import com.ncatz.babyguard.model.TrackingKid;
import com.ncatz.babyguard.preferences.SettingsManager;
import com.ncatz.babyguard.repository.Repository;
import com.ncatz.yeray.calendarview.DiaryCalendarEvent;

import java.util.Date;
import java.util.Map;

/**
 * Created by yeray697 on 14/06/17.
 */

public class FCMService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    public static final String ACTION_LAUNCH_CALENDAR = "calendar";
    public static final String ACTION_LAUNCH_TRACKING = "tracking";
    public static final String ACTION_LAUNCH_CHAT = "chat";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String currentActivity = Babyguard_Application.getCurrentActivity();
        boolean inForeground = currentActivity != null && !currentActivity.equals("");
        boolean isTeacher;
        Map<String, String> data = remoteMessage.getData();
        PushNotification pushNotification = PushNotification.parseReceivedNotif(data);
        boolean messagePreview = SettingsManager.getBooleanPreference(SettingsManager.getKeyPreferenceByResourceId(R.string.notifications_preview_pref),true);
        String title = "",
                message = "";
        DiaryCalendarEvent event = null;
        TrackingKid tracking = null;
        //Intent intent = new Intent(this, NotificationActionService.class);
        Intent intent = new Intent(this, Login_Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP|   Intent.FLAG_ACTIVITY_NEW_TASK);
        switch (pushNotification.getType()) {
            case PushNotification.TYPE_CALENDAR_ADD:
                title = "Event added";
                event = pushNotification.getCalendar().getEvent();
                if (messagePreview) {
                    title += " - " + event.getTitle() + " - " + event.getDate();
                    message = event.getDescription();
                } else {
                    message = event.getDate();
                }
                intent.setAction(ACTION_LAUNCH_CALENDAR);
                break;
            case PushNotification.TYPE_CALENDAR_EDIT:
                title = "Event edited";
                event = pushNotification.getCalendar().getEvent();
                if (messagePreview) {
                    title += " - " + event.getTitle() + " - " + event.getDate();
                    message = event.getDescription();
                } else {
                    message = event.getDate();
                }
                intent.setAction(ACTION_LAUNCH_CALENDAR);
                break;
            case PushNotification.TYPE_CALENDAR_REMOVE:
                title = "Event removed";
                event = pushNotification.getCalendar().getEvent();
                if (messagePreview) {
                    title += " - " + event.getTitle() + " - " + event.getDate();
                    message = event.getDescription();
                } else {
                    message = event.getDate();
                }
                intent.setAction(ACTION_LAUNCH_CALENDAR);
                break;
            case PushNotification.TYPE_TRACKING_ADD:
                title = "Tracking added";
                tracking = pushNotification.getTracking().getTracking();
                if (messagePreview) {
                    title += " - " + tracking.getTypeString() + " - " + tracking.getTitle();
                    message = tracking.getDescription();
                } else {
                    message = tracking.getTypeString();
                }
                intent.setAction(ACTION_LAUNCH_TRACKING);
                break;
            case PushNotification.TYPE_TRACKING_EDIT:
                title = "Tracking edited";
                tracking = pushNotification.getTracking().getTracking();
                if (messagePreview) {
                    title += " - " + tracking.getTypeString() + " - " + tracking.getTitle();
                    message = tracking.getDescription();
                } else {
                    message = tracking.getTypeString();
                }
                intent.setAction(ACTION_LAUNCH_TRACKING);
                break;
            case PushNotification.TYPE_TRACKING_REMOVE:
                title = "Tracking removed";
                tracking = pushNotification.getTracking().getTracking();
                if (messagePreview) {
                    title += " - " + tracking.getTypeString() + " - " + tracking.getTitle();
                    message = tracking.getDescription();
                } else {
                    message = tracking.getTypeString();
                }
                intent.setAction(ACTION_LAUNCH_TRACKING);
                break;
            case PushNotification.TYPE_MESSAGE:
                ChatMessage chatMessage = pushNotification.getMessage().getMessage();
                /*if (inForeground) {
                    //If app is foreground, it is handled by firebase listeners
                    //Repository.getInstance().addMessage(chatMessage);
                } else {
                    try {
                        DatabaseHelper.getInstance().addMessage(chatMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
                String name;
                try {
                    name = Repository.getInstance().getNameByUserId( (Babyguard_Application.isTeacher()) ? chatMessage.getKid() : chatMessage.getTeacher());
                    if (messagePreview) {
                        title = name;
                        message = chatMessage.getMessage();
                    } else {
                        title = "New message";
                        message = name;
                    }
                } catch (Exception e) {
                    title = "New message";
                    message = "";
                }
                intent.setAction(ACTION_LAUNCH_CHAT);
                break;
        }
        String[] splittedActivity = currentActivity.split("_");
        if (!(currentActivity.startsWith("Chat_Fragment_") &&
                splittedActivity[2].equals(pushNotification.getFromUser()))) { //User is not in the chat

            intent.putExtra("from", pushNotification.getFromUser());
            intent.putExtra("to", pushNotification.getToUser());
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            int notificationId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
            sendNotification(notificationId, title,message, pendingIntent);
        }
    }

    private void sendNotification(int notificationId, String title, String messageBody, PendingIntent pendingIntent) {

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String vibrationCode = SettingsManager.getStringPreference(SettingsManager.getKeyPreferenceByResourceId(R.string.notifications_vibration_pref),"0");
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_notification)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setPriority(2) //Max
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        android.app.Notification defaultsValuesNotif = new android.app.Notification();
        long[] vibration;
        if (vibrationCode.equals("1")) { //Default
            defaultsValuesNotif.defaults |= android.app.Notification.DEFAULT_VIBRATE;
        } else {
            switch (vibrationCode) {
                case "0":  //Deactivated
                    vibration = new long[]{0};
                    break;
                case "2":  //Short
                    vibration = new long[]{0, 300, 100, 300};
                    break;
                default:  //Long
                    vibration = new long[]{0, 700, 300, 700};
                    break;
            }
            notificationBuilder.setVibrate(vibration);
        }
        defaultsValuesNotif.defaults |= android.app.Notification.DEFAULT_LIGHTS;
        defaultsValuesNotif.defaults |= android.app.Notification.DEFAULT_SOUND;
        notificationBuilder.setDefaults(defaultsValuesNotif.defaults);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    public static class NotificationActionService extends IntentService {
        public NotificationActionService() {
            super(NotificationActionService.class.getSimpleName());
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            String currentActivity = Babyguard_Application.getCurrentActivity();
            boolean inForeground = currentActivity != null && !currentActivity.equals("");
            String action = intent.getAction();
            if (ACTION_LAUNCH_CALENDAR.equals(action)) {

            } else if (ACTION_LAUNCH_TRACKING.equals(action)) {
            } else if (ACTION_LAUNCH_CHAT.equals(action)) {
            }
            Intent aintent = new Intent(this, Login_Activity.class);
            startActivity(aintent);
        }
    }
}
