package LeetCodeJava.Array;

// https://leetcode.ca/all/1272.html
// https://leetcode.com/problems/remove-interval/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 1272. Remove Interval
 * Given a sorted list of disjoint intervals, each interval intervals[i] = [a, b] represents the set of real numbers x such that a <= x < b.
 *
 * We remove the intersections between any interval in intervals and the interval toBeRemoved.
 *
 * Return a sorted list of intervals after all such removals.
 *
 *
 *
 * Example 1:
 *
 * Input: intervals = [[0,2],[3,4],[5,7]], toBeRemoved = [1,6]
 * Output: [[0,1],[6,7]]
 * Example 2:
 *
 * Input: intervals = [[0,5]], toBeRemoved = [2,3]
 * Output: [[0,2],[3,5]]
 *
 *
 * Constraints:
 *
 * 1 <= intervals.length <= 10^4
 * -10^9 <= intervals[i][0] < intervals[i][1] <= 10^9
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Google
 * Problem Solution
 * 1272-Remove-Interval
 *
 *
 *
 */
public class RemoveInterval {

    // V0
    // IDEA: INTERVAL OP + ARRAY
    public List<List<Integer>> removeInterval(int[][] intervals, int[] toBeRemoved) {
        List<List<Integer>> res = new ArrayList<>();
        int rStart = toBeRemoved[0];
        int rEnd = toBeRemoved[1];

        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];

            // 1. Check if there is no overlap at all
            if (end <= rStart || start >= rEnd) {
                res.add(Arrays.asList(start, end));
            } else {
                // 2. Overlap exists. There are two potential remaining parts:

                // Part A: The segment before the removal range starts
                if (start < rStart) {
                    res.add(Arrays.asList(start, rStart));
                }

                // Part B: The segment after the removal range ends
                if (end > rEnd) {
                    res.add(Arrays.asList(rEnd, end));
                }
            }
        }
        return res;
    }


    // V0-1
    // IDEA: SORT + INTERVAL (GPT)
    // TODO: validate
    public List<List<Integer>> removeInterval_0_1(
            int[][] intervals,
            int[] toBeRemoved) {

        List<List<Integer>> res = new ArrayList<>();

        int removeStart = toBeRemoved[0];
        int removeEnd = toBeRemoved[1];

        for (int[] interval : intervals) {

            int start = interval[0];
            int end = interval[1];

            // ------------------------------------------------
            // Case 1:
            // left remaining portion exists
            //
            // Example:
            // interval     = [0,5]
            // toBeRemoved  = [2,3]
            //
            // keep [0,2]
            // ------------------------------------------------
            if (start < removeStart) {

                int leftEnd = Math.min(end, removeStart);

                // valid interval only
                if (start < leftEnd) {

                    res.add(Arrays.asList(start, leftEnd));
                }
            }


            // ------------------------------------------------
            // Case 2:
            // right remaining portion exists
            //
            // Example:
            // interval     = [0,5]
            // toBeRemoved  = [2,3]
            //
            // keep [3,5]
            // ------------------------------------------------
            if (end > removeEnd) {

                int rightStart = Math.max(start, removeEnd);

                // valid interval only
                if (rightStart < end) {

                    res.add(Arrays.asList(rightStart, end));
                }
            }
        }

        return res;
    }



    // V0-2
    // IDEA: (GEMINI)
    // TODO: validate
    public List<List<Integer>> removeInterval_0_2(int[][] intervals, int[] toBeRemoved) {
        List<List<Integer>> res = new ArrayList<>();
        int rStart = toBeRemoved[0];
        int rEnd = toBeRemoved[1];

        for (int[] interval : intervals) {
            int start = interval[0];
            int end = interval[1];

            // 1. Check if there is no overlap at all
            if (end <= rStart || start >= rEnd) {
                res.add(Arrays.asList(start, end));
            } else {
                // 2. Overlap exists. There are two potential remaining parts:

                // Part A: The segment before the removal range starts
                if (start < rStart) {
                    res.add(Arrays.asList(start, rStart));
                }

                // Part B: The segment after the removal range ends
                if (end > rEnd) {
                    res.add(Arrays.asList(rEnd, end));
                }
            }
        }
        return res;
    }



    // V1
    // https://leetcode.ca/2019-05-25-1272-Remove-Interval/
    public List<List<Integer>> removeInterval_1(int[][] intervals, int[] toBeRemoved) {
        int x = toBeRemoved[0], y = toBeRemoved[1];
        List<List<Integer>> ans = new ArrayList<>();
        for (int[] e : intervals) {
            int a = e[0], b = e[1];
            if (a >= y || b <= x) {
                ans.add(Arrays.asList(a, b));
            } else {
                if (a < x) {
                    ans.add(Arrays.asList(a, x));
                }
                if (b > y) {
                    ans.add(Arrays.asList(y, b));
                }
            }
        }
        return ans;
    }



    // V2



    // V3





}
