package LeetCodeJava.Math;

// https://leetcode.com/problems/convex-polygon/

import java.util.List;

/**
 *  469. Convex Polygon
 *  Medium
 *
 *  You are given an array of points on the X-Y plane points where
 *  points[i] = [xi, yi]. The points form a polygon when joined sequentially.
 *
 *  Return true if this polygon is convex and false otherwise.
 *
 *  You may assume the polygon formed by given points is always a simple polygon.
 *  In other words, we ensure that exactly two edges intersect at each vertex and
 *  that edges otherwise don't intersect each other.
 *
 *  Example 1:
 *    Input: points = [[0,0],[0,5],[5,5],[5,0]]
 *    Output: true
 *
 *  Example 2:
 *    Input: points = [[0,0],[0,10],[10,10],[10,0],[5,5]]
 *    Output: false
 *
 *  Constraints:
 *    3 <= points.length <= 10^4
 *    points[i].length == 2
 *    -10^4 <= xi, yi <= 10^4
 *    All the given points are unique.
 */
public class ConvexPolygon {

    // V0
    // IDEA: walk every consecutive triple, all cross products must share the same sign
    /**
     * time = O(n)
     * space = O(1)
     */
    public boolean isConvex(List<List<Integer>> points) {
        int n = points.size();
        long prev = 0;
        for (int i = 0; i < n; i++) {
            long cur = cross(points.get(i), points.get((i + 1) % n), points.get((i + 2) % n));
            if (cur != 0) {
                if (cur * prev < 0) {
                    return false;
                }
                prev = cur;
            }
        }
        return true;
    }

    // cross product of (p1 - p0) x (p2 - p1)
    private long cross(List<Integer> p0, List<Integer> p1, List<Integer> p2) {
        long x1 = p1.get(0) - p0.get(0);
        long y1 = p1.get(1) - p0.get(1);
        long x2 = p2.get(0) - p1.get(0);
        long y2 = p2.get(1) - p1.get(1);
        return x1 * y2 - y1 * x2;
    }
}
