package com.ncatz.babyguard.utils;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ncatz.babyguard.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Class with useful methods
 * @author Yeray Ruiz Juárez
 * @version 1.0
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
        return networkInfo != null && networkInfo.isConnected();
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


    private static Animator mCurrentAnimator;
    private static int mShortAnimationDuration = -1;

    public static void cancelZoomedImage(ImageView ivExpandedImage) {
        ivExpandedImage.performClick();
    }

    public static String encryptPassword(String password) {
        return password;
    }

    public interface OnAnimationEnded{
        void finishing();
        void finished();
    }

    /**
     * Main source: https://developer.android.com/training/animation/zoom.html
     * Added too: http://stackoverflow.com/questions/15582434/using-a-valueanimator-to-make-a-textview-blink-different-colors
     * @param context Context
     * @param thumbView Where is going to be displayed the image
     * @param imageDrawable Image that is going to be displayed
     */
    public static void zoomImageFromThumb(final Context context, int containerId, final View thumbView, final ImageView destination, Drawable imageDrawable, final OnAnimationEnded onAnimationEnded) {

        mShortAnimationDuration = context.getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        destination.setImageDrawable(imageDrawable);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();


        thumbView.getGlobalVisibleRect(startBounds);
        ((Activity)context).findViewById(containerId)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);


        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        destination.setVisibility(View.VISIBLE);
        ((View)destination.getParent()).setVisibility(View.VISIBLE);


        destination.setPivotX(0f);
        destination.setPivotY(0f);


        AnimatorSet set = new AnimatorSet();

        set.setTarget(destination);
        set
                .play(ObjectAnimator.ofFloat(destination, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(destination, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(destination, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(destination,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        AnimatorSet setBackground = (AnimatorSet) AnimatorInflater.loadAnimator(context,R.animator.image_zoom_in);

        setBackground.setTarget(destination.getParent());

        setBackground.setInterpolator(new DecelerateInterpolator());
        setBackground.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        setBackground.start();
        mCurrentAnimator = set;

        final float startScaleFinal = startScale;
        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }
                onAnimationEnded.finishing();

                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(destination, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(destination,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(destination,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(destination,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        destination.setVisibility(View.GONE);
                        ((View)destination.getParent()).setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        destination.setVisibility(View.GONE);
                        ((View)destination.getParent()).setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                AnimatorSet setBackground = (AnimatorSet) AnimatorInflater.loadAnimator(context,R.animator.image_zoom_out);

                setBackground.setTarget(destination.getParent());

                setBackground.setInterpolator(new DecelerateInterpolator());
                setBackground.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mCurrentAnimator = null;
                        onAnimationEnded.finished();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        mCurrentAnimator = null;
                    }
                });
                setBackground.start();
                mCurrentAnimator = set;
            }
        });
    }

    public static String getTimeByUnix(String unixTime){
        String time = "";
        Long timeUnix = Long.parseLong(unixTime);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeUnix);
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        time = sdfTime.format(c.getTime());
        return time;
    }

    public static String getDateByUnix(String unixTime){
        String date = "";
        Long timeUnix = Long.parseLong(unixTime);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeUnix);
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
        date = sdfDate.format(c.getTime());
        return date;
    }

    public static String getTimeNow(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        sdf.setTimeZone(c.getTimeZone());
        return sdf.format(c.getTime());
    }

    public static String getTodayDate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_dd");
        sdf.setTimeZone(c.getTimeZone());
        return sdf.format(c.getTime());
    }
}
