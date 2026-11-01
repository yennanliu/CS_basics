package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-number-of-operations-to-convert-time/description/

/**
 * 2224. Minimum Number of Operations to Convert Time
 * Easy
 *
 * You are given two strings current and correct representing two 24-hour times.
 *
 * 24-hour times are formatted as "HH:MM", where HH is between 00 and 23, and MM is between 00 and 59. The
 * earliest 24-hour time is 00:00, and the latest is 23:59.
 *
 * In one operation you can increase the time current by 1, 5, 15, or 60 minutes. You can perform this
 * operation any number of times.
 *
 * Return the minimum number of operations needed to convert current to correct.
 *
 *
 * Example 1:
 *
 * Input: current = "02:30", correct = "04:35"
 * Output: 3
 *
 * Example 2:
 *
 * Input: current = "11:00", correct = "11:01"
 * Output: 1
 *
 *
 * Constraints:
 *
 * current and correct are in the format "HH:MM"
 * current <= correct
 *
 */
public class MinimumNumberOfOperationsToConvertTime {

    // the step sizes, largest first; each divides the next larger one, so greedy is optimal
    private static final int[] STEPS = {60, 15, 5, 1};

    // V0
    // IDEA: GREEDY COIN CHANGE - THE STEPS 1 | 5 | 15 | 60 FORM A CANONICAL COIN SYSTEM, TAKE THE BIGGEST FIRST
    /**
     * time = O(1)
     * space = O(1)
     */
    public int convertTime(String current, String correct) {
        int difference = toMinutes(correct) - toMinutes(current);

        int operations = 0;
        for (int step : STEPS) {
            operations += difference / step;
            difference %= step;
        }
        return operations;
    }

    /** Converts "HH:MM" to minutes since midnight. */
    private int toMinutes(String time) {
        int hours = Integer.parseInt(time.substring(0, 2));
        int minutes = Integer.parseInt(time.substring(3, 5));
        return hours * 60 + minutes;
    }
}
