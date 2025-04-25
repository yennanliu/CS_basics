package LeetCodeJava.DFS;

// https://leetcode.com/problems/max-area-of-island/
/**
 * 695. Max Area of Island
 * Solved
 * Medium
 * Topics
 * Companies
 * You are given an m x n binary matrix grid. An island is a group of 1's (representing land) connected 4-directionally (horizontal or vertical.) You may assume all four edges of the grid are surrounded by water.
 *
 * The area of an island is the number of cells with a value 1 in the island.
 *
 * Return the maximum area of an island in grid. If there is no island, return 0.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid = [[0,0,1,0,0,0,0,1,0,0,0,0,0],[0,0,0,0,0,0,0,1,1,1,0,0,0],[0,1,1,0,1,0,0,0,0,0,0,0,0],[0,1,0,0,1,1,0,0,1,0,1,0,0],[0,1,0,0,1,1,0,0,1,1,1,0,0],[0,0,0,0,0,0,0,0,0,0,1,0,0],[0,0,0,0,0,0,0,1,1,1,0,0,0],[0,0,0,0,0,0,0,1,1,0,0,0,0]]
 * Output: 6
 * Explanation: The answer is not 11, because the island must be connected 4-directionally.
 * Example 2:
 *
 * Input: grid = [[0,0,0,0,0,0,0,0]]
 * Output: 0
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 50
 * grid[i][j] is either 0 or 1.
 *
 */
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MaxAreaOfIsland {

    // V0
    // IDEA : DFS (modified by gpt)
    int biggestArea = 0;

    public int maxAreaOfIsland(int[][] grid) {
        int l = grid.length;
        int w = grid[0].length;

        for (int i = 0; i < l; i++) {
            for (int j = 0; j < w; j++) {
                if (grid[i][j] == 1) {
                    /** NOTE !!!
                     *
                     *  Reset curArea for each new island found
                     *
                     *  Get cur area in each getBiggestArea call,
                     *  then get max by comparing with biggestArea
                     */
                    int curArea = getBiggestArea(j, i, grid);
                    biggestArea = Math.max(biggestArea, curArea);
                }
            }
        }
        return biggestArea;
    }

    public int getBiggestArea(int x, int y, int[][] grid) {

        int l = grid.length;
        int w = grid[0].length;

        // NOTE !!! below check if optional
//        if (x < 0 || x >= w || y < 0 || y >= l || grid[y][x] != 1) {
//            return 0;
//        }

        grid[y][x] = -1; // mark as visited
        int area = 1; // current cell

        int[][] dirs = { {0, 1}, {0, -1}, {1, 0}, {-1, 0} };
        for (int[] dir : dirs) {
            int x_ = x + dir[0];
            int y_ = y + dir[1];
            if (x_ >= 0 && x_ < w && y_ >= 0 && y_ < l && grid[y_][x_] == 1) {
                area += getBiggestArea(x_, y_, grid);
            }
        }

        /** NOTE !!!
         *
         *  Need to return current area,
         *
         *  so, in the main func, can get current area as output
         *  in x, y direction transverse
         *
         *  (int curArea = getBiggestArea(j, i, grid);)
         *
         */
        return area;
    }

    // V0-1
    // IDEA : DFS
    int maxArea = 0;
    // NOTE !!! we NEED to use boolean instead of BOOLEAN,
    //          since boolean' default value is "false", BOOLEAN 's default value is "null"
    boolean[][] seen;

    public int maxAreaOfIsland_0_1(int[][] grid) {

        int len = grid.length;
        int width = grid[0].length;

        // edge case
        if (grid.length == 1 && grid[0].length == 1){
            if (grid[0][0] == 0){
                return 0;
            }
            return 1;
        }

        // NOTE !!! we use below to init a M X N matrix
        this.seen = new boolean[grid.length][grid[0].length];

        for (int i = 0; i < len; i++){
            for (int j = 0; j < width; j++){
                //System.out.println("i = " + i + " j = " + j);
                if (grid[i][j] == 1){
                    int _area = _getArea(grid, this.seen, j, i);
                    //System.out.println("_area = " + _area);
                    this.maxArea = Math.max(this.maxArea, _area);
                }
            }
        }
        return this.maxArea;
    }

    private int _getArea(int[][] grid, boolean[][] seen, int x, int y){

        int len = grid.length;
        int width = grid[0].length;

        if (x < 0 || x >= width || y < 0 || y >= len || seen[y][x] == true || grid[y][x] == 0){
            return 0;
        }

        seen[y][x] = true;

        return 1 + _getArea(grid, seen, x+1, y) +
                _getArea(grid, seen, x-1, y) +
                _getArea(grid, seen, x, y+1) +
                _getArea(grid, seen, x, y-1);
    }

    // V0-2
    // IDEA: DFS (gpt)
    public int maxAreaOfIsland_0_2(int[][] grid) {
        // Edge case
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int rows = grid.length;
        int cols = grid[0].length;
        int maxArea = 0;

        // Iterate through the grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 1) {
                    maxArea = Math.max(maxArea, findMaxArea(grid, i, j));
                }
            }
        }

        return maxArea;
    }

    private int findMaxArea(int[][] grid, int row, int col) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Boundary and condition check
        if (row < 0 || col < 0 || row >= rows || col >= cols || grid[row][col] != 1) {
            return 0;
        }

        // Mark as visited
        grid[row][col] = -1;

        /**
         *  NOTE !!!
         *
         *   now that below we `sum up all 4 directions return value`,
         *   since it can visit 4 directions freely at each (x,y)
         *
         *   (area is findMaxArea method return val)
         */
        // Explore all four directions and accumulate area
        return 1 + findMaxArea(grid, row + 1, col)
                + findMaxArea(grid, row - 1, col)
                + findMaxArea(grid, row, col + 1)
                + findMaxArea(grid, row, col - 1);
    }

    // V1-1
    // https://neetcode.io/problems/max-area-of-island
    // IDEA:  DFS
    private static final int[][] directions = {{1, 0}, {-1, 0},
            {0, 1}, {0, -1}};

    public int maxAreaOfIsland_1_1(int[][] grid) {
        int ROWS = grid.length, COLS = grid[0].length;
        int area = 0;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (grid[r][c] == 1) {
                    area = Math.max(area, dfs(grid, r, c));
                }
            }
        }

        return area;
    }

    private int dfs(int[][] grid, int r, int c) {
        if (r < 0 || c < 0 || r >= grid.length ||
                c >= grid[0].length || grid[r][c] == 0) {
            return 0;
        }

        grid[r][c] = 0;
        int res = 1;
        for (int[] dir : directions) {
            res += dfs(grid, r + dir[0], c + dir[1]);
        }
        return res;
    }


    // V1-2
    // https://neetcode.io/problems/max-area-of-island
    // IDEA: BFS
