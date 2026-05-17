package LeetCodeJava.BFS;

// https://leetcode.com/problems/map-of-highest-peak/description/

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 *  1765. Map of Highest Peak
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an integer matrix isWater of size m x n that represents a map of land and water cells.
 *
 * If isWater[i][j] == 0, cell (i, j) is a land cell.
 * If isWater[i][j] == 1, cell (i, j) is a water cell.
 * You must assign each cell a height in a way that follows these rules:
 *
 * The height of each cell must be non-negative.
 * If the cell is a water cell, its height must be 0.
 * Any two adjacent cells must have an absolute height difference of at most 1. A cell is adjacent to another cell if the former is directly north, east, south, or west of the latter (i.e., their sides are touching).
 * Find an assignment of heights such that the maximum height in the matrix is maximized.
 *
 * Return an integer matrix height of size m x n where height[i][j] is cell (i, j)'s height. If there are multiple solutions, return any of them.
 *
 *
 *
 * Example 1:
 *
 *
 *
 * Input: isWater = [[0,1],[0,0]]
 * Output: [[1,0],[2,1]]
 * Explanation: The image shows the assigned heights of each cell.
 * The blue cell is the water cell, and the green cells are the land cells.
 * Example 2:
 *
 *
 *
 * Input: isWater = [[0,0,1],[1,0,0],[0,0,0]]
 * Output: [[1,1,0],[0,1,1],[1,2,2]]
 * Explanation: A height of 2 is the maximum possible height of any assignment.
 * Any height assignment that has a maximum height of 2 while still meeting the rules will also be accepted.
 *
 *
 * Constraints:
 *
 * m == isWater.length
 * n == isWater[i].length
 * 1 <= m, n <= 1000
 * isWater[i][j] is 0 or 1.
 * There is at least one water cell.
 *
 *
 * Note: This question is the same as 542: https://leetcode.com/problems/01-matrix/
 *
 *
 */
public class MapOfHighestPeak {

    // V0
//    public int[][] highestPeak(int[][] isWater) {
//
//    }


    // V0-1
    // IDEA: MULTI SOURCE BFS (gpt)
    public int[][] highestPeak_0_1(int[][] isWater) {

        int l = isWater.length;
        int w = isWater[0].length;

        int[][] res = new int[l][w];

        // visited
        boolean[][] visited = new boolean[l][w];

        // bfs queue: {x, y, height}
        Queue<int[]> q = new LinkedList<>();

        // add all water cells as BFS sources
        for (int y = 0; y < l; y++) {
            for (int x = 0; x < w; x++) {
                if (isWater[y][x] == 1) {
                    visited[y][x] = true;
                    res[y][x] = 0;
                    q.offer(new int[] { x, y, 0 });
                }
            }
        }

        int[][] dirs = new int[][] {
                { 0, 1 },
                { 0, -1 },
                { 1, 0 },
                { -1, 0 }
        };

        while (!q.isEmpty()) {

            int[] cur = q.poll();

            int x = cur[0];
            int y = cur[1];
            int h = cur[2];

            for (int[] d : dirs) {

                int nx = x + d[0];
                int ny = y + d[1];

                // valid + not visited
                if (nx >= 0 && nx < w &&
                        ny >= 0 && ny < l &&
                        !visited[ny][nx]) {

                    visited[ny][nx] = true;

                    res[ny][nx] = h + 1;

                    q.offer(new int[] { nx, ny, h + 1 });
                }
            }
        }

        return res;
    }



