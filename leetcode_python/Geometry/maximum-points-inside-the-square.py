"""

3143. Maximum Points Inside the Square
Medium

You are given a 2D array points and a string s where, points[i] represents the coordinates of point i, and s[i] represents the tag of point i.

A valid square is a square centered at the origin (0, 0), has edges parallel to the axes, and does not contain two points with the same tag.

Return the maximum number of points contained in a valid square.

Note:

A point is considered to be inside the square if it lies on or within the square's boundaries.
The side length of the square can be zero.


Example 1:

Input: points = [[2,2],[-1,-2],[-4,4],[-3,1],[3,-3]], s = "abdca"
Output: 2
Explanation:
The square of side length 4 covers two points points[0] and points[1].

Example 2:

Input: points = [[1,1],[-2,-2],[-2,2]], s = "abb"
Output: 1
Explanation:
The square of side length 2 covers one point, which is points[0].

Example 3:

Input: points = [[1,1],[-1,-1],[2,-2]], s = "ccd"
Output: 0
Explanation:
It can be shown that any square centered at the origin that covers at least one point causes the sum of tags to be repeated.


Constraints:

1 <= s.length, points.length <= 10^5
points[i].length == 2
-10^9 <= points[i][0], points[i][1] <= 10^9
s.length == points.length
points consists of distinct coordinates.
s consists only of lowercase English letters.

"""

# V0
# IDEA : A SQUARE IS JUST A CHEBYSHEV RADIUS — FIND WHERE THE FIRST TAG REPEATS
#
#   a square of half-side r centred at the origin contains exactly the points
#   with max(|x|, |y|) <= r. so every point is described by one number, its
#   Chebyshev radius, and growing the square means sweeping r upward.
#
#   a tag becomes a conflict the moment its SECOND-smallest radius is
#   admitted, so the square must stop strictly before
#
#       limit = min over tags of (second smallest radius of that tag)
#
#   then the answer counts the points with radius < limit. tracking, per tag,
#   only the two smallest radii is enough — one pass, 26 pairs of numbers.
#
# time = O(n), space = O(1)  (26 tags)
class Solution(object):
    def maxPointsInsideSquare(self, points, s):
        INF = float('inf')
        best1 = [INF] * 26                # smallest radius per tag
        best2 = [INF] * 26                # second smallest

        radii = []
        for (x, y), tag in zip(points, s):
            r = max(abs(x), abs(y))
            radii.append(r)
            t = ord(tag) - 97
            if r < best1[t]:
                best2[t] = best1[t]
                best1[t] = r
            elif r < best2[t]:
                best2[t] = r

        limit = min(best2)                # first radius that would duplicate a tag
        return sum(1 for r in radii if r < limit)
