package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-number-of-lines-to-cover-points/description/

import java.util.*;

/**
 * 2152. Minimum Number of Lines to Cover Points
 * Medium
 *
 * You are given an array points where points[i] = [xi, yi] represents a point on an X-Y plane.
 *
 * Straight lines are going to be added to the X-Y plane, such that every point is covered by at least one line.
 *
 * Return the minimum number of straight lines needed to cover all the points.
 *
 *
 * Example 1:
 *
 * Input: points = [[0,1],[2,3],[4,5],[4,3]]
 * Output: 2
 * Explanation: The minimum number of straight lines needed is two. One possible solution is to add:
 * - One line connecting the point at (0, 1) to the point at (4, 5).
 * - Another line connecting the point at (2, 3) to the point at (4, 3).
 *
 * Example 2:
 *
 * Input: points = [[0,2],[-2,-2],[1,4]]
 * Output: 1
 * Explanation: The minimum number of straight lines needed is one. The only solution is to add:
 * - One line connecting the point at (-2, -2) to the point at (1, 4).
 *
 *
 * Constraints:
 *
 * 1 <= points.length <= 10
 * points[i].length == 2
 * -100 <= xi, yi <= 100
 * All the points are unique.
 *
 */
public class MinimumNumberOfLinesToCoverPoints {

    // V0
    // IDEA: BITMASK DP OVER UNCOVERED POINTS (n <= 10)
    //  line[i][j] = mask of every point collinear with points i and j (cross product, exact).
    //  dp(mask) = fewest lines covering mask; the lowest uncovered point i is either alone
    //  (1 line) or shares a line with some other uncovered j.
    /**
     * time = O(2^N * N)
     * space = O(2^N + N^2)
     */
    public int minimumLines(int[][] points) {
        int n = points.length;
        if (n <= 2) {
            return 1;
        }

        int[][] line = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int mask = 0;
                for (int k = 0; k < n; k++) {
                    if (isCollinear(points[i], points[j], points[k])) {
                        mask |= 1 << k;
                    }
                }
                line[i][j] = mask;
                line[j][i] = mask;
            }
        }

        // memo[mask] = answer for that set of uncovered points, -1 = not computed
        int[] memo = new int[1 << n];
        Arrays.fill(memo, -1);
        return dp((1 << n) - 1, n, line, memo);
    }

    /** Fewest lines that cover every point whose bit is set in mask. */
    private int dp(int mask, int n, int[][] line, int[] memo) {
        if (mask == 0) {
            return 0;
        }
        if (memo[mask] != -1) {
            return memo[mask];
        }

        // lowest uncovered point: it must be covered, fixing it avoids counting permutations
        int first = Integer.numberOfTrailingZeros(mask);
        int rest = mask & ~(1 << first);

        // option 1: a line through `first` alone
        int best = 1 + dp(rest, n, line, memo);
        // option 2: a line through `first` and another uncovered point j (covers all collinear points)
        for (int j = 0; j < n; j++) {
            if ((rest & (1 << j)) != 0) {
                best = Math.min(best, 1 + dp(mask & ~line[first][j], n, line, memo));
            }
        }
        memo[mask] = best;
        return best;
    }

    /** True if c lies on the line through a and b (cross product of ab and ac is zero). */
    private boolean isCollinear(int[] a, int[] b, int[] c) {
        return (b[0] - a[0]) * (c[1] - a[1]) == (b[1] - a[1]) * (c[0] - a[0]);
    }
}
