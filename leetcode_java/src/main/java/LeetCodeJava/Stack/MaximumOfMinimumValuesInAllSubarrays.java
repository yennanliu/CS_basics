package LeetCodeJava.Stack;

// https://leetcode.com/problems/maximum-of-minimum-values-in-all-subarrays/

/**
 *  1950. Maximum of Minimum Values in All Subarrays
 *  Medium
 *
 *  You are given an integer array nums of size n. You are asked to solve n
 *  queries for each integer i in the range 0 <= i < n.
 *
 *  To solve the ith query:
 *    1. Find the minimum value in each possible subarray of size i + 1 of nums.
 *    2. Find the maximum of those minimum values.
 *
 *  Return a 0-indexed integer array ans of size n such that ans[i] is the
 *  answer to the ith query.
 *
 *  Example 1:
 *    Input: nums = [0,1,2,4]
 *    Output: [4,2,1,0]
 *
 *  Example 2:
 *    Input: nums = [10,20,50,10]
 *    Output: [50,20,10,10]
 *
 *  Constraints:
 *    n == nums.length
 *    1 <= n <= 10^5
 *    0 <= nums[i] <= 10^9
 */
public class MaximumOfMinimumValuesInAllSubarrays {

    // V0
    // IDEA: MONOTONIC STACK (widest window where nums[i] is the minimum)
    //       + SUFFIX MAX
    //       For every index i, find the previous / next smaller element. In
    //       between, nums[i] IS the minimum, and that window has length
    //           w = right[i] - left[i] - 1
    //       so nums[i] is achievable as "min of a window of size w":
    //           best[w - 1] = max(best[w - 1], nums[i])
    //       Any window of size w also CONTAINS a window of every smaller size
    //       whose min is >= nums[i], so the answer array is non-increasing in
    //       the window size -> sweep right to left with a running max to fill
    //       the sizes nobody claimed.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[] findMaximums(int[] nums) {
        int n = nums.length;
        int[] left = new int[n];
        int[] right = new int[n];
        int[] stack = new int[n];
        int top = -1;

        for (int i = 0; i < n; i++) {
            while (top >= 0 && nums[stack[top]] >= nums[i]) {
                top--;
            }
            left[i] = (top >= 0) ? stack[top] : -1;
            stack[++top] = i;
        }

        top = -1;
        for (int i = n - 1; i >= 0; i--) {
            while (top >= 0 && nums[stack[top]] >= nums[i]) {
                top--;
            }
            right[i] = (top >= 0) ? stack[top] : n;
            stack[++top] = i;
        }

        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int w = right[i] - left[i] - 1;
            if (nums[i] > res[w - 1]) {
                res[w - 1] = nums[i];
            }
        }
        for (int i = n - 2; i >= 0; i--) {
            if (res[i + 1] > res[i]) {
                res[i] = res[i + 1];
            }
        }
        return res;
    }
}
