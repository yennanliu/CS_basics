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
//    public List<List<Integer>> removeInterval(int[][] intervals, int[] toBeRemoved) {
//    }


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
