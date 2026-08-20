package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/maximum-xor-after-operations/

/**
 *  2317. Maximum XOR After Operations
 *  Medium
 *
 *  You are given a 0-indexed integer array nums. In one operation, select any
 *  non-negative integer x and an index i, then update nums[i] to be equal to
 *  nums[i] AND (nums[i] XOR x).
 *
 *  Return the maximum possible bitwise XOR of all elements of nums after applying
 *  the operation any number of times.
 *
 *  Example 1:
 *    Input: nums = [3,2,4,6]
 *    Output: 7
 *    Explanation: with x = 4 and i = 3, nums[3] = 6 AND (6 XOR 4) = 2, so
 *                 nums = [3,2,4,2] and 3 ^ 2 ^ 4 ^ 2 = 7.
 *
 *  Example 2:
 *    Input: nums = [1,2,3,9,2]
 *    Output: 11
 *    Explanation: apply the operation zero times; 1 ^ 2 ^ 3 ^ 9 ^ 2 = 11.
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    0 <= nums[i] <= 10^8
 */
public class MaximumXORAfterOperations {

    // V0
    // IDEA: THE OPERATION CAN ONLY CLEAR BITS -> THE ANSWER IS THE OR OF ALL ELEMENTS
    //
    //  nums[i] AND (nums[i] XOR x) keeps a bit of nums[i] only when the matching bit
    //  of x is 0; it can never SET a bit that was 0. so each element may have any
    //  subset of its own set bits turned off, freely and independently.
    //
    //  therefore bit b of the final XOR can be made 1 iff at least one element has
    //  bit b set (keep it in exactly one element, clear it in the rest), and each
    //  bit is decided independently -> the maximum is simply the OR of all elements.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int maximumXOR(int[] nums) {
        int res = 0;
        for (int v : nums) {
            res |= v;
        }
        return res;
    }
}
