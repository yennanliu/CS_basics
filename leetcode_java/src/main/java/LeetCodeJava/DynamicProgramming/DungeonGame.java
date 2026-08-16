package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/dungeon-game/description/

import java.util.Arrays;

/**
 * 174. Dungeon Game
 * Hard
 *
 * The demons had captured the princess and imprisoned her in the bottom-right corner of
 * a dungeon. The dungeon consists of m x n rooms laid out in a 2D grid. Our valiant
 * knight was initially positioned in the top-left room and must fight his way through
 * dungeon to rescue the princess.
 *
 * The knight has an initial health point represented by a positive integer. If at any
 * point his health point drops to 0 or below, he dies immediately.
 *
 * Some of the rooms are guarded by demons (represented by negative integers), so the
 * knight loses health upon entering these rooms; other rooms are either empty
 * (represented as 0) or contain magic orbs that increase the knight's health
 * (represented by positive integers).
 *
 * To reach the princess as quickly as possible, the knight decides to move only rightward
 * or downward in each step.
 *
 * Return the knight's minimum initial health so that he can rescue the princess.
 *
 * Note that any room can contain threats or power-ups, even the first room the knight
 * enters and the bottom-right room where the princess is imprisoned.
 *
 *
 * Example 1:
 *
 * Input: dungeon = [[-2,-3,3],[-5,-10,1],[10,30,-5]]
 * Output: 7
 * Explanation: The initial health of the knight must be at least 7 if he follows the
 * optimal path: RIGHT -> RIGHT -> DOWN -> DOWN.
 *
 * Example 2:
 *
 * Input: dungeon = [[0]]
 * Output: 1
 *
 *
 * Constraints:
 *
 * m == dungeon.length
 * n == dungeon[i].length
 * 1 <= m, n <= 200
 * -1000 <= dungeon[i][j] <= 1000
 *
 */
public class DungeonGame {

