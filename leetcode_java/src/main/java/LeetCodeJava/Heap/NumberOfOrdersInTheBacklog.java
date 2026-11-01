package LeetCodeJava.Heap;

// https://leetcode.com/problems/number-of-orders-in-the-backlog/description/

import java.util.*;

/**
 * 1801. Number of Orders in the Backlog
 * Medium
 *
 * You are given a 2D integer array orders, where each
 * orders[i] = [price_i, amount_i, orderType_i] denotes that amount_i orders have
 * been placed of type orderType_i at the price price_i. The orderType_i is:
 *
 * - 0 if it is a batch of buy orders, or
 * - 1 if it is a batch of sell orders.
 *
 * Note that orders[i] represents a batch of amount_i independent orders with the
 * same price and order type. All orders represented by orders[i] will be placed
 * before all orders represented by orders[i+1] for all valid i.
 *
 * There is a backlog that consists of orders that have not been executed. The
 * backlog is initially empty. When an order is placed, the following happens:
 *
 * - If the order is a buy order, you look at the sell order with the smallest
 *   price in the backlog. If that sell order's price is smaller than or equal to
 *   the current buy order's price, they will match and be executed, and that sell
 *   order will be removed from the backlog. Else, the buy order is added to the
 *   backlog.
 * - Vice versa, if the order is a sell order, you look at the buy order with the
 *   largest price in the backlog. If that buy order's price is larger than or
 *   equal to the current sell order's price, they will match and be executed, and
 *   that buy order will be removed from the backlog. Else, the sell order is
 *   added to the backlog.
 *
 * Return the total amount of orders in the backlog after placing all the orders
 * from the input. Since this number can be large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: orders = [[10,5,0],[15,2,1],[25,1,1],[30,4,0]]
 * Output: 6
 * Explanation: Finally, the backlog has 5 buy orders with price 10, and 1 buy
 * order with price 30. So the total number of orders in the backlog is 6.
 *
 * Example 2:
 *
 * Input: orders = [[7,1000000000,1],[15,3,0],[5,999999995,0],[5,1,1]]
 * Output: 999999984
 * Explanation: Finally, the backlog has (1000000000-3) sell orders with price 7,
 * and (999999995-1) buy orders with price 5. So the total number of orders =
 * 1999999991, which is equal to 999999984 % (10^9 + 7).
 *
 *
 * Constraints:
 *
 * 1 <= orders.length <= 10^5
 * orders[i].length == 3
 * 1 <= price_i, amount_i <= 10^9
 * orderType_i is either 0 or 1.
 *
 */
public class NumberOfOrdersInTheBacklog {

    private static final int MOD = 1_000_000_007;
    private static final int BUY = 0;

    // V0
    // IDEA: TWO HEAPS SIMULATING AN ORDER BOOK (match whole batches, not single orders)
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int getNumberOfBacklogOrders(int[][] orders) {
        // each heap entry is {price, amount}
        // buy backlog: highest price first
        PriorityQueue<int[]> buyBook = new PriorityQueue<>((a, b) -> Integer.compare(b[0], a[0]));
        // sell backlog: lowest price first
        PriorityQueue<int[]> sellBook = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

        for (int[] order : orders) {
            int price = order[0];
            int amount = order[1];
            int type = order[2];

            if (type == BUY) {
                // eat the cheapest sells while they are priced <= this buy
                while (amount > 0 && !sellBook.isEmpty() && sellBook.peek()[0] <= price) {
                    int[] top = sellBook.poll();
                    int matched = Math.min(amount, top[1]);
                    amount -= matched;
                    // a partially consumed batch goes back with what is left
                    if (top[1] > matched) {
                        sellBook.offer(new int[] {top[0], top[1] - matched});
                    }
                }
                if (amount > 0) {
                    buyBook.offer(new int[] {price, amount});
                }
            } else {
                // eat the dearest buys while they are priced >= this sell
                while (amount > 0 && !buyBook.isEmpty() && buyBook.peek()[0] >= price) {
                    int[] top = buyBook.poll();
                    int matched = Math.min(amount, top[1]);
                    amount -= matched;
                    if (top[1] > matched) {
                        buyBook.offer(new int[] {top[0], top[1] - matched});
                    }
                }
                if (amount > 0) {
                    sellBook.offer(new int[] {price, amount});
                }
            }
        }

        // amounts reach 10^9 each, so sum in a long and reduce at the end
        long total = 0;
        for (int[] entry : buyBook) {
            total += entry[1];
        }
        for (int[] entry : sellBook) {
            total += entry[1];
        }
        return (int) (total % MOD);
    }
}
