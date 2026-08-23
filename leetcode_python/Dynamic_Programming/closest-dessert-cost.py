"""

1774. Closest Dessert Cost
Medium

You would like to make dessert and are preparing to buy the ingredients. You have n ice cream base flavors and m types of toppings to choose from. You must follow these rules when making your dessert:

There must be exactly one ice cream base.
You can add one or more types of topping or have no toppings at all.
There are at most two of each type of topping.

You are given three inputs:

baseCosts, an integer array of length n, where each baseCosts[i] represents the price of the i^th ice cream base flavor.
toppingCosts, an integer array of length m, where each toppingCosts[i] is the price of one of the i^th topping.
target, an integer representing your target price for dessert.

You want to make a dessert with a total cost as close to target as possible.

Return the closest possible cost of the dessert to target. If there are multiple, return the lower one.

Example 1:

Input: baseCosts = [1,7], toppingCosts = [3,4], target = 10
Output: 10
Explanation: Consider the following combination (all 0-indexed):
- Choose base 1: cost 7
- Take 1 of topping 0: cost 1 x 3 = 3
- Take 0 of topping 1: cost 0 x 4 = 0
Total: 7 + 3 + 0 = 10.

Example 2:

Input: baseCosts = [2,3], toppingCosts = [4,5,100], target = 18
Output: 17
Explanation: Consider the following combination (all 0-indexed):
- Choose base 1: cost 3
- Take 1 of topping 0: cost 1 x 4 = 4
- Take 2 of topping 1: cost 2 x 5 = 10
- Take 0 of topping 2: cost 0 x 100 = 0
Total: 3 + 4 + 10 + 0 = 17. You cannot make a dessert with a total cost of 18.

Example 3:

Input: baseCosts = [3,10], toppingCosts = [2,5], target = 9
Output: 8
Explanation: It is possible to make desserts with cost 8 and 10. Return 8 as it is the lower cost.

Constraints:

n == baseCosts.length
m == toppingCosts.length
1 <= n, m <= 10
1 <= baseCosts[i], toppingCosts[i] <= 10^4
1 <= target <= 10^4

"""

# V0
# IDEA : ENUMERATE EVERY TOPPING COMBINATION (m <= 10 -> at most 3^10 sums)
#
#   each topping is taken 0, 1 or 2 times, so the set of achievable topping
#   totals has at most 3^10 = 59049 members - build it incrementally and keep
#   it de-duplicated. then try every (base, topping total) pair.
#   NOTE : the tie-break is "return the LOWER cost", so on an equal distance
#          to target we keep the smaller total.
#
"""

DP def
    dp: the SET of achievable topping totals (a subset-sum reachability DP)

        dp_i[s] = True if sum s is reachable using only the first i toppings

        -> each topping may be taken 0, 1 or 2 times
           m <= 10, so at most 3^10 = 59049 distinct totals

DP eq

     dp_i = { s, s + c, s + 2*c  for every s in dp_(i-1) }

            where c = toppingCosts[i]


    -> e.g. then try every (base, topping total) pair:

         ans = the total minimising |base + s - target|,
               breaking ties toward the SMALLER total

     init: dp_0 = {0}

"""
# time = O(3^m + n * 3^m), space = O(3^m)
class Solution(object):
    def closestCost(self, baseCosts, toppingCosts, target):
        sums = set([0])
        for c in toppingCosts:
            nxt = set()
            for s in sums:
                nxt.add(s)
                nxt.add(s + c)
                nxt.add(s + 2 * c)
            sums = nxt

        res = None
        for b in baseCosts:
            for s in sums:
                total = b + s
                if res is None:
                    res = total
                    continue
                d_new, d_old = abs(total - target), abs(res - target)
                if d_new < d_old or (d_new == d_old and total < res):
                    res = total
        return res
