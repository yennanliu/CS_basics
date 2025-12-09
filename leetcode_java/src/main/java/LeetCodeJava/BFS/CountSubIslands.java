package LeetCodeJava.BFS;

// https://leetcode.com/problems/count-sub-islands/description/

import java.util.LinkedList;
import java.util.Queue;

/**
 *  1905. Count Sub Islands
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given two m x n binary matrices grid1 and grid2 containing only 0's (representing water) and 1's (representing land). An island is a group of 1's connected 4-directionally (horizontal or vertical). Any cells outside of the grid are considered water cells.
 *
 * An island in grid2 is considered a sub-island if there is an island in grid1 that contains all the cells that make up this island in grid2.
 *
 * Return the number of islands in grid2 that are considered sub-islands.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid1 = [[1,1,1,0,0],[0,1,1,1,1],[0,0,0,0,0],[1,0,0,0,0],[1,1,0,1,1]], grid2 = [[1,1,1,0,0],[0,0,1,1,1],[0,1,0,0,0],[1,0,1,1,0],[0,1,0,1,0]]
 * Output: 3
 * Explanation: In the picture above, the grid on the left is grid1 and the grid on the right is grid2.
 * The 1s colored red in grid2 are those considered to be part of a sub-island. There are three sub-islands.
 * Example 2:
 *
 *
 * Input: grid1 = [[1,0,1,0,1],[1,1,1,1,1],[0,0,0,0,0],[1,1,1,1,1],[1,0,1,0,1]], grid2 = [[0,0,0,0,0],[1,1,1,1,1],[0,1,0,1,0],[0,1,0,1,0],[1,0,0,0,1]]
 * Output: 2
 * Explanation: In the picture above, the grid on the left is grid1 and the grid on the right is grid2.
 * The 1s colored red in grid2 are those considered to be part of a sub-island. There are two sub-islands.
 *
 *
 * Constraints:
 *
 * m == grid1.length == grid2.length
 * n == grid1[i].length == grid2[i].length
 * 1 <= m, n <= 500
 * grid1[i][j] and grid2[i][j] are either 0 or 1.
 *
 *
 */
public class CountSubIslands {

    // V0
//    public int countSubIslands(int[][] grid1, int[][] grid2) {
//
//    }

    // V1-1
    // IDEA: BFS
    // https://leetcode.com/problems/count-sub-islands/editorial/
    // Directions in which we can traverse inside the grids.
    private final int[][] directions = {
            { 0, 1 },
            { 1, 0 },
            { 0, -1 },
            { -1, 0 },
    };

    // Helper method to check if the cell at the position (x, y) in the 'grid'
    // is a land cell.
    private boolean isCellLand_1_1(int x, int y, int[][] grid) {
        return grid[x][y] == 1;
    }

    // Traverse all cells of island starting at position (x, y) in 'grid2',
    // and check this island is a sub-island in 'grid1'.
    private boolean isSubIsland_1_1(
            int x,
            int y,
            int[][] grid1,
            int[][] grid2,
            boolean[][] visited) {
        int totalRows = grid2.length;
        int totalCols = grid2[0].length;

        boolean isSubIsland_1_1 = true;

        Queue<int[]> pendingCells = new LinkedList<>();
        // Push the starting cell in the queue and mark it as visited.
        pendingCells.offer(new int[] { x, y });
        visited[x][y] = true;

        // Traverse on all cells using the breadth-first search method.
        while (!pendingCells.isEmpty()) {
            int[] curr = pendingCells.poll();
            int currX = curr[0];
            int currY = curr[1];

            // If the current position cell is not a land cell in 'grid1',
            // then the current island can't be a sub-island.
            if (!isCellLand_1_1(currX, currY, grid1)) {
                isSubIsland_1_1 = false;
            }

            for (int[] direction : directions) {
                int nextX = currX + direction[0];
                int nextY = currY + direction[1];
                // If the next cell is inside 'grid2', is never visited and
                // is a land cell, then we traverse to the next cell.
                if (nextX >= 0 &&
                        nextY >= 0 &&
                        nextX < totalRows &&
                        nextY < totalCols &&
                        !visited[nextX][nextY] &&
                        isCellLand_1_1(nextX, nextY, grid2)) {
                    // Push the next cell in the queue and mark it as visited.
                    pendingCells.offer(new int[] { nextX, nextY });
                    visited[nextX][nextY] = true;
                }
            }
        }

        return isSubIsland_1_1;
    }

    public int countSubIslands_1_1(int[][] grid1, int[][] grid2) {
        int totalRows = grid2.length;
        int totalCols = grid2[0].length;

        boolean[][] visited = new boolean[totalRows][totalCols];
        int subIslandCounts = 0;

        // Iterate on each cell in 'grid2'
        for (int x = 0; x < totalRows; ++x) {
            for (int y = 0; y < totalCols; ++y) {
                // If cell at the position (x, y) in the 'grid2' is not visited,
                // is a land cell in 'grid2', and the island
                // starting from this cell is a sub-island in 'grid1', then we
                // increment the count of sub-islands.
                if (!visited[x][y] &&
                        isCellLand_1_1(x, y, grid2) &&
                        isSubIsland_1_1(x, y, grid1, grid2, visited)) {
                    subIslandCounts += 1;
                }
            }
        }
        // Return total count of sub-islands.
        return subIslandCounts;
    }



