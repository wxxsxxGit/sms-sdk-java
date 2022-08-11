
package com.xsxx.sms.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils
{

    //格式
    public static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE_MINUTE = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_DATETIME = "yyyyMMddHHmmss";
    public static final String PATTERN_CMPP_DATETIME = "yyMMddHHmm";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERNDATE = "yyyyMMdd";
    public static final String PATTERN_MONTH = "yyyy-MM";
    public static final String PATTERN_TIME = "HH:mm:ss";

    /**
     * 时间格式化
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern)
    {
        if (date == null)
        {
            throw new IllegalArgumentException("timestamp null illegal");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String format(Date date, SimpleDateFormat sdf)
    {
        if (date == null)
        {
            throw new IllegalArgumentException("timestamp null illegal");
        }
        return sdf.format(date);
    }

    public static String format(Date date)
    {
        return format(date, PATTERN_DATE_TIME);
    }

    public static String format(String pattern)
    {
        return format(new Date(), pattern);
    }

    /**
     * 当前时间戳
     *
     * @return
     */
    public static Timestamp timestamp()
    {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp timestamp(Date date)
    {
        return new Timestamp(date.getTime());
    }

    /**
     * 时间解析
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static Date parse(String dateStr, String pattern)
    {
        if (dateStr == null || dateStr.equals(""))
        {
            throw new RuntimeException("str date null");
        }
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(dateStr);
        }
        catch(ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static Date parse(String dateStr)
    {
        switch (dateStr.length())
        {
            case 19:
                return parse(dateStr, PATTERN_DATE_TIME);
            case 16:
                return parse(dateStr, PATTERN_DATE_MINUTE);
            case 14:
                return parse(dateStr, PATTERN_DATETIME);
            case 10:
                return parse(dateStr, PATTERN_DATE);
            case 8:
                return parse(dateStr, PATTERNDATE);
            case 7:
                return parse(dateStr, PATTERN_MONTH);
        }
        throw new IllegalArgumentException("dateStr unpareseable");
    }

    /**
     * add
     *
     * @param date
     * @param amount
     * @return
     */
    public static Date addYears(Date date, int amount)
    {
        return add(date, 1, amount);
    }

    public static Date addMonths(Date date, int amount)
    {
        return add(date, 2, amount);
    }

    public static Date addWeeks(Date date, int amount)
    {
        return add(date, 3, amount);
    }

    public static Date addDays(Date date, int amount)
    {
        return add(date, 5, amount);
    }

    public static Date addHours(Date date, int amount)
    {
        return add(date, 11, amount);
    }

    public static Date addMinutes(Date date, int amount)
    {
        return add(date, 12, amount);
    }

    public static Date addSeconds(Date date, int amount)
    {
        return add(date, 13, amount);
    }

    public static Date addMilliseconds(Date date, int amount)
    {
        return add(date, 14, amount);
    }

    private static Date add(Date date, int calendarField, int amount)
    {
        if (date == null)
        {
            throw new IllegalArgumentException("The date must not be null");
        }
        else
        {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(calendarField, amount);
            return c.getTime();
        }
    }

    /**
     * 获取指定时间所在月份的第一天
     *
     * @param c
     * @return
     */
    public static Date getFirstDayOfMonth(Calendar c)
    {
        c.set(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public static Date getFirstDayOfMonth(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return getFirstDayOfMonth(c);
    }

    /**
     * 获取指定时间所在月份的最后一天
     *
     * @param c
     * @return
     */
    public static Date getLastDayOfMonth(Calendar c)
    {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        if (month > 11)
        {
            month = 0;
            year = year + 1;
        }
        c.set(year, month, 0, 23, 59, 59);
        return c.getTime();
    }

    public static Date getLastDayOfMonth(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return getLastDayOfMonth(c);
    }

    /**
     * 是否是同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Date date1, Date date2)
    {
        if (date1 != null && date2 != null)
        {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return isSameDay(cal1, cal2);
        }
        else
        {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2)
    {
        if (cal1 != null && cal2 != null)
        {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1)
                && cal1.get(6) == cal2.get(6);
        }
        else
        {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    /**
     * 是否是今天
     *
     * @param date
     * @return
     */
    public static boolean isSameDay(Date date)
    {
        Date now = new Date();
        return isSameDay(date, now);
    }

    public static boolean isSameDay(Calendar c)
    {
        Calendar d = Calendar.getInstance();
        return isSameDay(c, d);
    }

    /**
     * 获取某天的起始时间
     *
     * @param c
     * @return
     */
    public static Date getStartTimeOfDate(Calendar c)
    {
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND,0);
        return c.getTime();
    }

    public static Date getStartTimeOfDate(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return getStartTimeOfDate(c);
    }

    /**
     * 获取某天的结束时间
     *
     * @param c
     * @return
     */
    public static Date getEndTimeOfDate(Calendar c)
    {
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }

    public static Date getEndTimeOfDate(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return getEndTimeOfDate(c);
    }

    /**
     * 时间转整型
     *
     * @param date
     * @return
     */
    public static int toInt(Date date)
    {
        if (date == null)
        {
            throw new IllegalArgumentException("toInt date null illegal");
        }
        return (int)(date.getTime()/1000);
    }
    public static int toInt()
    {
        return (int)(System.currentTimeMillis()/1000);
    }

    public static Date int2Date(int time){
        return new Date((long)time * 1000);
    }

    /**
     * 获取当日时间戳yyyyMMdd
     * @return
     */
    public static int getDayStamp(){
        return getDayStamp(new Date());
    }

    public static int getDayStamp(Date date){
        if(date == null){
            date = new Date();
        }
        return Integer.valueOf(format(date,PATTERNDATE));
    }
    
    /**
     * 获取当前日期,时间为最后一秒
     * @return
     */
    public static Date getTodayEndTime() {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.HOUR_OF_DAY, 23);
		ca.set(Calendar.MINUTE, 59);
		ca.set(Calendar.SECOND, 59);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTime();
	}
}