//    private static final int[][] directions = {{1, 0}, {-1, 0},
//            {0, 1}, {0, -1}};

    public int maxAreaOfIsland_1_2(int[][] grid) {
        int ROWS = grid.length, COLS = grid[0].length;
        int area = 0;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (grid[r][c] == 1) {
                    area = Math.max(area, bfs(grid, r, c));
                }
            }
        }

        return area;
    }

    private int bfs(int[][] grid, int r, int c) {
        Queue<int[]> q = new LinkedList<>();
        grid[r][c] = 0;
        q.add(new int[]{r, c});
        int res = 1;

        while (!q.isEmpty()) {
            int[] node = q.poll();
            int row = node[0], col = node[1];

            for (int[] dir : directions) {
                int nr = row + dir[0], nc = col + dir[1];
                if (nr >= 0 && nc >= 0 && nr < grid.length &&
                        nc < grid[0].length && grid[nr][nc] == 1) {
                    q.add(new int[]{nr, nc});
                    grid[nr][nc] = 0;
                    res++;
                }
            }
        }
        return res;
    }


    // V2
    // IDEA : DFS (recursive)
    // https://leetcode.com/problems/max-area-of-island/editorial/
    int[][] grid;
    boolean[][] _seen;

    public int area(int r, int c) {
        if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length ||
                _seen[r][c] || grid[r][c] == 0)
            return 0;
        _seen[r][c] = true;
        return (1 + area(r+1, c) + area(r-1, c)
                + area(r, c-1) + area(r, c+1));
    }

    public int maxAreaOfIsland_2(int[][] grid) {
        this.grid = grid;
        _seen = new boolean[grid.length][grid[0].length];
        int ans = 0;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                ans = Math.max(ans, area(r, c));
            }
        }
        return ans;
    }

    // V3
    // IDEA : DFS (iterative)
    // https://leetcode.com/problems/max-area-of-island/editorial/
    public int maxAreaOfIsland_3(int[][] grid) {
        boolean[][] seen = new boolean[grid.length][grid[0].length];
        int[] dr = new int[]{1, -1, 0, 0};
        int[] dc = new int[]{0, 0, 1, -1};

        int ans = 0;
        for (int r0 = 0; r0 < grid.length; r0++) {
            for (int c0 = 0; c0 < grid[0].length; c0++) {
                if (grid[r0][c0] == 1 && !seen[r0][c0]) {
                    int shape = 0;
                    Stack<int[]> stack = new Stack();
                    stack.push(new int[]{r0, c0});
                    seen[r0][c0] = true;
                    while (!stack.empty()) {
                        int[] node = stack.pop();
                        int r = node[0], c = node[1];
                        shape++;
                        for (int k = 0; k < 4; k++) {
                            int nr = r + dr[k];
                            int nc = c + dc[k];
                            if (0 <= nr && nr < grid.length &&
                                    0 <= nc && nc < grid[0].length &&
                                    grid[nr][nc] == 1 && !seen[nr][nc]) {
                                stack.push(new int[]{nr, nc});
                                seen[nr][nc] = true;
                            }
                        }
                    }
                    ans = Math.max(ans, shape);
                }
            }
        }
        return ans;
    }

}
