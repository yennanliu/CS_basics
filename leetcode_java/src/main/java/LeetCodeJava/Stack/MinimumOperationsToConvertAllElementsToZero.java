package LeetCodeJava.Stack;

// https://leetcode.com/problems/minimum-operations-to-convert-all-elements-to-zero/

/**
 *  3542. Minimum Operations to Convert All Elements to Zero
 *  Medium
 *
 *  You are given an array nums of size n, consisting of non-negative integers.
 *  Your task is to apply some (possibly zero) operations on the array so that
 *  all elements become 0.
 *
 *  In one operation, you can select a subarray [i, j] (where 0 <= i <= j < n)
 *  and set all occurrences of the minimum non-negative integer in that
 *  subarray to 0.
 *
 *  Return the minimum number of operations required to make all elements in
 *  the array 0.
 *
 *  Example 1:
 *    Input: nums = [0,2]
 *    Output: 1
 *
 *  Example 2:
 *    Input: nums = [3,1,2,1]
 *    Output: 3
 *    Explanation: zero the two 1s together, then the 2, then the 3.
 *
 *  Example 3:
 *    Input: nums = [1,2,1,2,1,2]
 *    Output: 4
 *
 *  Constraints:
 *    1 <= n == nums.length <= 10^5
 *    0 <= nums[i] <= 10^5
 */
public class MinimumOperationsToConvertAllElementsToZero {

    // V0
    // IDEA: MONOTONIC STACK OVER "NESTED" VALUE LEVELS
    //       An operation zeroes one value inside one contiguous window, so two
    //       equal values can share an operation only when nothing strictly
    //       smaller separates them (a smaller element inside the window would
    //       be the minimum instead).
    //       That is exactly what a non-decreasing stack tracks: push values as
    //       they come; when a smaller value arrives, every strictly larger
    //       value on the stack is closed off forever (it can never merge with
    //       anything to the right) so it costs one operation. Equal values
    //       sitting on top of the stack merge for free. Whatever remains on
    //       the stack at the end costs one operation each. Zeroes are never
    //       pushed.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minOperations(int[] nums) {
        int n = nums.length;
        int[] stack = new int[n];
        int top = -1;
        int ops = 0;

        for (int v : nums) {
            while (top >= 0 && stack[top] > v) {
                top--;
                ops++;
            }
            if (v > 0 && (top < 0 || stack[top] != v)) {
                stack[++top] = v;
            }
        }
        return ops + (top + 1);
    }
}
