package LeetCodeJava.Array;

// https://leetcode.com/problems/array-of-doubled-pairs/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *  954. Array of Doubled Pairs
 *  Medium
 *
 *  Given an integer array of even length arr, return true if it is possible to
 *  reorder arr such that arr[2 * i + 1] = 2 * arr[2 * i] for every
 *  0 <= i < len(arr) / 2, or false otherwise.
 *
 *
 *  Example 1:
 *
 *  Input: arr = [3,1,3,6]
 *  Output: false
 *
 *  Example 2:
 *
 *  Input: arr = [2,1,2,6]
 *  Output: false
 *
 *  Example 3:
 *
 *  Input: arr = [4,-2,2,-4]
 *  Output: true
 *  Explanation: We can take two groups, [-2,-4] and [2,4] to form [-2,-4,2,4]
 *  or [2,4,-2,-4].
 *
 *
 *  Constraints:
 *
 *  2 <= arr.length <= 3 * 10^4
 *  arr.length is even
 *  -10^5 <= arr[i] <= 10^5
 */
public class ArrayOfDoubledPairs {

    // V0
    // IDEA: COUNT MAP + process values by ascending ABSOLUTE value, greedily pair x with 2x
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public boolean canReorderDoubled(int[] arr) {
        if (arr == null || arr.length == 0) {
            return true;
        }
        Map<Integer, Integer> cnt = new HashMap<>();
        for (int x : arr) {
            cnt.put(x, cnt.getOrDefault(x, 0) + 1);
        }

        // zeros must come in pairs (0 pairs with 0)
        int zeros = cnt.getOrDefault(0, 0);
        if (zeros % 2 != 0) {
            return false;
        }
        cnt.remove(0);

        Integer[] keys = cnt.keySet().toArray(new Integer[0]);
        Arrays.sort(keys, (a, b) -> Math.abs(a) - Math.abs(b));

        for (Integer x : keys) {
            int c = cnt.getOrDefault(x, 0);
            if (c == 0) {
                continue;
            }
            /**
             * smallest |x| left must be the "single" of its pair,
             * so it needs c copies of 2*x available
             */
            int twice = cnt.getOrDefault(2 * x, 0);
            if (twice < c) {
                return false;
            }
            cnt.put(2 * x, twice - c);
            cnt.put(x, 0);
        }
        return true;
    }
}
