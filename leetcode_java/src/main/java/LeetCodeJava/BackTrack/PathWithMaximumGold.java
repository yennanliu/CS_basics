package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/path-with-maximum-gold/description/

import jdk.internal.net.http.common.Pair;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

/**
 * 1219. Path with Maximum Gold
 * Medium
 * Topics
 * Companies
 * Hint
 * In a gold mine grid of size m x n, each cell in this mine has an integer representing the amount of gold in that cell, 0 if it is empty.
 *
 * Return the maximum amount of gold you can collect under the conditions:
 *
 * Every time you are located in a cell you will collect all the gold in that cell.
 * From your position, you can walk one step to the left, right, up, or down.
 * You can't visit the same cell more than once.
 * Never visit a cell with 0 gold.
 * You can start and stop collecting gold from any position in the grid that has some gold.
 *
 *
 * Example 1:
 *
 * Input: grid = [[0,6,0],[5,8,7],[0,9,0]]
 * Output: 24
 * Explanation:
 * [[0,6,0],
 *  [5,8,7],
 *  [0,9,0]]
 * Path to get the maximum gold, 9 -> 8 -> 7.
 * Example 2:
 *
 * Input: grid = [[1,0,7],[2,0,6],[3,4,5],[0,3,0],[9,0,20]]
 * Output: 28
 * Explanation:
 * [[1,0,7],
 *  [2,0,6],
 *  [3,4,5],
 *  [0,3,0],
 *  [9,0,20]]
 * Path to get the maximum gold, 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 15
 * 0 <= grid[i][j] <= 100
 * There are at most 25 cells containing gold.
 *
 */
public class PathWithMaximumGold {

    // V0
    // TODO : implement
//    public int getMaximumGold(int[][] grid) {
//
//    }

    // V1-1
    // IDEA : DFS + BACKTRACK
    //https://leetcode.com/problems/path-with-maximum-gold/editorial/
    private final int[] DIRECTIONS = new int[] { 0, 1, 0, -1, 0 };

    public int getMaximumGold_1_1(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int maxGold = 0;

        // Search for the path with the maximum gold starting from each cell
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                maxGold = Math.max(maxGold, dfsBacktrack(grid, rows, cols, row, col));
            }
        }
        return maxGold;
    }

    private int dfsBacktrack(int[][] grid, int rows, int cols, int row, int col) {
        // Base case: this cell is not in the matrix or this cell has no gold
        if (row < 0 || col < 0 || row == rows || col == cols || grid[row][col] == 0) {
            return 0;
        }
        int maxGold = 0;

        // Mark the cell as visited and save the value
        int originalVal = grid[row][col];
        grid[row][col] = 0;

        // Backtrack in each of the four directions
        /**
         * NOTE !!! trick below
         *
         *  we get max on all possible "next step"
         *
         */
        for (int direction = 0; direction < 4; direction++) {
            maxGold = Math.max(maxGold,
                    dfsBacktrack(grid, rows, cols, DIRECTIONS[direction] + row,
                            DIRECTIONS[direction + 1] + col));
        }

        // Set the cell back to its original value
        grid[row][col] = originalVal;
        return maxGold + originalVal;
    }

    // V1-2
    // IDEA : BFS + BACKTRACK
    // https://leetcode.com/problems/path-with-maximum-gold/editorial/
