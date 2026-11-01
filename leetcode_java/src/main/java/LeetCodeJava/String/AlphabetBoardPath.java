package LeetCodeJava.String;

// https://leetcode.com/problems/alphabet-board-path/description/

/**
 * 1138. Alphabet Board Path
 * Medium
 *
 * On an alphabet board, we start at position (0, 0), corresponding to character board[0][0].
 *
 * Here, board = ["abcde", "fghij", "klmno", "pqrst", "uvwxy", "z"].
 *
 * We may make the following moves:
 *
 * 'U' moves our position up one row, if the position exists on the board;
 * 'D' moves our position down one row, if the position exists on the board;
 * 'L' moves our position left one column, if the position exists on the board;
 * 'R' moves our position right one column, if the position exists on the board;
 * '!' adds the character board[r][c] at our current position (r, c) to the answer.
 *
 * (Here, the only positions that exist on the board are positions with letters on them.)
 *
 * Return a sequence of moves that makes our answer equal to target in the minimum number
 * of moves. You may return any path that does so.
 *
 * Example 1:
 *
 * Input: target = "leet"
 * Output: "DDR!UURRR!!DDD!"
 *
 * Example 2:
 *
 * Input: target = "code"
 * Output: "RR!DDRR!UUL!R!"
 *
 * Constraints:
 *
 * 1 <= target.length <= 100
 * target consists only of English lowercase letters.
 *
 */
public class AlphabetBoardPath {

    // V0
    // IDEA: SIMULATION + MOVE ORDER U -> L -> R -> D (so the lonely 'z' at (5,0) is never left/entered illegally)
    //   letter ch sits at row = (ch - 'a') / 5, col = (ch - 'a') % 5
    //   row 5 only has column 0 ('z'):
    //     - go UP before RIGHT  => leaving 'z' is always legal
    //     - go LEFT before DOWN => entering 'z' is always legal
    /**
     * time = O(N * (R + C)), N = target length
     * space = O(N * (R + C))  (the output)
     */
    public String alphabetBoardPath(String target) {
        StringBuilder path = new StringBuilder();
        int row = 0;
        int col = 0;

        for (char ch : target.toCharArray()) {
            int nextRow = (ch - 'a') / 5;
            int nextCol = (ch - 'a') % 5;

            // up and left first, then right and down (see IDEA)
            appendMoves(path, 'U', row - nextRow);
            appendMoves(path, 'L', col - nextCol);
            appendMoves(path, 'R', nextCol - col);
            appendMoves(path, 'D', nextRow - row);
            path.append('!');

            row = nextRow;
            col = nextCol;
        }
        return path.toString();
    }

    /** Appends `times` copies of move (nothing when times <= 0). */
    private void appendMoves(StringBuilder path, char move, int times) {
        for (int i = 0; i < times; i++) {
            path.append(move);
        }
    }
}
