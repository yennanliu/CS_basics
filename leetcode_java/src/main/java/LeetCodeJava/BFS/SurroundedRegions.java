package LeetCodeJava.BFS;

// https://leetcode.com/problems/surrounded-regions/
/**
 * 130. Surrounded Regions
 * Solved
 * Medium
 * Topics
 * Companies
 * You are given an m x n matrix board containing letters 'X' and 'O', capture regions that are surrounded:
 *
 * Connect: A cell is connected to adjacent cells horizontally or vertically.
 * Region: To form a region connect every 'O' cell.
 * Surround: The region is surrounded with 'X' cells if you can connect the region with 'X' cells and none of the region cells are on the edge of the board.
 * To capture a surrounded region, replace all 'O's with 'X's in-place within the original board. You do not need to return anything.
 *
 *
 *
 * Example 1:
 *
 * Input: board = [["X","X","X","X"],["X","O","O","X"],["X","X","O","X"],["X","O","X","X"]]
 *
 * Output: [["X","X","X","X"],["X","X","X","X"],["X","X","X","X"],["X","O","X","X"]]
 *
 * Explanation:
 *
 *
 * In the above diagram, the bottom region is not captured because it is on the edge of the board and cannot be surrounded.
 *
 * Example 2:
 *
 * Input: board = [["X"]]
 *
 * Output: [["X"]]
 *
 *
 *
 * Constraints:
 *
 * m == board.length
 * n == board[i].length
 * 1 <= m, n <= 200
 * board[i][j] is 'X' or 'O'.
 *
 */
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SurroundedRegions {

    // V0
    // IDEA : DFS (fixed by gpt)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Breadth-First-Search/surrounded-regions.py
    /**
     *  3 steps
     *
     *  step 1) mark all `0` but connected to `boundary cell as '#'
     *
     *  step 2) mark rest of the `0` as 'X`
     *
     *  step 3) mark all '#' as '0'
     *
     */
    public void solve(char[][] board) {

        // edge
        if (board == null || board.length == 0 || board[0].length == 0) {
            return;
        }

        if (board.length == 1 || board[0].length == 1) {
            return;
        }

        int l = board.length;
        int w = board[0].length;

        // Step 1: Mark boundary-connected 'O's as '#'
        // NOTE !!! we can merge y-direction, x-direction visiting
        for (int i = 0; i < w; i++) {
            markAsNotAffected(board, i, 0);
            markAsNotAffected(board, i, l - 1);
        }

        for (int j = 0; j < l; j++) {
            markAsNotAffected(board, 0, j);
            markAsNotAffected(board, w - 1, j);
        }

        // Step 2: Flip the remaining 'O's to 'X'
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < w; j++) {
                /**
                 *  NOTE !!!
                 *
                 *  board[y][x],
                 *
                 *  so the first is Y-coordination
                 *  and the second is X-coordination
                 *
                 */
                if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                }
            }
        }

        // mark `all #` as `O`
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < w; j++) {
                if (board[i][j] == '#') {
                    board[i][j] = 'O';
                }
            }
        }

    }

    public void markAsNotAffected(char[][] board, int x, int y) {

        int l = board.length;
        int w = board[0].length;

        // NOTE !!! we validate below
        if (x < 0 || x >= w || y < 0 || y >= l || board[y][x] != 'O') {
            return;
        }

        // mark as visited
        board[y][x] = '#';

        markAsNotAffected(board, x + 1, y);
        markAsNotAffected(board, x - 1, y);
        markAsNotAffected(board, x, y + 1);
        markAsNotAffected(board, x, y - 1);
    }

    // V0-0-1
    // IDEA: DFS (fixed by gpt)
    public void solve_0_0_1(char[][] board) {
        if (board == null || board.length == 0)
            return;

        int l = board.length;
        int w = board[0].length;

        // Step 1 — Mark all boundary 'O' as '#'
        for (int x = 0; x < w; x++) {
            if (board[0][x] == 'O')
                dfsMark_0_0_1(0, x, board);
            if (board[l - 1][x] == 'O')
                dfsMark_0_0_1(l - 1, x, board);
        }
        for (int y = 0; y < l; y++) {
            if (board[y][0] == 'O')
                dfsMark_0_0_1(y, 0, board);
            if (board[y][w - 1] == 'O')
                dfsMark_0_0_1(y, w - 1, board);
        }

        // Step 2 — Flip remaining 'O' → 'X'
        for (int y = 0; y < l; y++) {
            for (int x = 0; x < w; x++) {
                if (board[y][x] == 'O')
                    board[y][x] = 'X';
            }
        }

        // Step 3 — Restore '#' → 'O'
        for (int y = 0; y < l; y++) {
            for (int x = 0; x < w; x++) {
                if (board[y][x] == '#')
                    board[y][x] = 'O';
            }
        }
    }

    // DFS to mark connected boundary area
    private void dfsMark_0_0_1(int y, int x, char[][] board) {
        int l = board.length, w = board[0].length;
        if (y < 0 || y >= l || x < 0 || x >= w || board[y][x] != 'O')
            return;

        board[y][x] = '#'; // mark as safe region

        dfsMark_0_0_1(y + 1, x, board);
        dfsMark_0_0_1(y - 1, x, board);
        dfsMark_0_0_1(y, x + 1, board);
        dfsMark_0_0_1(y, x - 1, board);
    }

    // V0-0-2
    // IDEA: DFS (fixed by gemini)
    private int rows;
    private int cols;
    // Moves are (dr, dc)
    private final int[][] MOVES = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

    public void solve_0_0_2(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) {
            return;
        }

        this.rows = board.length;
        this.cols = board[0].length;

        // --- Step 1: Mark all border 'O's and their connected components as 'E' (Exempt) ---

        // 1. Traverse top (r=0) and bottom (r=rows-1) rows
        for (int c = 0; c < cols; c++) {
            // Start DFS only if the border cell is 'O'
            if (board[0][c] == 'O') {
                dfs(board, 0, c);
            }
            if (board[rows - 1][c] == 'O') {
                dfs(board, rows - 1, c);
            }
        }

        // 2. Traverse left (c=0) and right (c=cols-1) columns
        // NOTE: Corners (0, 0), (0, cols-1), etc., are handled by the previous loops, but re-checking is fine.
        for (int r = 0; r < rows; r++) {
            if (board[r][0] == 'O') {
                dfs(board, r, 0);
            }
            if (board[r][cols - 1] == 'O') {
                dfs(board, r, cols - 1);
            }
        }

        // --- Step 2: Finalize the Board ---
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == 'O') {
                    // This 'O' was not reached from the border, so it is surrounded. Flip it to 'X'.
                    board[r][c] = 'X';
                } else if (board[r][c] == 'E') {
                    // This 'E' was reached from the border (Exempt). Flip it back to 'O'.
                    board[r][c] = 'O';
                }
            }
        }
    }

    /**
     * DFS helper to mark connected 'O' cells as 'E'.
     */
    private void dfs(char[][] board, int r, int c) {
        // Base Case: Check bounds and check if already visited or not land ('O').
        if (r < 0 || r >= rows || c < 0 || c >= cols || board[r][c] != 'O') {
            return;
        }

        // Mark the current cell as exempt
        board[r][c] = 'E';

        // Move to 4 neighbors
        for (int[] move : MOVES) {
            dfs(board, r + move[0], c + move[1]);
        }
    }



    // V0-1
    // IDEA: DFS (fixed by gpt)
    public void solve_0_1(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0)
            return;

        int rows = board.length;
        int cols = board[0].length;

        // Step 1: Mark boundary-connected 'O's as '#'
        for (int i = 0; i < rows; i++) {
            markAsNotAffected_(board, i, 0);
            markAsNotAffected_(board, i, cols - 1);
        }
        for (int j = 0; j < cols; j++) {
            markAsNotAffected_(board, 0, j);
            markAsNotAffected_(board, rows - 1, j);
        }

        // Step 2: Flip the remaining 'O's to 'X'
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                }
            }
        }

        // Step 3: Convert '#' back to 'O'
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == '#') {
                    board[i][j] = 'O';
                }
            }
        }
    }

    private void markAsNotAffected_(char[][] board, int row, int col) {
        int rows = board.length;
        int cols = board[0].length;

        // Bounds check + base case
        if (row < 0 || col < 0 || row >= rows || col >= cols || board[row][col] != 'O') {
            return;
        }

        board[row][col] = '#'; // mark as visited

        // Recurse in 4 directions
        markAsNotAffected_(board, row + 1, col);
        markAsNotAffected_(board, row - 1, col);
        markAsNotAffected_(board, row, col + 1);
        markAsNotAffected_(board, row, col - 1);
    }

    // V1-1
    // https://neetcode.io/problems/surrounded-regions
    // IDEA: DFS
    private int ROWS, COLS;

    public void solve_1_1(char[][] board) {
        ROWS = board.length;
        COLS = board[0].length;

        for (int r = 0; r < ROWS; r++) {
            if (board[r][0] == 'O') {
                capture(board, r, 0);
            }
            if (board[r][COLS - 1] == 'O') {
                capture(board, r, COLS - 1);
            }
        }

        for (int c = 0; c < COLS; c++) {
            if (board[0][c] == 'O') {
                capture(board, 0, c);
            }
            if (board[ROWS - 1][c] == 'O') {
                capture(board, ROWS - 1, c);
            }
        }

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] == 'O') {
                    board[r][c] = 'X';
                } else if (board[r][c] == 'T') {
                    board[r][c] = 'O';
                }
            }
        }
    }

    private void capture(char[][] board, int r, int c) {
        if (r < 0 || c < 0 || r >= ROWS ||
                c >= COLS || board[r][c] != 'O') {
            return;
        }
        board[r][c] = 'T';
        capture(board, r + 1, c);
        capture(board, r - 1, c);
        capture(board, r, c + 1);
        capture(board, r, c - 1);
    }


    // V1-2
    // https://neetcode.io/problems/surrounded-regions
    // IDEA: BFS
    //private int ROWS, COLS;
    private int[][] directions = new int[][]{
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
    };

    public void solve_1_2(char[][] board) {
        ROWS = board.length;
        COLS = board[0].length;

        capture(board);

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] == 'O') {
                    board[r][c] = 'X';
                } else if (board[r][c] == 'T') {
                    board[r][c] = 'O';
                }
            }
        }
    }

    private void capture(char[][] board) {
        Queue<int[]> q = new LinkedList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (r == 0 || r == ROWS - 1 ||
                        c == 0 || c == COLS - 1 &&
                        board[r][c] == 'O') {
                    q.offer(new int[]{r, c});
                }
            }
        }
        while (!q.isEmpty()) {
            int[] cell = q.poll();
            int r = cell[0], c = cell[1];
            if (board[r][c] == 'O') {
                board[r][c] = 'T';
                for (int[] direction : directions) {
                    int nr = r + direction[0], nc = c + direction[1];
                    if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS) {
                        q.offer(new int[]{nr, nc});
                    }
                }
            }
        }
    }


    // V1-3
    // https://neetcode.io/problems/surrounded-regions
    // IDEA: Disjoint Set Union


    // V2
    // IDEA : DFS (fixed by gpt)
    public void solve_2(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) {
            return;
        }

        int rows = board.length;
        int cols = board[0].length;

        // Mark border 'O's and connected 'O's as temporary 'T'
        // NOTE !!! mark as "T" (so we can flip them as "O" in the following step)
        for (int i = 0; i < rows; i++) {
            if (board[i][0] == 'O') {
                dfsMark(i, 0, board);
            }
            if (board[i][cols - 1] == 'O') {
                dfsMark(i, cols - 1, board);
            }
        }
        // Mark border 'O's and connected 'O's as temporary 'T'
        // NOTE !!! mark as "T" (so we can flip them as "O" in the following step)
        for (int j = 0; j < cols; j++) {
            if (board[0][j] == 'O') {
                dfsMark(0, j, board);
            }
            if (board[rows - 1][j] == 'O') {
                dfsMark(rows - 1, j, board);
            }
        }

        // Flip all remaining 'O' to 'X' and 'T' back to 'O'
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                /**
                 * NOTE !!!
                 *
                 *  if still 'O', means
                 *
                 *   1) they are connected 'O', surrounded by X
                 *   2) and NOT at boundary
                 */
                if (board[y][x] == 'O') {
                    board[y][x] = 'X';
                /**
                 *  NOTE !!!
                 *
                 *   if is 'T', means
                 *
                 *   1) they are connected 'O', but at boundary
                 *
                 *   so need to flip them back as 'O'
                 */
                } else if (board[y][x] == 'T') {
                    board[y][x] = 'O';
                }
            }
        }
    }

    private void dfsMark(int y, int x, char[][] board) {
        int rows = board.length;
        int cols = board[0].length;

        // If out of bounds or not 'O', return
        if (y < 0 || y >= rows || x < 0 || x >= cols || board[y][x] != 'O') {
            return;
        }

        // Mark 'O' as temporary 'T'
        board[y][x] = 'T';

        // Explore all 4 directions
        int[][] moves = { {0, 1}, {0, -1}, {1, 0}, {-1, 0} };
        for (int[] move : moves) {
            int newY = y + move[0];
            int newX = x + move[1];
            dfsMark(newY, newX, board);
        }
    }

    // V3
    // IDEA : DFS
    // https://leetcode.com/problems/surrounded-regions/editorial/

    class Pair<U, V> {
        public U first;
        public V second;

        public Pair(U first, V second) {
            this.first = first;
            this.second = second;
        }
    }

