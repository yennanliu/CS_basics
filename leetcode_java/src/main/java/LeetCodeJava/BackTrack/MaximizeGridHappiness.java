package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/maximize-grid-happiness/

/**
 *  1659. Maximize Grid Happiness
 *  Hard
 *
 *  You are given four integers, m, n, introvertsCount, and extrovertsCount.
 *  You have an m x n grid, and there are two types of people: introverts and
 *  extroverts. There are introvertsCount introverts and extrovertsCount
 *  extroverts. You should decide how many people you want to live in the grid
 *  and assign each of them one grid cell. Note that you do not have to have all
 *  the people living in the grid.
 *
 *  The happiness of each person is calculated as follows:
 *    - Introverts start with 120 happiness and lose 30 happiness for each
 *      neighbor (introvert or extrovert).
 *    - Extroverts start with 40 happiness and gain 20 happiness for each
 *      neighbor (introvert or extrovert).
 *
 *  Neighbors live in the directly adjacent cells north, east, south and west.
 *  The grid happiness is the sum of each person's happiness. Return the maximum
 *  possible grid happiness.
 *
 *  Example 1:
 *    Input: m = 2, n = 3, introvertsCount = 1, extrovertsCount = 2
 *    Output: 240
 *    Explanation: introvert at (1,1) -> 120, extroverts at (1,3) and (2,3)
 *                 -> 60 + 60. total = 240
 *
 *  Example 2:
 *    Input: m = 3, n = 1, introvertsCount = 2, extrovertsCount = 1
 *    Output: 260
 *
 *  Constraints:
 *    1 <= m, n <= 5
 *    0 <= introvertsCount, extrovertsCount <= min(m * n, 6)
 */
public class MaximizeGridHappiness {

    // V0
    // IDEA: PROFILE (BROKEN-PROFILE) DP + MEMO, cell by cell, base-3 mask
    //       fill cells in row-major order. when placing cell `pos` we only need
    //       the last n placed cells -> keep them as a base-3 number `mask`
    //         digit 0   = cell pos-1 (the LEFT neighbour, if same row)
    //         digit n-1 = cell pos-n (the UP   neighbour, if not first row)
    //       digit value : 0 = empty, 1 = introvert, 2 = extrovert.
    //       score the PAIR when the second member is placed, so each edge is
    //       counted exactly once:
    //         h[1][1] = -60, h[1][2] = h[2][1] = -10, h[2][2] = +40
    //       plus the base 120 / 40 for the person just placed.
    //       NOTE: people are optional -> "leave empty" (cur = 0) is always a
    //             candidate.
    //       NOTE: shifting the window drops the oldest digit
    //             -> mask % 3^(n-1), then * 3 + cur.
    /**
     * time = O(m*n * 3^n * I * E * 3)
     * space = O(m*n * 3^n * I * E)
     */
    private static final int[][] H = {
            {0, 0, 0},
            {0, -60, -10},
            {0, -10, 40}
    };
    private static final int[] BASE = {0, 120, 40};

    private int n;
    private int total;
    private int pw;
    private int[][][][] memo;

    public int getMaxGridHappiness(int m, int n, int introvertsCount, int extrovertsCount) {
        this.n = n;
        this.total = m * n;
        this.pw = (int) Math.pow(3, n - 1);
        int maskSize = this.pw * 3;
        this.memo = new int[total + 1][maskSize][introvertsCount + 1][extrovertsCount + 1];
        for (int a = 0; a <= total; a++) {
            for (int b = 0; b < maskSize; b++) {
                for (int c = 0; c <= introvertsCount; c++) {
                    for (int d = 0; d <= extrovertsCount; d++) {
                        this.memo[a][b][c][d] = -1;
                    }
                }
            }
        }
        return dfs(0, 0, introvertsCount, extrovertsCount);
    }

    private int dfs(int pos, int mask, int ic, int ec) {
        if (pos == total || (ic == 0 && ec == 0)) {
            return 0;
        }
        if (memo[pos][mask][ic][ec] != -1) {
            return memo[pos][mask][ic][ec];
        }

        int r = pos / n;
        int c = pos % n;
        int up = mask / pw;   // digit n-1 -> cell pos - n
        int left = mask % 3;  // digit 0   -> cell pos - 1

        int best = 0;
        for (int cur = 0; cur <= 2; cur++) {
            if (cur == 1 && ic == 0) {
                continue;
            }
            if (cur == 2 && ec == 0) {
                continue;
            }
            int gain = BASE[cur];
            if (r > 0) {
                gain += H[cur][up];
            }
            if (c > 0) {
                gain += H[cur][left];
            }
            int nmask = (mask % pw) * 3 + cur;
            int nic = (cur == 1) ? ic - 1 : ic;
            int nec = (cur == 2) ? ec - 1 : ec;
            int cand = gain + dfs(pos + 1, nmask, nic, nec);
            if (cand > best) {
                best = cand;
            }
        }

        memo[pos][mask][ic][ec] = best;
        return best;
    }
}
