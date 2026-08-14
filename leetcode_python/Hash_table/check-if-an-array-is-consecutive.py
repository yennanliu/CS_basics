"""

2229. Check if an Array Is Consecutive
Easy

Given an integer array nums, return true if nums is consecutive, otherwise return false.

An array is consecutive if it contains every number in the range [x, x + n - 1] (inclusive), where x is the minimum number in the array and n is the length of the array.


Example 1:

Input: nums = [1,3,4,2]
Output: true
Explanation:
The minimum value is 1 and the length of nums is 4.
All of the values in the range [x, x + n - 1] = [1, 1 + 4 - 1] = [1, 4] = (1, 2, 3, 4) occur in nums.
Therefore, nums is consecutive.

Example 2:

Input: nums = [1,3]
Output: false
Explanation:
The minimum value is 1 and the length of nums is 2.
The value 2 in the range [x, x + n - 1] = [1, 1 + 2 - 1], = [1, 2] = (1, 2) does not occur in nums.
Therefore, nums is not consecutive.

Example 3:

Input: nums = [3,5,4]
Output: true
Explanation:
The minimum value is 3 and the length of nums is 3.
All of the values in the range [x, x + n - 1] = [3, 3 + 3 - 1] = [3, 5] = (3, 4, 5) occur in nums.
Therefore, nums is consecutive.


Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 10^5

"""

# V0
# IDEA : HASH SET (three numbers must agree)
#
#   nums is consecutive  <=>  it holds n DISTINCT values spanning exactly
#   the window [min, min + n - 1], i.e.
#       len(set(nums)) == max - min + 1 == len(nums)
#
#   NOTE : checking only max - min + 1 == n is not enough - duplicates would
#          slip through (e.g. [1,1,3] has max-min+1 == 3 == n but is not
#          consecutive), hence the set size is compared as well.
#
# time = O(n), space = O(n)
class Solution(object):
    def isConsecutive(self, nums):
        n = len(nums)
        lo, hi = min(nums), max(nums)
        return len(set(nums)) == n and hi - lo + 1 == n
