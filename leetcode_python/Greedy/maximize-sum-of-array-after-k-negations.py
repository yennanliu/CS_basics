"""

1005. Maximize Sum Of Array After K Negations
Easy

Given an integer array nums and an integer k, modify the array in the following way:

choose an index i and replace nums[i] with -nums[i].

You should apply this process exactly k times. You may choose the same index i multiple times.

Return the largest possible sum of the array after modifying it in this way.


Example 1:

Input: nums = [4,2,3], k = 1
Output: 5
Explanation: Choose index 1 and nums becomes [4,-2,3].

Example 2:

Input: nums = [3,-1,0,2], k = 3
Output: 6
Explanation: Choose indices (1, 2, 2) and nums becomes [3,1,0,2].

Example 3:

Input: nums = [2,-3,-1,5,-4], k = 2
Output: 13
Explanation: Choose indices (1, 4) and nums becomes [2,3,-1,5,4].


Constraints:

1 <= nums.length <= 10^4
-100 <= nums[i] <= 100
1 <= k <= 10^4

"""

# V0
# IDEA : GREEDY + SORT
#
#   1) sort ascending, flip the most negative values first
#      (each flip of a negative gains 2 * abs(v))
#   2) if k flips are still left after all negatives are gone,
#      keep flipping the SMALLEST abs value back and forth.
#      only the parity of the leftover k matters:
#        - k even -> flip it twice -> no change
#        - k odd  -> we must pay 2 * min(abs value) once
#
# time = O(n log n)
# space = O(1), ignoring the sort
class Solution(object):
    def largestSumAfterKNegations(self, nums, k):
        nums.sort()

        i = 0
        while i < len(nums) and k > 0 and nums[i] < 0:
            nums[i] = -nums[i]
            k -= 1
            i += 1

        total = sum(nums)
        if k % 2 == 1:
            total -= 2 * min(nums)
        return total
