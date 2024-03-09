package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/word-search/


public class WordSearch {

    // V0
    // IDEA : DFS + BACKTRACK
    public boolean exist(char[][] board, String word) {

        int n = board.length;
        int m = board[0].length;

        /** NOTE !!! we define visited */
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

        /** NOTE !!! mark cur x, y as visited */
        visited[row][col] = true;
        if ( dfs(row + 1, col, word, lvl + 1, visited, board) ||
             dfs(row - 1, col, word, lvl + 1, visited, board) ||
             dfs(row, col + 1, word, lvl + 1, visited, board) ||
             dfs(row, col - 1, word, lvl + 1, visited, board) ){
            return true;
        }

        /** NOTE !!! Backtrack : undo cur x, y as visited */
        visited[row][col] = false;  // Backtrack

        return false;
    }

    // V1
    // IDEA : DFS + BACKTRACK
    // https://leetcode.com/problems/word-search/solutions/4791515/java-easy-solution-dfs-backtracking/
    public boolean exist2(char[][] board, String word) {
        int n = board.length;
        int m = board[0].length;

        boolean[][] visited = new boolean[n][m];

        for(int row = 0; row < n; row++){
            for(int col = 0; col < m; col++){
                if(board[row][col] == word.charAt(0)){
                    if(dfs2(row, col, word, 0, visited, board)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean dfs2(int row, int col, String word, int lvl, boolean[][] visited, char[][] board){
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
                dfs2(row + 1, col, word, lvl + 1, visited, board) ||
                dfs2(row - 1, col, word, lvl + 1, visited, board) ||
                dfs2(row, col + 1, word, lvl + 1, visited, board) ||
                dfs2(row, col - 1, word, lvl + 1, visited, board);

        visited[row][col] = false;  // Backtrack
        return didFindNextCharacter;
    }

    // V1'
    // IDEA : V1 variation
    public boolean exist_1(char[][] board, String word) {
        int n = board.length;
        int m = board[0].length;

        boolean[][] visited = new boolean[n][m];

        for(int row = 0; row < n; row++){
            for(int col = 0; col < m; col++){
                if(board[row][col] == word.charAt(0)){
                    if(dfs_1(row, col, word, 0, visited, board)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean dfs_1(int row, int col, String word, int lvl, boolean[][] visited, char[][] board){
        int n = board.length;
        int m = board[0].length;

        if(lvl == word.length()){
            return true;
        }

        if(row < 0 || row >= n || col < 0 || col >= m || visited[row][col] || board[row][col] != word.charAt(lvl)){
            return false;
        }

        visited[row][col] = true;

        if (dfs_1(row + 1, col, word, lvl + 1, visited, board)) {
            visited[row][col] = false;  // Backtrack
            return true;
        }
        if (dfs_1(row - 1, col, word, lvl + 1, visited, board)) {
            visited[row][col] = false;  // Backtrack
            return true;
        }
        if (dfs_1(row, col + 1, word, lvl + 1, visited, board)) {
            visited[row][col] = false;  // Backtrack
            return true;
        }
        if (dfs_1(row, col - 1, word, lvl + 1, visited, board)) {
            visited[row][col] = false;  // Backtrack
            return true;
        }

        visited[row][col] = false;  // Backtrack
        return false;
    }

    
    // V1''
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/word-search/editorial/
    private char[][] board;
    private int ROWS;
    private int COLS;

    public boolean exist_1_(char[][] board, String word) {
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
