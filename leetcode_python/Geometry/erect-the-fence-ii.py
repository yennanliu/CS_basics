"""

1924. Erect the Fence II
Hard

You are given a 2D integer array trees where trees[i] = [xi, yi] represents the location of the ith tree in the garden.

You are asked to fence the entire garden using the minimum length of rope possible. The garden is well-fenced only if all the trees are enclosed and the rope used forms a perfect circle. A tree is considered enclosed if it is inside or on the border of the circle.

More formally, you must form a circle using the rope with a center (x, y) and radius r where all trees lie inside or on the circle and r is minimum.

Return the center and radius of the circle as a length 3 array [x, y, r]. Answers within 10^-5 of the actual answer will be accepted.


Example 1:

Input: trees = [[1,1],[2,2],[2,0],[2,4],[3,3],[4,2]]
Output: [2.00000,2.00000,2.00000]
Explanation: The fence will have center = (2, 2) and radius = 2

Example 2:

Input: trees = [[1,2],[2,2],[4,2]]
Output: [2.50000,2.00000,1.50000]
Explanation: The fence will have center = (2.5, 2) and radius = 1.5


Constraints:

1 <= trees.length <= 3000
trees[i].length == 2
0 <= xi, yi <= 3000

"""

# V0
# IDEA : WELZL / RANDOMIZED INCREMENTAL MINIMUM ENCLOSING CIRCLE
#
#   the smallest enclosing circle is determined by at most 3 points on its
#   boundary. after a random shuffle, run 3 nested "repair" loops :
#
#     - keep a current circle for the prefix p[0..i-1]
#     - if p[i] falls outside, then p[i] MUST lie on the boundary of the answer
#       for the prefix p[0..i]; restart the scan with p[i] pinned
#     - one more level pins a second point, one more pins a third and the
#       circle is then the circumcircle of those 3 points
#
#   each level is entered with probability O(1/i) over the random order, so the
#   whole thing is O(n) EXPECTED even though it looks cubic.
#
#   NOTE : use a small epsilon when testing "is inside" so boundary points do
#          not trigger a needless (and infinite) rebuild.
#
# time = O(n) expected, space = O(n)
import random
class Solution(object):
    def outerTrees(self, trees):
        EPS = 1e-7

        def dist(a, b):
            return ((a[0] - b[0]) ** 2 + (a[1] - b[1]) ** 2) ** 0.5

        def from_two(a, b):
            cx = (a[0] + b[0]) / 2.0
            cy = (a[1] + b[1]) / 2.0
            return (cx, cy, dist(a, b) / 2.0)

        def from_three(a, b, c):
            # circumcircle via the perpendicular-bisector linear system
            ax, ay = a
            bx, by = b
            cx, cy = c
            d = 2.0 * (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by))
            if abs(d) < EPS:
                # collinear -> the answer is the circle on the 2 farthest points
                best = from_two(a, b)
                for pair in ((a, c), (b, c)):
                    cand = from_two(pair[0], pair[1])
                    if cand[2] > best[2]:
                        best = cand
                return best
            ux = ((ax * ax + ay * ay) * (by - cy)
                  + (bx * bx + by * by) * (cy - ay)
                  + (cx * cx + cy * cy) * (ay - by)) / d
            uy = ((ax * ax + ay * ay) * (cx - bx)
                  + (bx * bx + by * by) * (ax - cx)
                  + (cx * cx + cy * cy) * (bx - ax)) / d
            return (ux, uy, dist((ux, uy), a))

        def inside(circle, p):
            return dist((circle[0], circle[1]), p) <= circle[2] + EPS

        pts = [(float(x), float(y)) for x, y in trees]
        random.shuffle(pts)

        circle = (pts[0][0], pts[0][1], 0.0)
        for i in range(1, len(pts)):
            if inside(circle, pts[i]):
                continue
            circle = (pts[i][0], pts[i][1], 0.0)
            for j in range(i):
                if inside(circle, pts[j]):
                    continue
                circle = from_two(pts[i], pts[j])
                for k in range(j):
                    if not inside(circle, pts[k]):
                        circle = from_three(pts[i], pts[j], pts[k])

        return [circle[0], circle[1], circle[2]]
