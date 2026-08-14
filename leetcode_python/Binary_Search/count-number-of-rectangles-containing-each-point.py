"""

2250. Count Number of Rectangles Containing Each Point
Medium

You are given a 2D integer array rectangles where rectangles[i] = [li, hi] indicates that ith rectangle has a length of li and a height of hi. You are also given a 2D integer array points where points[j] = [xj, yj] is a point with coordinates (xj, yj).

The ith rectangle has its bottom-left corner point at the coordinates (0, 0) and its top-right corner point at (li, hi).

Return an integer array count of length points.length where count[j] is the number of rectangles that contain the jth point.

The ith rectangle contains the jth point if 0 <= xj <= li and 0 <= yj <= hi. Note that points that lie on the edges of a rectangle are also considered to be contained by that rectangle.


Example 1:

Input: rectangles = [[1,2],[2,3],[2,5]], points = [[2,1],[1,4]]
Output: [2,1]
Explanation:
The first rectangle contains no points.
The second rectangle contains only the point (2, 1).
The third rectangle contains the points (2, 1) and (1, 4).
The number of rectangles that contain the point (2, 1) is 2.
The number of rectangles that contain the point (1, 4) is 1.
Therefore, we return [2, 1].

Example 2:

Input: rectangles = [[1,1],[2,2],[3,3]], points = [[1,3],[1,1]]
Output: [1,3]
Explanation:
The first rectangle contains only the point (1, 1).
The second rectangle contains only the point (1, 1).
The third rectangle contains the points (1, 3) and (1, 1).
The number of rectangles that contain the point (1, 3) is 1.
The number of rectangles that contain the point (1, 1) is 3.
Therefore, we return [1, 3], in that order.


Constraints:

1 <= rectangles.length, points.length <= 5 * 10^4
rectangles[i].length == points[j].length == 2
1 <= li, xj <= 10^9
1 <= hi, yj <= 100

"""

# V0
# IDEA : HEIGHTS ONLY GO UP TO 100 — BUCKET BY HEIGHT, BINARY SEARCH LENGTHS
#
#   a rectangle [l, h] contains (x, y) iff l >= x AND h >= y. the height is
#   bounded by 100, so bucket the rectangles into 101 lists keyed by h and
#   sort the lengths inside each bucket.
#
#   then a query (x, y) sums, over the buckets h = y .. 100, how many lengths
#   are >= x — one bisect per bucket, so at most 100 binary searches.
#
#   NOTE : the small-domain dimension is what makes this cheap; the length
#          dimension is up to 10^9 and must stay a binary search.
#
# time = O(R log R + P * 100 * log R), space = O(R)
import bisect


class Solution(object):
    def countRectangles(self, rectangles, points):
        MAX_H = 100
        by_height = [[] for _ in range(MAX_H + 1)]
        for l, h in rectangles:
            by_height[h].append(l)
        for lengths in by_height:
            lengths.sort()

        res = []
        for x, y in points:
            total = 0
            for h in range(y, MAX_H + 1):
                lengths = by_height[h]
                total += len(lengths) - bisect.bisect_left(lengths, x)
            res.append(total)
        return res
