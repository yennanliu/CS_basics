package LeetCodeJava.DFS;

import java.util.*;

// https://leetcode.com/problems/pacific-atlantic-water-flow/description/
/**
 * 417. Pacific Atlantic Water Flow
 * Solved
 * Medium
 * Topics
 * Companies
 * There is an m x n rectangular island that borders both the Pacific Ocean and Atlantic Ocean. The Pacific Ocean touches the island's left and top edges, and the Atlantic Ocean touches the island's right and bottom edges.
 *
 * The island is partitioned into a grid of square cells. You are given an m x n integer matrix heights where heights[r][c] represents the height above sea level of the cell at coordinate (r, c).
 *
 * The island receives a lot of rain, and the rain water can flow to neighboring cells directly north, south, east, and west if the neighboring cell's height is less than or equal to the current cell's height. Water can flow from any cell adjacent to an ocean into the ocean.
 *
 * Return a 2D list of grid coordinates result where result[i] = [ri, ci] denotes that rain water can flow from cell (ri, ci) to both the Pacific and Atlantic oceans.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: heights = [[1,2,2,3,5],[3,2,3,4,4],[2,4,5,3,1],[6,7,1,4,5],[5,1,1,2,4]]
 * Output: [[0,4],[1,3],[1,4],[2,2],[3,0],[3,1],[4,0]]
 * Explanation: The following cells can flow to the Pacific and Atlantic oceans, as shown below:
 * [0,4]: [0,4] -> Pacific Ocean
 *        [0,4] -> Atlantic Ocean
 * [1,3]: [1,3] -> [0,3] -> Pacific Ocean
 *        [1,3] -> [1,4] -> Atlantic Ocean
 * [1,4]: [1,4] -> [1,3] -> [0,3] -> Pacific Ocean
 *        [1,4] -> Atlantic Ocean
 * [2,2]: [2,2] -> [1,2] -> [0,2] -> Pacific Ocean
 *        [2,2] -> [2,3] -> [2,4] -> Atlantic Ocean
 * [3,0]: [3,0] -> Pacific Ocean
 *        [3,0] -> [4,0] -> Atlantic Ocean
 * [3,1]: [3,1] -> [3,0] -> Pacific Ocean
 *        [3,1] -> [4,1] -> Atlantic Ocean
 * [4,0]: [4,0] -> Pacific Ocean
 *        [4,0] -> Atlantic Ocean
 * Note that there are other possible paths for these cells to flow to the Pacific and Atlantic oceans.
 * Example 2:
 *
 * Input: heights = [[1]]
 * Output: [[0,0]]
 * Explanation: The water can flow from the only cell to the Pacific and Atlantic oceans.
 *
 *
 * Constraints:
 *
 * m == heights.length
 * n == heights[r].length
 * 1 <= m, n <= 200
 * 0 <= heights[r][c] <= 105
 *
 */
public class PacificAtlanticWaterFlow {

