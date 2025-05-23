package LeetCodeJava.Array;

// https://leetcode.com/problems/valid-sudoku/
/**
 *
 Code
 Testcase
 Test Result
 Test Result
 36. Valid Sudoku
 Solved
 Medium
 Topics
 Companies
 Determine if a 9 x 9 Sudoku board is valid. Only the filled cells need to be validated according to the following rules:

 Each row must contain the digits 1-9 without repetition.
 Each column must contain the digits 1-9 without repetition.
 Each of the nine 3 x 3 sub-boxes of the grid must contain the digits 1-9 without repetition.
 Note:

 A Sudoku board (partially filled) could be valid but is not necessarily solvable.
 Only the filled cells need to be validated according to the mentioned rules.


 Example 1:


 Input: board =
 [["5","3",".",".","7",".",".",".","."]
 ,["6",".",".","1","9","5",".",".","."]
 ,[".","9","8",".",".",".",".","6","."]
 ,["8",".",".",".","6",".",".",".","3"]
 ,["4",".",".","8",".","3",".",".","1"]
 ,["7",".",".",".","2",".",".",".","6"]
 ,[".","6",".",".",".",".","2","8","."]
 ,[".",".",".","4","1","9",".",".","5"]
 ,[".",".",".",".","8",".",".","7","9"]]
 Output: true
 Example 2:

 Input: board =
 [["8","3",".",".","7",".",".",".","."]
 ,["6",".",".","1","9","5",".",".","."]
 ,[".","9","8",".",".",".",".","6","."]
 ,["8",".",".",".","6",".",".",".","3"]
 ,["4",".",".","8",".","3",".",".","1"]
 ,["7",".",".",".","2",".",".",".","6"]
 ,[".","6",".",".",".",".","2","8","."]
 ,[".",".",".","4","1","9",".",".","5"]
 ,[".",".",".",".","8",".",".","7","9"]]
 Output: false
 Explanation: Same as Example 1, except with the 5 in the top left corner being modified to 8. Since there are two 8's in the top left 3x3 sub-box, it is invalid.


 Constraints:

 board.length == 9
 board[i].length == 9
 board[i][j] is a digit 1-9 or '.'.


 */
import java.util.HashSet;
import java.util.Set;

public class ValidSudoku {

    // V0
    // IDEA: SET, MATRIX OP (fixed by GPT)
    // time: O(1), space: O(1)
    public boolean isValidSudoku(char[][] board) {
        // edge case: if board is empty, return true (no validation needed)
        if (board.length == 0 || board[0].length == 0) {
            return true;
        }

        int l = board.length;
        int w = board[0].length;

        // check rows (x-axis)
        for (int y = 0; y < l; y++) {
            HashSet<Character> set = new HashSet<>();
            for (int x = 0; x < w; x++) {
                // check if the cell is not empty
                if (board[y][x] != '.') {
                    if (set.contains(board[y][x])) {
                        return false; // found a duplicate
                    }
                    set.add(board[y][x]);
                }
            }
        }

        // check columns (y-axis)
        for (int x = 0; x < w; x++) {
            HashSet<Character> set = new HashSet<>();
            for (int y = 0; y < l; y++) {
                // check if the cell is not empty
                if (board[y][x] != '.') {
                    if (set.contains(board[y][x])) {
                        return false; // found a duplicate
                    }
                    set.add(board[y][x]);
                }
            }
        }

        // check 3x3 subgrids
        for (int i = 0; i < l; i += 3) { // iterate every 3 rows
            for (int j = 0; j < w; j += 3) { // iterate every 3 columns
                HashSet<Character> set = new HashSet<>();
                for (int x = i; x < i + 3; x++) {
                    for (int y = j; y < j + 3; y++) {
                        if (x < l && y < w && board[x][y] != '.') { // ensure within bounds
                            if (set.contains(board[x][y])) {
                                return false; // found a duplicate in the 3x3 subgrid
                            }
                            set.add(board[x][y]);
                        }
                    }
                }
            }
        }

        return true;
    }

    // V0-1
    // IDEA : SET
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0036-valid-sudoku.java
    // time: O(1), space: O(1)
    public boolean isValidSudoku_0_1(char[][] board) {
        //neetcode solution, slightly modified

        //a set of the characters that we have already come across (excluding '.' which denotes an empty space)
        Set<Character> rowSet = null;
        Set<Character> colSet = null;


        for (int i = 0; i < 9; i++) {
            //re-initialize the sets so we don't carry over found characters from the previous run
            rowSet = new HashSet<>();
            colSet = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                char r = board[i][j];
                char c = board[j][i];
                if (r != '.'){
                    if (rowSet.contains(r)){
                        return false;
                    } else {
                        rowSet.add(r);
                    }
                }
                if (c != '.'){
                    if (colSet.contains(c)){
                        return false;
                    } else {
                        colSet.add(c);
                    }
                }
            }
        }

