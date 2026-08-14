"""

2148. Count Elements With Strictly Smaller and Greater Elements
Easy

Given an integer array nums, return the number of elements that have both a strictly smaller and a strictly greater element appear in nums.


Example 1:

Input: nums = [11,7,2,15]
Output: 2
Explanation: The element 7 has the element 2 strictly smaller than it and the element 11 strictly greater than it.
Element 11 has element 7 strictly smaller than it and element 15 strictly greater than it.
In total there are 2 elements having both a strictly smaller and a strictly greater element appear in nums.

Example 2:

Input: nums = [-3,3,3,90]
Output: 2
Explanation: The element 3 has the element -3 strictly smaller than it and the element 90 strictly greater than it.
Since there are two elements with the value 3, in total there are 2 elements having both a strictly smaller and a strictly greater element appear in nums.


Constraints:

1 <= nums.length <= 100
-10^5 <= nums[i] <= 10^5

"""

# V0
# IDEA : ANYTHING STRICTLY BETWEEN THE GLOBAL MIN AND MAX QUALIFIES
#
#   an element has something strictly smaller iff it is above min(nums), and
#   something strictly greater iff it is below max(nums). so just count the
#   values in the open interval (min, max).
#
#   NOTE : when all values are equal, min == max and the count is 0, which
#          the same expression already gives.
#
# time = O(n), space = O(1)
class Solution(object):
    def countElements(self, nums):
        lo, hi = min(nums), max(nums)
        return sum(1 for x in nums if lo < x < hi)
