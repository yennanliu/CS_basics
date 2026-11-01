package LeetCodeJava.Math;

// https://leetcode.com/problems/maximum-number-of-upgradable-servers/description/

/**
 * 3155. Maximum Number of Upgradable Servers
 * Medium
 *
 * You have n data centers and need to upgrade their servers.
 *
 * You are given four arrays count, upgrade, sell, and money of length n, which show:
 *
 * The number of servers
 * The cost of upgrading a single server
 * The money you get by selling a server
 * The money you initially have
 *
 * for each data center respectively.
 *
 * Return an array answer, where for each data center, the corresponding element in answer represents
 * the maximum number of servers that can be upgraded.
 *
 * Note that the money from one data center cannot be used for another data center.
 *
 *
 * Example 1:
 *
 * Input: count = [4,3], upgrade = [3,5], sell = [4,2], money = [8,9]
 * Output: [3,2]
 * Explanation:
 * For the first data center, if we sell one server, we'll have 8 + 4 = 12 units of money and we can
 * upgrade the remaining 3 servers for 3 * 3 = 9 units of money.
 * For the second data center, if we sell one server, we'll have 9 + 2 = 11 units of money and we can
 * upgrade the remaining 2 servers for 2 * 5 = 10 units of money.
 *
 *
 * Constraints:
 *
 * 1 <= count.length == upgrade.length == sell.length == money.length <= 10^5
 * 1 <= count[i], upgrade[i], sell[i], money[i] <= 10^5
 *
 */
public class MaximumNumberOfUpgradableServers {

    // V0
    // IDEA: SOLVE THE BUDGET INEQUALITY FOR k
    //   upgrade k, sell (count - k):  k * upgrade <= money + (count - k) * sell
    //   =>  k <= (money + count * sell) / (upgrade + sell), capped by count
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[] maxUpgrades(int[] count, int[] upgrade, int[] sell, int[] money) {
        int n = count.length;
        int[] answer = new int[n];
        for (int i = 0; i < n; i++) {
            // long: count * sell can reach 10^10
            long budgetIfAllSold = money[i] + (long) count[i] * sell[i];
            long maxByBudget = budgetIfAllSold / (upgrade[i] + sell[i]);
            answer[i] = (int) Math.min(count[i], maxByBudget);
        }
        return answer;
    }

    // V1
    // IDEA: BINARY SEARCH ON k — "CAN WE UPGRADE k SERVERS?" IS MONOTONE IN k
    /**
     * time = O(N log C)
     * space = O(N)
     */
    public int[] maxUpgrades_1(int[] count, int[] upgrade, int[] sell, int[] money) {
        int n = count.length;
        int[] answer = new int[n];
        for (int i = 0; i < n; i++) {
            // k = 0 is always affordable; search the largest affordable k in [0, count]
            int left = 0;
            int right = count[i];
            while (left < right) {
                // upper mid so the loop always makes progress when left = mid
                int mid = left + (right - left + 1) / 2;
                long cost = (long) mid * upgrade[i];
                long available = money[i] + (long) (count[i] - mid) * sell[i];
                if (cost <= available) {
                    left = mid;
                } else {
                    right = mid - 1;
                }
            }
            answer[i] = left;
        }
        return answer;
    }
}
