"""

1377. Frog Position After T Seconds
Hard

Given an undirected tree consisting of n vertices numbered from 1 to n.
A frog starts jumping from vertex 1. In one second, the frog jumps from its
current vertex to another unvisited vertex if they are directly connected.
The frog can not jump back to a visited vertex. In case the frog can jump to
several vertices, it jumps randomly to one of them with the same probability.
Otherwise, when the frog can not jump to any unvisited vertex, it jumps forever
on the same vertex.

The edges of the undirected tree are given in the array edges, where
edges[i] = [ai, bi] means that exists an edge connecting the vertices ai and bi.

Return the probability that after t seconds the frog is on the vertex target.
Answers within 10^-5 of the actual answer will be accepted.


Example 1:

Input: n = 7, edges = [[1,2],[1,3],[1,7],[2,4],[2,6],[3,5]], t = 2, target = 4
Output: 0.16666666666666666
Explanation: The frog starts at vertex 1, jumping with 1/3 probability to the
vertex 2 after second 1 and then jumping with 1/2 probability to vertex 4 after
second 2. Thus the probability for the frog is on the vertex 4 after 2 seconds
is 1/3 * 1/2 = 1/6 = 0.16666666666666666.

Example 2:

Input: n = 7, edges = [[1,2],[1,3],[1,7],[2,4],[2,6],[3,5]], t = 1, target = 7
Output: 0.3333333333333333
Explanation: The frog starts at vertex 1, jumping with 1/3 = 0.3333333333333333
probability to the vertex 7 after second 1.


Constraints:

1 <= n <= 100
edges.length == n - 1
edges[i].length == 2
1 <= ai, bi <= n
1 <= t <= 50
1 <= target <= n

"""

# V0
# IDEA: DFS on the tree, carry the accumulated probability down
#
#  - the tree is rooted at vertex 1, so "unvisited neighbours" == "children"
#  - at a node with c children the probability splits evenly: p / c
#  - we are on `target` at second t only if
#       (a) we arrive exactly when the clock runs out (time == 0), or
#       (b) target is a leaf -> the frog is stuck there forever
#
# time = O(n)
# space = O(n)
from collections import defaultdict
class Solution(object):
    def frogPosition(self, n, edges, t, target):
        g = defaultdict(list)
        for u, v in edges:
            g[u].append(v)
            g[v].append(u)

        visited = [False] * (n + 1)

        def dfs(u, time_left, p):
            # NOTE !!! mark BEFORE collecting children,
            #          so the parent is never counted as a child
            visited[u] = True
            children = [v for v in g[u] if not visited[v]]

            if u == target:
                # arrived on time, or stuck here forever (leaf)
                return p if (time_left == 0 or not children) else 0.0

            # out of time, or dead end -> target not reachable from here
            if time_left == 0 or not children:
                return 0.0

            res = 0.0
            for v in children:
                res += dfs(v, time_left - 1, p / len(children))
            return res

        return dfs(1, t, 1.0)


# V1
# IDEA: BFS level by level (one level == one second)
# time = O(n)
# space = O(n)
from collections import defaultdict, deque
class Solution(object):
    def frogPosition(self, n, edges, t, target):
        g = defaultdict(list)
        for u, v in edges:
            g[u].append(v)
            g[v].append(u)

        q = deque([(1, 1.0)])
        visited = [False] * (n + 1)
        visited[1] = True

        while q and t >= 0:
            for _ in range(len(q)):
                u, p = q.popleft()
                # number of children (root 1 has no parent)
                cnt = len(g[u]) - (0 if u == 1 else 1)
                if u == target:
                    return p if cnt * t == 0 else 0.0
                for v in g[u]:
                    if not visited[v]:
                        visited[v] = True
                        q.append((v, p / cnt))
            t -= 1

        return 0.0
