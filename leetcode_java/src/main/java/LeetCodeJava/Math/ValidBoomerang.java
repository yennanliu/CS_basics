package LeetCodeJava.Math;

// https://leetcode.com/problems/valid-boomerang/description/

/**
 * 1037. Valid Boomerang
 * Easy
 *
 * Given an array points where points[i] = [xi, yi] represents a point on the X-Y plane,
 * return true if these points are a boomerang.
 *
 * A boomerang is a set of three points that are all distinct and not in a straight line.
 *
 *
 * Example 1:
 *
 * Input: points = [[1,1],[2,3],[3,2]]
 * Output: true
 *
 * Example 2:
 *
 * Input: points = [[1,1],[2,2],[3,3]]
 * Output: false
 *
 *
 * Constraints:
 *
 * points.length == 3
 * points[i].length == 2
 * 0 <= xi, yi <= 100
 *
 */
public class ValidBoomerang {

    // V0
    // IDEA: CROSS PRODUCT (no slope division, so no divide-by-zero on vertical lines)
    //   the points are collinear <=> (x2-x1)*(y3-y1) - (y2-y1)*(x3-x1) == 0.
    //   two identical points also make it 0, so one test covers "all distinct" too.
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean isBoomerang(int[][] points) {
        int x1 = points[0][0], y1 = points[0][1];
        int x2 = points[1][0], y2 = points[1][1];
        int x3 = points[2][0], y3 = points[2][1];

        // cross product of edge vectors (p1 -> p2) and (p1 -> p3)
        int cross = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
        return cross != 0;
    }
}
