"""

2274. Maximum Consecutive Floors Without Special Floors
Medium

Alice manages a company and has rented some floors of a building as office space. Alice has decided some of these floors should be special floors, used for relaxation only.

You are given two integers bottom and top, which denote that Alice has rented all the floors from bottom to top (inclusive). You are also given the integer array special, where special[i] denotes a special floor that Alice has designated for relaxation.

Return the maximum number of consecutive floors without a special floor.


Example 1:

Input: bottom = 2, top = 9, special = [4,6]
Output: 3
Explanation: The following are the ranges (inclusive) of consecutive floors without a special floor:
- (2, 3) with a total amount of 2 floors.
- (5, 5) with a total amount of 1 floor.
- (7, 9) with a total amount of 3 floors.
Therefore, we return the maximum number which is 3 floors.

Example 2:

Input: bottom = 6, top = 8, special = [7,6,8]
Output: 0
Explanation: Every floor rented is a special floor, so we return 0.


Constraints:

1 <= special.length <= 10^5
1 <= bottom <= special[i] <= top <= 10^9
All the values of special are unique.

"""

# V0
# IDEA : SORT THE SPECIAL FLOORS AND MEASURE THE GAPS BETWEEN THEM
#
#   after sorting, three kinds of gap exist :
#       below the first special floor : special[0] - bottom
#       between two consecutive ones  : special[i] - special[i-1] - 1
#       above the last one            : top - special[-1]
#
#   the answer is the largest of those. all three formulas already yield 0
#   when the floors are adjacent, so no special-casing is needed.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def maxConsecutive(self, bottom, top, special):
        special = sorted(special)
        res = max(special[0] - bottom, top - special[-1])
        for i in range(1, len(special)):
            res = max(res, special[i] - special[i - 1] - 1)
        return res


# V0-1
# IDEA : MIN-HEAP, CONSUMING THE SPECIAL FLOORS IN INCREASING ORDER
#
#   the gaps only need the special floors visited in ascending order - a total
#   sort is more than required, so heapify + repeated pop streams them out
#   instead. two sentinel tricks remove the edge cases V0 handles by hand :
#
#       prev starts at bottom - 1  -> the first gap is cur - (bottom-1) - 1
#       after the loop, top acts as the closing wall -> top - prev
#
#   heapify is O(n) and the n pops are O(log n) each, so the asymptotics match
#   sorting, but nothing is ever fully ordered in memory and the loop could
#   stop early if only the first few gaps mattered.
#
# time = O(n log n)
# space = O(n) for the heap
import heapq
class Solution(object):
    def maxConsecutive(self, bottom, top, special):
        heap = list(special)
        heapq.heapify(heap)

        res = 0
        prev = bottom - 1
        while heap:
            cur = heapq.heappop(heap)
            res = max(res, cur - prev - 1)
            prev = cur

        return max(res, top - prev)


# V0-2
# IDEA : PIGEONHOLE BUCKETS -> MAXIMUM GAP IN LINEAR TIME (LC 164 TRICK)
#
#   add the two virtual walls bottom-1 and top+1 to the special floors; the
#   answer is then simply (max gap between consecutive points) - 1.
#
#   sorting is not needed to find a MAX gap. spread the m points over m-1
#   buckets of equal width w = span / (m - 1). by the pigeonhole principle the
#   maximum gap is at least w, so it can never sit strictly inside one bucket
#   - it must run from some bucket's max to the next non-empty bucket's min.
#   so storing only (min, max) per bucket and scanning the buckets once is
#   enough, and the points inside a bucket never have to be ordered.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def maxConsecutive(self, bottom, top, special):
        pts = list(special) + [bottom - 1, top + 1]
        m = len(pts)
        lo, hi = min(pts), max(pts)
        if lo == hi:
            return 0

        width = max(1, (hi - lo) // (m - 1))
        nb = (hi - lo) // width + 1
        bmin = [None] * nb
        bmax = [None] * nb
        for p in pts:
            b = (p - lo) // width
            if bmin[b] is None or p < bmin[b]:
                bmin[b] = p
            if bmax[b] is None or p > bmax[b]:
                bmax[b] = p

        res = 0
        prev = None
        for b in range(nb):
            if bmin[b] is None:
                continue
            if prev is not None:
                res = max(res, bmin[b] - prev - 1)
            prev = bmax[b]

        return res
