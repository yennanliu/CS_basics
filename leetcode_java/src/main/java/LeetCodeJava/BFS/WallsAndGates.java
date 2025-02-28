package LeetCodeJava.BFS;

// https://leetcode.com/problems/walls-and-gates/description/
// https://leetcode.ca/all/286.html
/**
 * 286. Walls and Gates
 * You are given a m x n 2D grid initialized with these three possible values.
 *
 * -1 - A wall or an obstacle.
 * 0 - A gate.
 * INF - Infinity means an empty room. We use the value 231 - 1 = 2147483647 to represent INF as you may assume that the distance to a gate is less than 2147483647.
 * Fill each empty room with the distance to its nearest gate. If it is impossible to reach a gate, it should be filled with INF.
 *
 * Example:
 *
 * Given the 2D grid:
 *
 * INF  -1  0  INF
 * INF INF INF  -1
 * INF  -1 INF  -1
 *   0  -1 INF INF
 * After running your function, the 2D grid should be:
 *
 *   3  -1   0   1
 *   2   2   1  -1
 *   1  -1   2  -1
 *   0  -1   3   4
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Bloomberg ByteDance Facebook Google Microsoft Uber
 *
 */
import java.util.*;

public class WallsAndGates {

    // V0
    // IDEA : BFS
    public void wallsAndGates(int[][] rooms) {

        class Pair<U, V, W> {
            U key;
            V value;
            W value2;

            Pair(U key, V value, W value2) {
                this.key = key;
                this.value = value;
                this.value2 = value2;
            }

            U getKey() {
                return this.key;
            }

            V getValue() {
                return this.value;
            }

            W getValue2() {
                return this.value2;
            }

        }

        // edge case
        if (rooms.length == 1 && rooms[0].length == 1) {
            return;
        }

        int space_cnt = 0;
        int gete_cnt = 0;
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        int len = rooms.length;
        int width = rooms[0].length;

        // init queue
        Queue<Pair> q = new LinkedList<Pair>();

        // get cnt
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < len; j++) {
                if (rooms[j][i] == 0) {
                    gete_cnt += 1;
                    // NOTE !!! we should do BFS with "gete" instead of space point
                    q.add(new Pair(i, j, -1));
                } else if (rooms[j][i] == 2147483647) {
                    space_cnt += 1;
                    //q.add(new Pair(i, j, -1)); // append space point to queue, for BFS
                } else {
                    //obstacle_cnt += 1;
                }
            }
        }

        // if there is no gate or no space -> quit directly
        if (gete_cnt == 0 || space_cnt == 0) {
            return;
        }

        // bfs
        while (!q.isEmpty()) {

            Pair p = q.poll();
            int x = (int) p.getKey();
            int y = (int) p.getValue();
            int dist = (int) p.getValue2();

            for (int[] dir : dirs) {

                int dx = dir[0];
                int dy = dir[1];
                int new_x = x + dx;
                int new_y = y + dy;

                String idx = new_x + "-" + new_x;

                if (0 <= new_x && new_x < width && 0 <= new_x && new_y < len) {

                    // NOTE !!! don't use logic in commented code
//                    if (rooms[new_y][new_x] == 0 && !seen.contains(idx)) {
//                        rooms[new_y][new_x] = dist;
//                        seen.add(idx);
//                    } else {
//                        dist += 1;
//                        q.add(new Pair(new_x, new_y, dist));
//                    }

                    // NOTE !!! we do NOTHING if point is out of border or point is "NOT a space"
                    if (new_x < 0 || new_x > width || new_y < 0 || new_y > len || rooms[new_y][new_x] != 2147483647) {
                        continue;
                    }
                    rooms[new_y][new_x] = rooms[y][x] + 1;
                    q.add(new Pair(new_x, new_y, dist + 1));
                }

            }
        }
    }

    // V-1-1
    // https://neetcode.io/problems/islands-and-treasure
    // IDEA: Brute Force (Backtracking)
    private int[][] directions = {{1, 0}, {-1, 0},
            {0, 1}, {0, -1}};
    private int INF = 2147483647;
    private boolean[][] visit;
    private int ROWS, COLS;

    private int dfs(int[][] grid, int r, int c) {
        if (r < 0 || c < 0 || r >= ROWS ||
                c >= COLS || grid[r][c] == -1 || visit[r][c]) {
            return INF;
        }
        if (grid[r][c] == 0) {
            return 0;
        }
        visit[r][c] = true;
        int res = INF;
        for (int[] dir : directions) {
            int cur = dfs(grid, r + dir[0], c + dir[1]);
            if (cur != INF) {
                res = Math.min(res, 1 + cur);
            }
        }
        visit[r][c] = false;
        return res;
    }

    public void islandsAndTreasure_1_1(int[][] grid) {
        ROWS = grid.length;
        COLS = grid[0].length;
        visit = new boolean[ROWS][COLS];

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (grid[r][c] == INF) {
                    grid[r][c] = dfs(grid, r, c);
                }
            }
        }
    }


    // V-1-2
    // https://neetcode.io/problems/islands-and-treasure
    // IDEA: BFS
