package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximize-sum-of-array-after-k-negations/description/

import java.util.*;

/**
 * 1005. Maximize Sum Of Array After K Negations
 * Easy
 *
 * Given an integer array nums and an integer k, modify the array in the following way:
 *
 * choose an index i and replace nums[i] with -nums[i].
 *
 * You should apply this process exactly k times. You may choose the same index i multiple times.
 *
 * Return the largest possible sum of the array after modifying it in this way.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,2,3], k = 1
 * Output: 5
 * Explanation: Choose index 1 and nums becomes [4,-2,3].
 *
 * Example 2:
 *
 * Input: nums = [3,-1,0,2], k = 3
 * Output: 6
 * Explanation: Choose indices (1, 2, 2) and nums becomes [3,1,0,2].
 *
 * Example 3:
 *
 * Input: nums = [2,-3,-1,5,-4], k = 2
 * Output: 13
 * Explanation: Choose indices (1, 4) and nums becomes [2,3,-1,5,4].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^4
 * -100 <= nums[i] <= 100
 * 1 <= k <= 10^4
 *
 */
public class MaximizeSumOfArrayAfterKNegations {

    // V0
    // IDEA: GREEDY + SORT (flip the most negative values first; leftover odd k pays 2 * min abs once)
    /**
     * time = O(N log N)
     * space = O(1)
     */
    public int largestSumAfterKNegations(int[] nums, int k) {
        // sort ascending so the most negative values come first
        Arrays.sort(nums);

        // each flip of a negative value gains 2 * abs(value): spend flips on those first
        int i = 0;
        while (i < nums.length && k > 0 && nums[i] < 0) {
            nums[i] = -nums[i];
            k--;
            i++;
        }

        // sum everything, and remember the smallest absolute value
        int total = 0;
        int minValue = Integer.MAX_VALUE;
        for (int num : nums) {
            total += num;
            minValue = Math.min(minValue, num);
        }

        // leftover flips go back and forth on the smallest value:
        // an even count changes nothing, an odd count costs 2 * min once
        if (k % 2 == 1) {
            total -= 2 * minValue;
        }
        return total;
    }

    // V1
    // IDEA: MIN HEAP (always flip the current smallest value, k times)
    /**
     * time = O(N + K log N)
     * space = O(N)
     */
    public int largestSumAfterKNegations_1(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : nums) {
            minHeap.add(num);
        }

        // flipping the smallest value is always the best single move
        for (int step = 0; step < k; step++) {
            int smallest = minHeap.poll();
            minHeap.add(-smallest);
        }

        int total = 0;
        while (!minHeap.isEmpty()) {
            total += minHeap.poll();
        }
        return total;
    }
}
