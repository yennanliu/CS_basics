package LeetCodeJava.BFS;

// https://leetcode.com/problems/shortest-bridge/description/

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 934. Shortest Bridge
 * Medium
 * Topics
 * Companies
 * You are given an n x n binary matrix grid where 1 represents land and 0 represents water.
 *
 * An island is a 4-directionally connected group of 1's not connected to any other 1's. There are exactly two islands in grid.
 *
 * You may change 0's to 1's to connect the two islands to form one island.
 *
 * Return the smallest number of 0's you must flip to connect the two islands.
 *
 *
 *
 * Example 1:
 *
 * Input: grid = [[0,1],[1,0]]
 * Output: 1
 * Example 2:
 *
 * Input: grid = [[0,1,0],[0,0,0],[0,0,1]]
 * Output: 2
 * Example 3:
 *
 * Input: grid = [[1,1,1,1,1],[1,0,0,0,1],[1,0,1,0,1],[1,0,0,0,1],[1,1,1,1,1]]
 * Output: 1
 *
 *
 * Constraints:
 *
 * n == grid.length == grid[i].length
 * 2 <= n <= 100
 * grid[i][j] is either 0 or 1.
 * There are exactly two islands in grid.
 */
public class ShortestBridge {
    // V0
//    public int shortestBridge(int[][] grid) {
//
//    }

