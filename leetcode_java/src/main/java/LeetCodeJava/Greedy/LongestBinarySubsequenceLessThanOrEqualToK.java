package LeetCodeJava.Greedy;

// https://leetcode.com/problems/longest-binary-subsequence-less-than-or-equal-to-k/description/

import java.util.*;

/**
 * 2311. Longest Binary Subsequence Less Than or Equal to K
 * Medium
 *
 * You are given a binary string s and a positive integer k.
 *
 * Return the length of the longest subsequence of s that makes up a binary number less than or equal to k.
 *
 * Note:
 *
 * The subsequence can contain leading zeroes.
 * The empty string is considered to be equal to 0.
 * A subsequence is a string that can be derived from another string by deleting some or no characters without changing the order of the remaining characters.
 *
 *
 * Example 1:
 *
 * Input: s = "1001010", k = 5
 * Output: 5
 * Explanation: The longest subsequence of s that makes up a binary number less than or equal to 5 is "00010", as this number is equal to 2 in decimal.
 * Note that "00100" and "00101" are also possible, which are equal to 4 and 5 in decimal, respectively.
 * The length of this subsequence is 5, so 5 is returned.
 *
 * Example 2:
 *
 * Input: s = "00101001", k = 1
 * Output: 6
 * Explanation: "000001" is the longest subsequence of s that makes up a binary number less than or equal to 1, as this number is equal to 1 in decimal.
 * The length of this subsequence is 6, so 6 is returned.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * s[i] is either '0' or '1'.
 * 1 <= k <= 10^9
 *
 */
public class LongestBinarySubsequenceLessThanOrEqualToK {

    // V0
    // IDEA: GREEDY FROM THE RIGHT (EVERY '0' IS FREE, TAKE THE CHEAPEST '1's FIRST)
    //       leading zeros do not change the value, so all zeros are kept.
    //       a '1' taken when the answer already has `length` chars sits at bit
    //       position `length` and adds 2^length; the rightmost ones are cheapest.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int longestSubsequence(String s, int k) {
        // k <= 10^9 < 2^30, so a '1' at bit position >= 30 can never fit
        final int MAX_BITS = 30;

        int length = 0;
        long value = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if (c == '0') {
                // a zero adds nothing to the value
                length++;
            } else if (length < MAX_BITS && value + (1L << length) <= k) {
                // this '1' lands at bit position `length`
                value += 1L << length;
                length++;
            }
        }
        return length;
    }
}
