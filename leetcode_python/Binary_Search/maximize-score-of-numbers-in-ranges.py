"""

3281. Maximize Score of Numbers in Ranges
Medium

You are given an array of integers start and an integer d, representing n intervals [start[i], start[i] + d].

You are asked to choose n integers where the ith integer must belong to the ith interval. The score of the chosen integers is defined as the minimum absolute difference between any two integers that have been chosen.

Return the maximum possible score of the chosen integers.


Example 1:

Input: start = [6,0,3], d = 2
Output: 4
Explanation:
The maximum possible score can be obtained by choosing integers: 8, 0, and 4. The score of these chosen integers is min(|8 - 0|, |8 - 4|, |0 - 4|) which equals 4.

Example 2:

Input: start = [2,6,13,13], d = 5
Output: 5
Explanation:
The maximum possible score can be obtained by choosing integers: 2, 7, 13, and 18. The score of these chosen integers is min(|2 - 7|, |2 - 13|, |2 - 18|, |7 - 13|, |7 - 18|, |13 - 18|) which equals 5.


Constraints:

2 <= start.length <= 10^5
0 <= start[i] <= 10^9
0 <= d <= 10^9

"""

# V0
# IDEA : BINARY SEARCH THE GAP, PLACE GREEDILY LEFT TO RIGHT
#
#   "every pair at least g apart" gets harder as g grows, so feasibility is
#   monotone and binary search applies.
#
#   to test a gap g, sort the intervals by their start and walk them, each
#   time picking the SMALLEST legal value :
#       pick = max(start[i], previous + g)
#   and failing if that overshoots start[i] + d. taking the smallest value
#   leaves the most room for everything after it, so the greedy is exact.
#
#   the answer can reach (max start + d) - min start, which bounds the
#   search.
#
# time = O(n log n + n log(range)), space = O(n)
class Solution(object):
    def maxPossibleScore(self, start, d):
        pts = sorted(start)
        n = len(pts)

        def feasible(g):
            prev = pts[0]
            for i in range(1, n):
                cand = max(pts[i], prev + g)
                if cand > pts[i] + d:
                    return False
                prev = cand
            return True

        lo, hi = 0, (pts[-1] + d) - pts[0]
        while lo < hi:
            mid = (lo + hi + 1) // 2
            if feasible(mid):
                lo = mid
            else:
                hi = mid - 1
        return lo
