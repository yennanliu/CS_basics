package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-the-sum-of-subsequence-powers/description/

import java.util.*;

/**
 * 3098. Find the Sum of Subsequence Powers
 * Hard
 *
 * You are given an integer array nums of length n, and a positive integer k.
 *
 * The power of a subsequence is defined as the minimum absolute difference between any two elements in the subsequence.
 *
 * Return the sum of powers of all subsequences of nums which have length equal to k.
 *
 * Since the answer may be large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4], k = 3
 * Output: 4
 * Explanation:
 * There are 4 subsequences in nums which have length 3: [1,2,3], [1,3,4], [1,2,4], and [2,3,4]. The sum of powers is |2 - 3| + |3 - 4| + |2 - 1| + |3 - 4| = 4.
 *
 * Example 2:
 *
 * Input: nums = [2,2], k = 2
 * Output: 0
 * Explanation:
 * The only subsequence in nums which has length 2 is [2,2]. The sum of powers is |2 - 2| = 0.
 *
 * Example 3:
 *
 * Input: nums = [4,3,-1], k = 2
 * Output: 10
 * Explanation:
 * There are 3 subsequences in nums which have length 2: [4,3], [4,-1], and [3,-1]. The sum of powers is |4 - 3| + |4 - (-1)| + |3 - (-1)| = 10.
 *
 *
 * Constraints:
 *
 * 2 <= n == nums.length <= 50
 * -10^8 <= nums[i] <= 10^8
 * 2 <= k <= n
 *
 */
public class FindTheSumOfSubsequencePowers {

    // V0
    // IDEA: SUM THE MINIMUM BY LAYERS + COUNTING DP WITH A MOVING POINTER
    //   For a non-negative value v: v = sum over d >= 1 of [v >= d]. So
    //   answer = sum over d >= 1 of #{length-k subsequences whose min gap >= d}.
    //   That count only changes at the distinct pairwise differences D_1 < D_2 < ..., so
    //   answer = sum over t of (D_t - D_{t-1}) * countAtLeast(D_t).
    //   countAtLeast(d), with nums sorted: ways[j][i] = ways to pick j elements ending at i
    //   with all consecutive gaps >= d = sum of ways[j-1][t] for t with nums[i] - nums[t] >= d.
    //   Those t form a prefix, so a pointer plus prefix sums gives O(N * K) per d.
    /**
     * time = O(N^3 * K)
     * space = O(N^2 + N * K)
     */
    public int sumOfPowers(int[] nums, int k) {
        final long MOD = 1_000_000_007L;
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int n = sorted.length;

        // all distinct pairwise differences, plus 0 as the lower edge of the first layer
        TreeSet<Long> diffSet = new TreeSet<>();
        diffSet.add(0L);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                diffSet.add((long) sorted[j] - sorted[i]);
            }
        }
        List<Long> diffs = new ArrayList<>(diffSet);

        long total = 0;
        for (int t = 1; t < diffs.size(); t++) {
            long layerHeight = (diffs.get(t) - diffs.get(t - 1)) % MOD;
            long count = countAtLeast(sorted, k, diffs.get(t), MOD);
            total = (total + layerHeight * count) % MOD;
        }
        return (int) total;
    }

    /** Number of length-k subsequences of sorted whose consecutive gaps are all >= minGap. */
    private long countAtLeast(int[] sorted, int k, long minGap, long mod) {
        int n = sorted.length;
        // ways[i] for the current length j: subsequences of length j ending exactly at i
        long[] ways = new long[n];
        Arrays.fill(ways, 1);
        for (int length = 2; length <= k; length++) {
            // prefix[i] = sum of ways[0 .. i-1] for the previous length
            long[] prefix = new long[n + 1];
            for (int i = 0; i < n; i++) {
                prefix[i + 1] = (prefix[i] + ways[i]) % mod;
            }
            long[] nextWays = new long[n];
            // farEnough = number of leading indices t with sorted[i] - sorted[t] >= minGap
            int farEnough = 0;
            for (int i = 0; i < n; i++) {
                while (farEnough < i && (long) sorted[i] - sorted[farEnough] >= minGap) {
                    farEnough++;
                }
                nextWays[i] = prefix[farEnough];
            }
            ways = nextWays;
        }
        long count = 0;
        for (long w : ways) {
            count = (count + w) % mod;
        }
        return count;
    }
}
