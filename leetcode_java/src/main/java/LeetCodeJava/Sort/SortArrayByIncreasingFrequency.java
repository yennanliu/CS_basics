package LeetCodeJava.Sort;

// https://leetcode.com/problems/sort-array-by-increasing-frequency/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *  1636. Sort Array by Increasing Frequency
 *  Easy
 *
 *  Given an array of integers nums, sort the array in increasing order based on
 *  the frequency of the values. If multiple values have the same frequency, sort
 *  them in decreasing order.
 *
 *  Return the sorted array.
 *
 *  Example 1:
 *    Input: nums = [1,1,2,2,2,3]
 *    Output: [3,1,1,2,2,2]
 *    Explanation: '3' has frequency 1, '1' has frequency 2, '2' has frequency 3.
 *
 *  Example 2:
 *    Input: nums = [2,3,1,3,2]
 *    Output: [1,3,3,2,2]
 *    Explanation: '2' and '3' both have frequency 2, so they are sorted in
 *                 decreasing order.
 *
 *  Constraints:
 *    1 <= nums.length <= 100
 *    -100 <= nums[i] <= 100
 */
public class SortArrayByIncreasingFrequency {

    // V0
    // IDEA: FREQUENCY MAP + COMPOSITE COMPARATOR (freq ASC, value DESC)
    //       count occurrences, then sort the ORIGINAL elements (not the distinct
    //       keys) so each value is emitted exactly `freq` times and equal values
    //       naturally end up adjacent.
    //       the two criteria point opposite ways, so compare freq ascending and
    //       fall back to value descending.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int[] frequencySort(int[] nums) {
        Map<Integer, Integer> cnt = new HashMap<>();
        for (int x : nums) {
            cnt.put(x, cnt.getOrDefault(x, 0) + 1);
        }

        Integer[] boxed = new Integer[nums.length];
        for (int i = 0; i < nums.length; i++) {
            boxed[i] = nums[i];
        }

        Arrays.sort(boxed, (a, b) -> {
            int ca = cnt.get(a);
            int cb = cnt.get(b);
            if (ca != cb) {
                return Integer.compare(ca, cb);   // frequency ASC
            }
            return Integer.compare(b, a);         // value DESC
        });

        int[] res = new int[nums.length];
        for (int i = 0; i < boxed.length; i++) {
            res[i] = boxed[i];
        }
        return res;
    }
}
