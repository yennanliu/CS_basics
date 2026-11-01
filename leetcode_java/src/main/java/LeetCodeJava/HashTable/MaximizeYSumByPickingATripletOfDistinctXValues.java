package LeetCodeJava.HashTable;

// https://leetcode.com/problems/maximize-ysum-by-picking-a-triplet-of-distinct-xvalues/description/

import java.util.*;

/**
 * 3572. Maximize Y-Sum by Picking a Triplet of Distinct X-Values
 * Medium
 *
 * You are given two integer arrays x and y, each of length n. You must choose three distinct indices i, j, and k such that:
 *
 * x[i] != x[j]
 * x[j] != x[k]
 * x[k] != x[i]
 *
 * Your goal is to maximize the value of y[i] + y[j] + y[k] under these conditions. Return the maximum possible sum that can be obtained by choosing such a triplet of indices.
 *
 * If no such triplet exists, return -1.
 *
 *
 * Example 1:
 *
 * Input: x = [1,2,1,3,2], y = [5,3,4,6,2]
 * Output: 14
 * Explanation:
 * Choose i = 0 (x[i] = 1, y[i] = 5), j = 1 (x[j] = 2, y[j] = 3), k = 3 (x[k] = 3, y[k] = 6).
 * All three values chosen from x are distinct. 5 + 3 + 6 = 14 is the maximum we can obtain. Hence, the output is 14.
 *
 * Example 2:
 *
 * Input: x = [1,2,1,2], y = [4,5,6,7]
 * Output: -1
 * Explanation:
 * There are only two distinct values in x. Hence, the output is -1.
 *
 *
 * Constraints:
 *
 * n == x.length == y.length
 * 3 <= n <= 10^5
 * 1 <= x[i], y[i] <= 10^6
 *
 */
public class MaximizeYSumByPickingATripletOfDistinctXValues {

    // V0
    // IDEA: KEEP ONLY THE BEST y PER DISTINCT x, THEN TAKE THE TOP THREE
    //   only reusing an x value is forbidden, so for each x there is never a
    //   reason to pick anything but its largest y. after that collapse it is
    //   "sum of the three largest numbers", impossible with fewer than three
    //   distinct x values.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxSumDistinctTriplet(int[] x, int[] y) {
        Map<Integer, Integer> bestY = new HashMap<>();
        for (int i = 0; i < x.length; i++) {
            if (!bestY.containsKey(x[i]) || bestY.get(x[i]) < y[i]) {
                bestY.put(x[i], y[i]);
            }
        }

        if (bestY.size() < 3) {
            return -1;
        }

        // one pass keeps the three largest values: first >= second >= third
        int first = 0;
        int second = 0;
        int third = 0;
        for (int value : bestY.values()) {
            if (value > first) {
                third = second;
                second = first;
                first = value;
            } else if (value > second) {
                third = second;
                second = value;
            } else if (value > third) {
                third = value;
            }
        }
        return first + second + third;
    }
}
