package LeetCodeJava.Stack;

// https://leetcode.com/problems/final-prices-with-a-special-discount-in-a-shop/description/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 1475. Final Prices With a Special Discount in a Shop
 * Easy
 *
 * You are given an integer array prices where prices[i] is the price of the ith item in a
 * shop.
 *
 * There is a special discount for items in the shop. If you buy the ith item, then you will
 * receive a discount equivalent to prices[j] where j is the minimum index such that j > i
 * and prices[j] <= prices[i]. Otherwise, you will not receive any discount at all.
 *
 * Return an integer array answer where answer[i] is the final price you will pay for the
 * ith item of the shop, considering the special discount.
 *
 *
 * Example 1:
 *
 * Input: prices = [8,4,6,2,3]
 * Output: [4,2,4,2,3]
 *
 * Example 2:
 *
 * Input: prices = [1,2,3,4,5]
 * Output: [1,2,3,4,5]
 * Explanation: In this case, for all items, you will not receive any discount at all.
 *
 * Example 3:
 *
 * Input: prices = [10,1,1,6]
 * Output: [9,0,1,6]
 *
 *
 * Constraints:
 *
 * 1 <= prices.length <= 500
 * 1 <= prices[i] <= 1000
 *
 */
public class FinalPricesWithASpecialDiscountInAShop {

    // V0
    // IDEA: MONOTONIC STACK ("next smaller OR EQUAL element to the right")
    //       scan right to left keeping an increasing stack; pop everything strictly bigger
    //       than the current price, whatever is left on top is the discount
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[] finalPrices(int[] prices) {
        int n = prices.length;
        int[] result = prices.clone();
        // prices to the right, increasing from bottom to top
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = n - 1; i >= 0; i--) {
            int price = prices[i];
            // pop strictly greater (NOT >=): an equal price is a valid discount
            while (!stack.isEmpty() && stack.peek() > price) {
                stack.pop();
            }
            if (!stack.isEmpty()) {
                result[i] = price - stack.peek();
            }
            stack.push(price);
        }
        return result;
    }

    // V1
    // IDEA: BRUTE FORCE — for each item scan right for the first price <= it.
    //       n <= 500, so O(n^2) passes; V0 is the one to give in an interview.
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int[] finalPrices_1(int[] prices) {
        int n = prices.length;
        int[] result = prices.clone();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (prices[j] <= prices[i]) {
                    result[i] = prices[i] - prices[j];
                    break;
                }
            }
        }
        return result;
    }
}
