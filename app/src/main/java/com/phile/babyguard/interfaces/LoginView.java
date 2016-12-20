package com.phile.babyguard.interfaces;

/**
 * Login view's interface
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public interface LoginView {
    /**
     * Show an error on the device
     * @param messageError ErrorClass that will be showed
     * @param idView View where error will be showed. idView = ErrorClass.VIEW_TOAST will show the error on a Toast
     */
    void setMessageError(String messageError, int idView);
    /**
     * Set credentials passed as variable.
     * That credentials are set if user was logged the last time he used the app
     * @param email User's email
     * @param pass User's pass
     */
    void setCredentials(String email, String pass);
}
