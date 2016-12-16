package com.phile.babyguard.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Class with useful methods
 * @author Yeray Ruiz Juárez
 * @version 1.0
 * Created on 04/11/16
 */
public class Utils {
    /**
     * Method that check if device has internet
     * @param context Context of the application
     * @return Return a boolean with the result
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    /**
     * Method that hide the keyboard if it is possible
     * @param activity Activity that is active
     */
    public static void hideKeyboard(Activity activity){
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}