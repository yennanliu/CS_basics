"""

2106. Maximum Fruits Harvested After at Most K Steps
Hard

Fruits are available at some positions on an infinite x-axis. You are given a 2D integer array fruits where fruits[i] = [position_i, amount_i] depicts amount_i fruits at the position position_i. fruits is already sorted by position_i in ascending order, and each position_i is unique.

You are also given an integer startPos and an integer k. Initially, you are at the position startPos. From any position, you can either walk to the left or right. It takes one step to move one unit on the x-axis, and you can walk at most k steps in total. For every position you reach, you harvest all the fruits at that position, and the fruits will disappear from that position.

Return the maximum total number of fruits you can harvest.


Example 1:

Input: fruits = [[2,8],[6,3],[8,6]], startPos = 5, k = 4
Output: 9
Explanation:
The optimal way is to:
- Move right to position 6 and harvest 3 fruits
- Move right to position 8 and harvest 6 fruits
You moved 3 steps and harvested 3 + 6 = 9 fruits in total.

Example 2:

Input: fruits = [[0,9],[4,1],[5,7],[6,2],[7,4],[10,9]], startPos = 5, k = 4
Output: 14
Explanation:
You can move at most k = 4 steps, so you cannot go to position 0 nor 10.
The optimal way is to:
- Harvest the 7 fruits at the starting position 5
- Move left to position 4 and harvest 1 fruit
- Move right to position 6 and harvest 2 fruits
- Move right to position 7 and harvest 4 fruits
You moved 1 + 3 = 4 steps and harvested 7 + 1 + 2 + 4 = 14 fruits in total.

Example 3:

Input: fruits = [[0,3],[6,4],[8,5]], startPos = 3, k = 2
Output: 0
Explanation:
You can move at most k = 2 steps and cannot reach any position with fruits.


Constraints:

1 <= fruits.length <= 10^5
fruits[i].length == 2
0 <= startPos, position_i <= 2 * 10^5
position_i - 1 < position_i (0-indexed)
1 <= amount_i <= 10^4
0 <= k <= 2 * 10^5

"""

# V0
# IDEA : SLIDING WINDOW OVER THE SORTED POSITIONS + PREFIX SUMS
#
#   whatever you do, the harvested positions form a CONTIGUOUS interval
#   [L, R] containing... well, reachable from startPos. the cheapest walk
#   covering [L, R] turns around at most once, costing
#       min( 2*(start - L) + (R - start),
#            (start - L) + 2*(R - start) )
#   with the negative side clamped to 0 when the interval is entirely on one
#   side of start.
#
#   so slide a window [i, j] over the (sorted) fruit positions, shrinking
#   from the left while the walk cost exceeds k, and track the best prefix-sum
#   window total.
#
# time = O(n), space = O(n)
class Solution(object):
    def maxTotalFruits(self, fruits, startPos, k):
        n = len(fruits)
        prefix = [0] * (n + 1)
        for i, (_, amt) in enumerate(fruits):
            prefix[i + 1] = prefix[i] + amt

        def cost(left_pos, right_pos):
            # cheapest walk from startPos covering [left_pos, right_pos]
            left = max(0, startPos - left_pos)
            right = max(0, right_pos - startPos)
            return min(2 * left + right, left + 2 * right)

        res = 0
        i = 0
        for j in range(n):
            while i <= j and cost(fruits[i][0], fruits[j][0]) > k:
                i += 1
            if i <= j:
                res = max(res, prefix[j + 1] - prefix[i])
        return res


# V0-1
# IDEA : ENUMERATE THE BUDGET SPLIT (how far to walk against the turn) + BISECT
#
#   an optimal walk turns around AT MOST once, so it is fully described by one
#   number : how many steps d are spent on the "wrong" side before turning.
#   walking d left first then turning right leaves k - 2d steps for the right
#   side, so the harvested interval is
#
#       [ startPos - d , startPos + (k - 2d) ]
#
#   and the mirror case (right first) is
#
#       [ startPos - (k - 2d) , startPos + d ]
#
#   d only needs to run over 0 .. k // 2 (beyond that the far side is empty and
#   the mirror case already covers it). since `fruits` is sorted by position,
#   the amount inside an interval is one prefix-sum difference located by two
#   binary searches - no sliding pointer at all.
#
# time = O(k log n)
# space = O(n)
import bisect
class Solution(object):
    def maxTotalFruits(self, fruits, startPos, k):
        pos = [p for p, _ in fruits]
        prefix = [0] * (len(fruits) + 1)
        for i, (_, amt) in enumerate(fruits):
            prefix[i + 1] = prefix[i] + amt

        def total(lo, hi):
            a = bisect.bisect_left(pos, lo)
            b = bisect.bisect_right(pos, hi)
            return prefix[b] - prefix[a]

        res = 0
        for d in range(k // 2 + 1):
            rest = k - 2 * d
            # left d steps, then turn right
            res = max(res, total(startPos - d, startPos + rest))
            # right d steps, then turn left
            res = max(res, total(startPos - rest, startPos + d))
        return res


# V0-2
# IDEA : FIX THE LEFT END, BINARY SEARCH THE FURTHEST FEASIBLE RIGHT END
#
#   for a FIXED left index i, the walk cost min(2L + R, L + 2R) is monotone
#   non-decreasing as the right index j grows (R only grows). monotone predicate
#   -> binary search instead of the amortised two-pointer of V0.
#
#   and the best window starting at i is always the widest feasible one, because
#   amounts are strictly positive - so one binary search per i gives the answer,
#   with no invariant to maintain between iterations. slower than V0 by a log
#   factor, but each i is independent, which is what you want if the queries
#   (different startPos / k) arrive one at a time.
#
# time = O(n log n)
# space = O(n)
class Solution(object):
    def maxTotalFruits(self, fruits, startPos, k):
        n = len(fruits)
        pos = [p for p, _ in fruits]
        prefix = [0] * (n + 1)
        for i, (_, amt) in enumerate(fruits):
            prefix[i + 1] = prefix[i] + amt

        def cost(i, j):
            left = max(0, startPos - pos[i])
            right = max(0, pos[j] - startPos)
            return min(2 * left + right, left + 2 * right)

        res = 0
        for i in range(n):
            if cost(i, i) > k:
                continue
            lo, hi = i, n - 1
            while lo < hi:
                mid = (lo + hi + 1) // 2
                if cost(i, mid) <= k:
                    lo = mid
                else:
                    hi = mid - 1
            res = max(res, prefix[lo + 1] - prefix[i])
        return res
