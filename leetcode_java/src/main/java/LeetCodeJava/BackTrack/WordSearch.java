package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/word-search/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordSearch {

      // TODO : fix V0
//    // V0
//    public boolean exist(char[][] board, String word) {
//
//        if (board == null || board.length == 0){
//            return false;
//        }
//
//        Boolean[][] visited = new Boolean[board.length][board[0].length];
//        List<String> cur = new ArrayList<>();
//        return _help(board, word, cur, visited);
//    }
//
//    private boolean _help(char[][] board, String word, List<String> cur, Boolean[][] visited){
//
//        int len = board.length;
//        int width = board[0].length;
//
//        String curStr = array2String(cur);
//        if (curStr.equals(word)){
//            return true;
//        }
//
//        if (cur.size() > word.length()){
//            return false;
//        }
//
//        for (int i=0; i < len; i++){
//            for (int j=0; j < width; j++){
//                String val = String.valueOf(board[i][j]);
//                if (visited[i][j] == null){
//                    // do recursive
//                    cur.add(val);
//                    visited[i][j] = true;
//                    _help(board, word, cur, visited);
//                    // undo
//                    visited[i][j] = null;
//                    cur.remove(cur.size()-1);
//                    _help(board, word, cur, visited);
//                }
//            }
//        }
//
//        return false;
//    }
//
//    private String array2String(List<String> input){
//        String output = "";
//        for (String x : input){
//            output += x;
//        }
//        return output;
//    }

    // V1
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/word-search/editorial/
    private char[][] board;
    private int ROWS;
    private int COLS;

    public boolean exist_1(char[][] board, String word) {
        this.board = board;
        this.ROWS = board.length;
        this.COLS = board[0].length;

        for (int row = 0; row < this.ROWS; ++row)
            for (int col = 0; col < this.COLS; ++col)
                if (this.backtrack(row, col, word, 0))
                    return true;
        return false;
    }

    protected boolean backtrack(int row, int col, String word, int index) {
        /* Step 1). check the bottom case. */
        if (index >= word.length())
            return true;

        /* Step 2). Check the boundaries. */
        if (row < 0 || row == this.ROWS || col < 0 || col == this.COLS
                || this.board[row][col] != word.charAt(index))
            return false;

        /* Step 3). explore the neighbors in DFS */
        boolean ret = false;
        // mark the path before the next exploration
        this.board[row][col] = '#';

        int[] rowOffsets = {0, 1, 0, -1};
        int[] colOffsets = {1, 0, -1, 0};
        for (int d = 0; d < 4; ++d) {
            ret = this.backtrack(row + rowOffsets[d], col + colOffsets[d], word, index + 1);
            if (ret)
                break;
        }

        /* Step 4). clean up and return the result. */
        this.board[row][col] = word.charAt(index);
        return ret;
    }

    // V1-1
    // IDEA :
    // https://leetcode.com/problems/word-search/editorial/
    private char[][] board2;
    private int ROWS2;
    private int COLS2;

    public boolean exist_1_2(char[][] board, String word) {
        this.board2 = board;
        this.ROWS2 = board.length;
        this.COLS2 = board[0].length;

        for (int row = 0; row < this.ROWS2; ++row)
            for (int col = 0; col < this.COLS2; ++col)
                if (this.backtrack2(row, col, word, 0))
                    return true;
        return false;
    }

    protected boolean backtrack2(int row, int col, String word, int index) {
        /* Step 1). check the bottom case. */
        if (index >= word.length())
            return true;

        /* Step 2). Check the boundaries. */
        if (row < 0 || row == this.ROWS || col < 0 || col == this.COLS
                || this.board[row][col] != word.charAt(index))
            return false;

        /* Step 3). explore the neighbors in DFS */
        // mark the path before the next exploration
        this.board[row][col] = '#';

        int[] rowOffsets = {0, 1, 0, -1};
        int[] colOffsets = {1, 0, -1, 0};
        for (int d = 0; d < 4; ++d) {
            if (this.backtrack2(row + rowOffsets[d], col + colOffsets[d], word, index + 1))
                // return without cleanup
                return true;
        }

        /* Step 4). clean up and return the result. */
        this.board[row][col] = word.charAt(index);
        return false;
    }

}
