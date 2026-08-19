package LeetCodeJava.Array;

// https://leetcode.com/problems/shortest-unsorted-continuous-subarray/

import java.util.Arrays;

/**
 *  581. Shortest Unsorted Continuous Subarray
 *  Medium
 *
 *  Given an integer array nums, you need to find one continuous subarray such that
 *  if you only sort this subarray in ascending order, then the whole array will be
 *  sorted in ascending order.
 *
 *  Return the shortest such subarray and output its length.
 *
 *  Example 1:
 *  Input: nums = [2,6,4,8,10,9,15]
 *  Output: 5
 *  Explanation: You need to sort [6, 4, 8, 10, 9] in ascending order to make the
 *  whole array sorted in ascending order.
 *
 *  Example 2:
 *  Input: nums = [1,2,3,4]
 *  Output: 0
 *
 *  Constraints:
 *  1 <= nums.length <= 10^4
 *  -10^5 <= nums[i] <= 10^5
 *
 *  Follow up: Can you solve it in O(n) time complexity?
 */
public class ShortestUnsortedContinuousSubarray {

    // V0
    // IDEA: one left-to-right pass tracking running max finds the right boundary
    //       (last index that is smaller than some earlier element); one right-to-left
    //       pass tracking running min finds the left boundary.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int findUnsortedSubarray(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }
        int n = nums.length;
        int end = -1;
        int start = 0;

        int runningMax = nums[0];
        for (int i = 1; i < n; i++) {
            if (nums[i] < runningMax) {
                end = i;
            } else {
                runningMax = nums[i];
            }
        }

        int runningMin = nums[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            if (nums[i] > runningMin) {
                start = i;
            } else {
                runningMin = nums[i];
            }
        }

        return end == -1 ? 0 : end - start + 1;
    }

    // V1
    // IDEA: sort a copy and compare - first / last mismatching index
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int findUnsortedSubarray_1(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }
        int n = nums.length;
        int[] sorted = Arrays.copyOf(nums, n);
        Arrays.sort(sorted);

        int l = 0;
        while (l < n && nums[l] == sorted[l]) {
            l++;
        }
        if (l == n) {
            return 0;
        }
        int r = n - 1;
        while (r > l && nums[r] == sorted[r]) {
            r--;
        }
        return r - l + 1;
    }
}
