package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-ice-cream-bars/description/

import java.util.*;

/**
 * 1833. Maximum Ice Cream Bars
 * Medium
 *
 * It is a sweltering summer day, and a boy wants to buy some ice cream bars.
 *
 * At the store, there are n ice cream bars. You are given an array costs of
 * length n, where costs[i] is the price of the ith ice cream bar in coins. The
 * boy initially has coins coins to spend, and he wants to buy as many ice cream
 * bars as possible.
 *
 * Note: The boy can buy the ice cream bars in any order.
 *
 * Return the maximum number of ice cream bars the boy can buy with coins coins.
 *
 * You must solve the problem by counting sort.
 *
 *
 * Example 1:
 *
 * Input: costs = [1,3,2,4,1], coins = 7
 * Output: 4
 * Explanation: The boy can buy ice cream bars at indices 0,1,2,4 for a total price of 1 + 3 + 2 + 1 = 7.
 *
 * Example 2:
 *
 * Input: costs = [10,6,8,7,7,8], coins = 5
 * Output: 0
 * Explanation: The boy cannot afford any of the ice cream bars.
 *
 * Example 3:
 *
 * Input: costs = [1,6,3,1,2,5], coins = 20
 * Output: 6
 * Explanation: The boy can buy all the ice cream bars for a total price of 1 + 6 + 3 + 1 + 2 + 5 = 18.
 *
 *
 * Constraints:
 *
 * costs.length == n
 * 1 <= n <= 10^5
 * 1 <= costs[i] <= 10^5
 * 1 <= coins <= 10^8
 *
 */
public class MaximumIceCreamBars {

    private static final int MAX_COST = 100_000;

    // V0
    // IDEA: GREEDY (buy cheapest first) WITH COUNTING SORT, as the statement asks
    /**
     * time = O(N + M), M = 10^5 (max cost)
     * space = O(M)
     */
    public int maxIceCream(int[] costs, int coins) {
        // countAtPrice[p] = number of bars that cost exactly p
        int[] countAtPrice = new int[MAX_COST + 1];
        for (int cost : costs) {
            countAtPrice[cost]++;
        }

        int bought = 0;
        for (int price = 1; price <= MAX_COST; price++) {
            // every remaining bar costs at least `price`, so nothing more is affordable
            if (coins < price) {
                break;
            }
            if (countAtPrice[price] == 0) {
                continue;
            }
            // take as many bars of this price as the coins allow
            int take = Math.min(countAtPrice[price], coins / price);
            bought += take;
            coins -= take * price;
        }
        return bought;
    }

    // V1
    // IDEA: GREEDY WITH A COMPARISON SORT (same exchange argument, simpler to write)
    /**
     * time = O(N log N)
     * space = O(1) (in-place sort)
     */
    public int maxIceCream_1(int[] costs, int coins) {
        Arrays.sort(costs);
        int bought = 0;
        for (int cost : costs) {
            if (cost > coins) {
                break;
            }
            coins -= cost;
            bought++;
        }
        return bought;
    }
}
