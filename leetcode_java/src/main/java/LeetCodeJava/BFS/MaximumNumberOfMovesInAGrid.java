package LeetCodeJava.BFS;

// https://leetcode.com/problems/maximum-number-of-moves-in-a-grid/description/

import java.util.*;

/**
 * 2684. Maximum Number of Moves in a Grid
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed m x n matrix grid consisting of positive integers.
 *
 * You can start at any cell in the first column of the matrix, and traverse the grid in the following way:
 *
 * From a cell (row, col), you can move to any of the cells: (row - 1, col + 1), (row, col + 1) and (row + 1, col + 1) such that the value of the cell you move to, should be strictly bigger than the value of the current cell.
 * Return the maximum number of moves that you can perform.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid = [[2,4,3,5],[5,4,9,3],[3,4,2,11],[10,9,13,15]]
 * Output: 3
 * Explanation: We can start at the cell (0, 0) and make the following moves:
 * - (0, 0) -> (0, 1).
 * - (0, 1) -> (1, 2).
 * - (1, 2) -> (2, 3).
 * It can be shown that it is the maximum number of moves that can be made.
 * Example 2:
 *
 *
 * Input: grid = [[3,2,4],[2,1,9],[1,1,7]]
 * Output: 0
 * Explanation: Starting from any cell in the first column we cannot perform any moves.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 2 <= m, n <= 1000
 * 4 <= m * n <= 105
 * 1 <= grid[i][j] <= 106
 */
public class MaximumNumberOfMovesInAGrid {

    /**
     *  NOTE !!!
     *
     *  1. CAN'T use `Dijkstra` for this problem (reason as below)
     *  2. should use
     *      - DFS, BFS or DP
     *
     *  --------
     *
     *  The logic you're attempting to use (Dijkstra's with a Priority Queue)
     *  is INCORRECT for this problem because you are trying to find the
     *  maximum number of steps (moves), NOT the minimum cost path.
     *
     *  Since the problem has an inherent direction
     *  ( always moving to the next column (j -> j+1) ),
     *
     *  -> it simplifies to a Dynamic Programming (DP)
     *  problem or a straightforward column-by-column Breadth-First Search (BFS).
     *
     */


    // V0
//    public int maxMoves(int[][] grid) {
//
//    }


   // V0-0-1
   // IDEA: BFS + NOT VISITED (fixed by gemini)
    public int maxMoves_0_0_1(int[][] grid) {
        // Edge cases
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int R = grid.length;
        int C = grid[0].length;

        // The problem is about finding the maximum number of steps (max column index reached - 1).

        // Define moves: (row, col) delta.
        // Moves are: (r-1, c+1), (r, c+1), (r+1, c+1)
        /**
         *  NOTE !!!
         *
         *   -> format should be (y,x),
         *   since in the grid, we use [y,x] as the coordination notation as well.
         *
         *   It makes implementation simpler by using the same format
         *
         */
        int[][] moves = new int[][]{ {-1, 1}, {0, 1}, {1, 1} };

        // Queue stores the coordinates of reachable cells: [row, col]
        // We don't need to store moveCnt in the queue element, as the loop structure handles it.
        Queue<int[]> q = new LinkedList<>();

        /** NOTE !!!
         *
         *  we need `visited` to NOT revisit the already-visited grids
         */
        /** NOTE !!!
         *
         *  why `visited` NOT affect the visiting from the other (x,y)
         *
         *  -> in this problem, we can only move as below:
         *       - (row - 1, col + 1), (row, col + 1) and (row + 1, col + 1)
         *
         *     -> That means:
         * 	     •	BFS moves strictly rightward (never left).
         * 	     •	Each “layer” represents a specific column index.
         *
         *
         *  ----
         *
         *  visited Optimization:
         *     Added a boolean[][] visited array.
         *     Since we only care about the existence of a path to a cell
         *     and the moves are unit-cost (1 step per column),
         *     once a cell is reached, we don't need to check it again.
         *     This prevents redundant exploration and ensures O(L * W) complexity.
         *
         *
         * 	 And the reason we use `visited` here is:
         * 	    - make sure there's No backtracking or cycles
         *
         */
        // Visited array: Tracks cells that have been reached at least once.
        // This is a crucial optimization in this non-weighted graph.
        boolean[][] visited = new boolean[R][C];

        // 1. Initialize BFS: Add all cells in the first column
        for (int r = 0; r < R; r++) {
            q.add(new int[]{r, 0});
            visited[r][0] = true;
        }

        int maxMoves = 0; // Represents the max column index reached (C-1) - 0.

        // 2. BFS Traversal
        while (!q.isEmpty()) {

            // Loop through the current layer (all cells at the same column index)
            int size = q.size();
            for (int i = 0; i < size; i++) {
                int[] cur = q.poll();
                int r = cur[0];
                int c = cur[1];

                // Check the max move count (which is c) at the moment
                // The number of moves is the column index 'c'.
                maxMoves = Math.max(maxMoves, c);

                // Try all three possible next moves
                for (int[] move : moves) {
                    int nextR = r + move[0];
                    int nextC = c + move[1];

                    // Validate boundary
                    if (nextR >= 0 && nextR < R && nextC >= 0 && nextC < C) {

                        // Check 1: Move condition (Strictly greater value)
                        if (grid[nextR][nextC] > grid[r][c]) {

                            // Check 2: Optimization (If not visited yet)
                            if (!visited[nextR][nextC]) {
                                visited[nextR][nextC] = true;
                                q.add(new int[]{nextR, nextC});
                            }
                        }
                    }
                }
            }
        }

        // Since maxMoves stores the max column index reached (0-indexed),
        // the number of moves is simply maxMoves.
        return maxMoves;
    }

