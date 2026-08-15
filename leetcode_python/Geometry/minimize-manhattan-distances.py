"""

3102. Minimize Manhattan Distances
Hard

You are given an array points representing integer coordinates of some points on a 2D plane, where points[i] = [xi, yi].

The distance between two points is defined as their Manhattan distance.

Return the minimum possible value for maximum distance between any two points by removing exactly one point.


Example 1:

Input: points = [[3,10],[5,15],[10,2],[4,4]]
Output: 12
Explanation:
The maximum distance after removing each point is the following:
After removing the 0th point the maximum distance is between points (5, 15) and (10, 2), which is |5 - 10| + |15 - 2| = 18.
After removing the 1st point the maximum distance is between points (3, 10) and (10, 2), which is |3 - 10| + |10 - 2| = 15.
After removing the 2nd point the maximum distance is between points (5, 15) and (4, 4), which is |5 - 4| + |15 - 4| = 12.
After removing the 3rd point the maximum distance is between points (5, 15) and (10, 2), which is |5 - 10| + |15 - 2| = 18.
12 is the minimum possible maximum distance between any two points after removing exactly one point.

Example 2:

Input: points = [[1,1],[1,1],[1,1]]
Output: 0
Explanation:
Removing any of the points results in the maximum distance between any two points of 0.


Constraints:

3 <= points.length <= 10^5
points[i].length == 2
1 <= points[i][0], points[i][1] <= 10^8

"""

# V0
# IDEA : ROTATE TO CHEBYSHEV — MANHATTAN MAX BECOMES TWO INDEPENDENT SPREADS
#
#   with u = x + y and v = x - y,
#       |x1-x2| + |y1-y2| = max(|u1-u2|, |v1-v2|)
#   so the largest pairwise Manhattan distance in a set is simply
#       max(spread of u, spread of v)
#   where spread = max - min. no pair enumeration needed.
#
#   removing one point can only change a spread if that point is the current
#   extreme, so keeping the TWO largest and TWO smallest of u and of v is
#   enough : dropping point i falls back to the runner-up exactly when i held
#   the record.
#
#   try all n removals with those precomputed extremes and take the minimum.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def minimumDistance(self, points):
        us = sorted((x + y, i) for i, (x, y) in enumerate(points))
        vs = sorted((x - y, i) for i, (x, y) in enumerate(points))

        def spread_without(arr, i):
            lo = arr[0][0] if arr[0][1] != i else arr[1][0]
            hi = arr[-1][0] if arr[-1][1] != i else arr[-2][0]
            return hi - lo

        return min(max(spread_without(us, i), spread_without(vs, i))
                   for i in range(len(points)))
