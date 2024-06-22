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

    // V0'
    // IDEA : DFS + BACKTRACK (modified by GPT)
    public boolean exist_0(char[][] board, String word) {
        if (board == null || board.length == 0) {
            return false;
        }

        int l = board.length;
        int w = board[0].length;

        boolean[][] visited = new boolean[l][w];

        for (int i = 0; i < l; i++) {
            for (int j = 0; j < w; j++) {
                if (dfs_(board, i, j, 0, word, visited)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean dfs_(char[][] board, int y, int x, int idx, String word, boolean[][] visited) {

        if (idx == word.length()) {
            return true;
        }

        int l = board.length;
        int w = board[0].length;

        if (y < 0 || y >= l || x < 0 || x >= w || visited[y][x] || board[y][x] != word.charAt(idx)) {
            return false;
        }

        /** NOTE !!! we update visited on x, y here */
        visited[y][x] = true;

        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        /**
         *  NOTE !!!
         *
         *   instead of below structure:
         *
         *       boolean didFindNextCharacter =
         *                 dfs2(row + 1, col, word, lvl + 1, visited, board) ||
         *                 dfs2(row - 1, col, word, lvl + 1, visited, board) ||
         *                 dfs2(row, col + 1, word, lvl + 1, visited, board) ||
         *                 dfs2(row, col - 1, word, lvl + 1, visited, board);
         *
         *   we can use below logic as well:
         *
         *          for (int[] dir : dirs) {
         *             if (dfs_(board, y + dir[0], x + dir[1], idx + 1, word, visited)) {
         *                 return true;
         *             }
         *         }
         *
         */
        for (int[] dir : dirs) {
            if (dfs_(board, y + dir[0], x + dir[1], idx + 1, word, visited)) {
                return true;
            }
        }

        /** NOTE !!! we undo (backtrack) updated x, y here */
        visited[y][x] = false;

        return false;
    }

    // V0''
    // IDEA : modified version of v0' (gpt)
    public boolean exist_0_1(char[][] board, String word) {
        if (board == null || board.length == 0) {
            return false;
        }

        int l = board.length;
        int w = board[0].length;

        boolean[][] visited = new boolean[l][w];

        for (int i = 0; i < l; i++) {
            for (int j = 0; j < w; j++) {
                if (dfs_0_1(board, i, j, 0, word, visited)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean dfs_0_1(char[][] board, int y, int x, int idx, String word, boolean[][] visited) {
        if (idx == word.length()) {
            return true;
        }

        int l = board.length;
        int w = board[0].length;

        if (y < 0 || y >= l || x < 0 || x >= w || visited[y][x] || board[y][x] != word.charAt(idx)) {
            return false;
        }

        // Mark this cell as visited
        visited[y][x] = true;

        // Directions array for exploring up, down, left, and right
        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        /** NOTE !!! init found flag, for tracking found status */
        boolean found = false; // Variable to store if the word is found in any direction

        // Explore all four directions
        for (int[] dir : dirs) {
            if (dfs_0_1(board, y + dir[0], x + dir[1], idx + 1, word, visited)) {
                /** NOTE !!! if found, update found status */
                found = true;
                /** NOTE !!!
                 *
                 *  if found, break the loop,
                 *  then will go to final line in dfs (e.g. return found;),
                 *  and return found status directly
                 */
                break; // We can break out of the loop if we found the word in any direction
            }
        }

        // Backtrack: unmark this cell as visited
        visited[y][x] = false;

        return found;
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