    // V0-0-2
    // IDEA: DFS (fixed by gpt)
    // TLE error -> should use `DFS + MEMORIZATION` (as below)
    int maxMoveCnt = 0;
    public int maxMoves_0_0_2(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        for (int row = 0; row < m; row++) {
            dfs(grid, row, 0, 0);
        }

        return maxMoveCnt;
    }

    private void dfs(int[][] grid, int row, int col, int moves) {
        int m = grid.length;
        int n = grid[0].length;

        maxMoveCnt = Math.max(maxMoveCnt, moves);

        // possible moves
        int[][] dirs = {{-1, 1}, {0, 1}, {1, 1}};

        for (int[] d : dirs) {
            int nr = row + d[0];
            int nc = col + d[1];
            if (nr >= 0 && nr < m && nc < n && grid[nr][nc] > grid[row][col]) {
                dfs(grid, nr, nc, moves + 1);
            }
        }
    }

    // V0-0-3
    // IDEA: DFS + MEMORIZATION (fixed by gpt)
    public int maxMoves(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        /** NOTE !!!
         *
         *  the memorization for optimization
         */
        int[][] memo = new int[m][n];
        int res = 0;

        for (int row = 0; row < m; row++) {
            res = Math.max(res, dfs(grid, row, 0, memo));
        }

        return res;
    }

    private int dfs(int[][] grid, int row, int col, int[][] memo) {

        // Already computed
        /** NOTE !!!
         *
         *  the memorization for optimization
         */
        if (memo[row][col] != 0) return memo[row][col];

        int m = grid.length, n = grid[0].length;
        int best = 0;
        int[][] dirs = {{-1, 1}, {0, 1}, {1, 1}};

        for (int[] d : dirs) {
            int nr = row + d[0];
            int nc = col + d[1];
            if (nr >= 0 && nr < m && nc < n && grid[nr][nc] > grid[row][col]) {
                best = Math.max(best, 1 + dfs(grid, nr, nc, memo));
            }
        }

        memo[row][col] = best;
        return best;
    }

