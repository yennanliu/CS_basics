package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/word-search/
/**
 * 79. Word Search
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an m x n grid of characters board and a string word, return true if word exists in the grid.
 *
 * The word can be constructed from letters of sequentially adjacent cells, where adjacent cells are horizontally or vertically neighboring. The same letter cell may not be used more than once.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCCED"
 * Output: true
 * Example 2:
 *
 *
 * Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "SEE"
 * Output: true
 * Example 3:
 *
 *
 * Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCB"
 * Output: false
 *
 *
 * Constraints:
 *
 * m == board.length
 * n = board[i].length
 * 1 <= m, n <= 6
 * 1 <= word.length <= 15
 * board and word consists of only lowercase and uppercase English letters.
 */
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

        /**
         * NOTE !!!
         *
         *   we use `idx == word.length()` to validate if a matched result is found
         *   -> since the actual checking code is after `idx == word.length()`,
         *   so when idx == word.length() - 1, we DON'T KNOW YET is current idx can match the word.
         *   However, when `idx == word.length()`, we're sure that prev recursion (e.g. idx == word.length() - 1 can find the expected word),
         *   -> we're sure that word is matched, then can return true
         *
         *  1)  Why idx == word.length():
         *   - idx represents the index of the current character you're trying to match in the word.
         *   - If idx reaches the length of the word (word.length()), it means you've successfully matched all characters in the word.
         *   - Remember that in a zero-based index system, the last character in a string has an index of word.length() - 1, so when idx is equal to word.length(), it means you've finished matching all characters.
         *
         *  2) Why not idx == word.length() - 1:
         *   - If you only checked if (idx == word.length() - 1),
         *    you would return true only when you've
         *    matched the `second-to-last` character in the word.
         *    This would result in a false positive — you'd think you've found the word just
         *    before you've actually matched the entire word.
         *
         *
         *  3) Correct Behavior:
         * - idx == word.length() ensures that you’ve matched the entire word.
         *
         * - For example, let’s walk through the process:
         *
         * - Starting at idx = 0, you check the first character ('A').
         * - Then at idx = 1, you check the second character ('B').
         * - Finally, at idx = 2, you check the last character ('C').
         * - -> Once idx == word.length(), i.e., idx == 3,
         *      you know the entire word has been matched,
         *      and you return true.
         *
         *
         * 4)  Conclusion:
         *   To summarize, checking if (idx == word.length()) ensures that the DFS continues
         *   until the entire word is matched, whereas if (idx == word.length() - 1) would
         *   incorrectly return true prematurely, right before the final character is matched.
         *
         */
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

        /**
         *
         * NOTE  !!! don't need below undo (lvl -= 1;)
         *
         *
         *  Reason:
         *
         *   - We don’t need start_idx -= 1;
         *    because start_idx is passed by value, not by reference.
         *    So modifying it in the recursive call
         *    doesn’t affect the caller’s start_idx.
         *    We’re already handling the correct index in
         *    each recursive call by passing start_idx + 1.
         *
         *
         *  In Java, primitive types like int are passed by value. This means when you do:
         *
         *
         *  dfsFind(board, word, x+1, y, visited, start_idx + 1)
         *
         *  -> You’re passing a `copy` of start_idx + 1 to the recursive function.
         *     So, inside the recursive call, start_idx is a `new variable`,
         *     and changes to it won’t affect the start_idx in the calling function.
         *
         *
         *  - Why does undo apply only to visited[y][x]?
         *
         *    -> Because visited is a 2D array (object in memory),
         *       and arrays in Java are passed by reference.
         *       So modifying visited[y][x] does affect the caller’s version
         *       — that’s why we need to undo it when backtracking:
         *
         */
        //lvl -= 1;

        return false;
    }

    // V0-1
    // IDEA: DFS + BACKTRACK (GPT)
    public boolean exist_0_1(char[][] board, String word) {
        // Edge case: empty board
        if (board.length == 0 || board[0].length == 0) {
            return false;
        }

        int rows = board.length;
        int cols = board[0].length;
        boolean[][] visited = new boolean[rows][cols];

        // Try starting DFS from each cell
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == word.charAt(0) && dfs_0_1(board, word, i, j, 0, visited)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean dfs_0_1(char[][] board, String word, int x, int y, int idx, boolean[][] visited) {
        // If all characters are found
        if (idx == word.length()) {
            return true;
        }

        // Boundary and character check
        if (x < 0 || x >= board.length || y < 0 || y >= board[0].length || visited[x][y] || board[x][y] != word.charAt(idx)) {
            return false;
        }

        // Mark as visited
        visited[x][y] = true;

        // Explore all four directions
        int[][] moves = { {0, 1}, {0, -1}, {1, 0}, {-1, 0} };
        for (int[] move : moves) {
            /** NOTE !!!
             *
             *  if below recursive is true, return true directly
             */
            if (dfs_0_1(board, word, x + move[0], y + move[1], idx + 1, visited)) {
                return true;
            }
        }

        // Backtrack
        visited[x][y] = false;

        return false;
    }

    // V0-2
    // IDEA : DFS + BACKTRACK (modified by GPT)
    public boolean exist_0_2(char[][] board, String word) {
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
     */
    /**
     * 1) Detailed Explanation:
     *
     *  Role of return true in the Loop
     *
     * 	1.	Backtracking Recursion:
     * 	    •	The function backtrack is called recursively to explore possible paths in the grid.
     * 	    •	Each recursive call either returns true if the word can be constructed from the current path or false if it cannot.
     *
     * 	2.	Returning Early:
     * 	    •	As soon as one of the recursive calls returns true (indicating the word has been found), there is no need to continue exploring other directions. The word has already been successfully constructed.
     *
     * 	3.	Efficiency:
     * 	    •	Exiting the loop early saves computation by avoiding exploration of unnecessary paths.
     *
     *
     *  2) What Happens Without return true?
     *
     *  -> If the return true is omitted, the function will:
     * 	    1.	Continue to check all remaining directions in the directions array, even after finding a valid path.
     * 	    2.	Complete all recursive calls, backtrack, and ultimately return false for the current recursion level, even if a valid path exists deeper in the recursion tree.
     *
     *  -> This would result in the algorithm failing to detect that the word is present in the grid.
     *
     *
     *  3) Example:
     *      Let’s consider a small grid:
     *
     *   board = [
     *      ['A', 'B'],
     *      ['C', 'D']
     *      ];
     *   word = "AB";
     *
     *
     *  Start at (0, 0) (value A), matching the first character.
     * 	    •	Check neighbors:
     * 	    •	Move right to (0, 1) (value B), matching the second character. At this point, the word is found, so we return true.
     *
     *  With return true:
     *  	•	When the recursive call to (0, 1) returns true, the loop exits immediately, and the function propagates true all the way up.
     *
     *  Without return true:
     * 	    •	Even after (0, 1) finds the word, the function continues checking other directions (down, left, up), wasting computation. Eventually, it backtracks, losing the valid result.
     *
     *
     *
     * 4) Conclusion
     *
     * Returning true immediately when a valid path is found is
     * both correct and efficient. It skips redundant exploration
     * and ensures that the recursion terminates as soon as the word is found.
     * Other recursive logic is unaffected since the backtracking process
     * stops as soon as we achieve the goal.
     *
     */
    for (int[] dir : dirs) {
            if (dfs_(board, y + dir[0], x + dir[1], idx + 1, word, visited)) {
                /** NOTE !!!
                 *
                 *  need to return true IMMEDIATELY if a true solution is found
                 */
                return true;
            }
        }

        /** NOTE !!! we undo (backtrack) updated x, y here */
        visited[y][x] = false;

        return false;
    }

    // V0-3
    // IDEA : modified version of v0' (gpt)
    public boolean exist_0_3(char[][] board, String word) {
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

    // V0-4
    // IDEA: DFS + BACKTRACK (fixed by gpt)
    public boolean exist_0_4(char[][] board, String word) {
        // edge
        if (board.length == 0 || board[0].length == 0) {
            return false;
        }
        if (word == null || word.length() == 0) {
            return true; // ???
        }

        int l = board.length;
        int w = board[0].length;

        Boolean[][] visited = new Boolean[l][w]; // ??? init val = false
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < w; j++) {
                visited[i][j] = false;
            }
        }

        // dfs
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < w; j++) {
                if (board[i][j] == word.charAt(0)) {
                    if (canFind(board, word, j, i, visited, 0)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean canFind(char[][] board, String word, int x, int y, Boolean[][] visited, int idx) {

        int l = board.length;
        int w = board[0].length;

        if (idx == word.length()) {
            return true;
        }

        if (idx > word.length()) {
            return false;
        }

        // NOTE !!! we validate condition before go into `for loop and recursive call`
        if (x < 0 || x >= w || y < 0 || y >= l || visited[y][x] || board[y][x] != word.charAt(idx)) {
            return false;
        }

        //int[][] dirs = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        visited[y][x] = true;

        /**
         *  NOTE !!!
         *
         *   1) we use below structure
         *      if ( recursive_call_1 or recursive_call_2 ..) {
         *              return true
         *      }
         *
         *   2) since we need to `undo` visited record
         *      so after above logic, we modify visited[y][x] back to false (e.g. non-visited)
         *
         *   3) RETURN `false` at the final of recursive call
         *      -> since it can reach this point,
         *      -> means NOT POSSIBLE to find a solution
         *      -> return false
         */

        if (canFind(board, word, x + 1, y, visited, idx + 1) ||
                canFind(board, word, x - 1, y, visited, idx + 1) ||
                canFind(board, word, x, y + 1, visited, idx + 1) ||
                canFind(board, word, x, y - 1, visited, idx + 1)) {
            return true;
        }

        // undo
        visited[y][x] = false;

        /**
         * 3) RETURN `false` at the final of recursive call
         */
        return false;
    }

    // V1-1
    // https://neetcode.io/problems/search-for-word
    // IDEA: Backtracking (Hash Set)
//    private int ROWS, COLS;
//    private Set<Pair<Integer, Integer>> path = new HashSet<>();
//
//    public boolean exist_1_1(char[][] board, String word) {
//        ROWS = board.length;
//        COLS = board[0].length;
//
//        for (int r = 0; r < ROWS; r++) {
//            for (int c = 0; c < COLS; c++) {
//                if (dfs(board, word, r, c, 0)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private boolean dfs(char[][] board, String word, int r, int c, int i) {
//        if (i == word.length()) {
//            return true;
//        }
//
//        if (r < 0 || c < 0 || r >= ROWS || c >= COLS ||
//                board[r][c] != word.charAt(i) ||
//                path.contains(new Pair<>(r, c))) {
//            return false;
//        }
//
//        path.add(new Pair<>(r, c));
//        boolean res = dfs(board, word, r + 1, c, i + 1) ||
//                dfs(board, word, r - 1, c, i + 1) ||
//                dfs(board, word, r, c + 1, i + 1) ||
//                dfs(board, word, r, c - 1, i + 1);
//        path.remove(new Pair<>(r, c));
//
//        return res;
//    }


    // V1-2
    // https://neetcode.io/problems/search-for-word
    // IDEA: Backtracking (Visited Array)
    private int ROWS_1_2, COLS_1_2;
    private boolean[][] visited;

    public boolean exist_1_2(char[][] board, String word) {
        ROWS_1_2 = board.length;
        COLS_1_2 = board[0].length;
        visited = new boolean[ROWS_1_2][COLS_1_2];

        for (int r = 0; r < ROWS_1_2; r++) {
            for (int c = 0; c < COLS_1_2; c++) {
                if (dfs(board, word, r, c, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfs(char[][] board, String word, int r, int c, int i) {
        if (i == word.length()) {
            return true;
        }

        if (r < 0 || c < 0 || r >= ROWS_1_2 || c >= COLS_1_2 ||
                board[r][c] != word.charAt(i) || visited[r][c]) {
            return false;
        }

        visited[r][c] = true;
        boolean res = dfs(board, word, r + 1, c, i + 1) ||
                dfs(board, word, r - 1, c, i + 1) ||
                dfs(board, word, r, c + 1, i + 1) ||
                dfs(board, word, r, c - 1, i + 1);
        visited[r][c] = false;

        return res;
    }


    // V1-3
    // https://neetcode.io/problems/search-for-word
    // IDEA: Backtracking (Optimal)
    private int ROWS_1_3, COLS_1_3;

    public boolean exist_1_3(char[][] board, String word) {
        ROWS_1_3 = board.length;
        COLS_1_3 = board[0].length;

        for (int r = 0; r < ROWS_1_3; r++) {
            for (int c = 0; c < COLS_1_3; c++) {
                if (dfs_1_3(board, word, r, c, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfs_1_3(char[][] board, String word, int r, int c, int i) {
        if (i == word.length()) {
            return true;
        }
        if (r < 0 || c < 0 || r >= ROWS_1_3 || c >= COLS_1_3 ||
                board[r][c] != word.charAt(i) || board[r][c] == '#') {
            return false;
        }

        board[r][c] = '#';
        boolean res = dfs_1_3(board, word, r + 1, c, i + 1) ||
                dfs_1_3(board, word, r - 1, c, i + 1) ||
                dfs_1_3(board, word, r, c + 1, i + 1) ||
                dfs_1_3(board, word, r, c - 1, i + 1);
        board[r][c] = word.charAt(i);
        return res;
    }


    // V2
    // IDEA : DFS + BACKTRACK
    // https://leetcode.com/problems/word-search/solutions/4791515/java-easy-solution-dfs-backtracking/
    public boolean exist_2(char[][] board, String word) {
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

    // V3
    // IDEA : V1 variation
    public boolean exist_3(char[][] board, String word) {
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

    
    // V4
    // IDEA : BACKTRACK
    // https://leetcode.com/problems/word-search/editorial/
    private char[][] board;
    private int ROWS_4;
    private int COLS_4;

    public boolean exist_4(char[][] board, String word) {
        this.board = board;
        this.ROWS_4 = board.length;
        this.COLS_4 = board[0].length;

        for (int row = 0; row < this.ROWS_4; ++row)
            for (int col = 0; col < this.COLS_4; ++col)
                if (this.backtrack(row, col, word, 0))
                    return true;
        return false;
    }

    protected boolean backtrack(int row, int col, String word, int index) {
        /* Step 1). check the bottom case. */
        if (index >= word.length())
            return true;

        /* Step 2). Check the boundaries. */
        if (row < 0 || row == this.ROWS_4 || col < 0 || col == this.COLS_4
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

    // V5
    // IDEA :
    // https://leetcode.com/problems/word-search/editorial/
    private char[][] board2;
    private int ROWS2;
    private int COLS2;

    public boolean exist_5(char[][] board, String word) {
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
        if (row < 0 || row == this.ROWS2 || col < 0 || col == this.COLS2
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
