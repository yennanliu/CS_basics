package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/shortest-subarray-with-sum-at-least-k/description/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 862. Shortest Subarray with Sum at Least K
 * Hard
 *
 * Given an integer array nums and an integer k, return the length of the shortest
 * non-empty subarray of nums with a sum of at least k. If there is no such subarray,
 * return -1.
 *
 * A subarray is a contiguous part of an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [1], k = 1
 * Output: 1
 *
 * Example 2:
 *
 * Input: nums = [1,2], k = 4
 * Output: -1
 *
 * Example 3:
 *
 * Input: nums = [2,-1,2], k = 3
 * Output: 3
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * -10^5 <= nums[i] <= 10^5
 * 1 <= k <= 10^9
 *
 */
public class ShortestSubarrayWithSumAtLeastK {

    // V0
    // IDEA: PREFIX SUM + MONOTONIC DEQUE
    /**
     *   With NEGATIVE numbers the classic sliding window BREAKS (shrinking the
     *   window can INCREASE the sum), so we work on PREFIX SUMS instead:
     *
     *       sum(nums[j..i-1]) = prefix[i] - prefix[j]
     *
     *   We want the smallest (i - j) with prefix[i] - prefix[j] >= k.
     *
     *   Keep a deque of candidate indices j with STRICTLY INCREASING prefix values:
     *
     *     - pop from the BACK while prefix[back] >= prefix[i]:
     *       a LATER index with a smaller-or-equal prefix is always at least as good
     *       (shorter subarray AND bigger sum), so the older one is USELESS.
     *
     *     - pop from the FRONT while prefix[i] - prefix[front] >= k:
     *       record the length; that front can NEVER give a shorter answer for a
     *       later i, so it is safe to discard.
     *
     *   Every index enters and leaves the deque at most once -> LINEAR time.
     *
     *   NOTE !!! prefix sums reach 10^5 * 10^5 = 10^10, which OVERFLOWS int
     *            -> the prefix array must be `long`.
     *
     *   time  = O(n)
     *   space = O(n)
     */
    public int shortestSubarray(int[] nums, int k) {
        int n = nums.length;

        // prefix[i] = sum of the first i elements
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

        int ans = n + 1;
        Deque<Integer> q = new ArrayDeque<>(); // indices into prefix, values increasing

        for (int i = 0; i <= n; i++) {
            long cur = prefix[i];

            // keep the deque INCREASING
            while (!q.isEmpty() && prefix[q.peekLast()] >= cur) {
                q.pollLast();
            }

            // the front already satisfies the target -> shortest for this i
            while (!q.isEmpty() && cur - prefix[q.peekFirst()] >= k) {
                ans = Math.min(ans, i - q.pollFirst());
            }

            q.offerLast(i);
        }

        return ans <= n ? ans : -1;
    }

}
