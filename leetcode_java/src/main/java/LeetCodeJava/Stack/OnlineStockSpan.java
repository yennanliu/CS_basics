package LeetCodeJava.Stack;

// https://leetcode.com/problems/online-stock-span/description/
// https://leetcode.cn/problems/online-stock-span/

import java.util.Stack;

/**
 * 901. Online Stock Span
 * Medium
 * Topics
 * Companies
 * Design an algorithm that collects daily price quotes for some stock and returns the span of that stock's price for the current day.
 *
 * The span of the stock's price in one day is the maximum number of consecutive days (starting from that day and going backward) for which the stock price was less than or equal to the price of that day.
 *
 * For example, if the prices of the stock in the last four days is [7,2,1,2] and the price of the stock today is 2, then the span of today is 4 because starting from today, the price of the stock was less than or equal 2 for 4 consecutive days.
 * Also, if the prices of the stock in the last four days is [7,34,1,2] and the price of the stock today is 8, then the span of today is 3 because starting from today, the price of the stock was less than or equal 8 for 3 consecutive days.
 * Implement the StockSpanner class:
 *
 * StockSpanner() Initializes the object of the class.
 * int next(int price) Returns the span of the stock's price given that today's price is price.
 *
 *
 * Example 1:
 *
 * Input
 * ["StockSpanner", "next", "next", "next", "next", "next", "next", "next"]
 * [[], [100], [80], [60], [70], [60], [75], [85]]
 * Output
 * [null, 1, 1, 1, 2, 1, 4, 6]
 *
 * Explanation
 * StockSpanner stockSpanner = new StockSpanner();
 * stockSpanner.next(100); // return 1
 * stockSpanner.next(80);  // return 1
 * stockSpanner.next(60);  // return 1
 * stockSpanner.next(70);  // return 2
 * stockSpanner.next(60);  // return 1
 * stockSpanner.next(75);  // return 4, because the last 4 prices (including today's price of 75) were less than or equal to today's price.
 * stockSpanner.next(85);  // return 6
 *
 *
 * Constraints:
 *
 * 1 <= price <= 105
 * At most 104 calls will be made to next.
 *
 */
public class OnlineStockSpan {

    /**
     * Your StockSpanner object will be instantiated and called as such:
     * StockSpanner obj = new StockSpanner();
     * int param_1 = obj.next(price);
     */

    // V0
//    class StockSpanner {
//
//        public StockSpanner() {
//
//        }
//
//        public int next(int price) {
//
//        }
//    }

    // V1
    // https://www.youtube.com/watch?v=slYh0ZNEqSw

    // V2
    // https://leetcode.com/problems/online-stock-span/solutions/6554291/efficient-stock-span-calculation-using-m-3d7d/
    class StockSpanner_2 {
        Stack<int[]> st;

        public StockSpanner_2() {
            st = new Stack<>();
        }

        public int next(int price) {
            int span = 1;
            while (st.size() > 0 && price >= st.peek()[0]) {
                span += st.pop()[1];
            }
            st.push(new int[] { price, span });
            return span;
        }

    }

    // V3
    // https://leetcode.com/problems/online-stock-span/solutions/6390995/beats-100-o1-amortized-optimal-approach-hj1qw/
    class StockSpanner_3 {
        Stack<int[]> st;

        public StockSpanner_3() {
            st = new Stack<>();
        }

        public int next(int price) {
            int days = 1;
            while (!st.isEmpty() && st.peek()[0] <= price) {
                days += st.pop()[1];
            }
            st.push(new int[] { price, days });
            return days;
        }
    }

    // V4
    // https://leetcode.com/problems/online-stock-span/solutions/6487562/easyjava-code-with-detailed-comments-by-5s4mj/
    class StockSpanner_4 {
        Stack<int[]> st; // size of array would be 2 -> 0th index will hold price and 1st index will hold
        // span

        public StockSpanner_4() {
            st = new Stack<>(); // initialize our stack
        }

        public int next(int price) {
            // for every price span would be 1
            int span = 1;
            // we will iterate in our stack and check for past low prices
            // if we got past low price at the top of stack we will add that span in our
            // current span and will pop out that smaller price from stack, and will add our
            // current price with its span in stack
            while (st.isEmpty() == false && st.peek()[0] <= price) {
                span += st.peek()[1];// we will add lower price's span in our current span
                st.pop();// we will pop out lower price from our stack
            }
            // add our current price and span in stack
            st.push(new int[] { price, span });
            return span;
        }
    }

}
