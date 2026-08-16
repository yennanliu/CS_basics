package LeetCodeJava.Greedy;

// https://leetcode.com/problems/set-intersection-size-at-least-two/description/

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
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


    // V1
    // IDEA: KEEP THE WHOLE CHOSEN SET EXPLICITLY
    /**
     *  Same sort order, but instead of tracking only the two largest picks we keep
     *  the full chosen list and count how many of its members fall inside the
     *  current interval.
     *
     *  Slower (a scan per interval) yet it RETURNS THE SET, not just its size --
     *  which is what a follow-up normally asks for.
     *
     *  time  = O(n log n + n * |S|)
     *  space = O(|S|)
     */
    public int intersectionSizeTwo_1(int[][] intervals) {
        int[][] sorted = intervals.clone();
        Arrays.sort(sorted, (x, y) -> x[1] != y[1] ? x[1] - y[1] : y[0] - x[0]);

        List<Integer> chosen = new ArrayList<>();
        for (int[] itv : sorted) {
            int have = 0;
            for (int p : chosen) {
                if (p >= itv[0] && p <= itv[1]) {
                    have += 1;
                }
            }
            // top up to two, always taking the RIGHTMOST allowed points
            for (int add = have; add < 2; add++) {
                int cand = itv[1] - (1 - add);
                if (!chosen.contains(cand)) {
                    chosen.add(cand);
                }
            }
        }
        return chosen.size();
    }

    // V2
    // IDEA: TreeSet OF CHOSEN POINTS + subSet counting
    /**
     *  A TreeSet answers `how many chosen points lie in [start, end]?` with
     *  subSet() in O(log n + hits) instead of a linear scan.
     *
     *  Same greedy, but the membership question is delegated to an ordered set --
     *  which is what you would reach for if the interval count grew.
     *
     *  time  = O(n log n)
     *  space = O(n)
     */
    public int intersectionSizeTwo_2(int[][] intervals) {
        int[][] sorted = intervals.clone();
        Arrays.sort(sorted, (x, y) -> x[1] != y[1] ? x[1] - y[1] : y[0] - x[0]);

        TreeSet<Integer> chosen = new TreeSet<>();
        for (int[] itv : sorted) {
            int have = chosen.subSet(itv[0], true, itv[1], true).size();
            for (int add = have; add < 2; add++) {
                int cand = itv[1] - (1 - add);
                chosen.add(cand);
            }
        }
        return chosen.size();
    }

    // V3
    // IDEA: SORT BY START DESCENDING and sweep the OTHER way
    /**
     *  The mirror image of V0: sort by START descending (ties: end ascending) and
     *  keep the two SMALLEST chosen points, adding `start` and `start + 1` when
     *  an interval is short of two.
     *
     *  A useful check on the greedy -- if both directions agree, the ordering
     *  argument is not hiding an asymmetry.
     *
     *  time  = O(n log n)
     *  space = O(1)
     */
    public int intersectionSizeTwo_3(int[][] intervals) {
        int[][] sorted = intervals.clone();
        Arrays.sort(sorted, (x, y) -> x[0] != y[0] ? y[0] - x[0] : x[1] - y[1]);

        int res = 0;
        int a = Integer.MAX_VALUE; // second smallest chosen point
        int b = Integer.MAX_VALUE; // smallest chosen point

        for (int[] itv : sorted) {
            int start = itv[0];
            int end = itv[1];

            if (end >= a) {
                continue;              // both points already inside
            }
            if (end >= b) {
                res += 1;              // only the smallest is inside
                a = b;
                b = start;
            } else {
                res += 2;
                a = start + 1;
                b = start;
            }
        }
        return res;
    }

}
