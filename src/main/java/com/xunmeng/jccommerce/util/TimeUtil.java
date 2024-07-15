package com.xunmeng.jccommerce.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.date.format.FastDateFormat;
import lombok.extern.log4j.Log4j2;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 时间戳时间字符串转换工具类
 */
@Log4j2
@SuppressWarnings("unused")
public class TimeUtil {

    /**
     * 将时间转换为时间戳
     *
     * @param datetime 时间字符串
     * @param pattern  时间格式
     * @return 时间戳
     */
    public static long dateStrToTimestamp(String datetime, String pattern) {
        boolean matches = Pattern.matches(String.valueOf(DatePattern.REGEX_NORM), datetime);
        if (!matches) {
            log.error("非标日期时间,日期时间格式是否正确,{}", datetime);
            return 0L;
        }
        FastDateFormat sdf = FastDateFormat.getInstance(pattern);
        try {
            long timestamp = sdf.parse(datetime).getTime();
            return timestamp / 1000;
        } catch (Exception e) {
            log.error("时间转换为时间戳错误,{}", datetime);
            return 0L;
        }
    }

    /**
     * 将时间戳转换为时间字符串
     *
     * @param timestamp 时间戳
     * @param pattern   时间格式
     * @return 时间字符串
     */
    public static String timestampToDateStr(long timestamp, String pattern) {
        if (timestamp == 0) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        // 如果时间戳为毫秒级,怎转换为秒级
        if (timestamp > Integer.MAX_VALUE) {
            return sdf.format(new Date(timestamp));
        }
        return sdf.format(new Date(timestamp * 1000));
    }

    /**
     * 时间戳转LocalDateTime
     *
     * @param timestamp 10位时间戳
     * @return LocalDateTime
     */
    public static LocalDateTime getLocalDateTime(long timestamp) {
        if (timestamp < 1) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转时间戳
     *
     * @param localDateTime localDateTime
     * @return 时间戳(秒)
     */
    public static long getSecondTimestampFromLocalDateTime(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli() / 1000;
    }

    /**
     * 获取今天0点时间戳
     *
     * @return 今天0点时间戳
     */
    public static Long getTodayZeroTimestamp() {
        // 获取当前日期的0点时间
        LocalDateTime todayZero = LocalDate.now().atStartOfDay();
        // 转换为时间戳（秒）
        return todayZero.atZone(ZoneId.of("Asia/Shanghai")).toEpochSecond();
    }

    /**
     * 获取指定时间戳的0点时间戳
     *
     * @param timestamp 指定的时间戳(秒级)
     * @return 指定时间戳的0点时间戳
     */
    public static Long getZeroTime(long timestamp) {
        int oneDayTimestamps = 60 * 60 * 24;
        return timestamp - (timestamp + 60 * 60 * 8) % oneDayTimestamps;
    }

    /**
     * 获取当前时间戳(秒级)
     *
     * @return 当前时间戳
     */
    public static long getNowTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取指定偏移天数的时间戳(秒级)
     *
     * @return 指定偏移天数的时间戳
     */
    public static long timestampOffsetDay(int offset) {
        LocalDate localDate = LocalDate.now().plusDays(offset);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date.getTime() / 1000;
    }



    /**
     * 判断时间是否为秒级并返回对像
     *
     * @param timestamp 10/13位时间戳
     * @return 日期, 时间戳对象
     */
    public static List<Object> transferPayTime(long timestamp) {
        List<Object> result = new ArrayList<>();
        long millSecond = timestamp;
        if (String.valueOf(timestamp).length() == 13) {
            timestamp /= 1000;
        } else {
            millSecond *= 1000;
        }
        String payTimeDate = timestampToDateStr(timestamp, DatePattern.NORM_DATETIME_PATTERN);
        result.add(payTimeDate);
        result.add(millSecond);
        return result;
    }

    /**
     * 时间差(天)
     *
     * @param from 开始LocalDateTime
     * @param to   结束LocalDateTime
     * @return 时间差(天)
     */
    public static long timeGapDays(LocalDateTime from, LocalDateTime to) {
        if (Objects.isNull(from) && Objects.isNull(to)) {
            return 0;
        }
        if (Objects.isNull(to)) {
            to = LocalDateTime.now();
        }
        if (Objects.isNull(from)) {
            from = LocalDateTime.now();
        }
        if (from.isAfter(to)) {
            return LocalDateTimeUtil.between(to, from, ChronoUnit.DAYS);
        }
        return LocalDateTimeUtil.between(from, to, ChronoUnit.DAYS);
    }


    /**
     * 获取当前月份的开始时间戳（秒数）
     *
     * @return 当前月份开始时间的秒数表示，不包含毫秒部分
     */
    public static long getCurrentMonthStartTime() {
        // 获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();
        // 设置为当前月份的第一天
        LocalDateTime firstDayOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        // 转换为系统默认时区的ZonedDateTime
        ZonedDateTime startOfMonth = firstDayOfMonth.atZone(ZoneId.systemDefault());
        // 获取时间戳
        return startOfMonth.toInstant().toEpochMilli() / 1000;
    }

    /**
     * 获取今天的日期字符串
     *
     * @return 今天的日期字符串，格式为：yyyy-MM-dd
     */
    public static String getTodayDateString() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return today.format(formatter);
    }

    /**
     * 将秒数转换为几天几小时几秒, 0秒不显示
     * @param seconds 秒数
     * @return 转换后的字符串表示
     */
    public static String secsToMin(long seconds) {
        long days = TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) % 24;
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) % 60;
        long secs = seconds % 60;

        StringBuilder str = new StringBuilder();
        if (days > 0) {
            str.append(days).append("天");
        }
        if (hours > 0) {
            str.append(hours).append("小时");
        }
        if (minutes > 0) {
            str.append(minutes).append("分");
        }
        if (secs > 0) {
            str.append(secs).append("秒");
        }
        return str.toString();
    }

}