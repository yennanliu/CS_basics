package LeetCodeJava.Array;

// https://leetcode.com/problems/fill-a-special-grid/description/

/**
 * 3537. Fill a Special Grid
 * Medium
 *
 * You are given a non-negative integer n representing a 2^n x 2^n grid. You must
 * fill the grid with integers from 0 to 2^2n - 1 to make it special. A grid is
 * special if it satisfies all the following conditions:
 *
 * All numbers in the top-right quadrant are smaller than those in the bottom-right quadrant.
 * All numbers in the bottom-right quadrant are smaller than those in the bottom-left quadrant.
 * All numbers in the bottom-left quadrant are smaller than those in the top-left quadrant.
 * Each of its quadrants is also a special grid.
 *
 * Return the special 2^n x 2^n grid.
 *
 * Note: Any 1x1 grid is special.
 *
 *
 * Example 1:
 *
 * Input: n = 0
 * Output: [[0]]
 *
 * Example 2:
 *
 * Input: n = 1
 * Output: [[3,0],[2,1]]
 *
 * Example 3:
 *
 * Input: n = 2
 * Output: [[15,12,3,0],[14,13,2,1],[11,8,7,4],[10,9,6,5]]
 *
 *
 * Constraints:
 *
 * 0 <= n <= 10
 *
 */
public class FillASpecialGrid {

    // V0
    // IDEA: DIVIDE AND CONQUER -- PAINT EACH QUADRANT WITH ITS FORCED OFFSET
    //   the conditions order the quadrants: top-right < bottom-right < bottom-left < top-left.
    //   a quadrant of side h owns exactly h*h consecutive values, so its starting value
    //   is forced (base, base + h*h, base + 2h*h, base + 3h*h), and "each quadrant is
    //   also special" is literally the recursive call.
    /**
     * time = O(4^N)
     * space = O(4^N)
     */
    public int[][] specialGrid(int n) {
        int side = 1 << n; // 2^n
        int[][] grid = new int[side][side];
        fill(grid, 0, 0, side, 0);
        return grid;
    }

    /** Fills the size x size square whose top-left is (row, col) with base .. base + size^2 - 1. */
    private void fill(int[][] grid, int row, int col, int size, int base) {
        // a single cell can only hold the smallest value of its block
        if (size == 1) {
            grid[row][col] = base;
            return;
        }
        int half = size / 2;
        int quadrantCount = half * half;

        fill(grid, row, col + half, half, base);                          // top-right: smallest
        fill(grid, row + half, col + half, half, base + quadrantCount);     // bottom-right
        fill(grid, row + half, col, half, base + 2 * quadrantCount);        // bottom-left
        fill(grid, row, col, half, base + 3 * quadrantCount);               // top-left: largest
    }

    // V1
    // IDEA: BOTTOM-UP DOUBLING -- THE FOUR QUADRANTS ARE THE SAME GRID, SHIFTED
    //   start from [[0]]; each round paste four offset copies of the current grid
    //   (top-left +3q, top-right +0, bottom-left +2q, bottom-right +q, q = side^2).
    //   same recursion tree as V0, walked from the 1x1 grid up.
    /**
     * time = O(4^N)
     * space = O(4^N)
     */
    public int[][] specialGrid_1(int n) {
        int[][] grid = new int[][]{{0}};
        int side = 1;
        for (int round = 0; round < n; round++) {
            int quadrantCount = side * side;
            int[][] next = new int[2 * side][2 * side];
            for (int r = 0; r < side; r++) {
                for (int c = 0; c < side; c++) {
                    int value = grid[r][c];
                    next[r][c] = value + 3 * quadrantCount;               // top-left
                    next[r][c + side] = value;                            // top-right
                    next[r + side][c] = value + 2 * quadrantCount;        // bottom-left
                    next[r + side][c + side] = value + quadrantCount;     // bottom-right
                }
            }
            grid = next;
            side *= 2;
        }
        return grid;
    }
}
