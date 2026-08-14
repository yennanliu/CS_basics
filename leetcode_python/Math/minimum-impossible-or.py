"""

2568. Minimum Impossible OR
Medium

You are given a 0-indexed integer array nums.

We say that an integer x is expressible from nums if there exist some integers 0 <= index1 < index2 < ... < indexk < nums.length for which nums[index1] | nums[index2] | ... | nums[indexk] = x. In other words, an integer is expressible if it can be written as the bitwise OR of some subsequence of nums.

Return the minimum positive non-zero integer that is not expressible from nums.


Example 1:

Input: nums = [2,1]
Output: 4
Explanation: 1 and 2 are already present in the array. We know that 3 is expressible, since nums[0] | nums[1] = 2 | 1 = 3. Since 4 is not expressible, we return 4.

Example 2:

Input: nums = [5,3,2]
Output: 1
Explanation: We can show that 1 is the smallest number that is not expressible.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : BRAINTEASER - THE ANSWER IS ALWAYS A POWER OF 2
#
#   Claim : every non-power-of-2 positive integer x is either expressible, or
#   is "shadowed" by a smaller power of 2 that is not expressible.
#
#   Why : write x in binary. If EVERY set bit of x is itself present in nums
#   as a standalone power of 2, then OR-ing those elements gives exactly x, so
#   x is expressible. Otherwise some power of 2 p <= x is missing from nums,
#   and p is not expressible either (see below) - and p < x since x has >= 2
#   set bits. So x is never the SMALLEST non-expressible value.
#
#   Why a power of 2 p missing from nums is not expressible : OR only ever
#   adds bits, never removes them. To land on p (a single set bit) every chosen
#   element must be a subset of p's bits, i.e. must be exactly p or 0. Since
#   nums[i] >= 1 and p is not in nums, no subsequence ORs to p.
#
#   So : answer = smallest 2^i that does not literally appear in nums.
#
#   NOTE : we only need i in [0, 30) - nums[i] <= 10^9 < 2^30, so 2^30 is
#          guaranteed absent and the scan always terminates.
#
# time = O(n + log(max(nums))), space = O(n)
class Solution(object):
    def minImpossibleOR(self, nums):
        seen = set(nums)
        i = 0
        while (1 << i) in seen:
            i += 1
        return 1 << i
