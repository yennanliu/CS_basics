"""

3444. Minimum Increments for Target Multiples in an Array
Hard

You are given two arrays, nums and target.

In a single operation, you may increment any element of nums by 1.

Return the minimum number of operations required so that each element in target
has at least one multiple in nums.

Example 1:

Input: nums = [1,2,3], target = [4]

Output: 1

Explanation:

The minimum number of operations required to satisfy the condition is 1.

Increment 3 to 4 with just one operation, making 4 a multiple of itself.

Example 2:

Input: nums = [8,4], target = [10,5]

Output: 2

Explanation:

The minimum number of operations required to satisfy the condition is 2.

Increment 8 to 10 with 2 operations, making 10 a multiple of both 5 and 10.

Example 3:

Input: nums = [7,9,10], target = [7]

Output: 0

Explanation:

Target 7 already has a multiple in nums, so no additional operations are needed.

Constraints:

1 <= nums.length <= 5 * 10^4
1 <= target.length <= 4
target.length <= nums.length
1 <= nums[i], target[i] <= 10^4

"""

# V0
# IDEA : BITMASK DP OVER THE (AT MOST 4) TARGETS
#
#   target has at most 4 entries, so the state "which targets are already
#   satisfied" is one of 16 bitmasks.  one element of nums can satisfy several
#   targets at once, and it does so exactly when it becomes a multiple of their
#   lcm — so precompute lcm[S] for each of the 16 subsets, and the price of
#   assigning element v to subset S is the distance up to the next multiple,
#   (-v) % lcm[S].
#
#   then sweep the elements once, each element contributing at most one subset:
#     new[m | S] = min(new[m | S], dp[m] + price(v, S))
#   with S restricted to subsets disjoint from m (overlapping S would only pay
#   for targets already covered).  building the (m, S) pair list once collapses
#   the inner work to 3^4 = 81 combinations per element.
#
#   using a separate `new` array is what stops one element from being spent on
#   two different subsets in the same step.
#
"""

DP def
    target has at most 4 entries, so "which targets are already satisfied" is
    one of 16 bitmasks. one element can satisfy several targets at once -
    exactly when it becomes a multiple of their LCM.

    lcm[S]: the lcm of the targets in subset S  (precomputed for all 16)

    dp[m] : MIN total increments to have satisfied exactly the targets in m

DP eq

     price(v, S) = (-v) % lcm[S]        # distance up to the next multiple

     for each element v:

        dp_new[m | S] = min( dp_new[m | S], dp[m] + price(v, S) )

        for every S DISJOINT from m


    -> e.g. an overlapping S would only pay for targets already covered, so
              restricting to disjoint S loses nothing and collapses the work
              to 3^4 = 81 (m, S) pairs per element

     NOTE !!! writing into a SEPARATE `new` array is what stops one element
              from being spent on two different subsets in the same step

     init: dp[0] = 0
     ans = dp[(1 << t) - 1]

"""
# time = O(n * 3^t) with t <= 4, space = O(2^t)
from math import gcd


class Solution(object):
    def minimumIncrements(self, nums, target):
        t = len(target)
        full = 1 << t
        lcm = [1] * full
        for m in range(1, full):
            low = m & (-m)
            i = low.bit_length() - 1
            rest = lcm[m ^ low]
            v = target[i]
            lcm[m] = rest * v // gcd(rest, v)

        pairs = []
        for m in range(full):
            comp = (full - 1) ^ m
            s = comp
            while s:
                pairs.append((m, s, m | s))
                s = (s - 1) & comp

        INF = float('inf')
        dp = [INF] * full
        dp[0] = 0
        price = [0] * full
        for v in nums:
            for s in range(1, full):
                L = lcm[s]
                price[s] = (-v) % L
            new = dp[:]
            for m, s, ms in pairs:
                base = dp[m]
                if base == INF:
                    continue
                cand = base + price[s]
                if cand < new[ms]:
                    new[ms] = cand
            dp = new
        return dp[full - 1]
