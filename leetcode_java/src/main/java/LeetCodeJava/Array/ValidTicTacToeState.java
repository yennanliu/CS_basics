package LeetCodeJava.Array;

import java.util.Set;
import java.util.HashSet;
import java.util.Deque;
import java.util.ArrayDeque;
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


    // V1
    // IDEA: REVERSE SIMULATION — undo the last move step by step. A board is reachable
    //       iff we can peel pieces back to the empty board, and no intermediate board
    //       was already a finished (won) game.
    /**
     * time = O(1)  // bounded by the fixed 3x3 board (< 3000 undo paths)
     * space = O(1)
     */
    public boolean validTicTacToe_1(String[] board) {
        char[] cells = new char[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i * 3 + j] = board[i].charAt(j);
            }
        }
        return canUndo_1(cells);
    }

    private boolean canUndo_1(char[] cells) {
        int xCnt = 0, oCnt = 0;
        for (char c : cells) {
            if (c == 'X') {
                xCnt++;
            } else if (c == 'O') {
                oCnt++;
            }
        }
        if (xCnt == 0 && oCnt == 0) {
            return true;
        }
        if (xCnt != oCnt && xCnt != oCnt + 1) {
            return false;
        }
        // whoever has just moved (X moves first, so X leads by 0 or 1)
        char last = (xCnt == oCnt + 1) ? 'X' : 'O';
        for (int i = 0; i < 9; i++) {
            if (cells[i] != last) {
                continue;
            }
            cells[i] = ' ';
            // the predecessor board must NOT already be a finished game
            boolean finished = isWinFlat_1(cells, 'X') || isWinFlat_1(cells, 'O');
            boolean ok = !finished && canUndo_1(cells);
            cells[i] = last;
            if (ok) {
                return true;
            }
        }
        return false;
    }

    private boolean isWinFlat_1(char[] c, char p) {
        int[][] lines = new int[][] {
                { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 },
                { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
                { 0, 4, 8 }, { 2, 4, 6 } };
        for (int[] l : lines) {
            if (c[l[0]] == p && c[l[1]] == p && c[l[2]] == p) {
                return true;
            }
        }
        return false;
    }

    // V2
    // IDEA: brute force — BFS every state reachable from the empty board (only a few
    //       thousand exist) and test membership; kept as a readable correctness reference
    /**
     * time = O(1)  // <= 3^9 states, fixed board size
     * space = O(1)
     */
    public boolean validTicTacToe_2(String[] board) {
        String target = board[0] + board[1] + board[2];

        String empty = "         ";
        Set<String> reachable = new HashSet<>();
        Deque<String> q = new ArrayDeque<>();
        reachable.add(empty);
        q.add(empty);

        while (!q.isEmpty()) {
            String cur = q.poll();
            char[] arr = cur.toCharArray();
            // once someone has won the game stops -> no successor state
            if (isWinFlat_1(arr, 'X') || isWinFlat_1(arr, 'O')) {
                continue;
            }
            int xCnt = 0, oCnt = 0;
            for (char c : arr) {
                if (c == 'X') {
                    xCnt++;
                } else if (c == 'O') {
                    oCnt++;
                }
            }
            char turn = (xCnt == oCnt) ? 'X' : 'O';
            for (int i = 0; i < 9; i++) {
                if (arr[i] != ' ') {
                    continue;
                }
                arr[i] = turn;
                String next = new String(arr);
                arr[i] = ' ';
                if (reachable.add(next)) {
                    q.add(next);
                }
            }
        }
        return reachable.contains(target);
    }
}
