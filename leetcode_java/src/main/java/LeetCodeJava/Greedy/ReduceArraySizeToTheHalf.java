package LeetCodeJava.Greedy;

// https://leetcode.com/problems/reduce-array-size-to-the-half/description/

import java.util.*;

/**
 * 1338. Reduce Array Size to The Half
 * Medium
 *
 * You are given an integer array arr. You can choose a set of integers and
 * remove all the occurrences of these integers in the array.
 *
 * Return the minimum size of the set so that at least half of the integers
 * of the array are removed.
 *
 *
 * Example 1:
 *
 * Input: arr = [3,3,3,3,5,5,5,2,2,7]
 * Output: 2
 * Explanation: Choosing {3,7} will make the new array [5,5,5,2,2] which has
 * size 5 (i.e equal to half of the size of the old array).
 * Possible sets of size 2 are {3,5},{3,2},{5,2}.
 * Choosing set {2,7} is not possible as it will make the new array
 * [3,3,3,3,5,5,5] which has a size greater than half of the size of the
 * old array.
 *
 * Example 2:
 *
 * Input: arr = [7,7,7,7,7,7]
 * Output: 1
 * Explanation: The only possible set you can choose is {7}.
 * This will make the new array empty.
 *
 *
 * Constraints:
 *
 * 2 <= arr.length <= 10^5
 * arr.length is even.
 * 1 <= arr[i] <= 10^5
 *
 */
public class ReduceArraySizeToTheHalf {

    // V0
    // IDEA: GREEDY + COUNTER (remove the most frequent values first)
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int minSetSize(int[] arr) {
        int n = arr.length;

        Map<Integer, Integer> count = new HashMap<>();
        for (int num : arr) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }

        // frequencies, highest first
        List<Integer> frequencies = new ArrayList<>(count.values());
        frequencies.sort(Collections.reverseOrder());

        // each chosen value should remove as many elements as possible
        int removed = 0;
        int chosen = 0;
        for (int freq : frequencies) {
            removed += freq;
            chosen++;
            if (removed * 2 >= n) {
                break;
            }
        }
        return chosen;
    }

    // V0-1
    // IDEA: GREEDY + COUNTING SORT ON FREQUENCY (frequencies are at most n, so bucket them)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minSetSize_0_1(int[] arr) {
        int n = arr.length;

        Map<Integer, Integer> count = new HashMap<>();
        for (int num : arr) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }

        // valuesWithFreq[f] = how many distinct values occur exactly f times
        int[] valuesWithFreq = new int[n + 1];
        for (int freq : count.values()) {
            valuesWithFreq[freq]++;
        }

        int removed = 0;
        int chosen = 0;
        // walk frequencies from high to low, taking values until half is gone
        for (int freq = n; freq > 0 && removed * 2 < n; freq--) {
            int available = valuesWithFreq[freq];
            while (available > 0 && removed * 2 < n) {
                removed += freq;
                chosen++;
                available--;
            }
        }
        return chosen;
    }
}
