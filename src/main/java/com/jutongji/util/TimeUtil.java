package com.jutongji.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author xuw
 * @date 2018/3/21 17:26
 */
public class TimeUtil {
    private static final String DAY_BEGIN = "yyyy-MM-dd 00:00:00";
    private static final String DAY_END = "yyyy-MM-dd 23:59:59";

    private static final String NORMAL_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    /**
     * 返回当日0点
     *
     * @param date
     * @return
     */
    public static Date getDayBegin(Date date) {
        DateTime dt = new DateTime(date);
        return dt.withMillisOfDay(0).toDate();
    }

    /**
     * 返回当日最后一ms的时间
     *
     * @param date
     * @return
     */
    public static Date getDayEnd(Date date) {
        DateTime dt = new DateTime(date);
        return dt.withMillisOfDay(DateTimeConstants.MILLIS_PER_DAY - 1).toDate();
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(getDayBegin(new Date()));
        System.out.println(getDayEnd(new Date()));
        System.out.println(formatNormalDate(new Date()));

        SimpleDateFormat sdf1 = new SimpleDateFormat( NORMAL_PATTERN);
        SimpleDateFormat sdf2 = new SimpleDateFormat( NORMAL_PATTERN);
        System.out.println(checkTimeEquals(sdf1.parse("2018-07-10 03:20:00"),sdf2.parse("2018-07-10 03:20:00")));
    }

    public static String formatNormalDate(Date dateTime) {
        return formatDate(dateTime, NORMAL_PATTERN);
    }

    public static String formatDate(Date time, String pattern) {
        DateTime dateTime = new DateTime(time);
        return dateTime.toString(pattern, Locale.CHINESE);
    }

    /**
     * 检查时间是否在一个时间段
     *
     * @param startTime 时间段开始时间
     * @param endTime   时间段结束时间
     * @param checkTime 检测时间
     * @return
     */
    public static boolean checkTimeInTimeSlot(Date startTime, Date endTime, Date checkTime) {
        if (null == startTime || null == endTime || null == checkTime) {
            return false;
        }
        DateTime startDateTime = new DateTime(startTime);
        DateTime endDateTime = new DateTime(endTime);
        DateTime checkDateTime = new DateTime(checkTime);

        //只有检测时间在时间段内才返回true
        if (checkDateTime.isAfter(startDateTime) && checkDateTime.isBefore(endDateTime)) {
            return true;
        }
        return false;
    }

    /**
     * 检查两个Date日期是否时间一样
     * @param sourceTime
     * @param checkTime
     * @return
     */
    public static boolean checkTimeEquals(Date sourceTime, Date checkTime) {
        if (null == sourceTime || null == checkTime) {
            return false;
        }
        DateTime sourceDateTime = new DateTime(sourceTime);
        DateTime checkDateTime = new DateTime(checkTime);

        //只有检测时间在时间段内才返回true
        if (sourceDateTime.isEqual(checkDateTime)) {
            return true;
        }
        return false;
    }

}
