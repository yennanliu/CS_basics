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

    // V0
//    public int maxMoves(int[][] grid) {
//
//    }

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
