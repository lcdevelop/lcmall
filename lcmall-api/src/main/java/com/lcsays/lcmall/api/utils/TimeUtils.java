package com.lcsays.lcmall.api.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @Author lichuang
 * @Date 2021/2/15 4:34 下午
 * @Version 1.0
 */
public class TimeUtils {

    private static final SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+08:00'");
    private static final SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+08:00'");
    private static final SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Calendar getBeginOfToday() {
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        return today;
    }

    public static Date nSecondLater(int n) {
        Date now = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        calendar.add(Calendar.SECOND, n);
        return calendar.getTime();
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

    public static String date2YMD(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = (calendar.get(Calendar.MONTH) + 1);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String ret = String.valueOf(year);
        ret += "-";
        if (month < 10) {
            ret += "0";
        }
        ret += month;
        ret += "-";
        if (day < 10) {
            ret += "0";
        }
        ret += day;
        return ret;
    }

    public static String date2Str(Date date) {
        return format3.format(date);
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

    public static Date timeStr2Date(String timeStr) {
        Timestamp t = Timestamp.valueOf(timeStr);
        return new Date(t.getTime());
    }

    /**
     * timeStr必须是2011-05-09 11:49:45这种格式
     * @param timeStr
     * @return
     */
    public static String timeStr2Rfc3399(String timeStr) {
        Date d = new Date(Timestamp.valueOf(timeStr).getTime());
        return format1.format(d);
    }

    public static Date rfc33992Date(String rfc3399Time) {
        try {
            return format2.parse(rfc3399Time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Long currentTimeStamp() {
        return new Date().getTime();
    }

    public static void main(String args[]) {
        String t = "2021-10-20T00:04:33+08:00";
        System.out.println(TimeUtils.rfc33992Date(t));
    }
}
