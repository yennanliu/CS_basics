package LeetCodeJava.Sort;

// https://leetcode.com/problems/find-maximal-uncovered-ranges/description/

import java.util.*;

/**
 * 2655. Find Maximal Uncovered Ranges
 * Medium
 *
 * You are given an integer n which is the length of a 0-indexed array nums, and a 0-indexed 2D-array ranges, which is a list of sub-ranges of nums (sub-ranges may overlap).
 *
 * Each row ranges[i] has exactly 2 cells:
 *
 * ranges[i][0], which shows the start of the ith range (inclusive)
 * ranges[i][1], which shows the end of the ith range (inclusive)
 *
 * These ranges cover some cells of nums and leave some cells uncovered. Your task is to find all of the uncovered ranges with maximal length.
 *
 * Return a 2D-array answer of the uncovered ranges, sorted by the starting point in ascending order.
 *
 * By all of the uncovered ranges with maximal length, we mean satisfying two conditions:
 *
 * Each uncovered cell should belong to exactly one sub-range
 * There should not exist two ranges (l1, r1) and (l2, r2) such that r1 + 1 = l2
 *
 *
 * Example 1:
 *
 * Input: n = 10, ranges = [[3,5],[7,8]]
 * Output: [[0,2],[6,6],[9,9]]
 * Explanation: The ranges (3, 5) and (7, 8) are covered, so if we simplify the array nums to a binary array where 0 shows an uncovered cell and 1 shows a covered cell, the array becomes [0,0,0,1,1,1,0,1,1,0] in which we can observe that the ranges (0, 2), (6, 6) and (9, 9) aren't covered.
 *
 * Example 2:
 *
 * Input: n = 3, ranges = [[0,2]]
 * Output: []
 * Explanation: In this example, the whole of the array nums is covered and there are no uncovered cells so the output is an empty array.
 *
 * Example 3:
 *
 * Input: n = 7, ranges = [[2,4],[0,3]]
 * Output: [[5,6]]
 * Explanation: The ranges (0, 3) and (2, 4) are covered, so if we simplify the array nums to a binary array where 0 shows an uncovered cell and 1 shows a covered cell, the array becomes [1,1,1,1,1,0,0] in which we can observe that the range (5, 6) is uncovered.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^9
 * 0 <= ranges.length <= 10^6
 * ranges[i].length = 2
 * 0 <= ranges[i][j] <= n - 1
 * ranges[i][0] <= ranges[i][1]
 *
 */
public class FindMaximalUncoveredRanges {

    // V0
    // IDEA: SORT BY LEFT ENDPOINT + SWEEP THE "FURTHEST COVERED" WATERMARK
    //       n can be 10^9, so never build the array; work on the intervals. keep
    //       lastCovered = the largest index covered so far. an interval starting beyond
    //       lastCovered + 1 leaves the gap [lastCovered + 1, start - 1] uncovered.
    /**
     * time = O(M log M)
     * space = O(M)
     */
    public int[][] findMaximalUncoveredRanges(int n, int[][] ranges) {
        // sort a copy by start, so the caller's array is left alone
        int[][] sorted = new int[ranges.length][];
        for (int i = 0; i < ranges.length; i++) {
            sorted[i] = ranges[i];
        }
        Arrays.sort(sorted, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> gaps = new ArrayList<>();
        // -1 means nothing is covered yet
        long lastCovered = -1;
        for (int[] range : sorted) {
            int start = range[0];
            int end = range[1];
            if (lastCovered + 1 < start) {
                gaps.add(new int[] {(int) (lastCovered + 1), start - 1});
            }
            // max, not assignment: a range nested inside an earlier one must not shrink it
            lastCovered = Math.max(lastCovered, end);
        }
        // the tail after the last covered cell (also handles ranges == [])
        if (lastCovered + 1 < n) {
            gaps.add(new int[] {(int) (lastCovered + 1), n - 1});
        }

        int[][] result = new int[gaps.size()][];
        for (int i = 0; i < gaps.size(); i++) {
            result[i] = gaps.get(i);
        }
        return result;
    }
}
