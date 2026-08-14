"""

2493. Divide Nodes Into the Maximum Number of Groups
Hard

You are given a positive integer n representing the number of nodes in an undirected graph. The nodes are labeled from 1 to n.

You are also given a 2D integer array edges, where edges[i] = [ai, bi] indicates that there is a bidirectional edge between nodes ai and bi. Notice that the given graph may be disconnected.

Divide the nodes of the graph into m groups (1-indexed) such that:

Each node in the graph belongs to exactly one group.
For each pair of nodes in the graph that are connected by an edge [ai, bi], if ai belongs to the group with index x, and bi belongs to the group with index y, then |y - x| = 1.

Return the maximum number of groups (i.e., maximum m) into which you can divide the nodes. Return -1 if it is impossible to group the nodes with the given conditions.


Example 1:

Input: n = 6, edges = [[1,2],[1,4],[1,5],[2,6],[2,3],[4,6]]
Output: 4
Explanation: As shown in the image we:
- Add node 5 to the first group.
- Add node 1 to the second group.
- Add nodes 2 and 4 to the third group.
- Add nodes 3 and 6 to the fourth group.
We can see that every edge is satisfied.
It can be shown that that if we create a fifth group and move any node from the third or fourth group to it, at least on of the edges will not be satisfied.

Example 2:

Input: n = 3, edges = [[1,2],[2,3],[3,1]]
Output: -1
Explanation: If we add node 1 to the first group, node 2 to the second group, and node 3 to the third group to satisfy the first two edges, we can see that the third edge will not be satisfied.
It is not possible to assign the nodes to groups in such a way that satisfies all the edges.


Constraints:

1 <= n <= 500
1 <= edges.length <= 10^4
edges[i].length == 2
1 <= ai, bi <= n
ai != bi
There is at most one edge between any pair of vertices.

"""

# V0
# IDEA : THE GROUPING IS A BFS LAYERING — TRY EVERY START, PER COMPONENT
#
#   assigning groups so that adjacent nodes differ by exactly 1 means the
#   group index is a BFS DEPTH from whichever node lands in group 1. so for a
#   given start, the number of groups is that BFS's layer count, and the best
#   for a component is the maximum over all its possible starts.
#
#   the layering only exists if the graph is BIPARTITE : an edge joining two
#   nodes of the SAME depth is a contradiction (an odd cycle), so any such
#   edge means -1 for the whole graph.
#
#   components are independent — their group numbering can be shifted freely
#   — so the answer is the SUM of the per-component maxima.
#
#   n <= 500, so running a BFS from all n nodes is cheap.
#
# time = O(V * (V + E)), space = O(V + E)
from collections import deque, defaultdict


class Solution(object):
    def magnificentSets(self, n, edges):
        g = defaultdict(list)
        for a, b in edges:
            g[a].append(b)
            g[b].append(a)

        def bfs(src):
            """layer count starting at src, or -1 if an odd cycle shows up"""
            depth = {src: 1}
            q = deque([src])
            best = 1
            while q:
                u = q.popleft()
                for v in g[u]:
                    if v not in depth:
                        depth[v] = depth[u] + 1
                        best = max(best, depth[v])
                        q.append(v)
                    elif depth[v] == depth[u]:
                        return -1, depth
            return best, depth

        # label the components
        comp = [0] * (n + 1)
        cid = 0
        for start in range(1, n + 1):
            if comp[start]:
                continue
            cid += 1
            comp[start] = cid
            stack = [start]
            while stack:
                u = stack.pop()
                for v in g[u]:
                    if not comp[v]:
                        comp[v] = cid
                        stack.append(v)

        best_of = defaultdict(int)
        for src in range(1, n + 1):
            layers, _ = bfs(src)
            if layers == -1:
                return -1
            best_of[comp[src]] = max(best_of[comp[src]], layers)

        return sum(best_of.values())
