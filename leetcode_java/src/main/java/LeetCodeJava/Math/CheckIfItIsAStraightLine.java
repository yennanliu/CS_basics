package LeetCodeJava.Math;

// https://leetcode.com/problems/check-if-it-is-a-straight-line/description/

/**
 * 1232. Check If It Is a Straight Line
 * Easy
 *
 * You are given an array coordinates, coordinates[i] = [x, y], where [x, y] represents the
 * coordinate of a point. Check if these points make a straight line in the XY plane.
 *
 *
 * Example 1:
 *
 * Input: coordinates = [[1,2],[2,3],[3,4],[4,5],[5,6],[6,7]]
 * Output: true
 *
 * Example 2:
 *
 * Input: coordinates = [[1,1],[2,2],[3,4],[4,5],[5,6],[7,7]]
 * Output: false
 *
 *
 * Constraints:
 *
 * 2 <= coordinates.length <= 1000
 * coordinates[i].length == 2
 * -10^4 <= coordinates[i][0], coordinates[i][1] <= 10^4
 * coordinates contains no duplicate point.
 *
 */
public class CheckIfItIsAStraightLine {

    // V0
    // IDEA: CROSS PRODUCT AGAINST THE FIRST TWO POINTS
    //   every point must be collinear with the first two. compare cross products instead of
    //   slopes, so a vertical line (dx == 0) needs no special case and nothing is divided:
    //   (x1 - x0) * (y - y0) == (y1 - y0) * (x - x0)
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean checkStraightLine(int[][] coordinates) {
        int x0 = coordinates[0][0], y0 = coordinates[0][1];
        int dx = coordinates[1][0] - x0;
        int dy = coordinates[1][1] - y0;

        for (int i = 2; i < coordinates.length; i++) {
            int x = coordinates[i][0];
            int y = coordinates[i][1];
            // coordinates are within 1e4, so each product stays far below Integer.MAX_VALUE
            if (dx * (y - y0) != dy * (x - x0)) {
                return false;
            }
        }
        return true;
    }
}
