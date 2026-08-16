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


    // V1
    // IDEA: SAME DP, but the difference is COMPRESSED to an int rank
    /**
     *  V0 keys the inner map by a `long` difference. Collecting all O(n^2)
     *  differences once and replacing them by an integer RANK turns the inner maps
     *  into plain int arrays.
     *
     *  Same O(n^2) states, but array indexing instead of boxing and hashing.
     *
     *  time  = O(n^2)
     *  space = O(n^2)
     */
    public int numberOfArithmeticSlices_1(int[] nums) {
        int n = nums.length;

        Map<Long, Integer> rank = new HashMap<>();
        int[][] diffId = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                long d = (long) nums[i] - nums[j];
                diffId[i][j] = rank.computeIfAbsent(d, k -> rank.size());
            }
        }

        int distinct = rank.size();
        // dp[i][r] would be O(n * distinct); keep it sparse per row instead
        List<Map<Integer, Integer>> dp = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            dp.add(new HashMap<>());
        }

        int ans = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                int r = diffId[i][j];
                int atJ = dp.get(j).getOrDefault(r, 0);
                ans += atJ;
                dp.get(i).merge(r, atJ + 1, Integer::sum);
            }
        }
        return ans;
    }

    // V2
    // IDEA: COUNT ALL SUBSEQUENCES (length >= 2) AND SUBTRACT THE PAIRS
    /**
     *  Let W be the number of `weak` arithmetic subsequences (length >= 2). Every
     *  pair is one of them, so
     *
     *      answer = W - C(n, 2)
     *
     *  Accumulating W and subtracting the pair count at the end removes the
     *  per-step `ans += dp[j][d]` bookkeeping -- a single total instead of two.
     *
     *  time  = O(n^2)
     *  space = O(n^2)
     */
    public int numberOfArithmeticSlices_2(int[] nums) {
        int n = nums.length;
        List<Map<Long, Integer>> dp = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            dp.add(new HashMap<>());
        }

        long weak = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                long d = (long) nums[i] - nums[j];
                int atJ = dp.get(j).getOrDefault(d, 0);
                int here = atJ + 1;
                dp.get(i).merge(d, here, Integer::sum);
                weak += here;
            }
        }
        long pairs = (long) n * (n - 1) / 2;
        return (int) (weak - pairs);
    }

    // V3
    // IDEA: BRUTE FORCE over subsets (tiny n only)
    /**
     *  Enumerate every subset of size >= 3 and test whether it is arithmetic.
     *
     *  O(2^n * n), so it only runs for n <= ~20, but it is the definition and thus
     *  the oracle for the two DP formulations.
     *
     *  time  = O(2^n * n)
     *  space = O(n)
     */
    public int numberOfArithmeticSlices_3(int[] nums) {
        int n = nums.length;
        int res = 0;

        for (int mask = 0; mask < (1 << n); mask++) {
            if (Integer.bitCount(mask) < 3) {
                continue;
            }
            List<Integer> pick = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (((mask >> i) & 1) == 1) {
                    pick.add(nums[i]);
                }
            }
            long d = (long) pick.get(1) - pick.get(0);
            boolean ok = true;
            for (int t = 2; t < pick.size() && ok; t++) {
                if ((long) pick.get(t) - pick.get(t - 1) != d) {
                    ok = false;
                }
            }
            if (ok) {
                res += 1;
            }
        }
        return res;
    }

}
