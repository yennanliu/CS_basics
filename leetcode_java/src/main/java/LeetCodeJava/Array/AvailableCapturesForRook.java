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

}