    // V0
    // IDEA : DFS (fixed by GPT)
    // https://www.youtube.com/watch?v=s-VkcjHqkGI
    public List<List<Integer>> pacificAtlantic(int[][] heights) {

        if (heights == null || heights.length == 0 || heights[0].length == 0) {
            return new ArrayList<>();
        }

        int l = heights.length;
        int w = heights[0].length;

        /**
         *
         * The pacificReachable and atlanticReachable arrays are used to keep track
         * of which cells in the matrix can reach the Pacific and Atlantic oceans, respectively.
         *
         *
         * - pacificReachable[i][j] will be true if water
         *   can flow from cell (i, j) to the Pacific Ocean.
         *   The Pacific Ocean is on the top and left edges of the matrix.
         *
         * - atlanticReachable[i][j] will be true if water
         *   can flow from cell (i, j) to the Atlantic Ocean.
         *   The Atlantic Ocean is on the bottom and right edges of the matrix.
         *
         *
         * NOTE !!!!
         *
         * The pacificReachable and atlanticReachable arrays serve a dual purpose:
         *
         * Tracking Reachability: They track whether each cell can reach the respective ocean.
         *
         * Tracking Visited Cells: They also help in tracking whether a cell has already
         *                         been visited during the depth-first search (DFS)
         *                         to prevent redundant work and infinite loops.
         *
         *
         *   NOTE !!!
         *
         *    we use `boolean[][]` to track if a cell is reachable
         */
        boolean[][] pacificReachable = new boolean[l][w];
        boolean[][] atlanticReachable = new boolean[l][w];

        // check on x-axis
        /**
         *  NOTE !!!
         *
         *   we loop EVERY `cell` at x-axis  ( (x_1, 0), (x_2, 0), .... (x_1, l - 1), (x_2, l - 1) ... )
         *
         */
        for (int x = 0; x < w; x++) {
            dfs(heights, pacificReachable, 0, x);
            dfs(heights, atlanticReachable, l - 1, x);
        }

        // check on y-axis
        /**
         *  NOTE !!!
         *
         *   we loop EVERY `cell` at y-axis  (  (0, y_1), (0, y_2), .... (w-1, y_1), (w-1, y_2), ... )
         *
         */
        for (int y = 0; y < l; y++) {
            dfs(heights, pacificReachable, y, 0);
            dfs(heights, atlanticReachable, y, w - 1);
        }

        List<List<Integer>> commonCells = new ArrayList<>();
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < w; j++) {
                if (pacificReachable[i][j] && atlanticReachable[i][j]) {
                    commonCells.add(Arrays.asList(i, j));
                }
            }
        }
        return commonCells;
    }

    /**
     *  NOTE !!!
     *
     *   this dfs func return NOTHING,
     *   e.g. it updates the matrix value `in place`
     *
     *   example:  we pass `pacificReachable` as param to dfs,
     *             it modifies values in pacificReachable in place,
     *             but NOT return pacificReachable as response
     */
    private void dfs(int[][] heights, boolean[][] reachable, int y, int x) {

        int l = heights.length;
        int w = heights[0].length;

        reachable[y][x] = true;

        int[][] directions = new int[][]{{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] dir : directions) {
            int newY = y + dir[0];
            int newX = x + dir[1];

            /**
             *  NOTE !!!  only meet below conditions, then do recursion call
             *
             *  1. newX, newY still in range
             *  2. newX, newY is still not reachable (!reachable[newY][newX])
             *  3. heights[newY][newX] >= heights[y][x]
             *
             *
             *  NOTE !!!
             *
             *  The condition !reachable[newY][newX] in the dfs function
             *  ensures that each cell is only processed once
             *
             *  1. Avoid Infinite Loops
             *  2. Efficiency
             *  3. Correctness
             *
             *
             *  NOTE !!! "inverse" comparison
             *
             *  we use the "inverse" comparison, e.g.  heights[newY][newX] >= heights[y][x]
             *  so we start from "cur point" (heights[y][x]), and compare with "next point" (heights[newY][newX])
             *  if "next point" is "higher" than "cur point"  (e.g. heights[newY][newX] >= heights[y][x])
             *  -> then means water at "next point" can flow to "cur point"
             *  -> then we keep track back to next point of then "next point"
             *  -> repeat ...
             */
            if (newY >= 0 && newY < l && newX >= 0 && newX < w && !reachable[newY][newX] && heights[newY][newX] >= heights[y][x]) {
                dfs(heights, reachable, newY, newX);
            }
        }
    }

    // V1-1
    // https://neetcode.io/problems/pacific-atlantic-water-flow
    // IDEA: BRUTE FORCE (BACKTRACK)
    int ROWS, COLS;
    boolean pacific, atlantic;
    int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    public List<List<Integer>> pacificAtlantic_1_1(int[][] heights) {
        ROWS = heights.length;
        COLS = heights[0].length;
        List<List<Integer>> res = new ArrayList<>();

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                pacific = false;
                atlantic = false;
                dfs(heights, r, c, Integer.MAX_VALUE);
                if (pacific && atlantic) {
                    res.add(Arrays.asList(r, c));
                }
            }
        }
        return res;
    }

    private void dfs(int[][] heights, int r, int c, int prevVal) {
        if (r < 0 || c < 0) {
            pacific = true;
            return;
        }
        if (r >= ROWS || c >= COLS) {
            atlantic = true;
            return;
        }
        if (heights[r][c] > prevVal) {
            return;
        }

        int tmp = heights[r][c];
        heights[r][c] = Integer.MAX_VALUE;
        for (int[] dir : directions) {
            dfs(heights, r + dir[0], c + dir[1], tmp);
            if (pacific && atlantic) {
                break;
            }
        }
        heights[r][c] = tmp;
    }


    // V1-2
    // https://neetcode.io/problems/pacific-atlantic-water-flow
    // IDEA: DFS
