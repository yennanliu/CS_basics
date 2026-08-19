package LeetCodeJava.Math;

// https://leetcode.com/problems/erect-the-fence-ii/

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 *  1924. Erect the Fence II
 *  Hard
 *
 *  You are given a 2D integer array trees where trees[i] = [xi, yi] represents the
 *  location of the ith tree in the garden.
 *
 *  You are asked to fence the entire garden using the minimum length of rope possible.
 *  The garden is well-fenced only if all the trees are enclosed and the rope used forms a
 *  perfect circle. A tree is considered enclosed if it is inside or on the border of the
 *  circle.
 *
 *  More formally, you must form a circle using the rope with a center (x, y) and radius r
 *  where all trees lie inside or on the circle and r is minimum.
 *
 *  Return the center and radius of the circle as a length 3 array [x, y, r]. Answers
 *  within 10^-5 of the actual answer will be accepted.
 *
 *  Example 1:
 *    Input: trees = [[1,1],[2,2],[2,0],[2,4],[3,3],[4,2]]
 *    Output: [2.00000,2.00000,2.00000]
 *
 *  Example 2:
 *    Input: trees = [[1,2],[2,2],[4,2]]
 *    Output: [2.50000,2.00000,1.50000]
 *
 *  Constraints:
 *    1 <= trees.length <= 3000
 *    trees[i].length == 2
 *    0 <= xi, yi <= 3000
 */
public class ErectTheFenceII {

    private static final double EPS = 1e-7;

    // V0
    // IDEA: WELZL - RANDOMIZED INCREMENTAL MINIMUM ENCLOSING CIRCLE
    //
    //   the smallest enclosing circle is pinned by at most 3 boundary points. after a
    //   random shuffle, run 3 nested "repair" loops:
    //     - keep a current circle for the prefix p[0..i-1]
    //     - if p[i] falls outside then p[i] MUST lie on the boundary of the answer for
    //       the prefix p[0..i]; restart the scan with p[i] pinned
    //     - one more level pins a 2nd point, one more pins a 3rd and the circle is then
    //       the circumcircle of those 3 points
    //
    //   each level is entered with probability O(1/i) over the random order, so the whole
    //   thing is O(n) EXPECTED even though it looks cubic.
    //
    //   NOTE: use an epsilon when testing "is inside" so boundary points do not trigger a
    //         needless (and endless) rebuild.
    /**
     * time = O(N) expected
     * space = O(N)
     */
    public double[] outerTrees(int[][] trees) {
        int n = trees.length;
        List<double[]> pts = new ArrayList<>();
        for (int[] t : trees) {
            pts.add(new double[]{t[0], t[1]});
        }
        Collections.shuffle(pts);

        double[] circle = new double[]{pts.get(0)[0], pts.get(0)[1], 0.0};
        for (int i = 1; i < n; i++) {
            if (inside(circle, pts.get(i))) {
                continue;
            }
            circle = new double[]{pts.get(i)[0], pts.get(i)[1], 0.0};
            for (int j = 0; j < i; j++) {
                if (inside(circle, pts.get(j))) {
                    continue;
                }
                circle = fromTwo(pts.get(i), pts.get(j));
                for (int k = 0; k < j; k++) {
                    if (!inside(circle, pts.get(k))) {
                        circle = fromThree(pts.get(i), pts.get(j), pts.get(k));
                    }
                }
            }
        }
        return circle;
    }

    private double dist(double ax, double ay, double bx, double by) {
        double dx = ax - bx;
        double dy = ay - by;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private boolean inside(double[] circle, double[] p) {
        return dist(circle[0], circle[1], p[0], p[1]) <= circle[2] + EPS;
    }

    private double[] fromTwo(double[] a, double[] b) {
        double cx = (a[0] + b[0]) / 2.0;
        double cy = (a[1] + b[1]) / 2.0;
        return new double[]{cx, cy, dist(a[0], a[1], b[0], b[1]) / 2.0};
    }

    private double[] fromThree(double[] a, double[] b, double[] c) {
        double ax = a[0], ay = a[1];
        double bx = b[0], by = b[1];
        double cx = c[0], cy = c[1];
        double d = 2.0 * (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by));
        if (Math.abs(d) < EPS) {
            // collinear -> the answer is the circle on the 2 farthest points
            double[] best = fromTwo(a, b);
            double[] c1 = fromTwo(a, c);
            if (c1[2] > best[2]) {
                best = c1;
            }
            double[] c2 = fromTwo(b, c);
            if (c2[2] > best[2]) {
                best = c2;
            }
            return best;
        }
        double a2 = ax * ax + ay * ay;
        double b2 = bx * bx + by * by;
        double c2 = cx * cx + cy * cy;
        double ux = (a2 * (by - cy) + b2 * (cy - ay) + c2 * (ay - by)) / d;
        double uy = (a2 * (cx - bx) + b2 * (ax - cx) + c2 * (bx - ax)) / d;
        return new double[]{ux, uy, dist(ux, uy, ax, ay)};
    }
}
