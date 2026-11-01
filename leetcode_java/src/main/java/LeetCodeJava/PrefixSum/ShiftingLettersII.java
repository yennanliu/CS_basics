package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/shifting-letters-ii/description/

/**
 * 2381. Shifting Letters II
 * Medium
 *
 * You are given a string s of lowercase English letters and a 2D integer array shifts where shifts[i] = [start_i, end_i, direction_i]. For every i, shift the characters in s from the index start_i to the index end_i (inclusive) forward if direction_i = 1, or shift the characters backward if direction_i = 0.
 *
 * Shifting a character forward means replacing it with the next letter in the alphabet (wrapping around so that 'z' becomes 'a'). Similarly, shifting a character backward means replacing it with the previous letter in the alphabet (wrapping around so that 'a' becomes 'z').
 *
 * Return the final string after all such shifts to s are applied.
 *
 *
 * Example 1:
 *
 * Input: s = "abc", shifts = [[0,1,0],[1,2,1],[0,2,1]]
 * Output: "ace"
 * Explanation: Firstly, shift the characters from index 0 to index 1 backward. Now s = "zac".
 * Secondly, shift the characters from index 1 to index 2 forward. Now s = "zbd".
 * Finally, shift the characters from index 0 to index 2 forward. Now s = "ace".
 *
 * Example 2:
 *
 * Input: s = "dztz", shifts = [[0,0,0],[1,1,1]]
 * Output: "catz"
 * Explanation: Firstly, shift the characters from index 0 to index 0 backward. Now s = "cztz".
 * Finally, shift the characters from index 1 to index 1 forward. Now s = "catz".
 *
 *
 * Constraints:
 *
 * 1 <= s.length, shifts.length <= 5 * 10^4
 * shifts[i].length == 3
 * 0 <= start_i <= end_i < s.length
 * 0 <= direction_i <= 1
 * s consists of lowercase English letters.
 *
 */
public class ShiftingLettersII {

    // V0
    // IDEA: DIFFERENCE ARRAY — EACH SHIFT IS A RANGE +1 OR -1
    //       record +step at start and -step just past end, then one prefix
    //       sum gives the net shift of every position. Shifts commute, so
    //       only the totals matter.
    /**
     * time = O(N + Q)   Q = shifts.length
     * space = O(N)
     */
    public String shiftingLetters(String s, int[][] shifts) {
        int n = s.length();
        // diff[i] is the change in net shift when moving from index i - 1 to i
        int[] diff = new int[n + 1];
        for (int[] shift : shifts) {
            int start = shift[0];
            int end = shift[1];
            int step = (shift[2] == 1) ? 1 : -1;
            diff[start] += step;
            diff[end + 1] -= step;
        }

        StringBuilder result = new StringBuilder();
        int netShift = 0;
        for (int i = 0; i < n; i++) {
            netShift += diff[i];
            // Java's % can be negative, so normalise into [0, 26)
            int offset = ((s.charAt(i) - 'a' + netShift) % 26 + 26) % 26;
            result.append((char) ('a' + offset));
        }
        return result.toString();
    }
}
