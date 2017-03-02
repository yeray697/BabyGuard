package com.phile.babyguard.utils;

import android.view.View;

/**
 * Created by yeray697 on 2/03/17.
 * Check it at https://github.com/yeray697/OneClickListener
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