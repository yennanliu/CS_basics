package LeetCodeJava.Stack;

// https://leetcode.com/problems/maximal-range-that-each-element-is-maximum-in-it/

/**
 *  2832. Maximal Range That Each Element Is Maximum in It
 *  Medium
 *
 *  You are given a 0-indexed array nums of distinct integers.
 *
 *  Let us define a 0-indexed array ans of the same length as nums in the
 *  following way: ans[i] is the maximum length of a subarray nums[l..r], such
 *  that the maximum element in that subarray is equal to nums[i].
 *
 *  Return the array ans.
 *
 *  Example 1:
 *    Input: nums = [1,5,4,3,6]
 *    Output: [1,4,2,1,5]
 *
 *  Example 2:
 *    Input: nums = [1,2,3,4,5]
 *    Output: [1,2,3,4,5]
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    1 <= nums[i] <= 10^5
 *    All elements in nums are distinct.
 */
public class MaximalRangeThatEachElementIsMaximumInIt {

    // V0
    // IDEA: MONOTONIC STACK (previous greater / next greater element)
    //       The widest window in which nums[i] is the maximum stretches from
    //       just after the previous strictly-greater element to just before
    //       the next strictly-greater one:
    //           ans[i] = right[i] - left[i] - 1
    //       The window may freely contain SMALLER elements - it is blocked
    //       only by greater ones. Values are distinct, so "greater" and
    //       "greater or equal" coincide (no tie-breaking subtlety).
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[] maximumLengthOfRanges(int[] nums) {
        int n = nums.length;
        int[] left = new int[n];  // index of previous greater element
        int[] right = new int[n]; // index of next greater element
        int[] stack = new int[n];
        int top = -1;

        for (int i = 0; i < n; i++) {
            while (top >= 0 && nums[stack[top]] < nums[i]) {
                top--;
            }
            left[i] = (top >= 0) ? stack[top] : -1;
            stack[++top] = i;
        }

        top = -1;
        for (int i = n - 1; i >= 0; i--) {
            while (top >= 0 && nums[stack[top]] < nums[i]) {
                top--;
            }
            right[i] = (top >= 0) ? stack[top] : n;
            stack[++top] = i;
        }

        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = right[i] - left[i] - 1;
        }
        return res;
    }
}
