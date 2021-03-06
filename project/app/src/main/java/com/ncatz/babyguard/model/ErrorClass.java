package com.ncatz.babyguard.model;

import android.content.Context;

import com.ncatz.babyguard.utils.ErrorMapUtils;

/**
 * Error class with all errors
 *
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class ErrorClass {
    //COMMON ERRORS
    public static final int INCORRECT = 1;                  //Login: UserCredentials and/or password is incorrect
    public static final int SERVER_CONNECTION_ERROR = 2;    //Unable to connect to server
    //Individual log in errors
    public static final int USER_EMPTY = 10;               //LogIn: UserCredentials field is empty
    public static final int PASS_EMPTY = 11;                //LogIn: Pass field is empty

    private int code;
    private int idView;
    private boolean isThereAnError;

    public static final int VIEW_TOAST = 1;

    public ErrorClass() {
        this.setIfThereIsAnError(false);
    }

    public ErrorClass(int idView, int codeError) {
        setIdView(idView);
        setCode(codeError);
        this.setIfThereIsAnError(false);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getIdView() {
        return idView;
    }

    public void setIdView(int idView) {
        this.idView = idView;
    }

    public static String getMessageError(Context context, int code) {
        String message = ErrorMapUtils.getErrorMap(context).get(String.valueOf(code));
        message = context.getResources().getString(context.getResources().getIdentifier(message, "string", context.getPackageName()));
        return message;
    }

    public boolean isThereAnError() {
        return this.isThereAnError;
    }

    public void setIfThereIsAnError(boolean thereAnError) {
        this.isThereAnError = thereAnError;
    }
}
