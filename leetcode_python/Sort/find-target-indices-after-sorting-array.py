"""

2089. Find Target Indices After Sorting Array
Easy

You are given a 0-indexed integer array nums and a target element target.

A target index is an index i such that nums[i] == target.

Return a list of the target indices of nums after sorting nums in non-decreasing order. If there are no target indices, return an empty list. The returned list must be sorted in increasing order.


Example 1:

Input: nums = [1,2,5,2,3], target = 2
Output: [1,2]
Explanation: After sorting, nums is [1,2,2,3,5].
The indices where nums[i] == 2 are 1 and 2.

Example 2:

Input: nums = [1,2,5,2,3], target = 3
Output: [3]
Explanation: After sorting, nums is [1,2,2,3,5].
The index where nums[i] == 3 is 3.

Example 3:

Input: nums = [1,2,5,2,3], target = 5
Output: [4]
Explanation: After sorting, nums is [1,2,2,3,5].
The index where nums[i] == 5 is 4.


Constraints:

1 <= nums.length <= 100
1 <= nums[i], target <= 100

"""

# V0
# IDEA : COUNT, DON'T SORT — THE POSITIONS FOLLOW FROM TWO TALLIES
#
#   after sorting, all values < target come first. so if
#       smaller = #{x : x < target}
#       equal   = #{x : x == target}
#   the target indices are exactly  smaller, smaller+1, ..., smaller+equal-1.
#
#   NOTE : this is O(n) and skips the sort entirely; the empty range falls out
#          naturally when equal == 0.
#
# time = O(n), space = O(1) beyond the output
class Solution(object):
    def targetIndices(self, nums, target):
        smaller = sum(1 for x in nums if x < target)
        equal = sum(1 for x in nums if x == target)
        return list(range(smaller, smaller + equal))
