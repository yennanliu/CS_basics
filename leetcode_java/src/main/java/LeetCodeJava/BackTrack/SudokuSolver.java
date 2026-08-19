package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/sudoku-solver/

/**
 *  37. Sudoku Solver
 *  Hard
 *
 *  Write a program to solve a Sudoku puzzle by filling the empty cells.
 *
 *  A sudoku solution must satisfy all of the following rules:
 *   1. Each of the digits 1-9 must occur exactly once in each row.
 *   2. Each of the digits 1-9 must occur exactly once in each column.
 *   3. Each of the digits 1-9 must occur exactly once in each of the 9 3x3 sub-boxes.
 *
 *  The '.' character indicates empty cells.
 *
 *  Example 1:
 *   Input: board = [["5","3",".",".","7",".",".",".","."], ... ]
 *   Output: the (unique) filled board.
 *
 *  Constraints:
 *   board.length == 9
 *   board[i].length == 9
 *   board[i][j] is a digit or '.'.
 *   It is guaranteed that the input board has only one solution.
 */
public class SudokuSolver {

    // V0
    // IDEA: backtracking with row / col / box "seen digit" boolean tables
    /**
     * time = O(9^m), m = number of empty cells
     * space = O(m) recursion depth (+ O(1) for the fixed 9x9 tables)
     */
    public void solveSudoku(char[][] board) {
        if (board == null || board.length != 9 || board[0].length != 9) {
            return;
        }
        boolean[][] rows = new boolean[9][9];
        boolean[][] cols = new boolean[9][9];
        boolean[][] boxes = new boolean[9][9];

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] != '.') {
                    int d = board[r][c] - '1';
                    rows[r][d] = true;
                    cols[c][d] = true;
                    boxes[boxIdx(r, c)][d] = true;
                }
            }
        }
        backtrack(board, 0, rows, cols, boxes);
    }

    private int boxIdx(int r, int c) {
        return (r / 3) * 3 + (c / 3);
    }

    private boolean backtrack(char[][] board, int pos,
                              boolean[][] rows, boolean[][] cols, boolean[][] boxes) {
        if (pos == 81) {
            return true;
        }
        int r = pos / 9;
        int c = pos % 9;
        if (board[r][c] != '.') {
            return backtrack(board, pos + 1, rows, cols, boxes);
        }

        int b = boxIdx(r, c);
        for (int d = 0; d < 9; d++) {
            if (rows[r][d] || cols[c][d] || boxes[b][d]) {
                continue;
            }
            rows[r][d] = true;
            cols[c][d] = true;
            boxes[b][d] = true;
            board[r][c] = (char) ('1' + d);

            if (backtrack(board, pos + 1, rows, cols, boxes)) {
                return true;
            }

            // undo
            board[r][c] = '.';
            rows[r][d] = false;
            cols[c][d] = false;
            boxes[b][d] = false;
        }
        return false;
    }
}
