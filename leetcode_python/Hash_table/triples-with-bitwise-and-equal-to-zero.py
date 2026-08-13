"""

982. Triples with Bitwise AND Equal To Zero
Hard

Given an integer array nums, return the number of AND triples.

An AND triple is a triple of indices (i, j, k) such that:

0 <= i < nums.length
0 <= j < nums.length
0 <= k < nums.length
nums[i] & nums[j] & nums[k] == 0, where & represents the bitwise-AND operator.

Example 1:

Input: nums = [2,1,3]
Output: 12
Explanation: We could choose the following i, j, k triples:
(i=0, j=0, k=1) : 2 & 2 & 1
(i=0, j=1, k=0) : 2 & 1 & 2
(i=0, j=1, k=1) : 2 & 1 & 1
(i=0, j=1, k=2) : 2 & 1 & 3
(i=0, j=2, k=1) : 2 & 3 & 1
(i=1, j=0, k=0) : 1 & 2 & 2
(i=1, j=0, k=1) : 1 & 2 & 1
(i=1, j=0, k=2) : 1 & 2 & 3
(i=1, j=1, k=0) : 1 & 1 & 2
(i=1, j=2, k=0) : 1 & 3 & 2
(i=2, j=0, k=1) : 3 & 2 & 1
(i=2, j=1, k=0) : 3 & 1 & 2

Example 2:

Input: nums = [0,0,0]
Output: 27

Constraints:

1 <= nums.length <= 1000
0 <= nums[i] < 2^16

"""

# V0
# IDEA : HASH TABLE of PAIRWISE ANDs
#
#  - Brute force over (i, j, k) is O(n^3) = 10^9 -> too slow.
#  - But nums[i] < 2^16, so there are at most 65536 DISTINCT values of
#    (nums[i] & nums[j]). Bucket all n^2 pairs by their AND value first,
#    then for each nums[k] sum the buckets that AND to 0 with it.
#  - The triple is ordered (i, j, k all range independently), so no
#    de-duplication is needed.
#
# time = O(n^2 + n * 2^16)
# space = O(2^16)
from collections import Counter
class Solution(object):
    def countTriplets(self, nums):
        # cnt[v] = how many ordered pairs (i, j) have nums[i] & nums[j] == v
        cnt = Counter()
        for a in nums:
            for b in nums:
                cnt[a & b] += 1

        res = 0
        for c in nums:
            for v, times in cnt.items():
                if v & c == 0:
                    res += times
        return res
