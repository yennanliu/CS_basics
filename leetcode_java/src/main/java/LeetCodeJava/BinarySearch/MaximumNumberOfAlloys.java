package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/maximum-number-of-alloys/description/

import java.util.List;

/**
 * 2861. Maximum Number of Alloys
 * Medium
 *
 * You are the owner of a company that creates alloys using various types of metals. There are n different types of metals available, and you have access to k machines that can be used to create alloys. Each machine requires a specific amount of each metal type to create an alloy.
 *
 * For the ith machine to create an alloy, it needs composition[i][j] units of metal of type j. Initially, you have stock[i] units of metal type i, and purchasing one unit of metal type i costs cost[i] coins.
 *
 * Given integers n, k, budget, a 1-indexed 2D array composition, and 1-indexed arrays stock and cost, your goal is to maximize the number of alloys the company can create while staying within the budget of budget coins.
 *
 * All alloys must be created with the same machine.
 *
 * Return the maximum number of alloys that the company can create.
 *
 *
 * Example 1:
 *
 * Input: n = 3, k = 2, budget = 15, composition = [[1,1,1],[1,1,10]], stock = [0,0,0], cost = [1,2,3]
 * Output: 2
 *
 * Example 2:
 *
 * Input: n = 3, k = 2, budget = 15, composition = [[1,1,1],[1,1,10]], stock = [0,0,100], cost = [1,2,3]
 * Output: 5
 *
 * Example 3:
 *
 * Input: n = 2, k = 3, budget = 10, composition = [[2,1],[1,2],[1,1]], stock = [1,1], cost = [5,5]
 * Output: 2
 *
 *
 * Constraints:
 *
 * 1 <= n, k <= 100
 * 0 <= budget <= 10^8
 * composition.length == k
 * composition[i].length == n
 * 1 <= composition[i][j] <= 100
 * stock.length == cost.length == n
 * 0 <= stock[i] <= 10^8
 * 1 <= cost[i] <= 100
 *
 */
public class MaximumNumberOfAlloys {

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER, PER MACHINE -- "can make x alloys" is monotonic in x
    /**
     * time = O(K * N * log(maxStock + budget))
     * space = O(1)
     */
    public int maxNumberOfAlloys(int n, int k, int budget, List<List<Integer>> composition,
                                 List<Integer> stock, List<Integer> cost) {
        int maxStock = 0;
        for (int units : stock) {
            maxStock = Math.max(maxStock, units);
        }
        // each alloy needs >= 1 unit of every metal at >= 1 coin, so nothing beyond this is affordable
        long upperBound = (long) maxStock + budget;

        long best = 0;
        for (List<Integer> machine : composition) {
            // largest x in [best, upperBound] this machine can afford; starting at best skips useless work
            long low = best;
            long high = upperBound;
            while (low < high) {
                // upper mid so that "low = mid" always makes progress
                long mid = low + (high - low + 1) / 2;
                if (canMake(machine, mid, n, budget, stock, cost)) {
                    low = mid;
                } else {
                    high = mid - 1;
                }
            }
            best = low;
        }
        return (int) best;
    }

    /** True if `alloys` alloys on this machine cost at most `budget` coins of purchased metal. */
    private boolean canMake(List<Integer> machine, long alloys, int n, int budget,
                            List<Integer> stock, List<Integer> cost) {
        long spent = 0;
        for (int j = 0; j < n; j++) {
            long needToBuy = machine.get(j) * alloys - stock.get(j);
            if (needToBuy > 0) {
                spent += needToBuy * cost.get(j);
                // stop early: also keeps `spent` far from overflow
                if (spent > budget) {
                    return false;
                }
            }
        }
        return true;
    }
}
