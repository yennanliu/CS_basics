package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-height-by-stacking-cuboids/description/

import java.util.Arrays;

/**
 * 1691. Maximum Height by Stacking Cuboids
 * Hard
 *
 * Given n cuboids where the dimensions of the ith cuboid is cuboids[i] = [widthi, lengthi, heighti]
 * (0-indexed). Choose a subset of cuboids and place them on each other.
 *
 * You can place cuboid i on cuboid j if widthi <= widthj and lengthi <= lengthj and heighti <= heightj.
 * You can rearrange any cuboid's dimensions by rotating it to put it on another cuboid.
 *
 * Return the maximum height of the stacked cuboids.
 *
 *
 * Example 1:
 *
 * Input: cuboids = [[50,45,20],[95,37,53],[45,23,12]]
 * Output: 190
 * Explanation:
 * Cuboid 1 is placed on the bottom with the 53x37 side facing down with height 95.
 * Cuboid 0 is placed next with the 45x20 side facing down with height 50.
 * Cuboid 2 is placed next with the 23x12 side facing down with height 45.
 * The total height is 95 + 50 + 45 = 190.
 *
 * Example 2:
 *
 * Input: cuboids = [[38,25,45],[76,35,3]]
 * Output: 76
 * Explanation:
 * You can't place any of the cuboids on the other.
 * We choose cuboid 1 and rotate it so that the 35x3 side is facing down and its height is 76.
 *
 * Example 3:
 *
 * Input: cuboids = [[7,11,17],[7,17,11],[11,7,17],[11,17,7],[17,7,11],[17,11,7]]
 * Output: 102
 * Explanation:
 * After rearranging the cuboids, you can see that all cuboids have the same dimension.
 * You can place the 11x7 side down on all cuboids so their heights are 17.
 * The maximum height of stacked cuboids is 6 * 17 = 102.
 *
 *
 * Constraints:
 *
 * n == cuboids.length
 * 1 <= n <= 100
 * 1 <= widthi, lengthi, heighti <= 100
 *
 */
public class MaximumHeightByStackingCuboids {

    // V0
    // IDEA: SORT EACH CUBOID'S DIMENSIONS, SORT THE CUBOIDS, THEN LIS-STYLE DP
    //       rotating so that w <= l <= h is always optimal: the largest side goes up, and if
    //       A fits on B in any orientation, the sorted dimensions also fit pairwise.
    //       after sorting the cuboids lexicographically, a cuboid can only carry one
    //       earlier in the order on top of it, so:
    //         dp[i] = height[i] + max dp[j] over j < i where j fits on top of i
    //       (j fits on i when every sorted dimension of j <= that of i)
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int maxHeight(int[][] cuboids) {
        int n = cuboids.length;

        // sort each cuboid's own dimensions so the tallest side is the height
        int[][] boxes = new int[n][];
        for (int i = 0; i < n; i++) {
            boxes[i] = cuboids[i].clone();
            Arrays.sort(boxes[i]);
        }
        // then order the cuboids lexicographically by (width, length, height)
        Arrays.sort(boxes, (a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            if (a[1] != b[1]) {
                return Integer.compare(a[1], b[1]);
            }
            return Integer.compare(a[2], b[2]);
        });

        // dp[i] = tallest stack whose bottom cuboid is boxes[i]
        int[] dp = new int[n];
        int answer = 0;
        for (int i = 0; i < n; i++) {
            int bestOnTop = 0;
            for (int j = 0; j < i; j++) {
                // boxes[j][0] <= boxes[i][0] already holds from the sort
                boolean fits = boxes[j][1] <= boxes[i][1] && boxes[j][2] <= boxes[i][2];
                if (fits) {
                    bestOnTop = Math.max(bestOnTop, dp[j]);
                }
            }
            dp[i] = bestOnTop + boxes[i][2];
            answer = Math.max(answer, dp[i]);
        }
        return answer;
    }
}
