package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/select-cells-in-grid-with-maximum-score/description/

import java.util.*;

/**
 * 3276. Select Cells in Grid With Maximum Score
 * Hard
 *
 * You are given a 2D matrix grid consisting of positive integers.
 *
 * You have to select one or more cells from the matrix such that the following conditions are satisfied:
 *
 * No two selected cells are in the same row of the matrix.
 * The values in the set of selected cells are unique.
 *
 * Your score will be the sum of the values of the selected cells.
 *
 * Return the maximum score you can achieve.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,2,3],[4,3,2],[1,1,1]]
 * Output: 8
 * Explanation:
 * We can select the cells with values 1, 3, and 4 that are colored above.
 *
 * Example 2:
 *
 * Input: grid = [[8,7,6],[8,3,2]]
 * Output: 15
 * Explanation:
 * We can select the cells with values 7 and 8 that are colored above.
 *
 *
 * Constraints:
 *
 * 1 <= grid.length, grid[i].length <= 10
 * 1 <= grid[i][j] <= 100
 *
 */
public class SelectCellsInGridWithMaximumScore {

    // V0
    // IDEA: BITMASK DP OVER THE ROWS, ITERATING THE DISTINCT VALUES
    /**
     *  Two rules: at most one cell per row, and no value used twice.
     *  Sweeping the distinct VALUES makes the second rule automatic -- each value
     *  is processed once, and we decide which single row (if any) claims it.
     *
     *  DP def:
     *     dp[mask] = best score where exactly the rows in `mask` are used
     *                (at most 10 rows -> 1024 masks), -1 = unreachable
     *
     *  DP eq (for each distinct value v):
     *     next[mask | (1 << r)] = max(next[...], dp[mask] + v)
     *         for every row r that contains v and is free in mask
     *     next starts as a copy of dp (the option "skip v").
     *
     *  Building `next` from the OLD dp is what stops one value being taken twice.
     *
     *  Init:   dp[0] = 0
     *  Answer: max over dp
     *
     *  time = O(V * 2^M * M)   (V = distinct values, M = rows)
     *  space = O(2^M + V * M)
     */
    public int maxScore(List<List<Integer>> grid) {
        int rowCount = grid.size();

        // value -> rows that contain it (each row listed once)
        Map<Integer, List<Integer>> rowsWithValue = new HashMap<>();
        for (int r = 0; r < rowCount; r++) {
            Set<Integer> seenInRow = new HashSet<>(grid.get(r));
            for (int value : seenInRow) {
                rowsWithValue.computeIfAbsent(value, x -> new ArrayList<>()).add(r);
            }
        }

        int maskCount = 1 << rowCount;
        int[] dp = new int[maskCount];
        Arrays.fill(dp, -1);
        dp[0] = 0;

        for (Map.Entry<Integer, List<Integer>> entry : rowsWithValue.entrySet()) {
            int value = entry.getKey();
            List<Integer> rows = entry.getValue();

            // option: skip this value entirely
            int[] next = dp.clone();
            for (int mask = 0; mask < maskCount; mask++) {
                if (dp[mask] < 0) {
                    continue;
                }
                for (int r : rows) {
                    // row r already holds a picked cell -> cannot use it again
                    boolean rowUsed = ((mask >> r) & 1) == 1;
                    if (rowUsed) {
                        continue;
                    }
                    int newMask = mask | (1 << r);
                    next[newMask] = Math.max(next[newMask], dp[mask] + value);
                }
            }
            dp = next;
        }

        int best = 0;
        for (int score : dp) {
            best = Math.max(best, score);
        }
        return best;
    }
}
