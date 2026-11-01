package LeetCodeJava.Math;

// https://leetcode.com/problems/number-of-valid-clock-times/description/

/**
 * 2437. Number of Valid Clock Times
 * Easy
 *
 * You are given a string of length 5 called time, representing the current time on a digital clock in the format "hh:mm". The earliest possible time is "00:00" and the latest possible time is "23:59".
 *
 * In the string time, the digits represented by the ? symbol are unknown, and must be replaced with a digit from 0 to 9.
 *
 * Return an integer answer, the number of valid clock times that can be created by replacing every ? with a digit from 0 to 9.
 *
 *
 * Example 1:
 *
 * Input: time = "?5:00"
 * Output: 2
 * Explanation: We can replace the ? with either a 0 or 1, producing "05:00" or "15:00". Note that we cannot replace it with a 2, since the time "25:00" is invalid. In total, we have two choices.
 *
 * Example 2:
 *
 * Input: time = "0?:0?"
 * Output: 100
 * Explanation: Each ? can be replaced by any digit from 0 to 9, so we have 100 total choices.
 *
 * Example 3:
 *
 * Input: time = "??:??"
 * Output: 1440
 * Explanation: There are 24 possible choices for the hours, and 60 possible choices for the minutes. In total, we have 24 * 60 = 1440 choices.
 *
 *
 * Constraints:
 *
 * time is a valid string of length 5 in the format "hh:mm".
 * "00" <= hh <= "23"
 * "00" <= mm <= "59"
 * Some of the digits might be replaced with '?' and need to be replaced with digits from 0 to 9.
 *
 */
public class NumberOfValidClockTimes {

    // V0
    // IDEA: ENUMERATION - HOURS AND MINUTES ARE INDEPENDENT, SO MULTIPLY THEIR MATCH COUNTS
    /**
     * time = O(24 + 60)
     * space = O(1)
     */
    public int countTime(String time) {
        String hourPattern = time.substring(0, 2);
        String minutePattern = time.substring(3, 5);
        return countMatches(hourPattern, 24) * countMatches(minutePattern, 60);
    }

    /** How many values 0 .. limit-1, written as two digits, match the pattern ('?' matches any digit). */
    private int countMatches(String pattern, int limit) {
        int matches = 0;
        for (int value = 0; value < limit; value++) {
            char tens = (char) ('0' + value / 10);
            char ones = (char) ('0' + value % 10);
            boolean tensOk = pattern.charAt(0) == '?' || pattern.charAt(0) == tens;
            boolean onesOk = pattern.charAt(1) == '?' || pattern.charAt(1) == ones;
            if (tensOk && onesOk) {
                matches++;
            }
        }
        return matches;
    }
}
