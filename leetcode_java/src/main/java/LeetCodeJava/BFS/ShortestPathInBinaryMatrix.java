package LeetCodeJava.BFS;

// https://leetcode.com/problems/shortest-path-in-binary-matrix/description/

import java.util.LinkedList;
import java.util.Queue;

/**
 * 1091. Shortest Path in Binary Matrix
 * Medium
 * Topics
 * Companies
 * Hint
 * Given an n x n binary matrix grid, return the length of the shortest clear path in the matrix. If there is no clear path, return -1.
 *
 * A clear path in a binary matrix is a path from the top-left cell (i.e., (0, 0)) to the bottom-right cell (i.e., (n - 1, n - 1)) such that:
 *
 * All the visited cells of the path are 0.
 * All the adjacent cells of the path are 8-directionally connected (i.e., they are different and they share an edge or a corner).
 * The length of a clear path is the number of visited cells of this path.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid = [[0,1],[1,0]]
 * Output: 2
 * Example 2:
 *
 *
 * Input: grid = [[0,0,0],[1,1,0],[1,1,0]]
 * Output: 4
 * Example 3:
 *
 * Input: grid = [[1,0,0],[1,1,0],[1,1,0]]
 * Output: -1
 *
 *
 * Constraints:
 *
 * n == grid.length
 * n == grid[i].length
 * 1 <= n <= 100
 * grid[i][j] is 0 or 1
 *
 */
public class ShortestPathInBinaryMatrix {

    // V0
    // IDEA: BFS (fixed by gpt)
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
        public int shortestPathBinaryMatrix(int[][] grid) {
        int n = grid.length;

        // Edge case: Start or end point is blocked
        if (grid[0][0] != 0 || grid[n - 1][n - 1] != 0) {
            return -1;
        }

        // Directions for 8-connected neighbors
        int[][] directions = {
                { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 },
                { -1, -1 }, { -1, 1 }, { 1, 1 }, { 1, -1 }
        };

        // BFS Queue
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[] { 0, 0, 1 }); // {x, y, distance}

