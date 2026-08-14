"""

1879. Minimum XOR Sum of Two Arrays
Hard

You are given two integer arrays nums1 and nums2 of length n.

The XOR sum of the two integer arrays is (nums1[0] XOR nums2[0]) + (nums1[1] XOR nums2[1]) + ... + (nums1[n - 1] XOR nums2[n - 1]) (0-indexed).

For example, the XOR sum of [1,2,3] and [3,2,1] is equal to (1 XOR 3) + (2 XOR 2) + (3 XOR 1) = 2 + 0 + 2 = 4.

Rearrange the elements of nums2 such that the resulting XOR sum is minimized.

Return the XOR sum after the rearrangement.


Example 1:

Input: nums1 = [1,2], nums2 = [2,3]
Output: 2
Explanation: Rearrange nums2 so that it becomes [3,2].
The XOR sum is (1 XOR 3) + (2 XOR 2) = 2 + 0 = 2.

Example 2:

Input: nums1 = [1,0,3], nums2 = [5,3,4]
Output: 8
Explanation: Rearrange nums2 so that it becomes [5,4,3].
The XOR sum is (1 XOR 5) + (0 XOR 4) + (3 XOR 3) = 4 + 4 + 0 = 8.


Constraints:

n == nums1.length
n == nums2.length
1 <= n <= 14
0 <= nums1[i], nums2[i] <= 10^7

"""

# V0
# IDEA : BITMASK DP (min-cost perfect matching on a tiny bipartite graph)
#
#   this is an assignment problem : match every nums1[i] to a distinct
#   nums2[k] minimising sum of (nums1[i] XOR nums2[k]). n <= 14 so the
#   Hungarian algorithm is overkill - a subset DP is enough.
#
#   dp[mask] = min cost after matching the FIRST popcount(mask) items of
#              nums1 against exactly the nums2 indices inside mask.
#   the number of used nums1 items is implied by the mask -> no 2nd index.
#
#   transition : i = popcount(mask) is the next nums1 element to place,
#     dp[mask | 1<<k] = min(dp[mask | 1<<k], dp[mask] + (nums1[i] ^ nums2[k]))
#   for every k not yet in mask.
#
# time = O(2^n * n), space = O(2^n)
class Solution(object):
    def minimumXORSum(self, nums1, nums2):
        n = len(nums1)
        FULL = 1 << n
        INF = float('inf')

        dp = [INF] * FULL
        dp[0] = 0

        for mask in range(FULL):
            if dp[mask] == INF:
                continue
            # how many nums1 elements are already placed
            i = bin(mask).count('1')
            if i == n:
                continue
            x = nums1[i]
            for k in range(n):
                if mask & (1 << k):
                    continue
                nxt = mask | (1 << k)
                cost = dp[mask] + (x ^ nums2[k])
                if cost < dp[nxt]:
                    dp[nxt] = cost

        return dp[FULL - 1]
