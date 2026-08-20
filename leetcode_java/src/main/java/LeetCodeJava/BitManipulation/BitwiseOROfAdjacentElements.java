package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/bitwise-or-of-adjacent-elements/

/**
 *  3173. Bitwise OR of Adjacent Elements
 *  Easy
 *  (premium)
 *
 *  Given an array nums of length n, return an array answer of length n - 1
 *  such that answer[i] = nums[i] | nums[i + 1] where | is the bitwise OR
 *  operation.
 *
 *  Example 1:
 *    Input: nums = [1,3,7,15]
 *    Output: [3,7,15]
 *
 *  Example 2:
 *    Input: nums = [8,4,2]
 *    Output: [12,6]
 *
 *  Example 3:
 *    Input: nums = [5,4,9,11]
 *    Output: [5,13,11]
 *
 *  Constraints:
 *    2 <= nums.length <= 100
 *    0 <= nums[i] <= 100
 */
public class BitwiseOROfAdjacentElements {

    // V0
    // IDEA: DIRECT SCAN OVER THE ADJACENT PAIRS
    //       there are exactly n-1 adjacent pairs, so one pass filling
    //       res[i] = nums[i] | nums[i+1] is all that is needed.
    /**
     * time = O(N)
     * space = O(N)   // the output array
     */
    public int[] orArray(int[] nums) {
        int n = nums.length;
        int[] res = new int[n - 1];
        for (int i = 0; i + 1 < n; i++) {
            res[i] = nums[i] | nums[i + 1];
        }
        return res;
    }
}
