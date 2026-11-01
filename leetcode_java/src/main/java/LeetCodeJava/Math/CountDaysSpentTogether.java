package LeetCodeJava.Math;

// https://leetcode.com/problems/count-days-spent-together/description/

/**
 * 2409. Count Days Spent Together
 * Easy
 *
 * Alice and Bob are traveling to Rome for separate business meetings.
 *
 * You are given 4 strings arriveAlice, leaveAlice, arriveBob, and leaveBob. Alice will be in the city from the dates arriveAlice to leaveAlice (inclusive), while Bob will be in the city from the dates arriveBob to leaveBob (inclusive). Each will be a 5-character string in the format "MM-DD", corresponding to the month and day of the date.
 *
 * Return the total number of days that Alice and Bob are in Rome together.
 *
 * You can assume that all dates occur in the same calendar year, which is not a leap year. Note that the number of days per month can be represented as: [31,28,31,30,31,30,31,31,30,31,30,31].
 *
 *
 * Example 1:
 *
 * Input: arriveAlice = "08-15", leaveAlice = "08-18", arriveBob = "08-16", leaveBob = "08-19"
 * Output: 3
 * Explanation: Alice will be in Rome from August 15 to August 18. Bob will be in Rome from August 16 to August 19. They are both in Rome together on August 16th, 17th, and 18th, so the answer is 3.
 *
 * Example 2:
 *
 * Input: arriveAlice = "10-01", leaveAlice = "10-31", arriveBob = "11-01", leaveBob = "12-31"
 * Output: 0
 * Explanation: There is no day when Alice and Bob are in Rome together, so we return 0.
 *
 *
 * Constraints:
 *
 * All dates are provided in the format "MM-DD".
 * Alice and Bob's arrival dates are earlier than or equal to their leaving dates.
 * All dates begin and end in the same calendar year, which is not a leap year.
 *
 */
public class CountDaysSpentTogether {

    private static final int[] DAYS_IN_MONTH = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    // V0
    // IDEA: CONVERT EACH "MM-DD" TO A DAY OF THE YEAR, THEN INTERSECT TWO INCLUSIVE INTERVALS
    /**
     * time = O(1)
     * space = O(1)
     */
    public int countDaysTogether(String arriveAlice, String leaveAlice, String arriveBob, String leaveBob) {
        // the overlap starts when the later one arrives and ends when the earlier one leaves
        int start = Math.max(dayOfYear(arriveAlice), dayOfYear(arriveBob));
        int end = Math.min(dayOfYear(leaveAlice), dayOfYear(leaveBob));
        // inclusive on both ends; no overlap -> 0
        return Math.max(0, end - start + 1);
    }

    /** "MM-DD" -> 1-based day of a non-leap year. */
    private int dayOfYear(String date) {
        int month = Integer.parseInt(date.substring(0, 2));
        int day = Integer.parseInt(date.substring(3));
        int ordinal = day;
        for (int m = 0; m < month - 1; m++) {
            ordinal += DAYS_IN_MONTH[m];
        }
        return ordinal;
    }
}
