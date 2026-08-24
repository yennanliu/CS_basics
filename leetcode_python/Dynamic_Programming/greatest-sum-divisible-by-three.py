"""

1262. Greatest Sum Divisible by Three
Medium

Given an integer array nums, return the maximum possible sum of elements of
the array such that it is divisible by three.


Example 1:

Input: nums = [3,6,5,1,8]
Output: 18
Explanation: Pick numbers 3, 6, 1 and 8 their sum is 18 (maximum sum divisible by 3).

Example 2:

Input: nums = [4]
Output: 0
Explanation: Since 4 is not divisible by 3, do not pick any number.

Example 3:

Input: nums = [1,2,3,4,4]
Output: 12
Explanation: Pick numbers 1, 3, 4 and 4 their sum is 12 (maximum sum divisible by 3).


Constraints:

1 <= nums.length <= 4 * 10^4
1 <= nums[i] <= 10^4

"""

# V0
# IDEA : DP ON REMAINDER
#
#  dp[r] = biggest sum seen so far whose remainder mod 3 is r
#
#  for each x, a sum with remainder r can be extended to remainder (r + x) % 3
#
"""

DP def
    dp[r]: the BIGGEST sum seen so far whose remainder mod 3 is r

           -> -inf = that remainder is not reachable yet

DP eq

     for each x, for each reachable r:

        dp_new[(r + x) % 3] = max( dp_new[(r + x) % 3], dp[r] + x )


    -> e.g. every element either joins a running sum (moving it to a new
              remainder bucket) or is skipped (dp copied over)

     init: dp = [0, -inf, -inf]     # only remainder 0 is reachable (empty pick)
     ans = dp[0]

"""
# time = O(n)
# space = O(1)
class Solution(object):
    def maxSumDivThree(self, nums):
        NEG = float('-inf')
        # only remainder 0 is reachable at the start (empty pick, sum = 0)
        dp = [0, NEG, NEG]
        for x in nums:
            cur = dp[:]
            for r in range(3):
                if dp[r] == NEG:
                    continue
                nr = (dp[r] + x) % 3
                cur[nr] = max(cur[nr], dp[r] + x)
            dp = cur
        return dp[0]


# V1
# IDEA : GREEDY
#        take everything, then drop the smallest surplus:
#        if total % 3 == 1 -> drop one smallest num with num % 3 == 1,
#                             or two smallest with num % 3 == 2
#        (mirrored when total % 3 == 2)
"""

DP def
    dp[r]: the BIGGEST sum seen so far whose remainder mod 3 is r

           -> -inf = that remainder is not reachable yet

DP eq

     for each x, for each reachable r:

        dp_new[(r + x) % 3] = max( dp_new[(r + x) % 3], dp[r] + x )


    -> e.g. every element either joins a running sum (moving it to a new
              remainder bucket) or is skipped (dp copied over)

     init: dp = [0, -inf, -inf]     # only remainder 0 is reachable (empty pick)
     ans = dp[0]

"""
# time = O(n)
# space = O(1)
class Solution2(object):
    def maxSumDivThree(self, nums):
        total = sum(nums)
        if total % 3 == 0:
            return total

        # two smallest numbers of each remainder class
        one = sorted([x for x in nums if x % 3 == 1])[:2]
        two = sorted([x for x in nums if x % 3 == 2])[:2]

        best = 0
        if total % 3 == 1:
            if one:
                best = max(best, total - one[0])
            if len(two) == 2:
                best = max(best, total - two[0] - two[1])
        else:
            if two:
                best = max(best, total - two[0])
            if len(one) == 2:
                best = max(best, total - one[0] - one[1])
        return best
