package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-xor-score-subarray-queries/description/

/**
 * 3277. Maximum XOR Score Subarray Queries
 * Hard
 *
 * You are given an array nums of n integers, and a 2D integer array queries of size q, where queries[i] = [l_i, r_i].
 *
 * For each query, you must find the maximum XOR score of any subarray of nums[l_i..r_i].
 *
 * The XOR score of an array a is found by repeatedly applying the following operations on a so that only one element remains, that is the score:
 *
 * Simultaneously replace a[i] with a[i] XOR a[i + 1] for all indices i except the last one.
 * Remove the last element of a.
 *
 * Return an array answer of size q where answer[i] is the answer to query i.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,8,4,32,16,1], queries = [[0,2],[1,4],[0,5]]
 * Output: [12,60,60]
 * Explanation:
 * In the first query, nums[0..2] has 6 subarrays [2], [8], [4], [2, 8], [8, 4], and [2, 8, 4] each with a respective XOR score of 2, 8, 4, 10, 12, and 6. The answer for the query is 12, the largest of all XOR scores.
 *
 * Example 2:
 *
 * Input: nums = [0,7,3,2,8,5,1], queries = [[0,3],[1,5],[2,4],[2,6],[5,6]]
 * Output: [7,14,11,14,5]
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 2000
 * 0 <= nums[i] <= 2^31 - 1
 * 1 <= q == queries.length <= 10^5
 * queries[i].length == 2
 * queries[i] = [l_i, r_i]
 * 0 <= l_i <= r_i <= n - 1
 *
 */
public class MaximumXorScoreSubarrayQueries {

    // V0
    // IDEA: INTERVAL DP (PASCAL-LIKE XOR RECURRENCE) + 2D RUNNING MAXIMUM
    /**
     *  Collapsing a[l..r] one step at a time satisfies a Pascal-like
     *  recurrence, so both tables fill by increasing length.
     *
     *  DP def:
     *     score[l][r] = XOR score of the subarray nums[l..r]
     *     best[l][r]  = MAXIMUM score over all subarrays inside [l, r]
     *
     *  DP eq:
     *     score[l][r] = score[l][r-1] ^ score[l+1][r]
     *     best[l][r]  = max(score[l][r], best[l][r-1], best[l+1][r])
     *
     *  Init: score[i][i] = best[i][i] = nums[i]
     *
     *  Filling both tables is O(N^2) (4 * 10^6 for N = 2000); every query is then
     *  ONE lookup, which is what makes 10^5 queries cheap.
     *
     *  time = O(N^2 + Q)
     *  space = O(N^2)
     */
    public int[] maximumSubarrayXor(int[] nums, int[][] queries) {
        int n = nums.length;
        int[][] score = new int[n][n];
        int[][] best = new int[n][n];

        // length-1 subarrays: the score is the element itself
        for (int i = 0; i < n; i++) {
            score[i][i] = nums[i];
            best[i][i] = nums[i];
        }

        // longer subarrays only read shorter ones, so fill by length
        for (int length = 2; length <= n; length++) {
            for (int left = 0; left + length - 1 < n; left++) {
                int right = left + length - 1;
                score[left][right] = score[left][right - 1] ^ score[left + 1][right];

                int bestInside = Math.max(best[left][right - 1], best[left + 1][right]);
                best[left][right] = Math.max(score[left][right], bestInside);
            }
        }

        int[] answers = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            answers[q] = best[queries[q][0]][queries[q][1]];
        }
        return answers;
    }
}
