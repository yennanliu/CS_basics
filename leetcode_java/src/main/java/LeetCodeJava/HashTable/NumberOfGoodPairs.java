package LeetCodeJava.HashTable;

// https://leetcode.com/problems/number-of-good-pairs/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 1512. Number of Good Pairs
 * Easy
 *
 * Given an array of integers nums, return the number of good pairs.
 *
 * A pair (i, j) is called good if nums[i] == nums[j] and i < j.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,1,1,3]
 * Output: 4
 * Explanation: There are 4 good pairs (0,3), (0,4), (3,4), (2,5) 0-indexed.
 *
 * Example 2:
 *
 * Input: nums = [1,1,1,1]
 * Output: 6
 * Explanation: Each pair in the array are good.
 *
 * Example 3:
 *
 * Input: nums = [1,2,3]
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 1 <= nums[i] <= 100
 *
 */
public class NumberOfGoodPairs {

    // V0
    // IDEA: RUNNING COUNTER
    //   landing on value x, every earlier occurrence of x pairs with the current index
    //   (i < j holds automatically), so add count[x], then bump it.
    //   equivalent closed form: sum over values of C(freq, 2).
    /**
     * time = O(N)
     * space = O(C)  C = number of distinct values
     */
    public int numIdenticalPairs(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();
        int pairs = 0;
        for (int x : nums) {
            int seenBefore = count.getOrDefault(x, 0);
            pairs += seenBefore;
            count.put(x, seenBefore + 1);
        }
        return pairs;
    }
}
