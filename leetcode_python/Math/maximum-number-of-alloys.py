"""

2861. Maximum Number of Alloys
Medium

You are the owner of a company that creates alloys using various types of metals. There are n different types of metals available, and you have access to k machines that can be used to create alloys. Each machine requires a specific amount of each metal type to create an alloy.

For the ith machine to create an alloy, it needs composition[i][j] units of metal of type j. Initially, you have stock[i] units of metal type i, and purchasing one unit of metal type i costs cost[i] coins.

Given integers n, k, budget, a 1-indexed 2D array composition, and 1-indexed arrays stock and cost, your goal is to maximize the number of alloys the company can create while staying within the budget of budget coins.

All alloys must be created with the same machine.

Return the maximum number of alloys that the company can create.


Example 1:

Input: n = 3, k = 2, budget = 15, composition = [[1,1,1],[1,1,10]], stock = [0,0,0], cost = [1,2,3]
Output: 2
Explanation: It is optimal to use the 1st machine to create alloys.
To create 2 alloys we need to buy the:
- 2 units of metal of the 1st type.
- 2 units of metal of the 2nd type.
- 2 units of metal of the 3rd type.
In total, we need 2 * 1 + 2 * 2 + 2 * 3 = 12 coins, which is smaller than or equal to budget = 15.
Notice that we have 0 units of metal of each type and we have to buy all the required units of metal.
It can be proven that we can create at most 2 alloys.

Example 2:

Input: n = 3, k = 2, budget = 15, composition = [[1,1,1],[1,1,10]], stock = [0,0,100], cost = [1,2,3]
Output: 5
Explanation: It is optimal to use the 2nd machine to create alloys.
To create 5 alloys we need to buy:
- 5 units of metal of the 1st type.
- 5 units of metal of the 2nd type.
- 0 units of metal of the 3rd type.
In total, we need 5 * 1 + 5 * 2 + 0 * 3 = 15 coins, which is smaller than or equal to budget = 15.
It can be proven that we can create at most 5 alloys.

Example 3:

Input: n = 2, k = 3, budget = 10, composition = [[2,1],[1,2],[1,1]], stock = [1,1], cost = [5,5]
Output: 2
Explanation: It is optimal to use the 3rd machine to create alloys.
To create 2 alloys we need to buy the:
- 1 unit of metal of the 1st type.
- 1 unit of metal of the 2nd type.
In total, we need 1 * 5 + 1 * 5 = 10 coins, which is smaller than or equal to budget = 10.
It can be proven that we can create at most 2 alloys.


Constraints:

1 <= n, k <= 100
0 <= budget <= 10^8
composition.length == k
composition[i].length == n
1 <= composition[i][j] <= 100
stock.length == cost.length == n
0 <= stock[i] <= 10^8
1 <= cost[i] <= 100

"""

# V0
# IDEA : BINARY SEARCH ON THE ANSWER, PER MACHINE
#
#   Feasibility is MONOTONIC : if a machine can produce x alloys within budget it
#   can also produce anything below x (the required purchase only grows with x).
#   So binary search the largest feasible x for each machine and keep the best.
#
#   Cost of x alloys on machine m:
#       sum over metals j of max(0, composition[m][j] * x - stock[j]) * cost[j]
#
#   Search range : [0, max(stock) + budget].
#   NOTE : that upper bound is safe because composition[m][j] >= 1 and
#          cost[j] >= 1, so producing x alloys costs at least x - stock[j] coins
#          for every j — beyond max(stock) + budget nothing is affordable.
#   NOTE : the cost accumulator is compared against `budget` and can be broken out
#          of early; Python ints are arbitrary precision so there is no overflow
#          to guard against here (a fixed-width port would need 64-bit).
#
# time = O(k * n * log(max_stock + budget)), space = O(1)
class Solution(object):
    def maxNumberOfAlloys(self, n, k, budget, composition, stock, cost):
        def can_make(comp, x):
            spent = 0
            for j in range(n):
                need = comp[j] * x - stock[j]
                if need > 0:
                    spent += need * cost[j]
                    if spent > budget:
                        return False
            return True

        res = 0
        hi_cap = max(stock) + budget
        for comp in composition:
            lo, hi = res, hi_cap
            while lo < hi:
                mid = (lo + hi + 1) // 2
                if can_make(comp, mid):
                    lo = mid
                else:
                    hi = mid - 1
            res = lo
        return res