    // V0-4
    // IDEA: DP (gemini)
    // https://buildmoat.teachable.com/courses/7a7af3/lectures/63789835
    /**
     *  IDEA:
     *
     *
     *   e.g.:
     *
     *    We through grids, check `layer by layer`
     *
     *    step 1)
     *       mark all grid[y][0] as 1 (can visit)
     *    Step 2)
     *       for each grid[y][0], check if there next moves
     *          - grid[y-1][x]
     *          - grid[y][x-1]
     *          - grid[y+1][x+1]
     *
     *          can satisfy the rules:
     *              - can visit
     *              - prev grid val < cur grid val
     *
     *        if yes, mark grid[new_y][new_x] as can visit
     *        then we repeat step 2) for all of the new grid[new_y][new_x]
     *
     *          ....
     *
     *     step 3)  get the `valid` (x,y) with max (row idx + col idx)
     *              , then return as result

     *  ----
     *
     *  The text in the screenshot describes the steps for solving
     *  a dynamic programming problem on a grid, specifically corresponding
     *  to the logic of LeetCode 2684, **Maximum Number of Moves in a Grid**.
     *
     * Here is the translation of the Chinese text:
     *
     * ---
     *
     * ## 📝 English Translation of the Steps
     *
     * * You can create an array the same size as the grid to record
     *   whether each cell can be **reached**.
     *
     * * If a cell is in **column 0**, then the cell is reachable.
     *
     * * If a cell is at **(row, column)**,
     *    then check **(row-1, column-1), (row, column-1), (row+1, column-1)**.
     *    The cell is marked as reachable if **ALL** of the following conditions are met
     *    (for at least one of the previous cells):
     *       * The checked cell (the previous cell) is **reachable**.
     *       * The value of the checked cell (the previous cell) is **strictly less than** `grid[row][column]`.
     */
    /**
     * Calculates the maximum number of moves possible starting from any cell in the first column.
     * The move must be to the right, right-up, or right-down to a cell with a strictly greater value.
     * This implementation uses Dynamic Programming.
     * * @param grid The input grid.
     * @return The maximum number of moves.
     */
    public int maxMoves_0_0_4(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int R = grid.length; // Number of rows
        int C = grid[0].length; // Number of columns

        // DP table: visited[r][c] stores a value indicating reachability from column 0.
        // We will use 1 for reachable, 0 for unreachable.
        int[][] visited = new int[R][C];

        // Step 1: Initialize the first column (col 0) as reachable (visited[r][0] = 1).
        for (int r = 0; r < R; r++) {
            visited[r][0] = 1;
        }

        int maxMoves = 0;

        // Step 2: Iterate through columns from the second column (c=1) onwards.
        // NOTE: The C++ code's outer loop uses 'i' for column index (c) and 'j' for row index (r).
        // C++: for (int i = 1; i < grid[0].size(); i++) // i is column (c)
        // C++:   for (int j = 0; j < grid.size(); j++) // j is row (r)

        // i: current column index (c), from 1 to C-1
        for (int c = 1; c < C; c++) {
            // j: current row index (r), from 0 to R-1
            for (int r = 0; r < R; r++) {

                // k iterates over the relative row changes {-1, 0, 1}
                for (int k = -1; k <= 1; k++) {

                    // The C++ logic:
                    // ty = i - 1  -> previous column index (c - 1)
                    // tx = j + k  -> previous row index (r + k)

                    int prev_c = c - 1;
                    int prev_r = r + k;

                    // Check bounds for the previous cell (prev_r, prev_c)
                    // NOTE: The C++ code's bounds check is confusing due to mixed row/column names.
                    // The translation below uses standard Java indexing (row, col) = (r, c).

                    // C++ check: if (ty >= 0 && ty < grid[0].size() && tx >= 0 && tx < grid.size()
                    // Translation:

                    if (prev_r >= 0 && prev_r < R) { // Check row bounds (prev_r)

                        // Check value and reachability condition:
                        // C++ check: grid[tx][ty] < grid[j][i] && visited[tx][ty]
                        // Translation: grid[prev_r][prev_c] < grid[r][c] AND visited[prev_r][prev_c] == 1

                        if (grid[prev_r][prev_c] < grid[r][c] && visited[prev_r][prev_c] == 1) {

                            // If we can reach the current cell (r, c) from the previous column,
                            // mark it as reachable (visited[r][c] = 1).
                            // C++: visited[j][i] = 1;
                            visited[r][c] = 1;

                            // Optimization: Once the cell is marked as reachable,
                            // we can break the inner loop (k) since we only care if it's reachable, not how.
                            break;
                        }
                    }
                }

                // If the current cell is reachable, update maxMoves.
                if (visited[r][c] == 1) {
                    // maxMoves is simply the current column index (c) because the number of moves is C - 1.
                    maxMoves = Math.max(maxMoves, c);
                }
            }
        }

        // The maximum number of moves is the highest column index reached.
        return maxMoves;
    }

    // V0-1
    // IDEA: DFS + MEMORIZATION (fixed by gpt)
    private int[][] grid;
    private int[][] dp;
    private int rows, cols;
    private int[][] directions = { { -1, 1 }, { 0, 1 }, { 1, 1 } }; // right-up, right, right-down

    public int maxMoves_0_1(int[][] grid) {
        this.grid = grid;
        this.rows = grid.length;
        this.cols = grid[0].length;
        this.dp = new int[rows][cols];

        // initialize dp with -1 (unvisited)
        for (int i = 0; i < rows; i++) {
            Arrays.fill(dp[i], -1);
        }

        int maxMove = 0;
        // can start at ANY cell in the first column
        for (int r = 0; r < rows; r++) {
            maxMove = Math.max(maxMove, dfs(r, 0));
        }

        return maxMove;
    }

