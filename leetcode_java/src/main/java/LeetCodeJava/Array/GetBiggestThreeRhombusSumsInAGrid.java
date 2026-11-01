package LeetCodeJava.Array;

// https://leetcode.com/problems/get-biggest-three-rhombus-sums-in-a-grid/description/

import java.util.TreeSet;

/**
 * 1878. Get Biggest Three Rhombus Sums in a Grid
 * Medium
 *
 * You are given an m x n integer matrix grid.
 *
 * A rhombus sum is the sum of the elements that form the border of a regular rhombus shape in grid.
 * The rhombus must have the shape of a square rotated 45 degrees with each of the corners centered
 * in a grid cell.
 *
 * Note that the rhombus can have an area of 0, which is the degenerate single-cell rhombus.
 *
 * Return the biggest three distinct rhombus sums in the grid in descending order. If there are less
 * than three distinct values, return all of them.
 *
 *
 * Example 1:
 *
 * Input: grid = [[3,4,5,1,3],[3,3,4,2,3],[20,30,200,40,10],[1,5,5,4,1],[4,3,2,2,5]]
 * Output: [228,216,211]
 *
 * Example 2:
 *
 * Input: grid = [[1,2,3],[4,5,6],[7,8,9]]
 * Output: [20,9,8]
 *
 * Example 3:
 *
 * Input: grid = [[7,7,7]]
 * Output: [7]
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 50
 * 1 <= grid[i][j] <= 10^5
 *
 */
public class GetBiggestThreeRhombusSumsInAGrid {

    private static final int KEEP = 3;

    // V0
    // IDEA: ENUMERATE (TOP CORNER, HALF-DIAGONAL) AND WALK THE BORDER
    // a rhombus is fixed by its top cell (i, j) and half-diagonal k: corners (i, j), (i + k, j + k),
    // (i + 2k, j), (i + k, j - k). Walk the 4 edges (k cells each, corners counted once) and keep the
    // 3 largest DISTINCT sums in a TreeSet. k = 0 is the single cell.
    /**
     * time = O(M * N * K^2), K = min(M, N) / 2
     * space = O(1)
     */
    public int[] getBiggestThree(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        // ascending set of the largest distinct sums seen so far (size <= 3)
        TreeSet<Integer> top = new TreeSet<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                addCandidate(top, grid[i][j]);
                // grow the rhombus while its bottom, left and right corners stay inside the grid
                for (int k = 1; i + 2 * k < rows && j - k >= 0 && j + k < cols; k++) {
                    int border = 0;
                    for (int d = 0; d < k; d++) {
                        border += grid[i + d][j + d];             // top -> right
                        border += grid[i + k + d][j + k - d];     // right -> bottom
                        border += grid[i + 2 * k - d][j - d];     // bottom -> left
                        border += grid[i + k - d][j - k + d];     // left -> top
                    }
                    addCandidate(top, border);
                }
            }
        }

        // emit in descending order
        int[] answer = new int[top.size()];
        int index = 0;
        for (int value : top.descendingSet()) {
            answer[index++] = value;
        }
        return answer;
    }

    /** Adds a sum to the set, then drops the smallest so only the 3 largest distinct remain. */
    private void addCandidate(TreeSet<Integer> top, int sum) {
        top.add(sum);
        if (top.size() > KEEP) {
            top.pollFirst();
        }
    }
}
