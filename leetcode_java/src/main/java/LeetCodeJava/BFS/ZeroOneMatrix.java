package LeetCodeJava.BFS;

// https://leetcode.com/problems/01-matrix/description/

import java.util.*;

/**
 *
 542. 01 Matrix
 Medium
 Topics
 premium lock icon
 Companies
 Given an m x n binary matrix mat, return the distance of the nearest 0 for each cell.

 The distance between two cells sharing a common edge is 1.



 Example 1:


 Input: mat = [[0,0,0],[0,1,0],[0,0,0]]
 Output: [[0,0,0],[0,1,0],[0,0,0]]
 Example 2:


 Input: mat = [[0,0,0],[0,1,0],[1,1,1]]
 Output: [[0,0,0],[0,1,0],[1,2,1]]


 Constraints:

 m == mat.length
 n == mat[i].length
 1 <= m, n <= 104
 1 <= m * n <= 104
 mat[i][j] is either 0 or 1.
 There is at least one 0 in mat.


 Note: This question is the same as 1765: https://leetcode.com/problems/map-of-highest-peak/
 *
 *
 */
public class ZeroOneMatrix {

    // V0
    // IDEA: BFS
    // TODO : fix below
//    public int[][] updateMatrix(int[][] mat) {
//        // edge
//        if(mat == null || mat.length == 0 || mat[0].length == 0){
//            return null;
//        }
//
//        int l = mat.length;
//        int w = mat[0].length;
//
//        int[][] res = mat;
//
//        List<int[]> candidates = new ArrayList<>();
//        for(int i = 0; i < l; i++){
//            for(int j = 0; j < w; j++){
//                if(mat[i][j] == 1){
//                    candidates.add(new int[] {i, j});
//                }
//            }
//        }
//
//
//        // Queue : [x, y, steps]
//        Queue<int[]> q = new LinkedList<>();
//        boolean[][] visited = new boolean[l][w];
//
//        for(int[] c: candidates){
//            q.add(new int[] {c[0], c[1], 0});
//        }
//
//        int[][] dirs = new int[][] { {1,0}, {-1,0}, {0,1}, {0,-1} };
//
//        while (!q.isEmpty()){
//            int[] cur = q.poll();
//            int x = cur[0];
//            int y = cur[1];
//            int steps = cur[2];
//            visited[y][x] = true;
//            res[y][x] = steps;
//
//            for(int[] d: dirs){
//                int x_ = x + d[0];
//                int y_ = y + d[1];
//
//                if(x_ < 0 || x_ >= w || y_ < 0 || y >= l || visited[y_][x_] || mat[y_][x_] != 0){
//                    continue;
//                }
//
//                //res[y_][x_ ] = steps;
//                // add to queue
//                q.add(new int[] {x_, y_, steps + 1});
//            }
//
//        }
//
//
//        return res;
//    }

    // V0-1
    // IDEA:  multi-source BFS (fixed by gpt)
    public int[][] updateMatrix_0_1(int[][] mat) {
        if (mat == null || mat.length == 0 || mat[0].length == 0) {
            return new int[0][0];
        }

        int rows = mat.length;
        int cols = mat[0].length;
        int[][] res = new int[rows][cols];
        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new LinkedList<>();

        // 1️⃣ Add all 0s to the queue, set 1s as INF
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                /**
                 *  NOTE !!! below
                 *
                 *  - We use multi-source BFS starting from all 0s
                 *    to compute the minimum distance to a 0 for each 1.
                 *
                 */
                /**
                 * ## ❓ Why Does the Fixed Code Add All `0`s to the Queue (Not `1`s)?
                 *
                 * ---
                 *
                 * ## 🧠 Let's Think About the Goal
                 *
                 * The problem asks:
                 *
                 * > For each cell with a `1`, find the distance to the **nearest `0`**.
                 *
                 * There are **two basic ways** to approach this:
                 *
                 * ---
                 *
                 * ### ❌ Option A (your original code):
                 *
                 * **Start BFS from every `1`**, searching for the nearest `0`.
                 *
                 * #### Problem:
                 *
                 * * You perform a **BFS for every 1** in the matrix.
                 * * In worst case, you scan the whole matrix **once per 1**.
                 * * That’s **O(N × M × (N + M))** — very slow for large inputs.
                 *
                 * ---
                 *
                 * ### ✅ Option B (optimized):
                 *
                 * **Start BFS from every `0`**, and compute distance as you expand.
                 *
                 * #### Why this works:
                 *
                 * * You flip the problem: instead of asking *“how far is this 1 from a 0?”*, you ask *“how far can each 0 reach a 1?”*
                 * * When you expand from all 0s **at the same time**, you ensure that **each 1 gets the shortest path to a 0**, because BFS guarantees minimum-distance traversal.
                 * * Time complexity is **O(N × M)** — each cell is visited only once.
                 *
                 */
                if (mat[i][j] == 0) {
                    queue.offer(new int[] { i, j });
                    visited[i][j] = true;
                } else {
                    res[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        int[][] dirs = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

        // 2️⃣ BFS from all 0s
        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int row = curr[0];
            int col = curr[1];

            for (int[] d : dirs) {
                int newRow = row + d[0];
                int newCol = col + d[1];

                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && !visited[newRow][newCol]) {
                    res[newRow][newCol] = res[row][col] + 1;
                    queue.offer(new int[] { newRow, newCol });
                    visited[newRow][newCol] = true;
                }
            }
        }

        return res;
    }

