package LeetCodeJava.Math;

// https://leetcode.com/problems/transform-to-chessboard/description/
/**
 * 782. Transform to Chessboard
 * Hard
 *
 * You are given an n x n binary grid board. In each move, you can swap any two rows
 * with each other, or any two columns with each other.
 *
 * Return the minimum number of moves to transform the board into a chessboard board.
 * If the task is impossible, return -1.
 *
 * A chessboard board is a board where no 0's and no 1's are 4-directionally adjacent.
 *
 *
 * Example 1:
 *
 * Input: board = [[0,1,1,0],[0,1,1,0],[1,0,0,1],[1,0,0,1]]
 * Output: 2
 * Explanation: One potential sequence of moves is shown.
 * The first move swaps the first and second column.
 * The second move swaps the second and third row.
 *
 * Example 2:
 *
 * Input: board = [[0,1],[1,0]]
 * Output: 0
 * Explanation: Also note that the board with 0 in the top left corner,
 * is also a valid chessboard.
 *
 * Example 3:
 *
 * Input: board = [[1,0],[1,0]]
 * Output: -1
 * Explanation: No matter what sequence of moves you make,
 * you cannot end with a valid chessboard.
 *
 *
 * Constraints:
 *
 * n == board.length
 * n == board[i].length
 * 2 <= n <= 30
 * board[i][j] is either 0 or 1.
 *
 */
public class TransformToChessboard {

    // V0
    // IDEA: MATH / PATTERN OBSERVATION
    /**
     *  KEY OBSERVATIONS:
     *
     *   1) A board is fixable ONLY IF every row is either IDENTICAL to row 0
     *      or the exact COMPLEMENT of row 0 (same for columns).
     *      That is equivalent to:
     *          board[0][0] ^ board[i][0] ^ board[0][j] ^ board[i][j] == 0
     *      for every (i, j) -> the whole board is DETERMINED by its
     *      first row and first column.
     *
     *   2) Row / column swaps NEVER change the multiset of rows, so the
     *      number of 1s in row 0 (and col 0) must be n/2 or (n+1)/2.
     *
     *   3) Once valid, the answer is decided purely by how many entries of
     *      the first column (resp. first row) already sit on the `right`
     *      parity. Each swap fixes 2 misplaced entries, so we divide by 2.
     *      - n EVEN : the board may start with 0 or 1, take the CHEAPER one.
     *      - n ODD  : only ONE starting bit is possible, the odd count is invalid.
     *
     *  time  = O(n^2)
     *  space = O(1)
     */
    public int movesToChessboard(int[][] board) {
        int n = board.length;

        // 1) every 2x2 sub-rectangle formed with (0,0) must XOR to 0
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((board[0][0] ^ board[i][0] ^ board[0][j] ^ board[i][j]) != 0) {
                    return -1;
                }
            }
        }

        // 2) the first row / first column must be (almost) BALANCED
        int rowOnes = 0;
        int colOnes = 0;
        for (int i = 0; i < n; i++) {
            rowOnes += board[0][i];
            colOnes += board[i][0];
        }
        if (rowOnes < n / 2 || rowOnes > (n + 1) / 2) {
            return -1;
        }
        if (colOnes < n / 2 || colOnes > (n + 1) / 2) {
            return -1;
        }

        /** NOTE !!!
         *
         *  3) count the entries already matching the `starts with 0` pattern,
         *     i.e. board[i][0] == i % 2  ->  0,1,0,1,...
         */
        int rowSwap = 0;
        int colSwap = 0;
        for (int i = 0; i < n; i++) {
            if (board[i][0] == i % 2) {
                rowSwap += 1;
            }
            if (board[0][i] == i % 2) {
                colSwap += 1;
            }
        }

        if (n % 2 == 1) {
            /** NOTE !!!
             *
             *  odd n: only ONE of the two patterns is reachable,
             *  and the reachable one always leaves an EVEN number of mismatches
             */
            if (rowSwap % 2 == 1) {
                rowSwap = n - rowSwap;
            }
            if (colSwap % 2 == 1) {
                colSwap = n - colSwap;
            }
        } else {
            // even n: BOTH patterns are reachable, pick the cheaper one
            rowSwap = Math.min(rowSwap, n - rowSwap);
            colSwap = Math.min(colSwap, n - colSwap);
        }

        // each swap puts 2 lines in place
        return (rowSwap + colSwap) / 2;
    }

}
