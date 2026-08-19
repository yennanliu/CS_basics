package LeetCodeJava.Array;

// https://leetcode.com/problems/flipping-an-image/

/**
 *  832. Flipping an Image
 *  Easy
 *
 *  Given an n x n binary matrix image, flip the image horizontally, then invert
 *  it, and return the resulting image.
 *
 *  To flip an image horizontally means that each row of the image is reversed.
 *   - For example, flipping [1,1,0] horizontally results in [0,1,1].
 *
 *  To invert an image means that each 0 is replaced by 1, and each 1 is replaced
 *  by 0.
 *   - For example, inverting [0,1,1] results in [1,0,0].
 *
 *  Example 1:
 *  Input: image = [[1,1,0],[1,0,1],[0,0,0]]
 *  Output: [[1,0,0],[0,1,0],[1,1,1]]
 *
 *  Example 2:
 *  Input: image = [[1,1,0,0],[1,0,0,1],[0,1,1,1],[1,0,1,0]]
 *  Output: [[1,1,0,0],[0,1,1,0],[0,0,0,1],[1,0,1,0]]
 *
 *  Constraints:
 *   - n == image.length == image[i].length
 *   - 1 <= n <= 20
 *   - images[i][j] is either 0 or 1.
 */
public class FlippingAnImage {

    // V0
    // IDEA: two pointers per row, swap + invert in place (x ^ 1 flips a 0/1 bit)
    /**
     * time = O(n^2)
     * space = O(1)
     */
    public int[][] flipAndInvertImage(int[][] image) {
        if (image == null || image.length == 0) {
            return image;
        }
        for (int[] row : image) {
            int l = 0;
            int r = row.length - 1;
            while (l < r) {
                int tmp = row[l] ^ 1;
                row[l] = row[r] ^ 1;
                row[r] = tmp;
                l++;
                r--;
            }
            if (l == r) {
                row[l] = row[l] ^ 1;
            }
        }
        return image;
    }
}
