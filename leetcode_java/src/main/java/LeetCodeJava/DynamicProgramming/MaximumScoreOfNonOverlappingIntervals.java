package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-score-of-non-overlapping-intervals/description/

import java.util.*;

/**
 * 3414. Maximum Score of Non-overlapping Intervals
 * Hard
 *
 * You are given a 2D integer array intervals, where intervals[i] = [l_i, r_i, weight_i].
 * Interval i starts at position l_i and ends at r_i, and has a weight of weight_i. You can
 * choose up to 4 non-overlapping intervals. The score of the chosen intervals is defined as
 * the total sum of their weights.
 *
 * Return the lexicographically smallest array of at most 4 indices from intervals with
 * maximum score, representing your choice of non-overlapping intervals.
 *
 * Two intervals are said to be non-overlapping if they do not share any points. In
 * particular, intervals sharing a left or right boundary are considered overlapping.
 *
 *
 * Example 1:
 *
 * Input: intervals = [[1,3,2],[4,5,2],[1,5,5],[6,9,3],[6,7,1],[8,9,1]]
 * Output: [2,3]
 * Explanation: You can choose the intervals with indices 2, and 3 with respective weights
 * of 5, and 3.
 *
 * Example 2:
 *
 * Input: intervals = [[5,8,1],[6,7,7],[4,7,3],[9,10,6],[7,8,2],[11,14,3],[3,5,5]]
 * Output: [1,3,5,6]
 * Explanation: You can choose the intervals with indices 1, 3, 5, and 6 with respective
 * weights of 7, 6, 3, and 5.
 *
 *
 * Constraints:
 *
 * 1 <= intevals.length <= 5 * 10^4
 * intervals[i].length == 3
 * intervals[i] = [l_i, r_i, weight_i]
 * 1 <= l_i <= r_i <= 10^9
 * 1 <= weight_i <= 10^9
 *
 */
public class MaximumScoreOfNonOverlappingIntervals {

    // V0
    // IDEA: SUFFIX DP OVER (INTERVAL, BUDGET) + BINARY SEARCH, CARRYING THE WINNING INDEX LIST
    /**
     *  DP def (intervals sorted by left endpoint, scanned from the right):
     *     - score[i][j] = best total weight using only sorted[i:] while still allowed
     *                     to take j intervals
     *     - picks[i][j] = the SORTED list of original indices achieving it, kept
     *                     lexicographically smallest among equal scores
     *
     *  DP eq:
     *     - state(i, j) = better( state(i+1, j)                       // skip interval i
     *                           , weight_i + state(nxt(i), j-1) )     // take interval i
     *     - nxt(i) = first sorted interval whose left endpoint is STRICTLY past r_i
     *                (touching endpoints already overlap)
     *
     *  The tie-break is safe to optimise per sub-problem: inserting one fixed index
     *  into two sorted lists preserves their lexicographic order.
     *
     *  Answer: picks[0][4]
     *
     *  time  = O(N log N)   (4-fold constant; each list has at most 4 entries)
     *  space = O(N)
     */
    public int[] maximumWeight(List<List<Integer>> intervals) {
        int n = intervals.size();

        // sort original indices by left endpoint
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }
        Arrays.sort(order, (a, b) -> Integer.compare(intervals.get(a).get(0), intervals.get(b).get(0)));

        int[] sortedLefts = new int[n];
        for (int i = 0; i < n; i++) {
            sortedLefts[i] = intervals.get(order[i]).get(0);
        }

        // row n is the base case: score 0, no picks
        long[][] score = new long[n + 1][5];
        int[][][] picks = new int[n + 1][5][];
        for (int j = 0; j <= 4; j++) {
            picks[n][j] = new int[0];
        }

        for (int i = n - 1; i >= 0; i--) {
            int originalIndex = order[i];
            List<Integer> interval = intervals.get(originalIndex);
            int right = interval.get(1);
            int weight = interval.get(2);
            int next = firstLeftGreaterThan(sortedLefts, right);

            // taking 0 intervals is always the empty choice
            picks[i][0] = new int[0];
            for (int j = 1; j <= 4; j++) {
                // option 1: skip interval i
                long skipScore = score[i + 1][j];
                int[] skipPicks = picks[i + 1][j];

                // option 2: take interval i, then continue after it with j-1 left
                long takeScore = score[next][j - 1] + weight;
                int[] takePicks = insertSorted(picks[next][j - 1], originalIndex);

                boolean takeWins = takeScore > skipScore
                        || (takeScore == skipScore && compareLex(takePicks, skipPicks) < 0);
                if (takeWins) {
                    score[i][j] = takeScore;
                    picks[i][j] = takePicks;
                } else {
                    score[i][j] = skipScore;
                    picks[i][j] = skipPicks;
                }
            }
        }
        return picks[0][4];
    }

    /** Index of the first value in the sorted array that is strictly greater than target. */
    private int firstLeftGreaterThan(int[] sorted, int target) {
        int left = 0;
        int right = sorted.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sorted[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    /** Returns a new sorted array: the sorted input with value inserted in place. */
    private int[] insertSorted(int[] sortedList, int value) {
        int[] result = new int[sortedList.length + 1];
        int write = 0;
        boolean inserted = false;
        for (int x : sortedList) {
            if (!inserted && value < x) {
                result[write++] = value;
                inserted = true;
            }
            result[write++] = x;
        }
        if (!inserted) {
            result[write] = value;
        }
        return result;
    }

    /** Lexicographic compare; a proper prefix is the smaller one. */
    private int compareLex(int[] a, int[] b) {
        int common = Math.min(a.length, b.length);
        for (int i = 0; i < common; i++) {
            if (a[i] != b[i]) {
                return Integer.compare(a[i], b[i]);
            }
        }
        return Integer.compare(a.length, b.length);
    }
}
