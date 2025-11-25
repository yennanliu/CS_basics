package LeetCodeJava.DFS;

// https://leetcode.com/problems/flood-fill/description/
/**
 *  733. Flood Fill
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an image represented by an m x n grid of integers image, where image[i][j] represents the pixel value of the image. You are also given three integers sr, sc, and color. Your task is to perform a flood fill on the image starting from the pixel image[sr][sc].
 *
 * To perform a flood fill:
 *
 * Begin with the starting pixel and change its color to color.
 * Perform the same process for each pixel that is directly adjacent (pixels that share a side with the original pixel, either horizontally or vertically) and shares the same color as the starting pixel.
 * Keep repeating this process by checking neighboring pixels of the updated pixels and modifying their color if it matches the original color of the starting pixel.
 * The process stops when there are no more adjacent pixels of the original color to update.
 * Return the modified image after performing the flood fill.
 *
 *   Example 1:
 *
 * Input: image = [[1,1,1],[1,1,0],[1,0,1]], sr = 1, sc = 1, color = 2
 *
 * Output: [[2,2,2],[2,2,0],[2,0,1]]
 *
 * Explanation:
 *
 *
 *
 * From the center of the image with position (sr, sc) = (1, 1) (i.e., the red pixel), all pixels connected by a path of the same color as the starting pixel (i.e., the blue pixels) are colored with the new color.
 *
 * Note the bottom corner is not colored 2, because it is not horizontally or vertically connected to the starting pixel.
 *
 * Example 2:
 *
 * Input: image = [[0,0,0],[0,0,0]], sr = 0, sc = 0, color = 0
 *
 * Output: [[0,0,0],[0,0,0]]
 *
 * Explanation:
 *
 * The starting pixel is already colored with 0, which is the same as the target color. Therefore, no changes are made to the image.
 *
 *
 *
 * Constraints:
 *
 * m == image.length
 * n == image[i].length
 * 1 <= m, n <= 50
 * 0 <= image[i][j], color < 216
 * 0 <= sr < m
 * 0 <= sc < n
 *
 *
 */
public class FloodFill {

    // V0
//    public int[][] floodFill(int[][] image, int sr, int sc, int color) {
//
//    }

    // V1
    // IDEA: DFS
    // https://leetcode.com/problems/flood-fill/solutions/
    public int[][] floodFill_1(int[][] image, int sr, int sc, int newColor) {
        int color = image[sr][sc];
        if (color != newColor) {
            dfs(image, sr, sc, color, newColor);
        }
        return image;
    }

    public void dfs(int[][] image, int r, int c, int color, int newColor) {
        if (image[r][c] == color) {
            image[r][c] = newColor;
            if (r >= 1) {
                dfs(image, r - 1, c, color, newColor);
            }
            if (c >= 1) {
                dfs(image, r, c - 1, color, newColor);
            }
            if (r + 1 < image.length) {
                dfs(image, r + 1, c, color, newColor);
            }
            if (c + 1 < image[0].length) {
                dfs(image, r, c + 1, color, newColor);
            }
        }
    }


    // V2
    // IDEA: DFS
    // https://leetcode.com/problems/flood-fill/solutions/7369657/beats-10000-flood-fill-using-dfs-by-abhi-cqks/
    public int[][] floodFil_2(int[][] image, int sr, int sc, int color) {
        int startingColor = image[sr][sc];
        dfs_2(image, sr, sc, color, startingColor);
        return image;
    }

    private void dfs_2(int[][] image, int sr, int sc, int color, int startingColor) {
        // boundary + color checks
        if (sr >= image.length || sr < 0 ||
                sc >= image[0].length || sc < 0 ||
                image[sr][sc] != startingColor ||
                image[sr][sc] == color) {
            return;
        }

        // color the current pixel
        image[sr][sc] = color;

        // explore neighbors
        dfs_2(image, sr, sc - 1, color, startingColor); // left
        dfs_2(image, sr, sc + 1, color, startingColor); // right
        dfs_2(image, sr - 1, sc, color, startingColor); // up
        dfs_2(image, sr + 1, sc, color, startingColor); // down
    }


    // V3
    // IDEA: DFS
    // https://leetcode.com/problems/flood-fill/solutions/7364239/simple-java-code-beginner-friendly-dfs-a-d36x/
    public int[][] floodFill_3(int[][] image, int sr, int sc, int color) {
        dfsHelper(image, sr, sc, image[sr][sc], color);
        return image;
    }

    public void dfsHelper(int[][] img, int i, int j, int origColor, int newColor) {

        if (i < 0 || i >= img.length || j < 0 || j >= img[0].length || img[i][j] == newColor || img[i][j] != origColor)
            return;

        img[i][j] = newColor;

        dfsHelper(img, i - 1, j, origColor, newColor);
        dfsHelper(img, i + 1, j, origColor, newColor);
        dfsHelper(img, i, j - 1, origColor, newColor);
        dfsHelper(img, i, j + 1, origColor, newColor);
    }




}
