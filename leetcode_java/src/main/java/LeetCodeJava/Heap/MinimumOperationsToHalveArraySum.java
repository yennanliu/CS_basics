package LeetCodeJava.Heap;

// https://leetcode.com/problems/minimum-operations-to-halve-array-sum/

import java.util.Collections;
import java.util.PriorityQueue;

/**
 *  2208. Minimum Operations to Halve Array Sum
 *  Medium
 *
 *  You are given an array nums of positive integers. In one operation, you can
 *  choose any number from nums and reduce it to exactly half the number. (Note that
 *  you may choose this reduced number in future operations.)
 *
 *  Return the minimum number of operations to reduce the sum of nums by at least
 *  half.
 *
 *  Example 1:
 *    Input: nums = [5,19,8,1]
 *    Output: 3
 *    Explanation: sum = 33. 19 -> 9.5 -> 4.75, then 8 -> 4, leaving
 *                 [5, 4.75, 4, 1] with sum 14.75; 33 - 14.75 = 18.25 >= 33/2.
 *
 *  Example 2:
 *    Input: nums = [3,8,20]
 *    Output: 3
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    1 <= nums[i] <= 10^7
 */
public class MinimumOperationsToHalveArraySum {

    // V0
    // IDEA: ALWAYS HALVE THE CURRENT LARGEST — MAX-HEAP
    //       halving x removes exactly x/2 from the sum, so the biggest available
    //       number always yields the biggest saving; the choice only depends on the
    //       current multiset, so the greedy stays optimal step after step.
    //       NOTE: halves are kept as doubles (values <= 10^7, so no precision
    //             trouble within the ~O(n log n) steps needed).
    /**
     * time = O((n + ops) log n)
     * space = O(n)
     */
    public int halveArray(int[] nums) {
        double total = 0.0;
        PriorityQueue<Double> pq = new PriorityQueue<>(Collections.reverseOrder());
        for (int x : nums) {
            total += x;
            pq.add((double) x);
        }

        double target = total / 2;
        double removed = 0.0;
        int ops = 0;
        while (removed < target) {
            double x = pq.poll();
            double half = x / 2;
            removed += half;
            pq.add(half);
            ops++;
        }
        return ops;
    }
}
