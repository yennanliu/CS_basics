package LeetCodeJava.Math;

// https://leetcode.com/problems/find-maximum-area-of-a-triangle/

import java.util.HashMap;
import java.util.Map;

/**
 *  3588. Find Maximum Area of a Triangle
 *  Medium
 *
 *  You are given a 2D array coords of size n x 2, representing the coordinates of n
 *  points in an infinite Cartesian plane.
 *
 *  Find twice the maximum area of a triangle with its corners at any three elements from
 *  coords, such that at least one side of this triangle is parallel to the x-axis or
 *  y-axis. Formally, if the maximum area of such a triangle is A, return 2 * A.
 *
 *  If no such triangle exists, return -1.
 *  Note that a triangle cannot have zero area.
 *
 *  Example 1:
 *    Input: coords = [[1,1],[1,2],[3,2],[3,3]]
 *    Output: 2
 *    Explanation: the triangle (1,1), (1,2), (3,2) has base 1 and height 2 -> area 1.
 *
 *  Example 2:
 *    Input: coords = [[1,1],[2,2],[3,3]]
 *    Output: -1
 *    Explanation: the only possible triangle has no axis-parallel side.
 *
 *  Constraints:
 *    1 <= n == coords.length <= 10^5
 *    1 <= coords[i][0], coords[i][1] <= 10^6
 *    All coords[i] are unique.
 */
public class FindMaximumAreaOfATriangle {

    // V0
    // IDEA: THE AXIS-PARALLEL SIDE IS THE BASE; THE APEX IS A GLOBAL EXTREME
    //
    //   fix the base as two points sharing a y value; the widest such pair is always best
    //   because 2*area = base_length * height and the two factors are independent. the
    //   height is the vertical distance from that y to the apex, so the apex should be
    //   whichever of the globally smallest / largest y is further away.
    //
    //   running the same sweep with the two axes swapped covers vertical sides. grouping
    //   by the shared coordinate and keeping only each group's min/max is all the
    //   bookkeeping required.
    /**
     * time = O(N)
     * space = O(N)
     */
    public long maxArea(int[][] coords) {
        return Math.max(scan(coords, 0, 1), scan(coords, 1, 0));
    }

    // best base_length * height over sides parallel to axis `baseAxis`
    private long scan(int[][] coords, int baseAxis, int otherAxis) {
        Map<Integer, int[]> groups = new HashMap<>();   // shared coord -> {minBase, maxBase}
        int lo = Integer.MAX_VALUE;
        int hi = Integer.MIN_VALUE;
        for (int[] p : coords) {
            int b = p[baseAxis];
            int o = p[otherAxis];
            lo = Math.min(lo, o);
            hi = Math.max(hi, o);
            int[] cur = groups.get(o);
            if (cur == null) {
                groups.put(o, new int[]{b, b});
            } else {
                cur[0] = Math.min(cur[0], b);
                cur[1] = Math.max(cur[1], b);
            }
        }

        long best = -1;
        for (Map.Entry<Integer, int[]> e : groups.entrySet()) {
            int o = e.getKey();
            long base = (long) e.getValue()[1] - e.getValue()[0];
            if (base == 0) {
                continue;
            }
            long height = Math.max((long) o - lo, (long) hi - o);
            if (height > 0) {
                best = Math.max(best, base * height);
            }
        }
        return best;
    }
}
