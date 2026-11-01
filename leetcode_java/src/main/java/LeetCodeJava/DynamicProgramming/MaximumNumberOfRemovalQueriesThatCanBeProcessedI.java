package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-number-of-removal-queries-that-can-be-processed-i/description/
// https://leetcode.ca/2024-01-29-3018-Maximum-Number-of-Removal-Queries-That-Can-Be-Processed-I/

/**
 * 3018. Maximum Number of Removal Queries That Can Be Processed I
 * Hard
 *
 * You are given a 0-indexed array nums and a 0-indexed array queries.
 *
 * You can do the following operation at the beginning at most once:
 *
 * Replace nums with a subsequence of nums.
 *
 * We start processing queries in the given order; for each query, we do the following:
 *
 * If the first and the last element of nums is less than queries[i], the processing of queries ends.
 * Otherwise, we choose either the first or the last element of nums if it is greater than or equal to queries[i], and we remove the chosen element from nums.
 *
 * Return the maximum number of queries that can be processed by doing the operation optimally.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4,5], queries = [1,2,3,4,6]
 * Output: 4
 * Explanation: We don't do any operation and process the queries as follows:
 * 1- We choose and remove nums[0] since 1 <= 1, then nums becomes [2,3,4,5].
 * 2- We choose and remove nums[0] since 2 <= 2, then nums becomes [3,4,5].
 * 3- We choose and remove nums[0] since 3 <= 3, then nums becomes [4,5].
 * 4- We choose and remove nums[0] since 4 <= 4, then nums becomes [5].
 * 5- We can not choose any elements from nums since they are not greater than or equal to 5.
 * Hence, the answer is 4.
 * It can be shown that we can't process more than 4 queries.
 *
 * Example 2:
 *
 * Input: nums = [2,3,2], queries = [2,2,3]
 * Output: 3
 * Explanation: We don't do any operation and process the queries as follows:
 * 1- We choose and remove nums[0] since 2 <= 2, then nums becomes [3,2].
 * 2- We choose and remove nums[1] since 2 <= 2, then nums becomes [3].
 * 3- We choose and remove nums[0] since 3 <= 3, then nums becomes [].
 * Hence, the answer is 3.
 * It can be shown that we can't process more than 3 queries.
 *
 * Example 3:
 *
 * Input: nums = [3,4,3], queries = [4,3,2]
 * Output: 2
 * Explanation: First we replace nums with the subsequence of nums [4,3].
 * Then we can process the queries as follows:
 * 1- We choose and remove nums[0] since 4 <= 4, then nums becomes [3].
 * 2- We choose and remove nums[0] since 3 <= 3, then nums becomes [].
 * 3- We can not process any more queries since nums is empty.
 * Hence, the answer is 2.
 * It can be shown that we can't process more than 2 queries.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 1 <= queries.length <= 1000
 * 1 <= nums[i], queries[i] <= 10^9
 *
 */
public class MaximumNumberOfRemovalQueriesThatCanBeProcessedI {

    // V0
    // IDEA: 2D DP OVER (TAKEN FROM LEFT, TAKEN FROM RIGHT)
    //   Replacing nums by a subsequence is the same as being allowed to discard any
    //   element for free when it reaches an end. So two pointers eat the array inwards.
    //   dp[i][j] = max number of queries answered after consuming i elements from the left
    //   and j from the right. Queries are processed in order, so that count is also the
    //   index of the next query. -1 marks an unreachable state.
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int maximumProcessableQueries(int[] nums, int[] queries) {
        int n = nums.length;
        int m = queries.length;
        int[][] dp = new int[n + 1][n + 1];
        for (int[] row : dp) {
            java.util.Arrays.fill(row, -1);
        }
        dp[0][0] = 0;
        int best = 0;

        for (int i = 0; i <= n; i++) {
            for (int j = 0; i + j <= n; j++) {
                int answered = dp[i][j];
                if (answered == -1) {
                    continue;
                }
                best = Math.max(best, answered);
                // all queries done, or nothing left to take
                if (answered == m || i + j == n) {
                    continue;
                }
                int leftValue = nums[i];
                int rightValue = nums[n - 1 - j];

                // take the left end: discard it, or spend it on the next query
                int viaLeft = (leftValue >= queries[answered]) ? answered + 1 : answered;
                dp[i + 1][j] = Math.max(dp[i + 1][j], viaLeft);

                // take the right end: discard it, or spend it on the next query
                int viaRight = (rightValue >= queries[answered]) ? answered + 1 : answered;
                dp[i][j + 1] = Math.max(dp[i][j + 1], viaRight);
            }
        }
        return best;
    }
}
