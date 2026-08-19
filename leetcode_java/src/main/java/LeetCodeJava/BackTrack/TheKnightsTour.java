package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/the-knights-tour/

/**
 *  2664. The Knight's Tour
 *  Medium
 *
 *  Given two positive integers m and n which are the height and width of a
 *  0-indexed 2D-array board, a pair of positive integers (r, c) which is the
 *  starting position of the knight on the board.
 *
 *  Your task is to find an order of movements for the knight, in a manner that every
 *  cell of the board gets visited exactly once (the starting cell is considered
 *  visited and you shouldn't visit it again).
 *
 *  Return the array board in which the cells' values show the order of visiting the
 *  cell starting from 0 (the initial place of the knight).
 *
 *  Note that a knight can move from cell (r1, c1) to cell (r2, c2) if
 *  0 <= r2 <= m - 1 and 0 <= c2 <= n - 1 and min(|r1-r2|, |c1-c2|) = 1 and
 *  max(|r1-r2|, |c1-c2|) = 2.
 *
 *  Example 1:
 *    Input: m = 1, n = 1, r = 0, c = 0
 *    Output: [[0]]
 *
 *  Example 2:
 *    Input: m = 3, n = 4, r = 0, c = 0
 *    Output: [[0,3,6,9],[11,8,1,4],[2,5,10,7]]
 *
 *  Constraints:
 *    1 <= m, n <= 5
 *    0 <= r <= m - 1
 *    0 <= c <= n - 1
 *    The inputs will be generated such that there exists at least one possible order
 *    of movements with the given condition.
 */
public class TheKnightsTour {

    private static final int[][] DIRS = { { -2, -1 }, { -1, 2 }, { 2, 1 }, { 1, -2 },
            { -2, 1 }, { 1, 2 }, { 2, -1 }, { -1, -2 } };

    private int m;
    private int n;
    private int last;
    private int[][] g;
    private boolean done;

    // V0
    // IDEA: BACKTRACKING (DFS) OVER THE 8 KNIGHT MOVES
    //       the board is at most 5 x 5 = 25 cells, so a plain "try every move, undo
    //       on failure" DFS is affordable -- the "visit each cell exactly once" rule
    //       prunes almost all of the tree immediately.
    //
    //       g[i][j] holds the step number at which the knight lands on (i, j), or -1
    //       when the cell is still unvisited. That single array doubles as the answer
    //       AND as the visited marker -- no separate seen set needed.
    //
    //       NOTE : the `done` flag is what freezes the board. Without it the
    //              unwinding would erase the very tour we just found.
    //       NOTE : the termination test is on the STEP NUMBER (m*n - 1), which works
    //              because step numbers are assigned consecutively.
    //       NOTE : m*n == 1 is handled for free -> [[0]].
    /**
     * time = O(8^(m*n)) worst case (tiny in practice)
     * space = O(m*n)
     */
    public int[][] tourOfKnight(int m, int n, int r, int c) {
        this.m = m;
        this.n = n;
        this.last = m * n - 1;
        this.g = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                g[i][j] = -1;
            }
        }
        g[r][c] = 0;
        this.done = false;
        dfs(r, c);
        return g;
    }

    private void dfs(int i, int j) {
        if (g[i][j] == last) {
            done = true;
            return;
        }
        for (int[] d : DIRS) {
            int x = i + d[0];
            int y = j + d[1];
            if (x >= 0 && x < m && y >= 0 && y < n && g[x][y] == -1) {
                g[x][y] = g[i][j] + 1;
                dfs(x, y);
                if (done) {
                    return;
                }
                g[x][y] = -1;
            }
        }
    }
}
