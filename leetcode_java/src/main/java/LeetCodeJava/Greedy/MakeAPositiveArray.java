package LeetCodeJava.Greedy;

// https://leetcode.com/problems/make-a-positive-array/description/
// https://leetcode.ca/2025-05-04-3511-Make-a-Positive-Array/

import java.util.*;

/**
 * 3511. Make a Positive Array
 * Medium
 *
 * You are given an array nums. An array is considered positive if the sum of all
 * numbers in each subarray with more than two elements is positive.
 *
 * You can perform the following operation any number of times:
 *
 * Replace one element in nums with any integer between -10^18 and 10^18.
 *
 * Find the minimum number of operations needed to make nums positive.
 *
 * Example 1:
 *
 * Input: nums = [-10,15,-12]
 *
 * Output: 1
 *
 * Explanation:
 *
 * The only subarray with more than 2 elements is the array itself. The sum of all
 * elements is (-10) + 15 + (-12) = -7. By replacing nums[0] with 0, the new sum
 * becomes 0 + 15 + (-12) = 3. Thus, the array is now positive.
 *
 * Example 2:
 *
 * Input: nums = [-1,-2,3,-1,2,6]
 *
 * Output: 1
 *
 * Explanation:
 *
 * The only subarrays with more than 2 elements and a non-positive sum are:
 *
 * Subarray Indices | Subarray        | Sum | After Replacement | New Sum
 *                  |                 |     | (Set nums[1] = 1) |
 * -----------------+-----------------+-----+-------------------+--------
 * nums[0...2]      | [-1, -2, 3]     |   0 | [-1, 1, 3]        |       3
 * nums[0...3]      | [-1, -2, 3, -1] |  -1 | [-1, 1, 3, -1]    |       2
 * nums[1...3]      | [-2, 3, -1]     |   0 | [1, 3, -1]        |       3
 *
 * Thus, nums is positive after one operation.
 *
 * Example 3:
 *
 * Input: nums = [1,2,3]
 *
 * Output: 0
 *
 * Explanation:
 *
 * The array is already positive, so no operations are needed.
 *
 * Constraints:
 *
 * 3 <= nums.length <= 10^5
 *
 * -10^9 <= nums[i] <= 10^9
 *
 */
public class MakeAPositiveArray {

    // V0
    // IDEA: INTERVAL STABBING GREEDY + MONOTONIC DEQUE OVER PREFIX SUMS
    //       a replaced element can be 10^18, which makes every subarray containing it
    //       positive, so the task is: fewest indices hitting every "bad" subarray
    //       (length >= 3, sum <= 0). classic stabbing greedy: sweep the right end r and
    //       place a marker at r whenever a bad interval with no marker ends there.
    //       with prefix sums P, [l, r] is bad iff P[l] >= P[r + 1]; only l in
    //       (lastMarker, r - 2] matters, so keep a deque of those l with decreasing P[l]
    //       — its front is the window maximum.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int makeArrayPositive(int[] nums) {
        int n = nums.length;
        long[] prefixSum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }

        Deque<Integer> deque = new ArrayDeque<>(); // candidate starts l, prefixSum[l] strictly decreasing
        int lastMarker = -1;                       // index of the most recent replacement
        int operations = 0;

        for (int r = 0; r < n; r++) {
            int newStart = r - 2;                  // a subarray ending at r needs l <= r - 2
            if (newStart >= 0) {
                while (!deque.isEmpty() && prefixSum[deque.peekLast()] <= prefixSum[newStart]) {
                    deque.pollLast();
                }
                deque.offerLast(newStart);
            }
            // starts at or before the last marker are already stabbed
            while (!deque.isEmpty() && deque.peekFirst() <= lastMarker) {
                deque.pollFirst();
            }
            // some unstabbed subarray [l, r] has sum <= 0 -> replace nums[r]
            if (!deque.isEmpty() && prefixSum[deque.peekFirst()] >= prefixSum[r + 1]) {
                operations++;
                lastMarker = r;
                deque.clear();
            }
        }
        return operations;
    }
}
