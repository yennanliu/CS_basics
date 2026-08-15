"""

3139. Minimum Cost to Equalize Array
Hard

You are given an integer array nums and two integers cost1 and cost2. You are allowed to perform either of the following operations any number of times:

Choose an index i from nums and increase nums[i] by 1 for a cost of cost1.
Choose two different indexes i, j, from nums and increase nums[i] and nums[j] by 1 for a cost of cost2.

Return the minimum cost required to make all elements in the array equal.

Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: nums = [4,1], cost1 = 5, cost2 = 2
Output: 15
Explanation:
The following operations can be performed to make the values equal:
Increase nums[1] by 1 for a cost of 5. nums becomes [4,2].
Increase nums[1] by 1 for a cost of 5. nums becomes [4,3].
Increase nums[1] by 1 for a cost of 5. nums becomes [4,4].
The total cost is 15.

Example 2:

Input: nums = [2,3,3,3,5], cost1 = 2, cost2 = 1
Output: 6
Explanation:
The following operations can be performed to make the values equal:
Increase nums[0] and nums[1] by 1 for a cost of 1. nums becomes [3,4,3,3,5].
Increase nums[0] and nums[2] by 1 for a cost of 1. nums becomes [4,4,4,3,5].
Increase nums[0] and nums[3] by 1 for a cost of 1. nums becomes [5,4,4,4,5].
Increase nums[1] and nums[2] by 1 for a cost of 1. nums becomes [5,5,5,4,5].
Increase nums[3] by 1 for a cost of 2. nums becomes [5,5,5,5,5].
The total cost is 6.

Example 3:

Input: nums = [3,5,3], cost1 = 1, cost2 = 3
Output: 4
Explanation:
The following operations can be performed to make the values equal:
Increase nums[0] by 1 for a cost of 1. nums becomes [4,5,3].
Increase nums[0] by 1 for a cost of 1. nums becomes [5,5,3].
Increase nums[2] by 1 for a cost of 1. nums becomes [5,5,4].
Increase nums[2] by 1 for a cost of 1. nums becomes [5,5,5].
The total cost is 4.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^6
1 <= cost1 <= 10^6
1 <= cost2 <= 10^6

"""

# V0
# IDEA : FIX THE TARGET, THEN THE ONLY QUESTION IS HOW MANY INCREMENTS PAIR UP
#
#   for a target T >= max(nums) the work is fixed :
#       S = n*T - sum(nums)        total increments needed
#       d = T - min(nums)          the largest single element's share
#
#   a pair operation consumes two increments on DIFFERENT indices, so the
#   number of pairs is capped twice over :
#       floor(S / 2)   — there are only S increments
#       S - d          — the biggest debtor cannot pair with itself, so every
#                        pair must spend at least one increment elsewhere
#   giving  pairs = min(S // 2, S - d), and the leftovers go single.
#
#   when 2*cost1 <= cost2 pairing never pays, so the answer is S * cost1.
#   otherwise sweep T upward : raising T adds work but also feeds the "other"
#   side so more increments can pair, and the trade-off bottoms out before
#   2*max, which is the scan bound.
#
#   NOTE : the modulo is applied only at the very end — comparing costs
#          modulo 10^9+7 would pick the wrong minimum.
#
# time = O(n + max(nums)), space = O(1)
class Solution(object):
    def minCostToEqualizeArray(self, nums, cost1, cost2):
        MOD = 10 ** 9 + 7
        n = len(nums)
        mx, mn, total = max(nums), min(nums), sum(nums)

        if n == 1:
            return 0
        if 2 * cost1 <= cost2:
            return ((n * mx - total) * cost1) % MOD

        best = float('inf')
        for T in range(mx, 2 * mx + 1):
            S = n * T - total
            d = T - mn
            pairs = min(S // 2, S - d)
            if pairs < 0:
                pairs = 0
            cost = pairs * cost2 + (S - 2 * pairs) * cost1
            if cost < best:
                best = cost
        return best % MOD
