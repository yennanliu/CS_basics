package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-difficulty-of-a-job-schedule/

import java.util.Arrays;

/**
 *  1335. Minimum Difficulty of a Job Schedule
 *  Hard
 *
 *  You want to schedule a list of jobs in d days. Jobs are dependent
 *  (i.e to work on the ith job, you have to finish all the jobs j where 0 <= j < i).
 *
 *  You have to finish at least one task every day. The difficulty of a job schedule
 *  is the sum of difficulties of each day of the d days. The difficulty of a day is
 *  the maximum difficulty of a job done on that day.
 *
 *  Given jobDifficulty and d, return the minimum difficulty of a job schedule.
 *  If you cannot find a schedule for the jobs return -1.
 *
 *  Example 1:
 *    Input: jobDifficulty = [6,5,4,3,2,1], d = 2
 *    Output: 7   (first day 6,5,4,3,2 -> 6, second day 1 -> 1)
 *
 *  Example 2:
 *    Input: jobDifficulty = [9,9,9], d = 4
 *    Output: -1
 *
 *  Constraints:
 *    1 <= jobDifficulty.length <= 300
 *    0 <= jobDifficulty[i] <= 1000
 *    1 <= d <= 10
 */
public class MinimumDifficultyOfAJobSchedule {

    // V0
    // IDEA: DP. dp[i][k] = min difficulty of scheduling jobs[i..n-1] within k days.
    /**
     * time = O(n^2 * d)
     * space = O(n * d)
     */
    public int minDifficulty(int[] jobDifficulty, int d) {
        int n = jobDifficulty.length;
        if (n < d) {
            return -1;
        }
        final int INF = Integer.MAX_VALUE / 2;

        int[][] dp = new int[n + 1][d + 1];
        for (int[] row : dp) {
            Arrays.fill(row, INF);
        }
        dp[n][0] = 0;

        for (int i = n - 1; i >= 0; i--) {
            for (int k = 1; k <= d; k++) {
                int mx = 0;
                // j = last job index handled on the current day
                for (int j = i; j <= n - k; j++) {
                    mx = Math.max(mx, jobDifficulty[j]);
                    if (dp[j + 1][k - 1] < INF) {
                        dp[i][k] = Math.min(dp[i][k], mx + dp[j + 1][k - 1]);
                    }
                }
            }
        }
        return dp[0][d] >= INF ? -1 : dp[0][d];
    }

    // V1
    // IDEA: top-down memoization over (start index, remaining days).
    /**
     * time = O(n^2 * d)
     * space = O(n * d)
     */
    public int minDifficulty_1(int[] jobDifficulty, int d) {
        int n = jobDifficulty.length;
        if (n < d) {
            return -1;
        }
        Integer[][] memo = new Integer[n][d + 1];
        return dfs(jobDifficulty, 0, d, memo);
    }

    private int dfs(int[] jobs, int i, int days, Integer[][] memo) {
        int n = jobs.length;
        if (days == 0) {
            return i == n ? 0 : Integer.MAX_VALUE / 2;
        }
        if (n - i < days) {
            return Integer.MAX_VALUE / 2;
        }
        if (memo[i][days] != null) {
            return memo[i][days];
        }

        int res = Integer.MAX_VALUE / 2;
        int mx = 0;
        for (int j = i; j <= n - days; j++) {
            mx = Math.max(mx, jobs[j]);
            res = Math.min(res, mx + dfs(jobs, j + 1, days - 1, memo));
        }
        memo[i][days] = res;
        return res;
    }
}
