package com.phile.babyguard.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.phile.babyguard.Babyguard_Application;
import com.phile.babyguard.Home_Activity;
import com.phile.babyguard.KidList_Activity;
import com.phile.babyguard.R;
import com.phile.babyguard.model.Kid;
import com.phile.babyguard.repository.Repository;
import com.yeray697.calendarview.DiaryCalendarEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yeray697 on 5/03/17.
 */

public class CalendarServices extends Service {
    private static final long DELAY = 5000;
    private Date today;
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
                if (((Babyguard_Application)getApplicationContext()).getUser() != null){
                    ArrayList<Kid> kids = (ArrayList<Kid>) Repository.getInstance().getKids();
                    ArrayList<DiaryCalendarEvent> calendarAux;
                    Kid kid;
                    DiaryCalendarEvent event;
                    for (int i = 0; i < kids.size(); i++){
                        kid = kids.get(i);
                        calendarAux =  kids.get(i).getCalendarEvents();
                        for (int j = 1; j < calendarAux.size(); j++){
                            try {
                                event = calendarAux.get(j);
                                if (isToday(simpleDateFormat.parse(event.getDate()))){
                                    sendNotification(kid,event);
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

    private void sendNotification(Kid kid, DiaryCalendarEvent event) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int id = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        Intent newIntent = new Intent(this, Home_Activity.class);
        newIntent.putExtra(KidList_Activity.KID_EXTRA,kid);
        newIntent.putExtra(Home_Activity.ACTION,Home_Activity.ACTION_OPEN_CALENDAR);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,CALENDAR_NOTIFICATION,newIntent,0);

        NotificationCompat.BigTextStyle notiStyle = new
                NotificationCompat.BigTextStyle();
        notiStyle.setBigContentTitle(event.getTitle());
        notiStyle.setSummaryText(kid.getName() + " - " + event.getDate());
        notiStyle.bigText(event.getDescription());

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(event.getTitle());
        builder.setContentText(kid.getName() + " - " + event.getDate());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setStyle(notiStyle);

        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setDefaults(Notification.DEFAULT_LIGHTS);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id,builder.build());




    }

    private boolean isToday(Date date) {
        return date.equals(today);
    }
}
