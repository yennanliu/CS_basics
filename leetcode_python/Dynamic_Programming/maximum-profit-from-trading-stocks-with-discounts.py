"""

3562. Maximum Profit from Trading Stocks with Discounts
Hard

You are given an integer n, representing the number of employees in a company.
Each employee is assigned a unique ID from 1 to n, and employee 1 is the CEO, is
the direct or indirect boss of every employee. You are given two 1-based integer
arrays, present and future, each of length n, where:

present[i] represents the current price at which the i^th employee can buy a
stock today.

future[i] represents the expected price at which the i^th employee can sell the
stock tomorrow.

The company's hierarchy is represented by a 2D integer array hierarchy, where
hierarchy[i] = [u_i, v_i] means that employee u_i is the direct boss of employee
v_i.

Additionally, you have an integer budget representing the total funds available
for investment.

However, the company has a discount policy: if an employee's direct boss
purchases their own stock, then the employee can buy their stock at half the
original price (floor(present[v] / 2)).

Return the maximum profit that can be achieved without exceeding the given
budget.

Note:

You may buy each stock at most once.

You cannot use any profit earned from future stock prices to fund additional
investments and must buy only from budget.

Example 1:

Input: n = 2, present = [1,2], future = [4,3], hierarchy = [[1,2]], budget = 3

Output: 5

Explanation:

Employee 1 buys the stock at price 1 and earns a profit of 4 - 1 = 3.

Since Employee 1 is the direct boss of Employee 2, Employee 2 gets a discounted
price of floor(2 / 2) = 1.

Employee 2 buys the stock at price 1 and earns a profit of 3 - 1 = 2.

The total buying cost is 1 + 1 = 2 <= budget. Thus, the maximum total profit
achieved is 3 + 2 = 5.

Example 2:

Input: n = 2, present = [3,4], future = [5,8], hierarchy = [[1,2]], budget = 4

Output: 4

Explanation:

Employee 2 buys the stock at price 4 and earns a profit of 8 - 4 = 4.

Since both employees cannot buy together, the maximum profit is 4.

Example 3:

Input: n = 3, present = [4,6,8], future = [7,9,11], hierarchy = [[1,2],[1,3]],
budget = 10

Output: 10

Explanation:

Employee 1 buys the stock at price 4 and earns a profit of 7 - 4 = 3.

Employee 3 would get a discounted price of floor(8 / 2) = 4 and earns a profit
of 11 - 4 = 7.

Employee 1 and Employee 3 buy their stocks at a total cost of 4 + 4 = 8 <=
budget. Thus, the maximum total profit achieved is 3 + 7 = 10.

Example 4:

Input: n = 3, present = [5,2,3], future = [8,5,6], hierarchy = [[1,2],[2,3]],
budget = 7

Output: 12

Explanation:

Employee 1 buys the stock at price 5 and earns a profit of 8 - 5 = 3.

Employee 2 would get a discounted price of floor(2 / 2) = 1 and earns a profit
of 5 - 1 = 4.

Employee 3 would get a discounted price of floor(3 / 2) = 1 and earns a profit
of 6 - 1 = 5.

The total cost becomes 5 + 1 + 1 = 7 <= budget. Thus, the maximum total profit
achieved is 3 + 4 + 5 = 12.

Constraints:

1 <= n <= 160

present.length, future.length == n

1 <= present[i], future[i] <= 50

hierarchy.length == n - 1

hierarchy[i] == [u_i, v_i]

1 <= u_i, v_i <= n

u_i != v_i

1 <= budget <= 160

There are no duplicate edges.

Employee 1 is the direct or indirect boss of every employee.

The input graph hierarchy is guaranteed to have no cycles.

"""

