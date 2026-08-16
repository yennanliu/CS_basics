package LeetCodeJava.Array;

// https://leetcode.com/problems/bricks-falling-when-hit/description/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
/**
 * 803. Bricks Falling When Hit
 * Hard
 *
 * You are given an m x n binary grid, where each 1 represents a brick and
 * 0 represents an empty space. A brick is stable if:
 *
 * It is directly connected to the top of the grid, or
 * At least one other brick in its four adjacent cells is stable.
 *
 * You are also given an array hits, which is a sequence of erasures we want to apply.
 * Each time we want to erase the brick at the location hits[i] = (rowi, coli).
 * The brick on that location (if it exists) will disappear. Some other bricks may no
 * longer be stable because of that erasure and will fall. Once a brick falls, it is
 * immediately erased from the grid (i.e., it does not land on other stable bricks).
 *
 * Return an array result, where each result[i] is the number of bricks that will fall
 * after the ith erasure is applied.
 *
 * Note that an erasure may refer to a location with no brick,
 * and if it does, no bricks drop.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,0,0,0],[1,1,1,0]], hits = [[1,0]]
 * Output: [2]
 * Explanation: Starting with the grid:
 * [[1,0,0,0],
 *  [1,1,1,0]]
 * We erase the brick at (1,0), resulting in the grid:
 * [[1,0,0,0],
 *  [0,1,1,0]]
 * The two remaining bricks on row 1 are no longer stable as they are no longer
 * connected to the top nor adjacent to another stable brick, so they will fall.
 * The resulting grid is:
 * [[1,0,0,0],
 *  [0,0,0,0]]
 * Hence the result is [2].
 *
 * Example 2:
 *
 * Input: grid = [[1,0,0,0],[1,1,0,0]], hits = [[1,1],[1,0]]
 * Output: [0,0]
 * Explanation: no brick ever loses its connection to the top,
 * so nothing falls after either erasure.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 200
 * grid[i][j] is 0 or 1.
 * 1 <= hits.length <= 4 * 10^4
 * hits[i].length == 2
 * 0 <= xi <= m - 1
 * 0 <= yi <= n - 1
 * All (xi, yi) are unique.
 *
 */
public class BricksFallingWhenHit {

    // V0
    // IDEA: UNION FIND + REVERSE TIME (`re-add the bricks`)
    /**
     *   Union-Find can MERGE components but cannot SPLIT them, so we run time
     *   BACKWARDS:
     *
     *     1) erase every hit brick up front -> the `final` grid
     *     2) union-find the survivors, with a virtual node TOP joined to row 0
     *     3) walk the hits in REVERSE and ADD each brick back.
     *        The number of bricks that fell at that hit equals
     *            (size of TOP component after) - (before) - 1
     *        (the `-1` excludes the re-added brick itself)
     *
     *   time  = O(m * n * alpha + h * alpha),  h = hits.length
     *   space = O(m * n)
     */

    private int[] parent;
    private int[] size;
    private int[][] g;
    private int m;
    private int n;
    private int TOP;

    private final int[][] DIRS = new int[][] { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    public int[] hitBricks(int[][] grid, int[][] hits) {
        this.m = grid.length;
        this.n = grid[0].length;
        this.TOP = m * n; // virtual `ceiling` node

        this.parent = new int[m * n + 1];
        this.size = new int[m * n + 1];
        for (int i = 0; i < m * n + 1; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        /** NOTE !!! step 1)
         *
         *  build the grid AFTER all hits are applied
         *  (we must NOT mutate the caller's `grid`, we still need it in step 3)
         */
        this.g = new int[m][n];
        for (int r = 0; r < m; r++) {
            System.arraycopy(grid[r], 0, g[r], 0, n);
        }
        for (int[] h : hits) {
            g[h[0]][h[1]] = 0;
        }

        // step 2) union-find over the surviving bricks
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (g[r][c] == 1) {
                    connect(r, c);
                }
            }
        }

        /** NOTE !!! step 3)
         *
         *  replay the hits in REVERSE, adding bricks back
         */
        int[] res = new int[hits.length];
        for (int i = hits.length - 1; i >= 0; i--) {
            int r = hits[i][0];
            int c = hits[i][1];

            // the hit removed nothing
            if (grid[r][c] == 0) {
                continue;
            }

            int before = size[find(TOP)];
            g[r][c] = 1;
            connect(r, c);
            int after = size[find(TOP)];

            // -1 : the brick we just put back is NOT a `fallen` brick
            res[i] = Math.max(0, after - before - 1);
        }

        return res;
    }

