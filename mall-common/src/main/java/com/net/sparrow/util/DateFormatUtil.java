package com.net.sparrow.util;

import com.net.sparrow.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间格式工具
 */
@Slf4j
public abstract class DateFormatUtil {

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-mm-dd HH:mm:ss";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    private DateFormatUtil() {

    }

    /**
     * 获取当前时间字符串
     *
     * @return 当前时间字符串
     */
    public static String nowDay() {
        return parseToString(new Date(), YYYY_MM_DD);
    }

    /**
     * 获取当前时间字符串
     *
     * @return 当前时间字符串
     */
    public static String now() {
        return parseToString(new Date());
    }

    /**
     * 将时间转换成字符串格式
     *
     * @param date 指定时间
     * @return 字符串格式的时间
     */
    public static String parseToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        return simpleDateFormat.format(date);
    }

    /**
     * 将时间转换成字符串格式
     *
     * @param date   指定时间
     * @param format 指定格式
     * @return 字符串格式的时间
     */
    public static String parseToString(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 将字符串格式转换成Date
     *
     * @param time 指定时间
     * @return Date
     */
    public static Date parseToDate(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        try {
            return simpleDateFormat.parse(time);
        } catch (ParseException e) {
            log.info("时间转换错误，原因：", e);
            throw new BusinessException(HttpStatus.BAD_REQUEST.value(), "时间转换错误");
        }
    }
}
