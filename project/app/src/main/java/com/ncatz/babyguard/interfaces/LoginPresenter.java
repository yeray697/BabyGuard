package com.ncatz.babyguard.interfaces;

/**
 * Login presenter's interface
 *
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public interface LoginPresenter {
    /**
     * Method used to log in with credentials passed
     *
     * @param username UserCredentials's email
     * @param password UserCredentials's pass
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
