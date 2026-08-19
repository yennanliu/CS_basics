package LeetCodeJava.Math;

// https://leetcode.com/problems/maximum-value-of-an-alternating-sequence/

/**
 *  3993. Maximum Value of an Alternating Sequence
 *  Medium
 *
 *  You are given three integers n, s, and m.
 *
 *  A sequence seq of integers of length n is considered valid if:
 *
 *   - seq[0] = s.
 *   - The sequence is alternating, meaning that either:
 *       seq[0] > seq[1] < seq[2] > ..., or
 *       seq[0] < seq[1] > seq[2] < ....
 *   - For every adjacent pair, |seq[i] - seq[i - 1]| <= m.
 *
 *  A sequence of length 1 is considered alternating.
 *
 *  Return the maximum possible element that can appear in any valid sequence.
 *
 *
 *  Example 1:
 *
 *  Input: n = 4, s = 3, m = 5
 *  Output: 12
 *  Explanation: one valid sequence is [3, 8, 7, 12], max element = 12.
 *
 *  Example 2:
 *
 *  Input: n = 2, s = 4, m = 3
 *  Output: 7
 *  Explanation: one valid sequence is [4, 7].
 *
 *
 *  Constraints:
 *
 *  1 <= n, s <= 10^9
 *  1 <= m <= 10^5
 */
public class MaximumValueOfAnAlternatingSequence {

    // V0
    // IDEA: MATH
    /**
     *  Peaks sit at ODD indices when we start with a rise:
     *    s, s+m, s+m-1, s+2m-1, ...
     *  Every rise adds at most m, every dip between two rises costs at
     *  least 1 (strict alternation).
     *
     *  #peaks reachable within length n  ->  k = n / 2
     *  best value = s + k*m - (k-1) = s + k*(m-1) + 1   (for k >= 1)
     *
     *  Starting with a dip is never better (its peaks are s + k*(m-1)).
     *
     * time = O(1)
     * space = O(1)
     */
    public long maximumValue(int n, int s, int m) {
        if (n == 1) {
            return s;
        }
        long k = n / 2;
        return (long) s + k * ((long) m - 1) + 1;
    }
}
