"""

1674. Minimum Moves to Make Array Complementary
Medium

You are given an integer array nums of even length n and an integer limit. In one move, you can
replace any integer from nums with another integer between 1 and limit, inclusive.

The array nums is complementary if for all indices i (0-indexed), nums[i] + nums[n - 1 - i] equals
the same number. For example, the array [1,2,3,4] is complementary because for all indices i,
nums[i] + nums[n - 1 - i] = 5.

Return the minimum number of moves required to make nums complementary.


Example 1:

Input: nums = [1,2,4,3], limit = 4
Output: 1
Explanation: In 1 move, you can change nums to [1,2,2,3] (underlined elements are changed).
nums[0] + nums[3] = 1 + 3 = 4.
nums[1] + nums[2] = 2 + 2 = 4.
nums[2] + nums[1] = 2 + 2 = 4.
nums[3] + nums[0] = 3 + 1 = 4.
Therefore, nums[i] + nums[n-1-i] = 4 for every i, so nums is complementary.

Example 2:

Input: nums = [1,2,2,1], limit = 2
Output: 2
Explanation: In 2 moves, you can change nums to [2,2,2,2]. You cannot change any number to 3 since
3 > limit.

Example 3:

Input: nums = [1,2,1,2], limit = 2
Output: 0
Explanation: nums is already complementary.


Constraints:

n == nums.length
2 <= n <= 10^5
1 <= nums[i] <= limit <= 10^5
n is even.

"""

# V0
# IDEA : DIFFERENCE ARRAY / LINE SWEEP over every candidate target sum T
#
#   the common sum T ranges over [2, 2*limit]. for ONE pair (x, y) with x <= y:
#     cost 0  -> only when T == x + y
#     cost 1  -> when T can be hit by changing exactly one side, i.e.
#                x + 1 <= T <= y + limit
#     cost 2  -> everything else (both sides must be rewritten)
#
#   so : start every T at cost 2, then subtract 1 on the interval
#        [x+1, y+limit], then subtract 1 more at the single point x+y.
#   record those interval updates in a difference array, prefix-sum it once,
#   and take the minimum over T.
#
#   NOTE : the cost-1 window is [x+1, y+limit] because keeping the SMALL side x
#          reaches at most x+limit and at least x+1, keeping the BIG side y
#          reaches [y+1, y+limit] -- their union is exactly [x+1, y+limit].
#
# time = O(n + limit), space = O(limit)
class Solution(object):
    def minMoves(self, nums, limit):
        n = len(nums)
        hi = 2 * limit
        diff = [0] * (hi + 2)

        for i in range(n // 2):
            x, y = nums[i], nums[n - 1 - i]
            if x > y:
                x, y = y, x
            # base cost 2 over the whole range
            diff[2] += 2
            diff[hi + 1] -= 2
            # one change is enough on [x+1, y+limit]
            diff[x + 1] -= 1
            diff[y + limit + 1] += 1
            # zero change at exactly x + y
            diff[x + y] -= 1
            diff[x + y + 1] += 1

        res = None
        cur = 0
        for t in range(2, hi + 1):
            cur += diff[t]
            if res is None or cur < res:
                res = cur
        return res
