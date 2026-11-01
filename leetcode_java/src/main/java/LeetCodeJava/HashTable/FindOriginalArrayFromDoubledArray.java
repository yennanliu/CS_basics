package LeetCodeJava.HashTable;

// https://leetcode.com/problems/find-original-array-from-doubled-array/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 2007. Find Original Array From Doubled Array
 * Medium
 *
 * An integer array original is transformed into a doubled array changed by appending twice the
 * value of every element in original, and then randomly shuffling the resulting array.
 *
 * Given an array changed, return original if changed is a doubled array. If changed is not a
 * doubled array, return an empty array. The elements in original may be returned in any order.
 *
 *
 * Example 1:
 *
 * Input: changed = [1,3,4,2,6,8]
 * Output: [1,3,4]
 *
 * Example 2:
 *
 * Input: changed = [6,3,0,1]
 * Output: []
 *
 * Example 3:
 *
 * Input: changed = [1]
 * Output: []
 *
 *
 * Constraints:
 *
 * 1 <= changed.length <= 10^5
 * 0 <= changed[i] <= 10^5
 *
 */
public class FindOriginalArrayFromDoubledArray {

    private static final int MAX_VALUE = 100_000;

    // V0
    // IDEA: SORT + GREEDY MATCHING WITH A COUNT MAP
    // the SMALLEST remaining value cannot be anyone's double, so it belongs to original and must
    // consume one copy of twice itself. Decrementing x before looking up 2x makes zeros pair with
    // each other (a run of zeros must be even).
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int[] findOriginalArray(int[] changed) {
        if (changed.length % 2 == 1) {
            return new int[0];
        }

        Map<Integer, Integer> count = new HashMap<>();
        for (int value : changed) {
            count.put(value, count.getOrDefault(value, 0) + 1);
        }

        int[] sorted = changed.clone();
        Arrays.sort(sorted);
        int[] original = new int[changed.length / 2];
        int size = 0;
        for (int value : sorted) {
            // already consumed as somebody's double
            if (count.get(value) == 0) {
                continue;
            }
            count.put(value, count.get(value) - 1);
            int doubled = value * 2;
            int doubledLeft = count.getOrDefault(doubled, 0);
            if (doubledLeft == 0) {
                return new int[0];
            }
            count.put(doubled, doubledLeft - 1);
            original[size++] = value;
        }
        return original;
    }

    // V1
    // IDEA: COUNTING SORT (values are bounded by 10^5)
    // same greedy, but walking the value range 0..10^5 in ascending order replaces both the sort and
    // the hash map with one count array.
    /**
     * time = O(N + V), V = 10^5
     * space = O(V)
     */
    public int[] findOriginalArray_1(int[] changed) {
        if (changed.length % 2 == 1) {
            return new int[0];
        }
        int[] count = new int[MAX_VALUE + 1];
        for (int value : changed) {
            count[value]++;
        }

        int[] original = new int[changed.length / 2];
        int size = 0;
        for (int value = 0; value <= MAX_VALUE; value++) {
            while (count[value] > 0) {
                count[value]--;
                int doubled = value * 2;
                if (doubled > MAX_VALUE || count[doubled] == 0) {
                    return new int[0];
                }
                count[doubled]--;
                original[size++] = value;
            }
        }
        return original;
    }
}
