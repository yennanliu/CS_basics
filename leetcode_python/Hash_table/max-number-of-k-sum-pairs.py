"""

1679. Max Number of K-Sum Pairs
Medium

You are given an integer array nums and an integer k.

In one operation, you can pick two numbers from the array whose sum equals k and remove them from
the array.

Return the maximum number of operations you can perform on the array.


Example 1:

Input: nums = [1,2,3,4], k = 5
Output: 2
Explanation: Starting with nums = [1,2,3,4]:
- Remove numbers 1 and 4, then nums = [2,3]
- Remove numbers 2 and 3, then nums = []
There are no more pairs that sum up to 5, hence a total of 2 operations.

Example 2:

Input: nums = [3,1,3,4,3], k = 6
Output: 1
Explanation: Starting with nums = [3,1,3,4,3]:
- Remove the first two 3's, then nums = [1,4,3]
There are no more pairs that sum up to 6, hence a total of 1 operation.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9
1 <= k <= 10^9

"""

# V0
# IDEA : HASH TABLE, ONE PASS (greedily close a pair the moment its partner exists)
#
#   keep a counter of "values still waiting for a partner".
#   for each v : if k - v is waiting, consume it and score a pair;
#                otherwise park v as waiting.
#
#   this is optimal because every v is interchangeable with any other copy of v,
#   so matching eagerly never blocks a better future pairing.
#
#   NOTE : handles v == k - v (e.g. k = 6, v = 3) correctly -- the first 3 parks,
#          the second 3 consumes it.
#
# time = O(n), space = O(n)
from collections import defaultdict
class Solution(object):
    def maxOperations(self, nums, k):
        waiting = defaultdict(int)
        res = 0
        for v in nums:
            need = k - v
            if waiting[need] > 0:
                waiting[need] -= 1
                res += 1
            else:
                waiting[v] += 1
        return res
