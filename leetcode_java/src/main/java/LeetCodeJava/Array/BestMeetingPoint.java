package LeetCodeJava.Array;

// https://leetcode.com/problems/best-meeting-point/description/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 296. Best Meeting Point
 * Hard
 * Lock: Prime
 *
 * Given an m x n binary grid grid where each 1 marks the home of one friend,
 * return the minimal total travel distance.
 *
 * The total travel distance is the sum of the distances between the houses of the friends
 * and the meeting point.
 *
 * The distance is calculated using Manhattan Distance, where
 * distance(p1, p2) = |p2.x - p1.x| + |p2.y - p1.y|.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,0,0,0,1],[0,0,0,0,0],[0,0,1,0,0]]
 * Output: 6
 * Explanation: Given three friends living at (0,0), (0,4), and (2,2).
 * The point (0,2) is an ideal meeting point, as the total travel distance of 2 + 2 + 2 = 6
 * is minimal. So return 6.
 *
 * Example 2:
 *
 * Input: grid = [[1,1]]
 * Output: 1
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 200
 * grid[i][j] is either 0 or 1.
 * There will be at least two friends in the grid.
 *
 */
public class BestMeetingPoint {

    // V0
    // IDEA: MEDIAN (Manhattan distance decomposes into independent x and y axes)
    /**
     *  |dx| + |dy| means the row coordinate and the column coordinate can be optimized
     *  SEPARATELY. On a single axis, the point minimizing the sum of absolute distances
     *  is the MEDIAN.
     *
     *  Instead of computing the median explicitly, we pair up the OUTERMOST points:
     *  for a sorted list, (arr[-1] - arr[0]) + (arr[-2] - arr[1]) + ... is exactly
     *  the minimal total distance (any point between the innermost pair works).
     *
     *  time  = O(m * n)   // the column list needs a sort: O(m*n*log(m*n)) worst case
     *  space = O(m * n)
     */
    public int minTotalDistance(int[][] grid) {
        // edge
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        List<Integer> rows = new ArrayList<>();
        List<Integer> cols = new ArrayList<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    rows.add(i);
                    cols.add(j);
                }
            }
        }

        /** NOTE !!!
         *
         *  `rows` already comes out sorted (we scan top to bottom),
         *  but `cols` does NOT -> it must be sorted explicitly.
         */
        Collections.sort(cols);

        return minDist(rows) + minDist(cols);
    }

    /** sum of the gaps between symmetric outer pairs */
    private int minDist(List<Integer> arr) {
        int i = 0;
        int j = arr.size() - 1;
        int total = 0;
        while (i < j) {
            total += arr.get(j) - arr.get(i);
            i += 1;
            j -= 1;
        }
        return total;
    }


    // V1
    // IDEA: EXPLICIT MEDIAN (sum of |x - median| on each axis)
    /**
     *  The textbook statement of the same fact: on one axis the optimum meeting
     *  coordinate IS the median, so pick it and add up the absolute distances.
     *
     *  More obvious than V0's outer-pair trick, and it also hands you the actual
     *  meeting POINT, not just the cost.
     *
     *  time  = O(m * n * log(m * n))   // the column sort dominates
     *  space = O(m * n)
     */
    public int minTotalDistance_1(int[][] grid) {
        List<Integer> rows = new ArrayList<>();
        List<Integer> cols = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    rows.add(i);
                    cols.add(j);
                }
            }
        }
        Collections.sort(cols); // rows already ascending from the scan order

        int medR = rows.get(rows.size() / 2);
        int medC = cols.get(cols.size() / 2);

        int total = 0;
        for (int r : rows) {
            total += Math.abs(r - medR);
        }
        for (int c : cols) {
            total += Math.abs(c - medC);
        }
        return total;
    }

    // V2
    // IDEA: COLUMN-MAJOR SCAN -> NO SORT AT ALL
    /**
     *  V0 has to sort the column list because a row-major scan produces columns
     *  out of order. Scanning COLUMN-MAJOR for the columns (and row-major for the
     *  rows) makes both lists ascending by construction.
     *
     *  -> drops the log factor: a strict O(m * n).
     *
     *  time  = O(m * n)
     *  space = O(m * n)
     */
    public int minTotalDistance_2(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        List<Integer> rows = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    rows.add(i);
                }
            }
        }

        List<Integer> cols = new ArrayList<>();
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                if (grid[i][j] == 1) {
                    cols.add(j);
                }
            }
        }

        return minDistSorted(rows) + minDistSorted(cols);
    }

    private int minDistSorted(List<Integer> arr) {
        int i = 0;
        int j = arr.size() - 1;
        int total = 0;
        while (i < j) {
            total += arr.get(j) - arr.get(i);
            i += 1;
            j -= 1;
        }
        return total;
    }

    // V3
    // IDEA: PER-AXIS HISTOGRAM + PREFIX SWEEP (try every candidate coordinate)
    /**
     *  Count how many friends live on each row / column, then sweep the axis once
     *  keeping (people seen so far, cost so far) so the cost of EVERY candidate
     *  coordinate is produced in O(1) each. Take the minimum.
     *
     *  Does not rely on the median argument at all -- it just evaluates every
     *  option -- so it survives generalisations (weighted friends, blocked rows).
     *
     *  time  = O(m * n + m + n)
     *  space = O(m + n)
     */
    public int minTotalDistance_3(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        int[] rowCnt = new int[m];
        int[] colCnt = new int[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    rowCnt[i] += 1;
                    colCnt[j] += 1;
                }
            }
        }

        return bestAxisCost(rowCnt) + bestAxisCost(colCnt);
    }

    /** min over c of sum(cnt[i] * |i - c|), by a left sweep then a right sweep */
    private int bestAxisCost(int[] cnt) {
        int len = cnt.length;

        int[] left = new int[len];  // cost of everyone at index <= i, measured at i
        int seen = 0;
        int cost = 0;
        for (int i = 0; i < len; i++) {
            cost += seen;      // every previously seen person moves one step further
            seen += cnt[i];
            left[i] = cost;
        }

        int[] right = new int[len];
        seen = 0;
        cost = 0;
        for (int i = len - 1; i >= 0; i--) {
            cost += seen;
            seen += cnt[i];
            right[i] = cost;
        }

        int best = Integer.MAX_VALUE;
        for (int i = 0; i < len; i++) {
            best = Math.min(best, left[i] + right[i]);
        }
        return best;
    }

}
