package LeetCodeJava.Sort;

// https://leetcode.com/problems/widest-vertical-area-between-two-points-containing-no-points/

import java.util.Arrays;

/**
 *  1637. Widest Vertical Area Between Two Points Containing No Points
 *  Medium
 *
 *  Given n points on a 2D plane where points[i] = [xi, yi], return the widest
 *  vertical area between two points such that no points are inside the area.
 *
 *  A vertical area is an area of fixed-width extending infinitely along the
 *  y-axis (i.e., infinite height). The widest vertical area is the one with the
 *  maximum width.
 *
 *  Note that points on the edge of a vertical area are not considered included
 *  in the area.
 *
 *  Example 1:
 *    Input: points = [[8,7],[9,9],[7,4],[9,7]]
 *    Output: 1
 *
 *  Example 2:
 *    Input: points = [[3,1],[9,0],[1,0],[1,4],[5,3],[8,8]]
 *    Output: 3
 *
 *  Constraints:
 *    n == points.length
 *    2 <= n <= 10^5
 *    points[i].length == 2
 *    0 <= xi, yi <= 10^9
 */
public class WidestVerticalAreaBetweenTwoPointsContainingNoPoints {

    // V0
    // IDEA: SORT BY X, TAKE THE LARGEST GAP BETWEEN CONSECUTIVE X VALUES
    //       the y coordinates are irrelevant — a vertical strip is defined purely
    //       by an x-interval, and it is empty exactly when no point's x falls
    //       strictly inside it. So sort the x values and answer
    //       max(x[i+1] - x[i]).
    //       NOTE: duplicated x values just give a gap of 0, which is harmless.
    //       NOTE: xi <= 10^9 so a gap still fits in int, but the x values are
    //             compared via Arrays.sort on int[] (no subtraction comparator).
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int maxWidthOfVerticalArea(int[][] points) {
        int n = points.length;
        int[] xs = new int[n];
        for (int i = 0; i < n; i++) {
            xs[i] = points[i][0];
        }
        Arrays.sort(xs);

        int res = 0;
        for (int i = 1; i < n; i++) {
            res = Math.max(res, xs[i] - xs[i - 1]);
        }
        return res;
    }
}
