"""

2219. Maximum Sum Score of Array
Medium

You are given a 0-indexed integer array nums of length n.

The sum score of nums at an index i where 0 <= i < n is the maximum of:

The sum of the first i + 1 elements of nums.
The sum of the last n - i elements of nums.

Return the maximum sum score of nums at any index.


Example 1:

Input: nums = [4,3,-2,5]
Output: 10
Explanation:
The sum score at index 0 is max(4, 4 + 3 + -2 + 5) = max(4, 10) = 10.
The sum score at index 1 is max(4 + 3, 3 + -2 + 5) = max(7, 6) = 7.
The sum score at index 2 is max(4 + 3 + -2, -2 + 5) = max(5, 3) = 5.
The sum score at index 3 is max(4 + 3 + -2 + 5, 5) = max(10, 5) = 10.
The maximum sum score of nums is 10.

Example 2:

Input: nums = [-3,-5]
Output: -3
Explanation:
The sum score at index 0 is max(-3, -3 + -5) = max(-3, -8) = -3.
The sum score at index 1 is max(-3 + -5, -5) = max(-8, -5) = -5.
The maximum sum score of nums is -3.


Constraints:

n == nums.length
1 <= n <= 10^5
-10^5 <= nums[i] <= 10^5

"""

# V0
# IDEA : PREFIX / SUFFIX SUM IN ONE PASS
#
#   at index i we need prefix[0..i] and suffix[i..n-1].
#   keep a running prefix `l` and a running suffix `r` (starts at total sum).
#   at each i : l += nums[i]  -> l is prefix[0..i]
#               r            -> still suffix[i..n-1]  (subtract AFTER using it)
#   answer = max over all i of max(l, r).
#
# time = O(n), space = O(1)
class Solution(object):
    def maximumSumScore(self, nums):
        l = 0
        r = sum(nums)
        res = None
        for x in nums:
            l += x
            cur = l if l > r else r
            if res is None or cur > res:
                res = cur
            r -= x
        return res
