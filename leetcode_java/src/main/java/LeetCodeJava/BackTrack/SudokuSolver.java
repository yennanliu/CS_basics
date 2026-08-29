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

    // V1
    // IDEA: bitmask constraint propagation + MRV heuristic (always fill the empty
    //       cell that has the FEWEST remaining candidates first) -> far smaller
    //       search tree than scanning cells in fixed order
    /**
     * time = O(9^m) worst case, m = number of empty cells (MRV prunes heavily in practice)
     * space = O(m) recursion depth
     */
    public void solveSudoku_1(char[][] board) {
        if (board == null || board.length != 9 || board[0].length != 9) {
            return;
        }
        int[] rowMask = new int[9];
        int[] colMask = new int[9];
        int[] boxMask = new int[9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] != '.') {
                    int bit = 1 << (board[r][c] - '1');
                    rowMask[r] |= bit;
                    colMask[c] |= bit;
                    boxMask[boxIdx(r, c)] |= bit;
                }
            }
        }
        solve_1(board, rowMask, colMask, boxMask);
    }

    private boolean solve_1(char[][] board, int[] rowMask, int[] colMask, int[] boxMask) {
        // MRV: pick the empty cell with the least candidates
        int bestR = -1;
        int bestC = -1;
        int bestCand = 0;
        int bestCnt = 10;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] != '.') {
                    continue;
                }
                int used = rowMask[r] | colMask[c] | boxMask[boxIdx(r, c)];
                int cand = ~used & 0x1FF;
                int cnt = Integer.bitCount(cand);
                if (cnt == 0) {
                    return false; // dead end
                }
                if (cnt < bestCnt) {
                    bestCnt = cnt;
                    bestCand = cand;
                    bestR = r;
                    bestC = c;
                    if (cnt == 1) {
                        break;
                    }
                }
            }
        }
        if (bestR == -1) {
            return true; // no empty cell left
        }

        int b = boxIdx(bestR, bestC);
        int cand = bestCand;
        while (cand != 0) {
            int bit = cand & -cand; // lowest set bit
            cand -= bit;
            rowMask[bestR] |= bit;
            colMask[bestC] |= bit;
            boxMask[b] |= bit;
            board[bestR][bestC] = (char) ('1' + Integer.numberOfTrailingZeros(bit));

            if (solve_1(board, rowMask, colMask, boxMask)) {
                return true;
            }

            board[bestR][bestC] = '.';
            rowMask[bestR] ^= bit;
            colMask[bestC] ^= bit;
            boxMask[b] ^= bit;
        }
        return false;
    }

    // V2
    // IDEA: brute force backtracking - no precomputed state at all, every
    //       placement is validated by re-scanning its row, column and 3x3 box.
    //       Kept as a readable correctness reference.
    /**
     * time = O(9^m * 9), m = number of empty cells
     * space = O(m) recursion depth
     */
    public void solveSudoku_2(char[][] board) {
        if (board == null || board.length != 9 || board[0].length != 9) {
            return;
        }
        solve_2(board, 0);
    }

    private boolean solve_2(char[][] board, int pos) {
        if (pos == 81) {
            return true;
        }
        int r = pos / 9;
        int c = pos % 9;
        if (board[r][c] != '.') {
            return solve_2(board, pos + 1);
        }
        for (char d = '1'; d <= '9'; d++) {
            if (!isValid_2(board, r, c, d)) {
                continue;
            }
            board[r][c] = d;
            if (solve_2(board, pos + 1)) {
                return true;
            }
            board[r][c] = '.';
        }
        return false;
    }

    private boolean isValid_2(char[][] board, int r, int c, char d) {
        for (int i = 0; i < 9; i++) {
            if (board[r][i] == d) {
                return false;
            }
            if (board[i][c] == d) {
                return false;
            }
            int br = (r / 3) * 3 + i / 3;
            int bc = (c / 3) * 3 + i % 3;
            if (board[br][bc] == d) {
                return false;
            }
        }
        return true;
    }
}
