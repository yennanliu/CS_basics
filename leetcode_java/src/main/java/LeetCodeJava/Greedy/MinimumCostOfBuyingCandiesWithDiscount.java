package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-cost-of-buying-candies-with-discount/description/

import java.util.Arrays;

/**
 * 2144. Minimum Cost of Buying Candies With Discount
 * Easy
 *
 * A shop is selling candies at a discount. For every two candies sold, the shop gives a third candy for
 * free.
 *
 * The customer can choose any candy to take away for free as long as the cost of the chosen candy is less
 * than or equal to the minimum cost of the two candies bought.
 *
 * For example, if there are 4 candies with costs 1, 2, 3, and 4, and the customer buys candies with costs 2
 * and 3, they can take the candy with cost 1 for free, but not the candy with cost 4.
 *
 * Given a 0-indexed integer array cost, where cost[i] denotes the cost of the ith candy, return the minimum
 * cost of buying all the candies.
 *
 *
 * Example 1:
 *
 * Input: cost = [1,2,3]
 * Output: 5
 *
 * Example 2:
 *
 * Input: cost = [6,5,7,9,2,2]
 * Output: 23
 *
 * Example 3:
 *
 * Input: cost = [5,5]
 * Output: 10
 *
 *
 * Constraints:
 *
 * 1 <= cost.length <= 100
 * 1 <= cost[i] <= 100
 *
 */
public class MinimumCostOfBuyingCandiesWithDiscount {

    // V0
    // IDEA: SORT DESCENDING, EVERY THIRD CANDY IS FREE
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int minimumCost(int[] cost) {
        int[] sorted = cost.clone();
        Arrays.sort(sorted);

        // walk from the most expensive down; in each trio the cheapest one (the third) is free
        int total = 0;
        int position = 0;
        for (int i = sorted.length - 1; i >= 0; i--) {
            if (position % 3 != 2) {
                total += sorted[i];
            }
            position++;
        }
        return total;
    }
}
