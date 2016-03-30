package com.xyn.appdetial.util;

import com.xyn.appdetial.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/3/29 0029.
 */
public class StringHelper {

    public static String formateTime(long time) {
        long diff = System.currentTimeMillis() - time;
        if (diff < 60 * 1000) {
            return ResHelper.getString(R.string.just_now);
        }
        if (diff < 60 * 60 * 1000) {
            return ResHelper
                    .getString(R.string.minutes_ago, diff / (60 * 1000));
        }
        if (diff < 24 * 60 * 60 * 1000) {
            return ResHelper.getString(R.string.hours_ago, diff
                    / (60 * 60 * 1000));
        }
        if (diff < 20 * 24 * 60 * 60 * 1000) {
            return ResHelper.getString(R.string.days_ago, diff
                    / (24 * 60 * 60 * 1000));
        }
        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        cal.setTimeInMillis(time);
        String fs = "MM-dd HH:mm";
        if (y != cal.get(Calendar.YEAR)) {
            fs = "yyyy-MM-dd HH:mm";
        }
        SimpleDateFormat mdhm = new SimpleDateFormat(fs, Locale.CHINA);
        return mdhm.format(new Date(time));
    }
}
