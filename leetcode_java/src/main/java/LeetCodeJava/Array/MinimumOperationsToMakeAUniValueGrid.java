package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-operations-to-make-a-uni-value-grid/description/

import java.util.Arrays;

/**
 * 2033. Minimum Operations to Make a Uni-Value Grid
 * Medium
 *
 * You are given a 2D integer grid of size m x n and an integer x. In one operation, you can add x
 * to or subtract x from any element in the grid.
 *
 * A uni-value grid is a grid where all the elements of it are equal.
 *
 * Return the minimum number of operations to make the grid uni-value. If it is not possible,
 * return -1.
 *
 *
 * Example 1:
 *
 * Input: grid = [[2,4],[6,8]], x = 2
 * Output: 4
 * Explanation: We can make every element equal to 4 by doing the following:
 * - Add x to 2 once.
 * - Subtract x from 6 once.
 * - Subtract x from 8 twice.
 * A total of 4 operations were used.
 *
 * Example 2:
 *
 * Input: grid = [[1,5],[2,3]], x = 1
 * Output: 5
 * Explanation: We can make every element equal to 3.
 *
 * Example 3:
 *
 * Input: grid = [[1,2],[3,4]], x = 2
 * Output: -1
 * Explanation: It is impossible to make every element equal.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 10^5
 * 1 <= m * n <= 10^5
 * 1 <= x, grid[i][j] <= 10^4
 *
 */
public class MinimumOperationsToMakeAUniValueGrid {

    // V0
    // IDEA: FEASIBILITY BY REMAINDER mod x, THEN SORT + MEDIAN
    //       every operation moves a value by exactly x, so two cells can only meet if they
    //       share the same remainder mod x -> otherwise -1. given that, the cost of target t
    //       is sum(|v - t|) / x, and a sum of absolute deviations is minimised at the MEDIAN.
    /**
     * time = O(K log K), K = m * n
     * space = O(K)
     */
    public int minOperations(int[][] grid, int x) {
        // flatten the grid so it can be sorted
        int rows = grid.length;
        int cols = grid[0].length;
        int[] values = new int[rows * cols];
        int idx = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                values[idx] = grid[r][c];
                idx++;
            }
        }

        // all values must agree mod x, or they can never become equal
        int remainder = values[0] % x;
        for (int value : values) {
            if (value % x != remainder) {
                return -1;
            }
        }

        // the median minimises the total distance
        Arrays.sort(values);
        int median = values[values.length / 2];

        // total moves can reach 10^5 * 10^4 / 1 = 10^9 -> sum in long for safety
        long operations = 0;
        for (int value : values) {
            operations += Math.abs(value - median) / x;
        }
        return (int) operations;
    }

    // V1
    // IDEA: COUNTING SORT TO FIND THE MEDIAN (values are bounded by 10^4)
    //       same feasibility check and median argument as V0, but the median is found by
    //       walking a count array instead of sorting -> linear in K plus the value range.
    /**
     * time = O(K + V), K = m * n, V = max value (10^4)
     * space = O(V)
     */
    public int minOperations_1(int[][] grid, int x) {
        int remainder = grid[0][0] % x;
        int maxValue = 0;
        int total = 0;

        // feasibility check + find the value range
        for (int[] row : grid) {
            for (int value : row) {
                if (value % x != remainder) {
                    return -1;
                }
                maxValue = Math.max(maxValue, value);
                total++;
            }
        }

        // count[v] = how many cells hold value v
        int[] count = new int[maxValue + 1];
        for (int[] row : grid) {
            for (int value : row) {
                count[value]++;
            }
        }

        // the median is the value at sorted index total / 2
        int target = total / 2;
        int seen = 0;
        int median = 0;
        for (int v = 0; v <= maxValue; v++) {
            seen += count[v];
            if (seen > target) {
                median = v;
                break;
            }
        }

        // each distinct value contributes count[v] * (|v - median| / x) moves
        long operations = 0;
        for (int v = 0; v <= maxValue; v++) {
            if (count[v] > 0) {
                operations += (long) count[v] * (Math.abs(v - median) / x);
            }
        }
        return (int) operations;
    }
}
