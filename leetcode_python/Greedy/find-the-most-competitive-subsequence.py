"""

1673. Find the Most Competitive Subsequence
Medium

Given an integer array nums and a positive integer k, return the most competitive subsequence of
nums of size k.

An array's subsequence is a resulting sequence obtained by erasing some (possibly zero) elements
from the array.

We define that a subsequence a is more competitive than a subsequence b (of the same length) if in
the first position where a and b differ, subsequence a has a number less than the corresponding
number in b. For example, [1,3,4] is more competitive than [1,3,5] because the first position they
differ is at the final number, and 4 is less than 5.


Example 1:

Input: nums = [3,5,2,6], k = 2
Output: [2,6]
Explanation: Among the set of every possible subsequence: {[3,5], [3,2], [3,6], [5,2], [5,6], [2,6]},
[2,6] is the most competitive.

Example 2:

Input: nums = [2,4,3,3,5,4,9,6], k = 4
Output: [2,3,3,4]


Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 10^9
1 <= k <= nums.length

"""

# V0
# IDEA : MONOTONIC (INCREASING) STACK + FEASIBILITY GUARD
#
#   "most competitive" = lexicographically smallest -> greedily make each earlier
#   position as small as possible.
#
#   scan left -> right keeping a non-decreasing stack:
#     while the stack top is BIGGER than the incoming value, popping it makes the
#     answer smaller -> pop.
#   guard : we may only pop if we can still fill k slots, i.e.
#     len(stack) + (n - i) > k   (remaining elements including nums[i])
#
#   NOTE : push only while len(stack) < k, so the stack never overshoots.
#
# time = O(n), space = O(k)
class Solution(object):
    def mostCompetitive(self, nums, k):
        n = len(nums)
        stack = []
        for i, v in enumerate(nums):
            while stack and stack[-1] > v and len(stack) + (n - i) > k:
                stack.pop()
            if len(stack) < k:
                stack.append(v)
        return stack
