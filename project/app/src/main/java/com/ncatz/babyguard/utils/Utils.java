package com.ncatz.babyguard.utils;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.ncatz.babyguard.Babyguard_Application;
import com.ncatz.babyguard.R;
import com.ncatz.babyguard.model.ChatMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.TimeZone;


/**
 * Class with useful methods
 *
 * @author Yeray Ruiz Juárez
 * @version 1.0
 */
public class Utils {
    /**
     * Method that check if device has internet
     *
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
     *
     * @param activity Activity that is active
     */
    public static void hideKeyboard(Activity activity) {
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

    public static ArrayList<ChatMessage> parseChatMessageToChat(ArrayList<ChatMessage> messages) {
        Collections.sort(messages, ChatMessage.comparator);
        ArrayList<ChatMessage> newMessages = new ArrayList<>();
        String lastDate = "";
        int size = messages.size();
        ChatMessage messageAux;
        ChatMessage auxDate;
        boolean isTodayReached = false;
        for (int i = 0; i < size; i++) {
            messageAux = messages.get(i);

            if (!isUnixToday(messageAux.getDatetime())) {
                if (lastDate.equals("")) {
                    lastDate = messageAux.getDatetime();
                    auxDate = new ChatMessage();
                    auxDate.setIfIsMessage(false);
                    auxDate.setDatetime(lastDate);
                    newMessages.add(auxDate);
                } else {
                    if (!isSameDay(lastDate, messageAux.getDatetime())) {
                        auxDate = new ChatMessage();
                        lastDate = messageAux.getDatetime();
                        auxDate.setIfIsMessage(false);
                        auxDate.setDatetime(lastDate);
                        newMessages.add(auxDate);
                    }
                }
            } else {
                if (!isTodayReached) {
                    auxDate = new ChatMessage();
                    auxDate.setIfIsMessage(false);
                    auxDate.setDatetime(messageAux.getDatetime());
                    newMessages.add(auxDate);
                    isTodayReached = true;
                }
            }
            newMessages.add(messageAux);
        }
        return newMessages;
    }

    public static String parseDateToUnix(String date) {
        String unix = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            unix = String.valueOf(sdf.parse(date).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return unix;
    }

    public interface OnAnimationEnded {
        void finishing();

        void finished();
    }

    /**
     * Main source: https://developer.android.com/training/animation/zoom.html
     * Added too: http://stackoverflow.com/questions/15582434/using-a-valueanimator-to-make-a-textview-blink-different-colors
     *
     * @param context       Context
     * @param thumbView     Where is going to be displayed the image
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
        ((Activity) context).findViewById(containerId)
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
        ((View) destination.getParent()).setVisibility(View.VISIBLE);


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
        AnimatorSet setBackground = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.image_zoom_in);

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
                                        View.Y, startBounds.top))
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
                        ((View) destination.getParent()).setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        destination.setVisibility(View.GONE);
                        ((View) destination.getParent()).setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                AnimatorSet setBackground = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.image_zoom_out);

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

    public static String getTimeByUnix(String unixTime) {
        String time = "";
        Long timeUnix = Long.parseLong(unixTime);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeUnix);
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
        time = sdfTime.format(c.getTime());
        return time;
    }

    public static boolean isUnixToday(String unixTime) {
        Calendar today = getTodayDateCalendar();
        ;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(Long.parseLong(unixTime));
        return c.after(today);
    }

    public static boolean isSameDay(String unixTime1, String unixTime2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeInMillis(Long.parseLong(unixTime1));
        cal2.setTimeInMillis(Long.parseLong(unixTime2));
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static String getDateByUnixChatDate(String unixTime) {
        String date;
        Calendar today = getTodayDateCalendar();
        Calendar c = Calendar.getInstance();
        Calendar yesterday = getTodayDateCalendar();
        yesterday.add(Calendar.DATE, -1);

        c.setTimeInMillis(Long.parseLong(unixTime));
        SimpleDateFormat sdf;
        if (c.after(today)) {
            date = Babyguard_Application.getContext().getString(R.string.today);
        } else {
            if (c.after(yesterday)) {
                date = Babyguard_Application.getContext().getString(R.string.yesterday);
            } else {
                sdf = new SimpleDateFormat("dd/MM/yy");
                date = sdf.format(c.getTime());
            }

        }
        return date;
    }

    public static String parseDatetimeToChat(String unixTime) {
        String parsed = "";
        Long timeUnix = Long.parseLong(unixTime);
        Calendar today = getTodayDateCalendar();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeUnix);
        SimpleDateFormat sdf;
        if (c.after(today)) {
            sdf = new SimpleDateFormat("HH:mm");

        } else {
            sdf = new SimpleDateFormat("dd/MM/yy");

        }
        parsed = sdf.format(c.getTime());


        return parsed;
    }

    public static Calendar getTodayDateCalendar() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today;
    }

    public static float dpToPx(float dp) {
        return dp * Babyguard_Application.getContext().getResources().getDisplayMetrics().density;
    }

    public static Bitmap shrinkImage(Bitmap bitmap, int maxDimensionSize) {
        int originalWidth = bitmap.getWidth();
        int originalHeigth = bitmap.getHeight();
        Bitmap shrinked;
        if (originalHeigth > maxDimensionSize || originalWidth > maxDimensionSize) {
            int newWidth, newHeight;
            if (originalHeigth > originalWidth) {
                newHeight = maxDimensionSize;
                newWidth = maxDimensionSize * originalWidth / originalHeigth;
            } else {
                newWidth = maxDimensionSize;
                newHeight = maxDimensionSize * originalHeigth / originalWidth;
            }
            float scaleWidth = ((float) newWidth) / originalWidth;
            float scaleHeight = ((float) newHeight) / originalHeigth;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            shrinked = Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeigth, matrix, false);

        } else {
            shrinked = bitmap;
        }

        return shrinked;
    }

    public static Bitmap uriToBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(Babyguard_Application.getContext().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        return baos.toByteArray();
    }

    public static byte[] prepareImageToUpload(Uri uri, int maxSize) {
        Bitmap bitmap = uriToBitmap(uri);
        byte[] image = null;
        if (bitmap != null) {
            bitmap = shrinkImage(bitmap, maxSize);
            image = bitmapToByteArray(bitmap);
        }
        return image;
    }
}
