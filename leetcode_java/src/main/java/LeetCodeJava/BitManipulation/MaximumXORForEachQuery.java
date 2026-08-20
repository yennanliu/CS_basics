package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/maximum-xor-for-each-query/

/**
 *  1829. Maximum XOR for Each Query
 *  Medium
 *
 *  You are given a sorted array nums of n non-negative integers and an integer
 *  maximumBit. You want to perform the following query n times:
 *
 *    1. Find a non-negative integer k < 2^maximumBit such that
 *       nums[0] XOR nums[1] XOR ... XOR nums[nums.length - 1] XOR k is maximized.
 *       k is the answer to the ith query.
 *    2. Remove the last element from the current array nums.
 *
 *  Return an array answer, where answer[i] is the answer to the ith query.
 *
 *  Example 1:
 *    Input: nums = [0,1,1,3], maximumBit = 2
 *    Output: [0,3,2,3]
 *
 *  Example 2:
 *    Input: nums = [2,3,4,7], maximumBit = 3
 *    Output: [5,2,6,5]
 *
 *  Constraints:
 *    nums.length == n
 *    1 <= n <= 10^5
 *    1 <= maximumBit <= 20
 *    0 <= nums[i] < 2^maximumBit
 *    nums is sorted in ascending order.
 */
public class MaximumXORForEachQuery {

    // V0
    // IDEA: RUNNING PREFIX XOR + COMPLEMENT UNDER THE MASK
    //
    //  let x be the xor of the current array. we want k < 2^maximumBit maximising
    //  x ^ k. every value fits in maximumBit bits, so the best achievable result is
    //  all-ones, i.e. mask = 2^maximumBit - 1, and the unique k reaching it is
    //     k = x ^ mask
    //  (flip exactly the bits of x that are 0 within the mask).
    //
    //  the queries strip the array from the right, so walk nums backwards and xor
    //  the removed element out of x each step — no recomputation.
    /**
     * time = O(n)
     * space = O(1) extra (O(n) output)
     */
    public int[] getMaximumXor(int[] nums, int maximumBit) {
        int n = nums.length;
        int mask = (1 << maximumBit) - 1;

        int x = 0;
        for (int v : nums) {
            x ^= v;
        }

        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = x ^ mask;
            x ^= nums[n - 1 - i];
        }
        return res;
    }
}
