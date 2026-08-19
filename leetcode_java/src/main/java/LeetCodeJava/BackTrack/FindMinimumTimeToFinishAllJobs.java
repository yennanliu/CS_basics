package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/find-minimum-time-to-finish-all-jobs/

import java.util.Arrays;

/**
 *  1723. Find Minimum Time to Finish All Jobs
 *  Hard
 *
 *  You are given an integer array jobs, where jobs[i] is the amount of time it
 *  takes to complete the ith job.
 *
 *  There are k workers that you can assign jobs to. Each job should be assigned
 *  to exactly one worker. The working time of a worker is the sum of the time it
 *  takes to complete all jobs assigned to them. Your goal is to devise an
 *  optimal assignment such that the maximum working time of any worker is
 *  minimized.
 *
 *  Return the minimum possible maximum working time of any assignment.
 *
 *  Example 1:
 *    Input: jobs = [3,2,3], k = 3
 *    Output: 3
 *    Explanation: By assigning each person one job, the maximum time is 3.
 *
 *  Example 2:
 *    Input: jobs = [1,2,4,7,8], k = 2
 *    Output: 11
 *    Explanation: Worker 1: 1, 2, 8 (= 11), Worker 2: 4, 7 (= 11).
 *
 *  Constraints:
 *    1 <= k <= jobs.length <= 12
 *    1 <= jobs[i] <= 10^7
 */
public class FindMinimumTimeToFinishAllJobs {

    // V0
    // IDEA: BACKTRACKING WITH BRANCH-AND-BOUND PRUNING
    //       assign jobs one by one to one of the k workers, tracking each
    //       worker's load. three prunings turn the naive k^n search into
    //       something that finishes instantly for n <= 12:
    //       1) SORT jobs DESCENDING -> big jobs first, so bad branches blow
    //          past the incumbent best near the root.
    //       2) SYMMETRY BREAK -> after trying job i on an EMPTY worker, stop;
    //          all remaining empty workers are interchangeable.
    //       3) BOUND -> skip a worker whose load would reach the current best.
    /**
     * time = O(k^n) worst case, heavily pruned (n <= 12)
     * space = O(n + k)
     */
    private int res;

    public int minimumTimeRequired(int[] jobs, int k) {
        int n = jobs.length;
        Integer[] boxed = new Integer[n];
        for (int i = 0; i < n; i++) {
            boxed[i] = jobs[i];
        }
        // descending
        Arrays.sort(boxed, java.util.Collections.reverseOrder());
        int[] sorted = new int[n];
        int total = 0;
        for (int i = 0; i < n; i++) {
            sorted[i] = boxed[i];
            total += sorted[i];
        }

        this.res = total;
        dfs(0, sorted, new int[k]);
        return this.res;
    }

    private void dfs(int i, int[] jobs, int[] load) {
        if (i == jobs.length) {
            int mx = 0;
            for (int x : load) {
                mx = Math.max(mx, x);
            }
            this.res = Math.min(this.res, mx);
            return;
        }
        for (int j = 0; j < load.length; j++) {
            if (load[j] + jobs[i] >= this.res) {
                continue;
            }
            load[j] += jobs[i];
            dfs(i + 1, jobs, load);
            load[j] -= jobs[i];
            // this worker was empty -> every later empty worker is a twin
            if (load[j] == 0) {
                break;
            }
        }
    }
}
