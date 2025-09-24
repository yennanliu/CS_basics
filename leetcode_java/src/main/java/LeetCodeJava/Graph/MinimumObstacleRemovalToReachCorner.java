package LeetCodeJava.Graph;

// https://leetcode.com/problems/minimum-obstacle-removal-to-reach-corner/description/

import java.util.*;

/**
 * 2290. Minimum Obstacle Removal to Reach Corner
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed 2D integer array grid of size m x n. Each cell has one of two values:
 *
 * 0 represents an empty cell,
 * 1 represents an obstacle that may be removed.
 * You can move up, down, left, or right from and to an empty cell.
 *
 * Return the minimum number of obstacles to remove so you can move from the upper left corner (0, 0) to the lower right corner (m - 1, n - 1).
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid = [[0,1,1],[1,1,0],[1,1,0]]
 * Output: 2
 * Explanation: We can remove the obstacles at (0, 1) and (0, 2) to create a path from (0, 0) to (2, 2).
 * It can be shown that we need to remove at least 2 obstacles, so we return 2.
 * Note that there may be other ways to remove 2 obstacles to create a path.
 * Example 2:
 *
 *
 * Input: grid = [[0,1,0,0,0],[0,1,0,1,0],[0,0,0,1,0]]
 * Output: 0
 * Explanation: We can move from (0, 0) to (2, 4) without removing any obstacles, so we return 0.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 105
 * 2 <= m * n <= 105
 * grid[i][j] is either 0 or 1.
 * grid[0][0] == grid[m - 1][n - 1] == 0
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 *
 */
public class MinimumObstacleRemovalToReachCorner {

    // V0
//    public int minimumObstacles(int[][] grid) {
//
//    }

    // V0-1
    // IDEA: Dijkstra's Algorithm (fixed by gpt)
    public int minimumObstacles(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int m = grid.length; // rows
        int n = grid[0].length; // cols

        // distance[y][x] = min obstacles to reach (y,x)
        int[][] dist = new int[m][n];
        for (int[] row : dist) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        dist[0][0] = 0;

        // PQ stores [cost, x, y]
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[] { 0, 0, 0 }); // start at (0,0) with cost=0

        int[][] moves = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int cost = cur[0], x = cur[1], y = cur[2];

            // Reached destination
            if (x == n - 1 && y == m - 1) {
                return cost;
            }

            // Skip if we already found better
            if (cost > dist[y][x])
                continue;

