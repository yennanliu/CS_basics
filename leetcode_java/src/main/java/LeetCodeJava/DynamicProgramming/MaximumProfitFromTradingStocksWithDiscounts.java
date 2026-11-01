package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-profit-from-trading-stocks-with-discounts/description/

import java.util.*;

/**
 * 3562. Maximum Profit from Trading Stocks with Discounts
 * Hard
 *
 * You are given an integer n, representing the number of employees in a company.
 * Each employee is assigned a unique ID from 1 to n, and employee 1 is the CEO, is
 * the direct or indirect boss of every employee. You are given two 1-based integer
 * arrays, present and future, each of length n, where:
 *
 * present[i] represents the current price at which the i^th employee can buy a
 * stock today.
 *
 * future[i] represents the expected price at which the i^th employee can sell the
 * stock tomorrow.
 *
 * The company's hierarchy is represented by a 2D integer array hierarchy, where
 * hierarchy[i] = [u_i, v_i] means that employee u_i is the direct boss of employee
 * v_i.
 *
 * Additionally, you have an integer budget representing the total funds available
 * for investment.
 *
 * However, the company has a discount policy: if an employee's direct boss
 * purchases their own stock, then the employee can buy their stock at half the
 * original price (floor(present[v] / 2)).
 *
 * Return the maximum profit that can be achieved without exceeding the given
 * budget.
 *
 * Note:
 *
 * You may buy each stock at most once.
 *
 * You cannot use any profit earned from future stock prices to fund additional
 * investments and must buy only from budget.
 *
 * Example 1:
 *
 * Input: n = 2, present = [1,2], future = [4,3], hierarchy = [[1,2]], budget = 3
 *
 * Output: 5
 *
 * Explanation:
 *
 * Employee 1 buys the stock at price 1 and earns a profit of 4 - 1 = 3.
 *
 * Since Employee 1 is the direct boss of Employee 2, Employee 2 gets a discounted
 * price of floor(2 / 2) = 1.
 *
 * Employee 2 buys the stock at price 1 and earns a profit of 3 - 1 = 2.
 *
 * The total buying cost is 1 + 1 = 2 <= budget. Thus, the maximum total profit
 * achieved is 3 + 2 = 5.
 *
 * Example 2:
 *
 * Input: n = 2, present = [3,4], future = [5,8], hierarchy = [[1,2]], budget = 4
 *
 * Output: 4
 *
 * Explanation:
 *
 * Employee 2 buys the stock at price 4 and earns a profit of 8 - 4 = 4.
 *
 * Since both employees cannot buy together, the maximum profit is 4.
 *
 * Example 3:
 *
 * Input: n = 3, present = [4,6,8], future = [7,9,11], hierarchy = [[1,2],[1,3]],
 * budget = 10
 *
 * Output: 10
 *
 * Explanation:
 *
 * Employee 1 buys the stock at price 4 and earns a profit of 7 - 4 = 3.
 *
 * Employee 3 would get a discounted price of floor(8 / 2) = 4 and earns a profit
 * of 11 - 4 = 7.
 *
 * Employee 1 and Employee 3 buy their stocks at a total cost of 4 + 4 = 8 <=
 * budget. Thus, the maximum total profit achieved is 3 + 7 = 10.
 *
 * Example 4:
 *
 * Input: n = 3, present = [5,2,3], future = [8,5,6], hierarchy = [[1,2],[2,3]],
 * budget = 7
 *
 * Output: 12
 *
 * Explanation:
 *
 * Employee 1 buys the stock at price 5 and earns a profit of 8 - 5 = 3.
 *
 * Employee 2 would get a discounted price of floor(2 / 2) = 1 and earns a profit
 * of 5 - 1 = 4.
 *
 * Employee 3 would get a discounted price of floor(3 / 2) = 1 and earns a profit
 * of 6 - 1 = 5.
 *
 * The total cost becomes 5 + 1 + 1 = 7 <= budget. Thus, the maximum total profit
 * achieved is 3 + 4 + 5 = 12.
 *
 * Constraints:
 *
 * 1 <= n <= 160
 *
 * present.length, future.length == n
 *
 * 1 <= present[i], future[i] <= 50
 *
 * hierarchy.length == n - 1
 *
 * hierarchy[i] == [u_i, v_i]
 *
 * 1 <= u_i, v_i <= n
 *
 * u_i != v_i
 *
 * 1 <= budget <= 160
 *
 * There are no duplicate edges.
 *
 * Employee 1 is the direct or indirect boss of every employee.
 *
 * The input graph hierarchy is guaranteed to have no cycles.
 *
 */