        //block
        //loop controls advance by 3 each time to jump through the boxes
        for (int i = 0; i < 9; i = i + 3) {
            for (int j = 0; j < 9; j = j + 3) {
                //checkBlock will return true if valid
                if (!checkBlock(i, j, board)) {
                    return false;
                }
            }
        }
        //passed all tests, therefore valid board
        return true;
    }

    public boolean checkBlock(int idxI, int idxJ, char[][] board) {
        Set<Character> blockSet = new HashSet<>();
        //if idxI = 3 and indJ = 0
        //rows = 6 and cols = 3
        int rows = idxI + 3;
        int cols = idxJ + 3;
        //and because i initializes to idxI but only goes to rows, we loop 3 times (once for each row)
        for (int i = idxI; i < rows; i++) {
            //same for columns
            for (int j = idxJ; j < cols; j++) {
                if (board[i][j] == '.') {
                    continue;
                }

                if (blockSet.contains(board[i][j])) {
                    return false;
                }

                blockSet.add(board[i][j]);
            }
        }

        return true;
    }

    // V1
    // IDEA : HASH SET
    // https://leetcode.com/problems/valid-sudoku/editorial/
    // time: O(1), space: O(1)
    public boolean isValidSudoku_1(char[][] board) {
        int N = 9;

        // Use hash set to record the status
        HashSet<Character>[] rows = new HashSet[N];
        HashSet<Character>[] cols = new HashSet[N];
        HashSet<Character>[] boxes = new HashSet[N];
        for (int r = 0; r < N; r++) {
            rows[r] = new HashSet<Character>();
            cols[r] = new HashSet<Character>();
            boxes[r] = new HashSet<Character>();
        }

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                char val = board[r][c];

                // Check if the position is filled with number
                if (val == '.') {
                    continue;
                }

                // Check the row
                if (rows[r].contains(val)) {
                    return false;
                }
                rows[r].add(val);

                // Check the column
                if (cols[c].contains(val)) {
                    return false;
                }
                cols[c].add(val);

                // Check the box
                /** NOTE trick here !!!! */
                int idx = (r / 3) * 3 + c / 3;
                if (boxes[idx].contains(val)) {
                    return false;
                }
                boxes[idx].add(val);
            }
        }
        return true;
    }

    // V2
    // IDEA : Array of Fixed Length
    // https://leetcode.com/problems/valid-sudoku/editorial/
    // time: O(1), space: O(1)
    public boolean isValidSudoku_2(char[][] board) {
        int N = 9;

        // Use an array to record the status
        int[][] rows = new int[N][N];
        int[][] cols = new int[N][N];
        int[][] boxes = new int[N][N];

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                // Check if the position is filled with number
                if (board[r][c] == '.') {
                    continue;
                }
                int pos = board[r][c] - '1';

                // Check the row
                if (rows[r][pos] == 1) {
                    return false;
                }
                rows[r][pos] = 1;

                // Check the column
                if (cols[c][pos] == 1) {
                    return false;
                }
                cols[c][pos] = 1;

                // Check the box
                int idx = (r / 3) * 3 + c / 3;
                if (boxes[idx][pos] == 1) {
                    return false;
                }
                boxes[idx][pos] = 1;
            }
        }
        return true;
    }

    // V3
    // IDEA : Bitmasking
    // https://leetcode.com/problems/valid-sudoku/editorial/
    // time: O(1), space: O(1)
    public boolean isValidSudoku_3(char[][] board) {
        int N = 9;

        // Use a binary number to record previous occurrence
        int[] rows = new int[N];
        int[] cols = new int[N];
        int[] boxes = new int[N];

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                // Check if the position is filled with number
                if (board[r][c] == '.') {
                    continue;
                }
                int val = board[r][c] - '0';
                int pos = 1 << (val - 1);

                // Check the row
                if ((rows[r] & pos) > 0) {
                    return false;
                }
                rows[r] |= pos;

                // Check the column
                if ((cols[c] & pos) > 0) {
                    return false;
                }
                cols[c] |= pos;

                // Check the box
                int idx = (r / 3) * 3 + c / 3;
                if ((boxes[idx] & pos) > 0) {
                    return false;
                }
                boxes[idx] |= pos;
            }
        }
        return true;
    }

}
