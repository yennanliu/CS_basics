package LeetCodeJava.Array;

// https://leetcode.com/problems/check-if-move-is-legal/description/
/**
 * 1958. Check if Move is Legal
 * Medium
 *
 * You are given a 0-indexed 8 x 8 grid board, where board[r][c] represents the cell (r, c) on a
 * game board. On the board, free cells are represented by '.', white cells are represented by 'W',
 * and black cells are represented by 'B'.
 *
 * Each move in this game consists of choosing a free cell and changing it to the color you are
 * playing as (either white or black). However, a move is only legal if, after changing it, the cell
 * becomes the endpoint of a good line (horizontal, vertical, or diagonal).
 *
 * A good line is a line of three or more cells (including the endpoints) where the endpoints of the
 * line are one color, and the remaining cells in the middle are the opposite color (no cells in the
 * line are free).
 *
 * Given two integers rMove and cMove and a character color representing the color you are playing
 * as (white or black), return true if changing cell (rMove, cMove) to color color is a legal move,
 * or false if it is not legal.
 *
 *
 * Example 1:
 *
 * Input: board = [[".",".",".","B",".",".",".","."],[".",".",".","W",".",".",".","."],[".",".",".","W",".",".",".","."],[".",".",".","W",".",".",".","."],["W","B","B",".","W","W","W","B"],[".",".",".","B",".",".",".","."],[".",".",".","B",".",".",".","."],[".",".",".","W",".",".",".","."]], rMove = 4, cMove = 3, color = "B"
 * Output: true
 *
 * Example 2:
 *
 * Input: board = [[".",".",".",".",".",".",".","."],[".","B",".",".","W",".",".","."],[".",".","W",".",".",".",".","."],[".",".",".","W","B",".",".","."],[".",".",".",".",".",".",".","."],[".",".",".",".","B","W",".","."],[".",".",".",".",".",".","W","."],[".",".",".",".",".",".",".","B"]], rMove = 4, cMove = 4, color = "W"
 * Output: false
 *
 *
 * Constraints:
 *
 * board.length == board[r].length == 8
 * 0 <= rMove, cMove < 8
 * board[rMove][cMove] == '.'
 * color is either 'B' or 'W'.
 *
 */
public class CheckIfMoveIsLegal {

    private static final int SIZE = 8;
    private static final char FREE = '.';
    private static final int[][] DIRECTIONS = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1}, {0, 1},
            {1, -1}, {1, 0}, {1, 1}
    };

    // V0
    // IDEA: WALK A RAY IN EACH OF THE 8 DIRECTIONS
    // from the placed cell, step outward while the cells are the OPPOSITE color; the line is good if
    // the walk then hits our own color after at least one opposite cell (3+ cells in total).
    // A free cell, the board edge, or our own color on the very first step kills the ray.
    /**
     * time = O(8 * 8) = O(1)
     * space = O(1)
     */
    public boolean checkMove(char[][] board, int rMove, int cMove, char color) {
        for (int[] direction : DIRECTIONS) {
            int r = rMove + direction[0];
            int c = cMove + direction[1];
            // number of opposite-colored cells stepped over so far
            int middle = 0;
            while (r >= 0 && r < SIZE && c >= 0 && c < SIZE) {
                char cell = board[r][c];
                if (cell == FREE) {
                    break;
                }
                if (cell == color) {
                    // closing endpoint: good only if something was sandwiched in between
                    if (middle > 0) {
                        return true;
                    }
                    break;
                }
                middle++;
                r += direction[0];
                c += direction[1];
            }
        }
        return false;
    }
}
