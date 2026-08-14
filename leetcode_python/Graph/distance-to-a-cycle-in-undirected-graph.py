"""

2204. Distance to a Cycle in Undirected Graph
Hard
(premium / locked problem)

You are given a positive integer n representing the number of nodes in a connected undirected graph containing exactly one cycle. The nodes are numbered from 0 to n - 1 (inclusive).

You are also given a 2D integer array edges, where edges[i] = [node1_i, node2_i] denotes that there is a bidirectional edge connecting node1_i and node2_i in the graph.

The distance between two nodes a and b is defined to be the minimum number of edges that are needed to go from a to b.

Return an integer array answer of size n, where answer[i] is the minimum distance between the ith node and any node in the cycle.


Example 1:

Input: n = 7, edges = [[1,2],[2,4],[4,3],[3,1],[0,1],[5,2],[6,5]]
Output: [1,0,0,0,0,1,2]
Explanation:
The nodes 1, 2, 3, and 4 form the cycle.
The distance from 0 to 1 is 1.
The distance from 1 to 1 is 0.
The distance from 2 to 2 is 0.
The distance from 3 to 3 is 0.
The distance from 4 to 4 is 0.
The distance from 5 to 2 is 1.
The distance from 6 to 2 is 2.

Example 2:

Input: n = 9, edges = [[0,1],[1,2],[0,2],[2,6],[6,7],[6,8],[0,3],[3,4],[3,5]]
Output: [0,0,0,1,2,2,1,2,2]
Explanation:
The nodes 0, 1, and 2 form the cycle.
The distance from 0 to 0 is 0.
The distance from 1 to 1 is 0.
The distance from 2 to 2 is 0.
The distance from 3 to 2 is 1.
The distance from 4 to 2 is 2.
The distance from 5 to 2 is 2.
The distance from 6 to 2 is 1.
The distance from 7 to 2 is 2.
The distance from 8 to 2 is 2.


Constraints:

3 <= n <= 10^5
edges.length == n
edges[i].length == 2
0 <= node1_i, node2_i <= n - 1
node1_i != node2_i
The graph is connected.
The graph has exactly one cycle.
There is at most one edge between any pair of nodes.

"""

# V0
# IDEA : PEEL THE TREES OFF (Kahn on degree 1), THEN MULTI-SOURCE BFS
#
#   with exactly n nodes and n edges the graph is a single cycle with trees
#   hanging off it. repeatedly deleting every degree-1 node strips those
#   trees away and leaves precisely the cycle — nothing on a cycle ever drops
#   below degree 2.
#
#   then run a BFS seeded with ALL cycle nodes at distance 0; each remaining
#   node gets its distance to the nearest cycle node in one sweep.
#
# time = O(V + E), space = O(V + E)
from collections import deque, defaultdict


class Solution(object):
    def distanceToCycle(self, n, edges):
        g = defaultdict(set)
        for u, v in edges:
            g[u].add(v)
            g[v].add(u)

        # strip the trees : repeatedly remove degree-1 nodes
        degree = [len(g[i]) for i in range(n)]
        removed = [False] * n
        q = deque(i for i in range(n) if degree[i] == 1)
        while q:
            u = q.popleft()
            removed[u] = True
            for v in g[u]:
                if not removed[v]:
                    degree[v] -= 1
                    if degree[v] == 1:
                        q.append(v)

        # multi-source BFS from every surviving (cycle) node
        res = [-1] * n
        q = deque()
        for i in range(n):
            if not removed[i]:
                res[i] = 0
                q.append(i)
        while q:
            u = q.popleft()
            for v in g[u]:
                if res[v] == -1:
                    res[v] = res[u] + 1
                    q.append(v)
        return res
