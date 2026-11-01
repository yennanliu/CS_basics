package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-cost-homecoming-of-a-robot-in-a-grid/description/

/**
 * 2087. Minimum Cost Homecoming of a Robot in a Grid
 * Medium
 *
 * There is an m x n grid, where (0, 0) is the top-left cell and (m - 1, n - 1) is the bottom-right cell.
 * You are given an integer array startPos where startPos = [startrow, startcol] indicates that initially, a
 * robot is at the cell (startrow, startcol). You are also given an integer array homePos where homePos =
 * [homerow, homecol] indicates that its home is at the cell (homerow, homecol).
 *
 * The robot needs to go to its home. It can move one cell in four directions: left, right, up, or down, and
 * it can not move outside the boundary. Every move incurs some cost. You are further given two 0-indexed
 * integer arrays: rowCosts of length m and colCosts of length n.
 *
 * If the robot moves up or down into a cell whose row is r, then this move costs rowCosts[r].
 * If the robot moves left or right into a cell whose column is c, then this move costs colCosts[c].
 *
 * Return the minimum total cost for this robot to return home.
 *
 *
 * Example 1:
 *
 * Input: startPos = [1, 0], homePos = [2, 3], rowCosts = [5, 4, 3], colCosts = [8, 2, 6, 7]
 * Output: 18
 *
 * Example 2:
 *
 * Input: startPos = [0, 0], homePos = [0, 0], rowCosts = [5], colCosts = [26]
 * Output: 0
 *
 *
 * Constraints:
 *
 * m == rowCosts.length
 * n == colCosts.length
 * 1 <= m, n <= 10^5
 * 0 <= rowCosts[r], colCosts[c] <= 10^4
 * startPos.length == 2
 * homePos.length == 2
 * 0 <= startrow, homerow < m
 * 0 <= startcol, homecol < n
 *
 */
public class MinimumCostHomecomingOfARobotInAGrid {

    // V0
    // IDEA: COST IS PATH-INDEPENDENT - SUM THE ROWS AND COLUMNS ENTERED ON A STRAIGHT WALK (NO SEARCH)
    /**
     * time = O(M + N)
     * space = O(1)
     */
    public int minCost(int[] startPos, int[] homePos, int[] rowCosts, int[] colCosts) {
        int startRow = startPos[0];
        int startCol = startPos[1];
        int homeRow = homePos[0];
        int homeCol = homePos[1];
        int cost = 0;

        // every row between start (exclusive) and home (inclusive) must be entered exactly once;
        // a detour only adds cost because every cost is >= 0
        int rowStep = homeRow > startRow ? 1 : -1;
        for (int r = startRow; r != homeRow; r += rowStep) {
            cost += rowCosts[r + rowStep];
        }

        // same for the columns
        int colStep = homeCol > startCol ? 1 : -1;
        for (int c = startCol; c != homeCol; c += colStep) {
            cost += colCosts[c + colStep];
        }
        return cost;
    }
}
