"""

3111. Minimum Rectangles to Cover Points
Medium

You are given a 2D integer array points, where points[i] = [xi, yi]. You are also given an integer w. Your task is to cover all the given points with rectangles.

Each rectangle has its lower end at some point (x1, 0) and its upper end at some point (x2, y2), where x1 <= x2, y2 >= 0, and the condition x2 - x1 <= w must be satisfied for each rectangle.

A point is considered covered by a rectangle if it lies within or on the boundary of the rectangle.

Return an integer denoting the minimum number of rectangles needed so that each point is covered by at least one rectangle.

Note: A point may be covered by more than one rectangle.


Example 1:

Input: points = [[2,1],[1,0],[1,4],[1,8],[3,5],[4,6]], w = 1
Output: 2
Explanation:
The image above shows one possible placement of rectangles to cover the points:
A rectangle with a lower end at (1, 0) and its upper end at (2, 8)
A rectangle with a lower end at (3, 0) and its upper end at (4, 8)

Example 2:

Input: points = [[0,0],[1,1],[2,2],[3,3],[4,4],[5,5],[6,6]], w = 2
Output: 3
Explanation:
The image above shows one possible placement of rectangles to cover the points:
A rectangle with a lower end at (0, 0) and its upper end at (2, 2)
A rectangle with a lower end at (3, 0) and its upper end at (5, 5)
A rectangle with a lower end at (6, 0) and its upper end at (6, 6)

Example 3:

Input: points = [[2,3],[1,2]], w = 0
Output: 2
Explanation:
The image above shows one possible placement of rectangles to cover the points:
A rectangle with a lower end at (1, 0) and its upper end at (1, 2)
A rectangle with a lower end at (2, 0) and its upper end at (2, 3)


Constraints:

1 <= points.length <= 10^5
points[i].length == 2
0 <= xi == points[i][0] <= 10^9
0 <= yi == points[i][1] <= 10^9
0 <= w <= 10^9

"""

# V0
# IDEA : y IS IRRELEVANT — IT IS A 1-D INTERVAL COVER ON THE x AXIS
#
#   a rectangle reaches from y = 0 up to any height, so it can always be made
#   tall enough. the only real constraint is the width : one rectangle covers
#   an x-interval of length at most w.
#
#   so sort the x-coordinates and sweep : open a rectangle at the first
#   uncovered x, and let it absorb everything up to x + w; the next x beyond
#   that starts a new rectangle. that greedy is optimal because the leftmost
#   uncovered point must be covered by SOME rectangle, and starting one
#   exactly there reaches as far right as any rectangle covering it could.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def minRectanglesToCoverPoints(self, points, w):
        xs = sorted(p[0] for p in points)
        res = 0
        limit = -1                       # right edge of the open rectangle
        for x in xs:
            if x > limit:
                res += 1
                limit = x + w
        return res
