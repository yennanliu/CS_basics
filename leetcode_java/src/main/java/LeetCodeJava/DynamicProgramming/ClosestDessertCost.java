package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/closest-dessert-cost/description/

import java.util.*;

/**
 * 1774. Closest Dessert Cost
 * Medium
 *
 * You would like to make dessert and are preparing to buy the ingredients. You have n ice
 * cream base flavors and m types of toppings to choose from. You must follow these rules
 * when making your dessert:
 *
 * There must be exactly one ice cream base.
 * You can add one or more types of topping or have no toppings at all.
 * There are at most two of each type of topping.
 *
 * You are given three inputs:
 *
 * baseCosts, an integer array of length n, where each baseCosts[i] represents the price
 * of the i^th ice cream base flavor.
 * toppingCosts, an integer array of length m, where each toppingCosts[i] is the price of
 * one of the i^th topping.
 * target, an integer representing your target price for dessert.
 *
 * You want to make a dessert with a total cost as close to target as possible.
 *
 * Return the closest possible cost of the dessert to target. If there are multiple,
 * return the lower one.
 *
 * Example 1:
 *
 * Input: baseCosts = [1,7], toppingCosts = [3,4], target = 10
 * Output: 10
 *
 * Example 2:
 *
 * Input: baseCosts = [2,3], toppingCosts = [4,5,100], target = 18
 * Output: 17
 *
 * Example 3:
 *
 * Input: baseCosts = [3,10], toppingCosts = [2,5], target = 9
 * Output: 8
 * Explanation: It is possible to make desserts with cost 8 and 10. Return 8 as it is the
 * lower cost.
 *
 * Constraints:
 *
 * n == baseCosts.length
 * m == toppingCosts.length
 * 1 <= n, m <= 10
 * 1 <= baseCosts[i], toppingCosts[i] <= 10^4
 * 1 <= target <= 10^4
 *
 */
public class ClosestDessertCost {

    // V0
    // IDEA: SET DP OVER TOPPINGS (each topping 0, 1 or 2 times), THEN TRY EVERY BASE
    /**
     *  reachable = every topping total that can be formed; for each topping, every
     *  existing total s branches into s, s + c, s + 2c (at most 3^m totals).
     *
     *  Then every base + topping total is a candidate; keep the one closest to
     *  target, breaking ties toward the cheaper one.
     *
     *  time = O(N * 3^M)
     *  space = O(3^M)
     */
    public int closestCost(int[] baseCosts, int[] toppingCosts, int target) {
        // all achievable topping sums
        Set<Integer> toppingSums = new HashSet<>();
        toppingSums.add(0);
        for (int cost : toppingCosts) {
            Set<Integer> next = new HashSet<>();
            for (int sum : toppingSums) {
                next.add(sum);            // skip this topping
                next.add(sum + cost);     // take one
                next.add(sum + 2 * cost); // take two
            }
            toppingSums = next;
        }

        int best = -1;
        for (int base : baseCosts) {
            for (int sum : toppingSums) {
                int total = base + sum;
                if (best == -1) {
                    best = total;
                    continue;
                }
                int newDiff = Math.abs(total - target);
                int bestDiff = Math.abs(best - target);
                // closer wins; on a tie, the lower cost wins
                if (newDiff < bestDiff || (newDiff == bestDiff && total < best)) {
                    best = total;
                }
            }
        }
        return best;
    }

    /** Best total found so far by the V1 search. */
    private int bestCost;

    // V1
    // IDEA: DFS / BACKTRACKING OVER TOPPINGS (0, 1, 2 of each), PRUNE ONCE PAST TARGET
    /**
     *  Same search space as V0, explored depth-first with no set. Once the running
     *  cost exceeds target, adding more toppings only moves further away, so stop.
     *
     *  time = O(N * 3^M)
     *  space = O(M)
     */
    public int closestCost_1(int[] baseCosts, int[] toppingCosts, int target) {
        bestCost = baseCosts[0];
        for (int base : baseCosts) {
            dfs(toppingCosts, 0, base, target);
        }
        return bestCost;
    }

    /** Tries 0, 1 or 2 of topping index and recurses, updating bestCost. */
    private void dfs(int[] toppingCosts, int index, int current, int target) {
        int curDiff = Math.abs(current - target);
        int bestDiff = Math.abs(bestCost - target);
        if (curDiff < bestDiff || (curDiff == bestDiff && current < bestCost)) {
            bestCost = current;
        }
        // all toppings decided, or already above target (more only gets worse)
        if (index == toppingCosts.length || current >= target) {
            return;
        }
        for (int count = 0; count <= 2; count++) {
            dfs(toppingCosts, index + 1, current + count * toppingCosts[index], target);
        }
    }
}
