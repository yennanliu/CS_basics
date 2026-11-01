package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-operations-to-write-the-letter-y-on-a-grid/description/

/**
 * 3071. Minimum Operations to Write the Letter Y on a Grid
 * Medium
 *
 * You are given a 0-indexed n x n grid where n is odd, and grid[r][c] is 0, 1, or 2.
 *
 * We say that a cell belongs to the Letter Y if it belongs to one of the following:
 *
 * The diagonal starting at the top-left cell and ending at the center cell of the grid.
 * The diagonal starting at the top-right cell and ending at the center cell of the grid.
 * The vertical line starting at the center cell and ending at the bottom border of the grid.
 *
 * The Letter Y is written on the grid if and only if:
 *
 * All values at cells belonging to the Y are equal.
 * All values at cells not belonging to the Y are equal.
 * The values at cells belonging to the Y are different from the values at cells not belonging to the Y.
 *
 * Return the minimum number of operations needed to write the letter Y on the grid given that in one operation you can change the value at any cell to 0, 1, or 2.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,2,2],[1,1,0],[0,1,0]]
 * Output: 3
 * Explanation: We can write Y on the grid by applying the changes highlighted in blue in the image above. After the operations, all cells that belong to Y, denoted in bold, have the same value of 1 while those that do not belong to Y are equal to 0.
 * It can be shown that 3 is the minimum number of operations needed to write Y on the grid.
 *
 * Example 2:
 *
 * Input: grid = [[0,1,0,1,0],[2,1,0,1,2],[2,2,2,0,1],[2,2,2,2,2],[2,1,2,2,2]]
 * Output: 12
 * Explanation: We can write Y on the grid by applying the changes highlighted in blue in the image above. After the operations, all cells that belong to Y, denoted in bold, have the same value of 0 while those that do not belong to Y are equal to 2.
 * It can be shown that 12 is the minimum number of operations needed to write Y on the grid.
 *
 *
 * Constraints:
 *
 * 3 <= n <= 49
 * n == grid.length == grid[i].length
 * 0 <= grid[i][j] <= 2
 * n is odd.
 *
 */
public class MinimumOperationsToWriteTheLetterYOnAGrid {

    // V0
    // IDEA: ONLY 6 TARGET COLOURINGS EXIST - COUNT ONCE, THEN SCORE EACH
    // the final grid is (value on the Y, value off the Y) with the two different:
    // 3 * 2 = 6 choices. tally each value inside and outside the Y once, then
    // cost(a, b) = (Y cells not already a) + (non-Y cells not already b).
    /**
     * time = O(N^2)
     * space = O(1)
     */
    public int minimumOperationsToWriteY(int[][] grid) {
        int n = grid.length;
        int center = n / 2;
        int[] inY = new int[3];
        int[] outY = new int[3];

        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                // above the centre the Y is the two diagonals; from the centre down it is the stem
                boolean onDiagonals = r < center && (r == c || r + c == n - 1);
                boolean onStem = r >= center && c == center;
                if (onDiagonals || onStem) {
                    inY[grid[r][c]]++;
                } else {
                    outY[grid[r][c]]++;
                }
            }
        }

        int totalIn = inY[0] + inY[1] + inY[2];
        int totalOut = outY[0] + outY[1] + outY[2];
        int best = Integer.MAX_VALUE;
        for (int yValue = 0; yValue < 3; yValue++) {
            for (int otherValue = 0; otherValue < 3; otherValue++) {
                if (yValue == otherValue) {
                    continue;
                }
                int cost = (totalIn - inY[yValue]) + (totalOut - outY[otherValue]);
                best = Math.min(best, cost);
            }
        }
        return best;
    }
}
