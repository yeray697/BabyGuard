package com.phile.babyguard.repository;

import android.content.Context;
import android.support.annotation.IntDef;

import com.phile.babyguard.model.InfoKid;
import com.phile.babyguard.model.Kid;
import com.phile.babyguard.presenter.KidListPresenterImpl;

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
    private static List<Kid> kids;
    private static KidListPresenterImpl.KidList mCallback;
    private Context context;
    private static Repository repository;
    private ArrayList<InfoKid> infoKid;

    @IntDef({Sort.CHRONOLOGIC, Sort.CATEGORY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Sort {
        int CHRONOLOGIC = 1;
        int CATEGORY = 2;
    }

    public Repository(Context context, KidListPresenterImpl.KidList mCallback) {
        this.context = context;
        this.mCallback = mCallback;
        kids = new ArrayList<>();
    }

    public static Repository getInstance(Context context, KidListPresenterImpl.KidList mCallback){
        if (repository == null)
            repository = new Repository(context, mCallback);
        loadKidsInfo();
        return repository;
    }

    /**
     * Load user's kids
     */
    private static void loadKidsInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                kids.add(new Kid("1","1","Joselito","https://pbs.twimg.com/profile_images/450103729383956480/Tiys3m4x.jpeg"));
                kids.add(new Kid("2","1","Lola","http://rackcdn.elephantjournal.com/wp-content/uploads/2012/01/bad-kid.jpg"));
                mCallback.OnLoadedKidList();
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

    /**
     * Get a kid by his id
     * @param id_kid Kid's id
     * @return Return the user
     */
    public Kid getKidById(String id_kid) {
        Kid kid = null;
        int count = 0;
        while (kid == null){
            if (kids.get(count).getIdKid().equals(id_kid))
                kid = kids.get(count);
            count++;
        }
        getInfoKidById(id_kid);
        return kid;
    }
    public void getInfoKidById(String id) {
        //TODO base de datos
        infoKid = new ArrayList<>();
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
    }
    public ArrayList<InfoKid> getInfoKidById( @Sort int sortType) {
        ArrayList<InfoKid> newInfoKid = new ArrayList<>(infoKid);
        if (sortType == Sort.CATEGORY) {
            Collections.sort(newInfoKid, InfoKid.CATEGORY);
        } else {
            Collections.sort(newInfoKid, InfoKid.CHRONOLOGIC);
        }
        return newInfoKid;
    }


}