    // V1-2
    // IDEA: DFS
    // https://leetcode.com/problems/count-sub-islands/editorial/
    // Directions in which we can traverse inside the grids.
    // Directions in which we can traverse inside the grids.
//    private final int[][] directions = {
//            { 0, 1 },
//            { 1, 0 },
//            { 0, -1 },
//            { -1, 0 },
//    };

    // Helper method to check if the cell at the position (x, y) in the 'grid'
    // is a land cell.
    private boolean isCellLand(int x, int y, int[][] grid) {
        return grid[x][y] == 1;
    }

    // Traverse all cells of island starting at position (x, y) in 'grid2',
    // and check if this island is a sub-island in 'grid1'.
    private boolean isSubIsland(
            int x,
            int y,
            int[][] grid1,
            int[][] grid2,
            boolean[][] visited) {
        int totalRows = grid2.length;
        int totalCols = grid2[0].length;
        // Traverse on all cells using the depth-first search method.
        boolean isSubIsland = true;

        // If the current cell is not a land cell in 'grid1', then the current island can't be a sub-island.
        if (!isCellLand(x, y, grid1)) {
            isSubIsland = false;
        }

        // Traverse on all adjacent cells.
        for (int[] direction : directions) {
            int nextX = x + direction[0];
            int nextY = y + direction[1];
            // If the next cell is inside 'grid2', is not visited, and is a land cell,
            // then we traverse to the next cell.
            if (nextX >= 0 &&
                    nextY >= 0 &&
                    nextX < totalRows &&
                    nextY < totalCols &&
                    !visited[nextX][nextY] &&
                    isCellLand(nextX, nextY, grid2)) {
                // Mark the next cell as visited.
                visited[nextX][nextY] = true;
                boolean nextCellIsPartOfSubIsland = isSubIsland(
                        nextX,
                        nextY,
                        grid1,
                        grid2,
                        visited);
                isSubIsland = isSubIsland && nextCellIsPartOfSubIsland;
            }
        }
        return isSubIsland;
    }

    public int countSubIslands_1_2(int[][] grid1, int[][] grid2) {
        int totalRows = grid2.length;
        int totalCols = grid2[0].length;

        boolean[][] visited = new boolean[totalRows][totalCols];
        int subIslandCounts = 0;

        // Iterate over each cell in 'grid2'.
        for (int x = 0; x < totalRows; ++x) {
            for (int y = 0; y < totalCols; ++y) {
                // If the cell at position (x, y) in 'grid2' is not visited,
                // is a land cell in 'grid2', and the island starting from this cell is a sub-island in 'grid1',
                // then increment the count of sub-islands.
                if (!visited[x][y] && isCellLand(x, y, grid2)) {
                    visited[x][y] = true;
                    if (isSubIsland(x, y, grid1, grid2, visited)) {
                        subIslandCounts += 1;
                    }
                }
            }
        }
        // Return total count of sub-islands.
        return subIslandCounts;
    }

    // V2-1
    // IDEA:
    // https://leetcode.ca/2021-07-29-1905-Count-Sub-Islands/
   // static int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} };

    public int countSubIslands_2_1(int[][] grid1, int[][] grid2) {
        int subIslands = 0;
        int m = grid1.length, n = grid1[0].length;
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid2[i][j] == 1 && !visited[i][j]) {
                    boolean isSubisland = breadthFirstSearch(grid1, grid2, visited, m, n, i, j);
                    if (isSubisland)
                        subIslands++;
                }
            }
        }
        return subIslands;
    }

    public boolean breadthFirstSearch(int[][] grid1, int[][] grid2, boolean[][] visited, int m, int n, int startRow, int startColumn) {
        boolean isSubisland = true;
        Queue<int[]> queue = new LinkedList<int[]>();
        queue.offer(new int[]{startRow, startColumn});
        visited[startRow][startColumn] = true;
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int row = cell[0], column = cell[1];
            if (grid1[row][column] == 0)
                isSubisland = false;
            for (int[] direction : directions) {
                int newRow = row + direction[0], newColumn = column + direction[1];
                if (newRow >= 0 && newRow < m && newColumn >= 0 && newColumn < n && grid2[newRow][newColumn] == 1 && !visited[newRow][newColumn]) {
                    visited[newRow][newColumn] = true;
                    queue.offer(new int[]{newRow, newColumn});
                }
            }
        }
        return isSubisland;
    }


    // V2-2
    // IDEA:
    // https://leetcode.ca/2021-07-29-1905-Count-Sub-Islands/
    public int countSubIslands_2_2(int[][] grid1, int[][] grid2) {
        int m = grid1.length;
        int n = grid1[0].length;
        int ans = 0;
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (grid2[i][j] == 1 && dfs(i, j, m, n, grid1, grid2)) {
                    ++ans;
                }
            }
        }
        return ans;
    }

    private boolean dfs(int i, int j, int m, int n, int[][] grid1, int[][] grid2) {
        boolean ans = grid1[i][j] == 1;
        grid2[i][j] = 0;
        int[] dirs = {-1, 0, 1, 0, -1};
        for (int k = 0; k < 4; ++k) {
            int x = i + dirs[k];
            int y = j + dirs[k + 1];
            if (x >= 0 && x < m && y >= 0 && y < n && grid2[x][y] == 1
                    && !dfs(x, y, m, n, grid1, grid2)) {
                ans = false;
            }
        }
        return ans;
    }



}
