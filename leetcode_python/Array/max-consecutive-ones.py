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


# V0-1
# IDEA : itertools.groupby ON MAXIMAL RUNS
#
#  groupby collapses the array into maximal runs of equal values, so the
#  answer is just the longest run whose key is 1. no manual counter and no
#  reset branch - the run boundaries come out of the grouping itself.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def findMaxConsecutiveOnes(self, nums):
        import itertools
        best = 0
        for key, grp in itertools.groupby(nums):
            if key == 1:
                best = max(best, len(list(grp)))
        return best


# V0-2
# IDEA : SLIDING WINDOW ALLOWING AT MOST 0 ZEROS
#
#  read the task as "longest subarray containing at most k zeros" with k = 0 :
#  grow the right edge, and while the window holds a zero drag the left edge
#  past it. the widest legal window is the answer.
#  the point of writing it this way is that raising k to 1 / 2 / K turns this
#  same block into LC 487 / LC 1004 - the running-counter form of V0 does not
#  generalise like that.
#
# time = O(n)
# space = O(1)
class Solution(object):
    def findMaxConsecutiveOnes(self, nums):
        left = 0
        zeros = 0
        best = 0
        for right in range(len(nums)):
            if nums[right] == 0:
                zeros += 1
            while zeros > 0:
                if nums[left] == 0:
                    zeros -= 1
                left += 1
            best = max(best, right - left + 1)
        return best
