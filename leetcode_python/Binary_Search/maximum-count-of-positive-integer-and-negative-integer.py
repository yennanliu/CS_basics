"""

2529. Maximum Count of Positive Integer and Negative Integer
Easy

Given an array nums sorted in non-decreasing order, return the maximum between
the number of positive integers and the number of negative integers.

In other words, if the number of positive integers in nums is pos and the number
of negative integers is neg, then return the maximum of pos and neg.

Note that 0 is neither positive nor negative.


Example 1:

Input: nums = [-2,-1,-1,1,2,3]
Output: 3
Explanation: There are 3 positive integers and 3 negative integers.
The maximum count among them is 3.

Example 2:

Input: nums = [-3,-2,-1,0,0,1,2]
Output: 3
Explanation: There are 2 positive integers and 3 negative integers.
The maximum count among them is 3.

Example 3:

Input: nums = [5,20,66,1314]
Output: 4
Explanation: There are 4 positive integers and 0 negative integers.
The maximum count among them is 4.


Constraints:

1 <= nums.length <= 2000
-2000 <= nums[i] <= 2000
nums is sorted in a non-decreasing order.

Follow up: Can you solve the problem in O(log(n)) time complexity?

"""

# V0
# IDEA : BINARY SEARCH ON THE SORTED ARRAY (the follow-up's O(log n))
#
#   the array is non-decreasing, so the negatives, the zeros and the positives
#   each form one contiguous block. Two boundary searches are enough:
#
#     neg = bisect_left(nums, 0)     -> index of the first value >= 0
#                                       == how many values are < 0
#     pos = n - bisect_right(nums, 0) -> count of values > 0
#
#   answer = max(neg, pos).
#
#   NOTE : the left/right variants are NOT interchangeable here. The zeros sit
#          between the two blocks and belong to neither count, so the negative
#          count must use bisect_LEFT(0) and the positive count must use
#          bisect_RIGHT(0) -- swapping them silently counts the zeros.
#
# time = O(log n), space = O(1)
import bisect
class Solution(object):
    def maximumCount(self, nums):
        n = len(nums)
        neg = bisect.bisect_left(nums, 0)       # values strictly < 0
        pos = n - bisect.bisect_right(nums, 0)  # values strictly > 0
        return max(neg, pos)
