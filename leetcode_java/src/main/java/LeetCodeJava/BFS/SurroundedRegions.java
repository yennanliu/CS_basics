package LeetCodeJava.BFS;

// https://leetcode.com/problems/surrounded-regions/

import java.util.LinkedList;
import java.util.List;

public class SurroundedRegions {

    // V0
    // IDEA : DFS OR BFS
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Breadth-First-Search/surrounded-regions.py

    // TODO : fix below
    // V0'
//    int[][] points = new int[][]{};
//    public void solve(char[][] board) {
//
//        if (board.length == 1 && board[0].length == 1){
//            return;
//        }
//
//        int len = board.length;
//        int width = board[0].length;
//
//        // dfs
//        // make points to "X"
//        List<List<Integer>> tmp_points = _collect(board, 0, 0, new ArrayList<>());
//        if (tmp_points != null && _is_border(tmp_points)){
//            flip(tmp_points, board);
//        }
//        return;
//    }
//
//    private List<List<Integer>> _collect(char[][] board, int x, int y, List<List<Integer>> points){
//
//        int len = board.length;
//        int width = board[0].length;
//
//        if (x < 0 || x >= width || y < 0 || y >= len || board[y][x] == 'X'){
//            return null;
//        }
//
//        if (board[y][x] == 'O'){
//            List<Integer> cur = new ArrayList<>();
//            cur.add(x);
//            cur.add(y);
//            points.add(cur);
//        }
//
//        _collect(board, x+1, y, points);
//        _collect(board, x-1, y, points);
//        _collect(board, x, y+1, points);
//        _collect(board, x, y-1, points);
//
//        return points;
//    }
//
//    private Boolean _is_border(List<List<Integer>> points){
//        for (List<Integer> x : points){
//            if (x.get(0) == 0 || x.get(1) == 0){
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private void flip(List<List<Integer>> points, char[][] board){
//        for (List<Integer> x : points){
//            board[x.get(0)][x.get(1)] = 'X';
//        }
//    }

    // V1
    // IDEA : DFS (fixed by gpt)
    public void solve_1(char[][] board) {
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

    // V1_1
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

    protected Integer ROWS = 0;
    protected Integer COLS = 0;

    public void solve_1_1(char[][] board) {
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


    // V2
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

    public void solve_2(char[][] board) {
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

    // V3
    // IDEA : DFS
    // https://leetcode.com/problems/surrounded-regions/solutions/3805983/java-100-faster-step-by-step-explained/
    public void solve(char[][] board) {
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