//    protected Integer ROWS = 0;
//    protected Integer COLS = 0;

    public void solve_3(char[][] board) {
        if (board == null || board.length == 0) {
            return;
        }
        this.ROWS = board.length;
        this.COLS = board[0].length;

        List<Pair<Integer, Integer>> borders = new LinkedList<Pair<Integer, Integer>>();
        // Step 1). construct the list of border cells
        // NOTE !!! this is "border" list for escaped celles (not considered as island)
        for (int r = 0; r < this.ROWS; ++r) {
            borders.add(new Pair(r, 0));
            borders.add(new Pair(r, this.COLS - 1));
        }
        for (int c = 0; c < this.COLS; ++c) {
            borders.add(new Pair(0, c));
            borders.add(new Pair(this.ROWS - 1, c));
        }

        // Step 2). mark the escaped cells
        // NOTE !!!  dfs only implement on "escaped cells"
        //           so
        for (Pair<Integer, Integer> pair : borders) {
            this.DFS(board, pair.first, pair.second);
        }

        // Step 3). flip the cells to their correct final states
        for (int r = 0; r < this.ROWS; ++r) {
            for (int c = 0; c < this.COLS; ++c) {
                if (board[r][c] == 'O')
                    board[r][c] = 'X';
                if (board[r][c] == 'E')
                    board[r][c] = 'O';
            }
        }
    }

    protected void DFS(char[][] board, int row, int col) {

        if (board[row][col] != 'O')
            return;

        board[row][col] = 'E';

        // TODO : double check below
        if (col < this.COLS - 1)
            this.DFS(board, row, col + 1);
        if (row < this.ROWS - 1)
            this.DFS(board, row + 1, col);
        if (col > 0)
            this.DFS(board, row, col - 1);
        if (row > 0)
            this.DFS(board, row - 1, col);
    }


    // V4
    // IDEA : BFS
    // https://leetcode.com/problems/surrounded-regions/editorial/
    class Pair2<U, V> {
        public U first;
        public V second;

        public Pair2(U first, V second) {
            this.first = first;
            this.second = second;
        }

    }

    protected Integer ROWS2 = 0;
    protected Integer COLS2 = 0;

    public void solve_4(char[][] board) {
        if (board == null || board.length == 0) {
            return;
        }
        this.ROWS2 = board.length;
        this.COLS2 = board[0].length;

        List<Pair<Integer, Integer>> borders = new LinkedList<Pair<Integer, Integer>>();
        // Step 1). construct the list of border cells
        for (int r = 0; r < this.ROWS2; ++r) {
            borders.add(new Pair(r, 0));
            borders.add(new Pair(r, this.COLS2 - 1));
        }
        for (int c = 0; c < this.COLS2; ++c) {
            borders.add(new Pair(0, c));
            borders.add(new Pair(this.ROWS2 - 1, c));
        }

        // Step 2). mark the escaped cells
        for (Pair<Integer, Integer> pair : borders) {
            this.BFS(board, pair.first, pair.second);
        }

        // Step 3). flip the cells to their correct final states
        for (int r = 0; r < this.ROWS2; ++r) {
            for (int c = 0; c < this.COLS2; ++c) {
                if (board[r][c] == 'O')
                    board[r][c] = 'X';
                if (board[r][c] == 'E')
                    board[r][c] = 'O';
            }
        }
    }

        protected void BFS(char[][] board, int r, int c) {
            LinkedList<Pair2<Integer, Integer>> queue = new LinkedList<Pair2<Integer, Integer>>();
            queue.offer(new Pair2<>(r, c));

            while (!queue.isEmpty()) {
                Pair2<Integer, Integer> pair = queue.pollFirst();
                int row = pair.first, col = pair.second;
                if (board[row][col] != 'O')
                    continue;

                board[row][col] = 'E';
                if (col < this.COLS - 1)
                    queue.offer(new Pair2<>(row, col + 1));
                if (row < this.ROWS - 1)
                    queue.offer(new Pair2<>(row + 1, col));
                if (col > 0)
                    queue.offer(new Pair2<>(row, col - 1));
                if (row > 0)
                    queue.offer(new Pair2<>(row - 1, col));
            }
        }

    // V5
    // IDEA : DFS
    // https://leetcode.com/problems/surrounded-regions/solutions/3805983/java-100-faster-step-by-step-explained/
    public void solve_5(char[][] board) {
        int rows = board.length;
        int cols = board[0].length;

        int[] delRows = {-1, 0, 1, 0}; // Array to represent changes in row index to traverse in all four directions
        int[] delCols = {0, 1, 0, -1}; // Array to represent changes in column index to traverse in all four directions

        int[][] visited = new int[rows][cols]; // Matrix to keep track of visited cells

        // Process first row and last row
        for (int i = 0; i < cols; i++) {
            // Process first row
            if (board[0][i] == 'O' && visited[0][i] == 0) {
                dfs(0, i, board, visited, delRows, delCols); // Call the dfs method to explore the connected region of 'O's
            }
            // Process last row
            if (board[rows - 1][i] == 'O' && visited[rows - 1][i] == 0) {
                dfs(rows - 1, i, board, visited, delRows, delCols); // Call the dfs method to explore the connected region of 'O's
            }
        }

        // Process first and last column
        for (int i = 0; i < rows; i++) {
            // Process first column
            if (board[i][0] == 'O' && visited[i][0] == 0) {
                dfs(i, 0, board, visited, delRows, delCols); // Call the dfs method to explore the connected region of 'O's
            }
            // Process last column
            if (board[i][cols - 1] == 'O' && visited[i][cols - 1] == 0) {
                dfs(i, cols - 1, board, visited, delRows, delCols); // Call the dfs method to explore the connected region of 'O's
            }
        }

        // Convert surrounded 'O's to 'X's
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 'O' && visited[i][j] == 0) {
                    board[i][j] = 'X'; // Mark the surrounded 'O's as 'X'
                }
            }
        }
    }

    // Depth First Search (DFS) method to explore the connected region of 'O's
    private static void dfs(int row, int col, char[][] board, int[][] visited, int[] delRows, int[] delCols) {
        visited[row][col] = 1; // Mark the current cell as visited

        int rows = board.length;
        int cols = board[0].length;

        // Explore all four directions
        for (int i = 0; i < 4; i++) {
            int newRow = row + delRows[i];
            int newCol = col + delCols[i];

            // Check if the new cell is within bounds and is an unvisited 'O'
            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols
                    && board[newRow][newCol] == 'O' && visited[newRow][newCol] == 0) {
                dfs(newRow, newCol, board, visited, delRows, delCols); // Recursively call dfs for the new cell
            }
        }
    }



}

