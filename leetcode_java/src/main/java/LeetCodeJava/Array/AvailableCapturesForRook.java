package LeetCodeJava.Array;

// https://leetcode.com/problems/available-captures-for-rook/description/
/**
 * 999. Available Captures for Rook
 * Easy
 *
 * You are given an 8 x 8 matrix representing a chessboard. There is exactly one white rook
 * represented by 'R', some number of white bishops 'B', and some number of black pawns 'p'.
 * Empty squares are represented by '.'.
 *
 * A rook can move any number of squares horizontally or vertically (up, down, left, right)
 * until it reaches another piece or the edge of the board. A rook is attacking a pawn if it
 * can move to the pawn's square in one move.
 *
 * Note: A rook cannot move through other pieces, such as bishops or pawns. This means a rook
 * cannot attack a pawn if there is another piece blocking the path.
 *
 * Return the number of pawns the white rook is attacking.
 *
 * Example 1:
 *
 * Input: board = [[".",".",".",".",".",".",".","."],[".",".",".","p",".",".",".","."],
 *                 [".",".",".","R",".",".",".","p"],[".",".",".",".",".",".",".","."],
 *                 [".",".",".",".",".",".",".","."],[".",".",".","p",".",".",".","."],
 *                 [".",".",".",".",".",".",".","."],[".",".",".",".",".",".",".","."]]
 * Output: 3
 * Explanation: In this example, the rook is attacking all the pawns.
 *
 * Example 2:
 *
 * Input: board = [[".",".",".",".",".",".",".","."],[".","p","p","p","p","p",".","."],
 *                 [".","p","p","B","p","p",".","."],[".","p","B","R","B","p",".","."],
 *                 [".","p","p","B","p","p",".","."],[".","p","p","p","p","p",".","."],
 *                 [".",".",".",".",".",".",".","."],[".",".",".",".",".",".",".","."]]
 * Output: 0
 * Explanation: The bishops are blocking the rook from attacking any of the pawns.
 *
 * Example 3:
 *
 * Input: board = [[".",".",".",".",".",".",".","."],[".",".",".","p",".",".",".","."],
 *                 [".",".",".","p",".",".",".","."],["p","p",".","R",".","p","B","."],
 *                 [".",".",".",".",".",".",".","."],[".",".",".","B",".",".",".","."],
 *                 [".",".",".","p",".",".",".","."],[".",".",".",".",".",".",".","."]]
 * Output: 3
 * Explanation: The rook is attacking the pawns at positions b5, d6, and f5.
 *
 * Constraints:
 *
 * board.length == 8
 * board[i].length == 8
 * board[i][j] is either 'R', '.', 'B', or 'p'
 * There is exactly one cell with board[i][j] == 'R'
 *
 */
public class AvailableCapturesForRook {

