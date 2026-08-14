"""

2597. The Number of Beautiful Subsets
Medium

You are given an array nums of positive integers and a positive integer k.

A subset of nums is beautiful if it does not contain two integers with an absolute difference equal to k.

Return the number of non-empty beautiful subsets of the array nums.

A subset of nums is an array that can be obtained by deleting some (possibly none) elements from nums. Two subsets are different if and only if the chosen indices to delete are different.


Example 1:

Input: nums = [2,4,6], k = 2
Output: 4
Explanation: The beautiful subsets of the array nums are: [2], [4], [6], [2, 6].
It can be proved that there are only 4 beautiful subsets in the array [2,4,6].

Example 2:

Input: nums = [1], k = 1
Output: 1
Explanation: The beautiful subset of the array nums is [1].
It can be proved that there is only 1 beautiful subset in the array [1].


Constraints:

1 <= nums.length <= 18
1 <= nums[i], k <= 1000

"""

# V0
# IDEA : GROUP BY (value % k) -> HOUSE-ROBBER DP ON EACH CHAIN
#
#   two values conflict only when they differ by EXACTLY k, so they must share
#   the same remainder mod k. Different remainder classes are fully independent
#   -> the total is the PRODUCT over classes, and we handle each class alone.
#
#   inside one class, sort the DISTINCT values. Consecutive values conflict
#   only when their gap is exactly k, so the class splits into maximal chains
#     v, v+k, v+2k, ...
#   and picking within a chain is "house robber": no two adjacent links.
#
#   a link with multiplicity c can be taken in (2^c - 1) non-empty ways (the
#   duplicates are distinguishable indices), or skipped.
#     skip = ways where the current link is NOT taken
#     take = ways where it IS taken = (previous skip) * (2^c - 1)
#   chain total = skip + take  (this counts the empty selection once).
#
#   NOTE : multiply every chain total together, then subtract 1 at the very end
#          to drop the single globally-empty subset -- subtracting per chain
#          would be wrong.
#
# time = O(n log n), space = O(n)
from collections import defaultdict
class Solution(object):
    def beautifulSubsets(self, nums, k):
        # remainder class -> {value: count}
        groups = defaultdict(lambda: defaultdict(int))
        for x in nums:
            groups[x % k][x] += 1

        res = 1
        for cnt in groups.values():
            vals = sorted(cnt)
            skip, take = 1, 0   # running house-robber state for the chain
            prev = None
            for v in vals:
                if prev is not None and v - prev != k:
                    # chain broke -> fold it in and restart
                    res *= (skip + take)
                    skip, take = 1, 0
                new_take = skip * ((1 << cnt[v]) - 1)
                skip = skip + take
                take = new_take
                prev = v
            res *= (skip + take)
        return res - 1
