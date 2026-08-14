"""

1129. Shortest Path with Alternating Colors
Medium

You are given an integer n, the number of nodes in a directed graph where the nodes
are labeled from 0 to n - 1. Each edge is red or blue in this graph, and there could be
self-edges and parallel edges.

You are given two arrays redEdges and blueEdges where:

redEdges[i] = [ai, bi] indicates that there is a directed red edge from node ai to node bi
in the graph, and
blueEdges[j] = [uj, vj] indicates that there is a directed blue edge from node uj to node vj
in the graph.

Return an array answer of length n, where each answer[x] is the length of the shortest path
from node 0 to node x such that the edge colors alternate along the path,
or -1 if such a path does not exist.


Example 1:

Input: n = 3, redEdges = [[0,1],[1,2]], blueEdges = []
Output: [0,1,-1]

Example 2:

Input: n = 3, redEdges = [[0,1]], blueEdges = [[2,1]]
Output: [0,1,-1]


Constraints:

1 <= n <= 100
0 <= redEdges.length, blueEdges.length <= 400
redEdges[i].length == blueEdges[j].length == 2
0 <= ai, bi, uj, vj < n

"""

# V0
# IDEA : BFS on a "doubled" state graph
#        state = (node, color of the edge we JUST used)
#        -> from state (i, c) we may only walk edges of color 1 - c
#        -> start from BOTH (0, 0) and (0, 1) so the 1st edge can be either color
#        -> BFS level by level, the 1st time a node is popped is its shortest distance
# time = O(n + m)
# space = O(n + m)
from collections import deque
class Solution(object):
    def shortestAlternatingPaths(self, n, redEdges, blueEdges):
        # g[0] = red adjacency, g[1] = blue adjacency
        g = [[[] for _ in range(n)], [[] for _ in range(n)]]
        for a, b in redEdges:
            g[0][a].append(b)
        for a, b in blueEdges:
            g[1][a].append(b)

        res = [-1] * n
        # (node, color of edge just used)
        q = deque([(0, 0), (0, 1)])
        visited = set([(0, 0), (0, 1)])
        d = 0
        while q:
            for _ in range(len(q)):
                i, c = q.popleft()
                if res[i] == -1:
                    res[i] = d
                # NOTE !!! flip the color -> next edge must be the other color
                c ^= 1
                for j in g[c][i]:
                    if (j, c) not in visited:
                        visited.add((j, c))
                        q.append((j, c))
            d += 1
        return res
