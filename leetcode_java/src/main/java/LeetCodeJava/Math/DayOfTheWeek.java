package LeetCodeJava.Math;

// https://leetcode.com/problems/day-of-the-week/description/

/**
 * 1185. Day of the Week
 * Easy
 *
 * Given a date, return the corresponding day of the week for that date.
 *
 * The input is given as three integers representing the day, month and year respectively.
 *
 * Return the answer as one of the following values {"Sunday", "Monday", "Tuesday", "Wednesday",
 * "Thursday", "Friday", "Saturday"}.
 *
 * Note: January 1, 1971 was a Friday.
 *
 *
 * Example 1:
 *
 * Input: day = 31, month = 8, year = 2019
 * Output: "Saturday"
 *
 * Example 2:
 *
 * Input: day = 18, month = 7, year = 1999
 * Output: "Sunday"
 *
 * Example 3:
 *
 * Input: day = 15, month = 8, year = 1993
 * Output: "Sunday"
 *
 *
 * Constraints:
 *
 * The given dates are valid dates between the years 1971 and 2100.
 *
 */
public class DayOfTheWeek {

    // V0
    // IDEA: COUNT DAYS ELAPSED SINCE A KNOWN ANCHOR (1971-01-01 was a Friday), THEN MOD 7
    //   whole years : 365, plus 1 for each leap year
    //   whole months: month lengths of the query year (+1 once past a leap February)
    //   days        : day - 1
    /**
     * time = O(year - 1971)
     * space = O(1)
     */
    public String dayOfTheWeek(int day, int month, int year) {
        int[] monthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        int elapsed = 0;
        for (int y = 1971; y < year; y++) {
            elapsed += isLeapYear(y) ? 366 : 365;
        }
        for (int m = 0; m < month - 1; m++) {
            elapsed += monthDays[m];
        }
        // February 29 only counts once we are past February
        if (month > 2 && isLeapYear(year)) {
            elapsed += 1;
        }
        elapsed += day - 1;

        // index 0 is the anchor's weekday, Friday
        String[] names = {"Friday", "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};
        return names[elapsed % 7];
    }

    /** Gregorian leap year rule. */
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
}
