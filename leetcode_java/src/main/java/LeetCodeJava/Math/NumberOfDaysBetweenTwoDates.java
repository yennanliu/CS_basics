package LeetCodeJava.Math;

// https://leetcode.com/problems/number-of-days-between-two-dates/description/

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * 1360. Number of Days Between Two Dates
 * Easy
 *
 * Write a program to count the number of days between two dates.
 *
 * The two dates are given as strings, their format is YYYY-MM-DD as shown in the examples.
 *
 *
 * Example 1:
 *
 * Input: date1 = "2019-06-29", date2 = "2019-06-30"
 * Output: 1
 *
 * Example 2:
 *
 * Input: date1 = "2020-01-15", date2 = "2019-12-31"
 * Output: 15
 *
 *
 * Constraints:
 *
 * The given dates are valid dates between the years 1971 and 2100.
 *
 */
public class NumberOfDaysBetweenTwoDates {

    private static final int START_YEAR = 1971;

    // V0
    // IDEA: MATH - convert each date to "days since 1971-01-01", then take the abs difference
    //   leap year: divisible by 4, except centuries not divisible by 400
    /**
     * time = O(Y + M)
     * space = O(1)
     */
    public int daysBetweenDates(String date1, String date2) {
        return Math.abs(daysSinceStart(date1) - daysSinceStart(date2));
    }

    /** Number of days from 1971-01-01 up to the given YYYY-MM-DD date. */
    private int daysSinceStart(String date) {
        String[] parts = date.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);

        int days = 0;
        // whole years before this one
        for (int y = START_YEAR; y < year; y++) {
            days += isLeapYear(y) ? 366 : 365;
        }
        // whole months before this one, inside this year
        for (int m = 1; m < month; m++) {
            days += daysInMonth(year, m);
        }
        return days + day;
    }

    /** Days in the given 1-indexed month of the given year. */
    private int daysInMonth(int year, int month) {
        int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (month == 2 && isLeapYear(year)) {
            return 29;
        }
        return days[month - 1];
    }

    /** Gregorian leap-year rule. */
    private boolean isLeapYear(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    // V1
    // IDEA: LIBRARY (java.time parses ISO dates and counts days directly)
    /**
     * time = O(1)
     * space = O(1)
     */
    public int daysBetweenDates_1(String date1, String date2) {
        LocalDate first = LocalDate.parse(date1);
        LocalDate second = LocalDate.parse(date2);
        long days = ChronoUnit.DAYS.between(first, second);
        return (int) Math.abs(days);
    }
}
