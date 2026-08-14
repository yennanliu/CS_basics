"""

2436. Minimum Split Into Subarrays With GCD Greater Than One
Medium

You are given an array nums consisting of positive integers.

Split the array into one or more disjoint subarrays such that:

Each element of the array belongs to exactly one subarray, and
The GCD of the elements of each subarray is strictly greater than 1.

Return the minimum number of subarrays that can be obtained after the split.

Note that:

The GCD of a subarray is the largest positive integer that evenly divides all the elements of the subarray.
A subarray is a contiguous part of the array.


Example 1:

Input: nums = [12,6,3,14,8]
Output: 2
Explanation: We can split the array into the subarrays: [12,6,3] and [14,8].
- The GCD of 12, 6 and 3 is 3, which is strictly greater than 1.
- The GCD of 14 and 8 is 2, which is strictly greater than 1.
It can be shown that splitting the array into one subarray will make the GCD = 1.

Example 2:

Input: nums = [4,12,6,14]
Output: 1
Explanation: We can split the array into only one subarray, which is the whole array.


Constraints:

1 <= nums.length <= 2000
2 <= nums[i] <= 10^9

"""

# V0
# IDEA : GREEDY + RUNNING GCD (extend the current piece as long as gcd stays > 1)
#
#   scan left to right keeping g = gcd of the current (open) subarray.
#   when adding nums[i] makes g == 1, the current piece must end BEFORE i,
#   so start a brand new piece at i (g = nums[i], count += 1).
#
#   why greedy is optimal : gcd is monotonically non-increasing as a piece
#   grows, so a longer prefix piece never hurts - cutting later can only
#   reduce the number of pieces.
#   NOTE : every nums[i] >= 2, so a single element always forms a valid piece.
#
# time = O(n * log(max(nums))), space = O(1)
from math import gcd
class Solution(object):
    def minimumSplits(self, nums):
        res, g = 1, 0
        for x in nums:
            g = gcd(g, x)
            if g == 1:
                res += 1
                g = x
        return res
