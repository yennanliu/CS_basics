package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-area-rectangle-ii/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  963. Minimum Area Rectangle II
 *  Medium
 *
 *  You are given an array of points in the X-Y plane points where
 *  points[i] = [xi, yi].
 *
 *  Return the minimum area of any rectangle formed from these points, with
 *  sides not necessarily parallel to the X and Y axes. If there is not any such
 *  rectangle, return 0.
 *
 *  Answers within 10^-5 of the actual answer will be accepted.
 *
 *  Example 1:
 *   Input: points = [[1,2],[2,1],[1,0],[0,1]]
 *   Output: 2.00000
 *
 *  Example 2:
 *   Input: points = [[0,1],[2,1],[1,1],[1,0],[2,0]]
 *   Output: 1.00000
 *
 *  Example 3:
 *   Input: points = [[0,3],[1,2],[3,1],[1,3],[2,1]]
 *   Output: 0
 *   Explanation: there is no possible rectangle to form from these points.
 *
 *  Constraints:
 *   - 1 <= points.length <= 50
 *   - points[i].length == 2
 *   - 0 <= xi, yi <= 4 * 10^4
 *   - All the given points are unique.
 */
public class MinimumAreaRectangleII {

    // V0
    // IDEA: GEOMETRY - four points form a rectangle iff its two diagonals share
    //       the same midpoint AND the same length. So group every point pair by
    //       (2*midpoint, squaredLength); any two pairs in the same group are the
    //       diagonals of a rectangle, whose area is |P-R| * |P-S|.
    /**
     * time = O(n^2 * g)   n = points.length, g = max group size (<= n^2)
     * space = O(n^2)
     */
    public double minAreaFreeRect(int[][] points) {
        int n = points.length;
        // key -> list of pairs (each pair stored as the 2 point indices)
        Map<String, List<int[]>> groups = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long cx = points[i][0] + points[j][0]; // 2 * midpoint x
                long cy = points[i][1] + points[j][1]; // 2 * midpoint y
                long len = dist2(points[i], points[j]);
                String key = cx + "#" + cy + "#" + len;
                List<int[]> lst = groups.get(key);
                if (lst == null) {
                    lst = new ArrayList<>();
                    groups.put(key, lst);
                }
                lst.add(new int[]{i, j});
            }
        }

        double res = Double.MAX_VALUE;
        for (List<int[]> lst : groups.values()) {
            for (int a = 0; a < lst.size(); a++) {
                for (int b = a + 1; b < lst.size(); b++) {
                    int[] d1 = lst.get(a);
                    int[] d2 = lst.get(b);
                    int[] p = points[d1[0]];
                    int[] q = points[d2[0]];
                    int[] r = points[d2[1]];
                    double side1 = Math.sqrt(dist2(p, q));
                    double side2 = Math.sqrt(dist2(p, r));
                    res = Math.min(res, side1 * side2);
                }
            }
        }
        return res == Double.MAX_VALUE ? 0 : res;
    }

    private long dist2(int[] p, int[] q) {
        long dx = p[0] - q[0];
        long dy = p[1] - q[1];
        return dx * dx + dy * dy;
    }
}
