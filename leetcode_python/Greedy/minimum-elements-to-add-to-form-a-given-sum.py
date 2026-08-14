"""

1785. Minimum Elements to Add to Form a Given Sum
Medium

You are given an integer array nums and two integers limit and goal. The array nums has an interesting property that abs(nums[i]) <= limit.

Return the minimum number of elements you need to add to make the sum of the array equal to goal. The array must maintain its property that abs(nums[i]) <= limit.

Note that abs(x) equals x if x >= 0, and -x otherwise.

Example 1:

Input: nums = [1,-1,1], limit = 3, goal = -4
Output: 2
Explanation: You can add -2 and -3, then the sum of the array will be 1 - 1 + 1 - 2 - 3 = -4.

Example 2:

Input: nums = [1,-10,9,1], limit = 100, goal = 0
Output: 1

Constraints:

1 <= nums.length <= 10^5
1 <= limit <= 10^6
-limit <= nums[i] <= limit
-10^9 <= goal <= 10^9

"""

# V0
# IDEA : GREEDY (every added element moves the sum by at most `limit`)
#
#   let d = abs(sum(nums) - goal). each new element contributes a value in
#   [-limit, limit], so it closes at most `limit` of the gap, and we can always
#   pick values that close exactly `limit` until the leftover (< limit) which
#   one last element covers -> ceil(d / limit).
#
# time = O(n), space = O(1)
class Solution(object):
    def minElements(self, nums, limit, goal):
        d = abs(sum(nums) - goal)
        return (d + limit - 1) // limit
