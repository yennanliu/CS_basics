package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/find-xor-beauty-of-array/

/**
 *  2527. Find Xor-Beauty of Array
 *  Medium
 *
 *  You are given a 0-indexed integer array nums.
 *
 *  The effective value of three indices i, j, and k is defined as
 *  ((nums[i] | nums[j]) & nums[k]).
 *
 *  The xor-beauty of the array is the XORing of the effective values of all the
 *  possible triplets of indices (i, j, k) where 0 <= i, j, k < n.
 *
 *  Return the xor-beauty of nums.
 *
 *  Example 1:
 *    Input: nums = [1,4]
 *    Output: 5
 *    Explanation: the 8 triplets give 1, 0, 1, 4, 1, 4, 0, 4 and their XOR is 5.
 *
 *  Example 2:
 *    Input: nums = [15,45,20,2,34,35,5,44,32,30]
 *    Output: 34
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    1 <= nums[i] <= 10^9
 */
public class FindXorBeautyOfArray {

    // V0
    // IDEA: CANCELLATION -> THE ANSWER IS JUST THE XOR OF ALL ELEMENTS
    //       the n^3 triplets cancel in pairs because XOR kills duplicates:
    //       1) if i != j, triplets (i,j,k) and (j,i,k) give the SAME effective
    //          value (OR is commutative), so every such pair XORs to 0. only
    //          i == j survives, leaving terms (nums[i] & nums[k]).
    //       2) among those, if i != k then (i,k) and (k,i) both give
    //          nums[i] & nums[k] (AND is commutative), so they cancel too.
    //       3) only i == j == k remains, contributing
    //          (nums[i] | nums[i]) & nums[i] = nums[i].
    //       NOTE: the cancellation relies on the triplets ranging over ALL
    //             ordered (i, j, k) INCLUDING repeats.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int xorBeauty(int[] nums) {
        int res = 0;
        for (int x : nums) {
            res ^= x;
        }
        return res;
    }
}
