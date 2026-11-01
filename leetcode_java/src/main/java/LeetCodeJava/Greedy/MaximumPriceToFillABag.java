package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-price-to-fill-a-bag/description/

import java.util.*;

/**
 * 2548. Maximum Price to Fill a Bag
 * Medium
 *
 * You are given a 2D integer array items where items[i] = [price_i, weight_i] denotes the price and weight of the ith item, respectively.
 *
 * You are also given a positive integer capacity.
 *
 * Each item can be divided into two items with ratios part1 and part2, where part1 + part2 == 1.
 *
 * The weight of the first item is weight_i * part1 and the price of the first item is price_i * part1.
 * Similarly, the weight of the second item is weight_i * part2 and the price of the second item is price_i * part2.
 *
 * Return the maximum total price to fill a bag of capacity capacity with given items. If it is impossible to fill a bag return -1. Answers within 10^-5 of the actual answer will be considered accepted.
 *
 *
 * Example 1:
 *
 * Input: items = [[50,1],[10,8]], capacity = 5
 * Output: 55.00000
 * Explanation:
 * We divide the 2nd item into two parts with part1 = 0.5 and part2 = 0.5.
 * The price and weight of the 1st item are 5, 4. And similarly, the price and the weight of the 2nd item are 5, 4.
 * The array items after operation becomes [[50,1],[5,4],[5,4]].
 * To fill a bag with capacity 5 we take the 1st element with a price of 50 and the 2nd element with a price of 5.
 * It can be proved that 55.0 is the maximum total price that we can achieve.
 *
 * Example 2:
 *
 * Input: items = [[100,30]], capacity = 50
 * Output: -1.00000
 * Explanation: It is impossible to fill a bag with the given item.
 *
 *
 * Constraints:
 *
 * 1 <= items.length <= 10^5
 * items[i].length == 2
 * 1 <= price_i, weight_i <= 10^4
 * 1 <= capacity <= 10^9
 *
 */
public class MaximumPriceToFillABag {

    // V0
    // IDEA: GREEDY (fractional knapsack — take the best price-per-weight first)
    //       sort by unit price (price / weight) descending, pour items in, split the last one.
    //       the bag must be filled COMPLETELY: if every item together is too light, return -1.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public double maxPrice(int[][] items, int capacity) {
        // copy so the caller's array keeps its order
        int[][] sorted = new int[items.length][];
        for (int i = 0; i < items.length; i++) {
            sorted[i] = items[i];
        }

        // higher price per unit weight first; compare a/b > c/d as a*d > c*b to stay exact
        Arrays.sort(sorted, (a, b) -> Long.compare((long) b[0] * a[1], (long) a[0] * b[1]));

        double total = 0.0;
        long remaining = capacity;

        for (int[] item : sorted) {
            if (remaining <= 0) {
                break;
            }
            int price = item[0];
            int weight = item[1];
            // take the whole item if it fits, otherwise the fraction that fills the bag
            long taken = Math.min(weight, remaining);
            total += (double) taken / weight * price;
            remaining -= taken;
        }

        // the bag could not be filled with everything we have
        if (remaining > 0) {
            return -1;
        }
        return total;
    }
}
