package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/count-substrings-that-satisfy-k-constraint-i/

/**
 *  3258. Count Substrings That Satisfy K-Constraint I
 *  Easy
 *
 *  You are given a binary string s and an integer k.
 *
 *  A binary string satisfies the k-constraint if either of the following holds:
 *   - The number of 0's in the string is at most k.
 *   - The number of 1's in the string is at most k.
 *
 *  Return an integer denoting the number of substrings of s that satisfy the
 *  k-constraint.
 *
 *  Example 1:
 *    Input: s = "10101", k = 1
 *    Output: 12
 *    Explanation: Every substring except "1010", "10101" and "0101" satisfies
 *                 the k-constraint.
 *
 *  Example 2:
 *    Input: s = "1010101", k = 2
 *    Output: 25
 *    Explanation: Every substring of length at most 5 satisfies it.
 *
 *  Example 3:
 *    Input: s = "11111", k = 1
 *    Output: 15
 *
 *  Constraints:
 *    1 <= s.length <= 50
 *    1 <= k <= s.length
 *    s[i] is either '0' or '1'.
 */
public class CountSubstringsThatSatisfyKConstraintI {

    // V0
    // IDEA: SLIDING WINDOW — THE CONSTRAINT IS MONOTONE IN THE WINDOW SIZE
    //       growing a substring can only raise both counts, so once a window
    //       breaks the rule every longer one with the same left end does too.
    //       that makes a two-pointer sweep exact: extend the right end, and
    //       shrink from the left until min(zeros, ones) <= k again. each right
    //       end then contributes (right - left + 1) valid substrings.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int countKConstraintSubstrings(String s, int k) {
        int zeros = 0;
        int ones = 0;
        int left = 0;
        int res = 0;
        for (int right = 0; right < s.length(); right++) {
            if (s.charAt(right) == '0') {
                zeros++;
            } else {
                ones++;
            }
            while (zeros > k && ones > k) {
                if (s.charAt(left) == '0') {
                    zeros--;
                } else {
                    ones--;
                }
                left++;
            }
            res += right - left + 1;
        }
        return res;
    }
}
