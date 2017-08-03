package com.jbnm.homehero.util;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by janek on 8/2/17.
 */

public class DateUtil {
    private DateUtil() {}

    public static long truncateDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long daysSince(long date) {
        long today = truncateDate(new Date());
        long diff = today - date;
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
