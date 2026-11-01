package LeetCodeJava.Math;

// https://leetcode.com/problems/day-of-the-year/description/

/**
 * 1154. Day of the Year
 * Easy
 *
 * Given a string date representing a Gregorian calendar date formatted as YYYY-MM-DD, return the
 * day number of the year.
 *
 *
 * Example 1:
 *
 * Input: date = "2019-01-09"
 * Output: 9
 * Explanation: Given date is the 9th day of the year in 2019.
 *
 * Example 2:
 *
 * Input: date = "2019-02-10"
 * Output: 41
 *
 *
 * Constraints:
 *
 * date.length == 10
 * date[4] == date[7] == '-', and all other date[i]'s are digits
 * date represents a calendar date between Jan 1st, 1900 and Dec 31st, 2019.
 *
 */
public class DayOfTheYear {

    // V0
    // IDEA: SUM THE LENGTHS OF ALL PREVIOUS MONTHS, THEN ADD THE DAY
    //   leap year: divisible by 400, or divisible by 4 but not by 100
    /**
     * time = O(1)
     * space = O(1)
     */
    public int dayOfYear(String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8));

        int[] monthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        boolean leap = year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
        if (leap) {
            monthDays[1] = 29;
        }

        // every full month before this one
        int dayNumber = 0;
        for (int i = 0; i < month - 1; i++) {
            dayNumber += monthDays[i];
        }
        return dayNumber + day;
    }
}
