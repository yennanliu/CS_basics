package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/continuous-subarrays/description/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.TreeMap;

/**
 * 2762. Continuous Subarrays
 * Medium
 *
 * You are given a 0-indexed integer array nums. A subarray of nums is called continuous if:
 *
 * Let i, i + 1, ..., j be the indices in the subarray. Then, for each pair of indices
 * i <= i1, i2 <= j, 0 <= |nums[i1] - nums[i2]| <= 2.
 *
 * Return the total number of continuous subarrays.
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [5,4,2,4]
 * Output: 8
 * Explanation:
 * Continuous subarray of size 1: [5], [4], [2], [4].
 * Continuous subarray of size 2: [5,4], [4,2], [2,4].
 * Continuous subarray of size 3: [4,2,4].
 * There are no subarrys of size 4.
 * Total continuous subarrays = 4 + 3 + 1 = 8.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3]
 * Output: 6
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 *
 */
public class ContinuousSubarrays {

    // V0
    // IDEA: SLIDING WINDOW + TWO MONOTONIC DEQUES (WINDOW MAX AND WINDOW MIN)
    //  "every pair within 2" is max(window) - min(window) <= 2, which stays true when the
    //  window shrinks, so each right end r has a smallest valid left end that never moves
    //  back; r contributes r - left + 1 subarrays.
    /**
     * time = O(N)
     * space = O(N)
     */
    public long continuousSubarrays(int[] nums) {
        // indices; values decreasing front to back -> front is the window max
        Deque<Integer> maxQueue = new ArrayDeque<>();
        // indices; values increasing front to back -> front is the window min
        Deque<Integer> minQueue = new ArrayDeque<>();

        long count = 0;
        int left = 0;
        for (int right = 0; right < nums.length; right++) {
            int value = nums[right];
            while (!maxQueue.isEmpty() && nums[maxQueue.peekLast()] <= value) {
                maxQueue.pollLast();
            }
            maxQueue.addLast(right);
            while (!minQueue.isEmpty() && nums[minQueue.peekLast()] >= value) {
                minQueue.pollLast();
            }
            minQueue.addLast(right);

            // spread too wide: drop whichever extreme sits further left, and move left past it
            while (nums[maxQueue.peekFirst()] - nums[minQueue.peekFirst()] > 2) {
                if (maxQueue.peekFirst() < minQueue.peekFirst()) {
                    left = maxQueue.pollFirst() + 1;
                } else {
                    left = minQueue.pollFirst() + 1;
                }
            }
            count += right - left + 1;
        }
        return count;
    }

    // V1
    // IDEA: SLIDING WINDOW + TREEMAP AS AN ORDERED MULTISET (simpler to say, extra log N)
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long continuousSubarrays_1(int[] nums) {
        // value -> how many times it occurs in the window
        TreeMap<Integer, Integer> window = new TreeMap<>();
        long count = 0;
        int left = 0;
        for (int right = 0; right < nums.length; right++) {
            window.put(nums[right], window.getOrDefault(nums[right], 0) + 1);
            // shrink until max - min <= 2
            while (window.lastKey() - window.firstKey() > 2) {
                int leaving = nums[left];
                int remaining = window.get(leaving) - 1;
                if (remaining == 0) {
                    window.remove(leaving);
                } else {
                    window.put(leaving, remaining);
                }
                left++;
            }
            count += right - left + 1;
        }
        return count;
    }
}