public class MaximumProfitFromTradingStocksWithDiscounts {

    // V0
    // IDEA: TREE KNAPSACK WITH "DID MY BOSS BUY?" AS AN EXTRA STATE
    //   the discount links an employee only to its direct boss, so with that one bit
    //   fixed the subtrees are independent.
    //   dp[u][bossBought][b] = best profit inside subtree(u) spending AT MOST b
    //   Children share the budget, so they are merged one by one with a knapsack
    //   convolution: merged[b] = max over spend s of acc[s] + child[b - s].
    //   At u: abstain -> children see "boss did not buy";
    //         buy at cost c (price, or price / 2 if the boss bought) ->
    //         children see "boss bought", plus future[u] - c, using b - c budget.
    /**
     * time = O(N * B^2)
     * space = O(N * B)
     */
    public int maxProfit(int n, int[] present, int[] future, int[][] hierarchy, int budget) {
        List<List<Integer>> children = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            children.add(new ArrayList<>());
        }
        for (int[] edge : hierarchy) {
            children.get(edge[0]).add(edge[1]);
        }

        // preorder from employee 1; reversed it finishes children before parents
        List<Integer> order = new ArrayList<>();
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(1);
        while (!stack.isEmpty()) {
            int u = stack.pop();
            order.add(u);
            for (int child : children.get(u)) {
                stack.push(child);
            }
        }

        // table[u][0] : boss did not buy, table[u][1] : boss bought
        int[][][] table = new int[n + 1][][];
        for (int idx = order.size() - 1; idx >= 0; idx--) {
            int u = order.get(idx);
            // children merged under "u did not buy" and under "u bought"
            int[] childrenIfSkip = new int[budget + 1];
            int[] childrenIfBuy = new int[budget + 1];
            for (int child : children.get(u)) {
                childrenIfSkip = merge(childrenIfSkip, table[child][0], budget);
                childrenIfBuy = merge(childrenIfBuy, table[child][1], budget);
                table[child] = null;
            }

            int price = present[u - 1];
            int gain = future[u - 1];
            table[u] = new int[2][budget + 1];
            for (int bossBought = 0; bossBought <= 1; bossBought++) {
                int cost = (bossBought == 1) ? price / 2 : price;
                for (int b = 0; b <= budget; b++) {
                    // option 1: u does not buy
                    int best = childrenIfSkip[b];
                    // option 2: u buys, its children get the discount
                    if (b >= cost) {
                        best = Math.max(best, childrenIfBuy[b - cost] + gain - cost);
                    }
                    table[u][bossBought][b] = best;
                }
            }
        }
        // the CEO has no boss
        return table[1][0][budget];
    }

    /** Knapsack convolution: best[b] = max over s of first[s] + second[b - s]. */
    private int[] merge(int[] first, int[] second, int budget) {
        int[] merged = new int[budget + 1];
        for (int b = 0; b <= budget; b++) {
            int best = 0;
            for (int spendFirst = 0; spendFirst <= b; spendFirst++) {
                best = Math.max(best, first[spendFirst] + second[b - spendFirst]);
            }
            merged[b] = best;
        }
        return merged;
    }
}
