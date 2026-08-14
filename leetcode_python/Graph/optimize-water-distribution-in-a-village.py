"""

1168. Optimize Water Distribution in a Village
Hard

There are n houses in a village. We want to supply water for all the houses by building wells and laying pipes.

For each house i, we can either build a well inside it directly with cost wells[i - 1] (note the -1 due to
0-indexing), or pipe in water from another well to it. The costs to lay pipes between houses are given by the
array pipes where each pipes[j] = [house1_j, house2_j, cost_j] represents the cost to connect house1_j and
house2_j together using a pipe. Connections are bidirectional, and there could be multiple valid connections
between the same two houses with different costs.

Return the minimum total cost to supply water to all houses.

Example 1:

Input: n = 3, wells = [1,2,2], pipes = [[1,2,1],[2,3,1]]
Output: 3
Explanation: The best strategy is to build a well in the first house with cost 1 and connect the other houses
to it with cost 2 so the total cost is 3.

Example 2:

Input: n = 2, wells = [1,1], pipes = [[1,2,1],[1,2,2]]
Output: 2
Explanation: We can supply water with cost two using one of the three options:
Option 1:
  - Build a well inside house 1 with cost 1.
  - Build a well inside house 2 with cost 1.
The total cost will be 2.
Option 2:
  - Build a well inside house 1 with cost 1.
  - Connect house 2 with house 1 with cost 1.
The total cost will be 2.
Option 3:
  - Build a well inside house 2 with cost 1.
  - Connect house 1 with house 2 with cost 1.
The total cost will be 2.

Constraints:

2 <= n <= 10^4
wells.length == n
0 <= wells[i] <= 10^5
1 <= pipes.length <= 10^4
pipes[j].length == 3
1 <= house1_j, house2_j <= n
0 <= cost_j <= 10^5
house1_j != house2_j

"""

# V0
# IDEA : MST (KRUSKAL) + VIRTUAL NODE 0
#
#  trick: add a "virtual water source" node 0.
#  building a well at house i == laying a pipe (0, i) with cost wells[i-1].
#  then the answer is simply the MST of the (n + 1) node graph.
#
# time = O((m + n) log (m + n))
# space = O(m + n)
class Solution(object):
    def minCostToSupplyWater(self, n, wells, pipes):
        # edge = (cost, u, v)
        edges = [(c, 0, i + 1) for i, c in enumerate(wells)]
        for a, b, c in pipes:
            edges.append((c, a, b))
        edges.sort()

        parent = list(range(n + 1))

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]  # path compression
                x = parent[x]
            return x

        res = 0
        cnt = n + 1  # number of connected components
        for c, a, b in edges:
            ra, rb = find(a), find(b)
            if ra == rb:
                continue
            parent[ra] = rb
            res += c
            cnt -= 1
            if cnt == 1:
                break
        return res


# V1
# IDEA : MST (PRIM) with min-heap, better when the graph is dense
# time = O((m + n) log n)
# space = O(m + n)
import heapq
from collections import defaultdict
class Solution(object):
    def minCostToSupplyWater(self, n, wells, pipes):
        g = defaultdict(list)
        for i, c in enumerate(wells):
            g[0].append((c, i + 1))
            g[i + 1].append((c, 0))
        for a, b, c in pipes:
            g[a].append((c, b))
            g[b].append((c, a))

        visited = [False] * (n + 1)
        pq = [(0, 0)]
        res = 0
        seen = 0
        while pq and seen <= n:
            c, u = heapq.heappop(pq)
            if visited[u]:
                continue
            visited[u] = True
            seen += 1
            res += c
            for nc, v in g[u]:
                if not visited[v]:
                    heapq.heappush(pq, (nc, v))
        return res
