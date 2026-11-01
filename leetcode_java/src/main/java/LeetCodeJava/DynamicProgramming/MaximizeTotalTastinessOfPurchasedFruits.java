package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximize-total-tastiness-of-purchased-fruits/description/

/**
 * 2431. Maximize Total Tastiness of Purchased Fruits
 * Medium
 *
 * (premium / locked problem)
 *
 * You are given two non-negative integer arrays price and tastiness, both arrays have the same length n. You are also given two non-negative integers maxAmount and maxCoupons.
 *
 * For every integer i in range [0, n - 1]:
 *
 * price[i] describes the price of the ith fruit.
 * tastiness[i] describes the tastiness of the ith fruit.
 *
 * You want to purchase some fruits such that the total tastiness is maximized and the total price does not exceed maxAmount.
 *
 * Additionally, you can use a coupon to purchase a fruit for half of its price (rounded down to the closest integer). You can use at most maxCoupons coupons.
 *
 * Return the maximum total tastiness that can be purchased.
 *
 * Note that:
 *
 * You can purchase each fruit at most once.
 * You can use coupons on some fruit at most once.
 *
 *
 * Example 1:
 *
 * Input: price = [10,20,20], tastiness = [5,8,8], maxAmount = 20, maxCoupons = 1
 * Output: 13
 * Explanation: It is possible to make total tastiness 13 in following way:
 * - Buy first fruit without coupon, so that total price = 0 + 10 and total tastiness = 0 + 5.
 * - Buy second fruit with coupon, so that total price = 10 + 10 and total tastiness = 5 + 8.
 * - Do not buy third fruit, so that total price = 20 and total tastiness = 13.
 * It can be proven that 13 is the maximum total tastiness that can be obtained.
 *
 * Example 2:
 *
 * Input: price = [10,15,7], tastiness = [5,8,20], maxAmount = 10, maxCoupons = 2
 * Output: 28
 * Explanation: It is possible to make total tastiness 28 in following way:
 * - Do not buy first fruit, so that total price = 0 and total tastiness = 0.
 * - Buy second fruit with coupon, so that total price = 0 + 7 and total tastiness = 0 + 8.
 * - Buy third fruit with coupon, so that total price = 7 + 3 and total tastiness = 8 + 20.
 * It can be proven that 28 is the maximum total tastiness that can be obtained.
 *
 *
 * Constraints:
 *
 * n == price.length == tastiness.length
 * 1 <= n <= 100
 * 0 <= price[i], tastiness[i] <= 1000
 * 0 <= maxAmount <= 1000
 * 0 <= maxCoupons <= 5
 *
 */
public class MaximizeTotalTastinessOfPurchasedFruits {

    // V0
    // IDEA: 0/1 KNAPSACK WITH TWO CAPACITIES (MONEY, COUPONS) — EACH FRUIT: SKIP, BUY, OR BUY WITH A COUPON
    /**
     * time = O(N * A * C), A = maxAmount, C = maxCoupons
     * space = O(A * C)
     */
    public int maxTastiness(int[] price, int[] tastiness, int maxAmount, int maxCoupons) {
        // dp[amount][coupons] = max tastiness spending at most amount and at most coupons
        int[][] dp = new int[maxAmount + 1][maxCoupons + 1];

        for (int i = 0; i < price.length; i++) {
            int fullPrice = price[i];
            int halfPrice = price[i] / 2;
            int taste = tastiness[i];

            // both capacities run downward so each fruit is bought at most once
            for (int amount = maxAmount; amount >= 0; amount--) {
                for (int coupons = maxCoupons; coupons >= 0; coupons--) {
                    int best = dp[amount][coupons];
                    // buy at full price
                    if (amount >= fullPrice) {
                        best = Math.max(best, dp[amount - fullPrice][coupons] + taste);
                    }
                    // buy at half price using one coupon
                    if (coupons >= 1 && amount >= halfPrice) {
                        best = Math.max(best, dp[amount - halfPrice][coupons - 1] + taste);
                    }
                    dp[amount][coupons] = best;
                }
            }
        }
        return dp[maxAmount][maxCoupons];
    }
}
