package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/maximum-sum-of-almost-unique-subarray/description/

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2841. Maximum Sum of Almost Unique Subarray
 * Medium
 *
 * You are given an integer array nums and two positive integers m and k.
 *
 * Return the maximum sum out of all almost unique subarrays of length k of nums. If no such
 * subarray exists, return 0.
 *
 * A subarray of nums is almost unique if it contains at least m distinct elements.
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,6,7,3,1,7], m = 3, k = 4
 * Output: 18
 *
 * Example 2:
 *
 * Input: nums = [5,9,9,2,4,5,4], m = 1, k = 3
 * Output: 23
 *
 * Example 3:
 *
 * Input: nums = [1,2,1,2,1,2,1], m = 3, k = 3
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2 * 10^4
 * 1 <= m <= k <= nums.length
 * 1 <= nums[i] <= 10^9
 *
 */
public class MaximumSumOfAlmostUniqueSubarray {

    // V0
    // IDEA: FIXED-SIZE SLIDING WINDOW + HASH MAP (RUNNING SUM AND DISTINCT COUNT)
    /**
     * time = O(N)
     * space = O(K)
     */
    public long maxSum(List<Integer> nums, int m, int k) {
        int n = nums.size();
        // value -> occurrences in the window; size() is the distinct count
        Map<Integer, Integer> window = new HashMap<>();
        long windowSum = 0;
        // no qualifying window -> 0
        long best = 0;

        for (int i = 0; i < n; i++) {
            int entering = nums.get(i);
            windowSum += entering;
            window.put(entering, window.getOrDefault(entering, 0) + 1);

            // keep the window at exactly k elements
            if (i >= k) {
                int leaving = nums.get(i - k);
                windowSum -= leaving;
                int remaining = window.get(leaving) - 1;
                if (remaining == 0) {
                    window.remove(leaving);
                } else {
                    window.put(leaving, remaining);
                }
            }

            if (i >= k - 1 && window.size() >= m) {
                best = Math.max(best, windowSum);
            }
        }
        return best;
    }
}
