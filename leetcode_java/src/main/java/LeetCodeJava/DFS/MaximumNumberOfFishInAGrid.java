package LeetCodeJava.DFS;

// https://leetcode.com/problems/maximum-number-of-fish-in-a-grid/description/

import java.util.LinkedList;
import java.util.Queue;

/**
 *  2658. Maximum Number of Fish in a Grid
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed 2D matrix grid of size m x n, where (r, c) represents:
 *
 * A land cell if grid[r][c] = 0, or
 * A water cell containing grid[r][c] fish, if grid[r][c] > 0.
 * A fisher can start at any water cell (r, c) and can do the following operations any number of times:
 *
 * Catch all the fish at cell (r, c), or
 * Move to any adjacent water cell.
 * Return the maximum number of fish the fisher can catch if he chooses his starting cell optimally, or 0 if no water cell exists.
 *
 * An adjacent cell of the cell (r, c), is one of the cells (r, c + 1), (r, c - 1), (r + 1, c) or (r - 1, c) if it exists.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid = [[0,2,1,0],[4,0,0,3],[1,0,0,4],[0,3,2,0]]
 * Output: 7
 * Explanation: The fisher can start at cell (1,3) and collect 3 fish, then move to cell (2,3) and collect 4 fish.
 * Example 2:
 *
 *
 * Input: grid = [[1,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,1]]
 * Output: 1
 * Explanation: The fisher can start at cells (0,0) or (3,3) and collect a single fish.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 10
 * 0 <= grid[i][j] <= 10
 *
 */
public class MaximumNumberOfFishInAGrid {

    // V0
    // IDEA: DFS (fixed by gpt)
    public int findMaxFish(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int l = grid.length;
        int w = grid[0].length;
        int maxFish = 0;

        for (int y = 0; y < l; y++) {
            for (int x = 0; x < w; x++) {
                if (grid[y][x] > 0) {
                    maxFish = Math.max(maxFish, getFishCnt(grid, x, y));
                }
            }
        }

        return maxFish;
    }

    private int getFishCnt(int[][] grid, int x, int y) {
        int l = grid.length;
        int w = grid[0].length;

        // ✅ Correct base case
        if (x < 0 || x >= w || y < 0 || y >= l || grid[y][x] == 0) {
            return 0;
        }

        int val = grid[y][x];

        // mark visited
        grid[y][x] = 0;

        return val
                + getFishCnt(grid, x - 1, y)
                + getFishCnt(grid, x + 1, y)
                + getFishCnt(grid, x, y - 1)
                + getFishCnt(grid, x, y + 1);
    }

    // V1-1
    // IDEA: DFS
    // https://leetcode.com/problems/maximum-number-of-fish-in-a-grid/editorial/

    // Helper function to count the number of fishes in a connected component
    private int calculateFishes(
            int[][] grid,
            boolean[][] visited,
            int row,
            int col) {
        // Check boundary conditions, water cells, or already visited cells
        if (row < 0 ||
                row >= grid.length ||
                col < 0 ||
                col >= grid[0].length ||
                grid[row][col] == 0 ||
                visited[row][col]) {
            return 0;
        }

        // Mark the current cell as visited
        visited[row][col] = true;

        // Accumulate the fish count from the current cell and its neighbors
        return (grid[row][col] +
                calculateFishes(grid, visited, row, col + 1) +
                calculateFishes(grid, visited, row, col - 1) +
                calculateFishes(grid, visited, row + 1, col) +
                calculateFishes(grid, visited, row - 1, col));
    }

    public int findMaxFish_1_1(int[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        int maxFishCount = 0;

        // A 2D array to track visited cells
        boolean[][] visited = new boolean[rows][cols];

        // Iterate through all cells in the grid
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Start a DFS for unvisited land cells (fish available)
                if (grid[row][col] > 0 && !visited[row][col]) {
                    maxFishCount = Math.max(
                            maxFishCount,
                            calculateFishes(grid, visited, row, col));
                }
            }
        }

        // Return the maximum fish count found
        return maxFishCount;
    }



    // V1-2
    // IDEA: BFS
    // https://leetcode.com/problems/maximum-number-of-fish-in-a-grid/editorial/
    // Function to perform BFS and count fishes in the connected component
    private int countFishes(
            int[][] grid,
            boolean[][] visited,
            int row,
            int col) {
        int numRows = grid.length, numCols = grid[0].length, fishCount = 0;
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[] { row, col });
        visited[row][col] = true;

        // Directions for exploring up, down, left, right
        int[] rowDirections = { 0, 0, 1, -1 };
        int[] colDirections = { 1, -1, 0, 0 };

        // BFS traversal
        while (!q.isEmpty()) {
            int[] cell = q.poll();
            row = cell[0];
            col = cell[1];
            fishCount += grid[row][col];

            // Exploring all four directions
            for (int i = 0; i < 4; i++) {
                int newRow = row + rowDirections[i];
                int newCol = col + colDirections[i];
                if (0 <= newRow &&
                        newRow < numRows &&
                        0 <= newCol &&
                        newCol < numCols &&
                        grid[newRow][newCol] != 0 &&
                        !visited[newRow][newCol]) {
                    q.add(new int[] { newRow, newCol });
                    visited[newRow][newCol] = true;
                }
            }
        }
        return fishCount;
    }

    // Function to find the maximum number of fishes
    public int findMaxFish_1_2(int[][] grid) {
        int numRows = grid.length, numCols = grid[0].length, result = 0;
        boolean[][] visited = new boolean[numRows][numCols];

        // Iterating through the entire grid
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (grid[i][j] != 0 && !visited[i][j]) {
                    result = Math.max(result, countFishes(grid, visited, i, j));
                }
            }
        }
        return result;
    }




    // V2





}
