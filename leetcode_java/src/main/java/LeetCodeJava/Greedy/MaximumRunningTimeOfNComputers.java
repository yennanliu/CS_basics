package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-running-time-of-n-computers/description/

/**
 * 2141. Maximum Running Time of N Computers
 * Hard
 *
 * You have n computers. You are given the integer n and a 0-indexed integer array batteries where the ith
 * battery can run a computer for batteries[i] minutes. You are interested in running all n computers
 * simultaneously using the given batteries.
 *
 * Initially, you can insert at most one battery into each computer. After that and at any integer time
 * moment, you can remove a battery from a computer and insert another battery any number of times. The
 * inserted battery can be a totally new battery or a battery from another computer. You may assume that the
 * removing and inserting processes take no time.
 *
 * Note that the batteries cannot be recharged.
 *
 * Return the maximum number of minutes you can run all the n computers simultaneously.
 *
 *
 * Example 1:
 *
 * Input: n = 2, batteries = [3,3,3]
 * Output: 4
 *
 * Example 2:
 *
 * Input: n = 2, batteries = [1,1,1,1]
 * Output: 2
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 * 1 <= batteries.length <= 10^5
 * 1 <= batteries[i] <= 10^9
 *
 */
public class MaximumRunningTimeOfNComputers {

    // V0
    // IDEA: BINARY SEARCH ON THE RUNTIME t + CAPPED-SUM FEASIBILITY (A BATTERY GIVES AT MOST min(b, t))
    /**
     * time = O(M log(total / N)), M = batteries.length
     * space = O(1)
     */
    public long maxRunTime(int n, int[] batteries) {
        long total = 0;
        for (int battery : batteries) {
            total += battery;
        }

        // t can never exceed total / n (every computer needs t minutes)
        long left = 0;
        long right = total / n;
        while (left < right) {
            // upper mid, so `left = mid` always makes progress
            long mid = left + (right - left + 1) / 2;
            if (canRun(n, batteries, mid)) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    /** True if the batteries hold n * minutes battery-minutes once each is capped at `minutes`. */
    private boolean canRun(int n, int[] batteries, long minutes) {
        // a battery powers one computer at a time, so it is useful for at most `minutes` minutes
        long usable = 0;
        for (int battery : batteries) {
            usable += Math.min(battery, minutes);
        }
        return usable >= (long) n * minutes;
    }
}
