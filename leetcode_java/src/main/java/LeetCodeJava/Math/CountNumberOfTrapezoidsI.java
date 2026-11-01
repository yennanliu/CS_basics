package LeetCodeJava.Math;

// https://leetcode.com/problems/count-number-of-trapezoids-i/description/

import java.util.*;

/**
 * 3623. Count Number of Trapezoids I
 * Medium
 *
 * You are given a 2D integer array points, where points[i] = [xi, yi] represents
 * the coordinates of the ith point on the Cartesian plane.
 *
 * A horizontal trapezoid is a convex quadrilateral with at least one pair of
 * horizontal sides (i.e. parallel to the x-axis). Two lines are parallel if and
 * only if they have the same slope.
 *
 * Return the number of unique horizontal trapezoids that can be formed by
 * choosing any four distinct points from points.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: points = [[1,0],[2,0],[3,0],[2,2],[3,2]]
 * Output: 3
 * Explanation:
 * There are three distinct ways to pick four points that form a horizontal
 * trapezoid:
 * Using points [1,0], [2,0], [3,2], and [2,2].
 * Using points [2,0], [3,0], [3,2], and [2,2].
 * Using points [1,0], [3,0], [3,2], and [2,2].
 *
 * Example 2:
 *
 * Input: points = [[0,0],[1,0],[0,1],[2,1]]
 * Output: 1
 * Explanation:
 * There is only one horizontal trapezoid that can be formed.
 *
 *
 * Constraints:
 *
 * 4 <= points.length <= 10^5
 * -10^8 <= xi, yi <= 10^8
 * All points are pairwise distinct.
 *
 */
public class CountNumberOfTrapezoidsI {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: COUNT HORIZONTAL SEGMENTS PER ROW, THEN PAIR SEGMENTS ACROSS DIFFERENT ROWS
    //       a horizontal side is two points with the same y, so a row with v points offers
    //       C(v, 2) sides. any side from row y1 plus any side from another row y2 gives four
    //       distinct points in convex position -> exactly one horizontal trapezoid, and no
    //       trapezoid is produced twice (it cannot have two different horizontal side pairs).
    //       so answer = sum over row pairs of sides(y1) * sides(y2), done with a running prefix.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int countTrapezoids(int[][] points) {
        // pointsPerRow: y -> how many points lie on that horizontal line
        Map<Integer, Integer> pointsPerRow = new HashMap<>();
        for (int[] point : points) {
            int y = point[1];
            pointsPerRow.put(y, pointsPerRow.getOrDefault(y, 0) + 1);
        }

        long answer = 0;
        long sidesSoFar = 0; // horizontal sides in the rows already processed (mod MOD)
        for (int count : pointsPerRow.values()) {
            long sides = (long) count * (count - 1) / 2 % MOD;
            // pair every side of this row with every side of an earlier row
            answer = (answer + sidesSoFar * sides) % MOD;
            sidesSoFar = (sidesSoFar + sides) % MOD;
        }
        return (int) answer;
    }
}
