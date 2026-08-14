"""

2012. Sum of Beauty in the Array
Medium

You are given a 0-indexed integer array nums. For each index i (1 <= i <= nums.length - 2) the beauty of nums[i] equals:

2, if nums[j] < nums[i] < nums[k], for all 0 <= j < i and for all i < k <= nums.length - 1.
1, if nums[i - 1] < nums[i] < nums[i + 1], and the previous condition is not satisfied.
0, if none of the previous conditions holds.

Return the sum of beauty of all nums[i] where 1 <= i <= nums.length - 2.


Example 1:

Input: nums = [1,2,3]
Output: 2
Explanation: For each index i in the range 1 <= i <= 1:
- The beauty of nums[1] equals 2.

Example 2:

Input: nums = [2,4,6,4]
Output: 1
Explanation: For each index i in the range 1 <= i <= 2:
- The beauty of nums[1] equals 1.
- The beauty of nums[2] equals 0.

Example 3:

Input: nums = [3,2,1]
Output: 0
Explanation: For each index i in the range 1 <= i <= 1:
- The beauty of nums[1] equals 0.


Constraints:

3 <= nums.length <= 10^5
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : PREFIX MAX + SUFFIX MIN
#
#   "nums[i] bigger than everything on the left and smaller than everything on
#   the right" is exactly
#
#       prefixMax[i - 1] < nums[i] < suffixMin[i + 1]     -> beauty 2
#
#   otherwise fall back to the cheap local test nums[i-1] < nums[i] < nums[i+1]
#   -> beauty 1, else 0.
#
#   NOTE : precompute the suffix minima in one backward pass, then sweep
#          forward carrying the running prefix maximum.
#
# time = O(n), space = O(n)
class Solution(object):
    def sumOfBeauties(self, nums):
        n = len(nums)

        suf_min = [0] * n
        suf_min[n - 1] = nums[n - 1]
        for i in range(n - 2, -1, -1):
            suf_min[i] = min(suf_min[i + 1], nums[i])

        res = 0
        pre_max = nums[0]
        for i in range(1, n - 1):
            if pre_max < nums[i] < suf_min[i + 1]:
                res += 2
            elif nums[i - 1] < nums[i] < nums[i + 1]:
                res += 1
            pre_max = max(pre_max, nums[i])

        return res
