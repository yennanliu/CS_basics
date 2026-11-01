package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-multiplication-score/description/

import java.util.*;

/**
 * 3290. Maximum Multiplication Score
 * Medium
 *
 * You are given an integer array a of size 4 and another integer array b of size at least 4.
 *
 * You need to choose 4 indices i0, i1, i2, and i3 from the array b such that i0 < i1 < i2 < i3. Your score will be equal to the value a[0] * b[i0] + a[1] * b[i1] + a[2] * b[i2] + a[3] * b[i3].
 *
 * Return the maximum score you can achieve.
 *
 *
 * Example 1:
 *
 * Input: a = [3,2,5,6], b = [2,-6,4,-5,-3,2,-7]
 * Output: 26
 * Explanation:
 * We can choose the indices 0, 1, 2, and 5. The score will be 3 * 2 + 2 * (-6) + 5 * 4 + 6 * 2 = 26.
 *
 * Example 2:
 *
 * Input: a = [-1,4,5,-2], b = [-5,-1,-3,-2,-4]
 * Output: -1
 * Explanation:
 * We can choose the indices 0, 1, 3, and 4. The score will be (-1) * (-5) + 4 * (-1) + 5 * (-2) + (-2) * (-4) = -1.
 *
 *
 * Constraints:
 *
 * a.length == 4
 * 4 <= b.length <= 10^5
 * -10^5 <= a[i], b[i] <= 10^5
 *
 */
public class MaximumMultiplicationScore {

    // V0
    // IDEA: 0/1-KNAPSACK STYLE DP OVER "HOW MANY COEFFICIENTS PLACED"
    /**
     *  Scanning b left to right, the only state that matters is how many of the
     *  four a-coefficients are already placed: indices must increase, so a later
     *  b element can only take a later coefficient.
     *
     *  DP def:
     *     dp[j] = best score having placed the first j coefficients (j = 0..4)
     *
     *  DP eq (for each x in b, j from 4 DOWN to 1):
     *     dp[j] = max(dp[j], dp[j-1] + a[j-1] * x)
     *     -> the downward j loop uses each b element at most once
     *
     *  Init:   dp[0] = 0, dp[1..4] = -inf (unreachable until enough elements seen)
     *  Answer: dp[4]
     *
     *  time = O(N)
     *  space = O(1)
     */
    public long maxScore(int[] a, int[] b) {
        // a sentinel far below any real score (|score| <= 4 * 10^10)
        final long UNREACHABLE = Long.MIN_VALUE / 2;

        long[] dp = new long[5];
        Arrays.fill(dp, UNREACHABLE);
        dp[0] = 0;

        for (int x : b) {
            for (int j = 4; j >= 1; j--) {
                if (dp[j - 1] == UNREACHABLE) {
                    continue;
                }
                // place coefficient a[j-1] on this element
                long candidate = dp[j - 1] + (long) a[j - 1] * x;
                dp[j] = Math.max(dp[j], candidate);
            }
        }
        return dp[4];
    }
}
