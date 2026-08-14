"""

2567. Minimum Score by Changing Two Elements
Medium

You are given an integer array nums.

The low score of nums is the minimum absolute difference between any two integers.
The high score of nums is the maximum absolute difference between any two integers.
The score of nums is the sum of the high and low scores.

Return the minimum score after changing two elements of nums.


Example 1:

Input: nums = [1,4,7,8,5]
Output: 3
Explanation:
Change nums[0] and nums[1] to be 6 so that nums becomes [6,6,7,8,5].
The low score is the minimum absolute difference: |6 - 6| = 0.
The high score is the maximum absolute difference: |8 - 5| = 3.
The sum of high and low score is 3.

Example 2:

Input: nums = [1,4,3]
Output: 0
Explanation:
Change nums[1] and nums[2] to 1 so that nums becomes [1,1,1].
The sum of maximum absolute difference and minimum absolute difference is 0.


Constraints:

3 <= nums.length <= 10^5
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : SORT + GREEDY (the low score is always free)
#
#   After sorting, low = min gap between adjacent elements, high = last - first.
#
#   Key observation : we get to change TWO elements, and n >= 3, so we can
#   always park one changed value on top of an existing value (a duplicate).
#   That forces low = 0 at zero cost, because a duplicate makes the minimum
#   absolute difference 0. So the answer is purely "minimise the high score
#   with two moves".
#
#   With two moves on a sorted array, the optimal is always to remove two
#   elements from the two ENDS (moving an interior element never shrinks the
#   range). Only 3 splits exist:
#     - drop the 2 smallest  -> nums[n-1] - nums[2]
#     - drop 1 smallest + 1 largest -> nums[n-2] - nums[1]
#     - drop the 2 largest   -> nums[n-3] - nums[0]
#
#   NOTE : the two "dropped" elements are not deleted, they are re-assigned to
#          a value inside the surviving range - which is exactly what also
#          pins low to 0. Both goals are satisfied by the same two moves.
#
#   NOTE : n >= 3 guarantees nums[2] and nums[n-3] exist, so no bounds check
#          is needed. When n == 3 all three candidates collapse to 0.
#
# time = O(n * log(n)), space = O(n) for the sort
class Solution(object):
    def minimizeSum(self, nums):
        nums.sort()
        n = len(nums)
        return min(nums[n - 1] - nums[2],
                   nums[n - 2] - nums[1],
                   nums[n - 3] - nums[0])
