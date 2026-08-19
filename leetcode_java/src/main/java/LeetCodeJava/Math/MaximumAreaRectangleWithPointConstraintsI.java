package LeetCodeJava.Math;

// https://leetcode.com/problems/maximum-area-rectangle-with-point-constraints-i/

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 *  3380. Maximum Area Rectangle With Point Constraints I
 *  Medium
 *
 *  You are given an array points where points[i] = [x_i, y_i] represents the coordinates
 *  of a point on an infinite plane.
 *
 *  Your task is to find the maximum area of a rectangle that:
 *    - Can be formed using four of these points as its corners.
 *    - Does not contain any other point inside or on its border.
 *    - Has its edges parallel to the axes.
 *
 *  Return the maximum area that you can obtain or -1 if no such rectangle is possible.
 *
 *  Example 1:
 *    Input: points = [[1,1],[1,3],[3,1],[3,3]]
 *    Output: 4
 *
 *  Example 2:
 *    Input: points = [[1,1],[1,3],[3,1],[3,3],[2,2]]
 *    Output: -1
 *    Explanation: the only rectangle always contains [2,2].
 *
 *  Constraints:
 *    1 <= points.length <= 10
 *    points[i].length == 2
 *    0 <= x_i, y_i <= 100
 *    All the given points are unique.
 */
public class MaximumAreaRectangleWithPointConstraintsI {

    // V0
    // IDEA: ONLY 10 POINTS - TRY EVERY (x1, x2) x (y1, y2) BOX
    //
    //   an axis-parallel rectangle is decided by two distinct x values and two distinct y
    //   values, so enumerate those pairs and check that all four corners exist.
    //
    //   the "nothing inside or on the border" rule is then a scan over the other points:
    //   any point whose coordinates both fall inside the closed box, and which is not one
    //   of the four corners, disqualifies it.
    /**
     * time = O(N^5)   // N <= 10
     * space = O(N)
     */
    public int maxRectangleArea(int[][] points) {
        Set<Integer> pts = new HashSet<>();      // encode as x * 1000 + y
        TreeSet<Integer> xsSet = new TreeSet<>();
        TreeSet<Integer> ysSet = new TreeSet<>();
        for (int[] p : points) {
            pts.add(p[0] * 1000 + p[1]);
            xsSet.add(p[0]);
            ysSet.add(p[1]);
        }
        Integer[] xs = xsSet.toArray(new Integer[0]);
        Integer[] ys = ysSet.toArray(new Integer[0]);

        int best = -1;
        for (int i = 0; i < xs.length; i++) {
            for (int j = i + 1; j < xs.length; j++) {
                int x1 = xs[i];
                int x2 = xs[j];
                for (int a = 0; a < ys.length; a++) {
                    for (int b = a + 1; b < ys.length; b++) {
                        int y1 = ys[a];
                        int y2 = ys[b];
                        if (!pts.contains(x1 * 1000 + y1) || !pts.contains(x1 * 1000 + y2)
                                || !pts.contains(x2 * 1000 + y1)
                                || !pts.contains(x2 * 1000 + y2)) {
                            continue;
                        }
                        boolean blocked = false;
                        for (int[] p : points) {
                            int px = p[0];
                            int py = p[1];
                            boolean isCorner = (px == x1 || px == x2) && (py == y1 || py == y2);
                            if (isCorner) {
                                continue;
                            }
                            if (x1 <= px && px <= x2 && y1 <= py && py <= y2) {
                                blocked = true;
                                break;
                            }
                        }
                        if (!blocked) {
                            best = Math.max(best, (x2 - x1) * (y2 - y1));
                        }
                    }
                }
            }
        }
        return best;
    }
}
