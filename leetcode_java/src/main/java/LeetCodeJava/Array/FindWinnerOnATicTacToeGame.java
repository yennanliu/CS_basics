package LeetCodeJava.Array;

// https://leetcode.com/problems/find-winner-on-a-tic-tac-toe-game/

/**
 *  1275. Find Winner on a Tic Tac Toe Game
 *  Easy
 *
 *  Tic-tac-toe is played by two players A and B on a 3 x 3 grid. The rules are:
 *
 *  Players take turns placing characters into empty squares ' '.
 *  The first player A always places 'X' characters, while the second player B
 *  always places 'O' characters.
 *  'X' and 'O' characters are always placed into empty squares, never on filled ones.
 *  The game ends when there are three of the same (non-empty) character filling
 *  any row, column, or diagonal, or when all squares are non-empty.
 *
 *  Given a 2D integer array moves where moves[i] = [rowi, coli] indicates that the
 *  ith move will be played on grid[rowi][coli], return the winner of the game if
 *  it exists ("A" or "B"). In case the game ends in a draw return "Draw".
 *  If there are still movements to play return "Pending".
 *
 *
 *  Example 1:
 *
 *  Input: moves = [[0,0],[2,0],[1,1],[2,1],[2,2]]
 *  Output: "A"
 *
 *  Example 2:
 *
 *  Input: moves = [[0,0],[1,1],[0,1],[0,2],[1,0],[2,0]]
 *  Output: "B"
 *
 *  Example 3:
 *
 *  Input: moves = [[0,0],[1,1],[2,0],[1,0],[1,2],[2,1],[0,1],[0,2],[2,2]]
 *  Output: "Draw"
 *
 *
 *  Constraints:
 *
 *  1 <= moves.length <= 9
 *  moves[i].length == 2
 *  0 <= rowi, coli <= 2
 *  There are no repeated elements on moves.
 *  moves follow the rules of tic tac toe.
 */
public class FindWinnerOnATicTacToeGame {

    // V0
    // IDEA: COUNTERS — +1 for player A, -1 for player B on each row/col/diagonal;
    //       a line reaching +-3 is a win
    /**
     * time = O(n), n = moves.length (<= 9)
     * space = O(1)
     */
    public String tictactoe(int[][] moves) {
        int n = 3;
        int[] rows = new int[n];
        int[] cols = new int[n];
        int diag = 0;
        int antiDiag = 0;

        for (int i = 0; i < moves.length; i++) {
            int r = moves[i][0];
            int c = moves[i][1];
            int player = (i % 2 == 0) ? 1 : -1; // A moves first

            rows[r] += player;
            cols[c] += player;
            if (r == c) {
                diag += player;
            }
            if (r + c == n - 1) {
                antiDiag += player;
            }

            if (Math.abs(rows[r]) == n || Math.abs(cols[c]) == n
                    || Math.abs(diag) == n || Math.abs(antiDiag) == n) {
                return player == 1 ? "A" : "B";
            }
        }

        return moves.length == n * n ? "Draw" : "Pending";
    }

    // V1
    // IDEA: SIMULATE THE BOARD — replay the moves onto a 3x3 char grid, then test
    //       all 8 winning lines explicitly for each player.
    /**
     * time = O(n + 8) = O(1)
     * space = O(1)  (fixed 3x3 grid)
     */
    public String tictactoe_1(int[][] moves) {
        char[][] g = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                g[i][j] = ' ';
            }
        }
        for (int i = 0; i < moves.length; i++) {
            g[moves[i][0]][moves[i][1]] = (i % 2 == 0) ? 'X' : 'O';
        }

        char[] players = new char[]{'X', 'O'};
        for (char p : players) {
            for (int i = 0; i < 3; i++) {
                if (g[i][0] == p && g[i][1] == p && g[i][2] == p) {
                    return p == 'X' ? "A" : "B";
                }
                if (g[0][i] == p && g[1][i] == p && g[2][i] == p) {
                    return p == 'X' ? "A" : "B";
                }
            }
            if (g[0][0] == p && g[1][1] == p && g[2][2] == p) {
                return p == 'X' ? "A" : "B";
            }
            if (g[0][2] == p && g[1][1] == p && g[2][0] == p) {
                return p == 'X' ? "A" : "B";
            }
        }

        return moves.length == 9 ? "Draw" : "Pending";
    }

    // V2
    // IDEA: BITMASK — pack each player's cells into a 9-bit mask (cell (r,c) -> bit
    //       r*3+c); a player wins iff their mask covers one of the 8 winning masks.
    /**
     * time = O(n + 8) = O(1)
     * space = O(1)
     */
    public String tictactoe_2(int[][] moves) {
        int[] mask = new int[2]; // mask[0] = A ('X'), mask[1] = B ('O')
        for (int i = 0; i < moves.length; i++) {
            mask[i % 2] |= 1 << (moves[i][0] * 3 + moves[i][1]);
        }

        int[] wins = {
                0b000000111, 0b000111000, 0b111000000, // rows
                0b001001001, 0b010010010, 0b100100100, // cols
                0b100010001, 0b001010100                // diagonals
        };
        for (int w : wins) {
            if ((mask[0] & w) == w) {
                return "A";
            }
            if ((mask[1] & w) == w) {
                return "B";
            }
        }

        return moves.length == 9 ? "Draw" : "Pending";
    }
}
