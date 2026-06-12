package com.teamsits.automatix.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppDate {

    public static final String YYYY_MM_DD_T_HH_MM_SS = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String MMMM_DD_YYYY = "MMMM dd, yyyy";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String DD_MM_YYYY = "dd-MM-yyyy";
    private static final Logger log = LoggerFactory.getLogger(AppDate.class);

    public static Calendar now() {
        return Calendar.getInstance();
    }

    public static Calendar calender(String dateString, String format) {
        try {
            Calendar calender = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            calender.setTime(sdf.parse(dateString));
            return calender;
        } catch (Throwable t) {
            log.error(t.getMessage());
        }
        return null;
    }

    public static String current(String format) {
        String dateString = "";
        try {
            Date date = now().getTime();
            dateString = new SimpleDateFormat(format).format(date);
        } catch (Throwable t) {
            log.error(t.getMessage());
        }
        return dateString;
    }

    public static String dateString(Calendar calendar, String format) {
        String dateString = "";
        try {
            Date date = calendar.getTime();
            dateString = new SimpleDateFormat(format).format(date);
        } catch (Throwable t) {
            log.error(t.getMessage());
        }
        return dateString;
    }
}
