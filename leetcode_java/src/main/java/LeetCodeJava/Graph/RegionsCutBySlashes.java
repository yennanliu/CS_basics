package LeetCodeJava.Graph;

// https://leetcode.com/problems/regions-cut-by-slashes/description/

import java.util.ArrayDeque;
import java.util.Deque;
/**
 * 959. Regions Cut By Slashes
 * Medium
 *
 * An n x n grid is composed of 1 x 1 squares where each 1 x 1 square consists of a
 * '/', '\', or blank space ' '. These characters divide the square into contiguous
 * regions.
 *
 * Given the grid grid represented as a string array, return the number of regions.
 *
 * Note that backslash characters are escaped, so a '\' is represented as '\\'.
 *
 * Example 1:
 *
 * Input: grid = [" /","/ "]
 * Output: 2
 *
 * Example 2:
 *
 * Input: grid = [" /","  "]
 * Output: 1
 *
 * Example 3:
 *
 * Input: grid = ["/\\","\\/"]
 * Output: 5
 * Explanation: Recall that because \ characters are escaped, "\\/" refers to \/,
 * and "/\\" refers to /\.
 *
 * Constraints:
 *
 * n == grid.length == grid[i].length
 * 1 <= n <= 30
 * grid[i][j] is either '/', '\', or ' '.
 *
 */
public class RegionsCutBySlashes {

    // V0
    // IDEA: UNION FIND on 4 TRIANGLES per CELL
    /**
     *  SPLIT every 1x1 cell into 4 triangles, indexed CLOCKWISE:
     *
     *        \  0  /
     *         \   /
     *      3    X    1
     *         /   \
     *        /  2  \
     *
     *    0 = top, 1 = right, 2 = bottom, 3 = left
     *
     *  INSIDE a cell:
     *     '/'   -> merge (0,3) and (1,2)
     *     '\'   -> merge (0,1) and (2,3)
     *     ' '   -> merge ALL four
     *
     *  BETWEEN cells:
     *     right neighbour  -> this cell's 1  with neighbour's 3
     *     bottom neighbour -> this cell's 2  with neighbour's 0
     *
     *  Start with 4*n*n components and DECREMENT on every successful union;
     *  what is left IS the number of regions.
     *
     *  time  = O(n^2 * a(n^2))
     *  space = O(n^2)
     */

    private int[] parent;

    public int regionsBySlashes(String[] grid) {
        int n = grid.length;

        this.parent = new int[4 * n * n];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
        }

        int count = 4 * n * n;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int base = 4 * (i * n + j);
                char ch = grid[i].charAt(j);

                if (ch == '/') {
                    if (union(base + 0, base + 3)) {
                        count -= 1;
                    }
                    if (union(base + 1, base + 2)) {
                        count -= 1;
                    }
                } else if (ch == '\\') {
                    if (union(base + 0, base + 1)) {
                        count -= 1;
                    }
                    if (union(base + 2, base + 3)) {
                        count -= 1;
                    }
                } else {
                    // blank -> all four triangles are one piece
                    if (union(base + 0, base + 1)) {
                        count -= 1;
                    }
                    if (union(base + 1, base + 2)) {
                        count -= 1;
                    }
                    if (union(base + 2, base + 3)) {
                        count -= 1;
                    }
                }

