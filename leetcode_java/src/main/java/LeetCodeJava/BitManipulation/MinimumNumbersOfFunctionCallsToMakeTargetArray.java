package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/minimum-numbers-of-function-calls-to-make-target-array/

/**
 *  1558. Minimum Numbers of Function Calls to Make Target Array
 *  Medium
 *
 *  You are given an integer array nums. You have an integer array arr of the same
 *  length with all values set to 0 initially. You also have a modify function that
 *  can either
 *    - increment arr[i] by 1 for a single chosen index i, or
 *    - double every element of arr.
 *
 *  You want to use the modify function to convert arr to nums using the minimum
 *  number of calls. Return that minimum number of function calls.
 *
 *  The test cases are generated so that the answer fits in a 32-bit signed integer.
 *
 *  Example 1:
 *    Input: nums = [1,5]
 *    Output: 5
 *    Explanation: [0,0] -> [0,1] -> [0,2] -> [0,4] -> [1,4] -> [1,5]:
 *                 1 increment + 2 doublings + 2 increments = 5 calls.
 *
 *  Example 2:
 *    Input: nums = [2,2]
 *    Output: 3
 *
 *  Example 3:
 *    Input: nums = [4,2,5]
 *    Output: 6
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    0 <= nums[i] <= 10^9
 */
public class MinimumNumbersOfFunctionCallsToMakeTargetArray {

    // V0
    // IDEA: BIT MANIPULATION (run the process backwards: halve everything, drop odd LSBs)
    //
    //  in reverse, an "+1 on one element" undoes a set LSB and a "double everything"
    //  undoes one right shift of the WHOLE array. so:
    //    - every set bit of every number costs exactly one +1 call
    //    - the doublings needed = (widest bit length) - 1, and they are SHARED by all
    //  the total is therefore popcount-sum + (widest - 1).
    //
    //  NOTE: an all-zero array needs 0 calls, so the "- 1" must be skipped there.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int minOperations(int[] nums) {
        int adds = 0;
        int widest = 0;
        for (int x : nums) {
            adds += Integer.bitCount(x);
            int bits = 32 - Integer.numberOfLeadingZeros(x);   // 0 when x == 0
            widest = Math.max(widest, bits);
        }
        return adds + (widest > 0 ? widest - 1 : 0);
    }
}
