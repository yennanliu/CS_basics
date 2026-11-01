package LeetCodeJava.Sort;

// https://leetcode.com/problems/number-of-intersecting-interval-pairs-i/description/

import java.util.Arrays;

/**
 * 4056. Number of Intersecting Interval Pairs I
 * Easy
 *
 * You are given a 2D integer array intervals of n elements, where
 * intervals[i] = [starti, endi] represents the closed interval from starti to
 * endi.
 *
 * Return the number of pairs of indices (i, j) such that 0 <= i < j < n and
 * intervals[i] and intervals[j] intersect.
 *
 * Two intervals intersect if they have at least one point in common, including
 * when they only share an endpoint.
 *
 *
 * Example 1:
 *
 * Input: intervals = [[1,2],[2,3],[3,4]]
 * Output: 2
 * Explanation:
 * There are 2 intersecting interval pairs:
 * Intervals [1, 2] and [2, 3] intersect at the point 2.
 * Intervals [2, 3] and [3, 4] intersect at the point 3.
 *
 * Example 2:
 *
 * Input: intervals = [[1,5],[2,4],[3,6]]
 * Output: 3
 * Explanation:
 * There are 3 intersecting interval pairs:
 * The intersection of [1, 5] and [2, 4] is [2, 4].
 * The intersection of [1, 5] and [3, 6] is [3, 5].
 * The intersection of [2, 4] and [3, 6] is [3, 4].
 *
 * Example 3:
 *
 * Input: intervals = [[1,2],[3,4],[5,6]]
 * Output: 0
 * Explanation:
 * There are no intersecting interval pairs. Hence, the answer is 0.
 *
 *
 * Constraints:
 *
 * 2 <= n == intervals.length <= 100
 * intervals[i] = [starti, endi]
 * 0 <= starti <= endi <= 100
 *
 */
public class NumberOfIntersectingIntervalPairsI {

    // V0
    // IDEA: SORT BY START + DOUBLE LOOP WITH EARLY BREAK
    //       pairs are unordered, so sorting does not change the answer. once sorted by start,
    //       for i < j we know start[j] >= start[i], so the two closed intervals meet iff
    //       start[j] <= end[i]; every later j starts even later, so the first miss -> break.
    /**
     * time = O(N^2)
     * space = O(1) (sort in place aside)
     */
    public int countIntersectingIntervals(int[][] intervals) {
        // edge case: fewer than 2 intervals form no pair
        if (intervals == null || intervals.length <= 1) {
            return 0;
        }

        // sort on start, then end : small -> big
        Arrays.sort(intervals, (a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(a[1], b[1]);
        });

        int count = 0;
        int n = intervals.length;
        for (int i = 0; i < n; i++) {
            int[] prev = intervals[i];
            for (int j = i + 1; j < n; j++) {
                int[] cur = intervals[j];
                // '>' not '>=' : closed intervals sharing an endpoint DO intersect
                if (cur[0] > prev[1]) {
                    // no later j can reach back to prev either
                    break;
                }
                count++;
            }
        }
        return count;
    }
}
