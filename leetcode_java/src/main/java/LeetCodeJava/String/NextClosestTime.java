package LeetCodeJava.String;

// https://leetcode.com/problems/next-closest-time/

import java.util.HashSet;
import java.util.Set;

/**
 *  681. Next Closest Time
 *  Medium
 *
 *  Given a time represented in the format "HH:MM", form the next closest time
 *  by reusing the current digits. There is no limit on how many times a digit
 *  can be reused.
 *  You may assume the given input string is always valid.
 *
 *  Example 1:
 *    Input:  time = "19:34"
 *    Output: "19:39"
 *    (next closest time out of digits 1, 9, 3, 4, which occurs 5 minutes later)
 *
 *  Example 2:
 *    Input:  time = "23:59"
 *    Output: "22:22"
 *
 *  Constraints:
 *    time.length == 5
 *    time is a valid time in the form "HH:MM".
 *    0 <= HH < 24, 0 <= MM < 60
 */
public class NextClosestTime {

    // V0
    // IDEA: brute force — walk the next 1440 minutes, return the first time
    //       whose digits are all contained in the original digit set
    /**
     * time = O(1)   (at most 1440 iterations)
     * space = O(1)
     */
    public String nextClosestTime(String time) {
        Set<Character> allowed = new HashSet<>();
        for (char c : time.toCharArray()) {
            if (c != ':') {
                allowed.add(c);
            }
        }

        int cur = Integer.parseInt(time.substring(0, 2)) * 60
                + Integer.parseInt(time.substring(3, 5));

        for (int i = 1; i <= 1440; i++) {
            int t = (cur + i) % 1440;
            String candidate = String.format("%02d:%02d", t / 60, t % 60);
            boolean ok = true;
            for (char c : candidate.toCharArray()) {
                if (c != ':' && !allowed.contains(c)) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                return candidate;
            }
        }
        return time;
    }

    // V1
    // IDEA: greedy from the least significant digit — try to bump one position
    //       up to the smallest strictly larger available digit, reset the rest
    //       to the smallest digit; if nothing works, all-smallest-digit time
    /**
     * time = O(1)
     * space = O(1)
     */
    public String nextClosestTime_1(String time) {
        char[] digits = (time.substring(0, 2) + time.substring(3, 5)).toCharArray();
        char[] sorted = digits.clone();
        java.util.Arrays.sort(sorted);

        for (int x = 3; x >= 0; x--) {
            for (char y : sorted) {
                if (y <= digits[x]) {
                    continue;
                }
                char[] cand = digits.clone();
                cand[x] = y;
                for (int k = x + 1; k < 4; k++) {
                    cand[k] = sorted[0];
                }
                int hh = (cand[0] - '0') * 10 + (cand[1] - '0');
                int mm = (cand[2] - '0') * 10 + (cand[3] - '0');
                if (hh < 24 && mm < 60) {
                    return "" + cand[0] + cand[1] + ':' + cand[2] + cand[3];
                }
            }
        }
        char m = sorted[0];
        return "" + m + m + ':' + m + m;
    }
}