    // V0-2
    // IDEA: MULTI SOURCE BFS (gemini)
    public int[][] highestPeak_0_2(int[][] isWater) {
        int rows = isWater.length;
        int cols = isWater[0].length;

        int[][] res = new int[rows][cols];
        // Use primitive boolean (defaults to false, avoiding NullPointerExceptions)
        boolean[][] visited = new boolean[rows][cols];

        // Queue stores only the coordinates: {row, col}
        Queue<int[]> q = new LinkedList<>();

        // 1. Initialize BFS queue with all water cells simultaneously
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (isWater[r][c] == 1) {
                    res[r][c] = 0;
                    visited[r][c] = true;
                    q.add(new int[] { r, c }); // Directly add to queue
                }
            }
        }

        int[][] dirs = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

        // 2. Multi-Source BFS loop
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int r = cur[0];
            int c = cur[1];

            for (int[] d : dirs) {
                int nr = r + d[0];
                int nc = c + d[1];

                // Check bounds and if the neighbor hasn't been visited yet
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !visited[nr][nc]) {
                    visited[nr][nc] = true;
                    // The neighbor's height is exactly 1 unit higher than the current cell
                    res[nr][nc] = res[r][c] + 1;
                    q.add(new int[] { nr, nc });
                }
            }
        }

        return res;
    }



    // V1-1
    // IDEA: BFS
    // https://leetcode.com/problems/map-of-highest-peak/editorial/
    public int[][] highestPeak_1_1(int[][] isWater) {
        int[] dx = { 0, 0, 1, -1 }; // Horizontal movement: right, left, down, up
        int[] dy = { 1, -1, 0, 0 }; // Vertical movement corresponding to dx

        int rows = isWater.length;
        int columns = isWater[0].length;

        // Initialize the height matrix with -1 (unprocessed cells)
        int[][] cellHeights = new int[rows][columns];
        for (int[] row : cellHeights) {
            Arrays.fill(row, -1);
        }

        // Queue to perform breadth-first search
        Queue<int[]> cellQueue = new LinkedList<>();

        // Add all water cells to the queue and set their height to 0
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < columns; y++) {
                if (isWater[x][y] == 1) {
                    cellQueue.add(new int[] { x, y });
                    cellHeights[x][y] = 0;
                }
            }
        }

        int heightOfNextLayer = 1; // Initial height for land cells adjacent to water

        // Perform BFS
        while (!cellQueue.isEmpty()) {
            int layerSize = cellQueue.size();

            // Iterate through all cells in the current layer
            for (int i = 0; i < layerSize; i++) {
                int[] currentCell = cellQueue.poll();

                // Check all four possible directions for neighboring cells
                for (int d = 0; d < 4; d++) {
                    int neighborX = currentCell[0] + dx[d];
                    int neighborY = currentCell[1] + dy[d];

                    // Check if the neighbor is valid and unprocessed
                    if (isValidCell_1_1(neighborX, neighborY, rows, columns) &&
                            cellHeights[neighborX][neighborY] == -1) {
                        cellHeights[neighborX][neighborY] = heightOfNextLayer;
                        cellQueue.add(new int[] { neighborX, neighborY });
                    }
                }
            }
            heightOfNextLayer++; // Increment height for the next layer
        }

        return cellHeights;
    }

    // Function to check if a cell is within the grid boundaries
    private boolean isValidCell_1_1(int x, int y, int rows, int columns) {
        return x >= 0 && y >= 0 && x < rows && y < columns;
    }



    // V1-2
    // IDEA: DP
    // https://leetcode.com/problems/map-of-highest-peak/editorial/
    public int[][] highestPeak_1_2(int[][] isWater) {
        int rows = isWater.length;
        int columns = isWater[0].length;
        final int INF = rows * columns; // Large value to represent uninitialized heights

        // Initialize the cellHeights matrix with INF (unprocessed cells)
        int[][] cellHeights = new int[rows][columns];
        for (int[] row : cellHeights) {
            Arrays.fill(row, INF);
        }

        // Set the height of water cells to 0
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (isWater[row][col] == 1) {
                    cellHeights[row][col] = 0; // Water cells have height 0
                }
            }
        }

        // Forward pass: updating heights based on top and left neighbors
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int minNeighborDistance = INF; // Initialize minimum neighbor distance

                // Check the cell above
                int neighborRow = row - 1;
                int neighborCol = col;
                if (isValidCell_1_2(neighborRow, neighborCol, rows, columns)) {
                    minNeighborDistance = Math.min(
                            minNeighborDistance,
                            cellHeights[neighborRow][neighborCol]);
                }

                // Check the cell to the left
                neighborRow = row;
                neighborCol = col - 1;
                if (isValidCell_1_2(neighborRow, neighborCol, rows, columns)) {
                    minNeighborDistance = Math.min(
                            minNeighborDistance,
                            cellHeights[neighborRow][neighborCol]);
                }

                // Set the current cell's height as the minimum of its neighbors + 1
                cellHeights[row][col] = Math.min(
                        cellHeights[row][col],
                        minNeighborDistance + 1);
            }
        }

        // Backward pass: updating heights based on bottom and right neighbors
        for (int row = rows - 1; row >= 0; row--) {
            for (int col = columns - 1; col >= 0; col--) {
                int minNeighborDistance = INF; // Initialize minimum neighbor distance

                // Check the cell below
                int neighborRow = row + 1;
                int neighborCol = col;
                if (isValidCell_1_2(neighborRow, neighborCol, rows, columns)) {
                    minNeighborDistance = Math.min(
                            minNeighborDistance,
                            cellHeights[neighborRow][neighborCol]);
                }

                // Check the cell to the right
                neighborRow = row;
                neighborCol = col + 1;
                if (isValidCell_1_2(neighborRow, neighborCol, rows, columns)) {
                    minNeighborDistance = Math.min(
                            minNeighborDistance,
                            cellHeights[neighborRow][neighborCol]);
                }

                // Set the current cell's height as the minimum of its neighbors + 1
                cellHeights[row][col] = Math.min(
                        cellHeights[row][col],
                        minNeighborDistance + 1);
            }
        }

        return cellHeights; // Return the calculated cell heights
    }

    // Function to check if a cell is within grid bounds
    private boolean isValidCell_1_2(int row, int col, int rows, int columns) {
        return row >= 0 && col >= 0 && row < rows && col < columns;
    }




    // V2

    // V3


}
