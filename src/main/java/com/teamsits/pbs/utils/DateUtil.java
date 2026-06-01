package com.teamsits.pbs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * File: DateUtil.java
 * Purpose: This class will be used for date related functionality
 */

public class DateUtil {
    public static final String DD_MM_YYYY_DASH = "dd-MM-yyyy";
    public static final String YYYY_MM_DD_DASH = "yyyy-MM-dd";
    public static final String DATE_FORMAT_YY = "yy";
    public static final String DATE_FORMAT_YYYY = "yyyy";
    private static final String DD_MM_YYYY_HH_MM_SS_DASH = "dd-MM-yyyy HH:mm:ss.S";

    /**
     * Get date as string format ("dd-MM-yyyy HH:mm:ss.S")
     *
     * @return String as a date
     */
    public static String getCurrentDateTimeAsString() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS_DASH);
        return sdf.format(cal.getTime());
    }

    /**
     * Get date as string format ("dd-MM-yyyy HH:mm:ss.S")
     *
     * @return Date as a date
     */
    public static Date getCurrentDateTimeAsDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY_DASH);
        String date = sdf.format(cal.getTime());
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Get only date part from the given date
     *
     * @param date Util date
     * @return Date as a date
     */
    public static Date getOnlyDate(Date date) {
        Calendar calDate = Calendar.getInstance();
        calDate.setTime(date);
        calDate.set(Calendar.HOUR, 0);
        calDate.set(Calendar.MINUTE, 0);
        calDate.set(Calendar.SECOND, 0);
        calDate.set(Calendar.MILLISECOND, 0);
        calDate.set(Calendar.AM_PM, Calendar.AM);
        date = calDate.getTime();
        return date;
    }

    /**
     * Get two dates are equal
     *
     * @param date1 Util date
     * @param date2 Util date
     * @return true or false as boolean
     */
    public static boolean compareTwoDate(Date date1, Date date2) {
        return getOnlyDate(date1).equals(getOnlyDate(date2));
    }

    /**
     * Get date as string format ("dd-MM-yyyy")
     *
     * @return Date as a string
     */
    public static String getStringDateForUI(String date) {
        SimpleDateFormat simpleDF = new SimpleDateFormat(DD_MM_YYYY_DASH); // "dd-MM-yyyy"
        Date dt = getDateFromString(date);
        return (dt != null) ? simpleDF.format(dt) : "";
    }

    /**
     * Get date as string format ("yyyy-MM-dd")
     *
     * @return Date as a date
     */
    public static Date getDateFromStringYYYY_MM_DD_DASH(String strDt) {
        SimpleDateFormat simpleDF = new SimpleDateFormat(YYYY_MM_DD_DASH); // "yyyy-MM-dd"
        if (strDt == null) {
            return null;
        }
        Date dt = null;
        try {
            dt = simpleDF.parse(strDt);
            return dt;
        } catch (Exception e) {
            return null;
        }
    }


    public static Date getDateFromString(String strDt) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH); // "dd-MM-yyyy"
        if (strDt == null) {
            return null;
        }
        try {
            Date date = formatter.parse(strDt);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getDMYfromDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH); // "dd-MM-yyyy"
        if (date == null) {
            return null;
        }
        try {
            String dateString = (formatter.format(date));
            return dateString;
        } catch (Exception e) {
            return null;
        }
    }

    public static Date getDateFromStringDMY(String strDt) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH); // "dd-MM-yyyy"
        if (strDt == null) {
            return null;
        }
        try {
            Date date = formatter.parse(strDt);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get 2 digit year of the given date
     *
     * @param date
     * @return int year "YY"
     */
    public static String getYearYY(Date date) {
        SimpleDateFormat simpleDF = new SimpleDateFormat(DATE_FORMAT_YY); // "yy"
        return simpleDF.format(date);
    }

    /**
     * Get 4 digit year of the given date
     *
     * @param date
     * @return int year
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Get month of the given date
     *
     * @param date
     * @return int month
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return (calendar.get(Calendar.MONTH) + 1);
    }

    /**
     * Get first date of the given month
     *
     * @param monthIndex
     * @return date
     */
    public static Date getFirstDateOfMonth(int monthIndex) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, monthIndex);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * Get last date of the given month
     *
     * @param monthIndex
     * @return date
     */
    public static Date getLastDateOfMonth(int monthIndex) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, monthIndex);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * Get last date of the previous month from the given date
     *
     * @param cDate
     * @return date
     */
    public static Date getPreviousMonthLastDate(Date cDate) {
//        int previousMothFromDate = getMonth(cDate)-2;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cDate);

        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        return calendar.getTime();
    }


    /**
     * Get date as string format ("dd-MM-yyyy HH:mm:ss.S")
     *
     * @return Date as a date
     */
    public static Date getDateTimeAsDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS_DASH);
        String dateResult = sdf.format(cal.getTime());
        try {
            return sdf.parse(dateResult);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Get 2 digit year of the given date
     *
     * @param date
     * @return int year "YYYY"
     */
    public static String getYearYYYY(Date date) {
        SimpleDateFormat simpleDF = new SimpleDateFormat(DATE_FORMAT_YYYY); // "yy"
        return simpleDF.format(date);
    }

    public static String getStringMonthDate() {
        Date date = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("dd MMMM yyyy zzzz");
        String stringDate = DateFor.format(date);

        return stringDate;

    }

    public static String getStringDateFromCalendar(Calendar date) {
        Calendar cal = date;
        cal.add(Calendar.DATE, 1);
        Date date1 = cal.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat(DD_MM_YYYY_HH_MM_SS_DASH);
        String date2 = format1.format(date1);
        return date2;
    }

    public static String getDateToStringDmy(Date date) {
        String dbDate = "";
        if (date == null) return dbDate;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DD_MM_YYYY_DASH);
        dbDate = simpleDateFormat.format(date);
        return dbDate;
    }

    public static String getDateToStringYmd(Date date) {
        String dbDate = "";
        if (date == null) return dbDate;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD_DASH);
        dbDate = simpleDateFormat.format(date);
        return dbDate;
    }

    public static String getNextDate(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(strDate));
            c.add(Calendar.DATE, 1);  // number of days to add
            strDate = sdf.format(c.getTime());
            return strDate;
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static Date getDateFromDateYYYY_MM_DD_DASH(Date uiDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String toDateTransfer = simpleDateFormat.format(uiDate);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(toDateTransfer);
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    public static Calendar getDateTimeAsCalendar(String date) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
            return cal;
        } catch (ParseException e) {
            return null;
        }
    }

    public static Calendar getDateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;

    }

    public static List<LocalDate> getAllDatesBetweenTwoDates(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<LocalDate> totalDates = new ArrayList<>();
        while (!start.isAfter(end)) {
            totalDates.add(start);
            start = start.plusDays(1);
        }
        return totalDates;
    }

    public static String getPreviousDateFromDate(String date) {
        if (date == null || date.equals("")) {
            return null;
        }
        Date d = getDateFromStringYYYY_MM_DD_DASH(date);
        LocalDate previousDate = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        previousDate = previousDate.minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return previousDate.format(formatter);
    }

    public static String getStringDateFromLocalDate(LocalDate date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        if (date == null) {
            return null;
        }
        try {
            return date.format(formatter);
        } catch (Exception e) {
            return null;
        }
    }
}