            for (int[] mv : moves) {
                int nx = x + mv[0];
                int ny = y + mv[1];

                if (nx >= 0 && nx < n && ny >= 0 && ny < m) {
                    int newCost = cost + grid[ny][nx];
                    if (newCost < dist[ny][nx]) {
                        dist[ny][nx] = newCost;
                        pq.offer(new int[] { newCost, nx, ny });
                    }
                }
            }
        }

        return -1; // should never happen
    }

    // V1-1
    // IDEA: Dijkstra's Algorithm
    // https://leetcode.com/problems/minimum-obstacle-removal-to-reach-corner/editorial/

    // Directions for movement: right, left, down, up
    private final int[][] directions = {
            { 0, 1 },
            { 0, -1 },
            { 1, 0 },
            { -1, 0 },
    };

    public int minimumObstacles_1_1(int[][] grid) {
        int m = grid.length, n = grid[0].length;

        int[][] minObstacles = new int[m][n];

        // Initialize all cells with a large value, representing unvisited cells
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                minObstacles[i][j] = Integer.MAX_VALUE;
            }
        }

        // Start from the top-left corner, accounting for its obstacle value
        minObstacles[0][0] = grid[0][0];

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);

        // Add the starting cell to the priority queue
        pq.add(new int[] { minObstacles[0][0], 0, 0 });

        while (!pq.isEmpty()) {
            // Process the cell with the fewest obstacles removed so far
            int[] current = pq.poll();
            int obstacles = current[0], row = current[1], col = current[2];

            // If we've reached the bottom-right corner, return the result
            if (row == m - 1 && col == n - 1) {
                return obstacles;
            }

            // Explore all four possible directions from the current cell
            for (int[] dir : directions) {
                int newRow = row + dir[0], newCol = col + dir[1];

                if (isValid(grid, newRow, newCol)) {
                    // Calculate the obstacles removed if moving to the new cell
                    int newObstacles = obstacles + grid[newRow][newCol];

                    // Update if we've found a path with fewer obstacles to the new cell
                    if (newObstacles < minObstacles[newRow][newCol]) {
                        minObstacles[newRow][newCol] = newObstacles;
                        pq.add(new int[] { newObstacles, newRow, newCol });
                    }
                }
            }
        }

        return minObstacles[m - 1][n - 1];
    }

    // Helper method to check if the cell is within the grid bounds
    private boolean isValid(int[][] grid, int row, int col) {
        return (row >= 0 && col >= 0 && row < grid.length && col < grid[0].length);
    }

    // V1-2
    // IDEA: BFS
    // https://leetcode.com/problems/minimum-obstacle-removal-to-reach-corner/editorial/
    // Directions for movement: right, left, down, up
    private final int[][] directions_1_2 = {
            { 0, 1 },
            { 0, -1 },
            { 1, 0 },
            { -1, 0 },
    };

    public int minimumObstacles_1_2(int[][] grid) {
        int m = grid.length, n = grid[0].length;

        // Distance matrix to store the minimum obstacles removed to reach each cell
        int[][] minObstacles = new int[m][n];

        // Initialize all cells with a large value, representing unvisited cells
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                minObstacles[i][j] = Integer.MAX_VALUE;
            }
        }

        minObstacles[0][0] = 0;

        Deque<int[]> deque = new ArrayDeque<>();
        deque.add(new int[] { 0, 0, 0 }); // {obstacles, row, col}

        while (!deque.isEmpty()) {
            int[] current = deque.poll();
            int obstacles = current[0], row = current[1], col = current[2];

            // Explore all four possible directions from the current cell
            for (int[] dir : directions_1_2) {
                int newRow = row + dir[0], newCol = col + dir[1];

                if (isValid_1_2(grid, newRow, newCol) &&
                        minObstacles[newRow][newCol] == Integer.MAX_VALUE) {
                    if (grid[newRow][newCol] == 1) {
                        // If it's an obstacle, add 1 to obstacles and push to the back
                        minObstacles[newRow][newCol] = obstacles + 1;
                        deque.addLast(
                                new int[] { obstacles + 1, newRow, newCol });
                    } else {
                        // If it's an empty cell, keep the obstacle count and push to the front
                        minObstacles[newRow][newCol] = obstacles;
                        deque.addFirst(new int[] { obstacles, newRow, newCol });
                    }
                }
            }
        }

        return minObstacles[m - 1][n - 1];
    }

    // Helper method to check if the cell is within the grid bounds
    private boolean isValid_1_2(int[][] grid, int row, int col) {
        return (row >= 0 && col >= 0 && row < grid.length && col < grid[0].length);
    }

    // V2
    // IDEA: BFS
    // https://leetcode.com/problems/minimum-obstacle-removal-to-reach-corner/solutions/6090483/minimum-obstacle-removal-bfs-approach-be-906q/
    public int minimumObstacles_2(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] distance = new int[m][n];
        for (int[] row : distance)
            Arrays.fill(row, Integer.MAX_VALUE);
        Deque<int[]> dq = new ArrayDeque<>();

        distance[0][0] = 0;
        dq.offerFirst(new int[] { 0, 0 });
        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

        while (!dq.isEmpty()) {
            int[] cell = dq.pollFirst();
            int x = cell[0], y = cell[1];
            for (int[] dir : directions) {
                int nx = x + dir[0], ny = y + dir[1];
                if (nx >= 0 && nx < m && ny >= 0 && ny < n) {
                    int newDist = distance[x][y] + grid[nx][ny];
                    if (newDist < distance[nx][ny]) {
                        distance[nx][ny] = newDist;
                        if (grid[nx][ny] == 0) {
                            dq.offerFirst(new int[] { nx, ny });
                        } else {
                            dq.offerLast(new int[] { nx, ny });
                        }
                    }
                }
            }
        }
        return distance[m - 1][n - 1];
    }


    // V3
    // https://leetcode.com/problems/minimum-obstacle-removal-to-reach-corner/solutions/2086036/lets-solve-this-problem-based-on-never-g-rxep/

}
