package LeetCodeJava.Tree;

// https://leetcode.com/problems/rectangle-area-ii/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * 850. Rectangle Area II
 * Hard
 *
 * You are given a 2D array of axis-aligned rectangles. Each rectangle[i] =
 * [xi1, yi1, xi2, yi2] denotes the ith rectangle where (xi1, yi1) are the coordinates
 * of the bottom-left corner, and (xi2, yi2) are the coordinates of the top-right corner.
 *
 * Calculate the total area covered by all rectangles in the plane.
 * Any area covered by two or more rectangles should only be counted once.
 *
 * Return the total area. Since the answer may be too large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: rectangles = [[0,0,2,2],[1,0,2,3],[1,0,3,1]]
 * Output: 6
 * Explanation: A total area of 6 is covered by all three rectangles.
 * From (1,1) to (2,2), the green and red rectangles overlap.
 * From (1,0) to (2,3), all three rectangles overlap.
 *
 * Example 2:
 *
 * Input: rectangles = [[0,0,1000000000,1000000000]]
 * Output: 49
 * Explanation: The answer is 10^18 modulo (10^9 + 7), which is 49.
 *
 *
 * Constraints:
 *
 * 1 <= rectangles.length <= 200
 * rectanges[i].length == 4
 * 0 <= xi1, yi1, xi2, yi2 <= 10^9
 * xi1 <= xi2
 * yi1 <= yi2
 * All rectangles have non zero area.
 *
 */
public class RectangleArea2 {

    // V0
    // IDEA: SWEEP LINE + COORDINATE COMPRESSION
    /**
     *   Sweep a HORIZONTAL line upward through all distinct y values.
     *   Between two consecutive y values the set of active rectangles NEVER
     *   changes, so the covered area of that horizontal SLAB is
     *
     *       (covered x length) * (slab height)
     *
     *   To get the covered x length, COMPRESS all distinct x coordinates into
     *   at most 2n `columns` and keep a counter per column of how many active
     *   rectangles cover it. A column contributes its width when its counter > 0.
     *
     *   Each rectangle becomes TWO events:
     *       (y1, +1, x1, x2)  bottom edge -> the rectangle becomes ACTIVE
     *       (y2, -1, x1, x2)  top edge    -> the rectangle becomes INACTIVE
     *
     *   NOTE !!! take the modulo ONLY at the very end - taking it earlier would
     *            corrupt the running width/height arithmetic.
     *
     *   NOTE !!! coordinates reach 10^9 so a single slab is up to 10^18
     *            -> `area` and the width arithmetic MUST be `long`.
     *            (the total stays under 10^18 because the slabs are disjoint in y)
     *
     *   time  = O(n^2)     // 2n events, each rescanning 2n columns
     *   space = O(n)
     */
    public int rectangleArea(int[][] rectangles) {
        final long MOD = 1_000_000_007L;

        // compress the x axis
        TreeSet<Integer> xsSet = new TreeSet<>();
        for (int[] r : rectangles) {
            xsSet.add(r[0]);
            xsSet.add(r[2]);
        }
        Integer[] xs = xsSet.toArray(new Integer[0]);

        Map<Integer, Integer> pos = new HashMap<>();
        for (int i = 0; i < xs.length; i++) {
            pos.put(xs[i], i);
        }

        // sweep events {y, delta, x1, x2}, sorted by y
        int[][] events = new int[rectangles.length * 2][4];
        int e = 0;
        for (int[] r : rectangles) {
            events[e++] = new int[] { r[1], 1, r[0], r[2] };
            events[e++] = new int[] { r[3], -1, r[0], r[2] };
        }
        Arrays.sort(events, (a, b) -> Integer.compare(a[0], b[0]));

        // count[i] = how many active rectangles cover column [xs[i], xs[i+1])
        int[] count = new int[xs.length];
        long area = 0;
        int prevY = events[0][0];

        for (int[] ev : events) {
            int y = ev[0];
            int delta = ev[1];

            // close off the slab between prevY and y with the CURRENT coverage
            long covered = 0;
            for (int i = 0; i + 1 < xs.length; i++) {
                if (count[i] > 0) {
                    covered += (long) xs[i + 1] - xs[i];
                }
            }
            area += covered * (y - prevY);
            prevY = y;

            // THEN apply this event
            for (int i = pos.get(ev[2]); i < pos.get(ev[3]); i++) {
                count[i] += delta;
            }
        }

        return (int) (area % MOD);
    }

}
