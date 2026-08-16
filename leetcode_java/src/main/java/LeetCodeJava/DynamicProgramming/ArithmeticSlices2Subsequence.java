package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/arithmetic-slices-ii-subsequence/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 446. Arithmetic Slices II - Subsequence
 * Hard
 *
 * Given an integer array nums, return the number of all the arithmetic
 * subsequences of nums.
 *
 * A sequence of numbers is called arithmetic if it consists of at least three
 * elements and if the difference between any two consecutive elements is the same.
 *
 * - For example, [1, 3, 5, 7, 9], [7, 7, 7, 7], and [3, -1, -5, -9] are arithmetic
 *   sequences.
 * - For example, [1, 1, 2, 5, 7] is not an arithmetic sequence.
 *
 * A subsequence of an array is a sequence that can be formed by removing some
 * elements (possibly none) of the array.
 *
 * - For example, [2,5,10] is a subsequence of [1,2,1,2,4,1,5,10].
 *
 * The test cases are generated so that the answer fits in 32-bit integer.
 *
 * Example 1:
 *
 * Input: nums = [2,4,6,8,10]
 * Output: 7
 * Explanation: All arithmetic subsequence slices are:
 * [2,4,6]
 * [4,6,8]
 * [6,8,10]
 * [2,4,6,8]
 * [4,6,8,10]
 * [2,4,6,8,10]
 * [2,6,10]
 *
 * Example 2:
 *
 * Input: nums = [7,7,7,7,7]
 * Output: 16
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * -2^31 <= nums[i] <= 2^31 - 1
 *
 */
public class ArithmeticSlices2Subsequence {

    // V0
    // IDEA: 2D DP OVER (INDEX, COMMON DIFFERENCE)
    /**
     *  DP def:
     *    dp[i][d] = number of `WEAK` arithmetic subsequences ending at index i with
     *               common difference d, where WEAK means length >= 2.
     *
     *  DP eq (for every j < i, d = nums[i] - nums[j]):
     *    dp[i][d] += dp[j][d] + 1
     *      - the `+ 1` is the BRAND NEW pair (nums[j], nums[i])   (length 2)
     *      - dp[j][d]  are the weak sequences ending at j that we EXTEND
     *
     *  Every one of those dp[j][d] sequences already had length >= 2, so extending
     *  it gives length >= 3 -> a REAL arithmetic subsequence. Hence:
     *
     *    ans += dp[j][d]
     *
     *  Counting WEAK (length >= 2) sequences is the trick: it lets ONE table carry
     *  both `things I can extend` and `things that already count`.
     *
     *  NOTE !!! nums[i] spans the full int range, so `nums[i] - nums[j]` OVERFLOWS
     *           int -> the difference key must be a `long`.
     *
     *  time  = O(n^2)
     *  space = O(n^2)
     */
    public int numberOfArithmeticSlices(int[] nums) {
        int n = nums.length;

        // dp[i] : difference -> count of weak sequences ending at i
        List<Map<Long, Integer>> dp = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            dp.add(new HashMap<>());
        }

        int ans = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                long d = (long) nums[i] - nums[j];

                int atJ = dp.get(j).getOrDefault(d, 0);
                // every weak seq ending at j becomes a VALID (len >= 3) one
                ans += atJ;
                // extend them, PLUS the new pair (j, i)
                dp.get(i).put(d, dp.get(i).getOrDefault(d, 0) + atJ + 1);
            }
        }

        return ans;
    }

}
