"""

2045. Second Minimum Time to Reach Destination
Hard

A city is represented as a bi-directional connected graph with n vertices where each vertex is labeled from 1 to n (inclusive). The edges in the graph are represented as a 2D integer array edges, where each edges[i] = [ui, vi] denotes a bi-directional edge between vertex ui and vertex vi. Every vertex pair is connected by at most one edge, and no vertex has an edge to itself. The time taken to traverse any edge is time minutes.

Each vertex has a traffic signal which changes its color from green to red and vice versa every change minutes. All signals change at the same time. You can enter a vertex at any time, but can leave a vertex only when the signal is green. You cannot wait at a vertex if the signal is green.

The second minimum value is defined as the smallest value strictly larger than the minimum value.

For example the second minimum value of [2, 3, 4] is 3, and the second minimum value of [2, 2, 4] is 4.

Given n, edges, time, and change, return the second minimum time it will take to go from vertex 1 to vertex n.

Notes:

You can go through any vertex any number of times, including 1 and n.
You can assume that when the journey starts, all signals have just turned green.


Example 1:

Input: n = 5, edges = [[1,2],[1,3],[1,4],[3,4],[4,5]], time = 3, change = 5
Output: 13
Explanation:
The minimum time taken is 9 minutes (path 1 -> 4 -> 5), and the second minimum is 13 minutes (path 1 -> 3 -> 4 -> 5).

Example 2:

Input: n = 2, edges = [[1,2]], time = 3, change = 2
Output: 11


Constraints:

2 <= n <= 10^4
n - 1 <= edges.length <= min(2 * 10^4, n * (n - 1) / 2)
edges[i].length == 2
1 <= ui, vi <= n
ui != vi
There are no duplicate edges.
Each vertex can be reached directly or indirectly from every other vertex.
1 <= time, change <= 5 * 10^4

"""

# V0
# IDEA : BFS TRACKING THE TWO SMALLEST *EDGE COUNTS* PER NODE, THEN SIMULATE
#
#   every edge costs the same `time`, so a path's cost depends only on its
#   number of edges. so : plain BFS keeping, for each node, dist1 (fewest
#   edges) and dist2 (second-fewest, strictly greater). a node is enqueued at
#   most twice, keeping it O(V + E).
#
#   then convert an edge count into minutes, replaying the traffic lights :
#       for each of the `steps` edges :
#           if (t // change) is ODD -> the light is red, wait until the next
#              multiple of change
#           t += time
#
#   NOTE : `dist2` must be STRICTLY greater than `dist1`; if the second-best
#          path has the same length it is not a valid "second minimum".
#
# time = O(V + E), space = O(V + E)
from collections import deque, defaultdict


class Solution(object):
    def secondMinimum(self, n, edges, time, change):
        g = defaultdict(list)
        for u, v in edges:
            g[u].append(v)
            g[v].append(u)

        INF = float('inf')
        dist1 = [INF] * (n + 1)
        dist2 = [INF] * (n + 1)
        dist1[1] = 0
        q = deque([(1, 0)])
        while q:
            u, d = q.popleft()
            for v in g[u]:
                nd = d + 1
                if nd < dist1[v]:
                    dist1[v] = nd
                    q.append((v, nd))
                elif dist1[v] < nd < dist2[v]:
                    dist2[v] = nd
                    q.append((v, nd))

        steps = dist2[n]
        t = 0
        for _ in range(steps):
            if (t // change) % 2 == 1:      # red light -> wait it out
                t = (t // change + 1) * change
            t += time
        return t
