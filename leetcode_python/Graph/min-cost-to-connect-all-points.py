"""

1584. Min Cost to Connect All Points
Medium

You are given an array points representing integer coordinates of some points on a
2D-plane, where points[i] = [xi, yi].

The cost of connecting two points [xi, yi] and [xj, yj] is the manhattan distance
between them: |xi - xj| + |yi - yj|, where |val| denotes the absolute value of val.

Return the minimum cost to make all points connected. All points are connected if
there is exactly one simple path between any two points.

Example 1:

Input: points = [[0,0],[2,2],[3,10],[5,2],[7,0]]

Output: 20

Explanation:

We can connect the points as shown above to get the minimum cost of 20.
Notice that there is a unique path between every pair of points.

Example 2:

Input: points = [[3,12],[-2,5],[-4,1]]

Output: 18

Constraints:

1 <= points.length <= 1000

-10^6 <= xi, yi <= 10^6

All pairs (xi, yi) are distinct.

"""

# V0
# IDEA : MST (PRIM'S ALGO) + MIN HEAP
#
#   "exactly one simple path between any two points" == a spanning TREE,
#   and the cheapest one is the MINIMUM SPANNING TREE of the complete graph
#   (every point is a node, every pair is an edge weighted by manhattan dist).
#
#   Prim grows ONE tree from point 0: the heap holds (cost, point) candidates,
#   and the cheapest edge leaving the tree is always safe to take (cut property).
#   A point popped a second time was already joined more cheaply -> skip it.
#
#   e.g. [[0,0],[2,2],[3,10],[5,2],[7,0]]
#        take 0 (0) -> 1 (4) -> 3 (3) -> 4 (4) -> 2 (9) -> 0+4+3+4+9 = 20
#
#   This is the interview version: same shape as Dijkstra (LC 743), only the
#   heap key is the EDGE cost, not the accumulated path cost.
#
# time = O(n^2 log n), space = O(n^2)
import heapq


class Solution(object):
    def minCostConnectPoints(self, points):
        """
        :type points: List[List[int]]
        :rtype: int
        """
        n = len(points)
        # edge case: 0 or 1 point needs no edge
        if n <= 1:
            return 0

        in_tree = set()
        # (cost to join this point to the tree, point index); start from point 0 for free
        min_heap = [(0, 0)]
        total_cost = 0

        while len(in_tree) < n:
            cost, point = heapq.heappop(min_heap)

            # NOTE !!! a point can be pushed many times with different costs;
            #          only its FIRST pop (the cheapest) joins the tree
            if point in in_tree:
                continue

            in_tree.add(point)
            total_cost += cost

            # offer an edge from the newly joined point to every point still outside
            x1, y1 = points[point]
            for other in range(n):
                if other not in in_tree:
                    x2, y2 = points[other]
                    distance = abs(x1 - x2) + abs(y1 - y2)
                    heapq.heappush(min_heap, (distance, other))

        return total_cost


# V0-1
# IDEA : MST (PRIM'S ALGO) + DIST ARRAY, NO HEAP
#
#   Same Prim as V0, but the graph is COMPLETE (n^2 edges), so a heap is
#   wasted work. Keep min_dist[i] = cheapest edge from the tree to point i,
#   and each round scan for the smallest one in O(n).
#   n rounds * O(n) scan = O(n^2), which is optimal: every edge must be looked at.
#
#   NOTE : the best bound for a dense graph; mention it after V0 in an interview.
#
# time = O(n^2), space = O(n)
class Solution2(object):
    def minCostConnectPoints(self, points):
        """
        :type points: List[List[int]]
        :rtype: int
        """
        n = len(points)
        if n <= 1:
            return 0

        INF = float("inf")
        # min_dist[i] = cheapest edge connecting point i to the current tree
        min_dist = [INF] * n
        in_tree = [False] * n
        min_dist[0] = 0
        total_cost = 0

        for _ in range(n):
            # pick the outside point closest to the tree
            closest = -1
            for i in range(n):
                if not in_tree[i] and (closest == -1 or min_dist[i] < min_dist[closest]):
                    closest = i

            in_tree[closest] = True
            total_cost += min_dist[closest]

            # the new tree point may offer a cheaper edge to the points still outside
            x1, y1 = points[closest]
            for i in range(n):
                if not in_tree[i]:
                    x2, y2 = points[i]
                    distance = abs(x1 - x2) + abs(y1 - y2)
                    if distance < min_dist[i]:
                        min_dist[i] = distance

        return total_cost


# V1
# IDEA : MST (KRUSKAL'S ALGO) + UNION FIND
#
#   Sort ALL edges by cost, take each one that joins two different components
#   (union-find says so in ~O(1)), skip each one that would close a cycle.
#   Stop once n - 1 edges are taken: a tree on n nodes has exactly n - 1 edges.
#
#   NOTE : sorting n^2 / 2 edges is slower and heavier than Prim on a
#          complete graph, but Kruskal is the one to know for SPARSE graphs
#          (e.g. LC 1168, LC 1135).
#
# time = O(n^2 log n), space = O(n^2)
class Solution3(object):
    def minCostConnectPoints(self, points):
        """
        :type points: List[List[int]]
        :rtype: int
        """
        n = len(points)
        if n <= 1:
            return 0

        # every pair of points is an edge: (cost, point_a, point_b)
        edges = []
        for i in range(n):
            for j in range(i + 1, n):
                distance = abs(points[i][0] - points[j][0]) + abs(points[i][1] - points[j][1])
                edges.append((distance, i, j))
        edges.sort()

        parent = list(range(n))
        rank = [0] * n

        def find(x):
            """Return the root of x's component, compressing the path on the way."""
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        def union(a, b):
            """Merge a's and b's components; return False if already merged (cycle)."""
            root_a = find(a)
            root_b = find(b)
            if root_a == root_b:
                return False
            # attach the shorter tree under the taller one
            if rank[root_a] < rank[root_b]:
                parent[root_a] = root_b
            elif rank[root_a] > rank[root_b]:
                parent[root_b] = root_a
            else:
                parent[root_b] = root_a
                rank[root_a] += 1
            return True

        total_cost = 0
        edges_used = 0
        for distance, a, b in edges:
            if union(a, b):
                total_cost += distance
                edges_used += 1
                # a spanning tree on n points has exactly n - 1 edges
                if edges_used == n - 1:
                    break

        return total_cost
