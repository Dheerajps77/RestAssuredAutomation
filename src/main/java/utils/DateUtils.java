package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Parses a string into a Date object using the default date format.
     *
     * @param dateString the string to parse
     * @return the parsed Date object
     * @throws ParseException if the string cannot be parsed
     */
    public static Date parseDate(String dateString) throws ParseException {
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(dateString);
    }

    public static int getCurrentYear() {
        return LocalDate.now().getYear();
    }

    /**
     * Formats a Date object into a string using the default date format.
     *
     * @param date the Date object to format
     * @return the formatted string
     */
    public static String formatDate(Date date) {
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(date);
    }

    /**
     * Parses a string into a Date object using the default date-time format.
     *
     * @param dateTimeString the string to parse
     * @return the parsed Date object
     * @throws ParseException if the string cannot be parsed
     */
    public static Date parseDateTime(String dateTimeString) throws ParseException {
        return new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT).parse(dateTimeString);
    }

    /**
     * Formats a Date object into a string using the default date-time format.
     *
     * @param date the Date object to format
     * @return the formatted string
     */
    public static String formatDateTime(Date date) {
        return new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT).format(date);
    }

    /**
     * Adds a specified number of days to a given date.
     *
     * @param date the original date
     * @param days the number of days to add
     * @return the new date
     */
    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    /**
     * Subtracts a specified number of days from a given date.
     *
     * @param date the original date
     * @param days the number of days to subtract
     * @return the new date
     */
    public static Date subtractDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        return calendar.getTime();
    }

    /**
     * Checks if two dates are equal.
     *
     * @param date1 the first date
     * @param date2 the second date
     * @return true if the dates are equal, false otherwise
     */
    public static boolean areDatesEqual(Date date1, Date date2) {
        return date1.compareTo(date2) == 0;
    }

    /**
     * Checks if a date is before another date.
     *
     * @param date1 the first date
     * @param date2 the second date
     * @return true if date1 is before date2, false otherwise
     */
    public static boolean isDateBefore(Date date1, Date date2) {
        return date1.compareTo(date2) < 0;
    }

    /**
     * Checks if a date is after another date.
     *
     * @param date1 the first date
     * @param date2 the second date
     * @return true if date1 is after date2, false otherwise
     */
    public static boolean isDateAfter(Date date1, Date date2) {
        return date1.compareTo(date2) > 0;
    }
}
