package LeetCodeJava.Stack;

// https://leetcode.com/problems/maximum-and-minimum-sums-of-at-most-size-k-subarrays/description/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 3430. Maximum and Minimum Sums of at Most Size K Subarrays
 * Hard
 *
 * You are given an integer array nums and a positive integer k. Return the sum of the maximum
 * and minimum elements of all subarrays with at most k elements.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3], k = 2
 * Output: 20
 * Explanation:
 * The subarrays of nums with at most 2 elements are:
 *   Subarray | Minimum | Maximum | Sum
 *   [1] | 1 | 1 | 2
 *   [2] | 2 | 2 | 4
 *   [3] | 3 | 3 | 6
 *   [1, 2] | 1 | 2 | 3
 *   [2, 3] | 2 | 3 | 5
 *   Final Total |  |  | 20
 *
 * Example 2:
 *
 * Input: nums = [1,-3,1], k = 2
 * Output: -6
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 80000
 * 1 <= k <= nums.length
 * -10^6 <= nums[i] <= 10^6
 *
 */
public class MaximumAndMinimumSumsOfAtMostSizeKSubarrays {

    // V0
    // IDEA: CONTRIBUTION TECHNIQUE WITH A MONOTONIC STACK + O(1) COUNT FOR THE LENGTH CAP
    //  nums[i] is the max of every subarray whose left end is in (prev >= nums[i], i] and right
    //  end in [i, next > nums[i]) — the >= / > asymmetry gives ties to exactly one index.
    //  With x = i - l <= P and y = r - i <= Q, the cap is x + y <= k - 1; count those (x, y) by
    //  inclusion-exclusion:  tri(K) - tri(K-P-1) - tri(K-Q-1) + tri(K-P-Q-2),  K = k - 1.
    //  The minimum is the same computation with the comparisons flipped (sign = -1).
    /**
     * time = O(N)
     * space = O(N)
     */
    public long minMaxSubarraySum(int[] nums, int k) {
        return sumOfExtremes(nums, k, 1) + sumOfExtremes(nums, k, -1);
    }

    /** Sum over i of nums[i] * (#subarrays of length <= k where i is the max; sign -1: the min). */
    private long sumOfExtremes(int[] nums, int k, int sign) {
        int n = nums.length;
        int[] leftBound = new int[n];   // smallest left end where i is still the extreme
        int[] rightBound = new int[n];  // largest right end where i is still the extreme

        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            long value = (long) nums[i] * sign;
            // pop strictly weaker elements: the previous element >= value stops the left side
            while (!stack.isEmpty() && (long) nums[stack.peek()] * sign < value) {
                stack.pop();
            }
            leftBound[i] = stack.isEmpty() ? 0 : stack.peek() + 1;
            stack.push(i);
        }

        stack.clear();
        for (int i = n - 1; i >= 0; i--) {
            long value = (long) nums[i] * sign;
            // pop weaker-or-equal elements: the next element > value stops the right side
            while (!stack.isEmpty() && (long) nums[stack.peek()] * sign <= value) {
                stack.pop();
            }
            rightBound[i] = stack.isEmpty() ? n - 1 : stack.peek() - 1;
            stack.push(i);
        }

        long total = 0;
        for (int i = 0; i < n; i++) {
            long subarrays = countCapped(i - leftBound[i], rightBound[i] - i, k - 1);
            total += (long) nums[i] * subarrays;
        }
        return total;
    }

    /** Number of (x, y) with 0 <= x <= maxX, 0 <= y <= maxY, x + y <= cap. */
    private long countCapped(long maxX, long maxY, long cap) {
        return triangle(cap) - triangle(cap - maxX - 1) - triangle(cap - maxY - 1)
                + triangle(cap - maxX - maxY - 2);
    }

    /** Number of (x, y) with x, y >= 0 and x + y <= t; 0 when t < 0. */
    private long triangle(long t) {
        if (t < 0) {
            return 0;
        }
        return (t + 1) * (t + 2) / 2;
    }
}
