package LeetCodeJava.Greedy;

// https://leetcode.com/problems/split-a-string-in-balanced-strings/description/

/**
 * 1221. Split a String in Balanced Strings
 * Easy
 *
 * Balanced strings are those that have an equal quantity of 'L' and 'R' characters.
 *
 * Given a balanced string s, split it into some number of substrings such that:
 *
 * Each substring is balanced.
 *
 * Return the maximum number of balanced strings you can obtain.
 *
 *
 * Example 1:
 *
 * Input: s = "RLRRLLRLRL"
 * Output: 4
 * Explanation: s can be split into "RL", "RRLL", "RL", "RL", each substring contains same
 * number of 'L' and 'R'.
 *
 * Example 2:
 *
 * Input: s = "RLRRRLLRLL"
 * Output: 2
 * Explanation: s can be split into "RL", "RRRLLRLL", each substring contains same number
 * of 'L' and 'R'.
 * Note that s cannot be split into "RL", "RR", "RL", "LR", "LL", because the 2nd and 5th
 * substrings are not balanced.
 *
 * Example 3:
 *
 * Input: s = "LLLLRRRR"
 * Output: 1
 * Explanation: s can be split into "LLLLRRRR".
 *
 *
 * Constraints:
 *
 * 2 <= s.length <= 1000
 * s[i] is either 'L' or 'R'.
 * s is a balanced string.
 *
 */
public class SplitAStringInBalancedStrings {

    // V0
    // IDEA: GREEDY + RUNNING BALANCE (cut every time #L - #R returns to 0)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int balancedStringSplit(String s) {
        int pieces = 0;
        // balance = (#L - #R) since the last cut
        int balance = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'L') {
                balance++;
            } else {
                balance--;
            }
            // a balanced chunk just closed; cutting as early as possible is optimal
            // because a later cut could only merge chunks, never create more
            if (balance == 0) {
                pieces++;
            }
        }
        return pieces;
    }
}
