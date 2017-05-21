package com.ncatz.babyguard.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.ncatz.babyguard.Babyguard_Application;
import com.ncatz.babyguard.R;
import com.ncatz.babyguard.firebase.FirebaseManager;
import com.ncatz.babyguard.interfaces.LoginPresenter;
import com.ncatz.babyguard.interfaces.LoginView;
import com.ncatz.babyguard.model.ErrorClass;
import com.ncatz.babyguard.model.UserCredentials;
import com.ncatz.babyguard.utils.Utils;

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
            view.setMessageError(error.getMessageError((Context) view,error.getCode()),error.getIdView());
        }
        if (TextUtils.isEmpty(password)) {
            error.setCode(ErrorClass.PASS_EMPTY);
            error.setIdView(idViewPass);
            error.setIfThereIsAnError(true);
            view.setMessageError(error.getMessageError((Context) view,error.getCode()),error.getIdView());
        }
        if (!error.isThereAnError()) {
            databaseLogin(username,password);
        }
    }

    @Override
    public void onDestroy() {
        this.view = null;
        this.onLoginFinishedListener = null;
    }

    @Override
    public void isUserSet() {
        UserCredentials userCredentials = ((Babyguard_Application)((Context)view).getApplicationContext()).getUserCredentials();
        if (userCredentials != null && !TextUtils.isEmpty(userCredentials.getUser())) {
            view.setCredentials(userCredentials.getUser(), userCredentials.getPass());
            login(userCredentials.getUser(), userCredentials.getPass());
        }
    }

    /**
     * Tries to login
     * @param mail UserCredentials's mail
     * @param password UserCredentials's pass
     */
    private void databaseLogin(final String mail, final String password) {
        FirebaseManager.getInstance().getFirebaseAuth().signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    ((Babyguard_Application) ((Context) view).getApplicationContext()).setUserCredentials(new UserCredentials(mail, password));
                    ((Babyguard_Application) ((Context) view).getApplicationContext()).addLoginListener(new Babyguard_Application.ActionEndListener() {
                        @Override
                        public void onEnd() {
                            onLoginFinishedListener.onSuccess();
                            ((Babyguard_Application) ((Context) view).getApplicationContext()).removeLoginListener();
                        }
                    });
                    FirebaseManager.getInstance().getUserInfo(mail);
                } else {
                    ErrorClass error = new ErrorClass();
                    error.setIdView(ErrorClass.VIEW_TOAST);
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                        error.setCode(ErrorClass.INCORRECT);
                    else
                        error.setCode(ErrorClass.SERVER_CONNECTION_ERROR);
                    view.setMessageError(error.getMessageError((Context) view, error.getCode()), error.getIdView());
                }
            }
        });
    }

}
