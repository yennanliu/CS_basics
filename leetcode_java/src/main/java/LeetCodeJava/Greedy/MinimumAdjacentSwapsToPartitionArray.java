package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-adjacent-swaps-to-partition-array/

import java.util.*;

/**
 *  3994. Minimum Adjacent Swaps to Partition Array
 *  Medium
 *
 *  NOTE: the batch file titles this entry "Transform Binary String Using Subsequence Sort",
 *  but the given url / python ref are LC 3994 (Minimum Adjacent Swaps to Partition Array),
 *  so the LC 3994 problem is implemented here (class name kept as requested).
 *
 *  You are given an integer array nums and two integers a and b such that a < b.
 *
 *  An array is called good if it can be split into three contiguous parts, in
 *  this order, such that:
 *   - Every element in the first part is less than a.
 *   - Every element in the second part is in the range [a, b] inclusive.
 *   - Every element in the third part is greater than b.
 *  Any of the three parts may be empty.
 *
 *  In one adjacent swap, you may swap two neighboring elements of nums.
 *  Return the minimum number of adjacent swaps required to make nums good,
 *  modulo 10^9 + 7.
 *
 *  Example 1:
 *  Input: nums = [1,3,2,4,5,6], a = 3, b = 4
 *  Output: 1
 *
 *  Example 2:
 *  Input: nums = [9,7,5,3], a = 4, b = 8
 *  Output: 5
 *
 *  Example 3:
 *  Input: nums = [3,7,5,9], a = 4, b = 8
 *  Output: 0
 *
 *  Constraints:
 *   - 1 <= nums.length <= 10^5
 *   - 1 <= nums[i] <= 10^9
 *   - 1 <= a < b <= 10^9
 */
public class MinimumAdjacentSwapsToPartitionArray {

    // V0
    // IDEA: GREEDY / INVERSION COUNTING
    //       label each element by its target group (0 : < a, 1 : [a,b], 2 : > b).
    //       the min number of adjacent swaps to sort the label sequence
    //       == number of inversions of that label sequence.
    //       -> a group-0 element must cross every group-1 / group-2 seen before it,
    //          a group-1 element must cross every group-2 seen before it.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int minAdjacentSwaps(int[] nums, int a, int b) {

        final long MOD = 1_000_000_007L;

        long cnt1 = 0; // number of "[a, b]" elements seen so far
        long cnt2 = 0; // number of "> b"    elements seen so far
        long res = 0;

        for (int val : nums) {
            if (val < a) {
                // must move in front of every group-1 and group-2 element seen so far
                res += cnt1 + cnt2;
            } else if (val <= b) {
                // must move in front of every group-2 element seen so far
                res += cnt2;
                cnt1++;
            } else {
                cnt2++;
            }
            res %= MOD;
        }

        return (int) (res % MOD);
    }
}
