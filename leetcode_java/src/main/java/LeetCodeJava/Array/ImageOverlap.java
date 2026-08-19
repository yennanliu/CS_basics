package LeetCodeJava.Array;

// https://leetcode.com/problems/image-overlap/

/**
 *  835. Image Overlap
 *  Medium
 *
 *  You are given two images, img1 and img2, represented as binary, square
 *  matrices of size n x n. A binary matrix has only 0s and 1s as values.
 *
 *  We translate one image however we choose by sliding all the 1 bits left,
 *  right, up, and/or down any number of units. We then place it on top of the
 *  other image. We can then calculate the overlap by counting the number of
 *  positions that have a 1 in both images.
 *
 *  Note also that a translation does not include any kind of rotation. Any 1
 *  bits that are translated outside of the matrix borders are erased.
 *
 *  Return the largest possible overlap.
 *
 *  Example 1:
 *  Input: img1 = [[1,1,0],[0,1,0],[0,1,0]], img2 = [[0,0,0],[0,1,1],[0,0,1]]
 *  Output: 3
 *
 *  Example 2:
 *  Input: img1 = [[1]], img2 = [[1]]
 *  Output: 1
 *
 *  Constraints:
 *   - n == img1.length == img1[i].length
 *   - n == img2.length == img2[i].length
 *   - 1 <= n <= 30
 *   - img1[i][j] is either 0 or 1.
 *   - img2[i][j] is either 0 or 1.
 */
public class ImageOverlap {

    // V0
    // IDEA: count how many (1-cell of img1, 1-cell of img2) pairs share the same
    //       (dx, dy) shift; the most frequent shift is the best overlap.
    /**
     * time = O(n^4)
     * space = O(n^2)
     */
    public int largestOverlap(int[][] img1, int[][] img2) {
        int n = img1.length;
        // shift range is [-(n-1), n-1] on each axis -> (2n-1) x (2n-1) buckets
        int size = 2 * n - 1;
        int[] count = new int[size * size];
        int best = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (img1[i][j] != 1) {
                    continue;
                }
                for (int i2 = 0; i2 < n; i2++) {
                    for (int j2 = 0; j2 < n; j2++) {
                        if (img2[i2][j2] != 1) {
                            continue;
                        }
                        int dx = i - i2 + n - 1;
                        int dy = j - j2 + n - 1;
                        int c = ++count[dx * size + dy];
                        if (c > best) {
                            best = c;
                        }
                    }
                }
            }
        }
        return best;
    }

    // V1
    // IDEA: brute force every possible (row, col) shift in both directions and
    //       count matching 1s directly.
    /**
     * time = O(n^4)
     * space = O(1)
     */
    public int largestOverlap_1(int[][] img1, int[][] img2) {
        int n = img1.length;
        int best = 0;
        for (int dx = -(n - 1); dx <= n - 1; dx++) {
            for (int dy = -(n - 1); dy <= n - 1; dy++) {
                best = Math.max(best, shiftAndCount(img1, img2, dx, dy));
            }
        }
        return best;
    }

    // count cells where img1[i][j] == 1 and img2[i-dx][j-dy] == 1
    private int shiftAndCount(int[][] img1, int[][] img2, int dx, int dy) {
        int n = img1.length;
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int i2 = i - dx;
                int j2 = j - dy;
                if (i2 < 0 || i2 >= n || j2 < 0 || j2 >= n) {
                    continue;
                }
                if (img1[i][j] == 1 && img2[i2][j2] == 1) {
                    cnt++;
                }
            }
        }
        return cnt;
    }
}
