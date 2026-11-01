package LeetCodeJava.Greedy;

// https://leetcode.com/problems/latest-time-you-can-obtain-after-replacing-characters/description/

/**
 * 3114. Latest Time You Can Obtain After Replacing Characters
 * Easy
 *
 * You are given a string s representing a 12-hour format time where some of the digits (possibly none) are replaced with a "?".
 *
 * 12-hour times are formatted as "HH:MM", where HH is between 00 and 11, and MM is between 00 and 59. The earliest 12-hour time is 00:00, and the latest is 11:59.
 *
 * You have to replace all the "?" characters in s with digits such that the time we obtain by the resulting string is a valid 12-hour format time and is the latest possible.
 *
 * Return the resulting string.
 *
 *
 * Example 1:
 *
 * Input: s = "1?:?4"
 * Output: "11:54"
 * Explanation: The latest 12-hour format time we can achieve by replacing "?" characters is "11:54".
 *
 * Example 2:
 *
 * Input: s = "0?:5?"
 * Output: "09:59"
 * Explanation: The latest 12-hour format time we can achieve by replacing "?" characters is "09:59".
 *
 *
 * Constraints:
 *
 * s.length == 5
 * s[2] is equal to the character ":".
 * All characters except s[2] are digits or "?" characters.
 * The input is generated such that there is at least one time between "00:00" and "11:59" that you can obtain after replacing the "?" characters.
 *
 */
public class LatestTimeYouCanObtainAfterReplacingCharacters {

    // V0
    // IDEA: ONLY 720 VALID TIMES EXIST - TRY THEM FROM THE LATEST DOWN
    /**
     *  Enumerate HH:MM from 11:59 down to 00:00 and return the first one that
     *  agrees with s on every non-'?' character.
     *
     *  time = O(1)   (720 candidates x 5 characters)
     *  space = O(1)
     */
    public String findLatestTime(String s) {
        for (int hour = 11; hour >= 0; hour--) {
            for (int minute = 59; minute >= 0; minute--) {
                String candidate = String.format("%02d:%02d", hour, minute);
                if (matches(s, candidate)) {
                    return candidate;
                }
            }
        }
        return "";
    }

    /** True if every non-'?' character of pattern equals the candidate's. */
    private boolean matches(String pattern, String candidate) {
        for (int i = 0; i < pattern.length(); i++) {
            char p = pattern.charAt(i);
            if (p != '?' && p != candidate.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}
