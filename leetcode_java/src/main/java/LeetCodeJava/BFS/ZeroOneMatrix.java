package LeetCodeJava.BFS;

// https://leetcode.com/problems/01-matrix/description/

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

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
//    public int[][] updateMatrix(int[][] mat) {
//
//    }

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
