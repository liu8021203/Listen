package com.ting.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 根据提供当前时间及给定的格式，返回时间字符串
 */
public class UtilDate {

    /**
     * 获取当前的时间字符串
     *
     * @param format yy-MM-dd HH
     * @return 字符串形式的当前时间
     */
    public static String getNowDate(String format) {
        return new SimpleDateFormat(format, Locale.US).format(new Date());
    }

    /**
     * 将给定时间转换成字符串
     *
     * @param date   时间戳
     * @param format 格式
     * @return str
     */
    public static String format(long date, String format) {
        return new SimpleDateFormat(format).format(new Date(date));
    }

    /**
     * 将给定时间转换成字符串
     *
     * @param date   long类型的字符串时间戳
     * @param format 格式
     * @return str
     */
    public static String format(String date, String format) {
        long s1 = 0;
        try {
            s1 = Long.parseLong(date);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (s1 <= 0) {
            return null;
        }
        return format(s1, format);
    }


    /**
     * 根据日期获取年龄
     *
     * @param year 1990
     * @return int;年龄
     */
    public static int getAge(String year) {
        int iAge;
        Calendar cal = Calendar.getInstance();
        int iCurrYear = cal.get(Calendar.YEAR);
        iAge = iCurrYear - Integer.valueOf(year);
        return iAge;
    }

    /**
     * 判断是否为合法的日期时间字符串
     *
     * @param strInput   时间字符串
     * @param dateFormat 格式
     * @return boolean;符合为true,不符合为false
     */
    public static boolean isDate(String strInput, String dateFormat) {
        boolean result = false;
        if (strInput != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.CHINESE);
            formatter.setLenient(false);
            try {
                formatter.format(formatter.parse(strInput));
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 验证小于当前日期 是否有效
     *
     * @param iYear  待验证日期(年)
     * @param iMonth 待验证日期(月 1-12)
     * @param iDate  待验证日期(日)
     * @return 符合为true, 不符合为false
     */
    public static boolean isDate(int iYear, int iMonth, int iDate) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int datePerMonth;
        if (iYear < 1900 || iYear >= year) {
            return false;
        }
        if (iMonth < 1 || iMonth > 12) {
            return false;
        }
        switch (iMonth) {
            case 4:
            case 6:
            case 9:
            case 11:
                datePerMonth = 30;
                break;
            case 2:
                boolean dm = ((iYear % 4 == 0 && iYear % 100 != 0) || (iYear % 400 == 0)) && (iYear > 1900 && iYear < year);
                datePerMonth = dm ? 29 : 28;
                break;
            default:
                datePerMonth = 31;
        }
        return (iDate >= 1) && (iDate <= datePerMonth);
    }


    /**
     * 将时间转换成格式化的字符串
     *
     * @return
     */
    public static String transformToTimeStr(int time) {
        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        if (hour == 0) {
            hour = 00;
        }
        int second = time % 60;
        minute %= 60;
        // return String.format("%02d:%02d:%02d", hour, minute, second);
        return String.format("%02d:%02d", minute, second);
    }

    /**
     * 将时间转换成格式化的字符串
     *
     * @return
     */
    public static String transformToTimeSECStr(int time) {
        int minute = time / 60;
        int hour = minute / 60;
        if (hour == 0) {
            hour = 00;
        }
        int second = time % 60;
        minute %= 60;
        // return String.format("%02d小时%02d分钟", hour, minute);
        return String.format("%02d:%02d:%02d", hour, minute, second);
        // return String.format("%02d:%02d", minute, second);
    }


    /**
     * 两个时间差的天数
     * <p/>
     * 到期时间点
     *
     * @return 天数 int 类型
     */
    @SuppressLint("SimpleDateFormat")
    public static long transformToDiffDays(String creattime, String expire_time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");

        Date d1 = new Date();
        Date d2 = new Date();
        long diffDays = 999;

        try {
            d1 = format.parse(creattime);
            d2 = format.parse(expire_time);
//			if (d2.getYear() == d1.getYear()) {
            long diff = d2.getTime() - d1.getTime();
            diffDays = diff / (24 * 60 * 60 * 1000);
//			}

        } catch (Exception e) {
            e.printStackTrace();
        }

        return diffDays;
    }


    public static String getDifferNowTime(long sendTime,long sysTime){
        String str = "";
        long time = sysTime - sendTime;
        if(time <= 10 * 60){
            str = "刚刚";
        }else if(time <= 60 * 60){
            str = (time / 60) + "分钟前";
        }else if(time <= 24 * 60 * 60){
            str = (time / 60 / 60) + "小时前";
        }else{
            str = format(sendTime * 1000, "yyyy-MM-dd HH:mm:ss");
        }
        return str;
    }

}
