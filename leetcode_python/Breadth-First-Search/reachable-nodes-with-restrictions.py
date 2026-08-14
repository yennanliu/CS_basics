"""

2368. Reachable Nodes With Restrictions
Medium

There is an undirected tree with n nodes labeled from 0 to n - 1 and n - 1 edges.

You are given a 2D integer array edges of length n - 1 where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the tree. You are also given an integer array restricted which represents restricted nodes.

Return the maximum number of nodes you can reach from node 0 without visiting a restricted node.

Note that node 0 will not be a restricted node.


Example 1:

Input: n = 7, edges = [[0,1],[1,2],[3,1],[4,0],[0,5],[5,6]], restricted = [4,5]
Output: 4
Explanation: The diagram above shows the tree.
We have that [0,1,2,3] is the only possible set of nodes you can reach from 0 without visiting a restricted node.

Example 2:

Input: n = 7, edges = [[0,1],[0,2],[0,5],[0,4],[3,2],[6,5]], restricted = [4,2,1]
Output: 3
Explanation: The diagram above shows the tree.
We have that [0,5,6] is the only possible set of nodes you can reach from 0 without visiting a restricted node.


Constraints:

2 <= n <= 10^5
edges.length == n - 1
edges[i].length == 2
0 <= ai, bi < n
ai != bi
edges represents a valid tree.
1 <= restricted.length < n
1 <= restricted[i] < n
All the values of restricted are unique.

"""

# V0
# IDEA : BFS FROM NODE 0, TREATING RESTRICTED NODES AS WALLS
#
#   put the restricted labels in a set and simply never enqueue them. the
#   graph is a tree, so no node is reachable "around" a blocked one — the
#   blocked node's whole branch is cut off automatically.
#
#   the answer is the size of the visited set.
#
# time = O(n), space = O(n)
from collections import deque, defaultdict


class Solution(object):
    def reachableNodes(self, n, edges, restricted):
        blocked = set(restricted)
        g = defaultdict(list)
        for a, b in edges:
            g[a].append(b)
            g[b].append(a)

        seen = [False] * n
        seen[0] = True
        q = deque([0])
        res = 0
        while q:
            u = q.popleft()
            res += 1
            for v in g[u]:
                if not seen[v] and v not in blocked:
                    seen[v] = True
                    q.append(v)
        return res
