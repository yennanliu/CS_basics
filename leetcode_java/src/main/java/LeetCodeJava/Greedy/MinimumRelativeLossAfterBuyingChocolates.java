package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-relative-loss-after-buying-chocolates/description/

import java.util.*;

/**
 * 2819. Minimum Relative Loss After Buying Chocolates
 * Hard
 *
 * You are given an integer array prices, which shows the chocolate prices and a 2D integer array queries, where queries[i] = [ki, mi].
 *
 * Alice and Bob went to buy some chocolates, and Alice suggested a way to pay for them, and Bob agreed.
 *
 * The terms for each query are as follows:
 *
 * If the price of a chocolate is less than or equal to ki, Bob pays for it.
 * Otherwise, Bob pays ki of it, and Alice pays the rest.
 *
 * Bob wants to select exactly mi chocolates such that his relative loss is minimized, more formally, if, in total, Alice has paid ai and Bob has paid bi, Bob wants to minimize bi - ai.
 *
 * Return an integer array ans where ans[i] is Bob's minimum relative loss possible for queries[i].
 *
 *
 * Example 1:
 *
 * Input: prices = [1,9,22,10,19], queries = [[18,4],[5,2]]
 * Output: [34,-21]
 * Explanation: For the 1st query Bob selects the chocolates with prices [1,9,10,22]. He pays 1 + 9 + 10 + 18 = 38 and Alice pays 0 + 0 + 0 + 4 = 4. So Bob's relative loss is 38 - 4 = 34.
 * For the 2nd query Bob selects the chocolates with prices [19,22]. He pays 5 + 5 = 10 and Alice pays 14 + 17 = 31. So Bob's relative loss is 10 - 31 = -21.
 * It can be shown that these are the minimum possible relative losses.
 *
 * Example 2:
 *
 * Input: prices = [1,5,4,3,7,11,9], queries = [[5,4],[5,7],[7,3],[4,5]]
 * Output: [4,16,7,1]
 * Explanation: For the 1st query Bob selects the chocolates with prices [1,3,9,11]. He pays 1 + 3 + 5 + 5 = 14 and Alice pays 0 + 0 + 4 + 6 = 10. So Bob's relative loss is 14 - 10 = 4.
 * For the 2nd query Bob has to select all the chocolates. He pays 1 + 5 + 4 + 3 + 5 + 5 + 5 = 28 and Alice pays 0 + 0 + 0 + 0 + 2 + 6 + 4 = 12. So Bob's relative loss is 28 - 12 = 16.
 * For the 3rd query Bob selects the chocolates with prices [1,3,11] and he pays 1 + 3 + 7 = 11 and Alice pays 0 + 0 + 4 = 4. So Bob's relative loss is 11 - 4 = 7.
 * For the 4th query Bob selects the chocolates with prices [1,3,7,9,11] and he pays 1 + 3 + 4 + 4 + 4 = 16 and Alice pays 0 + 0 + 3 + 5 + 7 = 15. So Bob's relative loss is 16 - 15 = 1.
 * It can be shown that these are the minimum possible relative losses.
 *
 * Example 3:
 *
 * Input: prices = [5,6,7], queries = [[10,1],[5,3],[3,3]]
 * Output: [5,12,0]
 * Explanation: For the 1st query Bob selects the chocolate with price 5 and he pays 5 and Alice pays 0. So Bob's relative loss is 5 - 0 = 5.
 * For the 2nd query Bob has to select all the chocolates. He pays 5 + 5 + 5 = 15 and Alice pays 0 + 1 + 2 = 3. So Bob's relative loss is 15 - 3 = 12.
 * For the 3rd query Bob has to select all the chocolates. He pays 3 + 3 + 3 = 9 and Alice pays 2 + 3 + 4 = 9. So Bob's relative loss is 9 - 9 = 0.
 * It can be shown that these are the minimum possible relative losses.
 *
 *
 * Constraints:
 *
 * 1 <= prices.length == n <= 10^5
 * 1 <= prices[i] <= 10^9
 * 1 <= queries.length <= 10^5
 * queries[i].length == 2
 * 1 <= ki <= 10^9
 * 1 <= mi <= n
 *
 */
public class MinimumRelativeLossAfterBuyingChocolates {

    // V0
    // IDEA: SORT + PREFIX SUM + BINARY SEARCH ON A CONVEX COST CURVE
    /**
     *  One chocolate of price p adds to (bob - alice):
     *      p <= k : p            (increasing in p)
     *      p >  k : 2 * k - p    (decreasing in p)
     *  So the best m items are always a PREFIX of the sorted cheap prices plus
     *  a SUFFIX of the sorted expensive ones. With i cheap items:
     *      f(i) = prefix[i] + 2 * k * (m - i) - (sum of the top (m - i) prices)
     *  f is convex: the step delta
     *      d(i) = prices[i] - (2 * k - prices[n - m + i])
     *  is non-decreasing, so binary search for the first i with d(i) >= 0.
     *
     *  time = O((N + Q) log N)
     *  space = O(N)
     */
    public long[] minimumRelativeLosses(int[] prices, int[][] queries) {
        int n = prices.length;
        int[] sortedPrices = prices.clone();
        Arrays.sort(sortedPrices);

        // prefix[i] = sum of the i cheapest prices
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + sortedPrices[i];
        }

        long[] answer = new long[queries.length];
        for (int q = 0; q < queries.length; q++) {
            long k = queries[q][0];
            int m = queries[q][1];

            // cheapCount = number of prices <= k
            int cheapCount = upperBound(sortedPrices, k);

            // i must leave room: at most cheapCount cheap, at most n - cheapCount expensive
            int low = Math.max(0, m - (n - cheapCount));
            int high = Math.min(m, cheapCount);

            // first i in [low, high) where taking one more cheap item stops helping
            while (low < high) {
                int mid = low + (high - low) / 2;
                long delta = sortedPrices[mid] - (2 * k - sortedPrices[n - m + mid]);
                if (delta >= 0) {
                    high = mid;
                } else {
                    low = mid + 1;
                }
            }
            answer[q] = cost(prefix, n, low, k, m);
        }
        return answer;
    }

    /** Loss of taking the cheapCount cheapest items plus the (m - cheapCount) priciest. */
    private long cost(long[] prefix, int n, int cheapCount, long k, int m) {
        int expensiveCount = m - cheapCount;
        long expensiveSum = prefix[n] - prefix[n - expensiveCount];
        return prefix[cheapCount] + 2 * k * expensiveCount - expensiveSum;
    }

    /** Number of elements <= target in a sorted array. */
    private int upperBound(int[] sorted, long target) {
        int left = 0;
        int right = sorted.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sorted[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
}
