package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-elements-with-maximum-frequency/description/

import java.util.*;

/**
 * 3005. Count Elements With Maximum Frequency
 * Easy
 *
 * You are given an array nums consisting of positive integers.
 *
 * Return the total frequencies of elements in nums such that those elements all have the maximum frequency.
 *
 * The frequency of an element is the number of occurrences of that element in the array.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,2,3,1,4]
 * Output: 4
 * Explanation: The elements 1 and 2 have a frequency of 2 which is the maximum frequency in the array.
 * So the number of elements in the array with maximum frequency is 4.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4,5]
 * Output: 5
 * Explanation: All elements of the array have a frequency of 1 which is the maximum.
 * So the number of elements in the array with maximum frequency is 5.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 1 <= nums[i] <= 100
 *
 */
public class CountElementsWithMaximumFrequency {

    // V0
    // IDEA: COUNT, FIND THE MAX FREQUENCY, SUM THE COUNTS THAT REACH IT
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxFrequencyElements(int[] nums) {
        Map<Integer, Integer> frequency = new HashMap<>();
        int maxFrequency = 0;
        for (int num : nums) {
            int f = frequency.getOrDefault(num, 0) + 1;
            frequency.put(num, f);
            maxFrequency = Math.max(maxFrequency, f);
        }

        // the answer counts elements, not distinct values
        int total = 0;
        for (int f : frequency.values()) {
            if (f == maxFrequency) {
                total += f;
            }
        }
        return total;
    }
}
