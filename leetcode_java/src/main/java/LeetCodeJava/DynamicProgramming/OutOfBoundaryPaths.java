package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/out-of-boundary-paths/

/**
 *  576. Out of Boundary Paths
 *  Medium
 *
 *  There is an m x n grid with a ball. The ball is initially at the position
 *  [startRow, startColumn]. You are allowed to move the ball to one of the four
 *  adjacent cells in the grid (possibly out of the grid crossing the grid
 *  boundary). You can apply at most maxMove moves to the ball.
 *
 *  Given the five integers m, n, maxMove, startRow, startColumn, return the
 *  number of paths to move the ball out of the grid boundary. Since the answer
 *  can be very large, return it modulo 10^9 + 7.
 *
 *  Example 1:
 *
 *  Input: m = 2, n = 2, maxMove = 2, startRow = 0, startColumn = 0
 *  Output: 6
 *
 *  Example 2:
 *
 *  Input: m = 1, n = 3, maxMove = 3, startRow = 0, startColumn = 1
 *  Output: 12
 *
 *  Constraints:
 *
 *  1 <= m, n <= 50
 *  0 <= maxMove <= 50
 *  0 <= startRow < m
 *  0 <= startColumn < n
 */
public class OutOfBoundaryPaths {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DP over move budget
    //  dp[s][x][y] = number of ways to get out of the grid from (x,y) within s moves
    //  a step that leaves the grid counts as 1 path
    /**
     * time = O(maxMove * m * n)
     * space = O(m * n)  // rolling 2 layers
     */
    public int findPaths(int m, int n, int maxMove, int startRow, int startColumn) {
        if (maxMove == 0) {
            return 0;
        }
        int[][] prev = new int[m][n]; // dp for s - 1 moves
        int[][] cur = new int[m][n];

        for (int s = 1; s <= maxMove; s++) {
            for (int x = 0; x < m; x++) {
                for (int y = 0; y < n; y++) {
                    long v = 0;
                    v += (x == 0) ? 1 : prev[x - 1][y];        // up
                    v += (x == m - 1) ? 1 : prev[x + 1][y];    // down
                    v += (y == 0) ? 1 : prev[x][y - 1];        // left
                    v += (y == n - 1) ? 1 : prev[x][y + 1];    // right
                    cur[x][y] = (int) (v % MOD);
                }
            }
            int[][] tmp = prev;
            prev = cur;
            cur = tmp;
        }
        return prev[startRow][startColumn];
    }

    // V1
    // IDEA: TOP-DOWN DFS + MEMO
    /**
     * time = O(maxMove * m * n)
     * space = O(maxMove * m * n)
     */
    public int findPaths_1(int m, int n, int maxMove, int startRow, int startColumn) {
        Integer[][][] memo = new Integer[maxMove + 1][m][n];
        return dfs(m, n, maxMove, startRow, startColumn, memo);
    }

    private int dfs(int m, int n, int moveLeft, int x, int y, Integer[][][] memo) {
        if (x < 0 || x >= m || y < 0 || y >= n) {
            return 1; // got out
        }
        if (moveLeft == 0) {
            return 0;
        }
        if (memo[moveLeft][x][y] != null) {
            return memo[moveLeft][x][y];
        }
        long res = 0;
        res += dfs(m, n, moveLeft - 1, x - 1, y, memo);
        res += dfs(m, n, moveLeft - 1, x + 1, y, memo);
        res += dfs(m, n, moveLeft - 1, x, y - 1, memo);
        res += dfs(m, n, moveLeft - 1, x, y + 1, memo);
        memo[moveLeft][x][y] = (int) (res % MOD);
        return memo[moveLeft][x][y];
    }

    // V2
    // IDEA: FORWARD PROPAGATION - instead of asking "how many ways out from every cell"
    //       (V0 / V1), push the ball's ways FORWARD from the start cell one move at a
    //       time; every step that leaves the grid is harvested into the answer directly
    /**
     * time = O(maxMove * m * n)
     * space = O(m * n)
     */
    public int findPaths_2(int m, int n, int maxMove, int startRow, int startColumn) {
        long res = 0;
        long[][] cur = new long[m][n];
        cur[startRow][startColumn] = 1;
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        for (int s = 0; s < maxMove; s++) {
            long[][] nxt = new long[m][n];
            for (int x = 0; x < m; x++) {
                for (int y = 0; y < n; y++) {
                    long ways = cur[x][y];
                    if (ways == 0) {
                        continue;
                    }
                    for (int[] dir : dirs) {
                        int nx = x + dir[0];
                        int ny = y + dir[1];
                        if (nx < 0 || nx >= m || ny < 0 || ny >= n) {
                            res = (res + ways) % MOD;
                        } else {
                            nxt[nx][ny] = (nxt[nx][ny] + ways) % MOD;
                        }
                    }
                }
            }
            cur = nxt;
        }
        return (int) res;
    }
}
