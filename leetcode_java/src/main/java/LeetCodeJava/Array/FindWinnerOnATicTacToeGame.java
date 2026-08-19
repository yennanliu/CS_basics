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
}
