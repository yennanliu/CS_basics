package LeetCodeJava.Queue;

// https://leetcode.com/problems/maximum-number-of-robots-within-budget/

/**
 *  2398. Maximum Number of Robots Within Budget
 *  Hard
 *
 *  You have n robots. You are given two 0-indexed integer arrays, chargeTimes and
 *  runningCosts, both of length n. The ith robot costs chargeTimes[i] units to
 *  charge and costs runningCosts[i] units to run. You are also given an integer
 *  budget.
 *
 *  The total cost of running k chosen robots is equal to
 *  max(chargeTimes) + k * sum(runningCosts), where max(chargeTimes) is the largest
 *  charge cost among the k robots and sum(runningCosts) is the sum of running
 *  costs among the k robots.
 *
 *  Return the maximum number of consecutive robots you can run such that the total
 *  cost does not exceed budget.
 *
 *  Example 1:
 *    Input: chargeTimes = [3,6,1,3,4], runningCosts = [2,1,3,4,5], budget = 25
 *    Output: 3
 *    Explanation: For the first 3 robots the total cost is
 *                 max(3,6,1) + 3 * sum(2,1,3) = 6 + 3 * 6 = 24 <= 25.
 *
 *  Example 2:
 *    Input: chargeTimes = [11,12,19], runningCosts = [10,8,7], budget = 19
 *    Output: 0
 *    Explanation: No robot can be run without exceeding the budget.
 *
 *  Constraints:
 *    chargeTimes.length == runningCosts.length == n
 *    1 <= n <= 5 * 10^4
 *    1 <= chargeTimes[i], runningCosts[i] <= 10^5
 *    1 <= budget <= 10^15
 */
public class MaximumNumberOfRobotsWithinBudget {

    // V0
    // IDEA: SLIDING WINDOW + MONOTONIC (DECREASING) DEQUE
    //       cost(l..r) = max(chargeTimes[l..r]) + (r-l+1) * sum(runningCosts[l..r])
    //       both terms only GROW when the window grows, so feasibility is monotonic
    //       in the window -> a two pointer window works.
    //       the only tricky part is max() over a window that shrinks from the left:
    //       keep a deque of indices whose chargeTimes are decreasing, so the front
    //       is always the window maximum.
    //       NOTE: when advancing `l`, pop the deque front FIRST if it equals the
    //             old `l`, otherwise the max keeps pointing at an evicted item.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maximumRobots(int[] chargeTimes, int[] runningCosts, long budget) {
        int n = chargeTimes.length;
        int[] dq = new int[n];
        int head = 0;
        int tail = 0;

        int res = 0;
        long sum = 0;
        int l = 0;

        for (int r = 0; r < n; r++) {
            sum += runningCosts[r];
            while (head < tail && chargeTimes[dq[tail - 1]] <= chargeTimes[r]) {
                tail--;
            }
            dq[tail++] = r;

            while (head < tail
                    && chargeTimes[dq[head]] + (long) (r - l + 1) * sum > budget) {
                if (dq[head] == l) {
                    head++;
                }
                sum -= runningCosts[l];
                l++;
            }
            if (r - l + 1 > res) {
                res = r - l + 1;
            }
        }
        return res;
    }
}
