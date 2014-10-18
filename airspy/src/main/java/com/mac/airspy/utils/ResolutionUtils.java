package com.mac.airspy.utils;

import android.content.Context;

/**
 * Created by Maciej on 2014-10-17.
 */
public class ResolutionUtils {
    public static int dpToPx(Context ctx, int dp) {
        return (int) (ctx.getResources().getDisplayMetrics().density * dp);
    }
}
