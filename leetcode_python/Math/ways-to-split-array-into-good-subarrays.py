"""

2750. Ways to Split Array Into Good Subarrays
Medium

You are given a binary array nums.

A subarray of an array is good if it contains exactly one element with the value 1.

Return an integer denoting the number of ways to split the array nums into good subarrays. As the number may be too large, return it modulo 10^9 + 7.

A subarray is a contiguous non-empty sequence of elements within an array.


Example 1:

Input: nums = [0,1,0,0,1]
Output: 3
Explanation: There are 3 ways to split nums into good subarrays:
- [0,1] [0,0,1]
- [0,1,0] [0,1]
- [0,1,0,0] [1]

Example 2:

Input: nums = [0,1,0]
Output: 1
Explanation: There is 1 way to split nums into good subarrays:
- [0,1,0]


Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 1

"""

# V0
# IDEA : MULTIPLICATION PRINCIPLE (count the cut positions between consecutive 1s)
#
#  every part must hold EXACTLY one 1, so between two consecutive 1s sitting
#  at indices j < i there has to be exactly ONE cut, and that cut can go in
#  any of the gaps right after j, right after j+1, ..., right after i-1
#  -> that is (i - j) independent choices.
#
#  the zeros BEFORE the first 1 and AFTER the last 1 have no freedom at all:
#  they must join the neighbouring 1's part, contributing a factor of 1.
#
#  the choices for different gaps are independent, so the answer is simply
#  the product of all (i - j).
#
#   NOTE : if the array contains NO 1 at all, no valid split exists -> 0.
#          (an array with exactly one 1 gives the empty product -> 1.)
#   NOTE : take the modulo inside the loop; the product otherwise blows up.
#
# time = O(n), space = O(1)
class Solution(object):
    def numberOfGoodSubarraySplits(self, nums):
        MOD = 10 ** 9 + 7
        res = 1
        prev = -1
        for i, x in enumerate(nums):
            if x == 0:
                continue
            if prev != -1:
                res = res * (i - prev) % MOD
            prev = i
        return 0 if prev == -1 else res