        // Visited set to avoid revisiting
        boolean[][] visited = new boolean[n][n];
        visited[0][0] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];
            int distance = current[2];

            // Check if we reached the destination
            if (x == n - 1 && y == n - 1) {
                return distance;
            }

            // Explore all 8 neighbors
            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                // Check if the new position is valid
                if (newX >= 0 && newX < n && newY >= 0 && newY < n
                        && grid[newX][newY] == 0 && !visited[newX][newY]) {
                    queue.offer(new int[] { newX, newY, distance + 1 });
                    /** NOTE !!! mark as visited
                     *
                     *  below is the CRITICAL OPTIMIZATION
                     *  -> we mark cell `visited` RIGHT AFTER
                     *     it added to queue
                     *
                     *  -> by doing so, we can reduce the redundant
                     *     enqueueing (e.g. if the cell already enqueued,
                     *     the SAME cell should NOT enqueued AGAIN
                     *     within the same while loop)
                     *
                     *
                     * -> if we put `mark as visited outside for loop,
                     *    such issue will happen, and cause TLE error)
                     *
                     */
                    visited[newX][newY] = true;
                }
            }
        }

        // If no path is found, return -1
        return -1;
    }

    // V0-0-1
    // IDEA: BFS (fixed by gemini)
    public int shortestPathBinaryMatrix_0_0_1(int[][] grid) {
        // edge
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return -1;
        }
        int l = grid.length;
        int w = grid[0].length;

        // ???
        if (grid[0][0] == 1 || grid[l - 1][w - 1] == 1) {
            return -1;
        }

        // NOTE !!! 8 directions
        int[][] moves = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, { 1, 1 }, { -1, -1 }, { -1, 1 },
                { 1, -1 } };

        Queue<Integer[]> q = new LinkedList<>();
        q.add(new Integer[] { 0, 0 });
        int step = 1;

        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                // NOTE !!!
                Integer[] cur = q.poll();
                int x = cur[1];
                int y = cur[0];
                // if already arrived dest
                if (x == w - 1 && y == l - 1) {
                    return step;
                }

                /**  NOTE !!!
                 *
                 *  DON'T put `grid[y][x] = 2; ` at below,
                 *  -> should do the `mark visited`  RIGHT AFTER
                 *  the element is added to queue
                 */
                // mark as visited
                //grid[y][x] = 2; // ??
                for (int[] m : moves) {
                    int x_ = x + m[1];
                    int y_ = y + m[0];
                    if (x_ >= 0 && x_ < w && y_ >= 0 && y_ < l) {
                        if (grid[y_][x_] == 0) {
                            q.add(new Integer[] { y_, x_ });
                            /** NOTE !!! mark as visited
                             *
                             *  below is the CRITICAL OPTIMIZATION
                             *  -> we mark cell `visited` RIGHT AFTER
                             *     it added to queue
                             *
                             *  -> by doing so, we can reduce the redundant
                             *     enqueueing (e.g. if the cell already enqueued,
                             *     the SAME cell should NOT enqueued AGAIN
                             *     within the same while loop)
                             *
                             *
                             * -> if we put `mark as visited outside for loop,
                             *    such issue will happen, and cause TLE error)
                             *
                             */
                            grid[y_][x_] = 2; // ??
                        }
                    }
                }
            }
            step += 1;
        }

        return -1;
    }

    // V0-0-2
    // IDEA: BFS (fixed by gpt)
    public int shortestPathBinaryMatrix_0_0_2(int[][] grid) {
        // edge
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return -1;
        }

        int l = grid.length;
        int w = grid[0].length;

        // if start or end is blocked
        if (grid[0][0] != 0 || grid[l - 1][w - 1] != 0) {
            return -1;
        }

        /** NOTE !!! // Directions for 8-connected neighbors  */
        // 8-directional moves
        int[][] moves = new int[][] {
                { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 },
                { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 }
        };

        boolean[][] visited = new boolean[l][w];
        Queue<int[]> q = new LinkedList<>();

        // start at (0,0) with path length 1
        q.add(new int[] { 0, 0, 1 });
        /** NOTE !!! set (0,0) as visited  */
        visited[0][0] = true;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int move = cur[2];

            // reached target
            if (x == w - 1 && y == l - 1) {
                return move;
            }

            for (int[] m : moves) {
                int nx = x + m[0];
                int ny = y + m[1];

                /** NOTE !!!
                 *
                 *    validate if `new x, y` is a valid one,
                 *    -> then add them to queue, proceed to BFS
                 */
                if (nx < 0 || nx >= w || ny < 0 || ny >= l)
                    continue;
                if (visited[ny][nx] || grid[ny][nx] != 0)
                    continue;

                visited[ny][nx] = true;
                q.add(new int[] { nx, ny, move + 1 });
            }
        }

        return -1;
    }

    // V0-1
    // IDEA: BFS (fixed by gpt)
    public int shortestPathBinaryMatrix_0_1(int[][] grid) {
        int n = grid.length;

        // Edge case: Start or end cell is blocked
        if (grid[0][0] != 0 || grid[n - 1][n - 1] != 0) {
            return -1;
        }

        // Directions for 8-connected neighbors
        int[][] directions = {
                { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 },
                { -1, -1 }, { -1, 1 }, { 1, 1 }, { 1, -1 }
        };

        // BFS queue: {x, y, pathLength}
        Queue<int[]> queue = new LinkedList<>();

        /** NOTE !!! init visisted as boolean array */
        boolean[][] visited = new boolean[n][n];

        // Initialize BFS
        /**
         * Queue : {x, y, path_len}
         */
        queue.add(new int[] { 0, 0, 1 });
        visited[0][0] = true;

        // BFS
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];
            int pathLength = current[2];

            // Check if we've reached the destination
            /** NOTE !!!  return res directly if it can reach `final status` */
            if (x == n - 1 && y == n - 1) {
                return pathLength;
            }

            // Explore all neighbors
            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                // Check if the neighbor is valid
                if (newX >= 0 && newX < n && newY >= 0 && newY < n
                        && grid[newX][newY] == 0 && !visited[newX][newY]) {
                    queue.add(new int[] { newX, newY, pathLength + 1 });
                    /** NOTE !!!  update seen status */
                    visited[newX][newY] = true;
                }
            }
        }

        // If no path is found
        return -1;
    }


    // V1
    // https://leetcode.com/problems/shortest-path-in-binary-matrix/solutions/2043319/why-use-bfs-search-every-possible-path-v-aaov/
    // IDEA: BFS
    public int shortestPathBinaryMatrix_1(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return -1;
        }

        int ans = 0;

        int row = grid.length;
        int col = grid[0].length;

        if (grid[0][0] == 1 || grid[row - 1][col - 1] == 1) {
            return -1;
        }

        int[][] dirs = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };

        boolean[][] visited = new boolean[row][col];

        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[] { 0, 0 });
        visited[0][0] = true;

        while (!queue.isEmpty()) {
            int size = queue.size();
            ans++;

            for (int i = 0; i < size; i++) {
                int[] curPos = queue.poll();

                if (curPos[0] == row - 1 && curPos[1] == col - 1) {
                    return ans;
                }

                for (int[] dir : dirs) {
                    int nextX = curPos[0] + dir[0];
                    int nextY = curPos[1] + dir[1];

                    if (nextX < 0 || nextX >= row || nextY < 0 || nextY >= col || visited[nextX][nextY]
                            || grid[nextX][nextY] == 1) {
                        continue;
                    }

                    visited[nextX][nextY] = true;
                    queue.offer(new int[] { nextX, nextY });
                }
            }
        }

        return -1;
    }

    // V2
    // IDEA: BFS
    // https://leetcode.com/problems/shortest-path-in-binary-matrix/solutions/1541087/easy-and-clear-java-bfs-solution-with-ex-yeqm/
    public int shortestPathBinaryMatrix_2(int[][] grid) {

        // initialization for 8 directions, a map(map is the name, it is a Queue for
        // BFS) and row-column boundaries
        int[][] dir = { { 1, 1 }, { 1, 0 }, { 0, 1 }, { 1, -1 }, { -1, 1 }, { -1, 0 }, { 0, -1 }, { -1, -1 } };
        Queue<int[]> map = new LinkedList<>();
        int rMax = grid.length;
        int cMax = grid[0].length;

        // if start point is blocked, return -1, otherwise give map the start point
        if (grid[0][0] == 1)
            return -1;

        // first two parameters are coordinates, third keep track of the distance
        map.offer(new int[] { 0, 0, 1 });

        while (!map.isEmpty()) {
            // get current coordinates and distance travelled
            int[] location = map.poll();
            int r = location[0];
            int c = location[1];
            int distance = location[2];

            // return if reaches the destination
            if (r == rMax - 1 && c == cMax - 1)
                return distance;

            // search 8 directions for unexplored points around current point
            for (int[] d : dir) {
                int r2 = r + d[0];
                int c2 = c + d[1];
                if (r2 < rMax && r2 >= 0 && c2 < cMax && c2 >= 0 && grid[r2][c2] == 0) {
                    // add unexplored point to map and increment distance by 1
                    map.offer(new int[] { r2, c2, distance + 1 });
                    // set this point to 1 as explored
                    grid[r2][c2] = 1;
                }
            }
        }
        // whole space searched, cannot reach destination
        return -1;
    }

    // V3
    // IDEA: BFS
    // https://leetcode.com/problems/shortest-path-in-binary-matrix/solutions/3584016/java-bfs-beats-70-18-lines-clean-code-by-w3hy/
