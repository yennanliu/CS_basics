"""

1787. Make the XOR of All Segments Equal to Zero
Hard

You are given an array nums and an integer k. The XOR of a segment [left, right] where left <= right is the XOR of all the elements with indices between left and right, inclusive: nums[left] XOR nums[left+1] XOR ... XOR nums[right].

Return the minimum number of elements to change in the array such that the XOR of all segments of size k is equal to zero.

Example 1:

Input: nums = [1,2,0,3,0], k = 1
Output: 3
Explanation: Modify the array from [1,2,0,3,0] to from [0,0,0,0,0].

Example 2:

Input: nums = [3,4,5,2,1,7,3,4,7], k = 3
Output: 3
Explanation: Modify the array from [3,4,5,2,1,7,3,4,7] to [3,4,7,3,4,7,3,4,7].

Example 3:

Input: nums = [1,2,4,1,2,5,1,2,6], k = 3
Output: 3
Explanation: Modify the array from [1,2,4,1,2,5,1,2,6] to [1,2,3,1,2,3,1,2,3].

Constraints:

1 <= k <= nums.length <= 2000
0 <= nums[i] < 2^10

"""

# V0
# IDEA : DP OVER RESIDUE CLASSES i % k, STATE = RUNNING XOR (values < 2^10)
#
#   xor(nums[i..i+k-1]) == xor(nums[i+1..i+k]) forces nums[i] == nums[i+k],
#   so the array must become periodic with period k, and the xor of one whole
#   period must be 0. hence we only choose one value per residue class
#   0..k-1, and their xor has to be 0.
#   f[j] = min changes over the classes handled so far whose xor is j.
#     - set class i to a value v that already appears c times in it :
#         f'[j] = min(f'[j], f[j ^ v] + size[i] - c)
#     - set class i to a value that appears nowhere in it : any target xor is
#       reachable at cost size[i], so f'[j] = min(f) + size[i] for every j.
#   NOTE : the second case is what keeps the transition O(2^10 + distinct
#          values) instead of O(2^10 * 2^10).
#
"""

DP def
    xor(nums[i..i+k-1]) == xor(nums[i+1..i+k]) forces nums[i] == nums[i+k],
    so the array must become PERIODIC with period k, and the xor of one whole
    period must be 0. so we pick ONE value per residue class 0..k-1 and their
    xor must be 0.

    f[j]: MIN changes over the residue classes handled so far

          whose running xor is j       (values < 2^10)

DP eq

     for residue class i (size[i] = its length, cnt[i] = its value histogram):

        set class i to a value v that already appears c times in it:
            f_new[j] = min( f_new[j], f[j ^ v] + size[i] - c )

        set class i to a value appearing NOWHERE in it:
            any target xor is reachable at cost size[i]
            f_new[j] = min(f) + size[i]      for EVERY j


    -> e.g. that second case is what keeps each transition
              O(2^10 + distinct values) instead of O(2^10 * 2^10)

     init: f[0] = 0, rest inf
     ans = f[0] after all k classes

"""
# time = O(n * 2^10 + k * 2^10), space = O(n + 2^10)
from collections import Counter
class Solution(object):
    def minChanges(self, nums, k):
        M = 1 << 10
        cnt = [Counter() for _ in range(k)]
        size = [0] * k
        for i in range(len(nums)):
            cnt[i % k][nums[i]] += 1
            size[i % k] += 1

        INF = float("inf")
        f = [INF] * M
        f[0] = 0
        for i in range(k):
            base = min(f) + size[i]
            g = [base] * M
            for j in range(M):
                for v, c in cnt[i].items():
                    prev = f[j ^ v]
                    if prev + size[i] - c < g[j]:
                        g[j] = prev + size[i] - c
            f = g
        return f[0]
