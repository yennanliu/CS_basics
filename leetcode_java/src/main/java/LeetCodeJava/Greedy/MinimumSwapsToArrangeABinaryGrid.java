package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-swaps-to-arrange-a-binary-grid/description/

/**
 * 1536. Minimum Swaps to Arrange a Binary Grid
 * Medium
 *
 * Given an n x n binary grid, in one step you can choose two adjacent rows of the
 * grid and swap them.
 *
 * A grid is said to be valid if all the cells above the main diagonal are zeros.
 *
 * Return the minimum number of steps needed to make the grid valid, or -1 if the
 * grid cannot be valid.
 *
 * The main diagonal of a grid is the diagonal that starts at cell (1, 1) and ends
 * at cell (n, n).
 *
 *
 * Example 1:
 *
 * Input: grid = [[0,0,1],[1,1,0],[1,0,0]]
 * Output: 3
 *
 * Example 2:
 *
 * Input: grid = [[0,1,1,0],[0,1,1,0],[0,1,1,0],[0,1,1,0]]
 * Output: -1
 * Explanation: All rows are similar, swaps have no effect on the grid.
 *
 * Example 3:
 *
 * Input: grid = [[1,0,0],[1,1,0],[1,1,1]]
 * Output: 0
 *
 *
 * Constraints:
 *
 * n == grid.length == grid[i].length
 * 1 <= n <= 200
 * grid[i][j] is either 0 or 1
 *
 */
public class MinimumSwapsToArrangeABinaryGrid {

    // V0
    // IDEA: REDUCE EACH ROW TO ITS TRAILING-ZERO COUNT, THEN GREEDY BUBBLING
    //       row placed at position i needs at least (n - 1 - i) trailing zeros.
    //       fill positions top-down: take the NEAREST qualifying row below and
    //       bubble it up with adjacent swaps. The rows it skips only move one
    //       slot down, which never hurts them (they need a looser slot anyway).
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int minSwaps(int[][] grid) {
        int n = grid.length;

        // trailingZeros[i] = length of the run of zeros at the end of row i
        int[] trailingZeros = new int[n];
        for (int i = 0; i < n; i++) {
            int count = 0;
            for (int j = n - 1; j >= 0; j--) {
                if (grid[i][j] != 0) {
                    break;
                }
                count++;
            }
            trailingZeros[i] = count;
        }

        int swaps = 0;
        for (int i = 0; i < n; i++) {
            int need = n - 1 - i;

            // find the first row at or below i with enough trailing zeros
            int found = -1;
            for (int j = i; j < n; j++) {
                if (trailingZeros[j] >= need) {
                    found = j;
                    break;
                }
            }
            if (found == -1) {
                return -1;
            }

            // bubbling row `found` up to position i costs (found - i) swaps
            swaps += found - i;
            for (int k = found; k > i; k--) {
                int tmp = trailingZeros[k];
                trailingZeros[k] = trailingZeros[k - 1];
                trailingZeros[k - 1] = tmp;
            }
        }
        return swaps;
    }
}
