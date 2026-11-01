package LeetCodeJava.HashTable;

// https://leetcode.com/problems/sum-of-unique-elements/description/

import java.util.*;

/**
 * 1748. Sum of Unique Elements
 * Easy
 *
 * You are given an integer array nums. The unique elements of an array are the elements that appear
 * exactly once in the array.
 *
 * Return the sum of all the unique elements of nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,2]
 * Output: 4
 * Explanation: The unique elements are [1,3], and the sum is 4.
 *
 * Example 2:
 *
 * Input: nums = [1,1,1,1,1]
 * Output: 0
 * Explanation: There are no unique elements, and the sum is 0.
 *
 * Example 3:
 *
 * Input: nums = [1,2,3,4,5]
 * Output: 15
 * Explanation: The unique elements are [1,2,3,4,5], and the sum is 15.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 1 <= nums[i] <= 100
 *
 */
public class SumOfUniqueElements {

    // V0
    // IDEA: HASH TABLE COUNT, THEN SUM THE VALUES SEEN EXACTLY ONCE
    //       "unique" means occurs once, not distinct: a repeated value adds nothing
    /**
     * time = O(N)
     * space = O(N)
     */
    public int sumOfUnique(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int num : nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }

        int sum = 0;
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            if (entry.getValue() == 1) {
                sum += entry.getKey();
            }
        }
        return sum;
    }
}
