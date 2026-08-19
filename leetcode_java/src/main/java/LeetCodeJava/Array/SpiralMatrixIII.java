package LeetCodeJava.Array;

// https://leetcode.com/problems/spiral-matrix-iii/

/**
 *  885. Spiral Matrix III
 *  Medium
 *
 *  You start at the cell (rStart, cStart) of an rows x cols grid facing east.
 *  The northwest corner is at the first row and column in the grid, and the
 *  southeast corner is at the last row and column.
 *
 *  You will walk in a clockwise spiral shape to visit every position in this
 *  grid. Whenever you move outside the grid's boundary, we continue our walk
 *  outside the grid (but may return to the grid boundary later.). Eventually, we
 *  reach all rows * cols spaces of the grid.
 *
 *  Return an array of coordinates representing the positions of the grid in the
 *  order you visited them.
 *
 *  Example 1:
 *  Input: rows = 1, cols = 4, rStart = 0, cStart = 0
 *  Output: [[0,0],[0,1],[0,2],[0,3]]
 *
 *  Example 2:
 *  Input: rows = 5, cols = 6, rStart = 1, cStart = 4
 *  Output: [[1,4],[1,5],[2,5],[2,4],[2,3],[1,3],[0,3],[0,4],[0,5],[3,5],[3,4],
 *           [3,3],[3,2],[2,2],[1,2],[0,2],[4,5],[4,4],[4,3],[4,2],[4,1],[3,1],
 *           [2,1],[1,1],[0,1],[4,0],[3,0],[2,0],[1,0],[0,0]]
 *
 *  Constraints:
 *   - 1 <= rows, cols <= 100
 *   - 0 <= rStart < rows
 *   - 0 <= cStart < cols
 */
public class SpiralMatrixIII {

    // V0
    // IDEA: walk the clockwise spiral (E, S, W, N) with run lengths 1,1,2,2,3,3...
    //       recording only the steps that land inside the grid
    /**
     * time = O(max(rows, cols)^2)
     * space = O(1) (excluding output)
     */
    public int[][] spiralMatrixIII(int rows, int cols, int rStart, int cStart) {
        int total = rows * cols;
        int[][] res = new int[total][2];
        // east, south, west, north
        int[] dr = new int[]{0, 1, 0, -1};
        int[] dc = new int[]{1, 0, -1, 0};

        int r = rStart;
        int c = cStart;
        res[0][0] = r;
        res[0][1] = c;
        int cnt = 1;

        int dir = 0;
        int len = 1; // current run length, grows every 2 direction changes
        while (cnt < total) {
            for (int rep = 0; rep < 2; rep++) {
                for (int step = 0; step < len; step++) {
                    r += dr[dir];
                    c += dc[dir];
                    if (r >= 0 && r < rows && c >= 0 && c < cols) {
                        res[cnt][0] = r;
                        res[cnt][1] = c;
                        cnt++;
                        if (cnt == total) {
                            return res;
                        }
                    }
                }
                dir = (dir + 1) % 4;
            }
            len++;
        }
        return res;
    }
}
