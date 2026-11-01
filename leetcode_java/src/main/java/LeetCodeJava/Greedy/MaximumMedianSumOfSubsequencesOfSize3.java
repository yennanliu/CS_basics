package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-median-sum-of-subsequences-of-size-3/description/

import java.util.*;

/**
 * 3627. Maximum Median Sum of Subsequences of Size 3
 * Medium
 *
 * You are given an integer array nums with a length divisible by 3.
 *
 * You want to make the array empty in steps. In each step, you can select any
 * three elements from the array, compute their median, and remove the selected
 * elements from the array.
 *
 * The median of an odd-length sequence is defined as the middle element of the
 * sequence when it is sorted in non-decreasing order.
 *
 * Return the maximum possible sum of the medians computed from the selected
 * elements.
 *
 * Example 1:
 *
 * Input: nums = [2,1,3,2,1,3]
 * Output: 5
 * Explanation:
 * In the first step, select elements at indices 2, 4, and 5, which have a
 * median 3. After removing these elements, nums becomes [2, 1, 2].
 * In the second step, select elements at indices 0, 1, and 2, which have a
 * median 2. After removing these elements, nums becomes empty.
 * Hence, the sum of the medians is 3 + 2 = 5.
 *
 * Example 2:
 *
 * Input: nums = [1,1,10,10,10,10]
 * Output: 20
 * Explanation:
 * In the first step, select elements at indices 0, 2, and 3, which have a
 * median 10. After removing these elements, nums becomes [1, 10, 10].
 * In the second step, select elements at indices 0, 1, and 2, which have a
 * median 10. After removing these elements, nums becomes empty.
 * Hence, the sum of the medians is 10 + 10 = 20.
 *
 * Constraints:
 *
 * 1 <= nums.length <= 5 * 10^5
 * nums.length % 3 == 0
 * 1 <= nums[i] <= 10^9
 *
 */
public class MaximumMedianSumOfSubsequencesOfSize3 {

    // V0
    // IDEA: SORT + GREEDY — SMALLEST n/3 ARE FILLERS, MEDIANS ARE EVERY OTHER FROM THE TOP
    //       each group wastes one element below its median and one above. the n/3
    //       smallest values serve as the "below" elements; the rest are taken from the
    //       top in pairs (largest above, second largest = median).
    //       so the medians are sorted[n-2], sorted[n-4], ..., n/3 of them.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long maximumMedianSum(int[] nums) {
        int[] sorted = Arrays.copyOf(nums, nums.length);
        Arrays.sort(sorted);
        int n = sorted.length;

        long total = 0;
        for (int group = 1; group <= n / 3; group++) {
            total += sorted[n - 2 * group];
        }
        return total;
    }
}
