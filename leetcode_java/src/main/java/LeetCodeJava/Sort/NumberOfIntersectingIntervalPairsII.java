package LeetCodeJava.Sort;

// https://leetcode.com/problems/number-of-intersecting-interval-pairs-ii/description/

import java.util.Arrays;

/**
 * 4057. Number of Intersecting Interval Pairs II
 * Medium
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
 * 2 <= n == intervals.length <= 10^5
 * intervals[i] = [starti, endi]
 * 0 <= starti <= endi <= 10^9
 *
 */
public class NumberOfIntersectingIntervalPairsII {

    // V0
    // IDEA: SCAN LINE + SORT -- EACH NEW START MEETS EVERY INTERVAL STILL OPEN
    //       n = 10^5 rules out LC 4056's double loop. turn every interval into (start, +1) and
    //       (end, -1) events and sweep left -> right keeping active = intervals open right now.
    //       two intervals intersect iff, when the later-starting one opens, the other is still
    //       open -> every pair is counted once: result += active, then active++.
    //       NOT max C(active, 2) over the sweep -- that is only the biggest group at one point.
    //       at the same coordinate starts go BEFORE ends, since touching closed intervals meet.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long countIntersectingIntervals(int[][] intervals) {
        // edge case: fewer than 2 intervals form no pair
        if (intervals == null || intervals.length <= 1) {
            return 0;
        }

        // events[k] = {position, delta}: delta = 1 opens an interval, -1 closes one
        int n = intervals.length;
        int[][] events = new int[2 * n][];
        for (int i = 0; i < n; i++) {
            events[2 * i] = new int[] { intervals[i][0], 1 };
            events[2 * i + 1] = new int[] { intervals[i][1], -1 };
        }

        // position ascending; at the same position a start (+1) before an end (-1)
        Arrays.sort(events, (a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(b[1], a[1]);
        });

        // up to C(10^5, 2) ~ 5 * 10^9 pairs -> long
        long pairs = 0;
        int active = 0;
        for (int[] event : events) {
            if (event[1] == 1) {
                // the new interval meets every one still open
                pairs += active;
                active++;
            } else {
                active--;
            }
        }
        return pairs;
    }

    // V1
    // IDEA: COMPLEMENT COUNT -- ALL PAIRS MINUS DISJOINT PAIRS (SORT + BINARY SEARCH)
    //       two closed intervals are disjoint iff one ends strictly before the other starts,
    //       and then exactly one of them is "the one that ends first", so each disjoint pair
    //       is counted once by: for every interval i, #intervals with end < start[i].
    //       sort the ends once and binary search each start.
    //       answer = n * (n - 1) / 2 - disjointPairs.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long countIntersectingIntervals_1(int[][] intervals) {
        // edge case: fewer than 2 intervals form no pair
        if (intervals == null || intervals.length <= 1) {
            return 0;
        }

        int n = intervals.length;
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            ends[i] = intervals[i][1];
        }
        Arrays.sort(ends);

        long disjointPairs = 0;
        for (int[] interval : intervals) {
            // how many intervals finish strictly before this one starts
            disjointPairs += countLessThan(ends, interval[0]);
        }

        long totalPairs = (long) n * (n - 1) / 2;
        return totalPairs - disjointPairs;
    }

    /** Number of elements in the sorted array that are strictly less than target. */
    private int countLessThan(int[] sorted, int target) {
        int left = 0;
        int right = sorted.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sorted[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
}
