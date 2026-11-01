package LeetCodeJava.Math;

// https://leetcode.com/problems/number-of-days-in-a-month/description/

/**
 * 1118. Number of Days in a Month
 * Easy
 *
 * Given a year year and a month month, return the number of days of that month.
 *
 *
 * Example 1:
 *
 * Input: year = 1992, month = 7
 * Output: 31
 *
 * Example 2:
 *
 * Input: year = 2000, month = 2
 * Output: 29
 *
 * Example 3:
 *
 * Input: year = 1900, month = 2
 * Output: 28
 *
 *
 * Constraints:
 *
 * 1583 <= year <= 2100
 * 1 <= month <= 12
 *
 */
public class NumberOfDaysInAMonth {

    // V0
    // IDEA: LEAP YEAR RULE + MONTH LENGTH TABLE
    //   leap year: divisible by 4 and not by 100, or divisible by 400
    //   (2000 is a leap year, 1900 is not). only February depends on it.
    /**
     * time = O(1)
     * space = O(1)
     */
    public int numberOfDays(int year, int month) {
        // padded with a leading 0 so the 1-based month indexes it directly
        int[] days = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (month == 2 && isLeapYear(year)) {
            return 29;
        }
        return days[month];
    }

    /** Gregorian leap year rule. */
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
}
