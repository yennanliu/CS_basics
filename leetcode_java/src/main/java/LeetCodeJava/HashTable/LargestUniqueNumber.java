package LeetCodeJava.HashTable;

// https://leetcode.com/problems/largest-unique-number/description/

import java.util.*;

/**
 * 1133. Largest Unique Number
 * Easy
 *
 * Given an integer array nums, return the largest integer that only occurs once.
 * If no integer occurs once, return -1.
 *
 *
 * Example 1:
 *
 * Input: nums = [5,7,3,9,4,9,8,3,1]
 * Output: 8
 * Explanation: The maximum integer in the array is 9 but it is repeated.
 * The number 8 occurs only once, so it is the answer.
 *
 * Example 2:
 *
 * Input: nums = [9,9,8,8]
 * Output: -1
 * Explanation: There is no number that occurs only once.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2000
 * 0 <= nums[i] <= 1000
 *
 */
public class LargestUniqueNumber {

    // V0
    // IDEA: HASH TABLE (count every value, take the max among values seen once)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int largestUniqueNumber(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int num : nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }

        int largest = -1;
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            if (entry.getValue() == 1) {
                largest = Math.max(largest, entry.getKey());
            }
        }
        return largest;
    }
}
