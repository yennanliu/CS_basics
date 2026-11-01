package LeetCodeJava.Math;

// https://leetcode.com/problems/count-lattice-points-inside-a-circle/description/

/**
 * 2249. Count Lattice Points Inside a Circle
 * Medium
 *
 * Given a 2D integer array circles where circles[i] = [xi, yi, ri] represents the center (xi, yi) and radius ri of the ith circle drawn on a grid, return the number of lattice points that are present inside at least one circle.
 *
 * Note:
 *
 * A lattice point is a point with integer coordinates.
 * Points that lie on the circumference of a circle are also considered to be inside it.
 *
 *
 * Example 1:
 *
 * Input: circles = [[2,2,1]]
 * Output: 5
 * Explanation:
 * The figure above shows the given circle.
 * The lattice points present inside the circle are (1, 2), (2, 1), (2, 2), (2, 3), and (3, 2) and are shown in green.
 * Other points such as (1, 1) and (1, 3), which are shown in red, are not considered inside the circle.
 * Hence, the number of lattice points present inside at least one circle is 5.
 *
 * Example 2:
 *
 * Input: circles = [[2,2,2],[3,4,1]]
 * Output: 16
 * Explanation:
 * The figure above shows the given circles.
 * There are exactly 16 lattice points which are present inside at least one circle.
 * Some of them are (0, 2), (2, 0), (2, 4), (3, 2), and (4, 4).
 *
 *
 * Constraints:
 *
 * 1 <= circles.length <= 200
 * circles[i].length == 3
 * 1 <= xi, yi <= 100
 * 1 <= ri <= min(xi, yi)
 *
 */
public class CountLatticePointsInsideACircle {

    // V0
    // IDEA: THE COORDINATE SPACE IS TINY - MARK COVERED POINTS IN A GRID
    //   centres and radii are <= 100, so every covered point lies in [0, 200] x [0, 200].
    //   for each circle scan only its bounding box; squared distances keep the test exact.
    /**
     * time = O(circles * r^2)
     * space = O(200^2)
     */
    public int countLatticePoints(int[][] circles) {
        final int MAX_COORD = 200;
        boolean[][] covered = new boolean[MAX_COORD + 1][MAX_COORD + 1];
        int count = 0;

        for (int[] circle : circles) {
            int cx = circle[0];
            int cy = circle[1];
            int radius = circle[2];
            for (int x = cx - radius; x <= cx + radius; x++) {
                for (int y = cy - radius; y <= cy + radius; y++) {
                    int dx = x - cx;
                    int dy = y - cy;
                    // <= includes points on the circumference; the grid de-duplicates overlaps
                    if (dx * dx + dy * dy <= radius * radius && !covered[x][y]) {
                        covered[x][y] = true;
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
