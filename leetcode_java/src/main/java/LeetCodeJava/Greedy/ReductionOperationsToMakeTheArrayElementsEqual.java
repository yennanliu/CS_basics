package LeetCodeJava.Greedy;

// https://leetcode.com/problems/reduction-operations-to-make-the-array-elements-equal/description/

import java.util.*;

/**
 * 1887. Reduction Operations to Make the Array Elements Equal
 * Medium
 *
 * Given an integer array nums, your goal is to make all elements in nums equal.
 * To complete one operation, follow these steps:
 *
 * 1. Find the largest value in nums. Let its index be i (0-indexed) and its value
 *    be largest. If there are multiple elements with the largest value, pick the
 *    smallest i.
 * 2. Find the next largest value in nums strictly smaller than largest. Let its
 *    value be nextLargest.
 * 3. Reduce nums[i] to nextLargest.
 *
 * Return the number of operations to make all elements in nums equal.
 *
 *
 * Example 1:
 *
 * Input: nums = [5,1,3]
 * Output: 3
 * Explanation: It takes 3 operations to make all elements in nums equal:
 * 1. largest = 5 at index 0. nextLargest = 3. Reduce nums[0] to 3. nums = [3,1,3].
 * 2. largest = 3 at index 0. nextLargest = 1. Reduce nums[0] to 1. nums = [1,1,3].
 * 3. largest = 3 at index 2. nextLargest = 1. Reduce nums[2] to 1. nums = [1,1,1].
 *
 * Example 2:
 *
 * Input: nums = [1,1,1]
 * Output: 0
 * Explanation: All elements in nums are already equal.
 *
 * Example 3:
 *
 * Input: nums = [1,1,2,2,3]
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 5 * 10^4
 * 1 <= nums[i] <= 5 * 10^4
 *
 */
public class ReductionOperationsToMakeTheArrayElementsEqual {

    // V0
    // IDEA: SORT + COUNT DISTINCT LEVELS BELOW EACH ELEMENT
    /**
     * time = O(N log N)
     * space = O(N) for the sorted copy
     */
    public int reductionOperations(int[] nums) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        // an element at the r-th distinct value (0-indexed, ascending) needs exactly r steps down
        int operations = 0;
        int rank = 0;
        for (int i = 1; i < sorted.length; i++) {
            if (sorted[i] != sorted[i - 1]) {
                rank++;
            }
            operations += rank;
        }
        return operations;
    }
}
