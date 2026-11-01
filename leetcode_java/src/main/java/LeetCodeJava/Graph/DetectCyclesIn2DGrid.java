package LeetCodeJava.Graph;

// https://leetcode.com/problems/detect-cycles-in-2d-grid/description/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 1559. Detect Cycles in 2D Grid
 * Medium
 *
 * Given a 2D array of characters grid of size m x n, you need to find if there exists any
 * cycle consisting of the same value in grid.
 *
 * A cycle is a path of length 4 or more in the grid that starts and ends at the same cell.
 * From a given cell, you can move to one of the cells adjacent to it - in one of the four
 * directions (up, down, left, or right), if it has the same value of the current cell.
 *
 * Also, you cannot move to the cell that you visited in your last move. For example, the
 * cycle (1, 1) -> (1, 2) -> (1, 1) is invalid because from (1, 2) we visited (1, 1) which
 * was the last visited cell.
 *
 * Return true if any cycle of the same value exists in grid, otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: grid = [["a","a","a","a"],["a","b","b","a"],["a","b","b","a"],["a","a","a","a"]]
 * Output: true
 *
 * Example 2:
 *
 * Input: grid = [["c","c","c","a"],["c","d","c","c"],["c","c","e","c"],["f","c","c","c"]]
 * Output: true
 *
 * Example 3:
 *
 * Input: grid = [["a","b","b"],["b","z","b"],["b","b","a"]]
 * Output: false
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 500
 * grid consists only of lowercase English letters.
 *
 */
public class DetectCyclesIn2DGrid {

    // V0
    // IDEA: UNION FIND — a node is a cell, an edge joins two 4-adjacent cells with the SAME
    //       letter. Offer each edge once (look only right and down); an edge whose two
    //       endpoints already share a root closes a cycle. A grid has no triangles, so any
    //       such cycle already has length >= 4.
    /**
     * time = O(M * N * alpha(M * N))
     * space = O(M * N)
     */
    public boolean containsCycle(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // cell (r, c) is node r * cols + c
        int[] parent = new int[rows * cols];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
        }

        int[][] rightAndDown = {{0, 1}, {1, 0}};
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                for (int[] dir : rightAndDown) {
                    int nr = r + dir[0];
                    int nc = c + dir[1];
                    if (nr >= rows || nc >= cols || grid[nr][nc] != grid[r][c]) {
                        continue;
                    }
                    int rootA = find(parent, r * cols + c);
                    int rootB = find(parent, nr * cols + nc);
                    if (rootA == rootB) {
                        // already connected by another path -> this edge closes a cycle
                        return true;
                    }
                    parent[rootA] = rootB;
                }
            }
        }
        return false;
    }

    /** Root of x, with path halving to keep the trees shallow. */
    private int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    // V1
    // IDEA: ITERATIVE DFS + PARENT TRACKING — flood fill each same-letter component, pushing
    //       (cell, the cell we came from). Reaching an already-visited neighbour that is NOT
    //       the cell we came from means a second route -> cycle. Iterative, because a
    //       500 x 500 single-letter grid would overflow a recursive DFS.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public boolean containsCycle_1(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] visited = new boolean[rows][cols];
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int startR = 0; startR < rows; startR++) {
            for (int startC = 0; startC < cols; startC++) {
                if (visited[startR][startC]) {
                    continue;
                }
                char letter = grid[startR][startC];
                visited[startR][startC] = true;

                // each entry: {row, col, parentRow, parentCol}
                Deque<int[]> stack = new ArrayDeque<>();
                stack.push(new int[]{startR, startC, -1, -1});

                while (!stack.isEmpty()) {
                    int[] cur = stack.pop();
                    int r = cur[0];
                    int c = cur[1];
                    for (int[] dir : directions) {
                        int nr = r + dir[0];
                        int nc = c + dir[1];
                        if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                            continue;
                        }
                        // different letter, or stepping straight back the way we came
                        if (grid[nr][nc] != letter || (nr == cur[2] && nc == cur[3])) {
                            continue;
                        }
                        if (visited[nr][nc]) {
                            return true;
                        }
                        visited[nr][nc] = true;
                        stack.push(new int[]{nr, nc, r, c});
                    }
                }
            }
        }
        return false;
    }
}
