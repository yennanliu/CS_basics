package LeetCodeJava.DFS;

// https://leetcode.com/problems/number-of-islands/

import java.util.*;

public class NumberOfIslands {

    // V0
    // IDEA : DFS
    int num_island = 0;
    boolean[][] _seen;
    public int numIslands(char[][] grid) {

        if (grid.length == 1 && grid[0].length == 1){
            if (grid[0][0] == '1'){
                return 1;
            }
            return 0;
        }

        int len = grid.length;
        int width = grid[0].length;

        // NOTE !!! how we init M X N boolean matrix
        this._seen = new boolean[len][width];

        for (int i = 0; i < len; i++){
            for (int j = 0; j < width; j++){
                if (_is_island(grid, j, i, this._seen)){
                    this.num_island += 1;
                }
            }
        }

        return this.num_island;
    }

    private boolean _is_island(char[][] grid, int x, int y, boolean[][] seen){

        int len = grid.length;
        int width = grid[0].length;

        // NOTE !!! boundary condition :  x >= width, y >= len
        // since index = lenth - 1
        if (x < 0 || x >= width || y < 0 || y >= len || this._seen[y][x] == true || grid[y][x] == '0'){
            return false;
        }

        this._seen[y][x] = true;

        /** NOTE !!! we do 4 direction traverse on the same time */
        _is_island(grid, x+1, y, seen);
        _is_island(grid, x-1, y, seen);
        _is_island(grid, x, y+1, seen);
        _is_island(grid, x, y-1, seen);

        // NOTE !!! if code can arrive here, means there is at least "1 direction" meet "1" value
        //          -> there is an island
        //          -> so we return true as we found an island
        return true;
    }

    // V0'
    // IDEA: DFS (with looping)
    public int numIslands_0(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int l = grid.length;
        int w = grid[0].length;
        int res = 0;

        for (int i = 0; i < l; i++) {
            for (int j = 0; j < w; j++) {
                /**
                 *  NOTE !!!
                 *
                 *   if grid[i][j] == '1', no need to collect the coordinate (x,y),
                 *   -> just add res with 1,
                 *   -> and call dfs function
                 */
                if (grid[i][j] == '1') {
                    res += 1;
                    dfs(grid, i, j);
                }
            }
        }

        return res;
    }

    /** NOTE !!!
     *
     *   NO NEED to return boolean val on this helper function (dfs),
     *   since we mark point as "visited" in place with traversing,
     *   so no response (void) is OK
     */
    private void dfs(char[][] grid, int y, int x) {
        
        int l = grid.length;
        int w = grid[0].length;

        if (y < 0 || y >= l || x < 0 || x >= w || grid[y][x] != '1') {
            return;
        }

        grid[y][x] = '#'; // Mark the cell as visited

        int[][] dirs = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        for (int[] dir : dirs) {
            int newY = y + dir[0];
            int newX = x + dir[1];
            dfs(grid, newY, newX);
        }
    }

    // V0'
    // IDEA : DFS (with looping) (modified by GPT)
    int num_island_2 = 0;
    boolean[][] _seen_2;

    public int numIslands_0_1(char[][] grid) {
        if (grid.length == 1 && grid[0].length == 1) {
            return grid[0][0] == '1' ? 1 : 0;
        }

        int len = grid.length;
        int width = grid[0].length;
        this._seen = new boolean[len][width];

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < width; j++) {
                if (_is_island_2(grid, j, i, this._seen_2)) {
                    this.num_island_2 += 1;
                }
            }
        }

        return this.num_island_2;
    }

    private boolean _is_island_2(char[][] grid, int x, int y, boolean[][] seen) {

        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        int len = grid.length;
        int width = grid[0].length;

        if (x < 0 || x >= width || y < 0 || y >= len || this._seen[y][x] || grid[y][x] == '0') {
            return false;
        }

        this._seen_2[y][x] = true;

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            _is_island(grid, newX, newY, seen);
        }

        return true;
    }



    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/number-of-islands/editorial/
    void dfs_1(char[][] grid, int r, int c) {
        int nr = grid.length;
        int nc = grid[0].length;

        if (r < 0 || c < 0 || r >= nr || c >= nc || grid[r][c] == '0') {
            return;
        }

        grid[r][c] = '0';
        dfs_1(grid, r - 1, c);
        dfs_1(grid, r + 1, c);
        dfs_1(grid, r, c - 1);
        dfs_1(grid, r, c + 1);
    }

    public int numIslands_1(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }

        int nr = grid.length;
        int nc = grid[0].length;
        int num_islands = 0;
        for (int r = 0; r < nr; ++r) {
            for (int c = 0; c < nc; ++c) {
                if (grid[r][c] == '1') {
                    ++num_islands;
                    dfs_1(grid, r, c);
                }
            }
        }

        return num_islands;
    }

    // V2
    // IDEA : BFS
    // https://leetcode.com/problems/number-of-islands/editorial/
    public int numIslands_2(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }

        int nr = grid.length;
        int nc = grid[0].length;
        int num_islands = 0;

        for (int r = 0; r < nr; ++r) {
            for (int c = 0; c < nc; ++c) {
                if (grid[r][c] == '1') {
                    ++num_islands;
                    grid[r][c] = '0'; // mark as visited
                    Queue<Integer> neighbors = new LinkedList<>();
                    neighbors.add(r * nc + c);
                    while (!neighbors.isEmpty()) {
                        int id = neighbors.remove();
                        int row = id / nc;
                        int col = id % nc;
                        if (row - 1 >= 0 && grid[row-1][col] == '1') {
                            neighbors.add((row-1) * nc + col);
                            grid[row-1][col] = '0';
                        }
                        if (row + 1 < nr && grid[row+1][col] == '1') {
                            neighbors.add((row+1) * nc + col);
                            grid[row+1][col] = '0';
                        }
                        if (col - 1 >= 0 && grid[row][col-1] == '1') {
                            neighbors.add(row * nc + col-1);
                            grid[row][col-1] = '0';
                        }
                        if (col + 1 < nc && grid[row][col+1] == '1') {
                            neighbors.add(row * nc + col+1);
                            grid[row][col+1] = '0';
                        }
                    }
                }
            }
        }

        return num_islands;
    }

}
