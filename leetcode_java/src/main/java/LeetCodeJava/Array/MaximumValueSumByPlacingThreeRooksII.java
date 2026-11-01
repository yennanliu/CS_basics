package LeetCodeJava.Array;

// https://leetcode.com/problems/maximum-value-sum-by-placing-three-rooks-ii/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 3257. Maximum Value Sum by Placing Three Rooks II
 * Hard
 *
 * You are given a m x n 2D array board representing a chessboard, where board[i][j] represents the value of the cell (i, j).
 *
 * Rooks in the same row or column attack each other. You need to place three rooks on the chessboard such that the rooks do not attack each other.
 *
 * Return the maximum sum of the cell values on which the rooks are placed.
 *
 *
 * Example 1:
 *
 * Input: board = [[-3,1,1,1],[-3,1,-3,1],[-3,2,1,1]]
 * Output: 4
 * Explanation:
 * We can place the rooks in the cells (0, 2), (1, 3), and (2, 1) for a sum of 1 + 1 + 2 = 4.
 *
 * Example 2:
 *
 * Input: board = [[1,2,3],[4,5,6],[7,8,9]]
 * Output: 15
 * Explanation:
 * We can place the rooks in the cells (0, 0), (1, 1), and (2, 2) for a sum of 1 + 5 + 9 = 15.
 *
 * Example 3:
 *
 * Input: board = [[1,1,1],[1,1,1],[1,1,1]]
 * Output: 3
 * Explanation:
 * We can place the rooks in the cells (0, 2), (1, 1), and (2, 0) for a sum of 1 + 1 + 1 = 3.
 *
 *
 * Constraints:
 *
 * 3 <= m == board.length <= 500
 * 3 <= n == board[i].length <= 500
 * -10^9 <= board[i][j] <= 10^9
 *
 */
public class MaximumValueSumByPlacingThreeRooksII {

    // V0
    // IDEA: FIX THE MIDDLE ROW + PREFIX / SUFFIX TOP-3 CELLS WITH DISTINCT COLUMNS
    //   at most two columns are ever blocked, so among three cells with pairwise
    //   distinct columns at least one always survives.
    /**
     * time = O(M * N)
     * space = O(M)
     */
    public long maximumValueSum(int[][] board) {
        int rows = board.length;

        // each candidate is {value, column}
        List<List<long[]>> rowTop = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            List<long[]> top = new ArrayList<>();
            for (int c = 0; c < board[r].length; c++) {
                addCandidate(top, board[r][c], c);
            }
            rowTop.add(top);
        }

        // prefix[r] = best 3 distinct-column cells over rows 0..r
        List<List<long[]>> prefix = new ArrayList<>();
        List<long[]> running = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            running = mergeCandidates(running, rowTop.get(r));
            prefix.add(running);
        }

        // suffix[r] = best 3 distinct-column cells over rows r..rows-1
        List<List<long[]>> suffix = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            suffix.add(null);
        }
        running = new ArrayList<>();
        for (int r = rows - 1; r >= 0; r--) {
            running = mergeCandidates(running, rowTop.get(r));
            suffix.set(r, running);
        }

        long best = Long.MIN_VALUE;
        // the middle rook's row; one rook above it, one below it
        for (int mid = 1; mid < rows - 1; mid++) {
            for (long[] middle : rowTop.get(mid)) {
                for (long[] above : prefix.get(mid - 1)) {
                    if (above[1] == middle[1]) {
                        continue;
                    }
                    for (long[] below : suffix.get(mid + 1)) {
                        if (below[1] == middle[1] || below[1] == above[1]) {
                            continue;
                        }
                        best = Math.max(best, above[0] + middle[0] + below[0]);
                    }
                }
            }
        }
        return best;
    }

    /** Merges two candidate lists into a new list of the best 3 with distinct columns. */
    private List<long[]> mergeCandidates(List<long[]> first, List<long[]> second) {
        List<long[]> merged = new ArrayList<>();
        for (long[] candidate : first) {
            addCandidate(merged, candidate[0], (int) candidate[1]);
        }
        for (long[] candidate : second) {
            addCandidate(merged, candidate[0], (int) candidate[1]);
        }
        return merged;
    }

    /**
     * Inserts (value, column) into a list kept sorted by value descending, holding at most
     * 3 entries with pairwise distinct columns (a column keeps only its best value).
     */
    private void addCandidate(List<long[]> top, long value, int column) {
        // same column already present: keep the larger value only
        for (int i = 0; i < top.size(); i++) {
            if (top.get(i)[1] == column) {
                if (top.get(i)[0] >= value) {
                    return;
                }
                top.remove(i);
                break;
            }
        }

        // find the insertion point that keeps the list sorted descending
        int position = 0;
        while (position < top.size() && top.get(position)[0] >= value) {
            position++;
        }
        if (position >= 3) {
            return;
        }
        top.add(position, new long[] {value, column});
        if (top.size() > 3) {
            top.remove(3);
        }
    }
}
