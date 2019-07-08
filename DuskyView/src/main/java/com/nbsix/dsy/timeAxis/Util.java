package com.nbsix.dsy.timeAxis;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    /**
     * format date
     * @param time :ms
     * @return
     */
    public static String LongtoStringFormat(long time) {
        Date currentTime = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd a HH:mm");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public static int Dp2Px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int Sp2Px(Context context, float spValue) {
        return (int) (spValue * context.getResources().getDisplayMetrics().scaledDensity + 0.5f);
    }
}
