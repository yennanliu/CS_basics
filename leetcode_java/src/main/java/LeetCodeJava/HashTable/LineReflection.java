package LeetCodeJava.HashTable;

// https://leetcode.ca/all/356.html
// https://leetcode.com/problems/line-reflection/description/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *356. Line Reflection
 * Given n points on a 2D plane, find if there is such a line parallel to y-axis that reflect the given points.
 *
 * Example 1:
 *
 * Input: [[1,1],[-1,1]]
 * Output: true
 * Example 2:
 *
 * Input: [[1,1],[-1,-1]]
 * Output: false
 * Difficulty:
 * Medium
 */
public class LineReflection {

    // V0
    // IDEA: HASH SET + `minX + maxX` MIRROR
    /**
     *  If a vertical mirror line x = m exists, then the LEFT most and the
     *  RIGHT most point must be each other's reflection,
     *  so   2 * m = minX + maxX.
     *
     *  -> compute sum = minX + maxX ONCE, then every point (x, y)
     *     must have its partner (sum - x, y) present.
     *
     *  NOTE:
     *   - a HASH SET makes duplicated points harmless (they map to
     *     the same key, and a point is checked against the set only)
     *   - the `single column` case (all x equal) works out of the box:
     *     sum - x == x, so each point mirrors onto ITSELF -> true
     *
     *  time = O(N), space = O(N)
     */
    public boolean isReflected(int[][] points) {
        // edge
        if (points == null || points.length == 0) {
            return true;
        }

        Set<Long> set = new HashSet<>();
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;

        for (int[] p : points) {
            minX = Math.min(minX, p[0]);
            maxX = Math.max(maxX, p[0]);
            set.add(encodePoint(p[0], p[1]));
        }

        // sum == 2 * mirrorX  (avoids any floating point)
        long sum = (long) minX + (long) maxX;

        for (int[] p : points) {
            int mirrorX = (int) (sum - p[0]);
            if (!set.contains(encodePoint(mirrorX, p[1]))) {
                return false;
            }
        }

        return true;
    }

    // pack (x, y) into a single long, so no string building is needed
    private long encodePoint(int x, int y) {
        return (((long) x) << 32) | (y & 0xffffffffL);
    }

    // V0-2
    // IDEA: HASHSET (fixed by gpt)
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isReflected_0_2(int[][] points) {
        if (points == null || points.length == 0) return true;

        Set<String> pointSet = new HashSet<>();
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;

        // Build set and find min/max x
        for (int[] p : points) {
            minX = Math.min(minX, p[0]);
            maxX = Math.max(maxX, p[0]);
            pointSet.add(p[0] + "#" + p[1]); // Use string as key
        }

        // NOTE !!! the `mirror` may NOT exists at `x=0` line
        double mirror = (minX + maxX) / 2.0;

        // Check reflection
        for (int[] p : points) {
            double reflectedX = 2 * mirror - p[0];
            String reflectedPoint = (int)reflectedX + "#" + p[1];
            if (!pointSet.contains(reflectedPoint)) {
                return false;
            }
        }

        return true;
    }

    // V1
    // https://leetcode.ca/2016-11-20-356-Line-Reflection/
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isReflected_1(int[][] points) {
        final int inf = 1 << 30;
        int minX = inf, maxX = -inf;
        Set<List<Integer>> pointSet = new HashSet<>();
        for (int[] p : points) {
            minX = Math.min(minX, p[0]);
            maxX = Math.max(maxX, p[0]);

            //pointSet.add(List.of(p[0], p[1]));
            List<Integer> tmp = new ArrayList<>();
            tmp.add(p[0]);
            tmp.add(p[1]);
            pointSet.add(tmp);
        }
        int s = minX + maxX;
        for (int[] p : points) {
            // NOTE !!! the set holds List<Integer>, so the probe MUST be a
            //          List<Integer> as well (an int[] never `equals` a List)
            List<Integer> probe = new ArrayList<>();
            probe.add(s - p[0]);
            probe.add(p[1]);
            if (!pointSet.contains(probe)) {
                return false;
            }
        }
        return true;
    }

    // V2

}
