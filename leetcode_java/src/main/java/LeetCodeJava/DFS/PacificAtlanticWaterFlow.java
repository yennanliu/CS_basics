package LeetCodeJava.DFS;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PacificAtlanticWaterFlow {

    // TODO : fix below
//    List<List<Integer>> res = new ArrayList<>();
//    //boolean[][] seen;
//
//    // V0
//    public List<List<Integer>> pacificAtlantic(int[][] heights) {
//
//        // double check ??
//        if (heights.length == 1 && heights[0].length == 1) {
//            List<Integer> _ans = new ArrayList<>();
//            _ans.add(0);
//            _ans.add(0);
//            this.res.add(_ans);
//            return this.res;
//        }
//
//        int len = heights.length;
//        int width = heights[0].length;
//
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < len; y++) {
//
//                boolean[][] seen = new boolean[len][width];
//                String status = _help(heights, x, y, 0, 0, seen, null);
//                System.out.println(">>> status = " + status + " (x,y) = " + x + ", " + y);
//
//                if (status != null && status.contains("a") && status.contains("p")) {
//                    List<Integer> _cur = new ArrayList<>();
//                    _cur.add(x);
//                    _cur.add(y);
//                    this.res.add(_cur);
//                }
//            }
//        }
//
//        return this.res;
//    }
//
//    private String _help(int[][] heights, int x, int y, int lastX, int lastY, boolean[][] seen, String status) {
//
//        int len = heights.length;
//        int width = heights[0].length;
//
//        if (y == 0 || x == 0){
//            status += "-p";
//        }
//
//        if (y == len-1 || x == width-1){
//            status += "-a";
//        }
//
//        seen[y][x] = true;
//
//        if (x >= width || x < 0 || y >= len || y < 0 || seen[y][x] == true || heights[lastY][lastX] < heights[y][x]) {
//            return status;
//        }
//
//        return  _help(heights, x+1, y, x, y, seen, status) +
//                _help(heights, x-1, y, x, y, seen, status)+
//                _help(heights, x, y+1, x, y, seen, status)+
//                _help(heights, x, y-1, x, y, seen, status);
//    }

    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/pacific-atlantic-water-flow/editorial/
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
        landHeights = matrix;
        boolean[][] pacificReachable = new boolean[numRows][numCols];
        boolean[][] atlanticReachable = new boolean[numRows][numCols];

        // Loop through each cell adjacent to the oceans and start a DFS
        for (int i = 0; i < numRows; i++) {
            dfs(i, 0, pacificReachable);
            dfs(i, numCols - 1, atlanticReachable);
        }
        for (int i = 0; i < numCols; i++) {
            dfs(0, i, pacificReachable);
            dfs(numRows - 1, i, atlanticReachable);
        }

        // Find all cells that can reach both oceans
        List<List<Integer>> commonCells = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (pacificReachable[i][j] && atlanticReachable[i][j]) {
                    // TODO : fix below
                    //commonCells.add(List.of(i, j));
                }
            }
        }
        return commonCells;
    }

    private void dfs(int row, int col, boolean[][] reachable) {
        // This cell is reachable, so mark it
        reachable[row][col] = true;
        for (int[] dir : DIRECTIONS) { // Check all 4 directions
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            // Check if new cell is within bounds
            if (newRow < 0 || newRow >= numRows || newCol < 0 || newCol >= numCols) {
                continue;
            }
            // Check that the new cell hasn't already been visited
            if (reachable[newRow][newCol]) {
                continue;
            }
            // Check that the new cell has a higher or equal height,
            // So that water can flow from the new cell to the old cell
            if (landHeights[newRow][newCol] < landHeights[row][col]) {
                continue;
            }
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
