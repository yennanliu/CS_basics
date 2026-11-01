package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/constrained-subsequence-sum/description/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.PriorityQueue;

/**
 * 1425. Constrained Subsequence Sum
 * Hard
 *
 * Given an integer array nums and an integer k, return the maximum sum of a
 * non-empty subsequence of that array such that for every two consecutive
 * integers in the subsequence, nums[i] and nums[j], where i < j, the condition
 * j - i <= k is satisfied.
 *
 * A subsequence of an array is obtained by deleting some number of elements
 * (can be zero) from the array, leaving the remaining elements in their
 * original order.
 *
 *
 * Example 1:
 *
 * Input: nums = [10,2,-10,5,20], k = 2
 * Output: 37
 * Explanation: The subsequence is [10, 2, 5, 20].
 *
 * Example 2:
 *
 * Input: nums = [-1,-2,-3], k = 1
 * Output: -1
 * Explanation: The subsequence must be non-empty, so we choose the largest number.
 *
 * Example 3:
 *
 * Input: nums = [10,-2,-10,-5,20], k = 2
 * Output: 23
 * Explanation: The subsequence is [10, -2, -5, 20].
 *
 *
 * Constraints:
 *
 * 1 <= k <= nums.length <= 10^5
 * -10^4 <= nums[i] <= 10^4
 *
 */
public class ConstrainedSubsequenceSum {

    // V0
    // IDEA: DP + MONOTONIC DEQUE (sliding window maximum)
    //       dp[i] = max sum of a valid subsequence that ENDS at index i
    //       dp[i] = nums[i] + max(0, max(dp[i-k] .. dp[i-1]))
    //       (the max(0, ...) means "start a fresh subsequence at i")
    //       the inner max is a sliding window maximum of width k, so keep a
    //       deque of indices whose dp values are decreasing; the front is the max.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int constrainedSubsetSum(int[] nums, int k) {
        int n = nums.length;
        int[] dp = new int[n];
        Deque<Integer> window = new ArrayDeque<>();
        int best = Integer.MIN_VALUE;

        for (int i = 0; i < n; i++) {
            // drop indices that fell out of the window [i-k, i-1]
            while (!window.isEmpty() && window.peekFirst() < i - k) {
                window.pollFirst();
            }

            int bestPrev = window.isEmpty() ? 0 : dp[window.peekFirst()];
            dp[i] = nums[i] + Math.max(0, bestPrev);
            best = Math.max(best, dp[i]);

            // keep dp values decreasing from front to back
            while (!window.isEmpty() && dp[window.peekLast()] <= dp[i]) {
                window.pollLast();
            }
            window.offerLast(i);
        }
        return best;
    }

    // V1
    // IDEA: DP + MAX HEAP (lazy deletion)
    //       same recurrence, but the window max comes from a heap of
    //       {dp value, index}; entries whose index is older than i - k are
    //       popped only when they reach the top.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int constrainedSubsetSum_1(int[] nums, int k) {
        int n = nums.length;
        int[] dp = new int[n];
        // max heap on dp value
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> Integer.compare(b[0], a[0]));
        int best = Integer.MIN_VALUE;

        for (int i = 0; i < n; i++) {
            // lazily discard entries outside the window
            while (!heap.isEmpty() && heap.peek()[1] < i - k) {
                heap.poll();
            }

            int bestPrev = heap.isEmpty() ? 0 : heap.peek()[0];
            dp[i] = nums[i] + Math.max(0, bestPrev);
            best = Math.max(best, dp[i]);
            heap.offer(new int[] {dp[i], i});
        }
        return best;
    }
}
