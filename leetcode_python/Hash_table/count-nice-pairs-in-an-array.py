"""

1814. Count Nice Pairs in an Array
Medium

You are given an array nums that consists of non-negative integers. Let us define rev(x) as the reverse of the non-negative integer x. For example, rev(123) = 321, and rev(120) = 21. A pair of indices (i, j) is nice if it satisfies all of the following conditions:

0 <= i < j < nums.length
nums[i] + rev(nums[j]) == nums[j] + rev(nums[i])

Return the number of nice pairs of indices. Since that number can be too large, return it modulo 10^9 + 7.


Example 1:

Input: nums = [42,11,1,97]
Output: 2
Explanation: The two pairs are:
 - (0,3) : 42 + rev(97) = 42 + 79 = 121, 97 + rev(42) = 97 + 24 = 121.
 - (1,2) : 11 + rev(1) = 11 + 1 = 12, 1 + rev(11) = 1 + 11 = 12.

Example 2:

Input: nums = [13,10,35,24,76]
Output: 4


Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 10^9

"""

# V0
# IDEA : REARRANGE THE EQUATION INTO AN INVARIANT, THEN GROUP-COUNT
#
#   nums[i] + rev(nums[j]) == nums[j] + rev(nums[i])
#     <=> nums[i] - rev(nums[i]) == nums[j] - rev(nums[j])
#
#   so define key(x) = x - rev(x). a pair is nice iff both elements share the
#   same key -> count keys, and every group of size v contributes v*(v-1)/2.
#
#   NOTE : the pairwise condition collapses to a per-element value; this is the
#          whole trick, otherwise it is O(n^2).
#
# time = O(n * d), d = number of digits (<= 10)
# space = O(n)
from collections import Counter
class Solution(object):
    def countNicePairs(self, nums):
        MOD = 10 ** 9 + 7

        def rev(x):
            y = 0
            while x:
                y = y * 10 + x % 10
                x //= 10
            return y

        cnt = Counter(x - rev(x) for x in nums)
        res = 0
        for v in cnt.values():
            res += v * (v - 1) // 2
        return res % MOD
