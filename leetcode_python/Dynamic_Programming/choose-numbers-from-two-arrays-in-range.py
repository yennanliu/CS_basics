"""

2143. Choose Numbers From Two Arrays in Range
Hard
(premium / locked problem)

You are given two 0-indexed integer arrays nums1 and nums2 of length n.

A range [l, r] (inclusive) where 0 <= l <= r < n is balanced if:

For every i in the range [l, r], you pick either nums1[i] or nums2[i].
The sum of the numbers you pick from nums1 equals to the sum of the numbers you pick from nums2 (the sum is considered to be 0 if you pick no numbers from an array).

Two balanced ranges from [l1, r1] and [l2, r2] are considered to be different if at least one of the following is true:

l1 != l2
r1 != r2
nums1[i] is picked in the first range, and nums2[i] is picked in the second range.

Return the number of different ranges that are balanced. Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: nums1 = [1,2,5], nums2 = [2,6,3]
Output: 3
Explanation: The balanced ranges are:
- [0, 1] where we choose nums2[0], and nums1[1].
  The sum of the numbers chosen from nums1 equals the sum of the numbers chosen from nums2: 2 = 2.
- [0, 2] where we choose nums1[0], nums2[1], and nums1[2].
  The sum of the numbers chosen from nums1 equals the sum of the numbers chosen from nums2: 1 + 5 = 6.
- [0, 2] where we choose nums1[0], nums1[1], and nums2[2].
  The sum of the numbers chosen from nums1 equals the sum of the numbers chosen from nums2: 1 + 2 = 3.
Note that the second and third balanced ranges are different.
In the second balanced range, we choose nums2[1] and in the third balanced range, we choose nums1[1].

Example 2:

Input: nums1 = [0,1], nums2 = [1,0]
Output: 4
Explanation: The balanced ranges are:
- [0, 0] where we choose nums1[0].
  The sum of the numbers chosen from nums1 equals the sum of the numbers chosen from nums2: 0 = 0.
- [1, 1] where we choose nums2[1].
  The sum of the numbers chosen from nums1 equals the sum of the numbers chosen from nums2: 0 = 0.
- [0, 1] where we choose nums1[0] and nums2[1].
  The sum of the numbers chosen from nums1 equals the sum of the numbers chosen from nums2: 0 = 0.
- [0, 1] where we choose nums2[0] and nums1[1].
  The sum of the numbers chosen from nums1 equals the sum of the numbers chosen from nums2: 1 = 1.


Constraints:

n == nums1.length == nums2.length
1 <= n <= 100
0 <= nums1[i], nums2[i] <= 100

"""

# V0
# IDEA : ROLLING DP OVER THE RUNNING DIFFERENCE, ALL STARTS AT ONCE
#
#   track  d = (sum taken from nums1) - (sum taken from nums2)  for every
#   range that is still "open" at index i :
#
#       dp[d] = how many (start, choice-pattern) combinations reach difference d
#
#   at index i every open range must pick one side, so each state branches
#       d -> d + nums1[i]      (pick from nums1)
#       d -> d - nums2[i]      (pick from nums2)
#   and a brand-new range starting AT i is seeded with the same two options.
#
#   after processing index i, dp[0] counts exactly the balanced ranges ending
#   at i — add it to the answer. summing over i covers every (l, r).
#
#   NOTE : "different" counts distinct CHOICE PATTERNS too, which is why the
#          dp stores counts rather than a reachability set. that also means
#          zero-valued entries genuinely create separate options (see
#          example 2).
#
# time = O(n * S) with S the span of possible differences, space = O(S)
from collections import defaultdict


class Solution(object):
    def countSubranges(self, nums1, nums2):
        MOD = 10 ** 9 + 7
        dp = defaultdict(int)
        res = 0
        for a, b in zip(nums1, nums2):
            nxt = defaultdict(int)
            for d, c in dp.items():
                nxt[d + a] = (nxt[d + a] + c) % MOD
                nxt[d - b] = (nxt[d - b] + c) % MOD
            # a new range starting right here
            nxt[a] = (nxt[a] + 1) % MOD
            nxt[-b] = (nxt[-b] + 1) % MOD
            dp = nxt
            res = (res + dp[0]) % MOD
        return res