                // stitch to the cell on the RIGHT
                if (j + 1 < n && union(base + 1, 4 * (i * n + j + 1) + 3)) {
                    count -= 1;
                }
                // stitch to the cell BELOW
                if (i + 1 < n && union(base + 2, 4 * ((i + 1) * n + j) + 0)) {
                    count -= 1;
                }
            }
        }

        return count;
    }

    private int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    /** returns true when two DIFFERENT components got merged */
    private boolean union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) {
            return false;
        }
        parent[ra] = rb;
        return true;
    }


    // V1
    // IDEA: UPSCALE EACH CELL TO A 3x3 BLOCK + FLOOD FILL
    /**
     *  Draw the picture instead of reasoning about it: blow every cell up to a 3x3
     *  pixel block and paint the slash as three diagonal pixels.
     *
     *  '/' fills (0,2) (1,1) (2,0);  '\' fills (0,0) (1,1) (2,2).
     *
     *  The answer is then simply the number of connected 0-regions -- no triangle
     *  indexing and no union-find at all.
     *
     *  NOTE !!! 3 is the SMALLEST factor that works; at 2x2 the two diagonals of a
     *           '/' and a '\' in adjacent cells would leak into each other.
     *
     *  time  = O(n^2)
     *  space = O(n^2)
     */
    public int regionsBySlashes_1(String[] grid) {
        int n = grid.length;
        int[][] g = new int[n * 3][n * 3];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                char c = grid[i].charAt(j);
                if (c == '/') {
                    g[i * 3][j * 3 + 2] = 1;
                    g[i * 3 + 1][j * 3 + 1] = 1;
                    g[i * 3 + 2][j * 3] = 1;
                } else if (c == '\\') {
                    g[i * 3][j * 3] = 1;
                    g[i * 3 + 1][j * 3 + 1] = 1;
                    g[i * 3 + 2][j * 3 + 2] = 1;
                }
            }
        }

        int size = n * 3;
        int regions = 0;
        int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (g[i][j] != 0) {
                    continue;
                }
                regions += 1;
                Deque<int[]> q = new ArrayDeque<>();
                q.offer(new int[] { i, j });
                g[i][j] = 2;
                while (!q.isEmpty()) {
                    int[] cur = q.poll();
                    for (int[] d : dirs) {
                        int nr = cur[0] + d[0];
                        int nc = cur[1] + d[1];
                        if (nr >= 0 && nr < size && nc >= 0 && nc < size && g[nr][nc] == 0) {
                            g[nr][nc] = 2;
                            q.offer(new int[] { nr, nc });
                        }
                    }
                }
            }
        }

        return regions;
    }

    // V2
    // IDEA: EULER'S FORMULA  V - E + F = 1 + C
    /**
     *  Treat the drawing as a planar graph on the (n+1) x (n+1) lattice points.
     *  Every slash is one edge between two lattice points; the border supplies the
     *  rest. Then Euler gives the face count directly:
     *
     *      regions = E - V + 1 + C
     *
     *  where C is the number of connected components of the drawn graph.
     *
     *  A completely different toolkit -- topology rather than flood fill -- and it
     *  touches only O(n^2) lattice points.
     *
     *  time  = O(n^2 * alpha)
     *  space = O(n^2)
     */
    public int regionsBySlashes_2(String[] grid) {
        int n = grid.length;
        int side = n + 1;
        int[] par = new int[side * side];
        for (int i = 0; i < par.length; i++) {
            par[i] = i;
        }

        int edges = 0;
        int merges = 0;

        // the whole border is drawn
        for (int i = 0; i < n; i++) {
            merges += uni2(par, i, i + 1) ? 1 : 0;                                   // top
            edges += 1;
            merges += uni2(par, n * side + i, n * side + i + 1) ? 1 : 0;             // bottom
            edges += 1;
            merges += uni2(par, i * side, (i + 1) * side) ? 1 : 0;                   // left
            edges += 1;
            merges += uni2(par, i * side + n, (i + 1) * side + n) ? 1 : 0;           // right
            edges += 1;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                char c = grid[i].charAt(j);
                if (c == '/') {
                    merges += uni2(par, i * side + (j + 1), (i + 1) * side + j) ? 1 : 0;
                    edges += 1;
                } else if (c == '\\') {
                    merges += uni2(par, i * side + j, (i + 1) * side + (j + 1)) ? 1 : 0;
                    edges += 1;
                }
            }
        }

        int vertices = side * side;
        int components = vertices - merges;
        // Euler: F = E - V + 1 + C, and F counts the outer face too -> subtract it
        return edges - vertices + components;
    }

    private boolean uni2(int[] par, int a, int b) {
        int ra = find2(par, a);
        int rb = find2(par, b);
        if (ra == rb) {
            return false;
        }
        par[ra] = rb;
        return true;
    }

    private int find2(int[] par, int x) {
        while (par[x] != x) {
            par[x] = par[par[x]];
            x = par[x];
        }
        return x;
    }

    // V3
    // IDEA: UNION FIND OVER THE UPSCALED 3n x 3n PIXEL GRID
    /**
     *  The same 3x scaling as V1, but the regions are counted with union-find over
     *  the empty pixels rather than by flood fill.
     *
     *  Compared with V0 the unit of merging is a PIXEL rather than a triangle, so
     *  there is no per-cell case analysis at all -- only `is this pixel drawn?`.
     *
     *  time  = O(n^2 * alpha)
     *  space = O(n^2)
     */
    public int regionsBySlashes_3(String[] grid) {
        int n = grid.length;
        int size = n * 3;
        boolean[][] drawn = new boolean[size][size];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                char c = grid[i].charAt(j);
                if (c == '/') {
                    drawn[i * 3][j * 3 + 2] = true;
                    drawn[i * 3 + 1][j * 3 + 1] = true;
                    drawn[i * 3 + 2][j * 3] = true;
                } else if (c == '\\') {
                    drawn[i * 3][j * 3] = true;
                    drawn[i * 3 + 1][j * 3 + 1] = true;
                    drawn[i * 3 + 2][j * 3 + 2] = true;
                }
            }
        }

        int[] par = new int[size * size];
        for (int i = 0; i < par.length; i++) {
            par[i] = i;
        }

        int empty = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (drawn[i][j]) {
                    continue;
                }
                empty += 1;
                if (i + 1 < size && !drawn[i + 1][j]) {
                    if (uni2(par, i * size + j, (i + 1) * size + j)) {
                        empty -= 1;
                    }
                }
                if (j + 1 < size && !drawn[i][j + 1]) {
                    if (uni2(par, i * size + j, i * size + j + 1)) {
                        empty -= 1;
                    }
                }
            }
        }

        return empty;
    }

}
