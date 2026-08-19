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
}
