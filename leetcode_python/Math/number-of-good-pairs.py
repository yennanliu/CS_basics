"""

1512. Number of Good Pairs
Easy

Given an array of integers nums, return the number of good pairs.

A pair (i, j) is called good if nums[i] == nums[j] and i < j.


Example 1:

Input: nums = [1,2,3,1,1,3]
Output: 4
Explanation: There are 4 good pairs (0,3), (0,4), (3,4), (2,5) 0-indexed.

Example 2:

Input: nums = [1,1,1,1]
Output: 6
Explanation: Each pair in the array are good.

Example 3:

Input: nums = [1,2,3]
Output: 0


Constraints:

1 <= nums.length <= 100
1 <= nums[i] <= 100

"""

# V0
# IDEA : RUNNING COUNTER
#
#   scan left to right. when we land on value x, every earlier occurrence
#   of x pairs with the current index (and i < j holds automatically),
#   so add cnt[x] to the answer, then bump cnt[x].
#
#   equivalent closed form : sum over values of C(freq, 2).
#
# time = O(n), space = O(C), C = number of distinct values
from collections import Counter
class Solution(object):
    def numIdenticalPairs(self, nums):
        cnt = Counter()
        res = 0
        for x in nums:
            res += cnt[x]
            cnt[x] += 1
        return res
