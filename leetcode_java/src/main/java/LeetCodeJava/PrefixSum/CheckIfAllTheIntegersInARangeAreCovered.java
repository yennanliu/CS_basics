package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/check-if-all-the-integers-in-a-range-are-covered/description/

/**
 * 1893. Check if All the Integers in a Range Are Covered
 * Easy
 *
 * You are given a 2D integer array ranges and two integers left and right. Each
 * ranges[i] = [start_i, end_i] represents an inclusive interval between start_i
 * and end_i.
 *
 * Return true if each integer in the inclusive range [left, right] is covered by
 * at least one interval in ranges. Return false otherwise.
 *
 * An integer x is covered by an interval ranges[i] = [start_i, end_i] if
 * start_i <= x <= end_i.
 *
 *
 * Example 1:
 *
 * Input: ranges = [[1,2],[3,4],[5,6]], left = 2, right = 5
 * Output: true
 * Explanation: Every integer between 2 and 5 is covered:
 * - 2 is covered by the first range.
 * - 3 and 4 are covered by the second range.
 * - 5 is covered by the third range.
 *
 * Example 2:
 *
 * Input: ranges = [[1,10],[10,20]], left = 21, right = 21
 * Output: false
 * Explanation: 21 is not covered by any range.
 *
 *
 * Constraints:
 *
 * 1 <= ranges.length <= 50
 * 1 <= start_i <= end_i <= 50
 * 1 <= left <= right <= 50
 *
 */
public class CheckIfAllTheIntegersInARangeAreCovered {

    // values are at most 50; one extra slot so diff[end + 1] is always in bounds
    private static final int SIZE = 52;

    // V0
    // IDEA: DIFFERENCE ARRAY (prefix sum of +1 / -1 marks = how many intervals cover x)
    /**
     * time = O(N + M), M = 52
     * space = O(M)
     */
    public boolean isCovered(int[][] ranges, int left, int right) {
        int[] diff = new int[SIZE];
        for (int[] range : ranges) {
            diff[range[0]]++;
            diff[range[1] + 1]--;
        }

        int coverage = 0;
        for (int x = 0; x < SIZE; x++) {
            coverage += diff[x];
            if (left <= x && x <= right && coverage <= 0) {
                return false;
            }
        }
        return true;
    }

    // V1
    // IDEA: BRUTE FORCE (check every x in [left, right] against every interval)
    /**
     * time = O((right - left + 1) * N)
     * space = O(1)
     */
    public boolean isCovered_1(int[][] ranges, int left, int right) {
        for (int x = left; x <= right; x++) {
            boolean covered = false;
            for (int[] range : ranges) {
                if (range[0] <= x && x <= range[1]) {
                    covered = true;
                    break;
                }
            }
            if (!covered) {
                return false;
            }
        }
        return true;
    }
}
