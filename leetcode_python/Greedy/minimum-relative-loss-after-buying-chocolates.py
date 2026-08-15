"""

2819. Minimum Relative Loss After Buying Chocolates
Hard

You are given an integer array prices, which shows the chocolate prices and a 2D integer array queries, where queries[i] = [ki, mi].

Alice and Bob went to buy some chocolates, and Alice suggested a way to pay for them, and Bob agreed.

The terms for each query are as follows:

If the price of a chocolate is less than or equal to ki, Bob pays for it.
Otherwise, Bob pays ki of it, and Alice pays the rest.

Bob wants to select exactly mi chocolates such that his relative loss is minimized, more formally, if, in total, Alice has paid ai and Bob has paid bi, Bob wants to minimize bi - ai.

Return an integer array ans where ans[i] is Bob's minimum relative loss possible for queries[i].


Example 1:

Input: prices = [1,9,22,10,19], queries = [[18,4],[5,2]]
Output: [34,-21]
Explanation: For the 1st query Bob selects the chocolates with prices [1,9,10,22]. He pays 1 + 9 + 10 + 18 = 38 and Alice pays 0 + 0 + 0 + 4 = 4. So Bob's relative loss is 38 - 4 = 34.
For the 2nd query Bob selects the chocolates with prices [19,22]. He pays 5 + 5 = 10 and Alice pays 14 + 17 = 31. So Bob's relative loss is 10 - 31 = -21.
It can be shown that these are the minimum possible relative losses.

Example 2:

Input: prices = [1,5,4,3,7,11,9], queries = [[5,4],[5,7],[7,3],[4,5]]
Output: [4,16,7,1]
Explanation: For the 1st query Bob selects the chocolates with prices [1,3,9,11]. He pays 1 + 3 + 5 + 5 = 14 and Alice pays 0 + 0 + 4 + 6 = 10. So Bob's relative loss is 14 - 10 = 4.
For the 2nd query Bob has to select all the chocolates. He pays 1 + 5 + 4 + 3 + 5 + 5 + 5 = 28 and Alice pays 0 + 0 + 0 + 0 + 2 + 6 + 4 = 12. So Bob's relative loss is 28 - 12 = 16.
For the 3rd query Bob selects the chocolates with prices [1,3,11] and he pays 1 + 3 + 7 = 11 and Alice pays 0 + 0 + 4 = 4. So Bob's relative loss is 11 - 4 = 7.
For the 4th query Bob selects the chocolates with prices [1,3,7,9,11] and he pays 1 + 3 + 4 + 4 + 4 = 16 and Alice pays 0 + 0 + 3 + 5 + 7 = 15. So Bob's relative loss is 16 - 15 = 1.
It can be shown that these are the minimum possible relative losses.

Example 3:

Input: prices = [5,6,7], queries = [[10,1],[5,3],[3,3]]
Output: [5,12,0]
Explanation: For the 1st query Bob selects the chocolate with price 5 and he pays 5 and Alice pays 0. So Bob's relative loss is 5 - 0 = 5.
For the 2nd query Bob has to select all the chocolates. He pays 5 + 5 + 5 = 15 and Alice pays 0 + 1 + 2 = 3. So Bob's relative loss is 15 - 3 = 12.
For the 3rd query Bob has to select all the chocolates. He pays 3 + 3 + 3 = 9 and Alice pays 2 + 3 + 4 = 9. So Bob's relative loss is 9 - 9 = 0.
It can be shown that these are the minimum possible relative losses.


Constraints:

1 <= prices.length == n <= 10^5
1 <= prices[i] <= 10^9
1 <= queries.length <= 10^5
queries[i].length == 2
1 <= ki <= 10^9
1 <= mi <= n

"""

# V0
# IDEA : SORT + PREFIX SUMS + GREEDY SHAPE ("CHEAP PREFIX + EXPENSIVE SUFFIX")
#        + BINARY SEARCH ON A CONVEX COST CURVE
#
#   Write down what one chocolate of price p contributes to (bob - alice):
#       p <= k :  bob pays p, alice pays 0        -> contribution = p
#       p >  k :  bob pays k, alice pays p - k    -> contribution = 2*k - p
#
#   So on the sorted price array the contribution is INCREASING in p on the
#   cheap side and DECREASING in p on the expensive side. To pick m items with
#   the smallest total contribution we therefore always take
#       - some PREFIX of the cheap group (the cheapest cheap items), and
#       - some SUFFIX of the expensive group (the priciest expensive items).
#   Nothing in the middle is ever worth taking.
#
#   Let idx = number of prices <= k (binary search on the sorted array). Taking
#   i cheap items and m - i expensive ones costs
#       f(i) = pre[i] + 2*k*(m - i) - (suffix sum of the top (m - i) prices)
#
#   f is CONVEX : stepping i -> i+1 swaps in prices[i] and swaps out the
#   cheapest chosen expensive item prices[n-(m-i)], for a delta of
#       d(i) = prices[i] - (2*k - prices[n - m + i])
#   whose two terms are both non-decreasing in i. So binary search for the
#   first i with d(i) >= 0 - that is the minimiser.
#
#   NOTE : i is constrained to [max(0, m - (n - idx)), min(m, idx)] - we can
#          neither take more cheap items than exist nor more expensive ones.
#   NOTE : Python ints are unbounded, so 2*k*(m-i) up to ~2*10^14 needs no
#          special care here; a fixed-width language would need 64-bit.
#
# time = O((n + q) * log n), space = O(n)
import bisect


class Solution(object):
    def minimumRelativeLosses(self, prices, queries):
        prices = sorted(prices)
        n = len(prices)

        pre = [0] * (n + 1)
        for i, p in enumerate(prices):
            pre[i + 1] = pre[i] + p
        total = pre[n]

        def cost(i, k, m):
            # i cheap items (prices[0..i-1]) + (m - i) priciest items
            j = m - i
            return pre[i] + 2 * k * j - (total - pre[n - j])

        ans = []
        for k, m in queries:
            idx = bisect.bisect_right(prices, k)     # count of prices <= k
            lo = m - (n - idx)
            if lo < 0:
                lo = 0
            hi = idx if idx < m else m

            # first i in [lo, hi) with cost(i + 1) - cost(i) >= 0
            a, b = lo, hi
            while a < b:
                mid = (a + b) // 2
                if prices[mid] - (2 * k - prices[n - m + mid]) >= 0:
                    b = mid
                else:
                    a = mid + 1
            ans.append(cost(a, k, m))
        return ans
