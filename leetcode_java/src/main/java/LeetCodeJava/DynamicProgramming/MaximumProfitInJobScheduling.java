package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-profit-in-job-scheduling/description/

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 1235. Maximum Profit in Job Scheduling
 * Hard
 *
 * We have n jobs, where every job is scheduled to be done from startTime[i] to endTime[i],
 * obtaining a profit of profit[i].
 *
 * You're given the startTime, endTime and profit arrays, return the maximum profit you can
 * take such that there are no two jobs in the subset with overlapping time range.
 *
 * If you choose a job that ends at time X you will be able to start another job that
 * starts at time X.
 *
 *
 * Example 1:
 *
 * Input: startTime = [1,2,3,3], endTime = [3,4,5,6], profit = [50,10,40,70]
 * Output: 120
 * Explanation: The subset chosen is the first and fourth job.
 * Time range [1-3]+[3-6] , we get profit of 120 = 50 + 70.
 *
 * Example 2:
 *
 * Input: startTime = [1,2,3,4,6], endTime = [3,5,10,6,9], profit = [20,20,100,70,60]
 * Output: 150
 * Explanation: The subset chosen is the first, fourth and fifth job.
 * Profit obtained 150 = 20 + 70 + 60.
 *
 * Example 3:
 *
 * Input: startTime = [1,1,1], endTime = [2,3,4], profit = [5,6,4]
 * Output: 6
 *
 *
 * Constraints:
 *
 * 1 <= startTime.length == endTime.length == profit.length <= 5 * 10^4
 * 1 <= startTime[i] < endTime[i] <= 10^9
 * 1 <= profit[i] <= 10^4
 *
 */
public class MaximumProfitInJobScheduling {

    // V0
    // IDEA: DP + BINARY SEARCH (WEIGHTED INTERVAL SCHEDULING)
    /**
     * sort jobs by END time
     * dp[i] = max profit using the first i jobs in that order (non-decreasing in i)
     *
     * dp[i + 1] = max( dp[i],                 // skip job i
     *                  dp[k] + profit[i] )    // take job i
     * k = number of jobs among the first i whose end <= start of job i
     *   -> binary search on the sorted end times; "<=" (upper bound) because a job
     *      may start exactly when another ends
     *
     * base: dp[0] = 0
     */
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
        int n = startTime.length;

        // jobs[i] = {start, end, profit}, sorted by end time
        int[][] jobs = new int[n][3];
        for (int i = 0; i < n; i++) {
            jobs[i][0] = startTime[i];
            jobs[i][1] = endTime[i];
            jobs[i][2] = profit[i];
        }
        Arrays.sort(jobs, (a, b) -> Integer.compare(a[1], b[1]));

        int[] dp = new int[n + 1];
        for (int i = 0; i < n; i++) {
            int start = jobs[i][0];
            int jobProfit = jobs[i][2];

            // how many of jobs[0..i-1] end at or before this job starts
            int compatible = countEndsAtOrBefore(jobs, i, start);

            int skip = dp[i];
            int take = dp[compatible] + jobProfit;
            dp[i + 1] = Math.max(skip, take);
        }
        return dp[n];
    }

    /** Number of jobs in jobs[0..limit) (sorted by end) whose end time is <= time. */
    private int countEndsAtOrBefore(int[][] jobs, int limit, int time) {
        int left = 0;
        int right = limit;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (jobs[mid][1] <= time) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    // V1
    // IDEA: SORT BY START + MIN-HEAP OF (END TIME, PROFIT IF THIS JOB IS THE LAST ONE)
    /**
     * Sweep jobs by start time. Every job already finished by this start can be
     * "cashed in": pop it and keep the best profit seen (`bestFinished`). Taking the
     * current job then yields bestFinished + profit, pushed with its end time.
     */
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int jobScheduling_1(int[] startTime, int[] endTime, int[] profit) {
        int n = startTime.length;

        int[][] jobs = new int[n][3];
        for (int i = 0; i < n; i++) {
            jobs[i][0] = startTime[i];
            jobs[i][1] = endTime[i];
            jobs[i][2] = profit[i];
        }
        Arrays.sort(jobs, (a, b) -> Integer.compare(a[0], b[0]));

        // {endTime, total profit of a chain ending with that job}, earliest end on top
        PriorityQueue<int[]> running = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        int bestFinished = 0;

        for (int[] job : jobs) {
            int start = job[0];
            // every chain that has ended by `start` can be followed by this job
            while (!running.isEmpty() && running.peek()[0] <= start) {
                bestFinished = Math.max(bestFinished, running.poll()[1]);
            }
            running.offer(new int[] {job[1], bestFinished + job[2]});
        }

        // the best chain may still be in the heap
        while (!running.isEmpty()) {
            bestFinished = Math.max(bestFinished, running.poll()[1]);
        }
        return bestFinished;
    }
}