    // V0
    // IDEA: 2D DP, FILLED BACKWARD (bottom-right -> top-left)
    /**
     *  WHY BACKWARD? Going FORWARD we'd need to track BOTH `max health so far` and
     *  `min health needed`, which are NOT independently optimizable. Going BACKWARD
     *  there is a SINGLE quantity to minimize.
     *
     *  DP def:
     *    - dp[i][j] = minimum health required when ENTERING room (i, j)
     *                 so the knight can reach the princess alive
     *
     *  DP eq:
     *    - dp[i][j] = max(1, min(dp[i+1][j], dp[i][j+1]) - dungeon[i][j])
     *      (health must stay >= 1 at EVERY step -> hence the max(1, ...))
     *
     *  Init:
     *    - dp[m][n-1] = dp[m-1][n] = 1   (virtual cells just PAST the princess)
     *    - every other out-of-range cell = INFINITY, so min() ignores it
     *
     *  time  = O(m * n)
     *  space = O(m * n)
     */
    public int calculateMinimumHP(int[][] dungeon) {
        if (dungeon == null || dungeon.length == 0 || dungeon[0].length == 0) {
            return 1;
        }

        int m = dungeon.length;
        int n = dungeon[0].length;
        final int INF = Integer.MAX_VALUE;

        // (m+1) x (n+1) so dp[i+1][j] / dp[i][j+1] are ALWAYS in range
        int[][] dp = new int[m + 1][n + 1];
        for (int[] row : dp) {
            Arrays.fill(row, INF);
        }
        dp[m][n - 1] = 1;
        dp[m - 1][n] = 1;

        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                int need = Math.min(dp[i + 1][j], dp[i][j + 1]) - dungeon[i][j];
                dp[i][j] = Math.max(1, need);
            }
        }

        return dp[0][0];
    }


    // V1
    // IDEA: 1D ROLLING ARRAY (one row instead of the whole grid)
    /**
     *  dp[i][j] only ever reads dp[i+1][j] and dp[i][j+1], so a single row that is
     *  overwritten right to left is enough.
     *
     *  O(n) memory instead of O(mn) -- the standard compression once the 2D version
     *  is understood.
     *
     *  time  = O(m * n)
     *  space = O(n)
     */
    public int calculateMinimumHP_1(int[][] dungeon) {
        int m = dungeon.length;
        int n = dungeon[0].length;
        final int INF = Integer.MAX_VALUE;

        int[] dp = new int[n + 1];
        Arrays.fill(dp, INF);
        dp[n - 1] = 1;   // the virtual cell just past the princess

        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                int down = dp[j];                     // still the row below
                int right = (j + 1 <= n - 1) ? dp[j + 1] : INF;
                if (i == m - 1 && j == n - 1) {
                    down = 1;
                    right = 1;
                }
                dp[j] = Math.max(1, Math.min(down, right) - dungeon[i][j]);
            }
            dp[n] = INF;
        }
        return dp[0];
    }

    // V2
    // IDEA: TOP-DOWN MEMOISED RECURSION
    /**
     *  need(i, j) = max(1, min(need(i+1, j), need(i, j+1)) - dungeon[i][j])
     *
     *  Reads as the definition, and only the cells on some optimal path family are
     *  ever computed -- the natural first version before rolling it into a loop.
     *
     *  time  = O(m * n)
     *  space = O(m * n)
     */
    private Integer[][] memoHp;

    public int calculateMinimumHP_2(int[][] dungeon) {
        memoHp = new Integer[dungeon.length][dungeon[0].length];
        return needAt(dungeon, 0, 0);
    }

    private int needAt(int[][] d, int i, int j) {
        int m = d.length;
        int n = d[0].length;
        if (i == m - 1 && j == n - 1) {
            return Math.max(1, 1 - d[i][j]);
        }
        if (memoHp[i][j] != null) {
            return memoHp[i][j];
        }
        int best = Integer.MAX_VALUE;
        if (i + 1 < m) {
            best = Math.min(best, needAt(d, i + 1, j));
        }
        if (j + 1 < n) {
            best = Math.min(best, needAt(d, i, j + 1));
        }
        int res = Math.max(1, best - d[i][j]);
        memoHp[i][j] = res;
        return res;
    }

    // V3
    // IDEA: BINARY SEARCH THE STARTING HEALTH + a forward feasibility sweep
    /**
     *  `can the knight survive starting with h health?` is MONOTONE in h, so binary
     *  search h and check with a FORWARD DP that propagates the best surviving
     *  health per cell (or -infinity when unreachable).
     *
     *  Answers the question in the direction the story is told (start to princess)
     *  rather than backwards, at the cost of a log factor.
     *
     *  time  = O(m * n * log(maxHealth))
     *  space = O(m * n)
     */
    public int calculateMinimumHP_3(int[][] dungeon) {
        int m = dungeon.length;
        int n = dungeon[0].length;

        int lo = 1;
        int hi = 1;
        for (int[] row : dungeon) {
            for (int v : row) {
                if (v < 0) {
                    hi -= v;    // the worst case: pay for every demon
                }
            }
        }

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (survives(dungeon, mid)) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }

    /** best health reachable at each cell; -1 means `died on the way` */
    private boolean survives(int[][] d, int start) {
        int m = d.length;
        int n = d[0].length;
        int[][] best = new int[m][n];
        for (int[] row : best) {
            Arrays.fill(row, -1);
        }

        best[0][0] = start + d[0][0];
        if (best[0][0] <= 0) {
            return false;
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int from = -1;
                if (i > 0) {
                    from = Math.max(from, best[i - 1][j]);
                }
                if (j > 0) {
                    from = Math.max(from, best[i][j - 1]);
                }
                if (from <= 0) {
                    continue;   // unreachable alive
                }
                int here = from + d[i][j];
                best[i][j] = here > 0 ? here : -1;
            }
        }
        return best[m - 1][n - 1] > 0;
    }

}
