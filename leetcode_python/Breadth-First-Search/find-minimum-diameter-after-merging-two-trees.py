"""

3203. Find Minimum Diameter After Merging Two Trees
Hard

There exist two undirected trees with n and m nodes, numbered from 0 to n - 1 and from 0 to m - 1, respectively. You are given two 2D integer arrays edges1 and edges2 of lengths n - 1 and m - 1, respectively, where edges1[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the first tree and edges2[i] = [ui, vi] indicates that there is an edge between nodes ui and vi in the second tree.

You must connect one node from the first tree with another node from the second tree with an edge.

Return the minimum possible diameter of the resulting tree.

The diameter of a tree is the length of the longest path between any two nodes in the tree.


Example 1:

Input: edges1 = [[0,1],[0,2],[0,3]], edges2 = [[0,1]]
Output: 3
Explanation:
We can obtain a tree of diameter 3 by connecting node 0 from the first tree with any node from the second tree.

Example 2:

Input: edges1 = [[0,1],[0,2],[0,3],[2,4],[2,5],[3,6],[2,7]], edges2 = [[0,1],[0,2],[0,3],[2,4],[2,5],[3,6],[2,7]]
Output: 5
Explanation:
We can obtain a tree of diameter 5 by connecting node 0 from the first tree with node 0 from the second tree.


Constraints:

1 <= n, m <= 10^5
edges1.length == n - 1
edges2.length == m - 1
edges1[i].length == edges2[i].length == 2
edges1[i] = [ai, bi]
0 <= ai, bi < n
edges2[i] = [ui, vi]
0 <= ui, vi < m
The input is generated such that edges1 and edges2 represent valid trees.

"""

# V0
# IDEA : JOIN THE TWO *CENTRES* — THE ANSWER IS A MAX OF THREE TERMS
#
#   the merged tree's longest path is one of :
#       a path living entirely in tree 1        -> d1
#       a path living entirely in tree 2        -> d2
#       a path crossing the new edge            -> ecc1 + 1 + ecc2
#   where ecc is the largest distance from the chosen endpoint to anywhere in
#   its own tree.
#
#   we get to choose the endpoints, and the best possible eccentricity in a
#   tree is its RADIUS, ceil(d / 2), attained at the centre. so
#
#       answer = max(d1, d2, ceil(d1/2) + ceil(d2/2) + 1)
#
#   and the diameters come from the classic double BFS : farthest node from
#   any start, then farthest from that node.
#
# time = O(n + m), space = O(n + m)
from collections import deque


class Solution(object):
    def minimumDiameterAfterMerge(self, edges1, edges2):

        def diameter(edges):
            n = len(edges) + 1
            if n == 1:
                return 0
            adj = [[] for _ in range(n)]
            for a, b in edges:
                adj[a].append(b)
                adj[b].append(a)

            def bfs(src):
                dist = [-1] * n
                dist[src] = 0
                q = deque([src])
                far = src
                while q:
                    u = q.popleft()
                    if dist[u] > dist[far]:
                        far = u
                    for v in adj[u]:
                        if dist[v] == -1:
                            dist[v] = dist[u] + 1
                            q.append(v)
                return far, dist[far]

            a, _ = bfs(0)
            _, d = bfs(a)
            return d

        d1 = diameter(edges1)
        d2 = diameter(edges2)
        cross = (d1 + 1) // 2 + (d2 + 1) // 2 + 1
        return max(d1, d2, cross)
