package com.lyh.util;

import lombok.extern.slf4j.Slf4j;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * 时间工具类
 */
@Slf4j
public class TimeUtil {

    /**
     * 获取今日最开始的时间
     */
    public static String nowStart() {
        DateTimeFormatter gs = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return gs.format(LocalDateTime.now()) + "00:00:00";
    }
    /**
     * 获取今日最后时间
     */
    public static String nowEnd() {
        DateTimeFormatter gs = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return gs.format(LocalDateTime.now())+ "23:59:59";
    }

    /**
     * 获取当前时间
     */
    public static String now() {
        DateTimeFormatter gs = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return gs.format(LocalDateTime.now());
    }
    /**
     * 获取当前时间之前的多少天的时间
     */
    public static String now(int s) {
        DateTimeFormatter gs = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return gs.format(LocalDateTime.now().minus(Period.ofDays(s)));
    }

    /**
     * 时间戳转成字符串时间
     */
    public static String timeStampToString(Long time) {
        //创建Date对象
        Date date = new Date(time);
        //创建SimpleDateFormat对象，指定日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 获取当前时间
     * @param str 时间格式
     */
    public static String now(String str) {
        DateTimeFormatter gs;
        if (str == null) {
            gs = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        } else {
            gs = DateTimeFormatter.ofPattern(str);
        }
        return gs.format(LocalDateTime.now());
    }


    /**
     * 是否在过去14年之内
     * 是返回true
     */
    public static boolean withinLast14y(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate inputDate = LocalDate.parse(dateString, formatter);
            LocalDate currentDate = LocalDate.now();
            Period period = Period.between(inputDate, currentDate);
            return period.getYears() <= 14;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * 给日期计算当前年龄
     */
    public static int calculateAge(String birthDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate birthDate = LocalDate.parse(birthDateString, formatter);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    /**
     * 根据用户的出生日期字符串判断星座
     * 格式为yyyy-MM-dd
     */
    public static String determineZodiacSign(String birthDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(birthDateStr, formatter);
        int month = birthDate.getMonthValue();
        int day = birthDate.getDayOfMonth();
        if (month == 1) {
            return (day <= 19) ? "摩羯座" : "水瓶座";
        } else if (month == 2) {
            return (day <= 18) ? "水瓶座" : "双鱼座";
        } else if (month == 3) {
            return (day <= 20) ? "双鱼座" : "白羊座";
        } else if (month == 4) {
            return (day <= 19) ? "白羊座" : "金牛座";
        } else if (month == 5) {
            return (day <= 20) ? "金牛座" : "双子座";
        } else if (month == 6) {
            return (day <= 20) ? "双子座" : "巨蟹座";
        } else if (month == 7) {
            return (day <= 22) ? "巨蟹座" : "狮子座";
        } else if (month == 8) {
            return (day <= 22) ? "狮子座" : "处女座";
        } else if (month == 9) {
            return (day <= 22) ? "处女座" : "天秤座";
        } else if (month == 10) {
            return (day <= 22) ? "天秤座" : "天蝎座";
        } else if (month == 11) {
            return (day <= 21) ? "天蝎座" : "射手座";
        } else {
            return (day <= 21) ? "射手座" : "摩羯座";
        }
    }


    /**
     * 时间戳
     * 一个时间与当前时间的固定差
     */
    public static boolean JsInstant(LocalDateTime localDateTime, long tm) {
        Long oa = localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        Long ob = Instant.now().toEpochMilli();
        long s = Math.abs(ob - oa);
        return s >= tm;
    }


    /**
     * 把字符串时间转换成LocalDateTime
     * @param str 时间字符串
     * @param fmt 时间格式
     */
    public static LocalDateTime StringDataToLocalDateTime(String str, String fmt) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(fmt);
        return LocalDateTime.parse(str, dtf);
    }

    public static LocalDateTime StringDataToLocalDateTime(String str) {
        str = str.substring(0, 16);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(str, dtf);
    }


    /**
     * 返回历史 or 现在 or 未来的时间
     *
     * @param str 时间格式
     * @param d   d < 0 前几天； d > 0 后几天
     */
    public static String getHsDate(String str, int d) {
        return LocalDateTime.now().plusDays(d).format(DateTimeFormatter.ofPattern(str));
    }


    /**
     * str1 比 str2 早 返回true
     * @param str1 开始时间
     * @param str2 结束时间
     */
    public static boolean isPastDate(String str1, String str2) {
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse1 = LocalDateTime.parse(str1, ftf);
        LocalDateTime parse2 = LocalDateTime.parse(str2, ftf);
        return parse2.isAfter(parse1);
    }

    /**
     * str1 比 现在早 返回true
     * @param str1 开始时间
     */
    public static boolean isNowPastDate(String str1) {
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse1 = LocalDateTime.parse(str1, ftf);
        LocalDateTime parse2 = LocalDateTime.parse(now(), ftf);
        return parse2.isAfter(parse1);
    }


    /**
     * str与现在相隔的 天-小时-分钟数
     * @param str 传进来的时间
     */
    public static String CalculatePoor(String str) {
        str = str.substring(0, 19);
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse = LocalDateTime.parse(str, ftf);
        Instant start_time = LocalDateTime.from(parse).atZone(ZoneId.systemDefault()).toInstant();
        Instant end_time = Instant.now();
        long all = Duration.between(start_time, end_time).getSeconds();

        long days = all / 86400;
        all = all % 86400;
        long hours = all / 3600;
        all = all % 3600;
        long minutes = all / 60;

        if (days == 0 && hours == 0) {
            return minutes + "分钟前";
        } else if (days == 0 && hours > 0) {
            return hours + "小时前";
        } else if (days > 0 && days < 30) {
            return days + "天前";
        } else if (days > 30 && days < 365) {
            return days / 30 + "个月前";
        } else if (days > 365) {
            return days / 365 + "年前";
        } else {
            return "刚刚";
        }
    }
}