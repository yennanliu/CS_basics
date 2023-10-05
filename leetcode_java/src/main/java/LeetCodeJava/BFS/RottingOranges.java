package LeetCodeJava.BFS;

// https://leetcode.com/problems/rotting-oranges/

import java.util.*;

public class RottingOranges {

    // TODO : fix below
    // V0
    // IDEA : BFS
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Breadth-First-Search/rotting-oranges.py
//    public int orangesRotting(int[][] grid) {
//
//        class Pair<U, V, W> {
//
//            U key;
//            V value;
//            W value2;
//
//            Pair(U key, V value, W value2) {
//                this.key = key;
//                this.value = value;
//                this.value2 = value2;
//            }
//
//            public U getKey() {
//                return this.key;
//            }
//
//            public V getValue() {
//                return this.value;
//            }
//
//            public W getValue2() {
//                return this.value2;
//            }
//
//        }
//
//        // Queue<Pair<Integer, Integer>> queue = new ArrayDeque();
//        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
//        int fresh = 0;
//        Queue<Pair<Integer, Integer, Integer>> rotting = new LinkedList<>();
//        HashSet<String> set = new HashSet<>();
//
//        int _len = grid.length;
//        int _width = grid[0].length;
//
//        for (int i = 0; i < _width; i++) {
//            for (int j = 0; j < _len; j++) {
//                if (grid[j][i] == 2) {
//                    // TODO : check difference : offer VS add
//                    //rotting.offer(new Pair<>(i, j));
//                    rotting.add(new Pair<>(i, j, 0));
//                }
//                if (grid[j][i] == 1) {
//                    fresh += 1;
//                }
//            }
//        }
//
//        if (fresh == 0) {
//            return 0;
//        }
//
//        // bfs
//        while (!rotting.isEmpty()) {
//
//            Pair<Integer, Integer, Integer> _cur = rotting.poll();
//            int x = _cur.getKey();
//            int y = _cur.getValue();
//            int _time = _cur.getValue2();
//
//            // go 4 directions
//            for (int[] dir : dirs) {
//                int dx = dir[0];
//                int dy = dir[1];
//                int _x = x + dx;
//                int _y = y + dy;
//                String idx = String.valueOf(x) + "-" + String.valueOf(y); // to avoid redo op (fresh -= 1, rotting.add) "visited" points
//                if (_x >= 0 && x < _width - 1 && _y >= 0 && _y < _len - 1) {
//                    if (grid[_y][_x] == 1 && !set.contains(idx)){
//                        grid[_y][_x] = 2;
//                        fresh -= 1;
//                        set.add(idx);
//                        System.out.println(">>> fresh = " + fresh + " idx = " + idx);
//                        if (fresh == 0) {
//                            return _time+1;
//                        }
//                        rotting.add(new Pair<>(_x, _y, _time+1));
//                    }
//                }
//            }
//        }
//
//        return fresh == 0 ? 0 : -1;
//    }

