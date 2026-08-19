package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/most-profit-assigning-work/

import java.util.Arrays;

/**
 *  826. Most Profit Assigning Work
 *  Medium
 *
 *  You have n jobs and m workers. You are given three arrays: difficulty, profit, and worker where:
 *
 *   - difficulty[i] and profit[i] are the difficulty and the profit of the ith job, and
 *   - worker[j] is the ability of jth worker (i.e., the jth worker can only complete a job
 *     with difficulty at most worker[j]).
 *
 *  Every worker can be assigned at most one job, but one job can be completed multiple times.
 *   - For example, if three workers attempt the same job that pays $1, then the total profit
 *     will be $3. If a worker cannot complete any job, their profit is $0.
 *
 *  Return the maximum profit we can achieve after assigning the workers to the jobs.
 *
 *  Example 1:
 *  Input: difficulty = [2,4,6,8,10], profit = [10,20,30,40,50], worker = [4,5,6,7]
 *  Output: 100
 *  Explanation: Workers are assigned jobs of difficulty [4,4,6,6] and they get
 *               profit of [20,20,30,30] separately.
 *
 *  Example 2:
 *  Input: difficulty = [85,47,57], profit = [24,66,99], worker = [40,25,25]
 *  Output: 0
 *
 *  Constraints:
 *   n == difficulty.length
 *   n == profit.length
 *   m == worker.length
 *   1 <= n, m <= 10^4
 *   1 <= difficulty[i], profit[i], worker[i] <= 10^5
 */
public class MostProfitAssigningWork {

    // V0
    // IDEA: sort jobs by difficulty + sort workers, sweep with a pointer keeping running max profit
    /**
     * time = O(n log n + m log m)
     * space = O(n)
     */
    public int maxProfitAssignment(int[] difficulty, int[] profit, int[] worker) {
        int n = difficulty.length;
        int[][] jobs = new int[n][2];
        for (int i = 0; i < n; i++) {
            jobs[i][0] = difficulty[i];
            jobs[i][1] = profit[i];
        }
        Arrays.sort(jobs, (a, b) -> Integer.compare(a[0], b[0]));
        Arrays.sort(worker);

        int res = 0;
        int i = 0;
        int maxProfit = 0;
        for (int ability : worker) {
            while (i < n && ability >= jobs[i][0]) {
                maxProfit = Math.max(maxProfit, jobs[i][1]);
                i++;
            }
            res += maxProfit;
        }
        return res;
    }

    // V1
    // IDEA: bucket by difficulty (values bounded by 10^5), prefix-max over difficulty axis
    /**
     * time = O(n + m + D), D = 10^5
     * space = O(D)
     */
    public int maxProfitAssignment_1(int[] difficulty, int[] profit, int[] worker) {
        final int MAX = 100001;
        int[] best = new int[MAX];
        for (int i = 0; i < difficulty.length; i++) {
            best[difficulty[i]] = Math.max(best[difficulty[i]], profit[i]);
        }
        for (int d = 1; d < MAX; d++) {
            best[d] = Math.max(best[d], best[d - 1]);
        }
        int res = 0;
        for (int w : worker) {
            res += best[w];
        }
        return res;
    }
}
