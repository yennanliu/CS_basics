package LeetCodeJava.Graph;

// https://leetcode.com/problems/regions-cut-by-slashes/description/
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

}
