package com.phile.babyguard.interfaces;

import com.phile.babyguard.model.ErrorClass;

/**
 * @author Yeray Ruiz Juárez
 * @version 1.0
 * Created on 10/12/16
 */
public interface LoginPresenter {
    /**
     * Method used to log in with credentials passed
     * @param username User's email
     * @param password User's pass
     */
    void login(String username, String password);

    void onDestroy();

    /**
     * Check if the user was logged the last time he used the app
     * If true, it tries to log in as usual
     */
    void isUserSet();
}
