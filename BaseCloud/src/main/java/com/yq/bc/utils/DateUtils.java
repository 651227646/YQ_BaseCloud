package com.yq.bc.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 成都一方思致科技有限公司
 *
 * @author yuanqi
 * @description 日期处理工具包
 * @date 2020/1/19
 */
public class DateUtils {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public DateUtils() {
    }

    public static Date parseDate(String dateStr, String format) throws ParseException {
        DateFormat df = new SimpleDateFormat(format);
        Date date = df.parse(dateStr);
        return date;
    }

    public static Date parseDate(String dateStr, String format, Date defaultValue) {
        DateFormat df = new SimpleDateFormat(format);
        Date date = null;

        try {
            date = df.parse(dateStr);
        } catch (Exception var6) {
            date = defaultValue;
        }

        return date;
    }

    public static Date parseDate(String dateStr) throws ParseException {
        return parseDate(dateStr, "yyyy-MM-dd");
    }

    public static Date parseDate(String dateStr, Date defaultValue) {
        return parseDate(dateStr, "yyyy-MM-dd", defaultValue);
    }

    public static Date parseTime(String timeStr) throws ParseException {
        return parseDate(timeStr, "HH:mm:ss");
    }

    public static Date parseTime(String timeStr, Date defaultValue) {
        return parseDate(timeStr, "HH:mm:ss", defaultValue);
    }

    public static Date parseDateTime(String dateTimeStr) throws ParseException {
        return parseDate(dateTimeStr, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parseDateTime(String dateTimeStr, Date defaultValue) {
        return parseDate(dateTimeStr, "yyyy-MM-dd HH:mm:ss", defaultValue);
    }

    public static String formatDate(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    public static String formatTime(Date date) {
        return formatDate(date, "HH:mm:ss");
    }

    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parseDate(long utcTime) throws ParseException {
        return new Date(utcTime);
    }

    public static Date parseUtcDate(String time, String timePatten) throws ParseException {
        return parseDate(TimeZone.getTimeZone("UTC"), time, timePatten, (Date)null);
    }

    public static Date parseUtcDate(String time, String timePatten, Date defaultValue) {
        try {
            return parseDate(TimeZone.getTimeZone("UTC"), time, timePatten, defaultValue);
        } catch (ParseException var4) {
            return defaultValue;
        }
    }

    public static Date parseDate(TimeZone timeZone, String time, String timePatten) throws ParseException {
        return parseDate(timeZone, time, timePatten, (Date)null);
    }

    public static Date parseDate(TimeZone timeZone, String time, String timePatten, Date defaultValue) throws ParseException {
        if (!StringUtils.isBlank(time) && timeZone != null && !StringUtils.isBlank(timePatten)) {
            SimpleDateFormat sdf = new SimpleDateFormat(timePatten);
            sdf.setTimeZone(timeZone);
            Date result = null;

            try {
                result = sdf.parse(time);
                return result;
            } catch (ParseException var7) {
                if (defaultValue != null) {
                    return defaultValue;
                } else {
                    throw var7;
                }
            }
        } else {
            return defaultValue;
        }
    }

    public static Date parseDate(long utcTime, Date defaultValue) {
        try {
            return new Date(utcTime);
        } catch (Exception var4) {
            return defaultValue;
        }
    }

    public static Date parseDate(Calendar calendar, long utcTime) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }

        calendar.setTimeInMillis(utcTime);
        return calendar.getTime();
    }

    public static Long getUtcTime(Date date, Long defaultValue) {
        try {
            return getUtcTime(date);
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    public static long getUtcTime(Date date) {
        return date.getTime() / 1000L;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        AssertUtils.notNull(date1, "第一个日期参数不能为null");
        AssertUtils.notNull(date2, "第二个日期参数不能为null");
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        AssertUtils.notNull(cal1, "第一个日历参数不能为null");
        AssertUtils.notNull(cal2, "第二个日历参数不能为null");
        return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
    }

    public static Date compute(Date date, int field, int value) {
        AssertUtils.notNull(date, "date参数不能为null");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(field, calendar.get(field) + value);
        return calendar.getTime();
    }

    public static Date computeDate(Date date, int value) {
        return compute(date, 6, value);
    }

    public static Date computeMonth(Date date, int value) {
        return compute(date, 2, value);
    }

    public static Date computeYear(Date date, int value) {
        return compute(date, 1, value);
    }

    public static Date computeHour(Date date, int value) {
        return compute(date, 11, value);
    }

    public static Date computeMinute(Date date, int value) {
        return compute(date, 12, value);
    }

    public static Date computeSecond(Date date, int value) {
        return compute(date, 13, value);
    }
}
