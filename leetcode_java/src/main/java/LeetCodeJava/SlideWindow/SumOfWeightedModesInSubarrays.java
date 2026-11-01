package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/sum-of-weighted-modes-in-subarrays/description/

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * 3672. Sum of Weighted Modes in Subarrays
 * Medium
 *
 * You are given an integer array nums and an integer k.
 *
 * For every subarray of length k:
 *
 * The mode is defined as the element with the highest frequency. If there are multiple choices
 * for a mode, the smallest such element is taken.
 * The weight is defined as mode * frequency(mode).
 *
 * Return the sum of the weights of all subarrays of length k.
 *
 * Note:
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 * The frequency of an element x is the number of times it occurs in the array.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,2,3], k = 3
 * Output: 8
 *
 * Example 2:
 *
 * Input: nums = [1,2,1,2], k = 2
 * Output: 3
 *
 * Example 3:
 *
 * Input: nums = [4,3,4,3], k = 3
 * Output: 14
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^5
 * 1 <= k <= nums.length
 *
 */
public class SumOfWeightedModesInSubarrays {

    // V0
    // IDEA: FIXED-SIZE SLIDING WINDOW + ONE LAZY MIN-HEAP PER FREQUENCY
    //  buckets[c] holds the values whose count is c (smallest on top). A value is pushed into
    //  buckets[c] every time its count becomes c, so a value whose count is c right now always
    //  has a live entry there; stale entries (count no longer c) are popped when they surface.
    //  The top frequency moves by at most one per add / remove.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long modeWeight(int[] nums, int k) {
        int n = nums.length;
        // value -> occurrences in the window
        Map<Integer, Integer> count = new HashMap<>();
        // frequency -> min-heap of values that had that frequency (possibly stale)
        Map<Integer, PriorityQueue<Integer>> buckets = new HashMap<>();
        int topFrequency = 0;
        long total = 0;

        for (int i = 0; i < n; i++) {
            // nums[i] enters
            int entering = nums[i];
            int enteringCount = count.getOrDefault(entering, 0) + 1;
            count.put(entering, enteringCount);
            buckets.computeIfAbsent(enteringCount, key -> new PriorityQueue<>()).add(entering);
            topFrequency = Math.max(topFrequency, enteringCount);

            // nums[i - k] leaves
            if (i >= k) {
                int leaving = nums[i - k];
                int leavingCount = count.get(leaving) - 1;
                count.put(leaving, leavingCount);
                if (leavingCount > 0) {
                    buckets.computeIfAbsent(leavingCount, key -> new PriorityQueue<>()).add(leaving);
                }
                // the top bucket may have lost its only live value: step down if so
                PriorityQueue<Integer> top = buckets.get(topFrequency);
                discardStale(top, count, topFrequency);
                if (top.isEmpty()) {
                    topFrequency--;
                }
            }

            // a full window: the mode is the smallest live value in the top bucket
            if (i >= k - 1) {
                PriorityQueue<Integer> top = buckets.get(topFrequency);
                discardStale(top, count, topFrequency);
                total += (long) top.peek() * topFrequency;
            }
        }
        return total;
    }

    /** Pops heap entries whose value no longer has exactly `frequency` copies in the window. */
    private void discardStale(PriorityQueue<Integer> heap, Map<Integer, Integer> count, int frequency) {
        while (!heap.isEmpty() && count.getOrDefault(heap.peek(), 0) != frequency) {
            heap.poll();
        }
    }
}
