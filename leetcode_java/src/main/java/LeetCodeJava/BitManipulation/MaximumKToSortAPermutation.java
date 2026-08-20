package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/maximum-k-to-sort-a-permutation/

/**
 *  3644. Maximum K to Sort a Permutation
 *  Medium
 *
 *  You are given an integer array nums of length n, where nums is a permutation
 *  of the numbers in the range [0..n - 1].
 *
 *  You may swap elements at indices i and j only if nums[i] AND nums[j] == k,
 *  where AND denotes the bitwise AND operation and k is a non-negative integer.
 *
 *  Return the maximum value of k such that the array can be sorted in
 *  non-decreasing order using any number of such swaps. If nums is already
 *  sorted, return 0.
 *
 *  Example 1:
 *    Input: nums = [0,3,2,1]
 *    Output: 1
 *    Explanation: with k = 1, swapping nums[1] = 3 and nums[3] = 1 is allowed
 *                 (3 AND 1 == 1) and yields [0,1,2,3].
 *
 *  Example 2:
 *    Input: nums = [0,1,3,2]
 *    Output: 2
 *    Explanation: with k = 2, swapping nums[2] = 3 and nums[3] = 2 is allowed
 *                 (3 AND 2 == 2) and yields [0,1,2,3].
 *
 *  Example 3:
 *    Input: nums = [3,2,1,0]
 *    Output: 0
 *
 *  Constraints:
 *    1 <= n == nums.length <= 10^5
 *    0 <= nums[i] <= n - 1
 *    nums is a permutation of integers from 0 to n - 1.
 */
public class MaximumKToSortAPermutation {

    // V0
    // IDEA: k MUST BE A SUBMASK OF EVERY MISPLACED VALUE -> TAKE THEIR AND
    //       necessity: a value sitting on the wrong index must be swapped at least
    //       once, and every swap involving v needs (v AND w) == k, which forces k
    //       to be a submask of v. so k is a submask of every misplaced value, i.e.
    //       k <= AND of them all.
    //       sufficiency: let k be exactly that AND. since 0 <= k < n the VALUE k is
    //       itself somewhere in the permutation, and for every misplaced v we have
    //       (k AND v) == k, so the element holding k is a universal hub that can be
    //       swapped with anything that must move; any permutation of the misplaced
    //       elements is realisable as a sequence of hub swaps.
    //       an already sorted array needs no swap, and the problem pins that to 0.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int sortPermutation(int[] nums) {
        int res = -1;                      // all ones = identity of AND
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != i) {
                res &= nums[i];
            }
        }
        return (res == -1) ? 0 : res;
    }
}
