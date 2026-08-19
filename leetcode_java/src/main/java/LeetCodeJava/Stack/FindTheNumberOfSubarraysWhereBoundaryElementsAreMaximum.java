package LeetCodeJava.Stack;

// https://leetcode.com/problems/find-the-number-of-subarrays-where-boundary-elements-are-maximum/

/**
 *  3113. Find the Number of Subarrays Where Boundary Elements Are Maximum
 *  Hard
 *
 *  You are given an array of positive integers nums.
 *
 *  Return the number of subarrays of nums, where the first and the last
 *  elements of the subarray are equal to the largest element in the subarray.
 *
 *  Example 1:
 *    Input: nums = [1,4,3,3,2]
 *    Output: 6
 *    Explanation: the 5 length-1 subarrays, plus [3,3].
 *
 *  Example 2:
 *    Input: nums = [3,3,3]
 *    Output: 6
 *
 *  Example 3:
 *    Input: nums = [1]
 *    Output: 1
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    1 <= nums[i] <= 10^9
 */
public class FindTheNumberOfSubarraysWhereBoundaryElementsAreMaximum {

    // V0
    // IDEA: MONOTONIC STACK OF (VALUE, HOW MANY COPIES ARE STILL "VISIBLE")
    //       A valid subarray is a pair of equal values i < j such that nothing
    //       strictly larger sits between them -> for each element, count how
    //       many earlier equal values are still unblocked.
    //       A non-increasing stack keeps exactly those unblocked candidates:
    //       when a new value arrives, every smaller value on the stack is now
    //       shadowed and pops off. What remains on top is either the same
    //       value - whose stored count says how many copies are visible, all
    //       of which pair with the new element - or a larger one.
    //       So each element contributes (visible copies) + 1, the +1 being the
    //       length-1 subarray.
    /**
     * time = O(N)
     * space = O(N)
     */
    public long numberOfSubarrays(int[] nums) {
        int n = nums.length;
        int[] val = new int[n];   // stack of values (non-increasing)
        int[] cnt = new int[n];   // how many visible copies of that value
        int top = -1;
        long res = 0;

        for (int x : nums) {
            while (top >= 0 && val[top] < x) {
                top--;
            }
            if (top >= 0 && val[top] == x) {
                cnt[top]++;
                res += cnt[top]; // pairs with each visible copy, plus itself
            } else {
                top++;
                val[top] = x;
                cnt[top] = 1;
                res += 1;
            }
        }
        return res;
    }
}