//    private int[][] directions = {{1, 0}, {-1, 0},
//            {0, 1}, {0, -1}};
//    private int INF = 2147483647;
//    private int ROWS, COLS;

    private int bfs(int[][] grid, int r, int c) {
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{r, c});
        boolean[][] visit = new boolean[ROWS][COLS];
        /** NOTE !!! we use visit to avoid duplicated visiting */
        visit[r][c] = true;
        int steps = 0;

        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                int[] curr = q.poll();
                int row = curr[0], col = curr[1];
                if (grid[row][col] == 0) return steps;
                for (int[] dir : directions) {
                    int nr = row + dir[0], nc = col + dir[1];
                    /** NOTE !!!
                     *
                     *  one of the not-stop condition is `grid[nr][nc] != -1`
                     *  -> e.g. if NOT wall (e.g. grid[y][x] == -1), we can move over it
                     *
                     *
                     *  -1 - A wall or an obstacle.
                     */
                    if (nr >= 0 && nr < ROWS && nc >= 0 && nc < COLS &&
                            !visit[nr][nc] && grid[nr][nc] != -1) {
                        visit[nr][nc] = true;
                        q.add(new int[]{nr, nc});
                    }
                }
            }
            steps++;
        }
        return INF;
    }

    public void islandsAndTreasure_1_2(int[][] grid) {
        ROWS = grid.length;
        COLS = grid[0].length;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (grid[r][c] == INF) {
                    grid[r][c] = bfs(grid, r, c);
                }
            }
        }
    }


    // V-1-3
    // https://neetcode.io/problems/islands-and-treasure
    // IDEA: Multi Source BFS
    public void islandsAndTreasure_1_3(int[][] grid) {
        Queue<int[]> q = new LinkedList<>();
        int m = grid.length;
        int n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0) {
                    q.add(new int[] { i, j });
                }
            }
        }
        if (q.size() == 0) return;

        int[][] dirs = { { -1, 0 }, { 0, -1 },
                { 1, 0 }, { 0, 1 } };
        while (!q.isEmpty()) {
            int[] node = q.poll();
            int row = node[0];
            int col = node[1];
            for (int[] dir : dirs) {
                int r = row + dir[0];
                int c = col + dir[1];
                if (r >= m || c >= n || r < 0 ||
                        c < 0 || grid[r][c] != Integer.MAX_VALUE) {
                    continue;
                }
                q.add(new int[] { r, c });

                grid[r][c] = grid[row][col] + 1;
            }
        }
    }

    // V2
    // IDEA BFS
    // https://leetcode.com/problems/walls-and-gates/editorial/
    private static final int EMPTY = Integer.MAX_VALUE;
    private static final int GATE = 0;
    private static final List<int[]> DIRECTIONS = Arrays.asList(
            new int[]{1, 0},
            new int[]{-1, 0},
            new int[]{0, 1},
            new int[]{0, -1}
    );

    public void wallsAndGates_2(int[][] rooms) {
        int m = rooms.length;
        if (m == 0) return;
        int n = rooms[0].length;
        Queue<int[]> q = new LinkedList<>();
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                if (rooms[row][col] == GATE) {
                    q.add(new int[]{row, col});
                }
            }
        }
        while (!q.isEmpty()) {
            int[] point = q.poll();
            int row = point[0];
            int col = point[1];
            for (int[] direction : DIRECTIONS) {
                int r = row + direction[0];
                int c = col + direction[1];
                if (r < 0 || c < 0 || r >= m || c >= n || rooms[r][c] != EMPTY) {
                    continue;
                }
                rooms[r][c] = rooms[row][col] + 1;
                q.add(new int[]{r, c});
            }
        }
    }

    // V2
    // IDEA BRUTE FORCE (TLE)
    // https://leetcode.com/problems/walls-and-gates/editorial/
//    private static final int EMPTY = Integer.MAX_VALUE;
//    private static final int GATE = 0;
//    private static final int WALL = -1;
//    private static final List<int[]> DIRECTIONS = Arrays.asList(
//            new int[] { 1,  0},
//            new int[] {-1,  0},
//            new int[] { 0,  1},
//            new int[] { 0, -1}
//    );
//
//    public void wallsAndGates_2(int[][] rooms) {
//        if (rooms.length == 0) return;
//        for (int row = 0; row < rooms.length; row++) {
//            for (int col = 0; col < rooms[0].length; col++) {
//                if (rooms[row][col] == EMPTY) {
//                    rooms[row][col] = distanceToNearestGate(rooms, row, col);
//                }
//            }
//        }
//    }
//
//    private int distanceToNearestGate(int[][] rooms, int startRow, int startCol) {
//        int m = rooms.length;
//        int n = rooms[0].length;
//        int[][] distance = new int[m][n];
//        Queue<int[]> q = new LinkedList<>();
//        q.add(new int[] { startRow, startCol });
//        while (!q.isEmpty()) {
//            int[] point = q.poll();
//            int row = point[0];
//            int col = point[1];
//            for (int[] direction : DIRECTIONS) {
//                int r = row + direction[0];
//                int c = col + direction[1];
//                if (r < 0 || c < 0 || r >= m || c >= n || rooms[r][c] == WALL
//                        || distance[r][c] != 0) {
//                    continue;
//                }
//                distance[r][c] = distance[row][col] + 1;
//                if (rooms[r][c] == GATE) {
//                    return distance[r][c];
//                }
//                q.add(new int[] { r, c });
//            }
//        }
//        return Integer.MAX_VALUE;
//    }

}
