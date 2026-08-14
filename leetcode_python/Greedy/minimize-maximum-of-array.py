"""

2439. Minimize Maximum of Array
Medium

You are given a 0-indexed array nums comprising of n non-negative integers.

In one operation, you must:

Choose an integer i such that 1 <= i < n and nums[i] > 0.
Decrease nums[i] by 1.
Increase nums[i - 1] by 1.

Return the minimum possible value of the maximum integer of nums after performing any number of operations.


Example 1:

Input: nums = [3,7,1,6]
Output: 5
Explanation:
One set of optimal operations is as follows:
1. Choose i = 1, and nums becomes [4,6,1,6].
2. Choose i = 3, and nums becomes [4,6,2,5].
3. Choose i = 1, and nums becomes [5,5,2,5].
The maximum integer of nums is 5. It can be shown that the maximum number cannot be less than 5.
Therefore, we return 5.

Example 2:

Input: nums = [10,1]
Output: 10
Explanation:
It is optimal to leave nums as is, and since 10 is the maximum value, we return 10.


Constraints:

n == nums.length
2 <= n <= 10^5
0 <= nums[i] <= 10^9

"""

# V0
# IDEA : GREEDY / PREFIX AVERAGE (value can only ever move LEFT)
#
#   an operation moves 1 unit from i to i-1, so mass can only flow leftwards.
#   therefore for every prefix [0..i] the total sum(0..i) is trapped inside
#   that prefix (extra mass may flow IN from the right, never out), and the
#   max of the prefix is at least ceil(sum(0..i) / (i + 1)).
#
#   that bound is achievable simultaneously for all prefixes (sweep left to
#   right, push down whatever exceeds the running average), so
#     answer = max over i of ceil(prefix_sum(i) / (i + 1))
#
# time = O(n), space = O(1)
class Solution(object):
    def minimizeArrayValue(self, nums):
        res = 0
        total = 0
        for i, x in enumerate(nums):
            total += x
            # ceil(total / (i + 1))
            need = (total + i) // (i + 1)
            if need > res:
                res = need
        return res
