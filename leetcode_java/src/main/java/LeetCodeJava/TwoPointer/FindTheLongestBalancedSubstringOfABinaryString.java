package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/find-the-longest-balanced-substring-of-a-binary-string/description/

/**
 * 2609. Find the Longest Balanced Substring of a Binary String
 * Easy
 *
 * You are given a binary string s consisting only of zeroes and ones.
 *
 * A substring of s is considered balanced if all zeroes are before ones and the number of zeroes
 * is equal to the number of ones inside the substring. Notice that the empty substring is
 * considered a balanced substring.
 *
 * Return the length of the longest balanced substring of s.
 *
 * A substring is a contiguous sequence of characters within a string.
 *
 *
 * Example 1:
 *
 * Input: s = "01000111"
 * Output: 6
 * Explanation: The longest balanced substring is "000111", which has length 6.
 *
 * Example 2:
 *
 * Input: s = "00111"
 * Output: 4
 * Explanation: The longest balanced substring is "0011", which has length 4.
 *
 * Example 3:
 *
 * Input: s = "111"
 * Output: 0
 * Explanation: There is no balanced substring except the empty substring, so the answer is 0.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 50
 * '0' <= s[i] <= '1'
 *
 */
public class FindTheLongestBalancedSubstringOfABinaryString {

    // V0
    // IDEA: RUN-LENGTH COUNTERS OVER EACH 0-RUN / 1-RUN PAIR
    //  a balanced substring is "0" * t + "1" * t, so it sits inside a run of zeros
    //  immediately followed by a run of ones; the best one there is 2 * min(zeros, ones).
    /**
     * time = O(N)
     * space = O(1)
     */
    public int findTheLongestBalancedSubstring(String s) {
        // the empty substring is balanced, so the answer is at least 0
        int best = 0;
        int zeros = 0;
        int ones = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '0') {
                // the first '0' after some '1's starts a brand new 0-run / 1-run pair
                if (ones > 0) {
                    zeros = 0;
                    ones = 0;
                }
                zeros++;
            } else {
                ones++;
                // best balanced block ending here uses min(zeros, ones) of each
                int balanced = 2 * Math.min(zeros, ones);
                best = Math.max(best, balanced);
            }
        }
        return best;
    }
}
