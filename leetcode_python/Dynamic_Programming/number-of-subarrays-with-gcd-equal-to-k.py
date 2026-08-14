"""

2447. Number of Subarrays With GCD Equal to K
Medium

Given an integer array nums and an integer k, return the number of subarrays of nums where the greatest common divisor of the subarray's elements is k.

A subarray is a contiguous non-empty sequence of elements within an array.

The greatest common divisor of an array is the largest integer that evenly divides all the array elements.


Example 1:

Input: nums = [9,3,1,2,6,3], k = 3
Output: 4
Explanation: The subarrays of nums where 3 is the greatest common divisor of all the subarray's elements are:
- [9,3,1,2,6,3]
- [9,3,1,2,6,3]
- [9,3,1,2,6,3]
- [9,3,1,2,6,3]

Example 2:

Input: nums = [4], k = 7
Output: 0
Explanation: There are no subarrays of nums where 7 is the greatest common divisor of all the subarray's elements.


Constraints:

1 <= nums.length <= 1000
1 <= nums[i], k <= 10^9

"""

# V0
# IDEA : DP OVER "DISTINCT GCDs ENDING AT i" (a classic logarithmic-state trick)
#
#   let cur = { g : how many subarrays ending at i have gcd g }.
#   moving to i + 1 :  cur' = { gcd(g, nums[i+1]) } merged with { nums[i+1] }
#
#   the key fact : extending a subarray leftwards can only keep the gcd or
#   at least HALVE it, so at any index there are only O(log(max)) distinct
#   gcd values -> the dict stays tiny.
#
#   answer += cur[k] at every index.
#
# time = O(n * log(max(nums)) * log(max(nums))), space = O(log(max(nums)))
from math import gcd
class Solution(object):
    def subarrayGCD(self, nums, k):
        res = 0
        prev = {}
        for x in nums:
            cur = {x: 1}
            for g, c in prev.items():
                ng = gcd(g, x)
                cur[ng] = cur.get(ng, 0) + c
            res += cur.get(k, 0)
            prev = cur
        return res
