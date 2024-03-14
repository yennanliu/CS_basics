package LeetCodeJava.DFS;

import java.util.*;

public class PacificAtlanticWaterFlow {

    // V0
    // IDEA : DFS
//    private static final int[][] DIRECTIONS_ = new int[][]{{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
//    private int numRows_;
//    private int numCols_;
//    private int[][] landHeights_;
//
//    public List<List<Integer>> pacificAtlantic(int[][] matrix) {
//        if (matrix.length == 0 || matrix[0].length == 0) {
//            return new ArrayList<>();
//        }
//
//        numRows_ = matrix.length;
//        numCols_ = matrix[0].length;
//        landHeights_ = matrix;
//        boolean[][] pacificReachable = new boolean[numRows_][numCols_];
//        boolean[][] atlanticReachable = new boolean[numRows_][numCols_];
//
//        for (int i = 0; i < numRows_; i++) {
//            dfs_(i, 0, pacificReachable);
//            dfs_(i, numCols_ - 1, atlanticReachable);
//        }
//        for (int i = 0; i < numCols_; i++) {
//            dfs_(0, i, pacificReachable);
//            dfs_(numRows_ - 1, i, atlanticReachable);
//        }
//
//        List<List<Integer>> commonCells = new ArrayList<>();
//        for (int i = 0; i < numRows_; i++) {
//            for (int j = 0; j < numCols_; j++) {
//                if (pacificReachable[i][j] && atlanticReachable[i][j]) {
//                    List<Integer> cell = new ArrayList<>();
//                    cell.add(i);
//                    cell.add(j);
//                    commonCells.add(cell);
//                }
//            }
//        }
//        return commonCells;
//    }
//
//    private void dfs_(int row, int col, boolean[][] reachable) {
//        reachable[row][col] = true;
//        for (int[] dir : DIRECTIONS_) {
//            int newRow = row + dir[0];
//            int newCol = col + dir[1];
//            if (newRow < 0 || newRow >= numRows_ || newCol < 0 || newCol >= numCols_ || reachable[newRow][newCol] || landHeights[newRow][newCol] < landHeights[row][col]) {
//                continue;
//            }
//            dfs(newRow, newCol, reachable);
//        }
//    }

    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/pacific-atlantic-water-flow/editorial/


    /** NOTE !!! init directions */
    private static final int[][] DIRECTIONS = new int[][]{{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
    private int numRows;
    private int numCols;
    private int[][] landHeights;

    public List<List<Integer>> pacificAtlantic_1(int[][] matrix) {

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

    // V2
    // IDEA : BFS
    // https://leetcode.com/problems/pacific-atlantic-water-flow/editorial/
    private static final int[][] DIRECTIONS_ = new int[][]{{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
    private int numRows_;
    private int numCols_;
    private int[][] landHeights_;

    public List<List<Integer>> pacificAtlantic_2(int[][] matrix) {
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
