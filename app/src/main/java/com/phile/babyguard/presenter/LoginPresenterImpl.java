package com.phile.babyguard.presenter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.phile.babyguard.R;
import com.phile.babyguard.interfaces.LoginPresenter;
import com.phile.babyguard.interfaces.LoginView;
import com.phile.babyguard.model.ErrorClass;
import com.phile.babyguard.utils.Utils;

/**
 * Created by yeray697 on 10/12/16.
 */

public class LoginPresenterImpl implements LoginPresenter {
    private LoginView view;
    private int idViewUser;
    private int idViewPass;
    private OnLoginFinishedListener onLoginFinishedListener;

    public LoginPresenterImpl(LoginView view, OnLoginFinishedListener onLoginFinishedListener) {
        this.view = view;
        this.onLoginFinishedListener = onLoginFinishedListener;
        this.idViewUser = R.id.tilUser_login;
        this.idViewPass = R.id.tilPass_login;

    }

    public interface OnLoginFinishedListener{
        void onFailure(ErrorClass error);

        void onSuccess();
    }
    @Override
    public void login(String username, String password) {
        ErrorClass error = new ErrorClass();
        if (TextUtils.isEmpty(username)) {
            error.setCode(ErrorClass.USER_EMPTY);
            error.setIdView(idViewUser);
            error.setIfThereIsAnError(true);
        } else if (TextUtils.isEmpty(password)) {
            error.setCode(ErrorClass.PASS_EMPTY);
            error.setIdView(idViewPass);
            error.setIfThereIsAnError(true);
        } else {
            if (Utils.isNetworkAvailable((Context) view)) {
                databaseLogin(username,password);
            } else {
                error.setCode(ErrorClass.USER_CONNECTION_ERROR);
                error.setIdView(ErrorClass.VIEW_TOAST);
                error.setIfThereIsAnError(true);
                onLoginFinishedListener.onFailure(error);
            }
        }
        if (error.isThereAnError())
            view.setMessageError(error.getMessageError((Context) view,error.getCode()),error.getIdView());
    }

    private void databaseLogin(String username, String password) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean error = false;
                if (!error)
                    onLoginFinishedListener.onSuccess();
                else
                    onLoginFinishedListener.onFailure(null);
            }
        }, 2000);
    }

    @Override
    public void onDestroy() {
        this.view = null;
        this.onLoginFinishedListener = null;
    }

    @Override
    public void isUserSet() {
        /*String email = ((Login_Application)((Context)view).getApplicationContext()).getEmailIfExists();
        String pass = ((Login_Application)((Context)view).getApplicationContext()).getPassIfExists();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
            view.setCredentials(email,pass);
            login(email,pass);
        }*/
    }

}
