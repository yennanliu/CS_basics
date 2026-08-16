"""

2903. Find Indices With Index and Value Difference I
Easy

You are given a 0-indexed integer array nums having length n, an integer indexDifference, and an integer valueDifference.

Your task is to find two indices i and j, both in the range [0, n - 1], that satisfy the following conditions:

- abs(i - j) >= indexDifference, and
- abs(nums[i] - nums[j]) >= valueDifference

Return an integer array answer, where answer = [i, j] if there are two such indices, and answer = [-1, -1] otherwise. If there are multiple choices for the two indices, return any of them.

Note: i and j may be equal.


Example 1:

Input: nums = [5,1,4,1], indexDifference = 2, valueDifference = 4
Output: [0,3]
Explanation: In this example, i = 0 and j = 3 can be selected.
abs(0 - 3) >= 2 and abs(nums[0] - nums[3]) >= 4.
Hence, a valid answer is [0,3].
[3,0] is also a valid answer.

Example 2:

Input: nums = [2,1], indexDifference = 0, valueDifference = 0
Output: [0,0]
Explanation: In this example, i = 0 and j = 0 can be selected.
abs(0 - 0) >= 0 and abs(nums[0] - nums[0]) >= 0.
Hence, a valid answer is [0,0].
Other valid answers are [0,1], [1,0], and [1,1].

Example 3:

Input: nums = [1,2,3], indexDifference = 2, valueDifference = 4
Output: [-1,-1]
Explanation: In this example, it can be shown that it is impossible to find two indices that satisfy both conditions.
Hence, [-1,-1] is returned.


Constraints:

1 <= n == nums.length <= 100
0 <= nums[i] <= 50
0 <= indexDifference <= 100
0 <= valueDifference <= 50

"""

# V0
# IDEA : SLIDING "GAP" + RUNNING MIN / MAX INDEX
#
#   abs() on both sides means we only ever need, for a right end i, the
#   SMALLEST and the LARGEST value among the indices that are already far
#   enough away from i (i.e. all j <= i - indexDifference).
#     -> if nums[i] - nums[mi] >= valueDifference, [mi, i] works
#     -> if nums[mx] - nums[i] >= valueDifference, [mx, i] works
#   one of those two must trigger whenever ANY valid pair ending at i exists,
#   because the best |difference| against a fixed nums[i] is achieved either
#   at the running min or at the running max.
#
#   NOTE : indexDifference can be 0, so i and j may be the same index; the
#          loop below naturally allows j == i since j = i - 0 = i.
#   NOTE : indexDifference can exceed n - 1, in which case the loop body never
#          runs and we correctly fall through to [-1, -1].
#
# time = O(n), space = O(1)
class Solution(object):
    def findIndices(self, nums, indexDifference, valueDifference):
        mi = mx = 0
        for i in range(indexDifference, len(nums)):
            j = i - indexDifference
            if nums[j] < nums[mi]:
                mi = j
            if nums[j] > nums[mx]:
                mx = j
            if nums[i] - nums[mi] >= valueDifference:
                return [mi, i]
            if nums[mx] - nums[i] >= valueDifference:
                return [mx, i]
        return [-1, -1]
