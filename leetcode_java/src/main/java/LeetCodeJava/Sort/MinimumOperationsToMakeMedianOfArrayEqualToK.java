package LeetCodeJava.Sort;

// https://leetcode.com/problems/minimum-operations-to-make-median-of-array-equal-to-k/

import java.util.Arrays;

/**
 *  3107. Minimum Operations to Make Median of Array Equal to K
 *  Medium
 *
 *  You are given an integer array nums and a non-negative integer k. In one
 *  operation, you can increase or decrease any element by 1.
 *
 *  Return the minimum number of operations needed to make the median of nums
 *  equal to k.
 *
 *  The median of an array is defined as the middle element of the array when it is
 *  sorted in non-decreasing order. If there are two choices for a median, the
 *  larger of the two values is taken.
 *
 *  Example 1:
 *    Input: nums = [2,5,6,8,5], k = 4
 *    Output: 2
 *    Explanation: subtract one from nums[1] and nums[4] -> [2,4,6,8,4], median 4.
 *
 *  Example 2:
 *    Input: nums = [2,5,6,8,5], k = 7
 *    Output: 3
 *
 *  Example 3:
 *    Input: nums = [1,2,3,4,5,6], k = 4
 *    Output: 0
 *
 *  Constraints:
 *    1 <= nums.length <= 2 * 10^5
 *    1 <= nums[i] <= 10^9
 *    1 <= k <= 10^9
 */
public class MinimumOperationsToMakeMedianOfArrayEqualToK {

    // V0
    // IDEA: SORT, THEN ONLY THE WRONG SIDE OF THE MEDIAN SLOT NEEDS FIXING
    //       after sorting, the median lives at index m = n / 2 (that index is the
    //       LARGER of the two middles when n is even, which is the rule here).
    //
    //       for the median to read k:
    //         nums[m] itself must become k        -> |nums[m] - k| moves
    //         everything left of m must be <= k   -> pull down any that exceed k
    //         everything right of m must be >= k  -> push up any that fall short
    //
    //       elements already on the correct side cost nothing, and moving them
    //       further would only waste operations.
    /**
     * time = O(N log N)
     * space = O(1)   // ignoring the sort
     */
    public long minOperationsToMakeMedianK(int[] nums, int k) {
        Arrays.sort(nums);
        int n = nums.length;
        int m = n / 2;

        long res = Math.abs((long) nums[m] - k);
        for (int i = 0; i < m; i++) {
            if (nums[i] > k) {
                res += (long) nums[i] - k;
            }
        }
        for (int i = m + 1; i < n; i++) {
            if (nums[i] < k) {
                res += (long) k - nums[i];
            }
        }
        return res;
    }
}
