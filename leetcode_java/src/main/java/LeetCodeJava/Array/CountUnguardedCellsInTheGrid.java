package LeetCodeJava.Array;

// https://leetcode.com/problems/count-unguarded-cells-in-the-grid/description/

/**
 * 2257. Count Unguarded Cells in the Grid
 * Medium
 *
 * You are given two integers m and n representing a 0-indexed m x n grid. You are also given two 2D integer arrays guards and walls where guards[i] = [rowi, coli] and walls[j] = [rowj, colj] represent the positions of the ith guard and jth wall respectively.
 *
 * A guard can see every cell in the four cardinal directions (north, east, south, or west) starting from their position unless obstructed by a wall or another guard. A cell is guarded if there is at least one guard that can see it.
 *
 * Return the number of unoccupied cells that are not guarded.
 *
 *
 * Example 1:
 *
 * Input: m = 4, n = 6, guards = [[0,0],[1,1],[2,3]], walls = [[0,1],[2,2],[1,4]]
 * Output: 7
 * Explanation: The guarded and unguarded cells are shown in red and green respectively in the above diagram.
 * There are a total of 7 unguarded cells, so we return 7.
 *
 * Example 2:
 *
 * Input: m = 3, n = 3, guards = [[1,1]], walls = [[0,1],[1,2],[2,1]]
 * Output: 4
 * Explanation: The unguarded cells are shown in green in the above diagram.
 * There are a total of 4 unguarded cells, so we return 4.
 *
 *
 * Constraints:
 *
 * 1 <= m, n <= 10^5
 * 2 <= m * n <= 10^5
 * 1 <= guards.length, walls.length <= 5 * 10^4
 * 2 <= guards.length + walls.length <= m * n
 * guards[j].length == walls[j].length == 2
 * 0 <= rowi, rowj < m
 * 0 <= coli, colj < n
 * All the positions in guards and walls are unique.
 *
 */
public class CountUnguardedCellsInTheGrid {

    private static final int FREE = 0;
    private static final int GUARD = 1;
    private static final int WALL = 2;
    private static final int SEEN = 3;

    // V0
    // IDEA: RAY-CAST FROM EACH GUARD, STOPPING AT A GUARD OR A WALL
    /**
     *  Mark guards and walls, then from every guard walk the four directions
     *  marking cells SEEN until a guard or wall blocks the view. A SEEN cell does
     *  not block, so the walk passes through it. Count the cells still FREE.
     *
     * time = O(M * N + G * (M + N)), G = guards.length
     * space = O(M * N)
     */
    public int countUnguarded(int m, int n, int[][] guards, int[][] walls) {
        int[][] grid = new int[m][n];
        for (int[] guard : guards) {
            grid[guard[0]][guard[1]] = GUARD;
        }
        for (int[] wall : walls) {
            grid[wall[0]][wall[1]] = WALL;
        }

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] guard : guards) {
            for (int[] dir : directions) {
                int r = guard[0] + dir[0];
                int c = guard[1] + dir[1];
                while (r >= 0 && r < m && c >= 0 && c < n && grid[r][c] != GUARD && grid[r][c] != WALL) {
                    grid[r][c] = SEEN;
                    r += dir[0];
                    c += dir[1];
                }
            }
        }

        int unguarded = 0;
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == FREE) {
                    unguarded++;
                }
            }
        }
        return unguarded;
    }

    // V1
    // IDEA: 4 LINE SWEEPS WITH A "LIT" FLAG (FIXED 4 * M * N WORK, NO PER-GUARD RAYS)
    /**
     *  Walk every row left->right and right->left, and every column top->bottom
     *  and bottom->top, carrying one boolean: a guard turns it on, a wall turns
     *  it off, and a free cell is seen while it is on.
     *
     * time = O(M * N)
     * space = O(M * N)
     */
    public int countUnguarded_1(int m, int n, int[][] guards, int[][] walls) {
        int[][] grid = new int[m][n];
        for (int[] guard : guards) {
            grid[guard[0]][guard[1]] = GUARD;
        }
        for (int[] wall : walls) {
            grid[wall[0]][wall[1]] = WALL;
        }
        boolean[][] seen = new boolean[m][n];

        for (int r = 0; r < m; r++) {
            boolean lit = false;
            for (int c = 0; c < n; c++) {
                lit = sweepCell(grid, seen, r, c, lit);
            }
            lit = false;
            for (int c = n - 1; c >= 0; c--) {
                lit = sweepCell(grid, seen, r, c, lit);
            }
        }
        for (int c = 0; c < n; c++) {
            boolean lit = false;
            for (int r = 0; r < m; r++) {
                lit = sweepCell(grid, seen, r, c, lit);
            }
            lit = false;
            for (int r = m - 1; r >= 0; r--) {
                lit = sweepCell(grid, seen, r, c, lit);
            }
        }

        int unguarded = 0;
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == FREE && !seen[r][c]) {
                    unguarded++;
                }
            }
        }
        return unguarded;
    }

    /** Processes one cell of a sweep and returns whether the line of sight is still lit after it. */
    private boolean sweepCell(int[][] grid, boolean[][] seen, int r, int c, boolean lit) {
        if (grid[r][c] == GUARD) {
            return true;
        }
        if (grid[r][c] == WALL) {
            return false;
        }
        if (lit) {
            seen[r][c] = true;
        }
        return lit;
    }
}
