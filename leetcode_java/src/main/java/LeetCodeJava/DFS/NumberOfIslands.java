package LeetCodeJava.DFS;

// https://leetcode.com/problems/number-of-islands/
/**
 *  200. Number of Islands
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an m x n 2D binary grid grid which represents a map of '1's (land) and '0's (water), return the number of islands.
 *
 * An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically. You may assume all four edges of the grid are all surrounded by water.
 *
 *
 *
 * Example 1:
 *
 * Input: grid = [
 *   ["1","1","1","1","0"],
 *   ["1","1","0","1","0"],
 *   ["1","1","0","0","0"],
 *   ["0","0","0","0","0"]
 * ]
 * Output: 1
 * Example 2:
 *
 * Input: grid = [
 *   ["1","1","0","0","0"],
 *   ["1","1","0","0","0"],
 *   ["0","0","1","0","0"],
 *   ["0","0","0","1","1"]
 * ]
 * Output: 3
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 300
 * grid[i][j] is '0' or '1'.
 *
 *
 */
import java.util.*;

public class NumberOfIslands {

    // V0
    // IDEA: DFS (with looping)
    public int numIslands(char[][] grid) {
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
                 *  1) if grid[i][j] == '1', no need to collect the coordinate (x,y),
                 *   -> just add res with 1,
                 *   -> and call dfs function
                 *
                 *   2) if grid[i][j] == '1', we do `res += 1` directly,
                 *   then use dfs update grid values
                 *
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

    /**
     * NOTE !!!
     *
     *  1) we do below checking before for loop & dfs call
     *   -> reason :
     *
     *       You cannot move the checking logic inside the for loop
     *       because it would return too early and stop exploring
     *       other valid directions. Let’s break it down step by step.
     *
     *
     *  2) Why the return inside the for loop is incorrect ?
     *
     *   -> Consider this incorrect version:
     *
     *   int[][] moves = { {0, 1}, {0, -1}, {1, 0}, {-1, 0} };
     *   for (int[] move : moves) {
     *     if (x + move[0] < 0 || x + move[0] >= rows || y + move[1] < 0 || y + move[1] >= cols || grid[x + move[0]][y + move[1]] != '1') {
     *         return; // ❌ WRONG! Stops checking other directions!
     *     }
     *     dfs(grid, x + move[0], y + move[1]);
     * }
     *
     *  -> Issue: `One invalid move causes an early exit` !!!!!
     * 	    •	If any of the four moves is out of bounds or lands on water ('0'), the function returns immediately.
     * 	    •	This prevents it from checking other valid moves.
     *
     */
    if (y < 0 || y >= l || x < 0 || x >= w || grid[y][x] != '1') {
            return;
        }

        /** NOTE !!!
         *
         *  need to mark visited cell, to avoid repetitive visiting
         *  or. can use `boolean visited[][] cache`
         */
        grid[y][x] = '#'; // Mark the cell as visited

        int[][] dirs = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        for (int[] dir : dirs) {
            int newY = y + dir[0];
            int newX = x + dir[1];
            dfs(grid, newY, newX);
        }
    }

    // V0-1
    // IDEA : DFS
    int num_island = 0;
    boolean[][] _seen;
    public int numIslands_0_1(char[][] grid) {

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

        /** NOTE !!! we do 4 direction traverse in sequence order */
        _is_island(grid, x+1, y, seen);
        _is_island(grid, x-1, y, seen);
        _is_island(grid, x, y+1, seen);
        _is_island(grid, x, y-1, seen);

        // NOTE !!! if code can arrive here, means there is at least "1 direction" meet "1" value
        //          -> there is an island
        //          -> so we return true as we found an island
        return true;
    }

    // V0-2
    // IDEA : DFS (with looping) (modified by GPT)
    int num_island_2 = 0;
    boolean[][] _seen_2;

    public int numIslands_0_2(char[][] grid) {
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

    // V0-3
    // IDEA: DFS + MODIFY grid value (fixed by gpt)
    public int numIslands_0_3(char[][] grid) {

        // edge
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        if (grid.length == 1 && grid[0].length == 1) {
            return grid[0][0] == '1' ? 1 : 0;
        }

        int cnt = 0;

        int l = grid.length;
        int w = grid[0].length;


        /**  NOTE !!!
         *
         *  we ONLY need to pick one to avoid `revisit`
         *   -> 1) modify grid val
         *      or
         *     2) use `visited` hashset
         *
         */
        // set : { x-y }
        // HashSet<String> visited = new HashSet<>();

        for (int i = 0; i < l; i++) {
            for (int j = 0; j < w; j++) {
                if (grid[i][j] == '1') {
                    isLandHelper(j, i, grid);
                    cnt += 1;
                }
            }
        }

        return cnt;
    }

    public void isLandHelper(int x, int y, char[][] grid) {

        int l = grid.length;
        int w = grid[0].length;

        int[][] dirs = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

        /** NOTE !!!
         *
         *   we validate `current x, y` first,
         *   ONLY if it's a `validated` point,
         *   then we proceed (e.g.  move up, down, left, right dirs)
         */
        if (x < 0 || x >= w || y < 0 || y >= l || grid[y][x] != '1') {
            return;
        }

        /** NOTE !!!
         *
         *  we `modify` to grid value (x,y)
         *  to avoid revisit
         */

        grid[y][x] = '@'; // ???

        for (int[] d : dirs) {

            int x_ = x + d[0];
            int y_ = y + d[1];

            isLandHelper(x_, y_, grid);
        }
        
    }


    // V1-1
    // https://neetcode.io/problems/count-number-of-islands
    // IDEA: DFS
    private static final int[][] directions = {{1, 0}, {-1, 0},
            {0, 1}, {0, -1}};

    public int numIslands_1_1(char[][] grid) {
        int ROWS = grid.length, COLS = grid[0].length;
        int islands = 0;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (grid[r][c] == '1') {
                    dfs_1_1(grid, r, c);
                    islands++;
                }
            }
        }

        return islands;
    }

    private void dfs_1_1(char[][] grid, int r, int c) {
        if (r < 0 || c < 0 || r >= grid.length ||
                c >= grid[0].length || grid[r][c] == '0') {
            return;
        }

        grid[r][c] = '0';
        for (int[] dir : directions) {
            dfs_1_1(grid, r + dir[0], c + dir[1]);
        }
    }

    // V1-2
    // https://neetcode.io/problems/count-number-of-islands
    // IDEA: BFS
