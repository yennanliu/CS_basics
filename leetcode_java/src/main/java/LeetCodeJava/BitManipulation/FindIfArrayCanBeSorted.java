package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/find-if-array-can-be-sorted/

/**
 *  3011. Find if Array Can Be Sorted
 *  Medium
 *
 *  You are given a 0-indexed array of positive integers nums.
 *
 *  In one operation, you can swap any two adjacent elements if they have the
 *  same number of set bits. You are allowed to do this operation any number of
 *  times (including zero).
 *
 *  Return true if you can sort the array, else return false.
 *
 *  Example 1:
 *    Input: nums = [8,4,2,30,15]
 *    Output: true
 *    Explanation: 2, 4, 8 all have one set bit and 15, 30 have four set bits,
 *                 so [8,4,2] can be sorted in place and so can [30,15], giving
 *                 [2,4,8,15,30].
 *
 *  Example 2:
 *    Input: nums = [1,2,3,4,5]
 *    Output: true
 *    Explanation: the array is already sorted.
 *
 *  Example 3:
 *    Input: nums = [3,16,8,4,2]
 *    Output: false
 *
 *  Constraints:
 *    1 <= nums.length <= 100
 *    1 <= nums[i] <= 2^8
 */
public class FindIfArrayCanBeSorted {

    // V0
    // IDEA: EQUAL-POPCOUNT RUNS SHUFFLE FREELY BUT NEVER CROSS EACH OTHER
    //       a swap needs both neighbours to share a popcount, so an element can
    //       move only inside a maximal RUN of consecutive equal-popcount values.
    //       inside a run adjacent swaps generate every permutation - that run can
    //       be fully sorted.
    //       nothing crosses a run boundary, so the runs stay in place, and the
    //       array is sortable iff each run's MAX <= the next run's MIN.
    //       one pass tracking the current run's min/max and the previous run's
    //       max settles it.
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean canSortArray(int[] nums) {
        int n = nums.length;
        int prevMax = 0;
        int i = 0;
        while (i < n) {
            int bits = Integer.bitCount(nums[i]);
            int curMin = nums[i];
            int curMax = nums[i];
            while (i + 1 < n && Integer.bitCount(nums[i + 1]) == bits) {
                i++;
                curMin = Math.min(curMin, nums[i]);
                curMax = Math.max(curMax, nums[i]);
            }
            if (curMin < prevMax) {
                return false;
            }
            prevMax = curMax;
            i++;
        }
        return true;
    }
}
