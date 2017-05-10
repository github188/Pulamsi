package com.pulamsi.base.baseUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间戳工具
 *
 * @author huqiu.lhq 2013-9-10
 */
public class TimeFromWhen {

    private static SimpleDateFormat sSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    private static Calendar sTargetTime;
    private static Calendar sCcurrentTime;

    static {
        TimeZone gmt8TimeZone = TimeZone.getTimeZone("GMT+08:00");
        sTargetTime = Calendar.getInstance();
        sTargetTime.setTimeZone(gmt8TimeZone);

        sCcurrentTime = Calendar.getInstance();
        sCcurrentTime.setTimeZone(gmt8TimeZone);
    }

    public static String nowString() {
        return sSimpleDateFormat.format(new Date());
    }

    public static String formatTime(String timeStr) {
        long time = 0;
        try {
            time = sSimpleDateFormat.parse(timeStr).getTime();
        } catch (ParseException e) {
        }
        return formatTime(time, System.currentTimeMillis());
    }

    public static long stringToLong(String timeStr) {
        long time = 0;
        try {
            time = sSimpleDateFormat.parse(timeStr).getTime();
        } catch (ParseException e) {
        }
        return time;
    }

    public static String formatTime(long formatTimestamp, long currentTimestamp) {

        sCcurrentTime.setTimeInMillis(currentTimestamp);
        int currentYear = sCcurrentTime.get(Calendar.YEAR);
        int currentDay = sCcurrentTime.get(Calendar.DAY_OF_YEAR);

        sTargetTime.setTimeInMillis(formatTimestamp);

        String format;
        int targetYear = sTargetTime.get(Calendar.YEAR);
        int targetDay = sTargetTime.get(Calendar.DAY_OF_YEAR);
        int temp = Math.abs(targetDay - currentDay);
        int hour;

        Date date = sTargetTime.getTime();
        hour = sTargetTime.get(Calendar.HOUR_OF_DAY);
        hour %= 24;

        if (hour >= 0 && hour < 5) {
            format = "凌晨 HH:mm";
        } else if (hour >= 5 && hour < 11) {
            format = "上午 HH:mm";
        } else if (hour >= 11 && hour < 13) {
            format = "中午 HH:mm";
        } else if (hour >= 13 && hour < 19) {
            format = "下午 HH:mm";
        } else {
            format = "晚上 HH:mm";
        }

        if (temp == 0) { // A.当天
            long diff = currentTimestamp - formatTimestamp;

            diff = diff / 1000;
            if (diff >= 0 && diff <= 10) {
                return "刚刚";
            } else if (diff < 60) {
                return diff + "秒钟前";
            }

            diff = diff / 60;
            if (diff < 60) {
                return diff + "分钟前";// N分钟前
            }

            diff = diff / 60;
            if (diff < 2) {
                return "1小时前";
            }
        } else if (temp <= 2) { // B.昨天、前天

            if (temp == 1) {
                format = "昨天 " + format;
            } else {
                format = "前天 " + format;
            }
        } else { // C.三天之外

            if (targetYear == currentYear) {
                format = "MM月dd日 " + format;
            } else {
                format = "yyyy年MM月dd日 " + format;
            }
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        String timeLabelStr = simpleDateFormat.format(date);
        return timeLabelStr;
    }

    public static String daysAgo(long formatTimestamp, long currentTimestamp) {

        sCcurrentTime.setTimeInMillis(currentTimestamp);
        int currentDay = sCcurrentTime.get(Calendar.DAY_OF_YEAR);
        int currentYear = sCcurrentTime.get(Calendar.YEAR);
        sTargetTime.setTimeInMillis(formatTimestamp);

        int targetYear = sTargetTime.get(Calendar.YEAR);
        int targetDay = sTargetTime.get(Calendar.DAY_OF_YEAR);

        int temp = (currentYear - targetYear) * 365 + currentDay - targetDay;

        if (temp == 0) {
            return "今天";
        } else if (temp == 1) {
            return "昨天";
        } else if (temp == 2) {
            return "前天";
        } else {
            return temp + "天之前";
        }
    }
}
