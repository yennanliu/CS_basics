package LeetCodeJava.Math;

// https://leetcode.com/problems/global-and-local-inversions/

/**
 *  775. Global and Local Inversions
 *  Medium
 *
 *  You are given an integer array nums of length n which represents a
 *  permutation of all the integers in the range [0, n - 1].
 *
 *  The number of global inversions is the number of the different pairs (i, j)
 *  where: 0 <= i < j < n and nums[i] > nums[j].
 *
 *  The number of local inversions is the number of indices i where:
 *  0 <= i < n - 1 and nums[i] > nums[i + 1].
 *
 *  Return true if the number of global inversions is equal to the number of
 *  local inversions.
 *
 *  Example 1:
 *    Input: nums = [1,0,2]
 *    Output: true
 *
 *  Example 2:
 *    Input: nums = [1,2,0]
 *    Output: false
 *
 *  Constraints:
 *   - n == nums.length
 *   - 1 <= n <= 10^5
 *   - 0 <= nums[i] < n
 *   - All the integers of nums are unique, nums is a permutation of [0, n-1].
 */
public class GlobalAndLocalInversions {

    // V0
    // IDEA: every LOCAL inversion is also a GLOBAL one, so the counts are equal
    //       only if there is NO non-local global inversion, i.e. no pair (i, j)
    //       with j > i + 1 and nums[i] > nums[j].
    //       For a permutation of [0, n-1] that is equivalent to
    //       |nums[i] - i| <= 1 for every i.
    /**
     * time = O(n)
     * space = O(1)
     */
    public boolean isIdealPermutation(int[] nums) {

        for (int i = 0; i < nums.length; i++) {
            if (Math.abs(nums[i] - i) > 1) {
                return false;
            }
        }

        return true;
    }

    // V1
    // IDEA: keep the max of nums[0 .. i]; if it is bigger than nums[i + 2],
    //       there is a global inversion that is not local.
    /**
     * time = O(n)
     * space = O(1)
     */
    public boolean isIdealPermutation_1(int[] nums) {

        int curMax = Integer.MIN_VALUE;

        for (int i = 0; i + 2 < nums.length; i++) {
            curMax = Math.max(curMax, nums[i]);
            if (curMax > nums[i + 2]) {
                return false;
            }
        }

        return true;
    }
}
