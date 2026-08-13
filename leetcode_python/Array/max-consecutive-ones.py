"""

485. Max Consecutive Ones
Easy

Given a binary array nums, return the maximum number of consecutive 1's in the
array.

Example 1:

Input: nums = [1,1,0,1,1,1]
Output: 3
Explanation: The first two digits or the last three digits are consecutive 1s.
The maximum number of consecutive 1s is 3.

Example 2:

Input: nums = [1,0,1,1,0,1]
Output: 2

Constraints:

1 <= nums.length <= 10^5
nums[i] is either 0 or 1.

"""

# V0
# IDEA : ONE PASS RUNNING COUNTER
#
#  Keep a counter of the current streak of 1's; a 0 resets it to 0.
#  Track the best streak seen so far.
#
# time = O(n)
# space = O(1)
class Solution(object):
    def findMaxConsecutiveOnes(self, nums):
        best = 0
        cur = 0
        for x in nums:
            if x == 1:
                cur += 1
                best = max(best, cur)
            else:
                cur = 0     # streak broken
        return best
