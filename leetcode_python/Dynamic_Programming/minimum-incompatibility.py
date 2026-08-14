"""

1681. Minimum Incompatibility
Hard

You are given an integer array nums and an integer k. You are asked to distribute this array into k
subsets of equal size such that there are no two equal elements in the same subset.

A subset's incompatibility is the difference between the maximum and minimum elements in that array.

Return the minimum possible sum of incompatibilities of the k subsets after distributing the array
optimally, or return -1 if it is not possible.

A subset is a group integers that appear in the array with no particular order.


Example 1:

Input: nums = [1,2,1,4], k = 2
Output: 4
Explanation: The optimal distribution of subsets is [1,2] and [1,4].
The incompatibility is (2-1) + (4-1) = 4.
Note that [1,1] and [2,4] would result in a smaller sum, but the first subset contains 2 equal
elements.

Example 2:

Input: nums = [6,3,8,1,3,1,2,2], k = 4
Output: 6
Explanation: The optimal distribution of subsets is [1,2], [2,3], [6,8], and [1,3].
The incompatibility is (2-1) + (3-2) + (8-6) + (3-1) = 6.

Example 3:

Input: nums = [5,3,3,6,3,3], k = 3
Output: -1
Explanation: It is impossible to distribute nums into 3 subsets where no two elements are equal in
the same subset.


Constraints:

1 <= k <= nums.length <= 16
nums.length is divisible by k
1 <= nums[i] <= nums.length

"""

# V0
# IDEA : BITMASK DP OVER SUBSETS (n <= 16 -> partition into groups of size m = n/k)
#
#   step 1 : precompute cost[g] for EVERY bitmask g that is a legal group,
#            i.e. popcount(g) == m and all its values are distinct;
#            cost[g] = max(g) - min(g). illegal groups get -1.
#
#   step 2 : dp[mask] = min total incompatibility to cover exactly the elements
#            in `mask` with complete groups. transition = pick one more group
#            out of the free positions.
#
#   two prunings that keep this fast:
#     (a) CANONICAL START -- the lowest free position must belong to the group we
#         are about to build, so we force that bit in and only enumerate submasks
#         of the remaining free positions. this kills the k! ordering blowup.
#     (b) DEDUPE BY VALUE -- among free positions holding the SAME value, only the
#         earliest may be used; equal values are interchangeable, so the rest are
#         redundant branches.
#
#   NOTE : if some value occurs more than k times, two copies must share a group
#          -> impossible, return -1 early.
#
# time = O(2^n * n) for the cost table + subset-sum DP, space = O(2^n)
from collections import Counter
class Solution(object):
    def minimumIncompatibility(self, nums, k):
        n = len(nums)
        m = n // k

        cnt = Counter(nums)
        if max(cnt.values()) > k:
            return -1

        INF = float("inf")
        full = 1 << n

        # ---- step 1 : legal groups + their cost
        cost = [-1] * full
        for g in range(1, full):
            if bin(g).count("1") != m:
                continue
            seen = set()
            lo, hi = INF, 0
            ok = True
            for j in range(n):
                if g >> j & 1:
                    v = nums[j]
                    if v in seen:
                        ok = False
                        break
                    seen.add(v)
                    lo = min(lo, v)
                    hi = max(hi, v)
            if ok:
                cost[g] = hi - lo

        # ---- step 2 : subset DP
        dp = [INF] * full
        dp[0] = 0
        for mask in range(full):
            if dp[mask] == INF:
                continue
            if mask == full - 1:
                continue

            # free positions, keeping only the first copy of each value
            cand = 0
            seen = set()
            for j in range(n):
                if not (mask >> j & 1) and nums[j] not in seen:
                    seen.add(nums[j])
                    cand |= 1 << j
            if bin(cand).count("1") < m:
                continue

            low = cand & (-cand)        # the lowest usable free position
            rest = cand ^ low
            sub = rest
            while True:
                g = sub | low
                if cost[g] != -1:
                    nxt = mask | g
                    if dp[mask] + cost[g] < dp[nxt]:
                        dp[nxt] = dp[mask] + cost[g]
                if sub == 0:
                    break
                sub = (sub - 1) & rest

        return dp[full - 1] if dp[full - 1] != INF else -1
