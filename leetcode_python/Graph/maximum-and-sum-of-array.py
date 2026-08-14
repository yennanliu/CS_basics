"""

2172. Maximum AND Sum of Array
Hard

You are given an integer array nums of length n and an integer numSlots such that 2 * numSlots >= n. There are numSlots slots numbered from 1 to numSlots.

You have to place all n integers into the slots such that each slot contains at most two numbers. The AND sum of a given placement is the sum of the bitwise AND of every number with its respective slot number.

For example, the AND sum of placing the numbers [1, 3] into slot 1 and [4, 6] into slot 2 is equal to (1 AND 1) + (3 AND 1) + (4 AND 2) + (6 AND 2) = 1 + 1 + 0 + 2 = 4.

Return the maximum possible AND sum of nums given numSlots slots.


Example 1:

Input: nums = [1,2,3,4,5,6], numSlots = 3
Output: 9
Explanation: One possible placement is [1, 4] into slot 1, [2, 6] into slot 2, and [3, 5] into slot 3.
This gives the maximum AND sum of (1 AND 1) + (4 AND 1) + (2 AND 2) + (6 AND 2) + (3 AND 3) + (5 AND 3) = 1 + 0 + 2 + 2 + 3 + 1 = 9.

Example 2:

Input: nums = [1,3,10,4,7,1], numSlots = 9
Output: 24
Explanation: One possible placement is [1, 1] into slot 1, [3] into slot 3, [4] into slot 4, [7] into slot 7, and [10] into slot 9.
This gives the maximum AND sum of (1 AND 1) + (1 AND 1) + (3 AND 3) + (4 AND 4) + (7 AND 7) + (10 AND 9) = 1 + 1 + 3 + 4 + 7 + 8 = 24.
Note that slots 2, 5, 6, and 8 are empty which is permitted.


Constraints:

n == nums.length
1 <= numSlots <= 9
1 <= n <= 2 * numSlots
1 <= nums[i] <= 15

"""

# V0
# IDEA : BITMASK DP OVER *HALF-SLOTS* (each slot split into two seats)
#
#   "at most two numbers per slot" is awkward to encode directly, so expand
#   the numSlots slots into 2 * numSlots SEATS, where seat p belongs to slot
#       p // 2 + 1
#   now every seat holds at most one number and a plain subset DP works.
#
#   dp[mask] = best AND sum after filling the seats in `mask`. because the
#   numbers are assigned in order, popcount(mask) tells us exactly WHICH
#   number comes next — no second dimension needed :
#       i = popcount(mask)
#       dp[mask | 1<<p] = max(..., dp[mask] + (nums[i] & slot_of(p)))
#
#   the answer is the best dp value over all masks with popcount == n.
#
#   NOTE : numSlots <= 9 so the state space is at most 2^18.
#
# time = O(2^(2*numSlots) * numSlots), space = O(2^(2*numSlots))
class Solution(object):
    def maximumANDSum(self, nums, numSlots):
        n = len(nums)
        seats = 2 * numSlots
        size = 1 << seats
        dp = [-1] * size
        dp[0] = 0
        res = 0

        for mask in range(size):
            if dp[mask] < 0:
                continue
            i = bin(mask).count('1')       # how many numbers are already placed
            if i == n:
                res = max(res, dp[mask])
                continue
            for p in range(seats):
                if mask >> p & 1:
                    continue
                nxt = mask | (1 << p)
                val = dp[mask] + (nums[i] & (p // 2 + 1))
                if val > dp[nxt]:
                    dp[nxt] = val
        return res
