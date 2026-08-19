package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/largest-plus-sign/

import java.util.HashSet;
import java.util.Set;

/**
 *  764. Largest Plus Sign
 *  Medium
 *
 *  You are given an integer n. You have an n x n binary grid with all values
 *  initially 1's except for some indices given in the array mines. The i-th
 *  element of the array mines is defined as mines[i] = [xi, yi] where
 *  grid[xi][yi] == 0.
 *
 *  Return the order of the largest axis-aligned plus sign of 1's contained in
 *  grid. If there is none, return 0.
 *
 *  An axis-aligned plus sign of 1's of order k has some center grid[r][c] == 1
 *  along with four arms of length k - 1 going up, down, left, and right, and
 *  made of 1's. Note that there could be 0's or 1's beyond the arms of the plus
 *  sign, only the relevant area of the plus sign is checked for 1's.
 *
 *  Example 1:
 *    Input: n = 5, mines = [[4,2]]
 *    Output: 2
 *
 *  Example 2:
 *    Input: n = 1, mines = [[0,0]]
 *    Output: 0
 *
 *  Constraints:
 *    - 1 <= n <= 500
 *    - 1 <= mines.length <= 5000
 *    - 0 <= xi, yi < n
 *    - All the pairs (xi, yi) are unique.
 */
public class LargestPlusSign {

    // V0
    // IDEA: DP - for each cell keep the MIN consecutive 1's run in the 4 directions
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int orderOfLargestPlusSign(int n, int[][] mines) {
        Set<Integer> banned = new HashSet<>();
        if (mines != null) {
            for (int[] m : mines) {
                banned.add(m[0] * n + m[1]);
            }
        }

        int[][] dp = new int[n][n];

        // left -> right, then right -> left
        for (int r = 0; r < n; r++) {
            int count = 0;
            for (int c = 0; c < n; c++) {
                count = banned.contains(r * n + c) ? 0 : count + 1;
                dp[r][c] = count;
            }
            count = 0;
            for (int c = n - 1; c >= 0; c--) {
                count = banned.contains(r * n + c) ? 0 : count + 1;
                dp[r][c] = Math.min(dp[r][c], count);
            }
        }

        // top -> bottom, then bottom -> top (and collect the answer)
        int res = 0;
        for (int c = 0; c < n; c++) {
            int count = 0;
            for (int r = 0; r < n; r++) {
                count = banned.contains(r * n + c) ? 0 : count + 1;
                dp[r][c] = Math.min(dp[r][c], count);
            }
            count = 0;
            for (int r = n - 1; r >= 0; r--) {
                count = banned.contains(r * n + c) ? 0 : count + 1;
                dp[r][c] = Math.min(dp[r][c], count);
                res = Math.max(res, dp[r][c]);
            }
        }
        return res;
    }
}
