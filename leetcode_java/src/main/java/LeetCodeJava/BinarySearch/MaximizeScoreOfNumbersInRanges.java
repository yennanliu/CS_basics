package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/maximize-score-of-numbers-in-ranges/description/

import java.util.Arrays;

/**
 * 3281. Maximize Score of Numbers in Ranges
 * Medium
 *
 * You are given an array of integers start and an integer d, representing n intervals [start[i], start[i] + d].
 *
 * You are asked to choose n integers where the ith integer must belong to the ith interval. The score of the chosen integers is defined as the minimum absolute difference between any two integers that have been chosen.
 *
 * Return the maximum possible score of the chosen integers.
 *
 *
 * Example 1:
 *
 * Input: start = [6,0,3], d = 2
 * Output: 4
 * Explanation:
 * The maximum possible score can be obtained by choosing integers: 8, 0, and 4. The score of these chosen integers is min(|8 - 0|, |8 - 4|, |0 - 4|) which equals 4.
 *
 * Example 2:
 *
 * Input: start = [2,6,13,13], d = 5
 * Output: 5
 * Explanation:
 * The maximum possible score can be obtained by choosing integers: 2, 7, 13, and 18. The score of these chosen integers is min(|2 - 7|, |2 - 13|, |2 - 18|, |7 - 13|, |7 - 18|, |13 - 18|) which equals 5.
 *
 *
 * Constraints:
 *
 * 2 <= start.length <= 10^5
 * 0 <= start[i] <= 10^9
 * 0 <= d <= 10^9
 *
 */
public class MaximizeScoreOfNumbersInRanges {

    // V0
    // IDEA: BINARY SEARCH THE MINIMUM GAP + GREEDY LEFT-TO-RIGHT PLACEMENT
    // "every pair at least gap apart" only gets harder as gap grows -> binary search the
    // largest feasible gap. Check: sort the starts and pick, in each interval, the SMALLEST
    // legal value max(start, previous + gap); fail if it passes start + d. Picking the smallest
    // value leaves the most room for the intervals after it.
    /**
     * time = O(N log N + N log(range))
     * space = O(N)
     */
    public int maxPossibleScore(int[] start, int d) {
        int[] sorted = start.clone();
        Arrays.sort(sorted);
        int n = sorted.length;

        long left = 0;
        long right = ((long) sorted[n - 1] + d) - sorted[0];
        while (left < right) {
            long mid = left + (right - left + 1) / 2;
            if (canSpread(sorted, d, mid)) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        return (int) left;
    }

    /** Whether one value per interval can be chosen with all adjacent picks >= gap apart. */
    private boolean canSpread(int[] sorted, long d, long gap) {
        long previous = sorted[0];
        for (int i = 1; i < sorted.length; i++) {
            long pick = Math.max(sorted[i], previous + gap);
            if (pick > sorted[i] + d) {
                return false;
            }
            previous = pick;
        }
        return true;
    }
}
