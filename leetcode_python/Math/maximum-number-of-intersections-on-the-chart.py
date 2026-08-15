"""

3009. Maximum Number of Intersections on the Chart
Hard
🔒 (premium)

There is a line chart consisting of n points connected by line segments. You are given a 1-indexed integer array y. The kth point has coordinates (k, y[k]). There are no horizontal lines; that is, no two consecutive points have the same y-coordinate.

We can draw an infinitely long horizontal line. Return the maximum number of points of intersection of the line with the chart.


Example 1:

Input: y = [1,2,1,2,1,3,2]
Output: 5
Explanation: As you can see in the image above, the line y = 1.5 has 5 intersections with the chart (in red crosses).
We can also see that the line y = 2 would have 5 intersections as well.
It can be shown that there is no horizontal line with more than 5 intersections.


Constraints:

2 <= y.length <= 10^5
1 <= y[i] <= 10^9
y[i] != y[i + 1] for i in range [1, n - 1]

"""

# V0
# IDEA : EACH SEGMENT IS AN INTERVAL OF y-VALUES — SWEEP FOR THE MAX OVERLAP
#
#   the horizontal line y = t meets the segment from (k, y[k]) to
#   (k+1, y[k+1]) exactly when t lies between the two endpoints. so every
#   segment becomes the interval [min, max] and the answer is the largest
#   number of intervals covering a single value.
#
#   two details make it exact :
#
#   1. DOUBLE every coordinate. the best line often sits at a half-integer
#      (y = 1.5 above); doubling makes those integers so a plain difference
#      array works.
#
#   2. consecutive segments SHARE a vertex, and a line through that vertex
#      touches the chart once, not twice. so for every segment after the
#      first, drop its starting vertex from its interval — shrink whichever
#      end that vertex sits at.
#
#   then a difference array over the interval endpoints, swept in sorted
#   order, gives the maximum overlap.
#
# time = O(n log n), space = O(n)
from collections import defaultdict


class Solution(object):
    def maxIntersectionCount(self, y):
        vals = [2 * v for v in y]
        diff = defaultdict(int)

        for i in range(len(vals) - 1):
            lo, hi = vals[i], vals[i + 1]
            if lo > hi:
                lo, hi = hi, lo
            if i > 0:
                # the vertex at vals[i] already belongs to the previous segment
                if vals[i] == lo:
                    lo += 1
                else:
                    hi -= 1
            diff[lo] += 1
            diff[hi + 1] -= 1

        best = cur = 0
        for key in sorted(diff):
            cur += diff[key]
            best = max(best, cur)
        return best
