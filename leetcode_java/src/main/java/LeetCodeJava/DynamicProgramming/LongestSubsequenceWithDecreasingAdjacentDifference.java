package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/longest-subsequence-with-decreasing-adjacent-difference/description/
/**
 * 3409. Longest Subsequence With Decreasing Adjacent Difference
 * Medium
 *
 * You are given an array of integers nums.
 *
 * Your task is to find the length of the longest subsequence seq of nums, such that the absolute
 * differences between consecutive elements form a non-increasing sequence of integers. In other
 * words, for a subsequence seq_0, seq_1, seq_2, ..., seq_m of nums,
 * |seq_1 - seq_0| >= |seq_2 - seq_1| >= ... >= |seq_m - seq_m - 1|.
 *
 * Return the length of such a subsequence.
 *
 *
 * Example 1:
 *
 * Input: nums = [16,6,3]
 * Output: 3
 * Explanation:
 * The longest subsequence is [16, 6, 3] with the absolute adjacent differences [10, 3].
 *
 * Example 2:
 *
 * Input: nums = [6,5,3,4,2,1]
 * Output: 4
 * Explanation:
 * The longest subsequence is [6, 4, 2, 1] with the absolute adjacent differences [2, 2, 1].
 *
 * Example 3:
 *
 * Input: nums = [10,20,10,19,10,20]
 * Output: 5
 * Explanation:
 * The longest subsequence is [10, 20, 10, 19, 10] with the absolute adjacent differences
 * [10, 10, 9, 9].
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 10^4
 * 1 <= nums[i] <= 300
 *
 */
public class LongestSubsequenceWithDecreasingAdjacentDifference {

    private static final int MAX_VALUE = 300;

    // V0
    // IDEA: DP KEYED ON (LAST VALUE, GAP FLOOR) — "LAST GAP AT LEAST d" MAKES EACH LOOKUP O(1)
    /**
     *  DP def:
     *     - dp[v][d] = longest valid subsequence so far that ends at value v and whose
     *                  last gap is AT LEAST d (a single element counts for every d)
     *  DP eq, for a new element x, sweeping d from MAX_VALUE down to 0:
     *     - best      = max(dp[x - d][d], dp[x + d][d])   // previous value exactly d away
     *     - newRow[d] = max(best + 1, newRow[d + 1])      // fold into every smaller floor
     *     - then dp[x][d] = max(dp[x][d], newRow[d])
     *  Answer: max over dp[x][0]
     *
     *  time = O(N * V), V = 300
     *  space = O(V^2)
     */
    public int longestSubsequence(int[] nums) {
        int[][] dp = new int[MAX_VALUE + 1][MAX_VALUE + 2];
        int answer = 1;

        for (int x : nums) {
            // newRow[d] = best length ending at x with last gap >= d
            int[] newRow = new int[MAX_VALUE + 2];
            for (int gap = MAX_VALUE; gap >= 0; gap--) {
                int best = 0;
                // previous element x - gap, whose own last gap was >= gap
                int lowerValue = x - gap;
                if (lowerValue >= 0) {
                    best = Math.max(best, dp[lowerValue][gap]);
                }
                // previous element x + gap
                int upperValue = x + gap;
                if (upperValue <= MAX_VALUE) {
                    best = Math.max(best, dp[upperValue][gap]);
                }
                // extend by x (or start fresh at length 1), and keep the "at least" invariant
                newRow[gap] = Math.max(best + 1, newRow[gap + 1]);
            }

            // merge only after the sweep, so x never extends its own new entries
            for (int gap = 0; gap <= MAX_VALUE; gap++) {
                dp[x][gap] = Math.max(dp[x][gap], newRow[gap]);
            }
            answer = Math.max(answer, dp[x][0]);
        }
        return answer;
    }
}
