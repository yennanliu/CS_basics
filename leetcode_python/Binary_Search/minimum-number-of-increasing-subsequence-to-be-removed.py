"""

3231. Minimum Number of Increasing Subsequence to Be Removed
Hard
🔒 (premium)

Given an array of integers nums, you are allowed to perform the following operation any number of times:

Remove a strictly increasing subsequence from the array.

Your task is to find the minimum number of operations required to make the array empty.


Example 1:

Input: nums = [5,3,1,4,2]
Output: 3
Explanation:
We remove subsequences [1, 2], [3, 4], [5].

Example 2:

Input: nums = [1,2,3,4,5]
Output: 1
Explanation:
We remove the subsequence [1, 2, 3, 4, 5].

Example 3:

Input: nums = [5,4,3,2,1]
Output: 5
Explanation:
We remove the subsequences [5], [4], [3], [2], [1].


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : DILWORTH — THE ANSWER IS THE LONGEST NON-INCREASING SUBSEQUENCE
#
#   order the positions by "i before j and nums[i] < nums[j]". a strictly
#   increasing subsequence is a CHAIN in that order, so the question is the
#   minimum number of chains covering everything.
#
#   Dilworth's theorem says that equals the largest ANTICHAIN — a set of
#   positions no two of which are comparable, i.e. a longest NON-INCREASING
#   subsequence.
#
#   that length comes from patience sorting : negate the values to turn
#   "non-increasing" into "non-decreasing", then keep the usual tails array
#   with bisect_RIGHT (equal values may extend a pile).
#
# time = O(n log n), space = O(n)
import bisect


class Solution(object):
    def minOperations(self, nums):
        tails = []
        for x in nums:
            v = -x                              # non-increasing -> non-decreasing
            pos = bisect.bisect_right(tails, v)
            if pos == len(tails):
                tails.append(v)
            else:
                tails[pos] = v
        return len(tails)
