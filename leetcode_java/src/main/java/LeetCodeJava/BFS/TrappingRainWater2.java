package LeetCodeJava.BFS;

import java.util.Comparator;
import java.util.PriorityQueue;

// https://leetcode.com/problems/trapping-rain-water-ii/description/
/**
 * 407. Trapping Rain Water II
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Given an m x n integer matrix heightMap representing the height of each unit cell in a 2D elevation map, return the volume of water it can trap after raining.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: heightMap = [[1,4,3,1,3,2],[3,2,1,3,2,4],[2,3,3,2,3,1]]
 * Output: 4
 * Explanation: After the rain, water is trapped between the blocks.
 * We have two small ponds 1 and 3 units trapped.
 * The total volume of water trapped is 4.
 * Example 2:
 *
 *
 * Input: heightMap = [[3,3,3,3,3],[3,2,2,2,3],[3,2,1,2,3],[3,2,2,2,3],[3,3,3,3,3]]
 * Output: 10
 *
 *
 * Constraints:
 *
 * m == heightMap.length
 * n == heightMap[i].length
 * 1 <= m, n <= 200
 * 0 <= heightMap[i][j] <= 2 * 104
 *
 */
public class TrappingRainWater2 {

    // V0
//    public int trapRainWater(int[][] heightMap) {
//
//    }

    // V0-1
    // IDEA: custom class + PQ (gpt)
    /** NOTE !!!
     *
     *  we define custom class: cell
     *
     *  -> so we can save r, c, h (row, column, height) info at once
     */
    private static class Cell {
        int r, c, h;

        Cell(int r, int c, int h) {
            this.r = r;
            this.c = c;
            this.h = h;
        }
    }

    public int trapRainWater_0_1(int[][] heightMap) {
        if (heightMap == null || heightMap.length == 0 || heightMap[0].length == 0)
            return 0;

        int rows = heightMap.length;
        int cols = heightMap[0].length;
        // A grid with fewer than 3 rows or cols CAN NOT trap water.
        if (rows < 3 || cols < 3){
            return 0;
        }

        /** NOTE !!! we define visited grid to avoid duplicated visit */
        boolean[][] visited = new boolean[rows][cols];

        // Min-heap (PQ) ordered by `cell height`
        //PriorityQueue<Cell> pq = new PriorityQueue<>(Comparator.comparingInt(c -> c.h));
        PriorityQueue<Cell> pq = new PriorityQueue<>(new Comparator<Cell>() {
            @Override
            public int compare(Cell o1, Cell o2) {
                int diff = o1.h - o2.h;
                return diff;
            }
        });


        /** NOTE !!!
         *
         *  1. Push all border cells
         *  2. mark above as VISITED
         *  3. we do `push` op on
         *     - cols
         *     - rows
         */
        // Push all border cells into the PQ and mark visited
        for (int c = 0; c < cols; c++) {
            pq.offer(new Cell(0, c, heightMap[0][c]));
            pq.offer(new Cell(rows - 1, c, heightMap[rows - 1][c]));
            visited[0][c] = true;
            visited[rows - 1][c] = true;
        }
        /** NOTE !!!
         *
         *  1. Push all border cells
         *  2. mark above as VISITED
         *  3. we do `push` op on
         *     - cols
         *     - rows
         */
        for (int r = 1; r < rows - 1; r++) {
            pq.offer(new Cell(r, 0, heightMap[r][0]));
            pq.offer(new Cell(r, cols - 1, heightMap[r][cols - 1]));
            visited[r][0] = true;
            visited[r][cols - 1] = true;
        }

        int totalWater = 0;

        /** NOTE !!! we can move 4 dirs */
        int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        // Expand from the lowest boundary inward
        while (!pq.isEmpty()) {
            Cell cell = pq.poll(); // current lowest boundary cell

            /** NOTE !!! BFS:  move 4 dirs */
            for (int[] d : dirs) {
                int nr = cell.r + d[0];
                int nc = cell.c + d[1];

                /** NOTE !!! validate should proceed with NEW x, y  */
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || visited[nr][nc])
                    continue;

                visited[nr][nc] = true;
                int neighborHeight = heightMap[nr][nc];

                /** NOTE !!! the `can trap water` condition  */
                // If neighbor is lower than current boundary -> it traps water
                if (neighborHeight < cell.h) {
                    totalWater += cell.h - neighborHeight;
                    // push with raised height = cell.h (the water level / new boundary)
                    pq.offer(new Cell(nr, nc, cell.h));
                } else {
                    // no water trapped, push with its own height
                    pq.offer(new Cell(nr, nc, neighborHeight));
                }
            }
        }

