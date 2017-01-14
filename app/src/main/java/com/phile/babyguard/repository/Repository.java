package com.phile.babyguard.repository;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntDef;

import com.phile.babyguard.model.InfoKid;
import com.phile.babyguard.model.Kid;
import com.phile.babyguard.model.NurserySchool;
import com.phile.babyguard.presenter.KidListPresenterImpl;
import com.yeray697.calendarview.CalendarEvent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Repository's singleton
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class Repository {
    private  List<Kid> kids;
    private  KidListPresenterImpl.KidList mCallback;
    private Context context;
    private static Repository repository;
    private NurserySchool nurserySchool;

    @IntDef({Sort.CHRONOLOGIC, Sort.CATEGORY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Sort {
        int CHRONOLOGIC = 1;
        int CATEGORY = 2;
    }

    private Repository(Context context, KidListPresenterImpl.KidList mCallback) {
        this.context = context;
        this.mCallback = mCallback;
        kids = new ArrayList<>();
        loadData();
    }

    public static Repository getInstance(Context context, KidListPresenterImpl.KidList mCallback){
        if (repository == null)
            repository = new Repository(context, mCallback);
        return repository;
    }

    /**
     * Load user's kids
     */
    private void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<InfoKid> infoKid = new ArrayList<>();
                infoKid.add(new InfoKid("","Sleep","12:50", InfoKid.Type.SLEEP,"To perfe"));
                infoKid.add(new InfoKid("","FOOD","11:50", InfoKid.Type.FOOD,"To perfe"));
                infoKid.add(new InfoKid("","FOOD","13:50", InfoKid.Type.FOOD,"To perfe"));
                infoKid.add(new InfoKid("","caca","09:50", InfoKid.Type.POOP,"To perfe"));
                infoKid.add(new InfoKid("","caca","10:50", InfoKid.Type.POOP,"To perfe"));
                infoKid.add(new InfoKid("","Sleep","12:50", InfoKid.Type.SLEEP,"To perfe"));
                infoKid.add(new InfoKid("","FOOD","11:50", InfoKid.Type.FOOD,"To perfe"));
                infoKid.add(new InfoKid("","FOOD","13:50", InfoKid.Type.FOOD,"To perfe"));
                infoKid.add(new InfoKid("","caca","09:50", InfoKid.Type.POOP,"To perfe"));
                infoKid.add(new InfoKid("","caca","10:50", InfoKid.Type.POOP,"To perfe"));
                ArrayList<String> telephone = new ArrayList<>();
                telephone.add("959855555");
                telephone.add("659859878");
                ArrayList<CalendarEvent> calendarEvents;
                calendarEvents = new ArrayList<>();
                calendarEvents.add(new CalendarEvent("Febrero",2017, 1,1,"Empieza febrero"));
                calendarEvents.add(new CalendarEvent("Fiesta",2017,11,31,"Fiesta en tu casa"));
                calendarEvents.add(new CalendarEvent("Año nuevo",2017, 0,1,"Feliz año a todos"));
                calendarEvents.add(new CalendarEvent("Vuelta al cole",2017,0,9,"Pocas vacaciones son"));
                calendarEvents.add(new CalendarEvent("Cumple Raquel",2017,1,28,"Cumpleaños de Raquel"));
                calendarEvents.add(new CalendarEvent("Marzo",2017,2,1,"Empieza febrero"));
                nurserySchool = new NurserySchool("Best guarderia","Calle falsa 123", "bestguarderia@gmail.com","bestguarderia.com", telephone);
                kids.add(new Kid("1","1","Joselito","https://pbs.twimg.com/profile_images/450103729383956480/Tiys3m4x.jpeg","Alérgico a los  cacahuetes",infoKid,calendarEvents));
                kids.add(new Kid("2","1","Lola","http://rackcdn.elephantjournal.com/wp-content/uploads/2012/01/bad-kid.jpg","Alérgico a la lactosa",infoKid,calendarEvents));
                mCallback.OnLoadedData();

            }
        }).start();
    }

    public static Repository getInstance(){
        return repository;
    }

    /**
     * Get user's kids
     * @return User's kids
     */
    public List<Kid> getKids() {
        return kids;
    }

    public ArrayList<InfoKid> getOrderedInfoKid( ArrayList<InfoKid> infoKid, @Sort int sortType) {
        ArrayList<InfoKid> newInfoKid = new ArrayList<>(infoKid);
        if (sortType == Sort.CATEGORY) {
            Collections.sort(newInfoKid, InfoKid.CATEGORY);
        } else {
            Collections.sort(newInfoKid, InfoKid.CHRONOLOGIC);
        }
        return newInfoKid;
    }

    public NurserySchool getNurserySchool(){
        return nurserySchool;
    }

}