//    private final int[] DIRECTIONS_2 = new int[] { 0, 1, 0, -1, 0 };
//    public int getMaximumGold_1_2(int[][] grid) {
//        int rows = grid.length;
//        int cols = grid[0].length;
//
//        // Find the total amount of gold in the grid
//        int totalGold = 0;
//        for (int[] row : grid) {
//            for (int cell : row) {
//                totalGold += cell;
//            }
//        }
//
//        // Search for the path with the maximum gold starting from each cell
//        int maxGold = 0;
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                if (grid[row][col] != 0) {
//                    maxGold = Math.max(maxGold, bfsBacktrack_2(grid, rows, cols, row, col));
//                    // If we found a path with the total gold, it's the max gold
//                    if (maxGold == totalGold) {
//                        return totalGold;
//                    }
//                }
//            }
//        }
//        return maxGold;
//    }
//
//    // Helper function to perform BFS
//    private int bfsBacktrack_2(int[][] grid, int rows, int cols, int row, int col) {
//        Queue<Pair<int[], Set<String>>> queue = new ArrayDeque<>();
//        Set<String> visited = new HashSet<>();
//        int maxGold = 0;
//        visited.add(row + "," + col);
//        queue.offer(new Pair<>(new int[] { row, col, grid[row][col] }, visited));
//
//        while (!queue.isEmpty()) {
//            Pair<int[], Set<String>> current = queue.poll();
//            int currRow = current.getKey()[0];
//            int currCol = current.getKey()[1];
//            int currGold = current.getKey()[2];
//            Set<String> currVis = current.getValue();
//            maxGold = Math.max(maxGold, currGold);
//
//            // Search for gold in each of the 4 neighbor cells
//            for (int direction = 0; direction < 4; direction++) {
//                int nextRow = currRow + DIRECTIONS_2[direction];
//                int nextCol = currCol + DIRECTIONS_2[direction + 1];
//
//                // If the next cell is in the matrix, has gold,
//                // and has not been visited, add it to the queue
//                if (nextRow >= 0 && nextRow < rows && nextCol >= 0 && nextCol < cols &&
//                        grid[nextRow][nextCol] != 0 &&
//                        !currVis.contains(nextRow + "," + nextCol)) {
//
//                    currVis.add(nextRow + "," + nextCol);
//                    Set<String> copyVis = new HashSet<>(currVis);
//                    queue.offer(new Pair<>(new int[] { nextRow, nextCol,
//                            currGold + grid[nextRow][nextCol]}, copyVis));
//                    currVis.remove(nextRow + "," + nextCol);
//
//                }
//            }
//        }
//        return maxGold;
//    }


    // V2
    // IDEA : DFS + BACKTRACK
    // https://leetcode.com/problems/path-with-maximum-gold/solutions/5154494/fastest-100-easy-to-understand-clean-concise/
    int[] roww = {1, -1, 0, 0};
    int[] coll = {0, 0, -1, 1};
    int maxGold = 0;

    public int dfs2(int[][] grid, int x, int y, int n, int m) {
        if (x < 0 || x >= n || y < 0 || y >= m || grid[x][y] == 0) return 0;

        int curr = grid[x][y];
        grid[x][y] = 0;
        int localMaxGold = curr;

        for (int i = 0; i < 4; i++) {
            int newX = x + roww[i];
            int newY = y + coll[i];
            localMaxGold = Math.max(localMaxGold, curr + dfs2(grid, newX, newY, n, m));
        }

        grid[x][y] = curr;
        return localMaxGold;
    }

    public int getMaximumGold_2(int[][] grid) {
        int n = grid.length, m = grid[0].length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] != 0) {
                    maxGold = Math.max(maxGold, dfs2(grid, i, j, n, m));
                }
            }
        }

        return maxGold;
    }

    // V3
    // IDEA : DFS + BACKTRACK
    // https://leetcode.com/problems/path-with-maximum-gold/solutions/398345/java-dfs-backtracking/
    int r = 0;
    int c = 0;
    int max = 0;
    public int getMaximumGold_3(int[][] grid) {
        r = grid.length;
        c = grid[0].length;
        for(int i = 0; i < r; i++) {
            for(int j = 0; j < c; j++) {
                if(grid[i][j] != 0) {
                    dfs3(grid, i, j, 0);
                }
            }
        }
        return max;
    }

    private void dfs3(int[][] grid, int i, int j, int cur) {
        if(i < 0 || i >= r || j < 0 || j >= c || grid[i][j] == 0) {
            max = Math.max(max, cur);
            return;
        }
        int val = grid[i][j];
        grid[i][j] = 0;
        dfs3(grid, i + 1, j, cur + val);
        dfs3(grid, i - 1, j, cur + val);
        dfs3(grid, i, j + 1, cur + val);
        dfs3(grid, i, j - 1, cur + val);
        grid[i][j] = val;
    }

    // V4
    // https://leetcode.com/problems/path-with-maximum-gold/solutions/5154481/video-explanation-easy-to-understand-4-languages-dfs-backtracking/
    public int getMaximumGold_4(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int maxGold = 0;

        // Helper function for DFS
        class Helper {
            public int dfs(int row, int col) {
                if (row < 0 || row >= m || col < 0 || col >= n || grid[row][col] == 0) {
                    return 0;
                }

                int currentGold = grid[row][col];
                grid[row][col] = 0; // Mark the cell as visited by setting it to 0

                // Recursively explore all four directions
                int d = dfs(row + 1, col);
                int u = dfs(row - 1, col);
                int r = dfs(row, col + 1);
                int l = dfs(row, col - 1);

                // Restore the cell value
                grid[row][col] = currentGold;

                return currentGold + Math.max(Math.max(d, u), Math.max(r, l));
            }
        }

        Helper helper = new Helper();

        // Iterate over all cells in the grid
        for (int row = 0; row < m; ++row) {
            for (int col = 0; col < n; ++col) {
                if (grid[row][col] > 0) {
                    maxGold = Math.max(maxGold, helper.dfs(row, col));
                }
            }
        }

        return maxGold;
    }

}
