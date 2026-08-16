package LeetCodeJava.Greedy;

// https://leetcode.com/problems/set-intersection-size-at-least-two/description/

import java.util.Arrays;

/**
 * 757. Set Intersection Size At Least Two
 * Hard
 *
 * You are given a 2D integer array intervals where intervals[i] = [start_i, end_i]
 * represents all the integers from start_i to end_i inclusively.
 *
 * A containing set is an array nums where each interval from intervals has at least
 * two integers in nums.
 *
 *   - For example, if intervals = [[1,3], [3,7], [8,9]], then [1,2,4,7,8,9] and
 *     [2,3,4,8,9] are containing sets.
 *
 * Return the minimum possible size of a containing set.
 *
 *
 * Example 1:
 *
 * Input: intervals = [[1,3],[3,7],[8,9]]
 * Output: 5
 * Explanation: let nums = [2, 3, 4, 8, 9].
 * It can be shown that there cannot be any containing array of size 4.
 *
 * Example 2:
 *
 * Input: intervals = [[1,3],[1,4],[2,5],[3,5]]
 * Output: 3
 * Explanation: let nums = [2, 3, 4].
 * It can be shown that there cannot be any containing array of size 2.
 *
 * Example 3:
 *
 * Input: intervals = [[1,2],[2,3],[2,4],[4,5]]
 * Output: 5
 * Explanation: let nums = [1, 2, 3, 4, 5].
 * It can be shown that there cannot be any containing array of size 4.
 *
 *
 * Constraints:
 *
 * 1 <= intervals.length <= 3000
 * intervals[i].length == 2
 * 0 <= start_i < end_i <= 10^8
 *
 */
public class SetIntersectionSizeAtLeastTwo {

    // V0
    // IDEA: GREEDY (sort by end ASC, start DESC; always pick the LARGEST points)
    /**
     *   Sort by END ascending -- then when we must add points for an interval, picking
     *   the LARGEST possible ones (end and end-1) maximises the chance they are REUSED
     *   by later intervals (whose ends are all >= this one).
     *
     *   The secondary `start DESCENDING` ordering makes TIGHTER intervals come first
     *   among equal ends, so we never pick points that a tighter sibling can't use.
     *
     *   We only need the TWO LARGEST chosen points (a < b) -- every previously chosen
     *   point is <= b, and any earlier point is too small to help future intervals.
     *     - start <= a : both a and b fall in [start, end] -> nothing to add
     *     - start <= b : only b is inside -> add ONE point, the largest (end)
     *     - else       : none inside -> add TWO points, end - 1 and end
     *
     *   time  = O(n log n)
     *   space = O(1) (ignoring the sort)
     */
    public int intersectionSizeTwo(int[][] intervals) {
        int[][] sorted = intervals.clone();
        Arrays.sort(sorted, (x, y) -> x[1] != y[1] ? x[1] - y[1] : y[0] - x[0]);

        int res = 0;
        int a = -1; // second largest chosen point
        int b = -1; // largest chosen point

        for (int[] itv : sorted) {
            int start = itv[0];
            int end = itv[1];

            if (start <= a) {
                // both a and b are inside [start, end] already
                continue;
            }
            if (start <= b) {
                // only b is inside -> one more point, as far RIGHT as allowed
                res += 1;
                a = b;
                b = end;
            } else {
                // nothing inside -> take the two RIGHTMOST points
                res += 2;
                a = end - 1;
                b = end;
            }
        }

        return res;
    }

}
