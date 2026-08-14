"""

1712. Ways to Split Array Into Three Subarrays
Medium

A split of an integer array is good if:

The array is split into three non-empty contiguous subarrays - named left, mid, right respectively from left to right.
The sum of the elements in left is less than or equal to the sum of the elements in mid, and the sum of the elements in mid is less than or equal to the sum of the elements in right.

Given nums, an array of non-negative integers, return the number of good ways to split nums. As the number may be too large, return it modulo 10^9 + 7.


Example 1:

Input: nums = [1,1,1]
Output: 1
Explanation: The only good way to split nums is [1] [1] [1].

Example 2:

Input: nums = [1,2,2,2,5,0]
Output: 3
Explanation: There are three good ways of splitting nums:
[1] [2] [2,2,5,0]
[1] [2,2] [2,5,0]
[1,2] [2,2] [5,0]

Example 3:

Input: nums = [3,2,1]
Output: 0
Explanation: There is no good way to split nums.


Constraints:

3 <= nums.length <= 10^5
0 <= nums[i] <= 10^4

"""

# V0
# IDEA : PREFIX SUM + BINARY SEARCH (nums are non-negative -> prefix is monotone)
#
#   let s[i] = nums[0] + ... + nums[i]. because every nums[i] >= 0, s is
#   NON-DECREASING, which is exactly what makes binary search legal here.
#
#   fix the end index i of `left`  (so left sum = s[i]).
#   the end index m of `mid` must satisfy both:
#     left <= mid    ->  s[m] - s[i] >= s[i]        ->  s[m] >= 2*s[i]
#     mid  <= right  ->  s[m] - s[i] <= s[-1]-s[m]  ->  s[m] <= (s[-1]+s[i])/2
#
#   both bounds are monotone in s[m], so the valid m form a contiguous
#   window [j, k) inside [i+1, n-1) - found with bisect_left / bisect_right.
#   add k - j to the answer.
#
#   NOTE : search range is (i+1, n-1): `mid` must be non-empty (m > i) and
#          `right` must be non-empty (m < n-1).
#   NOTE : take the modulo only at the end - Python ints do not overflow.
#
# time = O(n * log n), space = O(n)
from bisect import bisect_left, bisect_right
from itertools import accumulate
class Solution(object):
    def waysToSplit(self, nums):
        MOD = 10 ** 9 + 7

        n = len(nums)
        s = list(accumulate(nums))
        total = s[-1]

        res = 0
        for i in range(n - 2):
            # smallest m with s[m] >= 2 * s[i]
            j = bisect_left(s, 2 * s[i], i + 1, n - 1)
            # largest m with s[m] <= (total + s[i]) // 2, as an exclusive bound
            k = bisect_right(s, (total + s[i]) // 2, j, n - 1)
            res += k - j

        return res % MOD
