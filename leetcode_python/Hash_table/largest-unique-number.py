"""

1133. Largest Unique Number
Easy

Given an integer array nums, return the largest integer that only occurs once.
If no integer occurs once, return -1.


Example 1:

Input: nums = [5,7,3,9,4,9,8,3,1]
Output: 8
Explanation: The maximum integer in the array is 9 but it is repeated.
The number 8 occurs only once, so it is the answer.

Example 2:

Input: nums = [9,9,8,8]
Output: -1
Explanation: There is no number that occurs only once.


Constraints:

1 <= nums.length <= 2000
0 <= nums[i] <= 1000

"""

# V0
# IDEA : HASH TABLE (counting)
#        count every value, then take the max among values with count == 1
# time = O(n)
# space = O(n)
from collections import Counter
class Solution(object):
    def largestUniqueNumber(self, nums):
        cnt = Counter(nums)
        res = -1
        for x, c in cnt.items():
            if c == 1:
                res = max(res, x)
        return res
