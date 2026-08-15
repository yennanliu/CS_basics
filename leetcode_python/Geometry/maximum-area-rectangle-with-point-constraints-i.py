"""

3380. Maximum Area Rectangle With Point Constraints I
Medium

You are given an array points where points[i] = [x_i, y_i] represents the coordinates of a point on an infinite plane.

Your task is to find the maximum area of a rectangle that:

Can be formed using four of these points as its corners.
Does not contain any other point inside or on its border.
Has its edges parallel to the axes.

Return the maximum area that you can obtain or -1 if no such rectangle is possible.


Example 1:

Input: points = [[1,1],[1,3],[3,1],[3,3]]
Output: 4
Explanation:
We can make a rectangle with these 4 points as corners and there is no other point that lies inside or on the border. Hence, the maximum possible area would be 4.

Example 2:

Input: points = [[1,1],[1,3],[3,1],[3,3],[2,2]]
Output: -1
Explanation:
There is only one rectangle possible is with points [1,1], [1,3], [3,1] and [3,3] but [2,2] will always lie inside it. Hence, returning -1.

Example 3:

Input: points = [[1,1],[1,3],[3,1],[3,3],[1,2],[3,2]]
Output: 2
Explanation:
The maximum area rectangle is formed by the points [1,3], [1,2], [3,2], [3,3], which has an area of 2. Additionally, the points [1,1], [1,2], [3,1], [3,2] also form a valid rectangle with the same area.


Constraints:

1 <= points.length <= 10
points[i].length == 2
0 <= x_i, y_i <= 100
All the given points are unique.

"""

# V0
# IDEA : ONLY 10 POINTS — TRY EVERY (x1, x2) x (y1, y2) BOX
#
#   an axis-parallel rectangle is decided by two distinct x values and two
#   distinct y values, so enumerate those pairs and check that all four
#   corners exist.
#
#   the "nothing inside or on the border" rule is then a scan over the other
#   points : any point whose coordinates both fall inside the closed box, and
#   which is not one of the four corners, disqualifies it.
#
# time = O(n^5) worst case with n = 10, space = O(n)
class Solution(object):
    def maxRectangleArea(self, points):
        pts = set(map(tuple, points))
        xs = sorted(set(p[0] for p in points))
        ys = sorted(set(p[1] for p in points))

        best = -1
        for i in range(len(xs)):
            for j in range(i + 1, len(xs)):
                x1, x2 = xs[i], xs[j]
                for a in range(len(ys)):
                    for b in range(a + 1, len(ys)):
                        y1, y2 = ys[a], ys[b]
                        corners = {(x1, y1), (x1, y2), (x2, y1), (x2, y2)}
                        if not corners <= pts:
                            continue
                        blocked = False
                        for (px, py) in pts:
                            if (px, py) in corners:
                                continue
                            if x1 <= px <= x2 and y1 <= py <= y2:
                                blocked = True
                                break
                        if not blocked:
                            best = max(best, (x2 - x1) * (y2 - y1))
        return best
