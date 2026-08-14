"""

1748. Sum of Unique Elements
Easy

You are given an integer array nums. The unique elements of an array are the elements that appear exactly once in the array.

Return the sum of all the unique elements of nums.


Example 1:

Input: nums = [1,2,3,2]
Output: 4
Explanation: The unique elements are [1,3], and the sum is 4.

Example 2:

Input: nums = [1,1,1,1,1]
Output: 0
Explanation: There are no unique elements, and the sum is 0.

Example 3:

Input: nums = [1,2,3,4,5]
Output: 15
Explanation: The unique elements are [1,2,3,4,5], and the sum is 15.


Constraints:

1 <= nums.length <= 100
1 <= nums[i] <= 100

"""

# V0
# IDEA : HASH TABLE COUNT, THEN SUM THE COUNT-ONE KEYS
#
#   "unique" here means "occurs exactly once", not "distinct" - so a value
#   appearing twice contributes NOTHING (not even one copy).
#
#   count everything first, then add up the keys whose count is 1.
#
# time = O(n), space = O(n)
from collections import Counter
class Solution(object):
    def sumOfUnique(self, nums):
        cnt = Counter(nums)
        return sum(x for x, c in cnt.items() if c == 1)
