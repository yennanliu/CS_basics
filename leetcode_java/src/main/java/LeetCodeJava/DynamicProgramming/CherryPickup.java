package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/cherry-pickup/description/

import java.util.Arrays;

/**
 * 741. Cherry Pickup
 * Hard
 *
 * You are given an n x n grid representing a field of cherries, each cell is one of
 * three possible integers.
 *
 * 0 means the cell is empty, so you can pass through,
 * 1 means the cell contains a cherry that you can pick up and pass through, or
 * -1 means the cell contains a thorn that blocks your way.
 *
 * Return the maximum number of cherries you can collect by following the rules below:
 *
 * Starting at the position (0, 0) and reaching (n - 1, n - 1) by moving right or down
 * through valid path cells (cells with value 0 or 1).
 * After reaching (n - 1, n - 1), returning to (0, 0) by moving left or up through valid
 * path cells.
 * When passing through a path cell containing a cherry, you pick it up, and the cell
 * becomes an empty cell 0.
 * If there is no valid path between (0, 0) and (n - 1, n - 1), then no cherries can be
 * collected.
 *
 * Example 1:
 *
 * Input: grid = [[0,1,-1],[1,0,-1],[1,1,1]]
 * Output: 5
 * Explanation: The player started at (0, 0) and went down, down, right right to reach
 * (2, 2).
 * 4 cherries were picked up during this single trip, and the matrix becomes
 * [[0,1,-1],[0,0,-1],[0,0,0]].
 * Then, the player went left, up, up, left to return home, picking up one more cherry.
 * The total number of cherries picked up is 5, and this is the maximum possible.
 *
 * Example 2:
 *
 * Input: grid = [[1,1,-1],[1,-1,1],[-1,1,1]]
 * Output: 0
 *
 * Constraints:
 *
 * n == grid.length
 * n == grid[i].length
 * 1 <= n <= 50
 * grid[i][j] is -1, 0, or 1.
 * grid[0][0] != -1
 * grid[n - 1][n - 1] != -1
 *
 */
public class CherryPickup {

    // V0
    // IDEA: 2 WALKERS DP (round trip == two simultaneous down/right walks)
    /**
     *   KEY REFRAMING: going there AND coming back is the SAME as sending TWO walkers
     *   from (0,0) to (n-1,n-1), both only moving DOWN/RIGHT. If both step at the same
     *   pace, after k steps a walker at row r is at column k - r.
     *
     *   DP def:
     *     - dp[r1][r2] = max cherries collected after k steps, walker 1 on row r1,
     *                    walker 2 on row r2 (columns IMPLIED by k)
     *
     *   DP eq (4 predecessor combos: each walker came from UP or from LEFT):
     *     - dpK[r1][r2] = max(dpPrev[r1-1][r2-1], dpPrev[r1-1][r2],
     *                         dpPrev[r1][r2-1],   dpPrev[r1][r2]) + gain
     *
     *   NOTE !!! `gain` counts the cell ONCE when both walkers are on the SAME cell
     *            -- a cherry can only be picked up once.
     *
     *   NEG marks unreachable states; a blocked or unreachable end means 0.
     *
     *   time  = O(n^3)
     *   space = O(n^2)
     */
    public int cherryPickup(int[][] grid) {
        int n = grid.length;
        final int NEG = Integer.MIN_VALUE;

        int[][] dp = new int[n][n];
        for (int[] row : dp) {
            Arrays.fill(row, NEG);
        }
        dp[0][0] = grid[0][0];

        // k = number of steps taken; both walkers are on the anti-diagonal r + c == k
        for (int k = 1; k <= 2 * n - 2; k++) {
            int[][] ndp = new int[n][n];
            for (int[] row : ndp) {
                Arrays.fill(row, NEG);
            }

            int lo = Math.max(0, k - n + 1); // smallest row with a valid column
            int hi = Math.min(n - 1, k);     // largest row with a valid column

            for (int r1 = lo; r1 <= hi; r1++) {
                int c1 = k - r1;
                if (grid[r1][c1] == -1) {
                    continue;
                }
                for (int r2 = lo; r2 <= hi; r2++) {
                    int c2 = k - r2;
                    if (grid[r2][c2] == -1) {
                        continue;
                    }

                    int best = NEG;
                    for (int pr1 = r1 - 1; pr1 <= r1; pr1++) {
                        for (int pr2 = r2 - 1; pr2 <= r2; pr2++) {
                            if (pr1 >= 0 && pr2 >= 0 && dp[pr1][pr2] > best) {
                                best = dp[pr1][pr2];
                            }
                        }
                    }
                    if (best == NEG) {
                        continue; // unreachable state
                    }

                    int gain = grid[r1][c1];
                    if (r1 != r2) { // SAME cell -> count the cherry only once
                        gain += grid[r2][c2];
                    }
                    ndp[r1][r2] = best + gain;
                }
            }

            dp = ndp;
        }

        // if (n-1, n-1) was never reached, dp holds NEG -> no cherries
        return Math.max(0, dp[n - 1][n - 1]);
    }