    // V1

    // V2
    // https://leetcode.com/problems/01-matrix/solutions/6750388/video-using-queue-solution-by-niits-e1z6/
    public int[][] updateMatrix_2(int[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;
        int[][] directions = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
        Queue<int[]> queue = new ArrayDeque<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                /**
                 *  NOTE !!! below
                 *
                 */
                if (mat[i][j] == 0) {
                    queue.add(new int[] { i, j });
                } else {
                    mat[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int row = cell[0];
            int col = cell[1];

            for (int[] direction : directions) {
                int newRow = row + direction[0];
                int newCol = col + direction[1];

                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols
                        && mat[newRow][newCol] > mat[row][col] + 1) {
                    mat[newRow][newCol] = mat[row][col] + 1;
                    queue.add(new int[] { newRow, newCol });
                }
            }
        }

        return mat;
    }


    // V3
    // https://leetcode.com/problems/01-matrix/solutions/3920110/9487-multi-source-bfs-queue-by-vanamsen-gd1q/
    // IDEA:  Multi-source BFS + Queue
    public int[][] updateMatrix_3(int[][] mat) {
        if (mat == null || mat.length == 0 || mat[0].length == 0)
            return new int[0][0];

        int m = mat.length, n = mat[0].length;
        Queue<int[]> queue = new LinkedList<>();
        int MAX_VALUE = m * n;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                /**
                 *  NOTE !!! below
                 *
                 */
                if (mat[i][j] == 0) {
                    queue.offer(new int[] { i, j });
                } else {
                    mat[i][j] = MAX_VALUE;
                }
            }
        }

        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            for (int[] dir : directions) {
                int r = cell[0] + dir[0], c = cell[1] + dir[1];
                if (r >= 0 && r < m && c >= 0 && c < n && mat[r][c] > mat[cell[0]][cell[1]] + 1) {
                    queue.offer(new int[] { r, c });
                    mat[r][c] = mat[cell[0]][cell[1]] + 1;
                }
            }
        }

        return mat;
    }

    // V4
    // https://leetcode.com/problems/01-matrix/solutions/6770885/java-solution-using-bfs-by-navneetksingh-qnq6/
    // IDEA: BFS
    int m, n;
    int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    public int[][] updateMatrix_4(int[][] matrix) {
        m = matrix.length;
        n = matrix[0].length;

        int[][] result = new int[m][n];

        Queue<int[]> que = new LinkedList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                /**
                 *  NOTE !!! below
                 *
                 */
                if (matrix[i][j] == 0) {
                    result[i][j] = 0;
                    que.offer(new int[] { i, j });
                } else {
                    result[i][j] = -1;
                }
            }
        }

        while (!que.isEmpty()) {
            int[] cell = que.poll();
            int i = cell[0];
            int j = cell[1];

            for (int[] dir : directions) {

                int new_i = i + dir[0];
                int new_j = j + dir[1];

                if (new_i >= 0 && new_i < m && new_j >= 0 && new_j < n && result[new_i][new_j] == -1) {
                    result[new_i][new_j] = result[i][j] + 1;
                    que.add(new int[] { new_i, new_j });
                }
            }
        }

        return result;
    }


}
