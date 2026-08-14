"""

1995. Count Special Quadruplets
Easy

Given a 0-indexed integer array nums, return the number of distinct quadruplets (a, b, c, d) such that:

nums[a] + nums[b] + nums[c] == nums[d], and
a < b < c < d


Example 1:

Input: nums = [1,2,3,6]
Output: 1
Explanation: The only quadruplet that satisfies the requirement is (0, 1, 2, 3) because 1 + 2 + 3 == 6.

Example 2:

Input: nums = [3,3,6,4,5]
Output: 0
Explanation: There are no such quadruplets in [3,3,6,4,5].

Example 3:

Input: nums = [1,1,1,3,5]
Output: 4
Explanation: The 4 quadruplets that satisfy the requirement are:
- (0, 1, 2, 3): 1 + 1 + 1 == 3
- (0, 1, 3, 4): 1 + 1 + 3 == 5
- (0, 2, 3, 4): 1 + 1 + 3 == 5
- (1, 2, 3, 4): 1 + 1 + 3 == 5


Constraints:

4 <= nums.length <= 50
1 <= nums[i] <= 100

"""

# V0
# IDEA : HASH TABLE ON (nums[d] - nums[c])  -> O(n^2) instead of O(n^4)
#
#   rewrite the condition as
#       nums[a] + nums[b] == nums[d] - nums[c]
#   the left side depends only on (a, b), the right only on (c, d) - and the
#   two pairs are separated by b < c.
#
#   sweep b from right to left. before handling b, fold the pairs whose
#   c == b + 1 into a counter keyed by nums[d] - nums[c]; the counter then
#   holds every (c, d) with c > b. each a < b contributes
#   counter[nums[a] + nums[b]] quadruplets.
#
#   NOTE : the counter is never reset - that is exactly what accumulates all
#          c > b across the sweep.
#
# time = O(n^2), space = O(n^2) worst case for the counter keys
from collections import Counter
class Solution(object):
    def countQuadruplets(self, nums):
        n = len(nums)
        res = 0
        counter = Counter()
        for b in range(n - 3, 0, -1):
            c = b + 1
            for d in range(c + 1, n):
                counter[nums[d] - nums[c]] += 1
            for a in range(b):
                res += counter[nums[a] + nums[b]]
        return res
