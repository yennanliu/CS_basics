package LeetCodeJava.Array;

// https://leetcode.com/problems/longest-common-subsequence-between-sorted-arrays/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 1940. Longest Common Subsequence Between Sorted Arrays
 * Medium
 *
 * Given an array of integer arrays arrays where each arrays[i] is sorted in strictly increasing
 * order, return an integer array representing the longest common subsequence among all the arrays.
 *
 * A subsequence is a sequence that can be derived from another sequence by deleting some elements
 * (possibly none) without changing the order of the remaining elements.
 *
 *
 * Example 1:
 *
 * Input: arrays = [[1,3,4],[1,4,7,9]]
 * Output: [1,4]
 *
 * Example 2:
 *
 * Input: arrays = [[2,3,6,8],[1,2,3,5,6,7,10],[2,3,4,6,9]]
 * Output: [2,3,6]
 *
 * Example 3:
 *
 * Input: arrays = [[1,2,3,4,5],[6,7,8]]
 * Output: []
 *
 *
 * Constraints:
 *
 * 2 <= arrays.length <= 100
 * 1 <= arrays[i].length <= 100
 * 1 <= arrays[i][j] <= 100
 * arrays[i] is sorted in strictly increasing order.
 *
 */
public class LongestCommonSubsequenceBetweenSortedArrays {

    private static final int MAX_VALUE = 100;

    // V0
    // IDEA: COUNTING (every row is strictly increasing, so the LCS is the set intersection)
    // shared values already appear in the same increasing order in every row, and no row repeats a
    // value, so a value is in the answer exactly when it occurs arrays.length times overall.
    /**
     * time = O(N + V), N = total elements, V = 100
     * space = O(V)
     */
    public List<Integer> longestCommonSubsequence(int[][] arrays) {
        int[] count = new int[MAX_VALUE + 1];
        for (int[] row : arrays) {
            for (int value : row) {
                count[value]++;
            }
        }

        // scanning values in ascending order emits the answer already sorted
        List<Integer> common = new ArrayList<>();
        for (int value = 1; value <= MAX_VALUE; value++) {
            if (count[value] == arrays.length) {
                common.add(value);
            }
        }
        return common;
    }
}
