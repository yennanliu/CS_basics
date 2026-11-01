package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-partitions-with-max-min-difference-at-most-k/description/

import java.util.*;

/**
 * 3578. Count Partitions With Max-Min Difference at Most K
 * Medium
 *
 * You are given an integer array nums and an integer k. Your task is to partition nums into one or more non-empty contiguous segments such that in each segment, the difference between its maximum and minimum elements is at most k.
 *
 * Return the total number of ways to partition nums under this condition.
 *
 * Since the answer may be too large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: nums = [9,4,1,3,7], k = 4
 * Output: 6
 * Explanation:
 * There are 6 valid partitions where the difference between the maximum and minimum elements in each segment is at most k = 4:
 * [[9], [4], [1], [3], [7]]
 * [[9], [4], [1], [3, 7]]
 * [[9], [4], [1, 3], [7]]
 * [[9], [4, 1], [3], [7]]
 * [[9], [4, 1], [3, 7]]
 * [[9], [4, 1, 3], [7]]
 *
 * Example 2:
 *
 * Input: nums = [3,3,4], k = 0
 * Output: 2
 * Explanation:
 * There are 2 valid partitions that satisfy the given conditions:
 * [[3], [3], [4]]
 * [[3, 3], [4]]
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 5 * 10^4
 * 1 <= nums[i] <= 10^9
 * 0 <= k <= 10^9
 *
 */
public class CountPartitionsWithMaxMinDifferenceAtMostK {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: PREFIX DP + SLIDING WINDOW (MONOTONIC MAX / MIN DEQUES) FOR THE LEGAL CUTS
    //   dp[i] = number of valid partitions of the first i elements
    //   the last segment nums[j..i-1] is legal iff max - min <= k. Shrinking a
    //   segment never breaks that, so the legal j form a range [left, i-1] whose
    //   left end only moves right as i grows -> a sliding window finds it.
    //   dp[i] = dp[left] + ... + dp[i-1] = prefix[i-1] - prefix[left-1]
    /**
     * time = O(N)
     * space = O(N)
     */
    public int countPartitions(int[] nums, int k) {
        int n = nums.length;
        long[] dp = new long[n + 1];
        // prefix[i] = dp[0] + ... + dp[i]
        long[] prefix = new long[n + 1];
        dp[0] = 1;
        prefix[0] = 1;

        // indices with decreasing values (front = window max) / increasing values (front = min)
        Deque<Integer> maxDeque = new ArrayDeque<>();
        Deque<Integer> minDeque = new ArrayDeque<>();
        int left = 0;
        for (int i = 1; i <= n; i++) {
            int right = i - 1;
            int value = nums[right];
            while (!maxDeque.isEmpty() && nums[maxDeque.peekLast()] <= value) {
                maxDeque.pollLast();
            }
            maxDeque.addLast(right);
            while (!minDeque.isEmpty() && nums[minDeque.peekLast()] >= value) {
                minDeque.pollLast();
            }
            minDeque.addLast(right);

            // shrink until the window nums[left..right] is legal
            while (nums[maxDeque.peekFirst()] - nums[minDeque.peekFirst()] > k) {
                if (maxDeque.peekFirst() == left) {
                    maxDeque.pollFirst();
                }
                if (minDeque.peekFirst() == left) {
                    minDeque.pollFirst();
                }
                left++;
            }

            // sum of dp[left .. i-1]
            long beforeLeft = (left > 0) ? prefix[left - 1] : 0;
            dp[i] = ((prefix[i - 1] - beforeLeft) % MOD + MOD) % MOD;
            prefix[i] = (prefix[i - 1] + dp[i]) % MOD;
        }
        return (int) dp[n];
    }
}
