package com.phile.babyguard.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.phile.babyguard.Babyguard_Application;
import com.phile.babyguard.R;
import com.phile.babyguard.interfaces.LoginPresenter;
import com.phile.babyguard.interfaces.LoginView;
import com.phile.babyguard.model.ErrorClass;
import com.phile.babyguard.model.User;
import com.phile.babyguard.utils.Utils;

/**
 * Login presenter
 * @author Yeray Ruiz Juárez
 * @version 1.0
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

    /**
     * Used when login is finished (with or without error)
     */
    public interface OnLoginFinishedListener{
        /**
         * Used to start the next activity
         */
        void onSuccess();
    }

    @Override
    public void login(String username, String password) {
        Utils.hideKeyboard((Activity) view);
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
                view.setMessageError(error.getMessageError((Context) view,error.getCode()),error.getIdView());
            }
        }
        if (error.isThereAnError())
            view.setMessageError(error.getMessageError((Context) view,error.getCode()),error.getIdView());
    }

    @Override
    public void onDestroy() {
        this.view = null;
        this.onLoginFinishedListener = null;
    }

    @Override
    public void isUserSet() {
        User user = ((Babyguard_Application)((Context)view).getApplicationContext()).getUser();
        if (user != null && !TextUtils.isEmpty(user.getUser())) {
            view.setCredentials(user.getUser(),user.getPass());
            login(user.getUser(),user.getPass());
        }
    }

    /**
     * Tries to login
     * @param username User's name
     * @param password User's pass
     */
    private void databaseLogin(final String username, final String password) {
        if (username.equals("admin") && password.equals("Aa123456")) {
            ((Babyguard_Application)((Context)view).getApplicationContext()).setUser(new User(username,password,User.TYPE_TEACHER));
            onLoginFinishedListener.onSuccess();
        } else if (username.equals("lourdes") && password.equals("Aa123456")) {
            ((Babyguard_Application)((Context)view).getApplicationContext()).setUser(new User(username,password, User.TYPE_BABY));
            onLoginFinishedListener.onSuccess();
        } else {
            ErrorClass error = new ErrorClass();
            error.setIdView(ErrorClass.VIEW_TOAST);
            error.setCode(ErrorClass.INCORRECT);
            view.setMessageError(error.getMessageError((Context) view,error.getCode()),error.getIdView());
        }
    }
}
