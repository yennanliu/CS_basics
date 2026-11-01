package LeetCodeJava.HashTable;

// https://leetcode.com/problems/smallest-absent-positive-greater-than-average/description/

import java.util.*;

/**
 * 3678. Smallest Absent Positive Greater Than Average
 * Easy
 *
 * You are given an integer array nums.
 *
 * Return the smallest absent positive integer in nums such that it is
 * strictly greater than the average of all elements in nums.
 *
 * The average of an array is defined as the sum of all its elements divided
 * by the number of elements.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,5]
 * Output: 6
 * Explanation:
 * The average of nums is (3 + 5) / 2 = 8 / 2 = 4.
 * The smallest absent positive integer greater than 4 is 6.
 *
 * Example 2:
 *
 * Input: nums = [-1,1,2]
 * Output: 3
 * Explanation:
 * The average of nums is (-1 + 1 + 2) / 3 = 2 / 3 = 0.667.
 * The smallest absent positive integer greater than 0.667 is 3.
 *
 * Example 3:
 *
 * Input: nums = [4,-1]
 * Output: 2
 * Explanation:
 * The average of nums is (4 + (-1)) / 2 = 3 / 2 = 1.50.
 * The smallest absent positive integer greater than 1.50 is 2.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * -100 <= nums[i] <= 100
 *
 */
public class SmallestAbsentPositiveGreaterThanAverage {

    // V0
    // IDEA: START AT floor(average) + 1, THEN STEP PAST VALUES THAT ARE PRESENT
    //   the smallest integer strictly above avg is floor(avg) + 1 whether or not
    //   avg is whole; max(1, ...) enforces positivity. Math.floorDiv floors toward
    //   -infinity, which is the right direction for a negative sum. at most n
    //   present values can block the walk.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int smallestAbsent(int[] nums) {
        Set<Integer> present = new HashSet<>();
        int sum = 0;
        for (int num : nums) {
            present.add(num);
            sum += num;
        }

        int candidate = Math.max(1, Math.floorDiv(sum, nums.length) + 1);
        while (present.contains(candidate)) {
            candidate++;
        }
        return candidate;
    }
}