    /** join cell (r,c) with TOP (if on row 0) and its brick neighbours */
    private void connect(int r, int c) {
        if (r == 0) {
            union(r * n + c, TOP);
        }
        for (int[] d : DIRS) {
            int nr = r + d[0];
            int nc = c + d[1];
            if (nr >= 0 && nr < m && nc >= 0 && nc < n && g[nr][nc] == 1) {
                union(r * n + c, nr * n + nc);
            }
        }
    }

    private int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]]; // path halving
            x = parent[x];
        }
        return x;
    }

    private void union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra != rb) {
            parent[ra] = rb;
            size[rb] += size[ra];
        }
    }


    // V1
    // IDEA: FORWARD BRUTE FORCE (drop a brick, re-flood-fill from the top)
    /**
     *  The literal reading of the statement: apply each hit in order, then flood
     *  fill from row 0 to find everything still stable, and whatever disappeared
     *  since the previous round has fallen.
     *
     *  O(h * m * n) so it TLEs at h = 4 * 10^4, but it needs no reverse-time
     *  insight at all -- this is the oracle the union-find versions are checked
     *  against.
     *
     *  time  = O(h * m * n)
     *  space = O(m * n)
     */
    public int[] hitBricks_1(int[][] grid, int[][] hits) {
        int rows = grid.length;
        int cols = grid[0].length;

        int[][] g = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            g[r] = grid[r].clone();
        }

        int stable = countStable(g);
        int[] res = new int[hits.length];

        for (int t = 0; t < hits.length; t++) {
            int r = hits[t][0];
            int c = hits[t][1];
            if (g[r][c] == 0) {
                res[t] = 0;
                continue;
            }
            g[r][c] = 0;

            int now = countStable(g);
            // -1 : the brick we knocked out is not counted as `fallen`
            res[t] = Math.max(0, stable - now - 1);
            stable = now;

            // physically drop everything unstable so the next round starts clean
            dropUnstable(g);
        }

        return res;
    }

    /** number of bricks currently connected to the top row */
    private int countStable(int[][] g) {
        int rows = g.length;
        int cols = g[0].length;
        boolean[][] seen = new boolean[rows][cols];
        Deque<int[]> stack = new ArrayDeque<>();

        for (int c = 0; c < cols; c++) {
            if (g[0][c] == 1 && !seen[0][c]) {
                seen[0][c] = true;
                stack.push(new int[] { 0, c });
            }
        }

        int cnt = 0;
        int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        while (!stack.isEmpty()) {
            int[] cur = stack.pop();
            cnt += 1;
            for (int[] d : dirs) {
                int nr = cur[0] + d[0];
                int nc = cur[1] + d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                        && g[nr][nc] == 1 && !seen[nr][nc]) {
                    seen[nr][nc] = true;
                    stack.push(new int[] { nr, nc });
                }
            }
        }
        return cnt;
    }

    /** zero out every brick that is no longer connected to the top */
    private void dropUnstable(int[][] g) {
        int rows = g.length;
        int cols = g[0].length;
        boolean[][] seen = new boolean[rows][cols];
        Deque<int[]> stack = new ArrayDeque<>();

        for (int c = 0; c < cols; c++) {
            if (g[0][c] == 1 && !seen[0][c]) {
                seen[0][c] = true;
                stack.push(new int[] { 0, c });
            }
        }
        int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        while (!stack.isEmpty()) {
            int[] cur = stack.pop();
            for (int[] d : dirs) {
                int nr = cur[0] + d[0];
                int nc = cur[1] + d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                        && g[nr][nc] == 1 && !seen[nr][nc]) {
                    seen[nr][nc] = true;
                    stack.push(new int[] { nr, nc });
                }
            }
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (g[r][c] == 1 && !seen[r][c]) {
                    g[r][c] = 0;
                }
            }
        }
    }

    // V2
    // IDEA: REVERSE TIME + DFS (no union-find at all)
    /**
     *  Same reverse-time insight as V0, but the `how many bricks did this one
     *  re-attach?` question is answered by a DFS that MARKS reattached cells
     *  (grid value 1 -> 2) instead of by union-find component sizes.
     *
     *  Slower asymptotically, but far shorter, and it needs no virtual TOP node.
     *
     *  time  = O(h * m * n)
     *  space = O(m * n)
     */
    public int[] hitBricks_2(int[][] grid, int[][] hits) {
        int rows = grid.length;
        int cols = grid[0].length;

        int[][] g = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            g[r] = grid[r].clone();
        }
        for (int[] h : hits) {
            g[h[0]][h[1]] -= 1; // 1 -> 0 for a real brick, 0 -> -1 for a no-op hit
        }

        // mark everything still attached to the top as 2
        for (int c = 0; c < cols; c++) {
            markAttached(g, 0, c);
        }

        int[] res = new int[hits.length];
        int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        for (int t = hits.length - 1; t >= 0; t--) {
            int r = hits[t][0];
            int c = hits[t][1];
            g[r][c] += 1; // put the brick back

            if (g[r][c] != 1) {
                continue; // the hit removed nothing
            }
            // does it actually touch the stable structure?
            boolean touches = r == 0;
            for (int[] d : dirs) {
                int nr = r + d[0];
                int nc = c + d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && g[nr][nc] == 2) {
                    touches = true;
                }
            }
            if (!touches) {
                continue;
            }

            // -1 : the re-added brick itself is not a fallen brick
            res[t] = markAttached(g, r, c) - 1;
        }

        return res;
    }

    /** flood fill 1 -> 2 from (r, c); returns how many cells were converted */
    private int markAttached(int[][] g, int r, int c) {
        if (r < 0 || r >= g.length || c < 0 || c >= g[0].length || g[r][c] != 1) {
            return 0;
        }
        g[r][c] = 2;
        return 1 + markAttached(g, r - 1, c) + markAttached(g, r + 1, c)
                + markAttached(g, r, c - 1) + markAttached(g, r, c + 1);
    }

    // V3
    // IDEA: REVERSE TIME + UNION FIND WITHOUT A VIRTUAL TOP NODE
    /**
     *  V0 models `connected to the ceiling` with an extra node. Here each ROOT
     *  instead carries a boolean `touchesTop`, merged on union.
     *
     *  The stable count is then the sum of the sizes of all top-touching roots,
     *  maintained incrementally as `stableCount`.
     *
     *  Same complexity as V0; the point is that it avoids the off-by-one traps
     *  the virtual node introduces (TOP's own size must not be counted).
     *
     *  time  = O(m * n * alpha + h * alpha)
     *  space = O(m * n)
     */
    public int[] hitBricks_3(int[][] grid, int[][] hits) {
        int rows = grid.length;
        int cols = grid[0].length;
        int total = rows * cols;

        int[] par = new int[total];
        int[] sz = new int[total];
        boolean[] top = new boolean[total];
        for (int i = 0; i < total; i++) {
            par[i] = i;
            sz[i] = 1;
        }

        int[][] g = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            g[r] = grid[r].clone();
        }
        for (int[] h : hits) {
            g[h[0]][h[1]] = 0;
        }

        for (int c = 0; c < cols; c++) {
            if (g[0][c] == 1) {
                top[c] = true;
            }
        }

        int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (g[r][c] != 1) {
                    continue;
                }
                for (int[] d : dirs) {
                    int nr = r + d[0];
                    int nc = c + d[1];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && g[nr][nc] == 1) {
                        uni(par, sz, top, r * cols + c, nr * cols + nc);
                    }
                }
            }
        }

        int[] res = new int[hits.length];
        for (int t = hits.length - 1; t >= 0; t--) {
            int r = hits[t][0];
            int c = hits[t][1];
            if (grid[r][c] == 0) {
                continue;
            }

            int before = stableTotal(par, sz, top, g, rows, cols);
            g[r][c] = 1;
            int id = r * cols + c;
            if (r == 0) {
                top[fnd(par, id)] = true;
            }
            for (int[] d : dirs) {
                int nr = r + d[0];
                int nc = c + d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && g[nr][nc] == 1) {
                    uni(par, sz, top, id, nr * cols + nc);
                }
            }
            int after = stableTotal(par, sz, top, g, rows, cols);
            res[t] = Math.max(0, after - before - 1);
        }

        return res;
    }

    private int stableTotal(int[] par, int[] sz, boolean[] top, int[][] g, int rows, int cols) {
        Set<Integer> roots = new HashSet<>();
        int cnt = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (g[r][c] != 1) {
                    continue;
                }
                int root = fnd(par, r * cols + c);
                if (top[root] && roots.add(root)) {
                    cnt += sz[root];
                }
            }
        }
        return cnt;
    }

    private int fnd(int[] par, int x) {
        while (par[x] != x) {
            par[x] = par[par[x]];
            x = par[x];
        }
        return x;
    }

    private void uni(int[] par, int[] sz, boolean[] top, int a, int b) {
        int ra = fnd(par, a);
        int rb = fnd(par, b);
        if (ra == rb) {
            return;
        }
        par[ra] = rb;
        sz[rb] += sz[ra];
        top[rb] = top[rb] || top[ra]; // the merged root touches the top if either did
    }

}
