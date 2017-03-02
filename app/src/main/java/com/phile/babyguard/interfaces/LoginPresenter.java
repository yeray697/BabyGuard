package com.phile.babyguard.interfaces;

import com.phile.babyguard.model.ErrorClass;

/**
 * Login presenter's interface
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public interface LoginPresenter {
    /**
     * Method used to log in with credentials passed
     * @param username User's email
     * @param password User's pass
     */
    void login(String username, String password);

    /**
     * Used to uninitialize variables
     */
    void onDestroy();

    /**
     * Check if the user was logged the last time he used the app
     * If true, it tries to log in as usual
     */
    void isUserSet();
}
