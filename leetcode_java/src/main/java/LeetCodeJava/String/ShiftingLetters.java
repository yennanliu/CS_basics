package LeetCodeJava.String;

// https://leetcode.com/problems/shifting-letters/

/**
 *  848. Shifting Letters
 *  Medium
 *
 *  You are given a string s of lowercase English letters and an integer array
 *  shifts of the same length.
 *
 *  Call the shift() of a letter, the next letter in the alphabet, (wrapping
 *  around so that 'z' becomes 'a').
 *
 *  Now for each shifts[i] = x, we want to shift the first i + 1 letters of s,
 *  x times. Return the final string after all such shifts to s are applied.
 *
 *  Example 1:
 *  Input: s = "abc", shifts = [3,5,9]
 *  Output: "rpl"
 *  Explanation:
 *   after shifting the first 1 letters of s by 3, we have "dbc".
 *   after shifting the first 2 letters of s by 5, we have "igc".
 *   after shifting the first 3 letters of s by 9, we have "rpl".
 *
 *  Example 2:
 *  Input: s = "aaa", shifts = [1,2,3]
 *  Output: "gfd"
 *
 *  Constraints:
 *   - 1 <= s.length <= 10^5
 *   - shifts.length == s.length
 *   - 0 <= shifts[i] <= 10^9
 */
public class ShiftingLetters {

    // V0
    // IDEA: SUFFIX SUM - the total shift for index i is sum(shifts[i..n-1]) % 26,
    //       so walk from the right accumulating mod 26.
    /**
     * time = O(n)
     * space = O(n)
     */
    public String shiftingLetters(String s, int[] shifts) {
        int n = s.length();
        char[] res = s.toCharArray();
        int total = 0;
        for (int i = n - 1; i >= 0; i--) {
            total = (int) ((total + (long) shifts[i]) % 26);
            int idx = res[i] - 'a';
            res[i] = (char) ('a' + (idx + total) % 26);
        }
        return new String(res);
    }
}