# V0
# IDEA : TREE KNAPSACK WITH "DID MY BOSS BUY?" AS A SECOND STATE
#
#   the discount links an employee only to its direct boss, so each employee
#   needs two tables: the best its subtree can achieve when the boss bought
#   (its own price halved) and when the boss did not.  given that single bit
#   the subtrees are independent, which is what makes a tree DP possible at
#   all.
#
#   the budget is still shared across the whole subtree, so the children are
#   folded in one at a time with a knapsack convolution -- for every way of
#   splitting the money between the children handled so far and the new one,
#   keep the better profit.  the two branches at u are then
#     * u abstains: use the children's "boss did not buy" table unchanged;
#     * u buys at price p: use the children's "boss did buy" table, shift it by
#       p and add future[u] - p.
#   the tables mean "with at most b spent", which makes them non-decreasing and
#   removes every unreachable-state special case: doing nothing always scores 0.
#
#   each price is at least 1, so a subtree of s employees can never spend less
#   than s; capping every table at min(budget, total subtree price) is what
#   stops the folding from always paying the full budget^2.
#
"""

DP def
    the discount links an employee only to its DIRECT boss, so one extra bit
    makes the subtrees independent

    dp[u][0][b]: best profit in subtree(u) with at most b spent,
                 given the BOSS did NOT buy    (u pays full price)

    dp[u][1][b]: same, given the BOSS DID buy  (u pays price // 2)

DP eq

     fold the children in one at a time (knapsack CONVOLUTION over the shared
     budget), then at u:

        u ABSTAINS : use the children's "boss did not buy" table unchanged

        u BUYS at price p : use the children's "boss DID buy" table,
                            shift it by p and add future[u] - p


    -> e.g. the tables mean "with AT MOST b spent", so they are
              non-decreasing - which removes every unreachable-state special
              case (doing nothing always scores 0)

     each price is >= 1, so a subtree of s employees can never spend less
     than s; capping every table at min(budget, total subtree price) is what
     stops the folding from always paying the full budget^2

     ans = max over b of dp[root][0][b]

"""
# time = O(n * budget^2) worst case, space = O(n * budget)
class Solution(object):
    def maxProfit(self, n, present, future, hierarchy, budget):
        kids = [[] for _ in range(n + 1)]
        for u, v in hierarchy:
            kids[u].append(v)

        order = []
        stack = [1]
        while stack:
            u = stack.pop()
            order.append(u)
            stack.extend(kids[u])

        tab = [None] * (n + 1)                 # u -> (boss abstained, boss bought)
        cost = [0] * (n + 1)                   # total price inside the subtree

        for u in reversed(order):              # children are finished first
            price = present[u - 1]
            gain = future[u - 1]
            sub = price
            plain = [0]                        # children folded, u abstains
            disc = [0]                         # children folded, u buys
            for v in kids[u]:
                pv, dv = tab[v]
                tab[v] = None
                sub = min(sub + cost[v], budget + 1)
                cap = min(budget, len(plain) - 1 + len(pv) - 1)
                np_ = [0] * (cap + 1)
                nd = [0] * (cap + 1)
                for b in range(cap + 1):
                    bp = bd = 0
                    lo = b - (len(pv) - 1)
                    if lo < 0:
                        lo = 0
                    for i in range(lo, min(b, len(plain) - 1) + 1):
                        x = plain[i] + pv[b - i]
                        if x > bp:
                            bp = x
                        y = disc[i] + dv[b - i]
                        if y > bd:
                            bd = y
                    np_[b] = bp
                    nd[b] = bd
                plain, disc = np_, nd
            cost[u] = sub

            full = min(budget, sub)
            half = price // 2
            res_plain = [0] * (full + 1)
            res_disc = [0] * (full + 1)
            last = len(plain) - 1
            for b in range(full + 1):
                v0 = plain[b if b <= last else last]
                res_plain[b] = v0
                res_disc[b] = v0
            for b in range(price, full + 1):
                v1 = disc[min(b - price, len(disc) - 1)] + gain - price
                if v1 > res_plain[b]:
                    res_plain[b] = v1
            for b in range(half, full + 1):
                v1 = disc[min(b - half, len(disc) - 1)] + gain - half
                if v1 > res_disc[b]:
                    res_disc[b] = v1
            for b in range(1, full + 1):       # keep the tables non-decreasing
                if res_plain[b - 1] > res_plain[b]:
                    res_plain[b] = res_plain[b - 1]
                if res_disc[b - 1] > res_disc[b]:
                    res_disc[b] = res_disc[b - 1]
            tab[u] = (res_plain, res_disc)

        return tab[1][0][-1]
