"""

3392. Count Subarrays of Length Three With a Condition
Easy

Given an integer array nums, return the number of subarrays of length 3 such that the sum of the first and third numbers equals exactly half of the second number.


Example 1:

Input: nums = [1,2,1,4,1]
Output: 1
Explanation:
Only the subarray [1,4,1] contains exactly 3 elements where the sum of the first and third numbers equals half the middle number.

Example 2:

Input: nums = [1,1,1]
Output: 0
Explanation:
[1,1,1] is the only subarray of length 3. However, its first and third numbers do not add to half the middle number.


Constraints:

3 <= nums.length <= 100
-100 <= nums[i] <= 100

"""

# V0
# IDEA : SLIDE A WINDOW OF 3 AND TEST THE CONDITION WITHOUT DIVIDING
#
#   "first + third == middle / 2" is checked as 2 * (first + third) == middle
#   so no fractions appear — with an odd middle the doubled form simply never
#   matches, which is the right answer anyway.
#
# time = O(n), space = O(1)
class Solution(object):
    def countSubarrays(self, nums):
        res = 0
        for i in range(len(nums) - 2):
            if 2 * (nums[i] + nums[i + 2]) == nums[i + 1]:
                res += 1
        return res
