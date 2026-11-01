package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/largest-1-bordered-square/description/

/**
 * 1139. Largest 1-Bordered Square
 * Medium
 *
 * Given a 2D grid of 0s and 1s, return the number of elements in the largest square subgrid
 * that has all 1s on its border, or 0 if such a subgrid doesn't exist in the grid.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,1,1],[1,0,1],[1,1,1]]
 * Output: 9
 *
 * Example 2:
 *
 * Input: grid = [[1,1,0,0]]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= grid.length <= 100
 * 1 <= grid[0].length <= 100
 * grid[i][j] is 0 or 1
 *
 */
public class Largest1BorderedSquare {

    // V0
    // IDEA: DP (RUN LENGTHS OF 1s GOING DOWN / RIGHT) + ENUMERATE SQUARES LARGEST FIRST
    /**
     * down[r][c]  = consecutive 1s going DOWN  from (r, c), itself included
     * right[r][c] = consecutive 1s going RIGHT from (r, c), itself included
     *
     * scanning bottom-right -> top-left:
     *   down[r][c]  = grid[r][c] == 1 ? down[r+1][c] + 1 : 0
     *   right[r][c] = grid[r][c] == 1 ? right[r][c+1] + 1 : 0
     *
     * a square with top-left (r, c) and side k has an all-1 border iff
     *   right[r][c] >= k and down[r][c] >= k                (top and left edges)
     *   right[r+k-1][c] >= k and down[r][c+k-1] >= k        (bottom and right edges)
     */
    /**
     * time = O(M * N * min(M, N))
     * space = O(M * N)
     */
    public int largest1BorderedSquare(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        // one extra row / column of zeros so the recurrence needs no bounds check
        int[][] down = new int[rows + 1][cols + 1];
        int[][] right = new int[rows + 1][cols + 1];

        for (int r = rows - 1; r >= 0; r--) {
            for (int c = cols - 1; c >= 0; c--) {
                if (grid[r][c] == 1) {
                    down[r][c] = down[r + 1][c] + 1;
                    right[r][c] = right[r][c + 1] + 1;
                }
            }
        }

        // try the largest side first, so the first valid square is the answer
        for (int side = Math.min(rows, cols); side >= 1; side--) {
            for (int r = 0; r + side <= rows; r++) {
                for (int c = 0; c + side <= cols; c++) {
                    boolean topAndLeft = right[r][c] >= side && down[r][c] >= side;
                    boolean bottom = right[r + side - 1][c] >= side;
                    boolean rightEdge = down[r][c + side - 1] >= side;
                    if (topAndLeft && bottom && rightEdge) {
                        return side * side;
                    }
                }
            }
        }
        return 0;
    }
}
