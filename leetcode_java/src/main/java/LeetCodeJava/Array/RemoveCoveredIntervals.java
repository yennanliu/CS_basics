package LeetCodeJava.Array;

// https://leetcode.com/problems/remove-covered-intervals/description/

import java.util.Arrays;

/**
 *  1288. Remove Covered Intervals
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given an array intervals where intervals[i] = [li, ri] represent the interval [li, ri), remove all intervals that are covered by another interval in the list.
 *
 * The interval [a, b) is covered by the interval [c, d) if and only if c <= a and b <= d.
 *
 * Return the number of remaining intervals.
 *
 *
 *
 * Example 1:
 *
 * Input: intervals = [[1,4],[3,6],[2,8]]
 * Output: 2
 * Explanation: Interval [3,6] is covered by [2,8], therefore it is removed.
 * Example 2:
 *
 * Input: intervals = [[1,4],[2,3]]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= intervals.length <= 1000
 * intervals[i].length == 2
 * 0 <= li < ri <= 105
 * All the given intervals are unique.
 *
 *
 *
 */
public class RemoveCoveredIntervals {

    // V0
//    public int removeCoveredIntervals(int[][] intervals) {
//
//    }

    // V1

    // V2
    // https://leetcode.com/problems/remove-covered-intervals/solutions/7120872/greedy-interval-filtering-by-yashkambari-ecmu/
    public int removeCoveredIntervals_2(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> {
            if (a[0] == b[0]) {
                return Integer.compare(b[1], a[1]);
            } else {
                return Integer.compare(a[0], b[0]);
            }
        });

        int l = intervals[0][0];
        int r = intervals[0][1];
        int removed = 0;

        for (int i = 1; i < intervals.length; i++) {
            if (l <= intervals[i][0] && intervals[i][1] <= r) {
                removed++; // interval covered
            } else {
                l = intervals[i][0];
                r = intervals[i][1];
            }
        }
        return intervals.length - removed;
    }


    // V3
    // https://leetcode.com/problems/remove-covered-intervals/solutions/1784874/beginner-friendly-javajavascriptpython-s-uend/
    public int removeCoveredIntervals_3(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> (a[0] == b[0] ? b[1] - a[1] : a[0] - b[0]));
        int count = 0, cur = 0;
        for (int interval[] : intervals) {
            if (cur < interval[1]) {
                cur = interval[1];
                count++;
            }
        }
        return count;
    }




}
