package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-stable-subsequences/description/

import java.util.*;

/**
 * 3686. Number of Stable Subsequences
 * Hard
 *
 * You are given an integer array nums.
 *
 * A subsequence is stable if it does not contain three consecutive elements
 * with the same parity when the subsequence is read in order (i.e.,
 * consecutive inside the subsequence).
 *
 * Return the number of stable subsequences.
 *
 * Since the answer may be too large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3,5]
 * Output: 6
 * Explanation:
 * Stable subsequences are [1], [3], [5], [1, 3], [1, 5], and [3, 5].
 * Subsequence [1, 3, 5] is not stable because it contains three consecutive
 * odd numbers. Thus, the answer is 6.
 *
 * Example 2:
 *
 * Input: nums = [2,3,4,2]
 * Output: 14
 * Explanation:
 * The only subsequence that is not stable is [2, 4, 2], which contains three
 * consecutive even numbers. Thus, the answer is 14.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^5
 *
 */
public class NumberOfStableSubsequences {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DP ON (PARITY OF THE LAST ELEMENT, LENGTH OF ITS PARITY RUN)
    //   whether an element may be appended depends only on the tail: the parity of
    //   the last element and whether the last one or last two share it.
    //   runOfOne[p] = stable subsequences ending in exactly one element of parity p
    //   runOfTwo[p] = stable subsequences ending in exactly two elements of parity p
    //   for an element of parity p (q = the other parity):
    //     runOfOne[p] += runOfOne[q] + runOfTwo[q] + 1   (follow the other parity, or start)
    //     runOfTwo[p] += runOfOne[p]                     (old value: extend a run of one)
    //   a run of two is never extended - that would be three in a row.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int countStableSubsequences(int[] nums) {
        long[] runOfOne = new long[2];
        long[] runOfTwo = new long[2];
        for (int num : nums) {
            int p = num % 2;
            int q = 1 - p;
            long newRunsOfOne = (runOfOne[q] + runOfTwo[q] + 1) % MOD;
            long newRunsOfTwo = runOfOne[p];
            runOfOne[p] = (runOfOne[p] + newRunsOfOne) % MOD;
            runOfTwo[p] = (runOfTwo[p] + newRunsOfTwo) % MOD;
        }
        long total = runOfOne[0] + runOfOne[1] + runOfTwo[0] + runOfTwo[1];
        return (int) (total % MOD);
    }
}