    // TODO : fix below (should use BFS instead)
    // V0'
    // IDEA : DFS
//    public int ans = 0;
//    public int fresh = 0;
//
//    List<List<Integer>> rotting = new ArrayList() {};
//    // VO
//    public int orangesRotting(int[][] grid) {
//
//        // collect init rotting oranges
//        //int[][] rotting = new int[][]{};
//        //List<List<Integer>> fresh = new ArrayList(){};
//
//        int len = grid.length;
//        int width = grid[0].length;
//
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < len; j++) {
//                if (grid[j][i] == 2) {
//                    List<Integer> _tmp = new ArrayList() {};
//                    _tmp.add(i);
//                    _tmp.add(j);
//                    this.rotting.add(_tmp);
//                }
//                if (grid[j][i] == 1) {
////                    List<Integer> _tmp2 = new ArrayList(){};
////                    _tmp2.add(i);
////                    _tmp2.add(j);
////                    fresh.add(_tmp2);
//                    this.fresh += 1;
//                }
//            }
//        }
//
//        // if there is no fresh orange, quit directly
//        if (this.fresh == 0) {
//            return 0;
//        }
//
//        // dfs
//        for (List<Integer> point : rotting){
//
//            int x = point.get(0);
//            int y = point.get(1);
//
//            if (this.fresh == 0) {
//                return 0;
//            }
//            _dfs(grid, x, y);
//            this.ans += 1;
//
//        }
//        return this.ans;
//    }
//
//    private void _dfs(int[][] grid, int x, int y) {
//
//        int len = grid.length;
//        int width = grid[0].length;
//
//        if (x < 0 || x >= width || y < 0 || y >= len || grid[y][x] == 0) {
//            return;
//        }
//
//        if (grid[y][x] == 1) {
//            List<Integer> _tmp = new ArrayList() {};
//            _tmp.add(x);
//            _tmp.add(y);
//            this.rotting.add(_tmp);
//            grid[y][x] = 2;
//            this.fresh -= 1;
//        }
//
//        _dfs(grid, x + 1, y);
//        _dfs(grid, x - 1, y);
//        _dfs(grid, x, y + 1);
//        _dfs(grid, x, y - 1);
//    }

    // V1
    // IDEA : BFS
    // https://leetcode.com/problems/rotting-oranges/editorial/
    class Pair<U, V> {

        U key;
        V value;

        Pair(U key, V value) {
            this.key = key;
            this.value = value;
        }

        public U getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

    }

    public int orangesRotting_1(int[][] grid) {
        Queue<Pair<Integer, Integer>> queue = new ArrayDeque();

        // Step 1). build the initial set of rotten oranges
        int freshOranges = 0;
        int ROWS = grid.length, COLS = grid[0].length;

        for (int r = 0; r < ROWS; ++r)
            for (int c = 0; c < COLS; ++c)
                if (grid[r][c] == 2)
                    queue.offer(new Pair(r, c));
                else if (grid[r][c] == 1)
                    freshOranges++;

        // Mark the round / level, _i.e_ the ticker of timestamp
        queue.offer(new Pair(-1, -1));

        // Step 2). start the rotting process via BFS
        int minutesElapsed = -1;
        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        while (!queue.isEmpty()) {
            Pair<Integer, Integer> p = queue.poll();
            int row = p.getKey();
            int col = p.getValue();
            if (row == -1) {
                // We finish one round of processing
                minutesElapsed++;
                // to avoid the endless loop
                if (!queue.isEmpty())
                    queue.offer(new Pair(-1, -1));
            } else {
                // this is a rotten orange
                // then it would contaminate its neighbors
                for (int[] d : directions) {
                    int neighborRow = row + d[0];
                    int neighborCol = col + d[1];
                    if (neighborRow >= 0 && neighborRow < ROWS &&
                            neighborCol >= 0 && neighborCol < COLS) {
                        if (grid[neighborRow][neighborCol] == 1) {
                            // this orange would be contaminated
                            grid[neighborRow][neighborCol] = 2;
                            freshOranges--;
                            // this orange would then contaminate other oranges
                            queue.offer(new Pair(neighborRow, neighborCol));
                        }
                    }
                }
            }
        }

        // return elapsed minutes if no fresh orange left
        return freshOranges == 0 ? minutesElapsed : -1;
    }

    // V2
    // IDEA : in place BFS
    // https://leetcode.com/problems/rotting-oranges/editorial/
    // run the rotting process, by marking the rotten oranges with the timestamp
    public boolean runRottingProcess(int timestamp, int[][] grid, int ROWS, int COLS) {
        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        // flag to indicate if the rotting process should be continued
        boolean toBeContinued = false;
        for (int row = 0; row < ROWS; ++row)
            for (int col = 0; col < COLS; ++col)
                if (grid[row][col] == timestamp)
                    // current contaminated cell
                    for (int[] d : directions) {
                        int nRow = row + d[0], nCol = col + d[1];
                        if (nRow >= 0 && nRow < ROWS && nCol >= 0 && nCol < COLS)
                            if (grid[nRow][nCol] == 1) {
                                // this fresh orange would be contaminated next
                                grid[nRow][nCol] = timestamp + 1;
                                toBeContinued = true;
                            }
                    }
        return toBeContinued;
    }

    public int orangesRotting_2(int[][] grid) {
        int ROWS = grid.length, COLS = grid[0].length;
        int timestamp = 2;
        while (runRottingProcess(timestamp, grid, ROWS, COLS))
            timestamp++;

        // end of process, to check if there are still fresh oranges left
        for (int[] row : grid)
            for (int cell : row)
                // still got a fresh orange left
                if (cell == 1)
                    return -1;


        // return elapsed minutes if no fresh orange left
        return timestamp - 2;
    }

}
