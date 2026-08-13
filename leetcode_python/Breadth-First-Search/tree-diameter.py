"""

1245. Tree Diameter
Medium

The diameter of a tree is the number of edges in the longest path in that tree.

There is an undirected tree of n nodes labeled from 0 to n - 1. You are given a 2D array edges where edges.length == n - 1 and edges[i] = [ai, bi] indicates that there is an undirected edge between nodes ai and bi in the tree.

Return the diameter of the tree.


Example 1:

Input: edges = [[0,1],[0,2]]
Output: 2
Explanation: The longest path of the tree is the path 1 - 0 - 2.

Example 2:

Input: edges = [[0,1],[1,2],[2,3],[1,4],[4,5]]
Output: 4
Explanation: The longest path of the tree is the path 3 - 2 - 1 - 4 - 5.


Constraints:

n == edges.length + 1
1 <= n <= 10^4
0 <= ai, bi < n
ai != bi

"""

# V0
# IDEA : DOUBLE BFS (two-sweep)
"""
 Classic trick:
   1) BFS from ANY node -> the farthest node `a` is an endpoint of some diameter
   2) BFS from `a`      -> the farthest distance IS the diameter

 BFS (not DFS) is used on purpose: n can be 10^4 and the tree
 can be a path, so recursion would blow the Python stack.
"""
# time = O(n)
# space = O(n)
from collections import deque
class Solution(object):
    def treeDiameter(self, edges):
        n = len(edges) + 1
        g = [[] for _ in range(n)]
        for a, b in edges:
            g[a].append(b)
            g[b].append(a)

        def bfs(src):
            # return (farthest node, its distance from src)
            dist = [-1] * n
            dist[src] = 0
            q = deque([src])
            far, far_d = src, 0
            while q:
                cur = q.popleft()
                if dist[cur] > far_d:
                    far, far_d = cur, dist[cur]
                for nxt in g[cur]:
                    if dist[nxt] == -1:
                        dist[nxt] = dist[cur] + 1
                        q.append(nxt)
            return far, far_d

        a, _ = bfs(0)
        _, d = bfs(a)
        return d


# V1
# IDEA : ONE-PASS DFS (longest + 2nd longest branch at each node)
# time = O(n)
# space = O(n)
# NOTE: recursive - can hit the recursion limit on a long path tree
class Solution(object):
    def treeDiameter(self, edges):
        n = len(edges) + 1
        g = [[] for _ in range(n)]
        for a, b in edges:
            g[a].append(b)
            g[b].append(a)

        self.res = 0

        def depth(node, parent):
            # top 2 child depths -> their sum is the best path through `node`
            first = second = 0
            for nxt in g[node]:
                if nxt == parent:
                    continue
                d = depth(nxt, node) + 1
                if d > first:
                    first, second = d, first
                elif d > second:
                    second = d
            self.res = max(self.res, first + second)
            return first

        depth(0, -1)
        return self.res
