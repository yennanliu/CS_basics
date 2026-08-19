package LeetCodeJava.Math;

// https://leetcode.com/problems/maximum-number-of-darts-inside-of-a-circular-dartboard/

/**
 *  1453. Maximum Number of Darts Inside of a Circular Dartboard
 *  Hard
 *
 *  Alice is throwing n darts on a very large wall. You are given an array darts where
 *  darts[i] = [xi, yi] is the position of the ith dart that Alice threw on the wall.
 *
 *  Bob knows the positions of the n darts on the wall. He wants to place a dartboard of
 *  radius r on the wall so that the maximum number of darts that Alice throws lie on the
 *  dartboard.
 *
 *  Given the integer r, return the maximum number of darts that can lie on the dartboard.
 *
 *  Example 1:
 *    Input: darts = [[-2,0],[2,0],[0,2],[0,-2]], r = 2
 *    Output: 4
 *    Explanation: circle centered at (0,0) with radius = 2 contains all points.
 *
 *  Example 2:
 *    Input: darts = [[-3,0],[3,0],[2,6],[5,4],[0,9],[7,8]], r = 5
 *    Output: 5
 *    Explanation: circle centered at (0,4) with radius = 5 contains all but (7,8).
 *
 *  Constraints:
 *    1 <= darts.length <= 100
 *    darts[i].length == 2
 *    -10^4 <= xi, yi <= 10^4
 *    All the darts are unique
 *    1 <= r <= 5000
 */
public class MaximumNumberOfDartsInsideOfACircularDartboard {

    private static final double EPS = 1e-7;

    // V0
    // IDEA: GEOMETRY - "the best circle can always be pushed onto 2 points"
    //
    //   KEY OBSERVATION:
    //     take any optimal dartboard. slide it until its boundary touches a dart, then
    //     rotate it around that dart until the boundary touches a 2nd dart. the set of
    //     covered darts never shrinks. so an optimal circle either
    //       (a) covers just 1 dart, or
    //       (b) has 2 of the darts exactly on its boundary.
    //
    //   -> enumerate every pair (i, j) with dist(i, j) <= 2r, build the (at most 2)
    //      circle centers of radius r through both, and count covered darts.
    //
    //   NOTE: use an epsilon when comparing distances - the centers are floats.
    /**
     * time = O(N^3)
     * space = O(1)
     */
    public int numPoints(int[][] darts, int r) {
        int n = darts.length;
        double rr = (double) r * r;

        int res = 1;                    // a single dart is always coverable
        for (int i = 0; i < n; i++) {
            double x1 = darts[i][0];
            double y1 = darts[i][1];
            for (int j = i + 1; j < n; j++) {
                double x2 = darts[j][0];
                double y2 = darts[j][1];
                double dx = x2 - x1;
                double dy = y2 - y1;
                double d2 = dx * dx + dy * dy;
                if (d2 > 4.0 * rr) {
                    continue;           // too far apart for any radius-r circle
                }
                double d = Math.sqrt(d2);
                double mx = (x1 + x2) / 2.0;
                double my = (y1 + y2) / 2.0;
                // distance from the chord midpoint to the circle center
                double h = Math.sqrt(Math.max(0.0, rr - d2 / 4.0));
                // unit vector perpendicular to the chord
                double ux = -dy / d;
                double uy = dx / d;
                res = Math.max(res, count(darts, mx + h * ux, my + h * uy, rr));
                res = Math.max(res, count(darts, mx - h * ux, my - h * uy, rr));
            }
        }
        return res;
    }

    private int count(int[][] darts, double cx, double cy, double rr) {
        int c = 0;
        for (int[] p : darts) {
            double dx = p[0] - cx;
            double dy = p[1] - cy;
            if (dx * dx + dy * dy <= rr + EPS) {
                c++;
            }
        }
        return c;
    }
}
