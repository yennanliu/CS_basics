package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-array-length-after-pair-removals/description/

import java.util.*;

/**
 * 2856. Minimum Array Length After Pair Removals
 * Medium
 *
 * Given an integer array num sorted in non-decreasing order.
 *
 * You can perform the following operation any number of times:
 *
 * Choose two indices, i and j, where nums[i] < nums[j].
 * Then, remove the elements at indices i and j from nums. The remaining elements retain their original order, and the array is re-indexed.
 *
 * Return the minimum length of nums after applying the operation zero or more times.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4]
 * Output: 0
 *
 * Example 2:
 *
 * Input: nums = [1,1,2,2,3,3]
 * Output: 0
 *
 * Example 3:
 *
 * Input: nums = [1000000000,1000000000]
 * Output: 2
 * Explanation: Since both numbers are equal, they cannot be removed.
 *
 * Example 4:
 *
 * Input: nums = [2,3,4,4,4]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 * nums is sorted in non-decreasing order.
 *
 */
public class MinimumArrayLengthAfterPairRemovals {

    // V0
    // IDEA: MAJORITY-ELEMENT COUNTING (greedy pairing argument)
    //       only equal values block a removal. let maxRun = the size of the largest group of
    //       equal values. if maxRun * 2 <= n, elements of different groups can always be
    //       paired off -> n % 2 remains. otherwise the dominant group outnumbers all others
    //       combined and 2 * maxRun - n of it must remain.
    //       nums is sorted, so equal values are contiguous: one scan finds maxRun.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minLengthAfterRemovals(List<Integer> nums) {
        int n = nums.size();
        int maxRun = 0;
        int run = 0;
        for (int i = 0; i < n; i++) {
            // boxed values: compare with equals, never ==
            if (i > 0 && nums.get(i).equals(nums.get(i - 1))) {
                run++;
            } else {
                run = 1;
            }
            maxRun = Math.max(maxRun, run);
        }

        if (maxRun * 2 > n) {
            return 2 * maxRun - n;
        }
        return n % 2;
    }
}