    // V0
    // IDEA: SIMULATION - ray cast in the 4 rook directions
    /**
     *  Locate 'R', then walk OUTWARDS in each of the 4 directions until we
     *  either leave the board or hit a piece:
     *     - hit 'p' -> that pawn is captured, STOP that ray
     *     - hit 'B' -> blocked, STOP that ray with no capture
     *
     *  time  = O(m * n)   // scanning the board to find the rook dominates
     *  space = O(1)
     */
    public int numRookCaptures(char[][] board) {
        int rows = board.length;
        int cols = board[0].length;

        // locate the rook
        int ri = -1;
        int rj = -1;
        for (int i = 0; i < rows && ri == -1; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 'R') {
                    ri = i;
                    rj = j;
                    break;
                }
            }
        }

        int[][] dirs = new int[][] { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        int res = 0;
        for (int[] d : dirs) {
            int x = ri + d[0];
            int y = rj + d[1];

            /** NOTE !!!
             *
             *  we keep walking along ONE direction until board edge
             *  or the FIRST piece on the way
             */
            while (x >= 0 && x < rows && y >= 0 && y < cols) {
                if (board[x][y] == 'p') {
                    res += 1;
                    break; // captured -> this ray is done
                }
                if (board[x][y] == 'B') {
                    break; // blocked by our own bishop
                }
                x += d[0];
                y += d[1];
            }
        }

        return res;
    }


    // V1
    // IDEA: SCAN THE ROOK'S ROW AND COLUMN AS TWO 1-D STRINGS
    /**
     *  Instead of 4 ray walks, extract the rook's row and its column as two
     *  1-D arrays and reduce the problem to: `in this line, what is the first
     *  non-'.' cell on each side of the rook?`
     *
     *  time  = O(m * n)
     *  space = O(m + n)
     */
    public int numRookCaptures_1(char[][] board) {
        int rows = board.length;
        int cols = board[0].length;

        int ri = -1;
        int rj = -1;
        for (int i = 0; i < rows && ri == -1; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 'R') {
                    ri = i;
                    rj = j;
                    break;
                }
            }
        }

        char[] row = new char[cols];
        for (int j = 0; j < cols; j++) {
            row[j] = board[ri][j];
        }
        char[] col = new char[rows];
        for (int i = 0; i < rows; i++) {
            col[i] = board[i][rj];
        }

        return firstPiece(row, rj, -1) + firstPiece(row, rj, 1)
                + firstPiece(col, ri, -1) + firstPiece(col, ri, 1);
    }

    /** 1 if the first non-empty cell walking `step` from `start` is a pawn */
    private int firstPiece(char[] line, int start, int step) {
        for (int k = start + step; k >= 0 && k < line.length; k += step) {
            if (line[k] == '.') {
                continue;
            }
            return line[k] == 'p' ? 1 : 0;
        }
        return 0;
    }

    // V2
    // IDEA: ITERATE THE PAWNS (instead of walking out from the rook)
    /**
     *  Flip the direction of the search: for every 'p' on the board, ask whether
     *  it is attacked -- same row or column as the rook, and nothing in between.
     *
     *  Useful when the board is sparse in pawns; it also avoids any direction
     *  bookkeeping.
     *
     *  time  = O(m * n * max(m, n))
     *  space = O(1)
     */
    public int numRookCaptures_2(char[][] board) {
        int rows = board.length;
        int cols = board[0].length;

        int ri = -1;
        int rj = -1;
        for (int i = 0; i < rows && ri == -1; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 'R') {
                    ri = i;
                    rj = j;
                    break;
                }
            }
        }

        int res = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] != 'p') {
                    continue;
                }
                if (i == ri && clearBetween(board, ri, Math.min(j, rj) + 1, Math.max(j, rj), true)) {
                    res += 1;
                } else if (j == rj
                        && clearBetween(board, rj, Math.min(i, ri) + 1, Math.max(i, ri), false)) {
                    res += 1;
                }
            }
        }
        return res;
    }

    /** every cell strictly between the two endpoints is empty */
    private boolean clearBetween(char[][] board, int fixed, int from, int to, boolean alongRow) {
        for (int k = from; k < to; k++) {
            char c = alongRow ? board[fixed][k] : board[k][fixed];
            if (c != '.') {
                return false;
            }
        }
        return true;
    }

    // V3
    // IDEA: EXPAND ALL 4 RAYS IN LOCKSTEP (single distance loop)
    /**
     *  Rather than finishing one ray before starting the next, step the RADIUS
     *  outward once and probe all 4 directions at that radius, switching each
     *  direction off as soon as it hits something.
     *
     *  One loop, no early `break` per direction -- handy when you also want the
     *  DISTANCE of each capture.
     *
     *  time  = O(m * n)
     *  space = O(1)
     */
    public int numRookCaptures_3(char[][] board) {
        int rows = board.length;
        int cols = board[0].length;

        int ri = -1;
        int rj = -1;
        for (int i = 0; i < rows && ri == -1; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 'R') {
                    ri = i;
                    rj = j;
                    break;
                }
            }
        }

        int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        boolean[] alive = { true, true, true, true };
        int res = 0;

        for (int radius = 1; radius <= Math.max(rows, cols); radius++) {
            for (int d = 0; d < 4; d++) {
                if (!alive[d]) {
                    continue;
                }
                int x = ri + dirs[d][0] * radius;
                int y = rj + dirs[d][1] * radius;
                if (x < 0 || x >= rows || y < 0 || y >= cols) {
                    alive[d] = false;
                    continue;
                }
                if (board[x][y] == 'p') {
                    res += 1;
                    alive[d] = false;
                } else if (board[x][y] == 'B') {
                    alive[d] = false;
                }
            }
        }

        return res;
    }

}
