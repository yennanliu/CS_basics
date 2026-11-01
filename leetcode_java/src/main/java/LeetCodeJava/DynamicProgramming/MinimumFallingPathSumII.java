package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-falling-path-sum-ii/description/

/**
 * 1289. Minimum Falling Path Sum II
 * Hard
 *
 * Given an n x n integer matrix grid, return the minimum sum of a falling path
 * with non-zero shifts.
 *
 * A falling path with non-zero shifts is a choice of exactly one element from each
 * row of grid such that no two elements chosen in adjacent rows are in the same column.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,2,3],[4,5,6],[7,8,9]]
 * Output: 13
 * Explanation:
 * The possible falling paths are:
 * [1,5,9], [1,5,7], [1,6,7], [1,6,8],
 * [2,4,8], [2,4,9], [2,6,7], [2,6,8],
 * [3,4,8], [3,4,9], [3,5,7], [3,5,9]
 * The falling path with the smallest sum is [1,5,7], so the answer is 13.
 *
 * Example 2:
 *
 * Input: grid = [[7]]
 * Output: 7
 *
 *
 * Constraints:
 *
 * n == grid.length == grid[i].length
 * 1 <= n <= 200
 * -99 <= grid[i][j] <= 99
 *
 */
public class MinimumFallingPathSumII {

    // V0
    // IDEA: 1D DP (ROLLING ROW), SCAN EVERY OTHER COLUMN OF THE PREVIOUS ROW
    /**
     * dp[j] = min falling path sum ending at column j of the current row
     *
     * next[j] = grid[row][j] + min(dp[k] for k != j)
     *   -> the only constraint: the previous row may not use the same column
     *
     * base: dp = all 0 before the first row; answer = min(dp)
     */
    /**
     * time = O(N^3)
     * space = O(N)
     */
    public int minFallingPathSum(int[][] grid) {
        int n = grid.length;
        if (n == 1) {
            return grid[0][0];
        }

        int[] dp = new int[n];
        for (int[] row : grid) {
            int[] next = new int[n];
            for (int j = 0; j < n; j++) {
                int bestAbove = Integer.MAX_VALUE;
                for (int k = 0; k < n; k++) {
                    if (k != j) {
                        bestAbove = Math.min(bestAbove, dp[k]);
                    }
                }
                next[j] = row[j] + bestAbove;
            }
            dp = next;
        }

        int answer = Integer.MAX_VALUE;
        for (int value : dp) {
            answer = Math.min(answer, value);
        }
        return answer;
    }

    // V1
    // IDEA: 1D DP + KEEP ONLY THE TWO SMALLEST VALUES OF THE PREVIOUS ROW (USE THIS ONE)
    /**
     * min(dp[k] for k != j) is the previous row's smallest value, unless that value
     * sits in column j -- then it is the second smallest. So (smallest, its column,
     * second smallest) replaces the O(N) inner scan.
     */
    /**
     * time = O(N^2)
     * space = O(1)
     */
    public int minFallingPathSum_1(int[][] grid) {
        int n = grid.length;
        if (n == 1) {
            return grid[0][0];
        }

        // before the first row every column "costs" 0, and no column is excluded
        int smallest = 0;
        int smallestColumn = -1;
        int secondSmallest = 0;

        for (int[] row : grid) {
            int nextSmallest = Integer.MAX_VALUE;
            int nextSmallestColumn = -1;
            int nextSecond = Integer.MAX_VALUE;

            for (int j = 0; j < n; j++) {
                // the smallest above, unless it is in this same column
                int bestAbove = (j == smallestColumn) ? secondSmallest : smallest;
                int pathSum = row[j] + bestAbove;

                if (pathSum < nextSmallest) {
                    nextSecond = nextSmallest;
                    nextSmallest = pathSum;
                    nextSmallestColumn = j;
                } else if (pathSum < nextSecond) {
                    nextSecond = pathSum;
                }
            }
            smallest = nextSmallest;
            smallestColumn = nextSmallestColumn;
            secondSmallest = nextSecond;
        }
        return smallest;
    }
}
