package LeetCodeJava.BFS;

// https://leetcode.com/problems/rotting-oranges/
/**
 * 994. Rotting Oranges
 * Solved
 * Medium
 * Topics
 * Companies
 * You are given an m x n grid where each cell can have one of three values:
 *
 * 0 representing an empty cell,
 * 1 representing a fresh orange, or
 * 2 representing a rotten orange.
 * Every minute, any fresh orange that is 4-directionally adjacent to a rotten orange becomes rotten.
 *
 * Return the minimum number of minutes that must elapse until no cell has a fresh orange. If this is impossible, return -1.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid = [[2,1,1],[1,1,0],[0,1,1]]
 * Output: 4
 * Example 2:
 *
 * Input: grid = [[2,1,1],[0,1,1],[1,0,1]]
 * Output: -1
 * Explanation: The orange in the bottom left corner (row 2, column 0) is never rotten, because rotting only happens 4-directionally.
 * Example 3:
 *
 * Input: grid = [[0,2]]
 * Output: 0
 * Explanation: Since there are already no fresh oranges at minute 0, the answer is just 0.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 10
 * grid[i][j] is 0, 1, or 2.
 */
import java.util.*;

public class RottingOranges {

    // TODO : fix below
    // VO
    // IDEA : BFS
//    public int orangesRotting(int[][] grid) {
//
//        // edge
//        if(grid.length == 0 || grid[0].length == 0){
//            return 0;
//        }
//
//        if(grid.length == 1 && grid[0].length == 1){
//            if(grid[0][0] == 2){
//                return -1;
//            }
//            return 0; // ??
//        }
//
//        int l = grid.length;
//        int w = grid[0].length;
//
//        int freshCnt = 0;
//        int rottenCnt = 0;
//
//        List<List<Integer>> rottenList = new ArrayList<>();
//
//        // get cnt of `fresh` orange
//        for(int i = 0; i < l; i++){
//            for(int j = 0; j < w; j++){
//                if(grid[i][j] == 1){
//                    freshCnt += 1;
//                }else if (grid[i][j] == 2){
//                    rottenCnt += 1;
//                    List<Integer> tmp = new ArrayList<>();
//                    tmp.add(j);
//                    tmp.add(i);
//                    rottenList.add(tmp);
//                }
//            }
//        }
//
//        if(freshCnt == 0){
//            return 0;
//        }
//
//        int minutes = 0;
//
//        Queue<List<Integer>> q = new LinkedList<>();
//        for(List<Integer> x: rottenList){
//            q.add(x);
//        }
//
//        while(!q.isEmpty() || freshCnt <= 0){
//            List<Integer> cur = q.poll();
//            int x = cur.get(0);
//            int y = cur.get(1);
//
//            //grid[y][x] = 2; // make as `rotten`
//
//            int[][] dirs = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0} };
//
//            for(int[] d: dirs){
//                int x_ = x + d[0];
//                int y_ = y + d[1];
//                if(x_ < 0 || x_ >= w || y < 0 || y_ >= l || grid[y_][x_] == 0){
//                    continue;
//                }
//                List<Integer> cur2 = new ArrayList<>();
//                cur2.add(x_);
//                cur2.add(y_);
//
//                q.add(cur2);
//
//                if(grid[y_][x_] == 1){
//                    grid[y_][x_] = 2; // make as `rotten`
//                    freshCnt -= 1;
//                }
//            }
//
//            minutes += 1;
//        }
//
//        return minutes;
//    }

    // TODO : fix below
    // V0-1
    // IDEA : BFS (fixed by gpt)
    public int orangesRotting_0_1(int[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }

        int rows = grid.length;
        int cols = grid[0].length;
        int freshCnt = 0;
        Queue<int[]> queue = new LinkedList<>();

        // Initialize queue with all initially rotten oranges and count fresh oranges
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 2) {
                    queue.offer(new int[] { i, j });
                } else if (grid[i][j] == 1) {
                    freshCnt++;
                }
            }
        }

        // If no fresh oranges exist, return 0
        if (freshCnt == 0) {
            return 0;
        }

        int time = 0;
        int[][] directions = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

        // Perform BFS
        while (!queue.isEmpty()) {
            int size = queue.size();
            boolean anyRotten = false;

            for (int i = 0; i < size; i++) {
                int[] cell = queue.poll();
                int x = cell[0];
                int y = cell[1];

                for (int[] dir : directions) {
                    int newX = x + dir[0];
                    int newY = y + dir[1];

                    if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && grid[newX][newY] == 1) {
                        grid[newX][newY] = 2; // Mark as rotten
                        queue.offer(new int[] { newX, newY });
                        freshCnt--;
                        anyRotten = true;
                    }
                }
            }

            if (anyRotten) {
                time++;
            }
        }

        return freshCnt == 0 ? time : -1; // If fresh oranges remain, return -1
    }

    // V1-1
    // https://neetcode.io/problems/rotting-fruit
    // IDEA: BFS
    public int orangesRotting_1_1(int[][] grid) {
        Queue<int[]> q = new ArrayDeque<>();
        int fresh = 0;
        int time = 0;

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] == 1) {
                    fresh++;
                }
                if (grid[r][c] == 2) {
                    q.offer(new int[]{r, c});
                }
            }
        }

        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        while (fresh > 0 && !q.isEmpty()) {
            int length = q.size();
            for (int i = 0; i < length; i++) {
                int[] curr = q.poll();
                int r = curr[0];
                int c = curr[1];

                for (int[] dir : directions) {
                    int row = r + dir[0];
                    int col = c + dir[1];
                    if (row >= 0 && row < grid.length &&
                            col >= 0 && col < grid[0].length &&
                            grid[row][col] == 1) {
                        grid[row][col] = 2;
                        q.offer(new int[]{row, col});
                        fresh--;
                    }
                }
            }
            time++;
        }
        return fresh == 0 ? time : -1;
    }

    // V1-2
    // https://neetcode.io/problems/rotting-fruit
    // IDEA: BFS (No Queue)
    public int orangesRotting_1_2(int[][] grid) {
        int ROWS = grid.length, COLS = grid[0].length;
        int fresh = 0, time = 0;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (grid[r][c] == 1) fresh++;
            }
        }

        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        while (fresh > 0) {
            boolean flag = false;
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    if (grid[r][c] == 2) {
                        for (int[] d : directions) {
                            int row = r + d[0], col = c + d[1];
                            if (row >= 0 && col >= 0 &&
                                    row < ROWS && col < COLS &&
                                    grid[row][col] == 1) {
                                grid[row][col] = 3;
                                fresh--;
                                flag = true;
                            }
                        }
                    }
                }
            }

            if (!flag) return -1;

            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    if (grid[r][c] == 3) grid[r][c] = 2;
                }
            }

            time++;
        }

        return time;
    }

    // V2
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

    public int orangesRotting_2(int[][] grid) {
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

    // V3
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

    public int orangesRotting_3(int[][] grid) {
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
