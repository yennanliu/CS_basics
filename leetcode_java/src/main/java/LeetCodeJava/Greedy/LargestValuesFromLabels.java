package LeetCodeJava.Greedy;

// https://leetcode.com/problems/largest-values-from-labels/description/

import java.util.*;

/**
 * 1090. Largest Values From Labels
 * Medium
 *
 * You are given n item's value and label as two integer arrays values and labels.
 * You are also given two integers numWanted and useLimit.
 *
 * Your task is to find a subset of items with the maximum sum of their values such that:
 *
 * - The number of items is at most numWanted.
 * - The number of items with the same label is at most useLimit.
 *
 * Return the maximum sum.
 *
 *
 * Example 1:
 *
 * Input: values = [5,4,3,2,1], labels = [1,1,2,2,3], numWanted = 3, useLimit = 1
 * Output: 9
 * Explanation:
 * The subset chosen is the first, third, and fifth items with the sum of values 5 + 3 + 1.
 *
 * Example 2:
 *
 * Input: values = [5,4,3,2,1], labels = [1,3,3,3,2], numWanted = 3, useLimit = 2
 * Output: 12
 * Explanation:
 * The subset chosen is the first, second, and third items with the sum of values 5 + 4 + 3.
 *
 * Example 3:
 *
 * Input: values = [9,8,8,7,6], labels = [0,0,0,1,1], numWanted = 3, useLimit = 1
 * Output: 16
 * Explanation:
 * The subset chosen is the first and fourth items with the sum of values 9 + 7.
 *
 *
 * Constraints:
 *
 * n == values.length == labels.length
 * 1 <= n <= 2 * 10^4
 * 0 <= values[i], labels[i] <= 2 * 10^4
 * 1 <= numWanted, useLimit <= n
 *
 */
public class LargestValuesFromLabels {

    // V0
    // IDEA: GREEDY + SORT (biggest value first) + PER-LABEL COUNTER
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int largestValsFromLabels(int[] values, int[] labels, int numWanted, int useLimit) {
        int n = values.length;

        // pair each value with its label: items[i] = {value, label}
        int[][] items = new int[n][2];
        for (int i = 0; i < n; i++) {
            items[i][0] = values[i];
            items[i][1] = labels[i];
        }

        // every item costs one slot, so always prefer the biggest value still allowed
        Arrays.sort(items, (a, b) -> Integer.compare(b[0], a[0]));

        Map<Integer, Integer> usedPerLabel = new HashMap<>();
        int total = 0;
        int taken = 0;

        for (int[] item : items) {
            if (taken == numWanted) {
                break;
            }
            int value = item[0];
            int label = item[1];
            int used = usedPerLabel.getOrDefault(label, 0);
            // skip only when this label's quota is already spent
            if (used < useLimit) {
                usedPerLabel.put(label, used + 1);
                taken++;
                total += value;
            }
        }
        return total;
    }
}
