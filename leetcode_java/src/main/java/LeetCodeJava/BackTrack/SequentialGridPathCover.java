package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/sequential-grid-path-cover/

import java.util.ArrayList;
import java.util.List;

/**
 *  3565. Sequential Grid Path Cover
 *  Medium
 *
 *  You are given a 2D array grid of size m x n, and an integer k. There are k cells
 *  in grid containing the values from 1 to k exactly once, and the rest of the cells
 *  have a value 0.
 *
 *  You can start at any cell, and move from a cell to its neighbors (up, down, left,
 *  or right). You must find a path in grid which:
 *    Visits each cell in grid exactly once.
 *    Visits the cells with values from 1 to k in order.
 *
 *  Return a 2D array result of size (m * n) x 2, where result[i] = [xi, yi]
 *  represents the ith cell visited in the path. If there are multiple such paths,
 *  you may return any one of them. If no such path exists, return an empty array.
 *
 *  Example 1:
 *    Input: grid = [[0,0,0],[0,1,2]], k = 2
 *    Output: [[0,0],[1,0],[1,1],[1,2],[0,2],[0,1]]
 *
 *  Example 2:
 *    Input: grid = [[1,0,4],[3,0,2]], k = 4
 *    Output: []
 *
 *  Constraints:
 *    1 <= m == grid.length <= 5
 *    1 <= n == grid[i].length <= 5
 *    1 <= k <= m * n
 *    0 <= grid[i][j] <= k
 *    grid contains all integers between 1 and k exactly once.
 */
public class SequentialGridPathCover {

    private int[][] grid;
    private int m;
    private int n;
    private int total;
    private int seen; // bitmask over the <= 25 cells
    private List<int[]> path;

    private static final int[][] DIRS = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    // V0
    // IDEA: BACKTRACKING WITH THE "NEXT WANTED LABEL" AS PART OF THE STATE
    //       the path is a hamiltonian path in the grid graph, so brute force search
    //       is unavoidable; the ordering constraint is what makes it cheap. carry the
    //       next label we still owe, v (starting at 1). a cell may be stepped on only
    //       if it is blank or carries exactly v -- a cell holding a label larger than
    //       v would jump the queue, and one holding a smaller label was already
    //       consumed. that single test enforces "1..k in order" locally, so no global
    //       check at the end is needed.
    //
    //       the start cell is likewise restricted to blank or 1, and since the walk
    //       ends only when all m*n cells are on the path, covering every cell exactly
    //       once is guaranteed by construction.
    /**
     * time = O(m * n * 3^(m * n)) worst case
     * space = O(m * n)
     */
    public int[][] findPath(int[][] grid, int k) {
        this.grid = grid;
        this.m = grid.length;
        this.n = grid[0].length;
        this.total = m * n;
        this.path = new ArrayList<>();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0 || grid[i][j] == 1) {
                    if (dfs(i, j, 1)) {
                        int[][] out = new int[path.size()][];
                        for (int t = 0; t < path.size(); t++) {
                            out[t] = path.get(t);
                        }
                        return out;
                    }
                    path.clear();
                    seen = 0;
                }
            }
        }
        return new int[0][0];
    }

    private boolean dfs(int i, int j, int v) {
        path.add(new int[] { i, j });
        if (path.size() == total) {
            return true;
        }

        seen |= 1 << (i * n + j);
        if (grid[i][j] == v) {
            v++;
        }

        for (int[] d : DIRS) {
            int x = i + d[0];
            int y = j + d[1];
            if (x < 0 || x >= m || y < 0 || y >= n) {
                continue;
            }
            if ((seen & (1 << (x * n + y))) != 0) {
                continue;
            }
            if (grid[x][y] == 0 || grid[x][y] == v) {
                if (dfs(x, y, v)) {
                    return true;
                }
            }
        }

        seen ^= 1 << (i * n + j);
        path.remove(path.size() - 1);
        return false;
    }
}
