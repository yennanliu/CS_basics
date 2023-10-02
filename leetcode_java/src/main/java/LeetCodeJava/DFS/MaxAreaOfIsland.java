package LeetCodeJava.DFS;

// https://leetcode.com/problems/max-area-of-island/

import java.util.Stack;

public class MaxAreaOfIsland {

    // V0
    // IDEA : DFS
    int maxArea = 0;
    // NOTE !!! we NEED to use boolean instead of BOOLEAN,
    //          since boolean' default value is "false", BOOLEAN 's default value is "null"
    boolean[][] seen;

    public int maxAreaOfIsland(int[][] grid) {

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

    // V1
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

    public int maxAreaOfIsland_1(int[][] grid) {
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