//    private int[][] directions = {{1, 0}, {-1, 0},
//            {0, 1}, {0, -1}};
    public List<List<Integer>> pacificAtlantic_1_2(int[][] heights) {
        int ROWS = heights.length, COLS = heights[0].length;
        boolean[][] pac = new boolean[ROWS][COLS];
        boolean[][] atl = new boolean[ROWS][COLS];

        for (int c = 0; c < COLS; c++) {
            dfs(0, c, pac, heights);
            dfs(ROWS - 1, c, atl, heights);
        }
        for (int r = 0; r < ROWS; r++) {
            dfs(r, 0, pac, heights);
            dfs(r, COLS - 1, atl, heights);
        }

        List<List<Integer>> res = new ArrayList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (pac[r][c] && atl[r][c]) {
                    res.add(Arrays.asList(r, c));
                }
            }
        }
        return res;
    }

    private void dfs(int r, int c, boolean[][] ocean, int[][] heights) {
        ocean[r][c] = true;
        for (int[] d : directions) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nr < heights.length &&
                    nc >= 0 && nc < heights[0].length &&
                    !ocean[nr][nc] && heights[nr][nc] >= heights[r][c]) {
                dfs(nr, nc, ocean, heights);
            }
        }
    }


    // V1-3
    // https://neetcode.io/problems/pacific-atlantic-water-flow
    // IDEA: BFS
    //private int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    public List<List<Integer>> pacificAtlantic_1_3(int[][] heights) {
        int ROWS = heights.length, COLS = heights[0].length;
        boolean[][] pac = new boolean[ROWS][COLS];
        boolean[][] atl = new boolean[ROWS][COLS];

        Queue<int[]> pacQueue = new LinkedList<>();
        Queue<int[]> atlQueue = new LinkedList<>();

        for (int c = 0; c < COLS; c++) {
            pacQueue.add(new int[]{0, c});
            atlQueue.add(new int[]{ROWS - 1, c});
        }
        for (int r = 0; r < ROWS; r++) {
            pacQueue.add(new int[]{r, 0});
            atlQueue.add(new int[]{r, COLS - 1});
        }

        bfs(pacQueue, pac, heights);
        bfs(atlQueue, atl, heights);

        List<List<Integer>> res = new ArrayList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (pac[r][c] && atl[r][c]) {
                    res.add(Arrays.asList(r, c));
                }
            }
        }
        return res;
    }

    private void bfs(Queue<int[]> q, boolean[][] ocean, int[][] heights) {
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int r = cur[0], c = cur[1];
            ocean[r][c] = true;
            for (int[] d : directions) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nr < heights.length && nc >= 0 &&
                        nc < heights[0].length && !ocean[nr][nc] &&
                        heights[nr][nc] >= heights[r][c]) {
                    q.add(new int[]{nr, nc});
                }
            }
        }
    }


    // V2
    // IDEA : DFS
    // https://leetcode.com/problems/pacific-atlantic-water-flow/editorial/
    /** NOTE !!! init directions */
    private static final int[][] DIRECTIONS = new int[][]{{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
    private int numRows;
    private int numCols;
    private int[][] landHeights;

    public List<List<Integer>> pacificAtlantic_2(int[][] matrix) {

        // Check if input is empty
        if (matrix.length == 0 || matrix[0].length == 0) {
            return new ArrayList<>();
        }

        // Save initial values to parameters
        numRows = matrix.length;
        numCols = matrix[0].length;

        /** NOTE !!! cope matrix, for reference below  */
        landHeights = matrix;
        boolean[][] pacificReachable = new boolean[numRows][numCols];
        boolean[][] atlanticReachable = new boolean[numRows][numCols];

        /** NOTE !!! only move 1 direction at once, for avoiding double loop  */
        // Loop through each cell adjacent to the oceans and start a DFS
        for (int i = 0; i < numRows; i++) {
            dfs(i, 0, pacificReachable);
            dfs(i, numCols - 1, atlanticReachable);
        }

        /** NOTE !!! only move 1 direction at once, for avoiding double loop  */
        for (int i = 0; i < numCols; i++) {
            dfs(0, i, pacificReachable);
            dfs(numRows - 1, i, atlanticReachable);
        }

        // Find all cells that can reach both oceans
        List<List<Integer>> commonCells = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (pacificReachable[i][j] && atlanticReachable[i][j]) {
                    //commonCells.add(List.of(i, j));
//                    List<Integer> coord = new ArrayList<>();
//                    coord.add(i);
//                    coord.add(j);
//                    commonCells.add(coord);
                     commonCells.add(Arrays.asList(i, j));
                }
            }
        }
        return commonCells;
    }

    private void dfs(int row, int col, boolean[][] reachable) {

        /** NOTE !!! set visited as true */
        // This cell is reachable, so mark it
        reachable[row][col] = true;
        for (int[] dir : DIRECTIONS) { // Check all 4 directions
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            // Check if new cell is within bounds
            if (newRow < 0 || newRow >= numRows || newCol < 0 || newCol >= numCols) {

                /** NOTE !!! neglect below code and jump to next loop (continue) */
                continue;
            }

            // Check that the new cell hasn't already been visited
            if (reachable[newRow][newCol]) {

                /** NOTE !!! neglect below code and jump to next loop (continue) */
                continue;
            }

            // Check that the new cell has a higher or equal height,
            // So that water can flow from the new cell to the old cell
            if (landHeights[newRow][newCol] < landHeights[row][col]) {

                /** NOTE !!! neglect below code and jump to next loop (continue) */
                continue;
            }

            /** NOTE !!! if can reach here, means this move is "OK" then we do next move via recursion call */
            // If we've gotten this far, that means the new cell is reachable
            dfs(newRow, newCol, reachable);
        }
    }

    // V3
    // IDEA : BFS
    // https://leetcode.com/problems/pacific-atlantic-water-flow/editorial/
    private static final int[][] DIRECTIONS_ = new int[][]{{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
    private int numRows_;
    private int numCols_;
    private int[][] landHeights_;

    public List<List<Integer>> pacificAtlantic_3(int[][] matrix) {
        // Check if input is empty
        if (matrix.length == 0 || matrix[0].length == 0) {
            return new ArrayList<>();
        }

        // Save initial values to parameters
        numRows_ = matrix.length;
        numCols_ = matrix[0].length;
        landHeights_ = matrix;

        // Setup each queue with cells adjacent to their respective ocean
        Queue<int[]> pacificQueue = new LinkedList<>();
        Queue<int[]> atlanticQueue = new LinkedList<>();
        for (int i = 0; i < numRows_; i++) {
            pacificQueue.offer(new int[]{i, 0});
            atlanticQueue.offer(new int[]{i, numCols_ - 1});
        }
        for (int i = 0; i < numCols_; i++) {
            pacificQueue.offer(new int[]{0, i});
            atlanticQueue.offer(new int[]{numRows_ - 1, i});
        }

        // Perform a BFS for each ocean to find all cells accessible by each ocean
        boolean[][] pacificReachable = bfs(pacificQueue);
        boolean[][] atlanticReachable = bfs(atlanticQueue);

        // Find all cells that can reach both oceans
        List<List<Integer>> commonCells = new ArrayList<>();
        for (int i = 0; i < numRows_; i++) {
            for (int j = 0; j < numCols_; j++) {
                if (pacificReachable[i][j] && atlanticReachable[i][j]) {
                    // TODO : fix below
                    //commonCells.add(List.of(i, j));
                    commonCells.add(Arrays.asList(i, j));
                }
            }
        }
        return commonCells;
    }

    private boolean[][] bfs(Queue<int[]> queue) {
        boolean[][] reachable = new boolean[numRows_][numCols_];
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            // This cell is reachable, so mark it
            reachable[cell[0]][cell[1]] = true;
            for (int[] dir : DIRECTIONS_) { // Check all 4 directions
                int newRow = cell[0] + dir[0];
                int newCol = cell[1] + dir[1];
                // Check if new cell is within bounds
                if (newRow < 0 || newRow >= numRows_ || newCol < 0 || newCol >= numCols_) {
                    continue;
                }
                // Check that the new cell hasn't already been visited
                if (reachable[newRow][newCol]) {
                    continue;
                }
                // Check that the new cell has a higher or equal height,
                // So that water can flow from the new cell to the old cell
                if (landHeights_[newRow][newCol] < landHeights_[cell[0]][cell[1]]) {
                    continue;
                }
                // If we've gotten this far, that means the new cell is reachable
                queue.offer(new int[]{newRow, newCol});
            }
        }
        return reachable;
    }

}
