package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-number-of-operations-to-move-ones-to-the-end/description/

/**
 * 3228. Maximum Number of Operations to Move Ones to the End
 * Medium
 *
 * You are given a binary string s.
 *
 * You can perform the following operation on the string any number of times:
 *
 * Choose any index i from the string where i + 1 < s.length such that s[i] == '1' and s[i + 1] == '0'.
 * Move the character s[i] to the right until it reaches the end of the string or another '1'. For example, for s = "010010", if we choose i = 1, the resulting string will be s = "000110".
 *
 * Return the maximum number of operations that you can perform.
 *
 *
 * Example 1:
 *
 * Input: s = "1001101"
 * Output: 4
 * Explanation:
 * We can perform the following operations:
 * Choose index i = 0. The resulting string is s = "0011101".
 * Choose index i = 4. The resulting string is s = "0011011".
 * Choose index i = 3. The resulting string is s = "0010111".
 * Choose index i = 2. The resulting string is s = "0001111".
 *
 * Example 2:
 *
 * Input: s = "00111"
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s[i] is either '0' or '1'.
 *
 */
public class MaximumNumberOfOperationsToMoveOnesToTheEnd {

    // V0
    // IDEA: GREEDY COUNT — EVERY '1' CROSSES EACH ZERO-BLOCK TO ITS RIGHT EXACTLY ONCE
    //       one operation slides a '1' across the whole run of zeros in front of it,
    //       so a '1' is moved once per zero-block lying to its right.
    //       summing per block instead: when a new zero-block starts, every '1' seen
    //       so far will eventually cross it -> add the ones count.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxOperations(String s) {
        int operations = 0;
        int onesSeen = 0;

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '1') {
                onesSeen++;
            } else if (i == 0 || s.charAt(i - 1) == '1') {
                // this '0' starts a new zero-block: every '1' to its left crosses it once
                operations += onesSeen;
            }
        }
        return operations;
    }
}
