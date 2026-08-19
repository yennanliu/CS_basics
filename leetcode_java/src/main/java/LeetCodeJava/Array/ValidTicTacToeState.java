package LeetCodeJava.Array;

// https://leetcode.com/problems/valid-tic-tac-toe-state/

/**
 *  794. Valid Tic-Tac-Toe State
 *  Medium
 *
 *  Given a Tic-Tac-Toe board as a string array board, return true if and only if
 *  it is possible to reach this board position during the course of a valid
 *  tic-tac-toe game.
 *
 *  The board is a 3 x 3 array that consists of characters ' ', 'X', and 'O'.
 *  The ' ' character represents an empty square.
 *
 *  Here are the rules of Tic-Tac-Toe:
 *   - Players take turns placing characters into empty squares ' '.
 *   - The first player always places 'X' characters, while the second player
 *     always places 'O' characters.
 *   - 'X' and 'O' characters are always placed into empty squares, never filled ones.
 *   - The game ends when there are three of the same (non-empty) character
 *     filling any row, column, or diagonal.
 *   - The game also ends if all squares are non-empty.
 *   - No more moves can be played if the game is over.
 *
 *  Example 1:
 *    Input: board = ["O  ","   ","   "]
 *    Output: false
 *    Explanation: The first player always plays "X".
 *
 *  Example 2:
 *    Input: board = ["XXX","   ","OOO"]
 *    Output: false
 *
 *  Constraints:
 *    board.length == 3
 *    board[i].length == 3
 *    board[i][j] is either 'X', 'O', or ' '.
 */
public class ValidTicTacToeState {

    // V0
    // IDEA: COUNTING + WIN CHECK.
    //       #X must be #O or #O+1; if X wins then #X == #O + 1 and O cannot win;
    //       if O wins then #X == #O and X cannot win.
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean validTicTacToe(String[] board) {
        int xCnt = 0, oCnt = 0;
        for (String row : board) {
            for (int j = 0; j < row.length(); j++) {
                char c = row.charAt(j);
                if (c == 'X') {
                    xCnt++;
                } else if (c == 'O') {
                    oCnt++;
                }
            }
        }

        if (oCnt != xCnt && oCnt != xCnt - 1) {
            return false;
        }

        boolean xWin = isWin(board, 'X');
        boolean oWin = isWin(board, 'O');

        if (xWin && oWin) {
            return false;
        }
        if (xWin && xCnt != oCnt + 1) {
            return false;
        }
        if (oWin && xCnt != oCnt) {
            return false;
        }
        return true;
    }

    private boolean isWin(String[] board, char p) {
        for (int i = 0; i < 3; i++) {
            if (board[i].charAt(0) == p && board[i].charAt(1) == p && board[i].charAt(2) == p) {
                return true;
            }
            if (board[0].charAt(i) == p && board[1].charAt(i) == p && board[2].charAt(i) == p) {
                return true;
            }
        }
        if (board[0].charAt(0) == p && board[1].charAt(1) == p && board[2].charAt(2) == p) {
            return true;
        }
        if (board[0].charAt(2) == p && board[1].charAt(1) == p && board[2].charAt(0) == p) {
            return true;
        }
        return false;
    }
}
