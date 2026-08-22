"""

587. Erect the Fence
Hard

You are given an array trees where trees[i] = [xi, yi] represents the location of a tree in the garden.

You are asked to fence the entire garden using the minimum length of rope as it is expensive. The garden is well fenced only if all the trees are enclosed.

Return the coordinates of trees that are exactly located on the fence perimeter.

 

Example 1:


Input: points = [[1,1],[2,2],[2,0],[2,4],[3,3],[4,2]]
Output: [[1,1],[2,0],[3,3],[2,4],[4,2]]
Example 2:


Input: points = [[1,2],[2,2],[4,2]]
Output: [[4,2],[2,2],[1,2]]
 

Constraints:

1 <= points.length <= 3000
points[i].length == 2
0 <= xi, yi <= 100
All the given points are unique.

"""

# V0
# IDEA : MONOTONE CHAIN (ANDREW) — LOWER HULL + UPPER HULL, COLLINEAR KEPT
#
#   sort the points by (x, y) and sweep twice : left -> right builds the
#   lower hull, right -> left builds the upper one.  a point is dropped only
#   on a STRICT right turn (cross < 0), so points lying exactly ON a hull
#   edge survive — which is what this problem asks for (unlike the textbook
#   hull, where cross <= 0 also pops them).
#
#   the two chains share their two endpoints, hence the set() at the end.
#   with <= 3 points every point is on the fence by definition.
#
# time = O(n log n)  (the sort dominates; each sweep is O(n) amortised)
# space = O(n)
class Solution(object):
    def outerTrees(self, trees):
        points = sorted(set(map(tuple, trees)))
        if len(points) <= 3:
            return [list(p) for p in points]

        def cross(o, a, b):
            return ((a[0] - o[0]) * (b[1] - o[1])
                    - (a[1] - o[1]) * (b[0] - o[0]))

        def chain(seq):
            hull = []
            for p in seq:
                while len(hull) >= 2 and cross(hull[-2], hull[-1], p) < 0:
                    hull.pop()
                hull.append(p)
            return hull

        fence = set(chain(points)) | set(chain(points[::-1]))
        return [list(p) for p in fence]


# V0-1
# IDEA : JARVIS MARCH / GIFT WRAPPING — WALK THE HULL EDGE BY EDGE
#
#   start at the leftmost point and repeatedly pick the point q that no other
#   point sits to the left of (cross(p, q, r) > 0 means r is further
#   counterclockwise, so r becomes the new q).  ties (cross == 0) go to the
#   FARTHEST point, otherwise a fully collinear input would stop after one
#   step; every point lying between p and q is then part of that edge and
#   gets recorded too.
#
#   output-sensitive : cheaper than sorting when the hull has few vertices
#   (h small), much worse when almost every point is on it (h = n).
#
# time = O(n * h)  (h = number of hull vertices, worst case O(n^2))
# space = O(h)
class Solution(object):
    def outerTrees(self, trees):
        points = list(set(map(tuple, trees)))
        if len(points) <= 3:
            return [list(p) for p in points]

        def cross(o, a, b):
            return ((a[0] - o[0]) * (b[1] - o[1])
                    - (a[1] - o[1]) * (b[0] - o[0]))

        def dist2(a, b):
            return (a[0] - b[0]) ** 2 + (a[1] - b[1]) ** 2

        def on_segment(p, mid, q):
            return (min(p[0], q[0]) <= mid[0] <= max(p[0], q[0])
                    and min(p[1], q[1]) <= mid[1] <= max(p[1], q[1]))

        fence = set()
        start = min(points)
        p = start
        while True:
            q = points[1] if points[0] == p else points[0]
            for r in points:
                if r == p:
                    continue
                c = cross(p, q, r)
                if c > 0 or (c == 0 and dist2(p, r) > dist2(p, q)):
                    q = r
            fence.add(p)
            fence.add(q)
            for r in points:
                if (r != p and r != q and cross(p, q, r) == 0
                        and on_segment(p, r, q)):
                    fence.add(r)
            p = q
            if p == start:
                break
        return [list(x) for x in fence]


# V0-2
# IDEA : GRAHAM SCAN — POLAR SORT AROUND THE BOTTOM-MOST POINT, THEN ONE STACK
#
#   pivot = bottom-most (then leftmost) point, so every other point sits at a
#   polar angle in [0, 180).  sort by that angle using the CROSS PRODUCT as
#   the comparator (exact integer arithmetic — no atan2 rounding), ties by
#   distance to the pivot.
#
#   two details make the collinear points come out right :
#     - the LAST angular group is reversed (farthest first) so the points on
#       the closing edge are not cut away by the scan,
#     - the stack pops only on a STRICT right turn, keeping cross == 0.
#
# time = O(n log n)
# space = O(n)
import functools
class Solution(object):
    def outerTrees(self, trees):
        points = list(set(map(tuple, trees)))
        if len(points) <= 3:
            return [list(p) for p in points]

        def cross(o, a, b):
            return ((a[0] - o[0]) * (b[1] - o[1])
                    - (a[1] - o[1]) * (b[0] - o[0]))

        pivot = min(points, key=lambda p: (p[1], p[0]))
        rest = [p for p in points if p != pivot]

        def dist2(p):
            return (p[0] - pivot[0]) ** 2 + (p[1] - pivot[1]) ** 2

        def by_angle(a, b):
            c = cross(pivot, a, b)
            if c != 0:
                return -1 if c > 0 else 1
            return -1 if dist2(a) < dist2(b) else 1

        rest.sort(key=functools.cmp_to_key(by_angle))

        # reverse the final collinear group : farthest first
        i = len(rest) - 1
        while i > 0 and cross(pivot, rest[-1], rest[i - 1]) == 0:
            i -= 1
        rest[i:] = rest[i:][::-1]

        stack = [pivot]
        for p in rest:
            while len(stack) >= 2 and cross(stack[-2], stack[-1], p) < 0:
                stack.pop()
            stack.append(p)
        return [list(p) for p in stack]


