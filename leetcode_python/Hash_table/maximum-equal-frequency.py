"""

1224. Maximum Equal Frequency
Hard

Given an array nums of positive integers, return the longest possible length of an array
prefix of nums, such that it is possible to remove exactly one element from this prefix
so that every number that has appeared in it will have the same number of occurrences.

If after removing one element there are no remaining elements, it's still considered that
every appeared number has the same number of ocurrences (0).

Example 1:

Input: nums = [2,2,1,1,5,3,3,5]
Output: 7
Explanation: For the subarray [2,2,1,1,5,3,3] of length 7, if we remove nums[4] = 5, we
will get [2,2,1,1,3,3], so that each number will appear exactly twice.

Example 2:

Input: nums = [1,1,1,2,2,2,3,3,3,4,4,4,5]
Output: 13


Constraints:

2 <= nums.length <= 10^5
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : HASH TABLE (count of values) + HASH TABLE (count of counts)
"""
 cnt[v]   = how many times value v has appeared in the prefix
 ccnt[c]  = how many DISTINCT values currently have count c
 mx       = the largest count seen so far

 a prefix of length i is valid iff ONE of these holds:

   1) mx == 1
      -> every value appears once, drop any single one

   2) ccnt[mx] == 1  and  ccnt[mx] * mx + ccnt[mx-1] * (mx-1) == i
      -> exactly one value has the top count mx, all others have mx-1,
         drop one copy of the top value

   3) ccnt[1] == 1  and  ccnt[mx] * mx + 1 == i
      -> everything appears mx times except a single value that appears once,
         drop that lone value
"""
# time = O(n)
# space = O(n)
from collections import defaultdict
class Solution(object):
    def maxEqualFreq(self, nums):
        cnt = defaultdict(int)
        ccnt = defaultdict(int)
        res = 0
        mx = 0

        for i, v in enumerate(nums, 1):
            if cnt[v] > 0:
                ccnt[cnt[v]] -= 1
            cnt[v] += 1
            mx = max(mx, cnt[v])
            ccnt[cnt[v]] += 1

            if mx == 1:
                res = i
            elif ccnt[mx] == 1 and ccnt[mx] * mx + ccnt[mx - 1] * (mx - 1) == i:
                res = i
            elif ccnt[1] == 1 and ccnt[mx] * mx + 1 == i:
                res = i

        return res
