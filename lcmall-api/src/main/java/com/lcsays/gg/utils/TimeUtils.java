package com.lcsays.gg.utils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author lichuang
 * @Date 2021/2/15 4:34 下午
 * @Version 1.0
 */
public class TimeUtils {

    public static Calendar getBeginOfToday() {
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today;
    }

    public static long getBeginOfTodayTimestamp() {
        return TimeUtils.getBeginOfToday().getTimeInMillis();
    }

    public static int daysOfMillionSecond(long ms) {
        return (int) (ms / 86400000);
    }

    public static Calendar date2Calendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Calendar timestamp2Calendar(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp.getTime()));
        return calendar;
    }

    /**
     * timeStr必须是2011-05-09 11:49:45这种格式
     * @param timeStr
     * @return
     */
    public static Timestamp timeStr2Timestamp(String timeStr) {
        return Timestamp.valueOf(timeStr);
    }
}