    private int dfs(int r, int c) {
        if (c == cols - 1)
            return 0; // last column, no more moves
        if (dp[r][c] != -1)
            return dp[r][c];

        int maxNext = 0;
        for (int[] d : directions) {
            int nr = r + d[0];
            int nc = c + d[1];
            if (nr >= 0 && nr < rows && nc < cols && grid[nr][nc] > grid[r][c]) {
                maxNext = Math.max(maxNext, 1 + dfs(nr, nc));
            }
        }

        dp[r][c] = maxNext;
        return maxNext;
    }

    // V0-2
    // IDEA: BFS (gpt)
    public int maxMoves_0_2(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // directions: right-up, right, right-down
        int[][] directions = {{-1, 1}, {0, 1}, {1, 1}};

        // dp[r][c] = max moves to reach cell (r,c)
        int[][] dp = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            dp[r][0] = 0; // start from first column
        }

        int maxMove = 0;

        // BFS column by column
        for (int c = 0; c < cols - 1; c++) {
            for (int r = 0; r < rows; r++) {
                if (dp[r][c] == 0 && c != 0) continue; // skip unreachable cells

                for (int[] d : directions) {
                    int nr = r + d[0];
                    int nc = c + d[1];
                    if (nr >= 0 && nr < rows && nc < cols && grid[nr][nc] > grid[r][c]) {
                        dp[nr][nc] = Math.max(dp[nr][nc], dp[r][c] + 1);
                        maxMove = Math.max(maxMove, dp[nr][nc]);
                    }
                }
            }
        }

        return maxMove;
    }


    // V0-3
    // IDEA: DP (gemini)
    public int maxMoves_0_3(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int R = grid.length;
        int C = grid[0].length;

        // dp[r][c] stores the maximum moves to reach cell (r, c).
        // Initialized to 0. A move count of 0 means the cell is unreachable.
        // We use -1 to clearly mark unreachable cells and avoid confusion with 0 moves.
        int[][] dp = new int[R][C];
        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }

        // 1. Initialization: The first column is reachable with 0 moves.
        for (int r = 0; r < R; r++) {
            dp[r][0] = 0;
        }

        int maxMoves = 0;

        // 2. DP Traversal: Iterate column by column from c=1 to C-1
        for (int c = 1; c < C; c++) {
            for (int r = 0; r < R; r++) {

                int maxPrevMoves = -1;

                // Check all three possible incoming neighbors in column c-1:
                // (r-1, c-1), (r, c-1), (r+1, c-1)

                // Direction array for r: [-1, 0, 1]
                for (int dr = -1; dr <= 1; dr++) {
                    int prevR = r + dr;
                    int prevC = c - 1;

                    // Check boundaries and reachability
                    if (prevR >= 0 && prevR < R && dp[prevR][prevC] != -1) {

                        // Check move condition: grid[prevR][prevC] < grid[r][c]
                        if (grid[prevR][prevC] < grid[r][c]) {
                            // The maximum moves to reach the current cell (r, c)
                            // is 1 + max moves to reach any valid predecessor.
                            maxPrevMoves = Math.max(maxPrevMoves, dp[prevR][prevC]);
                        }
                    }
                }

                // If maxPrevMoves is not -1, it means (r, c) is reachable.
                if (maxPrevMoves != -1) {
                    dp[r][c] = maxPrevMoves + 1;
                    maxMoves = Math.max(maxMoves, dp[r][c]);
                }
            }
        }

        return maxMoves;
    }



    // TODO: validate below
    // V0-1
    // IDEA 3) dijkstra ???
