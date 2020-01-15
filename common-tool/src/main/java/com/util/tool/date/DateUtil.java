/**
 * fshows.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package com.util.tool.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author zhaoj
 * @version DateUtil.java, v 0.1 2019-03-13 10:21
 */
public class DateUtil {
    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
    private static final String DATETIME_FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_TIME_FORMAT = "yyyyMMddHHmmss";
    private static final String DATETIMEFORMAT = "yyMMddHH";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_FORMAT_MONTH = "yyyy-MM";
    private static final String DATETIME_FORMAT_T_SECOND = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * Get the previous time, from how many days to now.
     *
     * @return The new previous time.
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    public static Date previous(int days) {
        return new Date(System.currentTimeMillis() - days * 3600000L * 24L);
    }

    /**
     * Convert date and time to string like "yyyy-MM-dd HH:mm".
     */
    public static String formatDateTime(Date d) {
        return formatDate(d, DATETIME_FORMAT);
    }

    /**
     * Convert date and time to string like "yyyyMMddHH".
     *
     * @param d
     * @return
     */
    public static String formatDateTimeNoMin(Date d) {
        return formatDate(d, DATETIMEFORMAT);
    }

    /**
     * Convert date and time to string like "yyyyMMddHHmmss".
     */
    public static String formatDateToString(Date d) {
        return formatDate(d, DATE_TIME_FORMAT);
    }

    /**
     * Convert date and time to string like "yyyy-MM-dd HH:mm".
     */
    public static String formatDateTime(long d) {
        return formatDate(d, DATETIME_FORMAT);
    }

    /**
     * Convert date to String like "yyyy-MM-dd".
     */
    public static String formatDate(Date d) {
        return formatDate(d, DATE_FORMAT);
    }

    /**
     * Convert date to String like "yyyy-MM".
     */
    public static String formatDateMonth(Date d) {
        return formatDate(d, DATE_FORMAT_MONTH);
    }

    public static String formatDate(Date d, String dataFormat) {
        return new SimpleDateFormat(dataFormat).format(d);
    }

    public static String formatDate(long d, String dataFormat) {
        return new SimpleDateFormat(dataFormat).format(d);
    }

    /**
     * Parse date like "yyyy-MM-dd".
     */
    public static Date parseDate(String d) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(d);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Parse date like "yyyy-MM-dd" to "yyyy-MM-dd 00:00".
     */
    public static Date parseDateBegin(Date d) {
        try {
            String str_date = formatDate(d) + " 00:00";
            return parseDateTime(str_date);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Parse date like "yyyy-MM-dd" to "yyyy-MM-dd 23:59".
     */
    public static Date parseDateEnd(Date d) {
        try {
            String str_date = formatDate(d) + " 23:59";
            return parseDateTime(str_date);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Convert date and time to string like "yyyy-MM-dd HH:mm:ss".
     */
    public static String formatDateToStringHMS(Date d) {
        return new SimpleDateFormat(DATETIME_FORMAT_SECOND).format(d);
    }

    /**
     * Parse date like "yyyy-MM-dd" .
     *
     * @throws ParseException
     */
    public static Date paraseStringToDate(String d) throws ParseException {
        return parse(DATE_FORMAT, d);
    }

    /**
     * Parse date and time like "yyyy-MM-dd hh:mm".
     *
     * @throws ParseException
     */
    public static Date parseDateTime(String d) throws ParseException {
        return parse(DATETIME_FORMAT, d);
    }

    /**
     * Parse date like "yyyy-MM-dd HH:mm:ss" .
     *
     * @throws ParseException
     */
    public static Date parseStringToDateHMS(String d) throws ParseException {
        return parse(DATETIME_FORMAT_SECOND, d);
    }

    public static Date parseStringToDateTHMS(String d) throws ParseException {
        return parse(DATETIME_FORMAT_T_SECOND, d);
    }

    public static Date parse(String f, String d) throws ParseException {
        return new SimpleDateFormat(f).parse(d);
    }
}
