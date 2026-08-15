"""

2737. Find the Closest Marked Node
Medium

You are given a positive integer n which is the number of nodes of a 0-indexed directed weighted graph and a 0-indexed 2D array edges where edges[i] = [ui, vi, wi] indicates that there is an edge from node ui to node vi with weight wi.

You are also given a node s and a node array marked; your task is to find the minimum distance from s to any of the nodes in marked.

Return an integer denoting the minimum distance from s to any node in marked or -1 if there are no paths from s to any of the marked nodes.


Example 1:

Input: n = 4, edges = [[0,1,1],[1,2,3],[2,3,2],[0,3,4]], s = 0, marked = [2,3]
Output: 4
Explanation: There is one path from node 0 (the green node) to node 2 (a red node), which is 0->1->2, and has a distance of 1 + 3 = 4.
There are two paths from node 0 to node 3 (a red node), which are 0->1->2->3 and 0->3, the first one has a distance of 1 + 3 + 2 = 6 and the second one has a distance of 4.
The minimum of them is 4.

Example 2:

Input: n = 5, edges = [[0,1,2],[0,2,4],[1,3,1],[2,3,3],[3,4,2]], s = 1, marked = [0,4]
Output: 3
Explanation: There are no paths from node 1 (the green node) to node 0 (a red node).
There is one path from node 1 to node 4 (a red node), which is 1->3->4, and has a distance of 1 + 2 = 3.
So the answer is 3.

Example 3:

Input: n = 4, edges = [[0,1,1],[1,2,3],[2,3,2]], s = 3, marked = [0,1]
Output: -1
Explanation: There are no paths from node 3 (the green node) to any of the marked nodes (the red nodes), so the answer is -1.


Constraints:

2 <= n <= 500
1 <= edges.length <= 10^4
edges[i].length = 3
0 <= edges[i][0], edges[i][1] <= n - 1
1 <= edges[i][2] <= 10^6
1 <= marked.length <= n - 1
0 <= s, marked[i] <= n - 1
s != marked[i]
marked[i] != marked[j] for every i != j
The graph might have repeated edges.
The graph is generated such that it has no self-loops.

"""

import heapq

# V0
# IDEA : DIJKSTRA (SINGLE SOURCE) + MIN OVER THE MARKED SET
#
#   All weights are positive, so a single Dijkstra run from s gives the
#   shortest distance to EVERY node. The answer is then just the smallest
#   dist[v] over v in marked (or -1 when they are all unreachable).
#
#   NOTE : the graph is DIRECTED — only add u -> v, never the reverse.
#
#   NOTE : the graph may contain REPEATED edges between the same pair. That
#          is harmless for Dijkstra (the heavier duplicate simply never
#          improves anything), so no de-duplication is needed.
#
#   NOTE : we early-exit as soon as a marked node is popped from the heap.
#          Dijkstra pops nodes in non-decreasing distance order, so the first
#          marked node popped is already the closest one.
#
# time = O(E * log E), space = O(V + E)
class Solution(object):
    def minimumDistance(self, n, edges, s, marked):
        graph = [[] for _ in range(n)]
        for u, v, w in edges:
            graph[u].append((v, w))

        target = set(marked)
        dist = [-1] * n            # -1 == not settled yet
        pq = [(0, s)]

        while pq:
            d, u = heapq.heappop(pq)
            if dist[u] != -1:      # already settled with a smaller d
                continue
            dist[u] = d
            if u in target:
                return d
            for v, w in graph[u]:
                if dist[v] == -1:
                    heapq.heappush(pq, (d + w, v))

        return -1
