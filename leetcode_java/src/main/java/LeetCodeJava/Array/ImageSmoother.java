package LeetCodeJava.Array;

// https://leetcode.com/problems/image-smoother/

/**
 *  661. Image Smoother
 *  Easy
 *
 *  An image smoother is a filter of the size 3 x 3 that can be applied to each
 *  cell of an image by rounding down the average of the cell and the eight
 *  surrounding cells (i.e., the average of the nine cells in the blue smoother).
 *  If one or more of the surrounding cells of a cell is not present, we do not
 *  consider it in the average (i.e., the average of the four cells in the red
 *  smoother).
 *
 *  Given an m x n integer matrix img representing the grayscale of an image,
 *  return the image after applying the smoother on each cell of it.
 *
 *  Example 1:
 *  Input: img = [[1,1,1],[1,0,1],[1,1,1]]
 *  Output: [[0,0,0],[0,0,0],[0,0,0]]
 *
 *  Example 2:
 *  Input: img = [[100,200,100],[200,50,200],[100,200,100]]
 *  Output: [[137,141,137],[141,138,141],[137,141,137]]
 *
 *  Constraints:
 *  m == img.length
 *  n == img[i].length
 *  1 <= m, n <= 200
 *  0 <= img[i][j] <= 255
 */
public class ImageSmoother {

    // V0
    // IDEA: for each cell, sum the in-bound cells of its 3x3 neighbourhood and
    //       integer-divide by how many were counted
    /**
     * time = O(m * n)
     * space = O(m * n)  (the output)
     */
    public int[][] imageSmoother(int[][] img) {
        if (img == null || img.length == 0 || img[0].length == 0) {
            return img;
        }
        int m = img.length;
        int n = img[0].length;
        int[][] res = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int sum = 0;
                int cnt = 0;
                for (int r = i - 1; r <= i + 1; r++) {
                    for (int c = j - 1; c <= j + 1; c++) {
                        if (r < 0 || r >= m || c < 0 || c >= n) {
                            continue;
                        }
                        sum += img[r][c];
                        cnt++;
                    }
                }
                res[i][j] = sum / cnt;
            }
        }
        return res;
    }


    // V1
    // IDEA: 2D PREFIX SUM (integral image) - each 3x3 neighbourhood sum becomes O(1)
    /**
     * time = O(m * n)
     * space = O(m * n)
     */
    public int[][] imageSmoother_1(int[][] img) {
        if (img == null || img.length == 0 || img[0].length == 0) {
            return img;
        }
        int m = img.length;
        int n = img[0].length;

        int[][] pre = new int[m + 1][n + 1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                pre[i + 1][j + 1] = img[i][j] + pre[i][j + 1] + pre[i + 1][j] - pre[i][j];
            }
        }

        int[][] res = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int r1 = Math.max(0, i - 1);
                int r2 = Math.min(m - 1, i + 1);
                int c1 = Math.max(0, j - 1);
                int c2 = Math.min(n - 1, j + 1);
                int sum = pre[r2 + 1][c2 + 1] - pre[r1][c2 + 1] - pre[r2 + 1][c1] + pre[r1][c1];
                int cnt = (r2 - r1 + 1) * (c2 - c1 + 1);
                res[i][j] = sum / cnt;
            }
        }
        return res;
    }

    // V2
    // IDEA: BIT PACKING in-place - values fit in 8 bits, so stash the smoothed value
    //       in bits 8..15 of the SAME cell, then unpack (O(1) auxiliary space)
    /**
     * time = O(m * n)
     * space = O(1) extra (beyond the returned matrix)
     */
    public int[][] imageSmoother_2(int[][] img) {
        if (img == null || img.length == 0 || img[0].length == 0) {
            return img;
        }
        int m = img.length;
        int n = img[0].length;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int sum = 0;
                int cnt = 0;
                for (int r = Math.max(0, i - 1); r <= Math.min(m - 1, i + 1); r++) {
                    for (int c = Math.max(0, j - 1); c <= Math.min(n - 1, j + 1); c++) {
                        sum += img[r][c] & 0xFF; // only the ORIGINAL low byte
                        cnt++;
                    }
                }
                img[i][j] |= (sum / cnt) << 8;
            }
        }

        int[][] res = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = img[i][j] >> 8;
                img[i][j] &= 0xFF; // restore the input matrix
            }
        }
        return res;
    }
}
