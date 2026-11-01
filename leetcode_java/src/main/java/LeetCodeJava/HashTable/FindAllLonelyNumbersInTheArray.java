package LeetCodeJava.HashTable;

// https://leetcode.com/problems/find-all-lonely-numbers-in-the-array/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2150. Find All Lonely Numbers in the Array
 * Medium
 *
 * You are given an integer array nums. A number x is lonely when it appears only once, and no adjacent number (i.e. x + 1 or x - 1) appears in the array.
 *
 * Return all lonely numbers in nums. You may return the answer in any order.
 *
 *
 * Example 1:
 *
 * Input: nums = [10,6,5,8]
 * Output: [10,8]
 * Explanation:
 * - 10 is a lonely number since it appears exactly once and 9 and 11 does not appear in nums.
 * - 8 is a lonely number since it appears exactly once and 7 and 9 does not appear in nums.
 * - 5 is not a lonely number since 6 appears in nums and vice versa.
 * Hence, the lonely numbers in nums are [10, 8].
 * Note that [8, 10] may also be returned.
 *
 * Example 2:
 *
 * Input: nums = [1,3,5,3]
 * Output: [1,5]
 * Explanation:
 * - 1 is a lonely number since it appears exactly once and 0 and 2 does not appear in nums.
 * - 5 is a lonely number since it appears exactly once and 4 and 6 does not appear in nums.
 * - 3 is not a lonely number since it appears twice.
 * Hence, the lonely numbers in nums are [1, 5].
 * Note that [5, 1] may also be returned.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 0 <= nums[i] <= 10^6
 *
 */
public class FindAllLonelyNumbersInTheArray {

    // V0
    // IDEA: HASH MAP OF COUNTS (one map answers both "appears once" and "no neighbour")
    //   x is lonely iff count[x] == 1 and neither x - 1 nor x + 1 is a key of the map.
    /**
     * time = O(N)
     * space = O(N)
     */
    public List<Integer> findLonely(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int num : nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }

        // iterate over the distinct keys, so a duplicate is never tested twice
        List<Integer> lonely = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            int value = entry.getKey();
            boolean appearsOnce = entry.getValue() == 1;
            boolean hasNeighbour = count.containsKey(value - 1) || count.containsKey(value + 1);
            if (appearsOnce && !hasNeighbour) {
                lonely.add(value);
            }
        }
        return lonely;
    }
}
