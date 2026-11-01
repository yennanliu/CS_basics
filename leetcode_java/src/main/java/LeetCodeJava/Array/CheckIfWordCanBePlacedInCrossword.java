package LeetCodeJava.Array;

// https://leetcode.com/problems/check-if-word-can-be-placed-in-crossword/description/

/**
 * 2018. Check if Word Can Be Placed In Crossword
 * Medium
 *
 * You are given an m x n matrix board, representing the current state of a crossword puzzle. The crossword contains lowercase English letters (from solved words), ' ' to represent any empty cells, and '#' to represent any blocked cells.
 *
 * A word can be placed horizontally (left to right or right to left) or vertically (top to bottom or bottom to top) in the board if:
 *
 * It does not occupy a cell containing the character '#'.
 * The cell each letter is placed in must either be ' ' (empty) or match the letter already on the board.
 * There must not be any empty cells ' ' or other lowercase letters directly left or right of the word if the word was placed horizontally.
 * There must not be any empty cells ' ' or other lowercase letters directly above or below the word if the word was placed vertically.
 *
 * Given a string word, return true if word can be placed in board, or false otherwise.
 *
 *
 * Example 1:
 *
 * Input: board = [["#", " ", "#"], [" ", " ", "#"], ["#", "c", " "]], word = "abc"
 * Output: true
 * Explanation: The word "abc" can be placed as shown above (top to bottom).
 *
 * Example 2:
 *
 * Input: board = [[" ", "#", "a"], [" ", "#", "c"], [" ", "#", "a"]], word = "ac"
 * Output: false
 * Explanation: It is impossible to place the word because there will always be a space/letter above or below it.
 *
 * Example 3:
 *
 * Input: board = [["#", " ", "#"], [" ", " ", "#"], ["#", " ", "c"]], word = "ca"
 * Output: true
 * Explanation: The word "ca" can be placed as shown above (right to left).
 *
 *
 * Constraints:
 *
 * m == board.length
 * n == board[i].length
 * 1 <= m * n <= 2 * 10^5
 * board[i][j] will be ' ', '#', or a lowercase English letter.
 * 1 <= word.length <= max(m, n)
 * word will contain only lowercase English letters.
 *
 */
public class CheckIfWordCanBePlacedInCrossword {

    // V0
    // IDEA: SPLIT EVERY ROW / COLUMN INTO '#'-FREE SLOTS, THEN MATCH FORWARDS AND BACKWARDS
    /**
     *  A placement is legal iff the word exactly fills one maximal run of
     *  non-'#' cells: the "nothing directly before / after the word" rules are
     *  exactly "the run length equals word.length()".
     *  Matching the reversed word covers right-to-left and bottom-to-top.
     *
     * time = O(M * N)
     * space = O(max(M, N))
     */
    public boolean placeWordInCrossword(char[][] board, String word) {
        int rows = board.length;
        int cols = board[0].length;
        String reversed = new StringBuilder(word).reverse().toString();

        // horizontal slots: each row read left to right
        for (int r = 0; r < rows; r++) {
            StringBuilder line = new StringBuilder();
            for (int c = 0; c < cols; c++) {
                line.append(board[r][c]);
            }
            if (lineHasSlot(line.toString(), word, reversed)) {
                return true;
            }
        }

        // vertical slots: each column read top to bottom
        for (int c = 0; c < cols; c++) {
            StringBuilder line = new StringBuilder();
            for (int r = 0; r < rows; r++) {
                line.append(board[r][c]);
            }
            if (lineHasSlot(line.toString(), word, reversed)) {
                return true;
            }
        }
        return false;
    }

    /** Cuts the line on '#' and checks each slot of the right length against the word in both directions. */
    private boolean lineHasSlot(String line, String word, String reversed) {
        int start = 0;
        // walk one past the end so the last slot is closed like the others
        for (int i = 0; i <= line.length(); i++) {
            if (i == line.length() || line.charAt(i) == '#') {
                // slot is line[start, i)
                if (i - start == word.length()) {
                    String slot = line.substring(start, i);
                    if (fits(slot, word) || fits(slot, reversed)) {
                        return true;
                    }
                }
                start = i + 1;
            }
        }
        return false;
    }

    /** ' ' is a wildcard; a letter already on the board must match exactly. */
    private boolean fits(String slot, String word) {
        for (int i = 0; i < slot.length(); i++) {
            char cell = slot.charAt(i);
            if (cell != ' ' && cell != word.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    // V1
    // IDEA: BRUTE FORCE -- TRY THE WORD FROM EVERY CELL IN ALL 4 DIRECTIONS
    /**
     *  Lay the word down from (r, c) in one direction and check:
     *   - the cell just BEFORE the start is off board or '#'
     *   - the cell just AFTER the end is off board or '#'
     *   - every covered cell is ' ' or the matching letter
     *
     * time = O(M * N * L), L = word.length()
     * space = O(1)
     */
    public boolean placeWordInCrossword_1(char[][] board, String word) {
        int rows = board.length;
        int cols = board[0].length;
        int len = word.length();
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                for (int[] dir : directions) {
                    int dr = dir[0];
                    int dc = dir[1];
                    // the word must start right after a wall
                    if (!isWall(board, r - dr, c - dc)) {
                        continue;
                    }
                    // ... and end right before a wall
                    int endRow = r + dr * (len - 1);
                    int endCol = c + dc * (len - 1);
                    if (!isWall(board, endRow + dr, endCol + dc)) {
                        continue;
                    }
                    if (matchesFrom(board, word, r, c, dr, dc)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** Off the board counts as a wall, as does '#'. */
    private boolean isWall(char[][] board, int r, int c) {
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) {
            return true;
        }
        return board[r][c] == '#';
    }

    /** Whether every letter of word can sit on the cells starting at (r, c) moving by (dr, dc). */
    private boolean matchesFrom(char[][] board, String word, int r, int c, int dr, int dc) {
        for (int k = 0; k < word.length(); k++) {
            int row = r + dr * k;
            int col = c + dc * k;
            if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
                return false;
            }
            char cell = board[row][col];
            if (cell != ' ' && cell != word.charAt(k)) {
                return false;
            }
        }
        return true;
    }
}
