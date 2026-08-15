"""

3209. Number of Subarrays With AND Value of K
Hard

Given an array of integers nums and an integer k, return the number of subarrays of nums where the bitwise AND of the elements of the subarray equals k.


Example 1:

Input: nums = [1,1,1], k = 1
Output: 6
Explanation:
All subarrays contain only 1's.

Example 2:

Input: nums = [1,1,2], k = 1
Output: 3
Explanation:
Subarrays having an AND value of 1 are: [1,1,2], [1,1,2], [1,1,2].

Example 3:

Input: nums = [1,2,3], k = 2
Output: 2
Explanation:
Subarrays having an AND value of 2 are: [1,2,3], [1,2,3].


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i], k <= 10^9

"""

# V0
# IDEA : THE ANDs OF ALL SUBARRAYS ENDING AT i FORM AT MOST ~30 DISTINCT VALUES
#
#   extending a subarray leftwards can only CLEAR bits, so the ANDs of the
#   subarrays ending at index i form a chain that loses bits and never gains
#   any — at most 31 distinct values for 30-bit numbers.
#
#   so carry a small map {AND value -> how many subarrays ending here produce
#   it}. moving to the next element ANDs every key with nums[i] (merging the
#   collisions by adding counts) and adds the single-element subarray.
#
#   the answer accumulates the count stored under k at each step.
#
# time = O(30 * n), space = O(30)
from collections import defaultdict


class Solution(object):
    def countSubarrays(self, nums, k):
        res = 0
        prev = {}
        for x in nums:
            cur = defaultdict(int)
            cur[x] += 1
            for v, c in prev.items():
                cur[v & x] += c
            res += cur.get(k, 0)
            prev = cur
        return res
