"""

2006. Count Number of Pairs With Absolute Difference K
Easy

Given an integer array nums and an integer k, return the number of pairs (i, j) where i < j such that |nums[i] - nums[j]| == k.

The value of |x| is defined as:

x if x >= 0.
-x if x < 0.


Example 1:

Input: nums = [1,2,2,1], k = 1
Output: 4
Explanation: The pairs with an absolute difference of 1 are:
- [1,2,2,1]
- [1,2,2,1]
- [1,2,2,1]
- [1,2,2,1]

Example 2:

Input: nums = [1,3], k = 3
Output: 0
Explanation: There are no pairs with an absolute difference of 3.

Example 3:

Input: nums = [3,2,1,5,4], k = 2
Output: 3
Explanation: The pairs with an absolute difference of 2 are:
- [3,2,1,5,4]
- [3,2,1,5,4]
- [3,2,1,5,4]


Constraints:

1 <= nums.length <= 200
1 <= nums[i] <= 100
1 <= k <= 99

"""

# V0
# IDEA : HASH TABLE (one pass, count partners already seen)
#
#   scan left to right keeping cnt[v] = how many times v appeared BEFORE i.
#   the current x pairs with every earlier (x - k) and every earlier (x + k),
#   which covers |nums[i] - nums[j]| == k for j < i exactly once.
#
#   NOTE : k >= 1, so x - k and x + k can never be the same bucket -> no
#          double counting.
#
# time = O(n), space = O(n)
from collections import Counter
class Solution(object):
    def countKDifference(self, nums, k):
        cnt = Counter()
        res = 0
        for x in nums:
            res += cnt[x - k] + cnt[x + k]
            cnt[x] += 1
        return res
