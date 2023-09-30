package LeetCodeJava.DFS;

// https://leetcode.com/problems/max-area-of-island/

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

//
//class Point {
//    int x;
//    int y;
//
//    Point(int x, int y) {
//        this.x = x;
//        this.y = y;
//    }
//}

public class MaxAreaOfIsland {

    // TODO : fix below
//    int ans = 0;
//    List<Point> toVisit = Arrays.asList();
//    List<List<Integer>> collected;
//
//    // V0
//    public int maxAreaOfIsland(int[][] grid) {
//
//        int len = grid.length;
//        int width = grid[0].length;
//
//        if (len == 0 && width == 0) {
//            return 0;
//        }
//
//        // collect "1" points
//        for (int i = 0; i < len; i++) {
//            for (int j = 0; j < width; j++) {
//                if (grid[i][j] == 1) {
//                    toVisit.add(new Point(j, i));
//                }
//            }
//        }
//
//        // dfs
//        for (Point point : toVisit) {
//            int tmp = this._help(grid, point.y, point.x, Arrays.asList());
//            this.ans = Math.max(this.ans, tmp);
//        }
//        return this.ans;
//    }
//
//    private int _help(int[][] grid, int x, int y, List<Integer> tmp) {
//
//        int len = grid.length;
//        int width = grid[0].length;
//
//        if (grid[y][x] != 1) {
//            return 0;
//        }
//
//        if (x >= width || y >= len) {
//            return 0;
//        }
//
//        // mark as visit
//        grid[y][x] = -1;
//        // double check??
//        tmp.add(1);
//        _help(grid, x + 1, y, tmp);
//        _help(grid, x - 1, y, tmp);
//        _help(grid, x, y + 1, tmp);
//        _help(grid, x, y - 1, tmp);
//
//        return tmp.size();
//    }

    // V1
    // IDEA : DFS (recursive)
    // https://leetcode.com/problems/max-area-of-island/editorial/
    int[][] grid;
    boolean[][] seen;

    public int area(int r, int c) {
        if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length ||
                seen[r][c] || grid[r][c] == 0)
            return 0;
        seen[r][c] = true;
        return (1 + area(r+1, c) + area(r-1, c)
                + area(r, c-1) + area(r, c+1));
    }

    public int maxAreaOfIsland_1(int[][] grid) {
        this.grid = grid;
        seen = new boolean[grid.length][grid[0].length];
        int ans = 0;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                ans = Math.max(ans, area(r, c));
            }
        }
        return ans;
    }

    // V2
    // IDEA : DFS (iterative)
    // https://leetcode.com/problems/max-area-of-island/editorial/
    public int maxAreaOfIsland_2(int[][] grid) {
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
