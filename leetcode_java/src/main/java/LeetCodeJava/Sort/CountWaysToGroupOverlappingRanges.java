package LeetCodeJava.Sort;

// https://leetcode.com/problems/count-ways-to-group-overlapping-ranges/description/

import java.util.*;

/**
 * 2580. Count Ways to Group Overlapping Ranges
 * Medium
 *
 * You are given a 2D integer array ranges where ranges[i] = [starti, endi] denotes that all integers
 * between starti and endi (both inclusive) are contained in the ith range.
 *
 * You are to split ranges into two (possibly empty) groups such that:
 *
 * Each range belongs to exactly one group.
 * Any two overlapping ranges must belong to the same group.
 *
 * Two ranges are said to be overlapping if there exists at least one integer that is present in
 * both ranges.
 *
 * For example, [1, 3] and [2, 5] are overlapping because 2 and 3 occur in both ranges.
 *
 * Return the total number of ways to split ranges into two groups.
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: ranges = [[6,10],[5,15]]
 * Output: 2
 * Explanation:
 * The two ranges are overlapping, so they must be in the same group.
 * Thus, there are two possible ways:
 * - Put both the ranges together in group 1.
 * - Put both the ranges together in group 2.
 *
 * Example 2:
 *
 * Input: ranges = [[1,3],[10,20],[2,5],[4,8]]
 * Output: 4
 * Explanation:
 * Ranges [1,3], and [2,5] are overlapping. So, they must be in the same group.
 * Again, ranges [2,5] and [4,8] are also overlapping. So, they must also be in the same group.
 * Thus, there are four possible ways to group them:
 * - All the ranges in group 1.
 * - All the ranges in group 2.
 * - Ranges [1,3], [2,5], and [4,8] in group 1 and [10,20] in group 2.
 * - Ranges [1,3], [2,5], and [4,8] in group 2 and [10,20] in group 1.
 *
 *
 * Constraints:
 *
 * 1 <= ranges.length <= 10^5
 * ranges[i].length == 2
 * 0 <= starti <= endi <= 10^9
 *
 */
public class CountWaysToGroupOverlappingRanges {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: SORT + MERGE INTERVALS + POWER OF TWO
    //       overlap is transitive inside a cluster, so every cluster of overlapping ranges is
    //       one atom that must go to a single group. k independent clusters -> 2^k ways.
    //       the clusters are exactly the merged intervals: sort by start, sweep with the
    //       furthest end seen so far; a start strictly past it opens a new cluster.
    /**
     * time = O(N log N)
     * space = O(log N)
     */
    public int countWays(int[][] ranges) {
        // sort by start, so overlapping ranges become neighbours in the sweep
        Arrays.sort(ranges, (a, b) -> Integer.compare(a[0], b[0]));

        int clusters = 0;
        // furthest end covered by the current cluster (-1 = nothing yet; starts are >= 0)
        int furthestEnd = -1;
        for (int[] range : ranges) {
            int start = range[0];
            int end = range[1];
            // ranges are inclusive, so start == furthestEnd still overlaps -> strictly greater
            if (start > furthestEnd) {
                clusters++;
            }
            furthestEnd = Math.max(furthestEnd, end);
        }

        // each cluster independently picks group 1 or group 2
        long ways = 1;
        for (int i = 0; i < clusters; i++) {
            ways = (ways * 2) % MOD;
        }
        return (int) ways;
    }
}
