"""

2905. Find Indices With Index and Value Difference II
Medium

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

1 <= n == nums.length <= 10^5
0 <= nums[i] <= 10^9
0 <= indexDifference <= 10^5
0 <= valueDifference <= 10^9

"""

# V0
# IDEA : SLIDING "GAP" + RUNNING MIN / MAX INDEX
#
#   same problem as LC 2903 but with n up to 10^5, so the O(n^2) brute force
#   is out and we need the single pass.
#
#   walk the right end i from indexDifference to n - 1. every index
#   j <= i - indexDifference is "far enough" from i, so keep only the two
#   candidates that can ever matter against a fixed nums[i] :
#     mi = index of the min value seen among those j
#     mx = index of the max value seen among those j
#   then nums[i] - nums[mi] and nums[mx] - nums[i] are the two largest
#   achievable differences, and if neither reaches valueDifference no pair
#   ending at i can.
#
#   NOTE : mi / mx must be updated with j = i - indexDifference BEFORE the
#          checks, otherwise the newly eligible index is missed (this is also
#          what makes i == j work when indexDifference == 0).
#
# time = O(n), space = O(1)
class Solution(object):
    def findIndices(self, nums, indexDifference, valueDifference):
        n = len(nums)
        mi = mx = 0
        for i in range(indexDifference, n):
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
