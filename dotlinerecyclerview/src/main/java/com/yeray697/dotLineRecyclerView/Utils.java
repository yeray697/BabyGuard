package com.yeray697.dotLineRecyclerView;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by yeray697 on 25/12/16.
 */

public class Utils {
    public static float dpToPx(Context context, int number) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, number, context.getResources().getDisplayMetrics());
    }
}
