package LeetCodeJava.Array;

// https://leetcode.com/problems/sum-of-special-evenly-spaced-elements-in-array/description/
/**
 * 1714. Sum Of Special Evenly-Spaced Elements In Array
 * Hard
 *
 * You are given a 0-indexed integer array nums consisting of n non-negative integers.
 *
 * You are also given an array queries, where queries[i] = [xi, yi]. The answer to the ith query is
 * the sum of all nums[j] where xi <= j < n and (j - xi) is divisible by yi.
 *
 * Return an array answer where answer.length == queries.length and answer[i] is the answer to the
 * ith query modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: nums = [0,1,2,3,4,5,6,7], queries = [[0,3],[5,1],[4,2]]
 * Output: [9,18,10]
 * Explanation: The answers of the queries are as follows:
 * 1) The j indices that satisfy this query are 0, 3, and 6. nums[0] + nums[3] + nums[6] = 9
 * 2) The j indices that satisfy this query are 5, 6, and 7. nums[5] + nums[6] + nums[7] = 18
 * 3) The j indices that satisfy this query are 4 and 6. nums[4] + nums[6] = 10
 *
 * Example 2:
 *
 * Input: nums = [100,200,101,201,102,202,103,203], queries = [[0,7]]
 * Output: [303]
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 5 * 10^4
 * 0 <= nums[i] <= 10^9
 * 1 <= queries.length <= 1.5 * 10^5
 * 0 <= xi < n
 * 1 <= yi <= 5 * 10^4
 *
 */
public class SumOfSpecialEvenlySpacedElementsInArray {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: SQRT DECOMPOSITION (precompute small steps, walk big steps)
    // split at B = sqrt(n):
    //   step > B  : the walk touches fewer than sqrt(n) cells -> add them directly
    //   step <= B : only B distinct steps exist, so precompute suffix[step][j] = nums[j] + suffix[step][j + step]
    // brute force (walk every query, O(N * Q)) is the stepping stone, and times out at 5e4 * 1.5e5.
    /**
     * time = O((N + Q) * sqrt(N))
     * space = O(N * sqrt(N))
     */
    public int[] solve(int[] nums, int[][] queries) {
        int n = nums.length;
        int blockSize = (int) Math.sqrt(n);

        // suffix[step][j] = (nums[j] + nums[j + step] + ...) mod MOD; column n is the 0 sentinel
        long[][] suffix = new long[blockSize + 1][];
        for (int step = 1; step <= blockSize; step++) {
            long[] row = new long[n + 1];
            for (int j = n - 1; j >= 0; j--) {
                int next = Math.min(n, j + step);
                row[j] = (row[next] + nums[j]) % MOD;
            }
            suffix[step] = row;
        }

        int[] answer = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            int start = queries[q][0];
            int step = queries[q][1];
            if (step <= blockSize) {
                answer[q] = (int) suffix[step][start];
            } else {
                // few cells to visit: sum them directly
                long sum = 0;
                for (int j = start; j < n; j += step) {
                    sum = (sum + nums[j]) % MOD;
                }
                answer[q] = (int) sum;
            }
        }
        return answer;
    }
}
