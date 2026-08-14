"""

2604. Minimum Time to Eat All Grains
Hard

There are n hens and m grains on a line. You are given the initial positions of the hens and the grains in two integer arrays hens and grains of size n and m respectively.

Any hen can eat a grain if they are on the same position. The time taken for this is negligible. One hen can also eat multiple grains.

In 1 second, a hen can move right or left by 1 unit. The hens can move simultaneously and independently of each other.

Return the minimum time to eat all grains if the hens act optimally.


Example 1:

Input: hens = [3,6,7], grains = [2,4,7,9]
Output: 2
Explanation:
One of the ways hens eat all grains in 2 seconds is described below:
- The first hen eats the grain at position 2 in 1 second.
- The second hen eats the grain at position 4 in 2 seconds.
- The third hen eats the grains at positions 7 and 9 in 2 seconds.
So, the maximum time needed is 2.
It can be proven that the hens cannot eat all grains before 2 seconds.

Example 2:

Input: hens = [4,6,109,111,213,215], grains = [5,110,214]
Output: 1
Explanation:
One of the ways hens eat all grains in 1 second is described below:
- The first hen eats the grain at position 5 in 1 second.
- The fourth hen eats the grain at position 110 in 1 second.
- The sixth hen eats the grain at position 214 in 1 second.
- The other hens do not move.
So, the maximum time needed is 1.


Constraints:

1 <= hens.length, grains.length <= 2*10^4
0 <= hens[i], grains[j] <= 10^9

"""

# V0
# IDEA : BINARY SEARCH ON THE ANSWER + GREEDY FEASIBILITY SWEEP
#
#   "can all grains be eaten within t seconds?" is monotone in t, so binary
#   search t and write a linear feasibility check.
#
#   sort both arrays. an optimal plan hands each hen a CONTIGUOUS block of
#   grains (hens never need to cross each other), so we sweep hens left to
#   right with a pointer j on the leftmost grain not yet assigned.
#
#   for hen at x with leftmost pending grain y = grains[j]:
#     - y <= x : the hen must first walk LEFT to y, costing d = x - y.
#                if d > t -> infeasible: every remaining hen is even further
#                right, so nobody can reach y. return False.
#                all grains in [y, x] come free on that walk -> skip them.
#                then for each grain z to the right of x, the hen may either
#                go left-then-right ( d + (z - y) ) or right-then-left
#                ( (z - x) + (z - y) ) -> cost = min(d, z - x) + (z - y).
#     - y > x  : the hen only ever walks right, cost to reach z is z - x.
#
#   NOTE : the "left first vs right first" min() is the crux — a hen covering
#          grains on both sides picks whichever end it doubles back from.
#   NOTE : upper bound: a single hen can do everything in
#          |hens[0] - grains[0]| + (grains[-1] - grains[0]) seconds.
#
# time = O((n + m) * log U + n log n + m log m), space = O(1) extra
class Solution(object):
    def minimumTime(self, hens, grains):
        hens = sorted(hens)
        grains = sorted(grains)
        m = len(grains)

        def can(t):
            j = 0
            for x in hens:
                if j == m:
                    return True
                y = grains[j]
                if y <= x:
                    d = x - y
                    if d > t:
                        return False
                    while j < m and grains[j] <= x:
                        j += 1
                    while j < m and min(d, grains[j] - x) + (grains[j] - y) <= t:
                        j += 1
                else:
                    while j < m and grains[j] - x <= t:
                        j += 1
            return j == m

        lo, hi = 0, abs(hens[0] - grains[0]) + grains[-1] - grains[0]
        while lo < hi:
            mid = (lo + hi) // 2
            if can(mid):
                hi = mid
            else:
                lo = mid + 1
        return lo
