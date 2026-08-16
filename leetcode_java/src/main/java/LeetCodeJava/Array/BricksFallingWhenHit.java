package LeetCodeJava.Array;

// https://leetcode.com/problems/bricks-falling-when-hit/description/
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

}
