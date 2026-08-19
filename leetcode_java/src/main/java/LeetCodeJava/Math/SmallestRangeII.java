package LeetCodeJava.Math;

// https://leetcode.com/problems/smallest-range-ii/

import java.util.Arrays;

/**
 *  910. Smallest Range II
 *  Medium
 *
 *  You are given an integer array nums and an integer k.
 *
 *  For each index i where 0 <= i < nums.length, change nums[i] to be either
 *  nums[i] + k or nums[i] - k.
 *
 *  The score of nums is the difference between the maximum and minimum elements
 *  in nums.
 *
 *  Return the minimum score of nums after changing the values at each index.
 *
 *  Example 1:
 *   Input: nums = [1], k = 0
 *   Output: 0
 *
 *  Example 2:
 *   Input: nums = [0,10], k = 2
 *   Output: 6
 *   Explanation: change to [2,8] -> score 6.
 *
 *  Example 3:
 *   Input: nums = [1,3,6], k = 3
 *   Output: 3
 *   Explanation: change to [4,6,3] -> score 3.
 *
 *  Constraints:
 *   - 1 <= nums.length <= 10^4
 *   - 0 <= nums[i] <= 10^4
 *   - 0 <= k <= 10^4
 */
public class SmallestRangeII {

    // V0
    // IDEA: GREEDY + SORT. After sorting, the optimal choice adds k to a prefix
    //       nums[0..i] and subtracts k from the suffix nums[i+1..n-1].
    //       Try every split point i:
    //         max = max(nums[n-1] - k, nums[i] + k)
    //         min = min(nums[0] + k,   nums[i+1] - k)
    /**
     * time = O(n log n)
     * space = O(1)  (in-place sort)
     */
    public int smallestRangeII(int[] nums, int k) {
        Arrays.sort(nums);
        int n = nums.length;
        int res = nums[n - 1] - nums[0];
        for (int i = 0; i < n - 1; i++) {
            int max = Math.max(nums[n - 1] - k, nums[i] + k);
            int min = Math.min(nums[0] + k, nums[i + 1] - k);
            res = Math.min(res, max - min);
        }
        return res;
    }
}
