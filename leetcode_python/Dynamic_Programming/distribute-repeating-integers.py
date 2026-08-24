"""

1655. Distribute Repeating Integers
Hard

You are given an array of n integers, nums, where there are at most 50 unique values in the array.
You are also given an array of m customer order quantities, quantity, where quantity[i] is the amount
of integers the ith customer ordered. Determine if it is possible to distribute nums such that:

- The ith customer gets exactly quantity[i] integers,
- The integers the ith customer gets are all equal, and
- Every customer is satisfied.

Return true if it is possible to distribute nums according to the above conditions.


Example 1:

Input: nums = [1,2,3,4], quantity = [2]
Output: false
Explanation: The 0th customer cannot be given two different integers.

Example 2:

Input: nums = [1,2,3,3], quantity = [2]
Output: true
Explanation: The 0th customer is given [3,3]. The integers [1,2] are not used.

Example 3:

Input: nums = [1,1,2,2], quantity = [2,2]
Output: true
Explanation: The 0th customer is given [1,1], and the 1st customer is given [2,2].


Constraints:

n == nums.length
1 <= n <= 10^5
1 <= nums[i] <= 1000
m == quantity.length
1 <= m <= 10
1 <= quantity[i] <= 10^5
There are at most 50 unique values in nums.

"""

# V0
# IDEA : BITMASK DP OVER SUBSETS (m <= 10 customers, <= 50 distinct values)
#
#   only the COUNT of each distinct value matters -> cnt = [c1, c2, ... ck], k <= 50
#   state : dp[mask] = True if the set of customers in `mask` can be served
#           using the first i distinct values.
#   transition : for value i with count c, enumerate every sub-mask `sub` of
#           `mask` whose total demand need[sub] <= c, and check dp_prev[mask ^ sub].
#
#   NOTE : one distinct value can serve MANY customers at once (as long as the
#          sum of their quantities fits), hence the sub-mask enumeration.
#   NOTE : need[] is precomputed once -> need[sub] = sum of quantity[j] for j in sub.
#
"""

DP def
    only the COUNT of each distinct value matters -> cnt = [c1..ck], k <= 50
    and m = len(quantity) <= 10 customers

    dp[mask]: True if the set of customers in `mask` can ALL be served

              using the distinct values processed so far

    need[mask]: total quantity demanded by the customers in mask

DP eq

     for each value count c:

        dp_new[mask] = OR over sub-masks `sub` of `mask` with need[sub] <= c

                          of dp[mask ^ sub]


    -> e.g. ONE distinct value can serve MANY customers at once (as long as
              their quantities sum to <= c), which is why we enumerate
              SUB-MASKS -> O(k * 3^m)

     init: dp[0] = True
     ans = dp[(1 << m) - 1]

"""
# time = O(k * 3^m), space = O(2^m)
from collections import Counter
class Solution(object):
    def canDistribute(self, nums, quantity):
        m = len(quantity)
        full = 1 << m

        # need[mask] = total quantity demanded by the customers in mask
        need = [0] * full
        for mask in range(1, full):
            low = mask & (-mask)
            j = low.bit_length() - 1
            need[mask] = need[mask ^ low] + quantity[j]

        cnt = sorted(Counter(nums).values(), reverse=True)
        # only the m largest buckets can ever matter
        cnt = cnt[:m]

        dp = [False] * full
        dp[0] = True
        for c in cnt:
            nxt = list(dp)
            for mask in range(1, full):
                if nxt[mask]:
                    continue
                sub = mask
                while sub:
                    if need[sub] <= c and dp[mask ^ sub]:
                        nxt[mask] = True
                        break
                    sub = (sub - 1) & mask
            dp = nxt

        return dp[full - 1]
