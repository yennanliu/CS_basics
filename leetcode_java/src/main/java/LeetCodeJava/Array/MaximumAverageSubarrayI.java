package LeetCodeJava.Array;

// https://leetcode.com/problems/maximum-average-subarray-i/

/**
 *  643. Maximum Average Subarray I
 *  Easy
 *
 *  You are given an integer array nums consisting of n elements, and an integer k.
 *
 *  Find a contiguous subarray whose length is equal to k that has the maximum
 *  average value and return this value. Any answer with a calculation error less
 *  than 10^-5 will be accepted.
 *
 *  Example 1:
 *  Input: nums = [1,12,-5,-6,50,3], k = 4
 *  Output: 12.75000
 *  Explanation: Maximum average is (12 - 5 - 6 + 50) / 4 = 51 / 4 = 12.75
 *
 *  Example 2:
 *  Input: nums = [5], k = 1
 *  Output: 5.00000
 *
 *  Constraints:
 *  n == nums.length
 *  1 <= k <= n <= 10^5
 *  -10^4 <= nums[i] <= 10^4
 */
public class MaximumAverageSubarrayI {

    // V0
    // IDEA: SLIDING WINDOW - keep the sum of a fixed size k window
    /**
     * time = O(n)
     * space = O(1)
     */
    public double findMaxAverage(int[] nums, int k) {
        long sum = 0;
        for (int i = 0; i < k; i++) {
            sum += nums[i];
        }
        long best = sum;
        for (int i = k; i < nums.length; i++) {
            sum += nums[i] - nums[i - k];
            best = Math.max(best, sum);
        }
        return (double) best / k;
    }
}
