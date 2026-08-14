"""

2495. Number of Subarrays Having Even Product
Medium

Given a 0-indexed integer array nums, return the number of subarrays of nums having an even product.


Example 1:

Input: nums = [9,6,7,13]
Output: 6
Explanation: There are 6 subarrays with an even product:
- nums[0..1] = 9 * 6 = 54.
- nums[0..2] = 9 * 6 * 7 = 378.
- nums[0..3] = 9 * 6 * 7 * 13 = 4914.
- nums[1..1] = 6.
- nums[1..2] = 6 * 7 = 42.
- nums[1..3] = 6 * 7 * 13 = 546.

Example 2:

Input: nums = [7,3,5]
Output: 0
Explanation: There are no subarrays with an even product.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : DP (COUNT SUBARRAYS ENDING AT EACH INDEX) + LAST EVEN INDEX
#
#   a product is even <-> the subarray contains at least ONE even element.
#
#   so for each right end i, let `last` = index of the most recent even
#   element at or before i. every left end l in [0, last] gives an even
#   product -> that is exactly (last + 1) subarrays ending at i.
#
#   summing (last + 1) over all i gives the answer, which is the classic
#   "dp over subarrays ending here" collapsed to a single running variable.
#
#   NOTE : `last` starts at -1 so that a prefix with no even element
#          contributes (-1 + 1) = 0 subarrays.
#
# time = O(n), space = O(1)
class Solution(object):
    def evenProduct(self, nums):
        res = 0
        last = -1
        for i, x in enumerate(nums):
            if x % 2 == 0:
                last = i
            res += last + 1
        return res
