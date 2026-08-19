package LeetCodeJava.Heap;

// https://leetcode.com/problems/minimum-operations-to-exceed-threshold-value-ii/

import java.util.PriorityQueue;

/**
 *  3066. Minimum Operations to Exceed Threshold Value II
 *  Medium
 *
 *  You are given a 0-indexed integer array nums, and an integer k.
 *
 *  In one operation, you will:
 *   - Take the two smallest integers x and y in nums.
 *   - Remove x and y from nums.
 *   - Add min(x, y) * 2 + max(x, y) anywhere in the array.
 *
 *  Note that you can only apply the operation if nums contains at least two
 *  elements.
 *
 *  Return the minimum number of operations needed so that all elements of the array
 *  are greater than or equal to k.
 *
 *  Example 1:
 *    Input: nums = [2,11,10,1,3], k = 10
 *    Output: 2
 *    Explanation: remove 1 and 2 -> add 1*2+2 = 4 -> [4,11,10,3];
 *                 remove 3 and 4 -> add 3*2+4 = 10 -> [10,11,10]. all >= 10.
 *
 *  Example 2:
 *    Input: nums = [1,1,2,4,9], k = 20
 *    Output: 4
 *
 *  Constraints:
 *    2 <= nums.length <= 2 * 10^5
 *    1 <= nums[i] <= 10^9
 *    1 <= k <= 10^9
 *    The input is generated such that an answer always exists.
 */
public class MinimumOperationsToExceedThresholdValueII {

    // V0
    // IDEA: MIN-HEAP — THE OPERATION IS FORCED, SO JUST SIMULATE IT
    //       there is no choice: the two smallest are always consumed, and the
    //       replacement 2*min + max is strictly larger than both, so the array's
    //       minimum only ever rises. the stopping test is therefore just
    //       "is the heap top >= k".
    //       NOTE: use long — 2 * 10^9 + 10^9 overflows int.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int minOperations(int[] nums, int k) {
        PriorityQueue<Long> pq = new PriorityQueue<>();
        for (int x : nums) {
            pq.add((long) x);
        }

        int res = 0;
        while (pq.size() >= 2 && pq.peek() < k) {
            long x = pq.poll(); // the smaller
            long y = pq.poll();
            pq.add(x * 2 + y);
            res++;
        }
        return res;
    }
}