        return totalWater;
    }

    // V1
    // IDEA: BFS + Priority Queue
    // https://leetcode.com/problems/trapping-rain-water-ii/editorial/
    public int trapRainWater_1(int[][] heightMap) {
        // Direction arrays
        int[] dRow = { 0, 0, -1, 1 };
        int[] dCol = { -1, 1, 0, 0 };

        int numOfRows = heightMap.length;
        int numOfCols = heightMap[0].length;

        boolean[][] visited = new boolean[numOfRows][numOfCols];

        // Priority queue (min-heap) to process boundary cells in increasing height order
        PriorityQueue<Cell_1> boundary = new PriorityQueue<>();

        // Add the first and last column cells to the boundary and mark them as visited
        for (int i = 0; i < numOfRows; i++) {
            boundary.offer(new Cell_1(heightMap[i][0], i, 0));
            boundary.offer(
                    new Cell_1(heightMap[i][numOfCols - 1], i, numOfCols - 1));
            // Mark left and right boundary cells as visited
            visited[i][0] = visited[i][numOfCols - 1] = true;
        }

        // Add the first and last row cells to the boundary and mark them as visited
        for (int i = 0; i < numOfCols; i++) {
            boundary.offer(new Cell_1(heightMap[0][i], 0, i));
            boundary.offer(
                    new Cell_1(heightMap[numOfRows - 1][i], numOfRows - 1, i));
            // Mark top and bottom boundary cells as visited
            visited[0][i] = visited[numOfRows - 1][i] = true;
        }

        // Initialize the total water volume to 0
        int totalWaterVolume = 0;

        // Process cells in the boundary (min-heap will always pop the smallest height)
        while (!boundary.isEmpty()) {
            // Pop the cell with the smallest height from the boundary
            Cell_1 currentCell = boundary.poll();

            int currentRow = currentCell.row;
            int currentCol = currentCell.col;
            int minBoundaryHeight = currentCell.height;

            // Explore all 4 neighboring cells
            for (int direction = 0; direction < 4; direction++) {
                // Calculate the row and column of the neighbor
                int neighborRow = currentRow + dRow[direction];
                int neighborCol = currentCol + dCol[direction];

                // Check if the neighbor is within the grid bounds and not yet visited
                if (isValidCell(
                        neighborRow,
                        neighborCol,
                        numOfRows,
                        numOfCols) &&
                        !visited[neighborRow][neighborCol]) {
                    // Get the height of the neighbor cell
                    int neighborHeight = heightMap[neighborRow][neighborCol];

                    // If the neighbor's height is less than the current boundary height, water can be trapped
                    if (neighborHeight < minBoundaryHeight) {
                        // Add the trapped water volume
                        totalWaterVolume += minBoundaryHeight - neighborHeight;
                    }

                    // Push the neighbor into the boundary with updated height (to prevent water leakage)
                    boundary.offer(
                            new Cell_1(
                                    Math.max(neighborHeight, minBoundaryHeight),
                                    neighborRow,
                                    neighborCol));
                    visited[neighborRow][neighborCol] = true;
                }
            }
        }

        // Return the total amount of trapped water
        return totalWaterVolume;
    }

    // Class to store the height and coordinates of a cell in the grid
    private static class Cell_1 implements Comparable<Cell_1> {

        int height;
        int row;
        int col;

        // Constructor to initialize a cell
        public Cell_1(int height, int row, int col) {
            this.height = height;
            this.row = row;
            this.col = col;
        }

        // Overload the compareTo method to make the priority queue a min-heap based on height
        @Override
        public int compareTo(Cell_1 other) {
            // Min-heap comparison
            return Integer.compare(this.height, other.height);
        }
    }

    // Helper function to check if a cell is valid (within grid bounds)
    private boolean isValidCell(
            int row,
            int col,
            int numOfRows,
            int numOfCols) {
        return row >= 0 && col >= 0 && row < numOfRows && col < numOfCols;
    }

    // V2
    // https://leetcode.ca/2017-01-10-407-Trapping-Rain-Water-II/
    // --- Helper Class (Cell) ---
    class Cell_2 implements Comparable<Cell_2> {
        int row;
        int column;
        int height;

        public Cell_2(int row, int column, int height) {
            this.row = row;
            this.column = column;
            this.height = height;
        }

        // Min-Heap property: sorts by the minimum height first
        @Override
        public int compareTo(Cell_2 other) {
            return this.height - other.height;
        }
    }

    public int trapRainWater_2(int[][] heightMap) {
        if (heightMap == null || heightMap.length <= 2 || heightMap[0].length <= 2) {
            return 0; // Requires at least 3x3 grid to trap water internally
        }

        int rows = heightMap.length;
        int columns = heightMap[0].length;
        boolean[][] visited = new boolean[rows][columns];

        // Priority Queue (Min-Heap) stores the cells on the current "border"
        PriorityQueue<Cell_2> priorityQueue = new PriorityQueue<>();

        // 1. Initialize the border (all perimeter cells) and mark them as visited.
        // Top and bottom borders
        for (int i = 0; i < columns; i++) {
            visited[0][i] = true;
            visited[rows - 1][i] = true;
            priorityQueue.offer(new Cell_2(0, i, heightMap[0][i]));
            priorityQueue.offer(new Cell_2(rows - 1, i, heightMap[rows - 1][i]));
        }

        // Left and right borders (excluding corners already added)
        for (int i = 1; i < rows - 1; i++) {
            visited[i][0] = true;
            visited[i][columns - 1] = true;
            priorityQueue.offer(new Cell_2(i, 0, heightMap[i][0]));
            priorityQueue.offer(new Cell_2(i, columns - 1, heightMap[i][columns - 1]));
        }

        int totalTrappedWater = 0;
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        // 2. Perform the Multi-Source BFS/Dijkstra's traversal.
        while (!priorityQueue.isEmpty()) {

            // FIX: Poll the cell with the minimum height from the current border.
            // This height acts as the "water level" for all its neighbors.
            Cell_2 currentCell = priorityQueue.poll();
            int currentR = currentCell.row;
            int currentC = currentCell.column;
            // The height of the lowest barrier encountered so far that connects to this cell.
            int minBoundaryHeight = currentCell.height;

            // Check all 4 neighbors
            for (int[] direction : directions) {
                int newR = currentR + direction[0];
                int newC = currentC + direction[1];

                // Check boundaries
                if (newR < 0 || newR >= rows || newC < 0 || newC >= columns) {
                    continue;
                }

                // Check if already visited
                if (!visited[newR][newC]) {
                    visited[newR][newC] = true;
                    int neighborHeight = heightMap[newR][newC];

                    // --- Core Trapping Logic ---
                    if (minBoundaryHeight > neighborHeight) {
                        // The water level (minBoundaryHeight) is higher than the neighbor's height.
                        // The neighbor can trap water.
                        totalTrappedWater += minBoundaryHeight - neighborHeight;

                        // FIX: When adding the neighbor back to the border, its height must be
                        // the HIGHER of the two (minBoundaryHeight), because this neighbor is now
                        // a NEW BARRIER. Water cannot flow over this new barrier until a higher
                        // barrier is encountered.
                        priorityQueue.offer(new Cell_2(newR, newC, minBoundaryHeight));
                    } else {
                        // The neighbor is higher than or equal to the current water level.
                        // No water is trapped here, and the neighbor becomes the new, higher barrier.
                        priorityQueue.offer(new Cell_2(newR, newC, neighborHeight));
                    }
                }
            }
        }

        return totalTrappedWater;
    }


}
