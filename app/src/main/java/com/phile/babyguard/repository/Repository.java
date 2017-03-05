package com.phile.babyguard.repository;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntDef;

import com.phile.babyguard.database.DatabaseContract;
import com.phile.babyguard.database.DatabaseManager;
import com.phile.babyguard.model.InfoKid;
import com.phile.babyguard.model.Kid;
import com.phile.babyguard.model.NurserySchool;
import com.phile.babyguard.presenter.KidListPresenterImpl;
import com.yeray697.calendarview.DiaryCalendarEvent;

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
    private static Repository repository;
    private NurserySchool nurserySchool;

    public void update() {
        kids.clear();
        String nurserId = "1";
        kids.add(DatabaseManager.getInstance().getKid("1",nurserId));
        kids.add(DatabaseManager.getInstance().getKid("2",nurserId));
        nurserySchool = DatabaseManager.getInstance().getNursery(nurserId);
    }

    @IntDef({Sort.CHRONOLOGIC, Sort.CATEGORY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Sort {
        int CHRONOLOGIC = 1;
        int CATEGORY = 2;
    }

    private Repository() {
        kids = new ArrayList<>();
    }

    public static Repository getInstance(){
        if (repository == null) {
            repository = new Repository();
            repository.update();
        }
        return repository;
    }
    public void setCallback(KidListPresenterImpl.KidList callback) {
        this.mCallback = callback;
    }

    /**
     * Load user's kids
     */
    public void loadData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                kids.clear();
                String nurserId = "1";
                kids.add(DatabaseManager.getInstance().getKid("1",nurserId));
                kids.add(DatabaseManager.getInstance().getKid("2",nurserId));
                nurserySchool = DatabaseManager.getInstance().getNursery(nurserId);
                mCallback.OnLoadedData();

            }
        }).start();
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