//    public int maxMoves(int[][] grid) {
//        // edge
//        if(grid == null || grid.length == 0 || grid[0].length == 0){
//            return 0;
//        }
//        if(grid[0].length == 1 || grid.length == 1){
//            return 1; // ???
//        }
//
//
//        int maxMove = 0;
//
//        // PQ: {x, y, cost, moves}
//        PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
//            @Override
//            public int compare(Integer[] o1, Integer[] o2) {
//                int diff = o1[2] - o2[2];
//                return diff;
//            }
//        });
//
//        int l = grid.length;
//        int w = grid[0].length;
//
//        // ???
//        //  can start at ANY cell in the FIRST column,
//        for(int i = 0; i < l; i++){
//            int x = 0;
//            int y = i;
//            // init cost = 0
//            // init move = 0
//            pq.add(new Integer[]{x, y, 0, 0});
//        }
//
//        // 2D array record cost by (x, y) ???
//        // int init as 0 // /????
//        int[][] costStaus = new int[l][w]; // ???
//        for(int i = 0; i < l; i++){
//            for(int j = 0; j < w; j++){
//                // ?? init as max val
//                // so we know that which grid (x,y)
//                // is NOT visited yet
//                costStaus[i][j] = Integer.MAX_VALUE; // ?????
//            }
//        }
//
//        // (row - 1, col + 1), (row, col + 1) and (row + 1, col + 1)
//        int[][] moves = new int[][]{ {-1,1}, {0,1}, {1,1} };
//
//        // ?? dijkstra: BFS + PQ + cost status update ???
//        while (!pq.isEmpty()){
//            // edge: if even CAN'T move from 1st iteration ??
//
//            Integer[] cur = pq.poll();
//            int x = cur[0];
//            int y = cur[1];
//            int cost = cur[2];
//            int movesCnt = cur[3];
//
//            // ???
//            maxMove = Math.max(maxMove, movesCnt);
//
//            for(int[] m: moves){
//                int x_ = x + m[1];
//                int y_ = y + m[0];
//                int cost_ = cost + m[2]; // ???
//                // check 1) if still in grid boundary
//                if(x_ >= 0 && x_ < w && y_ >= 0 && y < l){
//                    // check 2) if next val is bigger than prev val
//                    if(grid[y_][x_] > grid[y][x]){
//
//                        //  check 3) check if `cost if less the cur status` // ???
//                        if(grid[y_][x_] > grid[y][x] + cost_){
//                            // add to PQ
//                            pq.add(new Integer[]{x_, y_, cost_, movesCnt + 1});
//
//                            // update cost status ???
//                            // relation ???
//                            grid[y_][x_] = cost_; // ???
//                        }
//                    }
//                }
//            }
//        }
//
//        return maxMove;
//    }


    // V1-1
    // IDEA: BFS
    // https://leetcode.com/problems/maximum-number-of-moves-in-a-grid/editorial/
    // The three possible directions for the next column.
    private final int[] dirs = { -1, 0, 1 };
    public int maxMoves_1_1(int[][] grid) {
        int M = grid.length, N = grid[0].length;

        Queue<int[]> q = new LinkedList<>();
        boolean[][] vis = new boolean[M][N];

        // Enqueue the cells in the first column.
        for (int i = 0; i < M; i++) {
            vis[i][0] = true;
            q.offer(new int[] { i, 0, 0 });
        }

        int maxMoves = 0;
        while (!q.isEmpty()) {
            int sz = q.size();

            while (sz-- > 0) {
                int[] v = q.poll();

                // Current cell with the number of moves made so far.
                int row = v[0], col = v[1], count = v[2];

                maxMoves = Math.max(maxMoves, count);

                for (int dir : dirs) {
                    // Next cell after the move.
                    int newRow = row + dir, newCol = col + 1;

                    // If the next cell isn't visited yet and is greater than
                    // the current cell value, add it to the queue with the
                    // incremented move count.
                    if (
                            newRow >= 0 &&
                                    newCol >= 0 &&
                                    newRow < M &&
                                    newCol < N &&
                                    !vis[newRow][newCol] &&
                                    grid[row][col] < grid[newRow][newCol]
                    ) {
                        vis[newRow][newCol] = true;
                        q.offer(new int[] { newRow, newCol, count + 1 });
                    }
                }
            }
        }

        return maxMoves;
    }


    // V1-2
    // IDEA:  Top-Down Dynamic Programming
    // https://leetcode.com/problems/maximum-number-of-moves-in-a-grid/editorial/

    // The three possible directions for the next column.
    //private final int[] dirs = { -1, 0, 1 };
    private int DFS(int row, int col, int[][] grid, int[][] dp) {
        int M = grid.length, N = grid[0].length;

        // If we have calculated the moves required for this cell, return the answer and skip the recursion.
        if (dp[row][col] != -1) {
            return dp[row][col];
        }

        int maxMoves = 0;
        for (int dir : dirs) {
            // Next cell after the move.
            int newRow = row + dir, newCol = col + 1;

            // If the next cell is valid and greater than the current cell value,
            // perform recursion to that cell with updated value of moves.
            if (
                    newRow >= 0 &&
                            newCol >= 0 &&
                            newRow < M &&
                            newCol < N &&
                            grid[row][col] < grid[newRow][newCol]
            ) {
                maxMoves = Math.max(
                        maxMoves,
                        1 + DFS(newRow, newCol, grid, dp)
                );
            }
        }

        dp[row][col] = maxMoves;
        return maxMoves;
    }

    public int maxMoves_1_2(int[][] grid) {
        int M = grid.length, N = grid[0].length;

        int[][] dp = new int[M][N];
        for (int i = 0; i < M; i++) {
            Arrays.fill(dp[i], -1);
        }

        int maxMoves = 0;
        // Start DFS from each cell in the first column.
        for (int i = 0; i < M; i++) {
            int movesRequired = DFS(i, 0, grid, dp);
            maxMoves = Math.max(maxMoves, movesRequired);
        }

        return maxMoves;
    }


    // V1-3
    // IDEA:  Bottom-up Dynamic Programming
    // https://leetcode.com/problems/maximum-number-of-moves-in-a-grid/editorial/
    public int maxMoves_1_3(int[][] grid) {
        int M = grid.length, N = grid[0].length;

        int[][] dp = new int[M][N];
        // Cells in the first column will have the moves as 1.
        // This is required to ensure we have a way if the cell is reachable or not
        // from the first column.
        for (int i = 0; i < M; i++) {
            dp[i][0] = 1;
        }

        int maxMoves = 0;
        for (int j = 1; j < N; j++) {
            for (int i = 0; i < M; i++) {
                // Check all the three next possible cells
                // Check if the next cell is greater than the previous one
                // Check if the previous cell was reachable,
                // if the value is > 0
                if (grid[i][j] > grid[i][j - 1] && dp[i][j - 1] > 0) {
                    dp[i][j] = Math.max(dp[i][j], dp[i][j - 1] + 1);
                }
                if (
                        i - 1 >= 0 &&
                                grid[i][j] > grid[i - 1][j - 1] &&
                                dp[i - 1][j - 1] > 0
                ) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - 1] + 1);
                }
                if (
                        i + 1 < M &&
                                grid[i][j] > grid[i + 1][j - 1] &&
                                dp[i + 1][j - 1] > 0
                ) {
                    dp[i][j] = Math.max(dp[i][j], dp[i + 1][j - 1] + 1);
                }

                maxMoves = Math.max(maxMoves, dp[i][j] - 1);
            }
        }

        return maxMoves;
    }


    // V1-4
    // IDEA: Space-Optimized Bottom-up Dynamic Programming
    // https://leetcode.com/problems/maximum-number-of-moves-in-a-grid/editorial/
    public int maxMoves_1_4(int[][] grid) {
        int M = grid.length, N = grid[0].length;

        // Create a dp array to store moves, with each cell having a size of 2.
        int[][] dp = new int[M][2];

        // Initialize the first column cells as reachable.
        for (int i = 0; i < M; i++) {
            dp[i][0] = 1;
        }

        int maxMoves = 0;

        // Iterate over each column starting from the second one.
        for (int j = 1; j < N; j++) {
            for (int i = 0; i < M; i++) {
                // Check if moving from the same row
                // of the previous column is possible.
                if (grid[i][j] > grid[i][j - 1] && dp[i][0] > 0) {
                    dp[i][1] = Math.max(dp[i][1], dp[i][0] + 1);
                }
                // Check if moving from the upper diagonal is possible.
                if (i - 1 >= 0 &&
                        grid[i][j] > grid[i - 1][j - 1] &&
                        dp[i - 1][0] > 0) {
                    dp[i][1] = Math.max(dp[i][1], dp[i - 1][0] + 1);
                }
                // Check if moving from the lower diagonal is possible.
                if (i + 1 < M &&
                        grid[i][j] > grid[i + 1][j - 1] &&
                        dp[i + 1][0] > 0) {
                    dp[i][1] = Math.max(dp[i][1], dp[i + 1][0] + 1);
                }

                // Update the maximum moves so far.
                maxMoves = Math.max(maxMoves, dp[i][1] - 1);
            }

            // Shift dp values for the next iteration.
            for (int k = 0; k < M; k++) {
                dp[k][0] = dp[k][1];
                dp[k][1] = 0;
            }
        }

        return maxMoves;
    }



}
