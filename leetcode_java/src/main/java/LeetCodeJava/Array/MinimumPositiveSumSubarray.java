package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-positive-sum-subarray/description/

import java.util.List;

/**
 * 3364. Minimum Positive Sum Subarray
 * Easy
 *
 * You are given an integer array nums and two integers l and r. Your task is to find the minimum sum of a subarray whose size is between l and r (inclusive) and whose sum is greater than 0.
 *
 * Return the minimum sum of such a subarray. If no such subarray exists, return -1.
 *
 *
 * Example 1:
 *
 * Input: nums = [3, -2, 1, 4], l = 2, r = 3
 * Output: 1
 * Explanation:
 * The subarrays of length between l = 2 and r = 3 where the sum is greater than 0 are:
 * [3, -2] with a sum of 1
 * [1, 4] with a sum of 5
 * [3, -2, 1] with a sum of 2
 * [-2, 1, 4] with a sum of 3
 * Out of these, the subarray [3, -2] has a sum of 1, which is the smallest positive sum.
 *
 * Example 2:
 *
 * Input: nums = [-2, 2, -3, 1], l = 2, r = 3
 * Output: -1
 * Explanation:
 * There is no subarray of length between l and r that has a sum greater than 0.
 *
 * Example 3:
 *
 * Input: nums = [1, 2, 3, 4], l = 2, r = 4
 * Output: 3
 * Explanation:
 * The subarray [1, 2] has a length of 2 and the minimum sum greater than 0.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 1 <= l <= r <= nums.length
 * -1000 <= nums[i] <= 1000
 *
 */
public class MinimumPositiveSumSubarray {

    // V0
    // IDEA: BRUTE FORCE WITH A RUNNING SUM PER START (n <= 100)
    /**
     * time = O(N^2)
     * space = O(1)
     */
    public int minimumSumSubarray(List<Integer> nums, int l, int r) {
        int n = nums.size();
        int best = Integer.MAX_VALUE;

        for (int start = 0; start < n; start++) {
            int windowSum = 0;
            for (int end = start; end < n; end++) {
                int length = end - start + 1;
                if (length > r) {
                    break;
                }
                windowSum += nums.get(end);
                // "greater than 0" is strict
                if (length >= l && windowSum > 0) {
                    best = Math.min(best, windowSum);
                }
            }
        }
        return best == Integer.MAX_VALUE ? -1 : best;
    }
}
