package LeetCodeJava.Array;

// https://leetcode.com/problems/game-of-life/

/**
 *  289. Game of Life
 *  Medium
 *
 *  The board is made up of an m x n grid of cells, where each cell has an
 *  initial state: live (1) or dead (0). Each cell interacts with its eight
 *  neighbors using the following four rules:
 *
 *   1. Any live cell with fewer than two live neighbors dies.
 *   2. Any live cell with two or three live neighbors lives on.
 *   3. Any live cell with more than three live neighbors dies.
 *   4. Any dead cell with exactly three live neighbors becomes a live cell.
 *
 *  The next state is created by applying the above rules simultaneously to
 *  every cell in the current state. Update board in-place.
 *
 *  Example 1:
 *   Input: board = [[0,1,0],[0,0,1],[1,1,1],[0,0,0]]
 *   Output: [[0,0,0],[1,0,1],[0,1,1],[0,1,0]]
 *
 *  Example 2:
 *   Input: board = [[1,1],[1,0]]
 *   Output: [[1,1],[1,1]]
 *
 *  Constraints:
 *   m == board.length, n == board[i].length
 *   1 <= m, n <= 25
 *   board[i][j] is 0 or 1
 */
public class GameOfLife {

    // V0
    // IDEA: IN-PLACE VIA 2-BIT ENCODING
    //       bit 0 = current state, bit 1 = next state; shift right at the end
    /**
     * time = O(m * n)
     * space = O(1)
     */
    public void gameOfLife(int[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) {
            return;
        }

        int m = board.length;
        int n = board[0].length;

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                int liveNeighbors = countLiveNeighbors(board, r, c);
                int cur = board[r][c] & 1;

                int next;
                if (cur == 1) {
                    next = (liveNeighbors == 2 || liveNeighbors == 3) ? 1 : 0;
                } else {
                    next = (liveNeighbors == 3) ? 1 : 0;
                }

                // NOTE !!! store next state in the 2nd bit
                board[r][c] = (next << 1) | cur;
            }
        }

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                board[r][c] >>= 1;
            }
        }
    }

    private int countLiveNeighbors(int[][] board, int r, int c) {
        int m = board.length;
        int n = board[0].length;
        int cnt = 0;

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }
                int nr = r + dr;
                int nc = c + dc;
                if (nr < 0 || nr >= m || nc < 0 || nc >= n) {
                    continue;
                }
                // NOTE !!! only look at bit 0 (the ORIGINAL state)
                cnt += board[nr][nc] & 1;
            }
        }

        return cnt;
    }
}
