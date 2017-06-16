package com.ncatz.babyguard.services;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ncatz.babyguard.Babyguard_Application;
import com.ncatz.babyguard.Login_Activity;
import com.ncatz.babyguard.R;
import com.ncatz.babyguard.database.DatabaseHelper;
import com.ncatz.babyguard.model.ChatMessage;
import com.ncatz.babyguard.model.Notification;
import com.ncatz.babyguard.model.TrackingKid;
import com.ncatz.babyguard.preferences.SettingsManager;
import com.ncatz.babyguard.repository.Repository;
import com.ncatz.yeray.calendarview.DiaryCalendarEvent;

import java.util.Date;
import java.util.List;
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
        Notification notification = Notification.parseReceivedNotif(data);
        boolean messagePreview = SettingsManager.getBooleanPreference(SettingsManager.getKeyPreferenceByResourceId(R.string.notifications_preview_pref),true);
        String title = "",
                message = "";
        DiaryCalendarEvent event = null;
        TrackingKid tracking = null;
        Intent intent = new Intent(this, NotificationActionService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        switch (notification.getType()) {
            case Notification.TYPE_CALENDAR_ADD:
                title = "Event added";
                event = notification.getCalendar().getEvent();
                if (messagePreview) {
                    title += " - " + event.getTitle() + " - " + event.getDate();
                    message = event.getDescription();
                } else {
                    message = event.getDate();
                }
                intent.setAction(ACTION_LAUNCH_CALENDAR);
                break;
            case Notification.TYPE_CALENDAR_EDIT:
                title = "Event edited";
                event = notification.getCalendar().getEvent();
                if (messagePreview) {
                    title += " - " + event.getTitle() + " - " + event.getDate();
                    message = event.getDescription();
                } else {
                    message = event.getDate();
                }
                intent.setAction(ACTION_LAUNCH_CALENDAR);
                break;
            case Notification.TYPE_CALENDAR_REMOVE:
                title = "Event removed";
                event = notification.getCalendar().getEvent();
                if (messagePreview) {
                    title += " - " + event.getTitle() + " - " + event.getDate();
                    message = event.getDescription();
                } else {
                    message = event.getDate();
                }
                intent.setAction(ACTION_LAUNCH_CALENDAR);
                break;
            case Notification.TYPE_TRACKING_ADD:
                title = "Tracking added";
                tracking = notification.getTracking().getTracking();
                if (messagePreview) {
                    title += " - " + tracking.getTypeString();
                    message = tracking.getDescription();
                } else {
                    message = tracking.getTypeString();
                }
                intent.setAction(ACTION_LAUNCH_TRACKING);
                break;
            case Notification.TYPE_TRACKING_EDIT:
                title = "Tracking edited";
                tracking = notification.getTracking().getTracking();
                if (messagePreview) {
                    title += " - " + tracking.getTypeString();
                    message = tracking.getDescription();
                } else {
                    message = tracking.getTypeString();
                }
                intent.setAction(ACTION_LAUNCH_TRACKING);
                break;
            case Notification.TYPE_TRACKING_REMOVE:
                title = "Tracking removed";
                tracking = notification.getTracking().getTracking();
                if (messagePreview) {
                    title += " - " + tracking.getTypeString();
                    message = tracking.getDescription();
                } else {
                    message = tracking.getTypeString();
                }
                intent.setAction(ACTION_LAUNCH_TRACKING);
                break;
            case Notification.TYPE_MESSAGE:
                ChatMessage chatMessage = notification.getMessage().getMessage();
                if (inForeground) {
                    //If app is foreground, it is handled by firebase listeners
                    //Repository.getInstance().addMessage(chatMessage);
                } else {
                    try {
                        DatabaseHelper.getInstance().addMessage(chatMessage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                String name;
                try {
                    name = Repository.getInstance().getNameByUserId( (Babyguard_Application.isTeacher()) ? chatMessage.getKid() : chatMessage.getTeacher());
                } catch (Exception e) {
                    name =
                }
                if (messagePreview) {
                    title = name;
                    message = chatMessage.getMessage();
                } else {
                    title = "New message";
                    message = name;
                }
                intent.setAction(ACTION_LAUNCH_CHAT);
                break;
        }
        intent.putExtra("from",notification.getFrom());
        intent.putExtra("to",notification.getTo());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        int notificationId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        sendNotification(notificationId, title,message, pendingIntent);
    }

    private void sendNotification(int notificationId, String title, String messageBody, PendingIntent pendingIntent) {

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String vibrationCode = SettingsManager.getStringPreference(SettingsManager.getKeyPreferenceByResourceId(R.string.notifications_vibration_pref),"0");
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
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
                // If you want to cancel the notification: NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
            } else if (ACTION_LAUNCH_TRACKING.equals(action)) {
                // TODO: handle action 1.
                // If you want to cancel the notification: NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
            } else if (ACTION_LAUNCH_CHAT.equals(action)) {
                // TODO: handle action 1.
                // If you want to cancel the notification: NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
            }
        }
    }
}
