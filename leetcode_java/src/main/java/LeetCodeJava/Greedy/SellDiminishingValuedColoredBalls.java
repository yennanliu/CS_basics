package LeetCodeJava.Greedy;

// https://leetcode.com/problems/sell-diminishing-valued-colored-balls/

import java.util.*;

/**
 *  1648. Sell Diminishing-Valued Colored Balls
 *  Medium
 *
 *  You have an inventory of different colored balls, and there is a customer
 *  that wants orders balls of any color.
 *
 *  Each colored ball's value is the number of balls of that color you currently
 *  have in your inventory (so the value decreases as you sell).
 *
 *  You are given an integer array inventory, where inventory[i] is the number of
 *  balls of the ith color, and an integer orders (the total number of balls the
 *  customer wants). Return the maximum total value you can attain, modulo 10^9 + 7.
 *
 *  Example 1:
 *  Input: inventory = [2,5], orders = 4
 *  Output: 14   (2 + 5 + 4 + 3)
 *
 *  Example 2:
 *  Input: inventory = [3,5], orders = 6
 *  Output: 19   (3 + 2 + 5 + 4 + 3 + 2)
 *
 *  Constraints:
 *   - 1 <= inventory.length <= 10^5
 *   - 1 <= inventory[i] <= 10^9
 *   - 1 <= orders <= min(sum(inventory[i]), 10^9)
 */
public class SellDiminishingValuedColoredBalls {

    // V0
    // IDEA: GREEDY + SORT + arithmetic series sum
    //       after selling, the `remaining` balls are spread as evenly as possible,
    //       so for each color we keep `each = remain / (N - i)` balls and sell the rest
    //       (from value d down to each + 1).
    /**
     * time = O(n log n)
     * space = O(1) (in place sort)
     */
    public int maxProfit(int[] inventory, int orders) {

        final long MOD = 1_000_000_007L;

        long total = 0;
        for (int x : inventory) {
            total += x;
        }

        Arrays.sort(inventory);

        long remain = total - orders; // number of balls kept (NOT sold)
        int n = inventory.length;
        long res = 0;

        for (int i = 0; i < n; i++) {
            long d = inventory[i];
            long each = remain / (n - i); // balls kept for this (and each remaining) color

            if (d > each) {
                // sum of (each+1) + (each+2) + ... + d
                long cnt = d - each;
                long sum = (each + 1 + d) * cnt / 2;
                res = (res + sum % MOD) % MOD;
            }

            remain -= Math.min(each, d);
        }

        return (int) (res % MOD);
    }
}
