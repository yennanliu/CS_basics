package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/n-queens-ii/description/

import java.util.*;

/**
 * 52. N-Queens II
 * Solved
 * Hard
 * Topics
 * Companies
 * The n-queens puzzle is the problem of placing n queens on an n x n chessboard such that no two queens attack each other.
 *
 * Given an integer n, return the number of distinct solutions to the n-queens puzzle.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: n = 4
 * Output: 2
 * Explanation: There are two distinct solutions to the 4-queens puzzle as shown.
 * Example 2:
 *
 * Input: n = 1
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= n <= 9
 *
 *
 */
public class NQueens2 {

    // V0
    // IDEA: N QUEENS (LC 51) + unique cnt
    // time: O(N!), space: O(N^2)
    public int totalNQueens(int n) {
        // edge
        if (n == 1 || n == 0) {
            return n; // ???
        }

        // LC 51 N QUEENS
        List<List<String>> result = new ArrayList<>();

        char[][] board = new char[n][n];
        for (char[] row : board)
            Arrays.fill(row, '.');

        boolean[] cols = new boolean[n]; // tracks columns
        boolean[] diag1 = new boolean[2 * n - 1]; // tracks ↘ diagonals (row - col + n - 1)
        boolean[] diag2 = new boolean[2 * n - 1]; // tracks ↙ diagonals (row + col)

        nQueenBacktrack(0, board, result, cols, diag1, diag2);

        return result.size();
    }

    private void nQueenBacktrack(int row, char[][] board, List<List<String>> result,
                                 boolean[] cols, boolean[] diag1, boolean[] diag2) {
        int n = board.length;
        if (row == n) {
            result.add(constructBoard(board));
            return;
        }

        for (int col = 0; col < n; col++) {
            int d1 = row - col + n - 1; // ↘ diagonal index
            int d2 = row + col; // ↙ diagonal index

            if (cols[col] || diag1[d1] || diag2[d2])
                continue;

            // Place queen
            board[row][col] = 'Q';
            cols[col] = diag1[d1] = diag2[d2] = true;

            nQueenBacktrack(row + 1, board, result, cols, diag1, diag2);

            // Backtrack
            board[row][col] = '.';
            cols[col] = diag1[d1] = diag2[d2] = false;
        }
    }

    private List<String> constructBoard(char[][] board) {
        List<String> res = new ArrayList<>();
        for (char[] row : board) {
            res.add(new String(row));
        }
        return res;
    }

    // V1
    // https://www.youtube.com/watch?v=nalYyLZgvCY
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0052-n-queens-ii.java
    // time: O(N!), space: O(N)
    int count = 0;

    public int totalNQueens_1(int n) {
        Set<Integer> colSet = new HashSet<>();
        Set<Integer> posDiagSet = new HashSet<>(); // (r + c)
        Set<Integer> negDiagSet = new HashSet<>(); // (r - c)
        backtrack(0, n, colSet, posDiagSet, negDiagSet);
        return count;
    }

    private void backtrack(
            int row,
            int n,
            Set<Integer> colSet,
            Set<Integer> posDiagSet,
            Set<Integer> negDiagSet) {
        if (row == n) {
            count += 1;
            return;
        }

        for (int col = 0; col < n; col++) {
            if (colSet.contains(col)
                    || posDiagSet.contains(row + col)
                    || negDiagSet.contains(row - col)) {
                continue;
            }
            colSet.add(col);
            posDiagSet.add(row + col);
            negDiagSet.add(row - col);
            backtrack(row + 1, n, colSet, posDiagSet, negDiagSet);
            colSet.remove(col);
            posDiagSet.remove(row + col);
            negDiagSet.remove(row - col);
        }
    }


    // V2
    // https://leetcode.com/problems/n-queens-ii/solutions/6667257/java-solution-by-_aman_varma-u29o/
    // time: O(N!), space: O(N^2)
    public boolean isSafe(int row, int col, int[] arr1, int[] arr2, int[] leftRow, int n) {
        return arr1[row + col] == 0 && arr2[(n - 1 - row) + col] == 0 && leftRow[row] == 0;

    }

    public void solve(char[][] board, int[] arr1, int[] arr2, int[] leftRow, int[] count, int col, int n) {
        if (col == n) {
            count[0]++;
            return;
        }

        for (int i = 0; i < n; i++) {
            if (isSafe(i, col, arr1, arr2, leftRow, n)) {
                board[i][col] = 'Q';
                arr1[i + col] = 1;
                arr2[(n - i - 1) + col] = 1;
                leftRow[i] = 1;

                solve(board, arr1, arr2, leftRow, count, col + 1, n);

                board[i][col] = '.';
                arr1[i + col] = 0;
                arr2[(n - i - 1) + col] = 0;
                leftRow[i] = 0;
            }
        }
    }

    public int totalNQueens_2(int n) {
        int[] count = new int[1];
        char[][] board = new char[n][n];
        int[] arr1 = new int[2 * n - 1];
        int[] arr2 = new int[2 * n - 1];
        int leftRow[] = new int[n];

        for (int i = 0; i < n; i++)
            Arrays.fill(board[i], '.');

        solve(board, arr1, arr2, leftRow, count, 0, n);

        return count[0];
    }


    // V3
    // https://leetcode.com/problems/n-queens-ii/solutions/6626336/n-queens-ii-count-valid-queen-placements-t9f6/
    // 🛡️ Check if it's safe to place a queen at (row, col)
    // time: O(N!), space: O(N^2)
    private boolean isSafe(int row, int col, int n, char[][] board) {
        // ⬅️ Check row
        for (int c = 0; c < col; c++)
            if (board[row][c] == 'Q')
                return false;

        // ↖️ Check upper-left diagonal
        for (int r = row, c = col; r >= 0 && c >= 0; r--, c--)
            if (board[r][c] == 'Q')
                return false;

        // ↙️ Check lower-left diagonal
        for (int r = row, c = col; r < n && c >= 0; r++, c--)
            if (board[r][c] == 'Q')
                return false;

        return true;
    }

    // 🔁 Try placing queens column by column
    // time: O(N!), space: O(N^2)
    private void backtrack(int col, int n, char[][] board, int[] count) {
        if (col == n) {
            count[0]++; // 🎉 Found a valid way!
            return;
        }

        for (int row = 0; row < n; row++) {
            if (isSafe(row, col, n, board)) {
                board[row][col] = 'Q'; // 👑 Place the queen
                backtrack(col + 1, n, board, count);
                board[row][col] = '.'; // ↩️ Remove (backtrack)
            }
        }
    }

    public int totalNQueens_3(int n) {
        char[][] board = new char[n][n];
        for (char[] row : board)
            Arrays.fill(row, '.');

        int[] count = new int[1]; // 🧮 Mutable counter
        backtrack(0, n, board, count);
        return count[0];
    }


}
