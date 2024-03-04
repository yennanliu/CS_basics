package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/word-search/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordSearch {

//      // TODO : fix V0
//      // V0
//      // IDEA : DFS OR BACKTRACK
//      private int len;
//      private int wid;
//      private char[][] board_;
//
//      public boolean exist(char[][] board, String word) {
//
//          if (board == null || board.length == 0){
//              return false;
//          }
//
//          len = board.length;
//          wid = board[0].length;
//          board_ = board;
//
//          for (int y = 0; y < len; y++){
//              for (int x = 0; x < wid; x++){
//                  int index = 0;
//                  if (this.backtrack_(x, y, word, index)){
//                      return true;
//                  }
//              }
//          }
//
//          return false;
//      }
//
//    boolean backtrack_(int x, int y, String word, int index){
//
//          if (index >= word.length()){
//            return true;
//          }
//
//          if (x < 0 || x >= wid || y < 0 || y >= len || this.board_[y][x] != word.charAt(index)){
//              return false;
//          }
//
//          //public int[][] DIRECTIONS = new int[][]{{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
//          int[][] moves = new int[][]{ {0, 1}, {1, 0}, {-1, 0}, {0, -1} };
//          for (int[] move : moves){
//              index += 1;
//              backtrack_(move[0], move[1], word, index);
//              this.board[x][y] = word.charAt(index);
//              index -= 1;
//          }
//          return false;
//    }


    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/word-search/solutions/4791515/java-easy-solution-dfs-backtracking/
    public boolean exist(char[][] board, String word) {
        int n = board.length;
        int m = board[0].length;

        boolean[][] visited = new boolean[n][m];

        for(int row = 0; row < n; row++){
            for(int col = 0; col < m; col++){
                if(board[row][col] == word.charAt(0)){
                    if(dfs(row, col, word, 0, visited, board)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean dfs(int row, int col, String word, int lvl, boolean[][] visited, char[][] board){
        int n = board.length;
        int m = board[0].length;

        if(lvl == word.length()){
            return true;
        }

        if(row < 0 || row >= n || col < 0 || col >= m || visited[row][col] || board[row][col] != word.charAt(lvl)){
            return false;
        }

        visited[row][col] = true;

        boolean didFindNextCharacter =
                dfs(row + 1, col, word, lvl + 1, visited, board)||
                        dfs(row - 1, col, word, lvl + 1, visited, board)||
                        dfs(row, col + 1, word, lvl + 1, visited, board)||
                        dfs(row, col - 1, word, lvl + 1, visited, board);

        visited[row][col] = false;  // Backtrack
        return didFindNextCharacter;
    }
    
    // V1'
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
