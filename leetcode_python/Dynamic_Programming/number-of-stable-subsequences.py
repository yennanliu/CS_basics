"""

3686. Number of Stable Subsequences
Hard

You are given an integer array nums.

A subsequence is stable if it does not contain three consecutive elements
with the same parity when the subsequence is read in order (i.e.,
consecutive inside the subsequence).

Return the number of stable subsequences.

Since the answer may be too large, return it modulo 10^9 + 7.


Example 1:

Input: nums = [1,3,5]
Output: 6
Explanation:
Stable subsequences are [1], [3], [5], [1, 3], [1, 5], and [3, 5].
Subsequence [1, 3, 5] is not stable because it contains three consecutive
odd numbers. Thus, the answer is 6.

Example 2:

Input: nums = [2,3,4,2]
Output: 14
Explanation:
The only subsequence that is not stable is [2, 4, 2], which contains three
consecutive even numbers. Thus, the answer is 14.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : DP ON (LAST PARITY, LENGTH OF THE CURRENT PARITY RUN)
#
#   the ban is local: only the TAIL of a subsequence decides whether the
#   next element may be appended. and the tail only matters through two
#   facts -- the parity of the last element, and whether the last one or
#   last two elements share that parity. the actual values are irrelevant,
#   so four counters suffice:
#     f[p][1] = stable subsequences ending in a run of exactly one p
#     f[p][2] = ... a run of exactly two p's
#
#   processing nums left to right, an element of parity p can be appended to
#   anything ending in parity 1-p (restarting the run at length 1), or start
#   a brand-new subsequence, or extend a run of length 1 to length 2. it can
#   NEVER extend a run of length 2 -- that is precisely the forbidden third
#   consecutive same-parity element, so those transitions are simply absent
#   rather than subtracted off.
#
#   this counts each subsequence once by its element positions, so equal
#   values at different positions are correctly counted separately.
#
# time = O(n), space = O(1)
class Solution(object):
    def countStableSubsequences(self, nums):
        MOD = 10 ** 9 + 7
        # f[parity][run length - 1]
        f = [[0, 0], [0, 0]]
        for x in nums:
            p = x & 1
            q = 1 - p
            add1 = (f[q][0] + f[q][1] + 1) % MOD   # follow the other parity, or start fresh
            add2 = f[p][0]                         # lengthen a run of one into a run of two
            f[p][0] = (f[p][0] + add1) % MOD
            f[p][1] = (f[p][1] + add2) % MOD
        return (f[0][0] + f[0][1] + f[1][0] + f[1][1]) % MOD
