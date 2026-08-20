package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/partition-array-into-two-equal-product-subsets/

/**
 *  3566. Partition Array into Two Equal Product Subsets
 *  Medium
 *
 *  You are given an integer array nums containing distinct positive integers and
 *  an integer target.
 *
 *  Determine if you can partition nums into two non-empty disjoint subsets, with
 *  each element belonging to exactly one subset, such that the product of the
 *  elements in each subset is equal to target.
 *
 *  Return true if such a partition exists and false otherwise.
 *
 *  Example 1:
 *    Input: nums = [3,1,6,8,4], target = 24
 *    Output: true
 *    Explanation: the subsets [3,8] and [1,6,4] each have a product of 24.
 *
 *  Example 2:
 *    Input: nums = [2,5,3,7], target = 15
 *    Output: false
 *
 *  Constraints:
 *    3 <= nums.length <= 12
 *    1 <= target <= 10^15
 *    1 <= nums[i] <= 100
 *    All elements of nums are distinct.
 */
public class PartitionArrayIntoTwoEqualProductSubsets {

    // V0
    // IDEA: BITMASK ENUMERATION OVER EVERY SPLIT
    //       n <= 12, so all 2^n colourings fit. the two subsets must together use
    //       every element, so one mask fully describes the split and the complement
    //       is the other side.
    //       NOTE (Java vs Python): the Python reference pre-filters on
    //       "total product == target * target", but target <= 10^15 makes
    //       target*target ~ 10^30 and the whole product up to 100^12 ~ 10^24 —
    //       both overflow a 64-bit long. so instead check BOTH sides directly,
    //       with an early break once a running product passes target (it can then
    //       only reach target*100 <= 10^17, still safe in a long).
    /**
     * time = O(2^N * N)
     * space = O(1)
     */
    public boolean checkEqualPartitions(int[] nums, long target) {
        int n = nums.length;
        int full = (1 << n) - 1;

        for (int mask = 1; mask < full; mask++) {
            if (productOf(nums, mask, target) != target) {
                continue;
            }
            if (productOf(nums, full ^ mask, target) == target) {
                return true;
            }
        }
        return false;
    }

    /** product of the elements picked by mask, aborted (as -1) once it exceeds cap */
    private long productOf(int[] nums, int mask, long cap) {
        long p = 1L;
        for (int i = 0; i < nums.length; i++) {
            if (((mask >> i) & 1) == 1) {
                p *= nums[i];
                if (p > cap) {
                    return -1L;
                }
            }
        }
        return p;
    }
}
