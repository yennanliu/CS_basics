package LeetCodeJava.Design;

// https://leetcode.com/problems/design-tic-tac-toe/

/**
 *  348. Design Tic-Tac-Toe
 *  Medium
 *
 *  Assume the following rules are for the tic-tac-toe game on an n x n board between
 *  two players:
 *   - A move is guaranteed to be valid and is placed on an empty block.
 *   - Once a winning condition is reached, no more moves are allowed.
 *   - A player who succeeds in placing n of their marks in a horizontal, vertical, or
 *     diagonal row wins the game.
 *
 *  Implement the TicTacToe class:
 *   - TicTacToe(int n) Initializes the object the size of the board n.
 *   - int move(int row, int col, int player) Indicates that the player with id player
 *     plays at the cell (row, col). Returns 0 (no one wins), 1 or 2 (that player wins).
 *
 *  Example 1:
 *    Input
 *      ["TicTacToe", "move", "move", "move", "move", "move", "move", "move"]
 *      [[3], [0,0,1], [0,2,2], [2,2,1], [1,1,2], [2,0,1], [1,0,2], [2,1,1]]
 *    Output
 *      [null, 0, 0, 0, 0, 0, 0, 1]
 *
 *  Constraints:
 *    2 <= n <= 100
 *    player is 1 or 2.
 *    0 <= row, col < n
 *    (row, col) are unique for each different call to move.
 *    At most n^2 calls will be made to move.
 *
 *  Follow-up: Could you do better than O(n^2) per move()?
 */
public class DesignTicTacToe {

    // V0
    // IDEA: no board at all - keep a signed COUNTER per row / col / diagonal.
    //       player 1 adds +1, player 2 adds -1; a counter hitting +n or -n means
    //       that line is fully owned -> O(1) per move.
    /**
     * time = O(1) per move
     * space = O(n)
     */
    private final int n;
    private final int[] rows;
    private final int[] cols;
    private int diag;
    private int antiDiag;

    public DesignTicTacToe(int n) {
        this.n = n;
        this.rows = new int[n];
        this.cols = new int[n];
        this.diag = 0;
        this.antiDiag = 0;
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public int move(int row, int col, int player) {

        int delta = (player == 1) ? 1 : -1;

        rows[row] += delta;
        cols[col] += delta;
        if (row == col) {
            diag += delta;
        }
        if (row + col == n - 1) {
            antiDiag += delta;
        }

        int target = delta * n;
        if (rows[row] == target || cols[col] == target
                || diag == target || antiDiag == target) {
            return player;
        }

        return 0;
    }
}
