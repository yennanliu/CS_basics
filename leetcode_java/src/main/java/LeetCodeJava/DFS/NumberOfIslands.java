package LeetCodeJava.DFS;

// https://leetcode.com/problems/number-of-islands/

import java.util.*;

public class NumberOfIslands {

    // V0
    // IDEA : DFS
    // TODO : fix below
//    int ans = 0;
//    public int numIslands(char[][] grid) {
//
//        if (grid.length == 0 && grid[0].length == 0){
//            return 0;
//        }
//
//        int _len = grid.length;
//        int _width = grid[0].length;
//
//        // get all "1"
//        List<List<Integer>> toVisit = new ArrayList();
//        for (int i = 0; i < _width; i++){
//            for (int j = 0; j < _len; j++){
//                String val = String.valueOf(grid[j][i]);
//                if (val == "1"){
//                    toVisit.add(Arrays.asList(j, i));
//                }
//            }
//        }
//
//        // dfs
//        for (List<Integer> point : toVisit){
//            int _x = point.get(1);
//            int _y = point.get(0);
//            if (_dfs(grid, _x, _y)){
//                this.ans += 1;
//            }
//        }
//
//        return this.ans;
//    }
//
//    private boolean _dfs(char[][] grid, int x, int y){
//
//        int _len = grid.length;
//        int _width = grid[0].length;
//
//        String val = String.valueOf(grid[y][x]);
//        if (val == "#" || val == "0"){
//            return false;
//        }
//
//        List<List<Integer>> diresctions = new ArrayList();
//        diresctions.add(Arrays.asList(0, 1));
//        diresctions.add(Arrays.asList(0, -1));
//        diresctions.add(Arrays.asList(1, 0));
//        diresctions.add(Arrays.asList(-1, 0));
//
//        for (List<Integer> direction : diresctions){
//
//            x += direction.get(1);
//            y += direction.get(0);
//
//            if (x >= 0 && x < _width && y >= 0 && y < _len){
//                // https://stackoverflow.com/questions/5859934/char-initial-value-in-java
//                grid[y][x] = '#';
//                _dfs(grid, x, y);
//            }
//
//        }
//        return true;
//    }

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