    // V1
    // IDEA: 3D DP indexed by (step, row1, row2) -- the full table
    /**
     *  The same two-walker reframing, but with the step index kept explicitly
     *  instead of rolled into two layers.
     *
     *  O(n^3) memory rather than O(n^2), yet the whole trajectory is recoverable
     *  afterwards -- which the rolling version throws away.
     *
     *  time  = O(n^3)
     *  space = O(n^3)
     */
    public int cherryPickup_1(int[][] grid) {
        int n = grid.length;
        final int NEG = Integer.MIN_VALUE / 2;
        int steps = 2 * n - 1;

        int[][][] dp = new int[steps][n][n];
        for (int[][] layer : dp) {
            for (int[] row : layer) {
                Arrays.fill(row, NEG);
            }
        }
        dp[0][0][0] = grid[0][0];

        for (int k = 1; k < steps; k++) {
            for (int r1 = Math.max(0, k - n + 1); r1 <= Math.min(n - 1, k); r1++) {
                int c1 = k - r1;
                if (grid[r1][c1] == -1) {
                    continue;
                }
                for (int r2 = Math.max(0, k - n + 1); r2 <= Math.min(n - 1, k); r2++) {
                    int c2 = k - r2;
                    if (grid[r2][c2] == -1) {
                        continue;
                    }
                    int best = NEG;
                    for (int p1 = r1 - 1; p1 <= r1; p1++) {
                        for (int p2 = r2 - 1; p2 <= r2; p2++) {
                            if (p1 >= 0 && p2 >= 0) {
                                best = Math.max(best, dp[k - 1][p1][p2]);
                            }
                        }
                    }
                    if (best == NEG) {
                        continue;
                    }
                    int gain = grid[r1][c1] + (r1 != r2 ? grid[r2][c2] : 0);
                    dp[k][r1][r2] = best + gain;
                }
            }
        }
        return Math.max(0, dp[steps - 1][n - 1][n - 1]);
    }

    // V2
    // IDEA: TOP-DOWN MEMOISED RECURSION on (r1, c1, r2)
    /**
     *  Because both walkers take the same number of steps, c2 is determined by
     *  r1 + c1 - r2 -- so three indices suffice.
     *
     *  Reads as `walk both forward and take the best of the four move combinations`,
     *  which is much closer to the reframing than the layered sweep.
     *
     *  time  = O(n^3)
     *  space = O(n^3)
     */
    private Integer[][][] memoCh;

    public int cherryPickup_2(int[][] grid) {
        int n = grid.length;
        memoCh = new Integer[n][n][n];
        int res = walk(grid, 0, 0, 0, n);
        return Math.max(0, res);
    }

    private int walk(int[][] grid, int r1, int c1, int r2, int n) {
        int c2 = r1 + c1 - r2;
        if (r1 >= n || c1 >= n || r2 >= n || c2 >= n
                || grid[r1][c1] == -1 || grid[r2][c2] == -1) {
            return Integer.MIN_VALUE / 2;
        }
        if (r1 == n - 1 && c1 == n - 1) {
            return grid[r1][c1];
        }
        if (memoCh[r1][c1][r2] != null) {
            return memoCh[r1][c1][r2];
        }

        int gain = grid[r1][c1] + (r1 != r2 ? grid[r2][c2] : 0);
        int best = Integer.MIN_VALUE / 2;
        best = Math.max(best, walk(grid, r1 + 1, c1, r2 + 1, n)); // both down
        best = Math.max(best, walk(grid, r1 + 1, c1, r2, n));     // down / right
        best = Math.max(best, walk(grid, r1, c1 + 1, r2 + 1, n)); // right / down
        best = Math.max(best, walk(grid, r1, c1 + 1, r2, n));     // both right

        int res = best <= Integer.MIN_VALUE / 4 ? Integer.MIN_VALUE / 2 : gain + best;
        memoCh[r1][c1][r2] = res;
        return res;
    }

    // V3
    // IDEA: ORDERED WALKERS (r1 <= r2) -- halve the state space
    /**
     *  The two walkers are INTERCHANGEABLE, so (r1, r2) and (r2, r1) describe the
     *  same situation. Forcing r1 <= r2 removes that symmetry and cuts the states
     *  (and the transitions) roughly in half.
     *
     *  NOTE !!! after a move the pair can come out unordered, so each transition
     *           normalises with a swap before indexing.
     *
     *  time  = O(n^3) with half the constant
     *  space = O(n^2)
     */
    public int cherryPickup_3(int[][] grid) {
        int n = grid.length;
        final int NEG = Integer.MIN_VALUE / 2;

        int[][] dp = new int[n][n];
        for (int[] row : dp) {
            Arrays.fill(row, NEG);
        }
        dp[0][0] = grid[0][0];

        for (int k = 1; k <= 2 * n - 2; k++) {
            int[][] ndp = new int[n][n];
            for (int[] row : ndp) {
                Arrays.fill(row, NEG);
            }
            int lo = Math.max(0, k - n + 1);
            int hi = Math.min(n - 1, k);

            for (int r1 = lo; r1 <= hi; r1++) {
                int c1 = k - r1;
                if (grid[r1][c1] == -1) {
                    continue;
                }
                // ONLY r2 >= r1 -- the symmetric half is redundant
                for (int r2 = r1; r2 <= hi; r2++) {
                    int c2 = k - r2;
                    if (grid[r2][c2] == -1) {
                        continue;
                    }
                    int best = NEG;
                    for (int p1 = r1 - 1; p1 <= r1; p1++) {
                        for (int p2 = r2 - 1; p2 <= r2; p2++) {
                            if (p1 < 0 || p2 < 0) {
                                continue;
                            }
                            // normalise the predecessor pair back into r1 <= r2
                            int a = Math.min(p1, p2);
                            int b = Math.max(p1, p2);
                            best = Math.max(best, dp[a][b]);
                        }
                    }
                    if (best == NEG) {
                        continue;
                    }
                    int gain = grid[r1][c1] + (r1 != r2 ? grid[r2][c2] : 0);
                    ndp[r1][r2] = best + gain;
                }
            }
            dp = ndp;
        }

        return Math.max(0, dp[n - 1][n - 1]);
    }

}
