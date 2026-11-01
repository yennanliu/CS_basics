package LeetCodeJava.Array;

// https://leetcode.com/problems/find-the-minimum-area-to-cover-all-ones-ii/description/

/**
 * 3197. Find the Minimum Area to Cover All Ones II
 * Hard
 *
 * You are given a 2D binary array grid. You need to find 3 non-overlapping rectangles having non-zero areas with horizontal and vertical sides such that all the 1's in grid lie inside these rectangles.
 *
 * Return the minimum possible sum of the area of these rectangles.
 *
 * Note that the rectangles are allowed to touch.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,0,1],[1,1,1]]
 * Output: 5
 * Explanation:
 * The 1's at (0, 0) and (1, 0) are covered by a rectangle of area 2.
 * The 1's at (0, 2) and (1, 2) are covered by a rectangle of area 2.
 * The 1 at (1, 1) is covered by a rectangle of area 1.
 *
 * Example 2:
 *
 * Input: grid = [[1,0,1,0],[0,1,0,1]]
 * Output: 5
 * Explanation:
 * The 1's at (0, 0) and (0, 2) are covered by a rectangle of area 3.
 * The 1 at (1, 1) is covered by a rectangle of area 1.
 * The 1 at (1, 3) is covered by a rectangle of area 1.
 *
 *
 * Constraints:
 *
 * 1 <= grid.length, grid[i].length <= 30
 * grid[i][j] is either 0 or 1.
 * The input is generated such that there are at least three 1's in grid.
 *
 */
public class FindTheMinimumAreaToCoverAllOnesII {

    // V0
    // IDEA: THREE NON-OVERLAPPING RECTANGLES = TWO GUILLOTINE CUTS, SIX SHAPES
    // three non-overlapping rectangles covering the 1s can always be separated by
    // cutting the board twice:
    //   two horizontal cuts (three bands) / two vertical cuts (three strips) /
    //   one horizontal cut, then a vertical cut on the top or the bottom part /
    //   one vertical cut, then a horizontal cut on the left or the right part.
    // the best rectangle for a region is the bounding box of its 1s (0 if empty),
    // so enumerate the cuts and add three bounding-box areas.
    /**
     * time = O((M^2 + N^2 + M * N) * M * N)
     * space = O(1)
     */
    public int minimumSum(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int best = Integer.MAX_VALUE;

        // three horizontal bands: rows [0, a), [a, b), [b, rows)
        for (int a = 1; a < rows; a++) {
            for (int b = a + 1; b < rows; b++) {
                int sum = boundingArea(grid, 0, a, 0, cols)
                        + boundingArea(grid, a, b, 0, cols)
                        + boundingArea(grid, b, rows, 0, cols);
                best = Math.min(best, sum);
            }
        }

        // three vertical strips: cols [0, a), [a, b), [b, cols)
        for (int a = 1; a < cols; a++) {
            for (int b = a + 1; b < cols; b++) {
                int sum = boundingArea(grid, 0, rows, 0, a)
                        + boundingArea(grid, 0, rows, a, b)
                        + boundingArea(grid, 0, rows, b, cols);
                best = Math.min(best, sum);
            }
        }

        // one horizontal cut at row a and one vertical cut at column c
        for (int a = 1; a < rows; a++) {
            for (int c = 1; c < cols; c++) {
                // top part split in two, bottom part whole
                int topSplit = boundingArea(grid, 0, a, 0, c)
                        + boundingArea(grid, 0, a, c, cols)
                        + boundingArea(grid, a, rows, 0, cols);
                // top part whole, bottom part split in two
                int bottomSplit = boundingArea(grid, 0, a, 0, cols)
                        + boundingArea(grid, a, rows, 0, c)
                        + boundingArea(grid, a, rows, c, cols);
                // left part split in two, right part whole
                int leftSplit = boundingArea(grid, 0, a, 0, c)
                        + boundingArea(grid, a, rows, 0, c)
                        + boundingArea(grid, 0, rows, c, cols);
                // left part whole, right part split in two
                int rightSplit = boundingArea(grid, 0, rows, 0, c)
                        + boundingArea(grid, 0, a, c, cols)
                        + boundingArea(grid, a, rows, c, cols);

                best = Math.min(best, Math.min(topSplit, bottomSplit));
                best = Math.min(best, Math.min(leftSplit, rightSplit));
            }
        }
        return best;
    }

    /** Area of the bounding box of the 1s in rows [rowStart, rowEnd) x cols [colStart, colEnd); 0 if none. */
    private int boundingArea(int[][] grid, int rowStart, int rowEnd, int colStart, int colEnd) {
        int top = Integer.MAX_VALUE;
        int bottom = -1;
        int left = Integer.MAX_VALUE;
        int right = -1;

        for (int r = rowStart; r < rowEnd; r++) {
            for (int c = colStart; c < colEnd; c++) {
                if (grid[r][c] == 1) {
                    top = Math.min(top, r);
                    bottom = Math.max(bottom, r);
                    left = Math.min(left, c);
                    right = Math.max(right, c);
                }
            }
        }

        // an empty region needs no rectangle
        if (bottom == -1) {
            return 0;
        }
        return (bottom - top + 1) * (right - left + 1);
    }
}
