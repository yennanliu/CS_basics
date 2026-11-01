package LeetCodeJava.HashTable;

// https://leetcode.com/problems/counting-elements/description/

import java.util.*;

/**
 * 1426. Counting Elements
 * Easy
 *
 * Given an integer array arr, count how many elements x there are, such that
 * x + 1 is also in arr. If there are duplicates in arr, count them separately.
 *
 *
 * Example 1:
 *
 * Input: arr = [1,2,3]
 * Output: 2
 * Explanation: 1 and 2 are counted cause 2 and 3 are in arr.
 *
 * Example 2:
 *
 * Input: arr = [1,1,3,3,5,5,7,7]
 * Output: 0
 * Explanation: No numbers are counted, cause there is no 2, 4, 6, or 8 in arr.
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 1000
 * 0 <= arr[i] <= 1000
 *
 */
public class CountingElements {

    // V0
    // IDEA: HASH TABLE (counter) — for every distinct x with x + 1 present,
    //       ALL occurrences of x count
    /**
     * time = O(N)
     * space = O(N)
     */
    public int countElements(int[] arr) {
        // value -> how many times it appears
        Map<Integer, Integer> count = new HashMap<>();
        for (int value : arr) {
            count.put(value, count.getOrDefault(value, 0) + 1);
        }

        int result = 0;
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            // duplicates are counted separately, so add the whole multiplicity
            if (count.containsKey(entry.getKey() + 1)) {
                result += entry.getValue();
            }
        }
        return result;
    }

    // V1
    // IDEA: HASH SET — only membership of x + 1 matters, so walk the original
    //       array (which already repeats duplicates) and test each element
    /**
     * time = O(N)
     * space = O(N)
     */
    public int countElements_1(int[] arr) {
        Set<Integer> seen = new HashSet<>();
        for (int value : arr) {
            seen.add(value);
        }

        int result = 0;
        for (int value : arr) {
            if (seen.contains(value + 1)) {
                result++;
            }
        }
        return result;
    }
}