//    private static final int[][] directions = {{1, 0}, {-1, 0},
//            {0, 1}, {0, -1}};

    public int numIslands_1_2(char[][] grid) {
        int ROWS = grid.length, COLS = grid[0].length;
        int islands = 0;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (grid[r][c] == '1') {
                    bfs(grid, r, c);
                    islands++;
                }
            }
        }

        return islands;
    }

    private void bfs(char[][] grid, int r, int c) {
        Queue<int[]> q = new LinkedList<>();
        grid[r][c] = '0';
        q.add(new int[]{r, c});

        while (!q.isEmpty()) {
            int[] node = q.poll();
            int row = node[0], col = node[1];

            for (int[] dir : directions) {
                int nr = row + dir[0], nc = col + dir[1];
                if (nr >= 0 && nc >= 0 && nr < grid.length &&
                        nc < grid[0].length && grid[nr][nc] == '1') {
                    q.add(new int[]{nr, nc});
                    grid[nr][nc] = '0';
                }
            }
        }
    }


    // V2
    // IDEA : DFS
    // https://leetcode.com/problems/number-of-islands/editorial/
    void dfs_2(char[][] grid, int r, int c) {
        int nr = grid.length;
        int nc = grid[0].length;

        if (r < 0 || c < 0 || r >= nr || c >= nc || grid[r][c] == '0') {
            return;
        }

        grid[r][c] = '0';
        dfs_2(grid, r - 1, c);
        dfs_2(grid, r + 1, c);
        dfs_2(grid, r, c - 1);
        dfs_2(grid, r, c + 1);
    }

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
                    dfs_2(grid, r, c);
                }
            }
        }

        return num_islands;
    }

    // V3
    // IDEA : BFS
    // https://leetcode.com/problems/number-of-islands/editorial/
    public int numIslands_3(char[][] grid) {
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

    /**
     *  Follow up:
     *
     *   - https://buildmoat.teachable.com/courses/7a7af3/lectures/62695429
     *
     *   If the grid was originally all water, and then there are multiple updates,
     *   each update will replace a water grid with land. That is, a coordinate (x, y) will
     *   be given to ensure that it was originally water and then converted to land.
     *   After each update, please confirm the current number of islands.
     *
     */
    // IDEA: UNION FIND
    int[] parent;
    static int[] dx = { 0, 0, 1, -1 };
    static int[] dy = { 1, -1, 0, 0 };
    static int directionCount = 4;

    private int findParent(int id) {
        if (parent[id] == id) {
            return id;
        }
        return parent[id] = findParent(parent[id]);
    }

    private boolean union(int x, int y) {
        int parentX = findParent(x);
        int parentY = findParent(y);
        if (parentX != parentY) {
            parent[parentX] = parentY;
            return true;
        }
        return false;
    }

    public ArrayList<Integer> numberOfIslands(int m, int n, int [][] position) {
        int [][] grid = new int[m][n];
        parent = new int[position.length+1];
        int islandsCount = 0;
        ArrayList<Integer> ans = new ArrayList<Integer>();
        for( int i = 0 ; i < position.length ; i++) {
            int id = i + 1;
            parent[id] = id;
            grid[position[i][0]][position[i][1]] = id;
            islandsCount++;
            for(int direction = 0 ; direction < directionCount ; direction++) {
                int targetX = position[i][0] + dx[direction];
                int targetY = position[i][1] + dy[direction];
                if (targetX >= 0 && targetX < m && targetY >=0 && targetY < n && grid[targetX][targetY] != 0) {
                    if(union(id, grid[targetX][targetY])) {
                        islandsCount--;
                    }
                }
            }
            ans.add(islandsCount);
        }
        return ans;
    }



}
