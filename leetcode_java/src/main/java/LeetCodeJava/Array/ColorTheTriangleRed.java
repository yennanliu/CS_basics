package LeetCodeJava.Array;

// https://leetcode.com/problems/color-the-triangle-red/description/

import java.util.*;

/**
 * 2647. Color the Triangle Red
 * Hard
 *
 * You are given an integer n. Consider an equilateral triangle of side length n, broken up into n^2 unit equilateral triangles. The triangle has n 1-indexed rows where the ith row has 2i - 1 unit equilateral triangles.
 *
 * The triangles in the ith row are also 1-indexed with coordinates from (i, 1) to (i, 2i - 1).
 *
 * Two triangles are neighbors if they share a side. For example:
 *
 * Triangles (1,1) and (2,2) are neighbors
 * Triangles (3,2) and (3,3) are neighbors.
 * Triangles (2,2) and (3,3) are not neighbors because they do not share any side.
 *
 * Initially, all the unit triangles are white. You want to choose k triangles and color them red. We will then run the following algorithm:
 *
 * 1. Choose a white triangle that has at least two red neighbors.
 *    - If there is no such triangle, stop the algorithm.
 * 2. Color that triangle red.
 * 3. Go to step 1.
 *
 * Choose the minimum k possible and set k triangles red before running this algorithm such that after the algorithm stops, all unit triangles are colored red.
 *
 * Return a 2D list of the coordinates of the triangles that you will color red initially. The answer has to be of the smallest size possible. If there are multiple valid solutions, return any.
 *
 *
 * Example 1:
 *
 * Input: n = 3
 * Output: [[1,1],[2,1],[2,3],[3,1],[3,5]]
 * Explanation: Initially, we choose the shown 5 triangles to be red. Then, we run the algorithm:
 * - Choose (2,2) that has three red neighbors and color it red.
 * - Choose (3,2) that has two red neighbors and color it red.
 * - Choose (3,4) that has three red neighbors and color it red.
 * - Choose (3,3) that has three red neighbors and color it red.
 * It can be shown that choosing any 4 triangles and running the algorithm will not make all triangles red.
 *
 * Example 2:
 *
 * Input: n = 2
 * Output: [[1,1],[2,1],[2,3]]
 * Explanation: Initially, we choose the shown 3 triangles to be red. Then, we run the algorithm:
 * - Choose (2,2) that has three red neighbors and color it red.
 * It can be shown that choosing any 2 triangles and running the algorithm will not make all triangles red.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 1000
 *
 */
public class ColorTheTriangleRed {

    // V0
    // IDEA: CONSTRUCTIVE PATTERN (walk the rows BOTTOM-UP in blocks of 4)
    //       in row i the odd cells (i, 1), (i, 3), ... point up, the even ones point down,
    //       and only a down cell touches the row above. the apex (1, 1) has one neighbour,
    //       so it must always be seeded. from row n upwards the cheapest seeding repeats
    //       with period 4:
    //         phase 0 : every upward cell of the row      (i seeds)
    //         phase 1 : only (i, 2)                       (1 seed)
    //         phase 2 : the upward cells from j = 3 on    (i - 1 seeds)
    //         phase 3 : only (i, 1)                       (1 seed)
    //       each phase leaves every white cell with two red neighbours once the cascade runs.
    /**
     * time = O(N^2)
     * space = O(1)
     */
    public int[][] colorRed(int n) {
        List<int[]> seeds = new ArrayList<>();
        // the apex can never be inferred, so it is always seeded
        seeds.add(new int[] {1, 1});

        // the phase follows how many rows we have emitted, not the row index
        int phase = 0;
        for (int row = n; row > 1; row--) {
            if (phase == 0) {
                for (int col = 1; col < 2 * row; col += 2) {
                    seeds.add(new int[] {row, col});
                }
            } else if (phase == 1) {
                seeds.add(new int[] {row, 2});
            } else if (phase == 2) {
                for (int col = 3; col < 2 * row; col += 2) {
                    seeds.add(new int[] {row, col});
                }
            } else {
                seeds.add(new int[] {row, 1});
            }
            phase = (phase + 1) % 4;
        }

        int[][] result = new int[seeds.size()][];
        for (int i = 0; i < seeds.size(); i++) {
            result[i] = seeds.get(i);
        }
        return result;
    }
}
