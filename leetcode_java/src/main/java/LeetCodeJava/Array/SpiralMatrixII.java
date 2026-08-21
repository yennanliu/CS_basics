package LeetCodeJava.Array;

// https://leetcode.com/problems/spiral-matrix-ii/

/**
 *  59. Spiral Matrix II
 *  Medium
 *
 *  Given a positive integer n, generate an n x n matrix filled with
 *  elements from 1 to n^2 in spiral order.
 *
 *  Example 1:
 *   Input: n = 3
 *   Output: [[1,2,3],[8,9,4],[7,6,5]]
 *
 *  Example 2:
 *   Input: n = 1
 *   Output: [[1]]
 *
 *  Constraints:
 *   1 <= n <= 20
 */
public class SpiralMatrixII {

    // V0
    // IDEA: LAYER BY LAYER BOUNDARY SHRINKING
    /**
     * time = O(n^2)
     * space = O(1) (excluding output)
     */
    public int[][] generateMatrix(int n) {
        int[][] res = new int[n][n];

        int top = 0;
        int bottom = n - 1;
        int left = 0;
        int right = n - 1;
        int val = 1;

        while (top <= bottom && left <= right) {

            // left -> right (top row)
            for (int c = left; c <= right; c++) {
                res[top][c] = val++;
            }
            top++;

            // top -> bottom (right col)
            for (int r = top; r <= bottom; r++) {
                res[r][right] = val++;
            }
            right--;

            // right -> left (bottom row)
            if (top <= bottom) {
                for (int c = right; c >= left; c--) {
                    res[bottom][c] = val++;
                }
                bottom--;
            }

            // bottom -> top (left col)
            if (left <= right) {
                for (int r = bottom; r >= top; r--) {
                    res[r][left] = val++;
                }
                left++;
            }
        }

        return res;
    }

    // V1
    // IDEA: DIRECTION VECTOR SIMULATION - walk 1 cell at a time and turn right
    //       whenever the next cell is out of the board or already filled (!= 0)
    /**
     * time = O(n^2)
     * space = O(1) (excluding output)
     */
    public int[][] generateMatrix_1(int n) {
        int[][] res = new int[n][n];
        int[] dr = new int[]{0, 1, 0, -1};
        int[] dc = new int[]{1, 0, -1, 0};

        int r = 0;
        int c = 0;
        int dir = 0;

        for (int val = 1; val <= n * n; val++) {
            res[r][c] = val;
            int nr = r + dr[dir];
            int nc = c + dc[dir];
            // NOTE !!! 0 means "not filled yet" (values start from 1)
            if (nr < 0 || nr >= n || nc < 0 || nc >= n || res[nr][nc] != 0) {
                dir = (dir + 1) % 4;
                nr = r + dr[dir];
                nc = c + dc[dir];
            }
            r = nr;
            c = nc;
        }
        return res;
    }

    // V2
    // IDEA: RECURSION - fill the outermost ring, then recurse on the inner
    //       (n-2) x (n-2) square with the next value
    /**
     * time = O(n^2)
     * space = O(n) recursion depth
     */
    public int[][] generateMatrix_2(int n) {
        int[][] res = new int[n][n];
        fillRing_2(res, 0, n - 1, 1);
        return res;
    }

    private void fillRing_2(int[][] m, int lo, int hi, int val) {
        if (lo > hi) {
            return;
        }
        if (lo == hi) {
            m[lo][lo] = val;
            return;
        }
        for (int c = lo; c <= hi; c++) {
            m[lo][c] = val++;
        }
        for (int r = lo + 1; r <= hi; r++) {
            m[r][hi] = val++;
        }
        for (int c = hi - 1; c >= lo; c--) {
            m[hi][c] = val++;
        }
        for (int r = hi - 1; r >= lo + 1; r--) {
            m[r][lo] = val++;
        }
        fillRing_2(m, lo + 1, hi - 1, val);
    }

}
