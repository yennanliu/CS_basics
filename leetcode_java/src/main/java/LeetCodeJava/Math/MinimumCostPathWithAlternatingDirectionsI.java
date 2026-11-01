package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-cost-path-with-alternating-directions-i/description/
// https://leetcode.ca/2025-07-28-3596-Minimum-Cost-Path-with-Alternating-Directions-I/

/**
 * 3596. Minimum Cost Path with Alternating Directions I
 * Medium
 *
 * You are given two integers m and n representing the number of rows and
 * columns of a grid, respectively.
 *
 * The cost to enter cell (i, j) is defined as (i + 1) * (j + 1).
 *
 * The path will always begin by entering cell (0, 0) on move 1 and paying the
 * entrance cost.
 *
 * At each step, you move to an adjacent cell, following an alternating
 * pattern:
 *
 * On odd-numbered moves, you must move either right or down.
 * On even-numbered moves, you must move either left or up.
 *
 * Return the minimum total cost required to reach (m - 1, n - 1). If it is
 * impossible, return -1.
 *
 *
 * Example 1:
 *
 * Input: m = 1, n = 1
 * Output: 1
 * Explanation:
 * You start at cell (0, 0).
 * The cost to enter (0, 0) is (0 + 1) * (0 + 1) = 1.
 * Since you're at the destination, the total cost is 1.
 *
 * Example 2:
 *
 * Input: m = 2, n = 1
 * Output: 3
 * Explanation:
 * You start at cell (0, 0) with cost (0 + 1) * (0 + 1) = 1.
 * Move 1 (odd): You can move down to (1, 0) with cost (1 + 1) * (0 + 1) = 2.
 * Thus, the total cost is 1 + 2 = 3.
 *
 *
 * Constraints:
 *
 * 1 <= m, n <= 10^6
 *
 */
public class MinimumCostPathWithAlternatingDirectionsI {

    // V0
    // IDEA: PARITY INVARIANT ON (row + col) -- ONLY THE 1x1, 2x1 AND 1x2 GRIDS ARE SOLVABLE
    //       an odd move (right/down) adds 1 to row + col, an even move (left/up) subtracts 1,
    //       so after any number of moves row + col is 0 or 1: we can only ever be at (0,0),
    //       (0,1) or (1,0). the target (m-1, n-1) is reachable only when (m-1) + (n-1) <= 1.
    //       1x1 costs the entrance 1; 2x1 / 1x2 cost 1 + 2 = 3 (one path, nothing to optimise).
    /**
     * time = O(1)
     * space = O(1)
     */
    public int minCost(int m, int n) {
        if (m == 1 && n == 1) {
            return 1;
        }
        if ((m == 2 && n == 1) || (m == 1 && n == 2)) {
            return 3;
        }
        return -1;
    }
}
