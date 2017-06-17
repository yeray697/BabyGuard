package com.ncatz.babyguard.utils;

import android.view.View;

/**
 * Class to handle multiple clicks on views
 */
public interface OneClickMultipleListener {
    /**
     * It works as {@link View.OnClickListener}
     */
    void onClick();

    /**
     * Handle if user click again if method has not finished yet
     */
    void onDoubleClick();
}