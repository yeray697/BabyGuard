package com.phile.babyguard.repository;

import android.content.Context;

import com.phile.babyguard.model.Kid;
import com.phile.babyguard.presenter.KidListPresenterImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by usuario on 19/12/16.
 */

public class Repository {
    static List<Kid> kids;
    private static KidListPresenterImpl.KidList mCallback;
    Context context;
    private static Repository repository;

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

    public List<Kid> getKids() {
        return kids;
    }

    public Kid getKidById(String id_kid) {
        Kid kid = null;
        int count = 0;
        while (kid == null){
            if (kids.get(count).getIdKid().equals(id_kid))
                kid = kids.get(count);
            count++;
        }
        return kid;
    }
}
