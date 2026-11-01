package LeetCodeJava.Math;

// https://leetcode.com/problems/maximum-number-of-intersections-on-the-chart/description/

import java.util.Map;
import java.util.TreeMap;

/**
 * 3009. Maximum Number of Intersections on the Chart
 * Hard
 *
 * There is a line chart consisting of n points connected by line segments. You are given a 1-indexed integer array y. The kth point has coordinates (k, y[k]). There are no horizontal lines; that is, no two consecutive points have the same y-coordinate.
 *
 * We can draw an infinitely long horizontal line. Return the maximum number of points of intersection of the line with the chart.
 *
 *
 * Example 1:
 *
 * Input: y = [1,2,1,2,1,3,2]
 * Output: 5
 * Explanation: The line y = 1.5 has 5 intersections with the chart.
 * We can also see that the line y = 2 would have 5 intersections as well.
 * It can be shown that there is no horizontal line with more than 5 intersections.
 *
 *
 * Constraints:
 *
 * 2 <= y.length <= 10^5
 * 1 <= y[i] <= 10^9
 * y[i] != y[i + 1] for i in range [1, n - 1]
 *
 */
public class MaximumNumberOfIntersectionsOnTheChart {

    // V0
    // IDEA: EACH SEGMENT IS AN INTERVAL OF y-VALUES -> SWEEP (difference map) FOR THE MAX OVERLAP
    //       double every y so half-integer lines become integers; drop each later segment's
    //       starting vertex so a shared vertex is counted once
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int maxIntersectionCount(int[] y) {
        // diff[v] = change in the number of segments covering value v (doubled coordinates)
        TreeMap<Long, Integer> diff = new TreeMap<>();

        for (int i = 0; i + 1 < y.length; i++) {
            long start = 2L * y[i];
            long end = 2L * y[i + 1];
            long low = Math.min(start, end);
            long high = Math.max(start, end);

            if (i > 0) {
                // the vertex at y[i] already belongs to the previous segment: exclude it here
                if (start == low) {
                    low += 1;
                } else {
                    high -= 1;
                }
            }
            diff.put(low, diff.getOrDefault(low, 0) + 1);
            diff.put(high + 1, diff.getOrDefault(high + 1, 0) - 1);
        }

        int best = 0;
        int covering = 0;
        for (Map.Entry<Long, Integer> entry : diff.entrySet()) {
            covering += entry.getValue();
            best = Math.max(best, covering);
        }
        return best;
    }
}
