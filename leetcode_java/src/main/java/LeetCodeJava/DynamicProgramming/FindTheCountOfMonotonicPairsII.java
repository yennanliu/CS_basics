package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-the-count-of-monotonic-pairs-ii/description/

/**
 * 3251. Find the Count of Monotonic Pairs II
 * Hard
 *
 * You are given an array of positive integers nums of length n.
 *
 * We call a pair of non-negative integer arrays (arr1, arr2) monotonic if:
 *
 * The lengths of both arrays are n.
 * arr1 is monotonically non-decreasing, in other words, arr1[0] <= arr1[1] <= ... <= arr1[n - 1].
 * arr2 is monotonically non-increasing, in other words, arr2[0] >= arr2[1] >= ... >= arr2[n - 1].
 * arr1[i] + arr2[i] == nums[i] for all indices 0 <= i <= n - 1.
 *
 * Return the count of monotonic pairs.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,2]
 * Output: 4
 * Explanation:
 * The good pairs are:
 * ([0, 1, 1], [2, 2, 1])
 * ([0, 1, 2], [2, 2, 0])
 * ([0, 2, 2], [2, 1, 0])
 * ([1, 2, 2], [1, 1, 0])
 *
 * Example 2:
 *
 * Input: nums = [5,5,5,5]
 * Output: 126
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 2000
 * 1 <= nums[i] <= 1000
 *
 */
public class FindTheCountOfMonotonicPairsII {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DP ON arr1[i] + PREFIX SUM (arr2 IS DETERMINED BY arr1)
    /**
     *  arr2[i] = nums[i] - arr1[i], so a pair is decided entirely by arr1, and
     *  the two monotonicity rules become rules on consecutive arr1 values:
     *
     *      arr1[i] >= arr1[i-1]                          (arr1 non-decreasing)
     *      arr1[i] >= arr1[i-1] + nums[i] - nums[i-1]    (arr2 non-increasing)
     *
     *  DP def:
     *     dp[v] = number of valid prefixes ending with arr1[i] == v,
     *             where 0 <= v <= nums[i] (so arr2[i] stays non-negative)
     *
     *  DP eq:
     *     dp_i[v] = sum of dp_{i-1}[p] for p <= v - max(0, nums[i] - nums[i-1])
     *     -> a prefix sum over the previous row answers each v in O(1)
     *
     *  Init:   dp_0[v] = 1 for v in [0, nums[0]]
     *  Answer: sum of the last row
     *
     *  time = O(N * M)   (M = max(nums))
     *  space = O(M)
     */
    public int countOfPairs(int[] nums) {
        int n = nums.length;
        int maxValue = 0;
        for (int num : nums) {
            maxValue = Math.max(maxValue, num);
        }

        // first position: arr1[0] may be anything in [0, nums[0]]
        long[] dp = new long[maxValue + 1];
        for (int v = 0; v <= nums[0]; v++) {
            dp[v] = 1;
        }

        for (int i = 1; i < n; i++) {
            // prefix[v + 1] = dp[0] + ... + dp[v]
            long[] prefix = new long[maxValue + 2];
            for (int v = 0; v <= maxValue; v++) {
                prefix[v + 1] = (prefix[v] + dp[v]) % MOD;
            }

            // arr1 must rise by at least this much to keep arr2 non-increasing
            int requiredRise = Math.max(0, nums[i] - nums[i - 1]);

            long[] next = new long[maxValue + 1];
            for (int v = 0; v <= nums[i]; v++) {
                // largest previous arr1 value that can precede v
                int bound = v - requiredRise;
                if (bound < 0) {
                    continue;
                }
                bound = Math.min(bound, maxValue);
                next[v] = prefix[bound + 1];
            }
            dp = next;
        }

        long total = 0;
        for (long ways : dp) {
            total = (total + ways) % MOD;
        }
        return (int) total;
    }
}
