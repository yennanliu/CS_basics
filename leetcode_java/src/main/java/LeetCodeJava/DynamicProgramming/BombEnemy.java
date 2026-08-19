package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/bomb-enemy/

/**
 *  361. Bomb Enemy
 *  Medium
 *
 *  Given an m x n matrix grid where each cell is either a wall 'W',
 *  an enemy 'E' or empty '0', return the maximum enemies you can kill
 *  using one bomb. You can only place the bomb in an empty cell.
 *
 *  The bomb kills all the enemies in the same row and column from the
 *  planted point until it hits the wall since it is too strong to be destroyed.
 *
 *  Example 1:
 *
 *  Input: grid = [["0","E","0","0"],["E","0","W","E"],["0","E","0","0"]]
 *  Output: 3
 *
 *  Example 2:
 *
 *  Input: grid = [["W","W","W"],["0","0","0"],["E","E","E"]]
 *  Output: 1
 *
 *  Constraints:
 *
 *  m == grid.length
 *  n == grid[i].length
 *  1 <= m, n <= 500
 *  grid[i][j] is either 'W', 'E', or '0'.
 */
public class BombEnemy {

    // V0
    // IDEA: DP - reuse the row / column enemy count until a wall is hit
    /**
     * time = O(m * n)
     * space = O(n)
     */
    public int maxKilledEnemies(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        int m = grid.length;
        int n = grid[0].length;

        int rowHits = 0;
        int[] colHits = new int[n];
        int res = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                // recompute row count when at start of row or right after a wall
                if (j == 0 || grid[i][j - 1] == 'W') {
                    rowHits = 0;
                    for (int k = j; k < n && grid[i][k] != 'W'; k++) {
                        if (grid[i][k] == 'E') {
                            rowHits++;
                        }
                    }
                }

                // recompute column count when at top of column or right below a wall
                if (i == 0 || grid[i - 1][j] == 'W') {
                    colHits[j] = 0;
                    for (int k = i; k < m && grid[k][j] != 'W'; k++) {
                        if (grid[k][j] == 'E') {
                            colHits[j]++;
                        }
                    }
                }

                if (grid[i][j] == '0') {
                    res = Math.max(res, rowHits + colHits[j]);
                }
            }
        }
        return res;
    }

    // V1
    // IDEA: BRUTE FORCE - scan 4 directions from every empty cell
    /**
     * time = O(m * n * (m + n))
     * space = O(1)
     */
    public int maxKilledEnemies_1(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        int m = grid.length;
        int n = grid[0].length;
        int res = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] != '0') {
                    continue;
                }
                int cnt = 0;
                for (int k = i - 1; k >= 0 && grid[k][j] != 'W'; k--) {
                    if (grid[k][j] == 'E') cnt++;
                }
                for (int k = i + 1; k < m && grid[k][j] != 'W'; k++) {
                    if (grid[k][j] == 'E') cnt++;
                }
                for (int k = j - 1; k >= 0 && grid[i][k] != 'W'; k--) {
                    if (grid[i][k] == 'E') cnt++;
                }
                for (int k = j + 1; k < n && grid[i][k] != 'W'; k++) {
                    if (grid[i][k] == 'E') cnt++;
                }
                res = Math.max(res, cnt);
            }
        }
        return res;
    }
}
