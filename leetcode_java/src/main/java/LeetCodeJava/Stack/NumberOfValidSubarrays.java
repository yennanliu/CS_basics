package LeetCodeJava.Stack;

// https://leetcode.com/problems/number-of-valid-subarrays/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  1063. Number of Valid Subarrays
 *  Hard
 *
 *  Given an integer array nums, return the number of non-empty subarrays with
 *  the leftmost element of the subarray not larger than other elements in the
 *  subarray.
 *
 *  A subarray is a contiguous part of an array.
 *
 *  Example 1:
 *    Input: nums = [1,4,2,5,3]
 *    Output: 11
 *    Explanation: the 11 valid subarrays are [1],[4],[2],[5],[3],[1,4],[2,5],
 *                 [1,4,2],[2,5,3],[1,4,2,5],[1,4,2,5,3]
 *
 *  Example 2:
 *    Input: nums = [3,2,1]
 *    Output: 3
 *
 *  Constraints:
 *    1 <= nums.length <= 5 * 10^4
 *    0 <= nums[i] <= 10^5
 */
public class NumberOfValidSubarrays {

    // V0
    // IDEA: MONOTONIC STACK (next strictly smaller element to the right)
    //       a subarray starting at i is valid <-> every element in it is >= nums[i]
    //       -> it may extend up to (but not include) the FIRST j > i with
    //          nums[j] < nums[i]
    //       -> # of valid subarrays starting at i = right[i] - i, where right[i]
    //          is the index of that first smaller element (n if none)
    //       scanning from the right with a stack of indices of increasing values
    //       gives every right[i] in one pass.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int validSubarrays(int[] nums) {
        int n = nums.length;
        Deque<Integer> stack = new ArrayDeque<>(); // indices, values increasing bottom->? (strictly increasing from top down)
        long ans = 0;
        for (int i = n - 1; i >= 0; i--) {
            // NOTE: pop with `>=` so equal values are NOT treated as a blocker
            while (!stack.isEmpty() && nums[stack.peek()] >= nums[i]) {
                stack.pop();
            }
            int right = stack.isEmpty() ? n : stack.peek();
            ans += (right - i);
            stack.push(i);
        }
        return (int) ans;
    }
}
