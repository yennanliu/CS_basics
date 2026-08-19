package LeetCodeJava.Math;

// https://leetcode.com/problems/largest-triangle-area/

/**
 *  812. Largest Triangle Area
 *  Easy
 *
 *  Given an array of points on the X-Y plane points where
 *  points[i] = [xi, yi], return the area of the largest triangle that can be
 *  formed by any three different points. Answers within 10^-5 of the actual
 *  answer will be accepted.
 *
 *  Example 1:
 *   Input: points = [[0,0],[0,1],[1,0],[0,2],[2,0]]
 *   Output: 2.00000
 *   Explanation: the five points are shown in the above figure.
 *                The red triangle is the largest.
 *
 *  Example 2:
 *   Input: points = [[1,0],[0,0],[0,1]]
 *   Output: 0.50000
 *
 *  Constraints:
 *   - 3 <= points.length <= 50
 *   - -50 <= xi, yi <= 50
 *   - All the given points are unique.
 */
public class LargestTriangleArea {

    // V0
    // IDEA: brute force over all triples + shoelace formula for the triangle area.
    /**
     * time = O(n^3)
     * space = O(1)
     */
    public double largestTriangleArea(int[][] points) {
        int n = points.length;
        double res = 0.0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    res = Math.max(res, area(points[i], points[j], points[k]));
                }
            }
        }
        return res;
    }

    private double area(int[] p1, int[] p2, int[] p3) {
        // shoelace formula
        int cross = p1[0] * (p2[1] - p3[1])
                + p2[0] * (p3[1] - p1[1])
                + p3[0] * (p1[1] - p2[1]);
        return Math.abs(cross) * 0.5;
    }
}
