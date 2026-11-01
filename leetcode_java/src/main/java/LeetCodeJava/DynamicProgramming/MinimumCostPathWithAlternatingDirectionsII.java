package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-cost-path-with-alternating-directions-ii/description/

import java.util.*;

/**
 * 3603. Minimum Cost Path with Alternating Directions II
 * Medium
 *
 * You are given two integers m and n representing the number of rows and
 * columns of a grid, respectively.
 *
 * The cost to enter cell (i, j) is defined as (i + 1) * (j + 1).
 *
 * You are also given a 2D integer array waitCost where waitCost[i][j] defines
 * the cost to wait on that cell.
 *
 * The path will always begin by entering cell (0, 0) on move 1 and paying the
 * entrance cost.
 *
 * At each step, you follow an alternating pattern:
 *
 * On odd-numbered seconds, you must move right or down to an adjacent cell,
 * paying its entry cost.
 * On even-numbered seconds, you must wait in place for exactly one second and
 * pay waitCost[i][j] during that second.
 *
 * Return the minimum total cost required to reach (m - 1, n - 1).
 *
 *
 * Example 1:
 *
 * Input: m = 1, n = 2, waitCost = [[1,2]]
 * Output: 3
 * Explanation:
 * The optimal path is:
 * Start at cell (0, 0) at second 1 with entry cost (0 + 1) * (0 + 1) = 1.
 * Second 1: Move right to cell (0, 1) with entry cost (0 + 1) * (1 + 1) = 2.
 * Thus, the total cost is 1 + 2 = 3.
 *
 * Example 2:
 *
 * Input: m = 2, n = 2, waitCost = [[3,5],[2,4]]
 * Output: 9
 * Explanation:
 * The optimal path is:
 * Start at cell (0, 0) at second 1 with entry cost (0 + 1) * (0 + 1) = 1.
 * Second 1: Move down to cell (1, 0) with entry cost (1 + 1) * (0 + 1) = 2.
 * Second 2: Wait at cell (1, 0), paying waitCost[1][0] = 2.
 * Second 3: Move right to cell (1, 1) with entry cost (1 + 1) * (1 + 1) = 4.
 * Thus, the total cost is 1 + 2 + 2 + 4 = 9.
 *
 * Example 3:
 *
 * Input: m = 2, n = 3, waitCost = [[6,1,4],[3,2,5]]
 * Output: 16
 * Explanation:
 * The optimal path is:
 * Start at cell (0, 0) at second 1 with entry cost (0 + 1) * (0 + 1) = 1.
 * Second 1: Move right to cell (0, 1) with entry cost (0 + 1) * (1 + 1) = 2.
 * Second 2: Wait at cell (0, 1), paying waitCost[0][1] = 1.
 * Second 3: Move down to cell (1, 1) with entry cost (1 + 1) * (1 + 1) = 4.
 * Second 4: Wait at cell (1, 1), paying waitCost[1][1] = 2.
 * Second 5: Move right to cell (1, 2) with entry cost (1 + 1) * (2 + 1) = 6.
 * Thus, the total cost is 1 + 2 + 1 + 4 + 2 + 6 = 16.
 *
 *
 * Constraints:
 *
 * 1 <= m, n <= 10^5
 * 2 <= m * n <= 10^5
 * waitCost.length == m
 * waitCost[0].length == n
 * 0 <= waitCost[i][j] <= 10^5
 *
 */
public class MinimumCostPathWithAlternatingDirectionsII {

    // V0
    // IDEA: GRID DP, CHARGING A CELL'S WAIT WHEN YOU STEP OUT OF IT
    //   odd seconds are moves and even seconds are forced waits, so the walk is an
    //   ordinary right / down path. We pay the entry cost (i + 1) * (j + 1) of every
    //   cell on it, plus one wait for every cell we LEAVE, except (0, 0) (second 1 is
    //   a move); the destination is never left, so its wait never counts.
    //   dp[i][j] = cheapest cost of arriving at (i, j), its entry cost included
    //   dp[i][j] = min(dp[i-1][j] + wait(i-1, j), dp[i][j-1] + wait(i, j-1)) + (i+1)*(j+1)
    //   one row is rolled, since m * n <= 10^5 but either side may be long.
    /**
     * time = O(M * N)
     * space = O(N)
     */
    public long minCost(int m, int n, int[][] waitCost) {
        long[] previousRow = new long[n];
        for (int i = 0; i < m; i++) {
            long[] currentRow = new long[n];
            for (int j = 0; j < n; j++) {
                long entryCost = (long) (i + 1) * (j + 1);
                if (i == 0 && j == 0) {
                    // the start: entered at second 0, its cost still counts
                    currentRow[j] = entryCost;
                    continue;
                }
                long best = Long.MAX_VALUE;
                if (i > 0) {
                    best = Math.min(best, previousRow[j] + waitWhenLeaving(waitCost, i - 1, j));
                }
                if (j > 0) {
                    best = Math.min(best, currentRow[j - 1] + waitWhenLeaving(waitCost, i, j - 1));
                }
                currentRow[j] = best + entryCost;
            }
            previousRow = currentRow;
        }
        return previousRow[n - 1];
    }

    /** The wait paid on leaving (i, j); leaving the start is free because second 1 is a move. */
    private long waitWhenLeaving(int[][] waitCost, int i, int j) {
        if (i == 0 && j == 0) {
            return 0;
        }
        return waitCost[i][j];
    }
}
