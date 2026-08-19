package LeetCodeJava.Array;

// https://leetcode.com/problems/number-of-subarrays-with-bounded-maximum/

/**
 *  795. Number of Subarrays with Bounded Maximum
 *  Medium
 *
 *  Given an integer array nums and two integers left and right, return the number
 *  of contiguous non-empty subarrays such that the value of the maximum array
 *  element in that subarray is in the range [left, right].
 *
 *  The test cases are generated so that the answer will fit in a 32-bit integer.
 *
 *  Example 1:
 *    Input: nums = [2,1,4,3], left = 2, right = 3
 *    Output: 3
 *    Explanation: There are three subarrays that meet the requirements:
 *    [2], [2, 1], [3].
 *
 *  Example 2:
 *    Input: nums = [2,9,2,5,6], left = 2, right = 8
 *    Output: 7
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    0 <= nums[i] <= 10^9
 *    0 <= left <= right <= 10^9
 */
public class NumberOfSubarraysWithBoundedMaximum {

    // V0
    // IDEA: COUNT(bound) = number of subarrays whose every element <= bound.
    //       answer = count(right) - count(left - 1).
    /**
     * time = O(n)
     * space = O(1)
     */
    public int numSubarrayBoundedMax(int[] nums, int left, int right) {
        return countMaxAtMost(nums, right) - countMaxAtMost(nums, left - 1);
    }

    private int countMaxAtMost(int[] nums, int bound) {
        int res = 0, cur = 0;
        for (int v : nums) {
            cur = (v <= bound) ? cur + 1 : 0;
            res += cur;
        }
        return res;
    }

    // V1
    // IDEA: ONE PASS DP. dp = number of valid subarrays ending at i;
    //       prev = index of the last element > right.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int numSubarrayBoundedMax_1(int[] nums, int left, int right) {
        int res = 0, dp = 0, prev = -1;
        for (int i = 0; i < nums.length; i++) {
            int a = nums[i];
            if (a > right) {
                dp = 0;
                prev = i;
            } else if (a >= left) {
                dp = i - prev;
                res += dp;
            } else {
                res += dp;
            }
        }
        return res;
    }
}
