package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-changes-to-make-alternating-binary-string/description/

/**
 * 1758. Minimum Changes To Make Alternating Binary String
 * Easy
 *
 * You are given a string s consisting only of the characters '0' and '1'. In one
 * operation, you can change any '0' to '1' or vice versa.
 *
 * The string is called alternating if no two adjacent characters are equal. For
 * example, the string "010" is alternating, while the string "0100" is not.
 *
 * Return the minimum number of operations needed to make s alternating.
 *
 *
 * Example 1:
 *
 * Input: s = "0100"
 * Output: 1
 * Explanation: If you change the last character to '1', s will be "0101", which is
 * alternating.
 *
 * Example 2:
 *
 * Input: s = "10"
 * Output: 0
 * Explanation: s is already alternating.
 *
 * Example 3:
 *
 * Input: s = "1111"
 * Output: 2
 * Explanation: You need two operations to reach "0101" or "1010".
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^4
 * s[i] is either '0' or '1'.
 *
 */
public class MinimumChangesToMakeAlternatingBinaryString {

    // V0
    // IDEA: COUNTING (ONLY TWO ALTERNATING TARGETS EXIST)
    //       count mismatches against "0101..."; the two targets disagree at every
    //       position, so the mismatches against "1010..." are n - that count.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minOperations(String s) {
        int n = s.length();
        int mismatchesWithZeroFirst = 0;
        for (int i = 0; i < n; i++) {
            // "0101...": even positions expect '0', odd positions expect '1'
            char expected;
            if (i % 2 == 0) {
                expected = '0';
            } else {
                expected = '1';
            }
            if (s.charAt(i) != expected) {
                mismatchesWithZeroFirst++;
            }
        }
        return Math.min(mismatchesWithZeroFirst, n - mismatchesWithZeroFirst);
    }
}
