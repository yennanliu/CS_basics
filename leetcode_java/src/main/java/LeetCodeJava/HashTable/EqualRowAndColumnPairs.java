package LeetCodeJava.HashTable;

// https://leetcode.com/problems/equal-row-and-column-pairs/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2352. Equal Row and Column Pairs
 * Medium
 *
 * Given a 0-indexed n x n integer matrix grid, return the number of pairs (ri, cj) such that row ri and column cj are equal.
 *
 * A row and column pair is considered equal if they contain the same elements in the same order (i.e., an equal array).
 *
 *
 * Example 1:
 *
 * Input: grid = [[3,2,1],[1,7,6],[2,7,7]]
 * Output: 1
 * Explanation: There is 1 equal row and column pair:
 * - (Row 2, Column 1): [2,7,7]
 *
 * Example 2:
 *
 * Input: grid = [[3,1,2,2],[1,4,4,5],[2,4,2,2],[2,4,2,2]]
 * Output: 3
 * Explanation: There are 3 equal row and column pairs:
 * - (Row 0, Column 0): [3,1,2,2]
 * - (Row 2, Column 2): [2,4,2,2]
 * - (Row 3, Column 2): [2,4,2,2]
 *
 *
 * Constraints:
 *
 * n == grid.length == grid[i].length
 * 1 <= n <= 200
 * 1 <= grid[i][j] <= 10^5
 *
 */
public class EqualRowAndColumnPairs {

    // V0
    // IDEA: HASH MAP OF ROWS (as lists), THEN LOOK EACH COLUMN UP
    //   count every row; each column then forms as many pairs as there are rows equal
    //   to it. beats the O(n^3) compare-every-row-with-every-column loop.
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int equalPairs(int[][] grid) {
        int n = grid.length;

        // List<Integer> has value-based equals/hashCode, so it works as a map key
        Map<List<Integer>, Integer> rowCount = new HashMap<>();
        for (int[] row : grid) {
            List<Integer> key = new ArrayList<>();
            for (int value : row) {
                key.add(value);
            }
            rowCount.put(key, rowCount.getOrDefault(key, 0) + 1);
        }

        int pairs = 0;
        for (int c = 0; c < n; c++) {
            List<Integer> column = new ArrayList<>();
            for (int r = 0; r < n; r++) {
                column.add(grid[r][c]);
            }
            pairs += rowCount.getOrDefault(column, 0);
        }
        return pairs;
    }
}
