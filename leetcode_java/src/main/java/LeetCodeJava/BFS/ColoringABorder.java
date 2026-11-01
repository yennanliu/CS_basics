package LeetCodeJava.BFS;

// https://leetcode.com/problems/coloring-a-border/description/

import java.util.*;

/**
 * 1034. Coloring A Border
 * Medium
 *
 * You are given an m x n integer matrix grid, and three integers row, col, and color.
 * Each value in the grid represents the color of the grid square at that location.
 *
 * Two squares are called adjacent if they are next to each other in any of the 4 directions.
 *
 * Two squares belong to the same connected component if they have the same color and
 * they are adjacent.
 *
 * The border of a connected component is all the squares in the connected component
 * that are either adjacent to (at least) a square not in the component, or on the
 * boundary of the grid (the first or last row or column).
 *
 * You should color the border of the connected component that contains the square
 * grid[row][col] with color.
 *
 * Return the final grid.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,1],[1,2]], row = 0, col = 0, color = 3
 * Output: [[3,3],[3,2]]
 *
 * Example 2:
 *
 * Input: grid = [[1,2,2],[2,3,2]], row = 0, col = 1, color = 3
 * Output: [[1,3,3],[2,3,3]]
 *
 * Example 3:
 *
 * Input: grid = [[1,1,1],[1,1,1],[1,1,1]], row = 1, col = 1, color = 2
 * Output: [[2,2,2],[2,1,2],[2,2,2]]
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 50
 * 1 <= grid[i][j], color <= 1000
 * 0 <= row < m
 * 0 <= col < n
 *
 */
public class ColoringABorder {

    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    // V0
    // IDEA: BFS (COLLECT THE WHOLE COMPONENT FIRST, THEN REPAINT ONLY ITS BORDER)
    //  NOTE: never repaint while exploring -- the new color would break the
    //        "same color" test of the traversal.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int[][] colorBorder(int[][] grid, int row, int col, int color) {
        int rows = grid.length;
        int cols = grid[0].length;
        int originColor = grid[row][col];

        // pass 1: BFS over the connected component that contains (row, col)
        boolean[][] inComponent = new boolean[rows][cols];
        Deque<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[] {row, col});
        inComponent[row][col] = true;
        List<int[]> componentCells = new ArrayList<>();

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            componentCells.add(cell);
            for (int[] dir : DIRECTIONS) {
                int nr = cell[0] + dir[0];
                int nc = cell[1] + dir[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                    continue;
                }
                if (!inComponent[nr][nc] && grid[nr][nc] == originColor) {
                    inComponent[nr][nc] = true;
                    queue.offer(new int[] {nr, nc});
                }
            }
        }

        // pass 2: a cell is on the border if some neighbour is outside the grid
        // or outside the component
        List<int[]> borderCells = new ArrayList<>();
        for (int[] cell : componentCells) {
            if (isBorder(cell[0], cell[1], inComponent)) {
                borderCells.add(cell);
            }
        }

        // pass 3: repaint only after the border is fully known
        for (int[] cell : borderCells) {
            grid[cell[0]][cell[1]] = color;
        }
        return grid;
    }

    // V1
    // IDEA: DFS (RECURSIVE, SAME COLLECT-THEN-REPAINT LOGIC)
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int[][] colorBorder_1(int[][] grid, int row, int col, int color) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] inComponent = new boolean[rows][cols];
        List<int[]> componentCells = new ArrayList<>();

        dfs(grid, row, col, grid[row][col], inComponent, componentCells);

        // decide every border cell before changing any color
        List<int[]> borderCells = new ArrayList<>();
        for (int[] cell : componentCells) {
            if (isBorder(cell[0], cell[1], inComponent)) {
                borderCells.add(cell);
            }
        }
        for (int[] cell : borderCells) {
            grid[cell[0]][cell[1]] = color;
        }
        return grid;
    }

    /** Marks every cell of originColor reachable from (r, c) as part of the component. */
    private void dfs(int[][] grid, int r, int c, int originColor, boolean[][] inComponent, List<int[]> componentCells) {
        if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length) {
            return;
        }
        if (inComponent[r][c] || grid[r][c] != originColor) {
            return;
        }
        inComponent[r][c] = true;
        componentCells.add(new int[] {r, c});
        for (int[] dir : DIRECTIONS) {
            dfs(grid, r + dir[0], c + dir[1], originColor, inComponent, componentCells);
        }
    }

    /** True if (r, c) touches the grid edge or a cell that is not in the component. */
    private boolean isBorder(int r, int c, boolean[][] inComponent) {
        int rows = inComponent.length;
        int cols = inComponent[0].length;
        for (int[] dir : DIRECTIONS) {
            int nr = r + dir[0];
            int nc = c + dir[1];
            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                return true;
            }
            if (!inComponent[nr][nc]) {
                return true;
            }
        }
        return false;
    }
}
