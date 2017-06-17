package com.ncatz.babyguard.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.ncatz.babyguard.Login_Activity;
import com.ncatz.babyguard.R;
import com.ncatz.babyguard.model.NurseryClass;
import com.ncatz.babyguard.model.NurserySchool;
import com.ncatz.babyguard.preferences.SettingsManager;
import com.ncatz.babyguard.repository.Repository;
import com.ncatz.yeray.calendarview.DiaryCalendarEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yeray697 on 17/06/17.
 */

public class CalendarService extends Service {
        private static final long DELAY = 5000;
        private Date today, tomorrow, aftertomorrow;
        private final int CALENDAR_NOTIFICATION = 1;
        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(final Intent intent, int flags, int startId) {
            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY, 0);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.MILLISECOND, 0);
                    today = c.getTime();
                    c.add(Calendar.DAY_OF_MONTH,1);
                    tomorrow = c.getTime();
                    c.add(Calendar.DAY_OF_MONTH,1);
                    aftertomorrow = c.getTime();
                    NurserySchool nurserySchool = Repository.getInstance().getNurserySchool();
                    if (nurserySchool != null){
                        Date date;
                        for (NurseryClass nurseryClass : nurserySchool.getNurseryClassesList())
                        {
                            for (DiaryCalendarEvent diaryCalendarEvents: nurseryClass.getCalendar()) {
                                try {
                                    date = simpleDateFormat.parse(diaryCalendarEvents.getDate());
                                    if (isToday(date)) {
                                        sendNotification(true, diaryCalendarEvents);
                                    } else if (isTomorrow(date)) {
                                        sendNotification(false, diaryCalendarEvents);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                }
            },1);
            return START_STICKY;
        }

        private void sendNotification(boolean isToday, DiaryCalendarEvent event) {
            Intent newIntent = new Intent(this, Login_Activity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,CALENDAR_NOTIFICATION,newIntent,0);

            NotificationCompat.BigTextStyle notiStyle = new
                    NotificationCompat.BigTextStyle();
            notiStyle.setBigContentTitle(event.getTitle());
            String date = (isToday) ? "Today" : "Tomorrow";
            notiStyle.setSummaryText(date + " - " + event.getTitle() + " - " + event.getDate());
            notiStyle.bigText(event.getDescription());


            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            String vibrationCode = SettingsManager.getStringPreference(SettingsManager.getKeyPreferenceByResourceId(R.string.notifications_vibration_pref),"0");
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_notification)
                    .setContentTitle(event.getTitle())
                    .setContentText(date + " - " + event.getDate())
                    .setAutoCancel(true)
                    .setPriority(2) //Max
                    .setSound(defaultSoundUri)
                    .setStyle(notiStyle)
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
            int notificationId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
            notificationManager.notify(notificationId, notificationBuilder.build());
        }

    private boolean isToday(Date date) {
        return (date.equals(today) || date.after(today)) && date.before(tomorrow);
    }

    private boolean isTomorrow(Date date) {
        return (date.equals(tomorrow) || date.after(tomorrow)) && date.before(aftertomorrow);
        //return date.equals(tomorrow) || (date.after(tomorrow) && date.before(today));
    }
}