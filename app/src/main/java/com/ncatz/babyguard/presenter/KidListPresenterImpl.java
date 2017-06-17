package com.ncatz.babyguard.presenter;

import android.content.Context;

import com.ncatz.babyguard.Babyguard_Application;
import com.ncatz.babyguard.firebase.FirebaseManager;
import com.ncatz.babyguard.interfaces.KidList_Presenter;
import com.ncatz.babyguard.interfaces.KidList_View;

/**
 * KidList presenter
 *
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class KidListPresenterImpl implements KidList_Presenter {
    private KidList_View view;
    private Babyguard_Application.ActionEndListener mCallback;

    @Override
    public void addListener() {
        ((Babyguard_Application) ((Context) view).getApplicationContext()).addKidListListener(mCallback);
    }

    @Override
    public void removeListener() {
        ((Babyguard_Application) ((Context) view).getApplicationContext()).removeKidListListener();
    }

    @Override
    public void startLoading() {
        String mail = ((Babyguard_Application) ((Context) view).getApplicationContext()).getUserCredentials().getUser();
        FirebaseManager.getInstance().getUserInfo(mail);
    }

    public KidListPresenterImpl(final KidList_View view) {
        this.view = view;
        mCallback = new Babyguard_Application.ActionEndListener() {
            @Override
            public void onEnd() {
                view.setKids();
            }
        };
    }

}