# V1
# https://awesome.dbyun.net/study/details/56/3692
# https://blog.csdn.net/Changxing_J/article/details/110739219

# V1'
# IDEA : Jarvis Algorithm
# https://leetcode.com/problems/erect-the-fence/solution/
# JAVA
# public class Solution {
#     public int orientation(int[] p, int[] q, int[] r) {
#         return (q[1] - p[1]) * (r[0] - q[0]) - (q[0] - p[0]) * (r[1] - q[1]);
#     }
#
#     public boolean inBetween(int[] p, int[] i, int[] q) {
#         boolean a = i[0] >= p[0] && i[0] <= q[0] || i[0] <= p[0] && i[0] >= q[0];
#         boolean b = i[1] >= p[1] && i[1] <= q[1] || i[1] <= p[1] && i[1] >= q[1];
#         return a && b;
#     }
#
#     public int[][] outerTrees(int[][] points) {
#         HashSet<int[]> hull = new HashSet<> ();
#         if (points.length < 4) {
#             for (int[] p: points)
#                 hull.add(p);
#             return hull.toArray(new int[hull.size()][]);
#         }
#         int left_most = 0;
#         for (int i = 0; i < points.length; i++)
#             if (points[i][0] < points[left_most][0])
#                 left_most = i;
#         int p = left_most;
#         do {
#             int q = (p + 1) % points.length;
#             for (int i = 0; i < points.length; i++) {
#                 if (orientation(points[p], points[i], points[q]) < 0) {
#                     q = i;
#                 }
#             }
#             for (int i = 0; i < points.length; i++) {
#                 if (i != p && i != q && orientation(points[p], points[i], points[q]) == 0 && inBetween(points[p], points[i], points[q])) {
#                     hull.add(points[i]);
#                 }
#             }
#             hull.add(points[q]);
#             p = q;
#         }
#         while (p != left_most);
#         return hull.toArray(new int[hull.size()][]);
#     }
# }

# V1''
# IDEA :  Graham Scan
# https://leetcode.com/problems/erect-the-fence/solution/
# JAVA
# public class Solution {
#     public int orientation(int[] p, int[] q, int[] r) {
#         return (q[1] - p[1]) * (r[0] - q[0]) - (q[0] - p[0]) * (r[1] - q[1]);
#     }
#     public int distance(int[] p, int[] q) {
#         return (p[0] - q[0]) * (p[0] - q[0]) + (p[1] - q[1]) * (p[1] - q[1]);
#     }
#
#     private static int[] bottomLeft(int[][] points) {
#         int[] bottomLeft = points[0];
#         for (int[] p: points)
#             if (p[1] < bottomLeft[1])
#                 bottomLeft = p;
#         return bottomLeft;
#     }
#     public int[][] outerTrees(int[][] points) {
#         if (points.length <= 1)
#             return points;
#         int[] bm = bottomLeft(points);
#         Arrays.sort(points, new Comparator<int[]> () {
#             public int compare(int[] p, int[] q) {
#                 double diff = orientation(bm, p, q) - orientation(bm, q, p);
#                 if (diff == 0)
#                     return distance(bm, p) - distance(bm, q);
#                 else
#                     return diff > 0 ? 1 : -1;
#             }
#         });
#         int i = points.length - 1;
#         while (i >= 0 && orientation(bm, points[points.length - 1], points[i]) == 0)
#             i--;
#         for (int l = i + 1, h = points.length - 1; l < h; l++, h--) {
#             int[] temp = points[l];
#             points[l] = points[h];
#             points[h] = temp;
#         }
#         Stack<int[]> stack = new Stack< > ();
#         stack.push(points[0]);
#         stack.push(points[1]);
#         for (int j = 2; j < points.length; j++) {
#             int[] top = stack.pop();
#             while (orientation(stack.peek(), top, points[j]) > 0)
#                 top = stack.pop();
#             stack.push(top);
#             stack.push(points[j]);
#         }
#         return stack.toArray(new int[stack.size()][]);
#     }
# }


# V1'''
# IDEA : Monotone Chain
# https://leetcode.com/problems/erect-the-fence/solution/
# JAVA
# public class Solution {
#     public int orientation(int[] p, int[] q, int[] r) {
#         return (q[1] - p[1]) * (r[0] - q[0]) - (q[0] - p[0]) * (r[1] - q[1]);
#     }
#     public int[][] outerTrees(int[][] points) {
#         Arrays.sort(points, new Comparator<int[]> () {
#             public int compare(int[] p, int[] q) {
#                 return q[0] - p[0] == 0 ? q[1] - p[1] : q[0] - p[0];
#             }
#         });
#         Stack<int[]> hull = new Stack<>();
#         for (int i = 0; i < points.length; i++) {
#             while (hull.size() >= 2 && orientation(hull.get(hull.size() - 2), hull.get(hull.size() - 1), points[i]) > 0)
#                 hull.pop();
#             hull.push(points[i]);
#         }
#         hull.pop();
#         for (int i = points.length - 1; i >= 0; i--) {
#             while (hull.size() >= 2 && orientation(hull.get(hull.size() - 2), hull.get(hull.size() - 1), points[i]) > 0)
#                 hull.pop();
#             hull.push(points[i]);
#         }
#         // remove redundant elements from the stack
#         HashSet<int[]> ret = new HashSet<>(hull);
#         return ret.toArray(new int[ret.size()][]);
#     }
# }

# V2
