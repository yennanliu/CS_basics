package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/check-if-there-is-a-path-with-equal-number-of-0s-and-1s/description/

/**
 * 2510. Check if There is a Path With Equal Number of 0's And 1's
 * Medium
 *
 * You are given a 0-indexed m x n binary matrix grid. You can move from a cell (row, col) to any
 * of the cells (row + 1, col) or (row, col + 1).
 *
 * Return true if there is a path from (0, 0) to (m - 1, n - 1) that visits an equal number of 0's
 * and 1's. Otherwise return false.
 *
 *
 * Example 1:
 *
 * Input: grid = [[0,1,0,0],[0,1,0,0],[1,0,1,0]]
 * Output: true
 * Explanation: There is a path with 3 cells with a value of 1 and 3 with a value of 0.
 *
 * Example 2:
 *
 * Input: grid = [[1,1,0],[0,0,1],[1,0,0]]
 * Output: false
 * Explanation: There is no path in this grid with an equal number of 0's and 1's.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 2 <= m, n <= 100
 * grid[i][j] is either 0 or 1.
 *
 */
public class CheckIfThereIsAPathWithEqualNumberOf0sAnd1s {

    // V0
    // IDEA: DP ON (CELL, ONES SO FAR) - reachable[r][c][ones] AS A BOOLEAN TABLE (THE BITSET DP, SPELLED OUT)
    /**
     * time = O(M * N * (M + N))
     * space = O(N * (M + N))
     */
    public boolean isThereAPath(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int pathLength = rows + cols - 1;
        if (pathLength % 2 == 1) {
            return false;
        }
        int targetOnes = pathLength / 2;

        // prev[c][ones] = some path to (r - 1, c) collected exactly `ones` ones
        boolean[][] prev = new boolean[cols][pathLength + 1];
        for (int r = 0; r < rows; r++) {
            boolean[][] cur = new boolean[cols][pathLength + 1];
            for (int c = 0; c < cols; c++) {
                int add = grid[r][c];
                if (r == 0 && c == 0) {
                    cur[0][add] = true;
                    continue;
                }
                for (int ones = 0; ones + add <= pathLength; ones++) {
                    boolean fromUp = r > 0 && prev[c][ones];
                    boolean fromLeft = c > 0 && cur[c - 1][ones];
                    if (fromUp || fromLeft) {
                        cur[c][ones + add] = true;
                    }
                }
            }
            prev = cur;
        }
        return prev[cols - 1][targetOnes];
    }

    // V1
    // IDEA: MIN / MAX ONES DP - EVERY PATH HAS m + n - 1 CELLS; THE REACHABLE ONE-COUNTS AT A CELL FORM A CONTIGUOUS RANGE
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public boolean isThereAPath_1(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // every monotone path visits exactly rows + cols - 1 cells; an odd count cannot split evenly
        int pathLength = rows + cols - 1;
        if (pathLength % 2 == 1) {
            return false;
        }
        int targetOnes = pathLength / 2;

        // minOnes[r][c] / maxOnes[r][c] = fewest / most ones collected by any path (0,0) -> (r,c).
        // two paths to a cell differ by swapping one cell at a time, which changes the count by
        // at most 1, so every value between min and max is also reachable.
        int[][] minOnes = new int[rows][cols];
        int[][] maxOnes = new int[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r == 0 && c == 0) {
                    minOnes[r][c] = grid[r][c];
                    maxOnes[r][c] = grid[r][c];
                    continue;
                }
                int bestMin = Integer.MAX_VALUE;
                int bestMax = Integer.MIN_VALUE;
                if (r > 0) {
                    bestMin = Math.min(bestMin, minOnes[r - 1][c]);
                    bestMax = Math.max(bestMax, maxOnes[r - 1][c]);
                }
                if (c > 0) {
                    bestMin = Math.min(bestMin, minOnes[r][c - 1]);
                    bestMax = Math.max(bestMax, maxOnes[r][c - 1]);
                }
                minOnes[r][c] = bestMin + grid[r][c];
                maxOnes[r][c] = bestMax + grid[r][c];
            }
        }

        return minOnes[rows - 1][cols - 1] <= targetOnes && targetOnes <= maxOnes[rows - 1][cols - 1];
    }
}
