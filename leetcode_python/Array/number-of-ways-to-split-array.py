"""

2270. Number of Ways to Split Array
Medium

You are given a 0-indexed integer array nums of length n.

nums contains a valid split at index i if the following are true:

The sum of the first i + 1 elements is greater than or equal to the sum of the last n - i - 1 elements.
There is at least one element to the right of i. That is, 0 <= i < n - 1.

Return the number of valid splits in nums.


Example 1:

Input: nums = [10,4,-8,7]
Output: 2
Explanation:
There are three ways of splitting nums into two non-empty parts:
- Split nums at index 0. Then, the first part is [10], and its sum is 10. The second part is [4,-8,7], and its sum is 3. Since 10 >= 3, i = 0 is a valid split.
- Split nums at index 1. Then, the first part is [10,4], and its sum is 14. The second part is [-8,7], and its sum is -1. Since 14 >= -1, i = 1 is a valid split.
- Split nums at index 2. Then, the first part is [10,4,-8], and its sum is 6. The second part is [7], and its sum is 7. Since 6 < 7, i = 2 is not a valid split.
Thus, the number of valid splits in nums is 2.

Example 2:

Input: nums = [2,3,1,0]
Output: 2
Explanation:
There are two valid splits in nums:
- Split nums at index 1. Then, the first part is [2,3], and its sum is 5. The second part is [1,0], and its sum is 1. Since 5 >= 1, i = 1 is a valid split.
- Split nums at index 2. Then, the first part is [2,3,1], and its sum is 6. The second part is [0], and its sum is 0. Since 6 >= 0, i = 2 is a valid split.


Constraints:

2 <= nums.length <= 10^5
-10^5 <= nums[i] <= 10^5

"""

# V0
# IDEA : PREFIX SUM (one pass, total sum known up front)
#
#   let total = sum(nums) and pre = sum of nums[0..i].
#   the suffix sum is total - pre, so the split at i is valid iff
#
#       pre >= total - pre     i.e.   2 * pre >= total
#
#   sweep i from 0 to n-2, keeping pre incrementally.
#
#   NOTE : values can be negative, so prefix sums are NOT monotone - we cannot
#          binary search, but the linear sweep is already O(n).
#
# time = O(n), space = O(1)
class Solution(object):
    def waysToSplitArray(self, nums):
        total = sum(nums)
        pre = 0
        res = 0
        for i in range(len(nums) - 1):
            pre += nums[i]
            if 2 * pre >= total:
                res += 1
        return res


# V0-1
# IDEA : BRUTE FORCE (recompute both halves per split point)
#
#   for every split index i in [0, n-2], literally slice the array in two
#   and sum each side. no prefix bookkeeping at all - this is the direct
#   translation of the problem statement, kept as the baseline that the
#   prefix-sum version above optimises.
#
# time = O(n^2)
# space = O(n)   (the slices)
class Solution(object):
    def waysToSplitArray(self, nums):
        n = len(nums)
        res = 0
        for i in range(n - 1):
            if sum(nums[:i + 1]) >= sum(nums[i + 1:]):
                res += 1
        return res


# V0-2
# IDEA : EXPLICIT PREFIX / SUFFIX ARRAYS (two pass, built with accumulate)
#
#   pre[i] = nums[0] + ... + nums[i]
#   suf[i] = nums[i] + ... + nums[n-1]   (accumulate over the reversed array)
#
#   then a split at i is valid iff pre[i] >= suf[i + 1]. this trades O(n)
#   extra memory for the ability to answer "sum of the left / right part"
#   for ANY i in O(1) afterwards (handy if the splits were queried, not counted).
#
# time = O(n)
# space = O(n)
from itertools import accumulate


class Solution(object):
    def waysToSplitArray(self, nums):
        n = len(nums)
        pre = list(accumulate(nums))
        suf = list(accumulate(nums[::-1]))[::-1]
        return sum(1 for i in range(n - 1) if pre[i] >= suf[i + 1])
