"""

2561. Rearranging Fruits
Hard

You have two fruit baskets containing n fruits each. You are given two 0-indexed integer arrays basket1 and basket2 representing the cost of fruit in each basket. You want to make both baskets equal. To do so, you can use the following operation as many times as you want:

- Choose two indices i and j, and swap the ith fruit of basket1 with the jth fruit of basket2.
- The cost of the swap is min(basket1[i], basket2[j]).

Two baskets are considered equal if sorting them according to the fruit cost makes them exactly the same baskets.

Return the minimum cost to make both the baskets equal or -1 if impossible.


Example 1:

Input: basket1 = [4,2,2,2], basket2 = [1,4,1,2]
Output: 1
Explanation: Swap index 1 of basket1 with index 0 of basket2, which has cost 1. Now basket1 = [4,1,2,2] and basket2 = [2,4,1,2]. Rearranging both the arrays makes them equal.

Example 2:

Input: basket1 = [2,3,4,1], basket2 = [3,2,5,1]
Output: -1
Explanation: It can be shown that it is impossible to make both the baskets equal.


Constraints:

basket1.length == basket2.length
1 <= basket1.length <= 10^5
1 <= basket1[i], basket2[i] <= 10^9

"""

# V0
# IDEA : GREEDY + COUNTING (with a "cheapest fruit as middleman" trick)
#
#   count each value with +1 for basket1 and -1 for basket2. a value whose
#   net count is odd can never be balanced -> return -1.
#
#   for a value x with net count v, |v| / 2 copies of x must LEAVE the basket
#   that has the surplus. collect all those copies into one list `nums`;
#   by construction exactly half of them come from basket1 and half from
#   basket2, and each swap moves one from each side -> we perform
#   len(nums) / 2 swaps, and the elements we should PAY for are the smaller
#   half of `nums` (sort it and take the first half).
#
#   NOTE : the direct swap of a surplus element x costs x, but we can also
#          route it through the globally cheapest fruit mi: swap x with mi,
#          then swap mi with the partner. that costs 2 * mi and is cheaper
#          whenever 2 * mi < x — hence min(x, 2 * mi) per swap.
#   NOTE : mi is the minimum over ALL values present in either basket, not
#          just over the leftover `nums` (any fruit can act as the middleman).
#
# time = O(n log n), space = O(n)
from collections import Counter


class Solution(object):
    def minCost(self, basket1, basket2):
        cnt = Counter()
        for a, b in zip(basket1, basket2):
            cnt[a] += 1
            cnt[b] -= 1
        mi = min(cnt)
        nums = []
        for x, v in cnt.items():
            if v % 2 != 0:
                return -1
            nums.extend([x] * (abs(v) // 2))
        nums.sort()
        return sum(min(x, mi * 2) for x in nums[:len(nums) // 2])
