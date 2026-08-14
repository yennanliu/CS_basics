"""

2470. Number of Subarrays With LCM Equal to K
Medium

Given an integer array nums and an integer k, return the number of subarrays of nums where the least common multiple of the subarray's elements is k.

A subarray is a contiguous non-empty sequence of elements within an array.

The least common multiple of an array is the smallest positive integer that is divisible by all the elements of the array.


Example 1:

Input: nums = [3,6,2,7,1], k = 6
Output: 4
Explanation: The subarrays of nums where 6 is the least common multiple of all the subarray's elements are:
- [3,6,2,7,1]
- [3,6,2,7,1]
- [3,6,2,7,1]
- [3,6,2,7,1]

Example 2:

Input: nums = [3], k = 2
Output: 0
Explanation: There are no subarrays of nums where 2 is the least common multiple of all the subarray's elements.


Constraints:

1 <= nums.length <= 1000
1 <= nums[i], k <= 1000

"""

# V0
# IDEA : EXTEND EACH START WHILE THE RUNNING LCM STILL DIVIDES k
#
#   the LCM of a growing subarray is non-decreasing and always a multiple of
#   the previous one, so once it exceeds k — or stops dividing k — no longer
#   subarray from that start can ever come back to k. that early break keeps
#   the O(n^2) scan tight.
#
#   lcm(a, b) = a // gcd(a, b) * b, dividing first to keep the value small.
#
# time = O(n^2 log(max)), space = O(1)
from math import gcd


class Solution(object):
    def subarrayLCM(self, nums, k):
        n = len(nums)
        res = 0
        for i in range(n):
            cur = 1
            for j in range(i, n):
                cur = cur // gcd(cur, nums[j]) * nums[j]
                if k % cur:
                    break              # can never divide back down to k
                if cur == k:
                    res += 1
        return res
