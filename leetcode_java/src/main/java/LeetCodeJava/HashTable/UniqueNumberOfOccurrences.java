package LeetCodeJava.HashTable;

// https://leetcode.com/problems/unique-number-of-occurrences/description/

import java.util.*;

/**
 * 1207. Unique Number of Occurrences
 * Easy
 *
 * Given an array of integers arr, return true if the number of occurrences of each
 * value in the array is unique or false otherwise.
 *
 *
 * Example 1:
 *
 * Input: arr = [1,2,2,1,1,3]
 * Output: true
 * Explanation: The value 1 has 3 occurrences, 2 has 2 and 3 has 1. No two values
 * have the same number of occurrences.
 *
 * Example 2:
 *
 * Input: arr = [1,2]
 * Output: false
 *
 * Example 3:
 *
 * Input: arr = [-3,0,1,-3,1,1,1,-3,10,0]
 * Output: true
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 1000
 * -1000 <= arr[i] <= 1000
 *
 */
public class UniqueNumberOfOccurrences {

    // V0
    // IDEA: HASH TABLE (count each value) + SET (the counts must all be distinct)
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean uniqueOccurrences(int[] arr) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int value : arr) {
            count.put(value, count.getOrDefault(value, 0) + 1);
        }

        Set<Integer> seenCounts = new HashSet<>();
        for (int occurrences : count.values()) {
            // add returns false when this count was already used by another value
            if (!seenCounts.add(occurrences)) {
                return false;
            }
        }
        return true;
    }
}