//    public int shortestPathBinaryMatrix(int[][] grid) {
//        if (grid[0][0] == 1)
//            return -1;
//
//        var moves = new int[][] { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 },
//                { 1, 1 } };
//        var n = grid.length;
//        var seen = new boolean[n][n];
//        var queue = new ArrayDeque<int[]>();
//        queue.offer(new int[] { 0, 0 });
//
//        for (var cnt = 1; !queue.isEmpty(); cnt++) {
//            for (var i = queue.size(); i > 0; i--) {
//                var cell = queue.poll();
//
//                if (cell[0] == n - 1 && cell[1] == n - 1)
//                    return cnt;
//
//                for (var move : moves) {
//                    var x = cell[0] + move[0];
//                    var y = cell[1] + move[1];
//
//                    if (x >= 0 && x < n && y >= 0 && y < n && !seen[x][y] && grid[x][y] == 0) {
//                        seen[x][y] = true;
//                        queue.offer(new int[] { x, y });
//                    }
//                }
//            }
//        }
//        return -1;
//    }

    // V4
    // IDEA: DFS
    // https://leetcode.com/problems/shortest-path-in-binary-matrix/solutions/313937/posting-my-java-dfs-solution-54ms-to-dem-hi3l/
    // Transfer the dist value at (r,c) to or from neighbor cells.
    // Whenever a cell has a updated (smaller) dist value, a recursive call of
    // grow() will be done on behalf of it.
    private void grow(int[][] grid, int[][] dist, int r, int c) {
        int m = grid.length, n = grid[0].length;
        int d0 = dist[r][c];
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0)
                    continue;
                int x = r + i;
                int y = c + j;
                if (x >= 0 && x < m && y >= 0 && y < n) {
                    if (grid[x][y] == 1)
                        continue;
                    int d1 = dist[x][y];
                    if (d1 < d0 - 1) { // get a smaller value from a neighbor; then re-start the process.
                        dist[r][c] = d1 + 1;
                        grow(grid, dist, r, c); // TODO some optimization to avoid stack overflow
                        return;
                    } else if (d1 > d0 + 1) { // give a smaller value to a neighbor
                        dist[x][y] = d0 + 1;
                        grow(grid, dist, x, y);
                    }
                }
            }
        }
    }

    public int shortestPathBinaryMatrix_4(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] dist = new int[m][n]; // dist[i][j]: distance of the cell (i,j) to (0,0)
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = Integer.MAX_VALUE;
            }
        }
        dist[0][0] = 1;
        if (grid[0][0] == 1 || grid[m - 1][n - 1] == 1)
            return -1;
        grow(grid, dist, 0, 0);
        return (dist[m - 1][n - 1] != Integer.MAX_VALUE ? dist[m - 1][n - 1] : -1);
    }



}
