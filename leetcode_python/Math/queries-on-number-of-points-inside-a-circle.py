"""

1828. Queries on Number of Points Inside a Circle
Medium

You are given an array points where points[i] = [xi, yi] is the coordinates of the ith point on a 2D plane. Multiple points can have the same coordinates.

You are also given an array queries where queries[j] = [xj, yj, rj] describes a circle centered at (xj, yj) with a radius of rj.

For each query queries[j], compute the number of points inside the jth circle. Points on the border of the circle are considered inside.

Return an array answer, where answer[j] is the answer to the jth query.


Example 1:

Input: points = [[1,3],[3,3],[5,3],[2,2]], queries = [[2,3,1],[4,3,1],[1,1,2]]
Output: [3,2,2]
Explanation: The points and circles are shown above.
queries[0] is the green circle, queries[1] is the red circle, and queries[2] is the blue circle.

Example 2:

Input: points = [[1,1],[2,2],[3,3],[4,4],[5,5]], queries = [[1,2,2],[2,2,2],[4,3,2],[4,3,3]]
Output: [2,3,2,4]
Explanation: The points and circles are shown above.
queries[0] is green, queries[1] is red, queries[2] is blue, and queries[3] is purple.


Constraints:

1 <= points.length <= 500
points[i].length == 2
0 <= xi, yi <= 500
1 <= queries.length <= 500
queries[j].length == 3
0 <= xj, yj <= 500
1 <= rj <= 500
All coordinates are integers.


Follow up: Could you find the answer for each query in better complexity than O(n)?

"""

# V0
# IDEA : BRUTE FORCE WITH SQUARED DISTANCES (500 x 500 is tiny)
#
#   a point (px, py) is inside the circle (cx, cy, r) iff
#     (px - cx)^2 + (py - cy)^2 <= r^2
#
#   NOTE : compare SQUARED distances -- staying in integers avoids any float
#          rounding at the border, and "on the border counts" is then exact.
#   NOTE : n, q <= 500 so n*q = 250000 checks, which is the intended solution;
#          the follow-up would need a 2D prefix sum over the 501x501 grid.
#
# time = O(n * q), space = O(1) extra
class Solution(object):
    def countPoints(self, points, queries):
        res = []
        for cx, cy, r in queries:
            rr = r * r
            cnt = 0
            for px, py in points:
                dx, dy = px - cx, py - cy
                if dx * dx + dy * dy <= rr:
                    cnt += 1
            res.append(cnt)
        return res