    // V0-1
    // IDEA: DFS + BFS (gpt)
    public int shortestBridge_0_1(int[][] grid) {
        int n = grid.length;
        boolean found = false;
        Queue<int[]> queue = new LinkedList<>();

        // 1) find first island, mark as 2, and add all its cells to queue
        for (int y = 0; y < n && !found; y++) {
            for (int x = 0; x < n && !found; x++) {
                if (grid[y][x] == 1) {
                    dfsMark(grid, x, y, queue);
                    found = true;
                }
            }
        }

        // 2) BFS expand until we reach second island
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        int steps = 0;
        boolean[][] visited = new boolean[n][n];

        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size-- > 0) {
                int[] cur = queue.poll();
                int x = cur[0], y = cur[1];

                for (int[] d : dirs) {
                    int nx = x + d[0];
                    int ny = y + d[1];

                    if (nx >= 0 && nx < n && ny >= 0 && ny < n && !visited[ny][nx]) {
                        visited[ny][nx] = true;

                        if (grid[ny][nx] == 1) {
                            return steps; // reached second island
                        }

                        if (grid[ny][nx] == 0) {
                            queue.add(new int[] { nx, ny });
                        }
                    }
                }
            }
            steps++;
        }

        return -1;
    }

    // DFS to mark the first island
    void dfsMark(int[][] grid, int x, int y, Queue<int[]> queue) {
        int n = grid.length;
        if (x < 0 || x >= n || y < 0 || y >= n || grid[y][x] != 1)
            return;

        grid[y][x] = 2; // mark first island
        queue.add(new int[] { x, y });

        dfsMark(grid, x + 1, y, queue);
        dfsMark(grid, x - 1, y, queue);
        dfsMark(grid, x, y + 1, queue);
        dfsMark(grid, x, y - 1, queue);
    }


    // V0-2
    // IDEA: DFS + BFS (gemini)
    private int R;
    private int C;
    // 4 directions: Up, Down, Left, Right
    private final int[][] MOVES = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

    // Queue to hold the border cells of the first island for the Multi-Source BFS
    private Queue<int[]> boundaryQueue;

    /**
     * Solves LC 934: Shortest Bridge using DFS (to find island) + BFS (to find bridge).
     * Time Complexity: O(R * C)
     */
    public int shortestBridge_0_2(int[][] grid) {
        this.R = grid.length;
        this.C = grid[0].length;
        this.boundaryQueue = new LinkedList<>();

        // 1. Find the first island and mark its cells
        // The loop starts at false, flips to true after finding the first island.
        boolean firstIslandFound = false;

        for (int r = 0; r < R; r++) {
            if (firstIslandFound)
                break;
            for (int c = 0; c < C; c++) {
                if (grid[r][c] == 1) {
                    // Start DFS to find and mark the entire island
                    dfsMarkIsland(grid, r, c);
                    firstIslandFound = true;
                    break;
                }
            }
        }

        // 2. Multi-Source BFS to find the shortest bridge
        int bridgeLength = 0;

        while (!boundaryQueue.isEmpty()) {
            int size = boundaryQueue.size();

            for (int i = 0; i < size; i++) {
                int[] curr = boundaryQueue.poll();
                int r = curr[0];
                int c = curr[1];

                // Check all 4 neighbors
                for (int[] move : MOVES) {
                    int nr = r + move[0];
                    int nc = c + move[1];

                    if (isValid(nr, nc)) {

                        // If we hit a '1', we found the second island. Return the current bridge length.
                        if (grid[nr][nc] == 1) {
                            return bridgeLength;
                        }

                        // If we hit '0' (water), expand the island (bridge)
                        if (grid[nr][nc] == 0) {
                            grid[nr][nc] = 2; // Mark as part of the expanding bridge/first island
                            boundaryQueue.offer(new int[] { nr, nc });
                        }
                        // Note: If grid[nr][nc] == 2, we skip it (already visited/expanded)
                    }
                }
            }
            // Increment the bridge length for the next level (next ring of water cells)
            bridgeLength++;
        }

        // Should not happen if two islands exist
        return -1;
    }

    /**
     * Helper function to perform DFS: marks the first island and populates the boundaryQueue.
     * Marks all cells of the first island as '2'.
     */
    private void dfsMarkIsland(int[][] grid, int r, int c) {
        // Base case: Out of bounds, already marked ('2'), or water ('0')
        if (!isValid(r, c) || grid[r][c] == 0 || grid[r][c] == 2) {
            return;
        }

        // Mark the current cell as belonging to the first island
        grid[r][c] = 2;

        // Check if this cell is a boundary cell (adjacent to water)
        boolean isBoundary = false;

        for (int[] move : MOVES) {
            int nr = r + move[0];
            int nc = c + move[1];

            if (isValid(nr, nc)) {
                if (grid[nr][nc] == 0) {
                    isBoundary = true; // Found adjacent water
                } else if (grid[nr][nc] == 1) {
                    // Continue DFS to mark the connected land
                    dfsMarkIsland(grid, nr, nc);
                }
            } else {
                // If the island touches the edge of the grid, that's also considered a boundary
                isBoundary = true;
            }
        }

        // Only add the cell to the queue if it's a boundary cell
        if (isBoundary) {
            boundaryQueue.offer(new int[] { r, c });
        }
    }

    private boolean isValid(int r, int c) {
        return r >= 0 && r < R && c >= 0 && c < C;
    }


    // V1-1
    // https://leetcode.com/problems/shortest-bridge/editorial/
    // IDEA: Depth-First-Search + Breadth-First-Search
    private List<int[]> bfsQueue;

    // Recursively check the neighboring land cell of current cell grid[x][y] and
    // add all
    // land cells of island A to bfsQueue.
    private void dfs(int[][] grid, int x, int y, int n) {
        grid[x][y] = 2;
        bfsQueue.add(new int[] { x, y });
        for (int[] pair : new int[][] { { x + 1, y }, { x - 1, y }, { x, y + 1 }, { x, y - 1 } }) {
            int curX = pair[0], curY = pair[1];
            if (0 <= curX && curX < n && 0 <= curY && curY < n && grid[curX][curY] == 1) {
                dfs(grid, curX, curY, n);
            }
        }
    }

    // Find any land cell, and we treat it as a cell of island A.
    public int shortestBridge_1_1(int[][] grid) {
        int n = grid.length;
        int firstX = -1, firstY = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    firstX = i;
                    firstY = j;
                    break;
                }
            }
        }

        // Add all land cells of island A to bfsQueue.
        bfsQueue = new ArrayList<>();
        dfs(grid, firstX, firstY, n);

        int distance = 0;
        while (!bfsQueue.isEmpty()) {
            List<int[]> newBfs = new ArrayList<>();
            for (int[] pair : bfsQueue) {
                int x = pair[0], y = pair[1];
                for (int[] nextPair : new int[][] { { x + 1, y }, { x - 1, y }, { x, y + 1 }, { x, y - 1 } }) {
                    int curX = nextPair[0], curY = nextPair[1];
                    if (0 <= curX && curX < n && 0 <= curY && curY < n) {
                        if (grid[curX][curY] == 1) {
                            return distance;
                        } else if (grid[curX][curY] == 0) {
                            newBfs.add(nextPair);
                            grid[curX][curY] = -1;
                        }
                    }
                }
            }

            // Once we finish one round without finding land cells of island B, we will
            // start the next round on all water cells that are 1 cell further away from
            // island A and increment the distance by 1.
            bfsQueue = newBfs;
            distance++;
        }

        return distance;
    }

    // V1-2
    // https://leetcode.com/problems/shortest-bridge/editorial/
    // IDEA: BFS
    public int shortestBridge_1_2(int[][] grid) {
        int n = grid.length;
        int firstX = -1, firstY = -1;

        // Find any land cell, and we treat it as a cell of island A.
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    firstX = i;
                    firstY = j;
                    break;
                }
            }
        }

        // bfsQueue for BFS on land cells of island A; secondBfsQueue for BFS on water
        // cells.
        /**
         *  NOTE !!!
         *
         *   -> bfsQueue for BFS on land cells of island A
         *
         *   -> secondBfsQueue for BFS on water cell
         */
        List<int[]> bfsQueue = new ArrayList<>();
        List<int[]> secondBfsQueue = new ArrayList<>();
        bfsQueue.add(new int[] { firstX, firstY });
        secondBfsQueue.add(new int[] { firstX, firstY });
        grid[firstX][firstY] = 2;

        // BFS for all land cells of island A and add them to secondBfsQueue.
        while (!bfsQueue.isEmpty()) {
            List<int[]> newBfs = new ArrayList<>();
            for (int[] cell : bfsQueue) {
                int x = cell[0];
                int y = cell[1];
                for (int[] next : new int[][] { { x + 1, y }, { x - 1, y }, { x, y + 1 }, { x, y - 1 } }) {
                    int curX = next[0];
                    int curY = next[1];
                    if (curX >= 0 && curX < n && curY >= 0 && curY < n && grid[curX][curY] == 1) {
                        newBfs.add(new int[] { curX, curY });
                        secondBfsQueue.add(new int[] { curX, curY });
                        grid[curX][curY] = 2;
                    }
                }
            }
            bfsQueue = newBfs;
        }

        int distance = 0;
        while (!secondBfsQueue.isEmpty()) {
            List<int[]> newBfs = new ArrayList<>();
            for (int[] cell : secondBfsQueue) {
                int x = cell[0];
                int y = cell[1];
                for (int[] next : new int[][] { { x + 1, y }, { x - 1, y }, { x, y + 1 }, { x, y - 1 } }) {
                    int curX = next[0];
                    int curY = next[1];
                    if (curX >= 0 && curX < n && curY >= 0 && curY < n) {
                        if (grid[curX][curY] == 1) {
                            return distance;
                        } else if (grid[curX][curY] == 0) {
                            newBfs.add(new int[] { curX, curY });
                            grid[curX][curY] = -1;
                        }
                    }
                }
            }

            // Once we finish one round without finding land cells of island B, we will
            // start the next round on all water cells that are 1 cell further away from
            // island A and increment the distance by 1.
            secondBfsQueue = newBfs;
            distance++;
        }
        return distance;
    }

    // V2
}
