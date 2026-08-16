"""

3620. Network Recovery Pathways
Hard

You are given a directed acyclic graph of n nodes numbered from 0 to n - 1.
This is represented by a 2D array edges of length m, where
edges[i] = [ui, vi, costi] indicates a one-way communication from node ui to
node vi with a recovery cost of costi.

Some nodes may be offline. You are given a boolean array online where
online[i] = true means node i is online. Nodes 0 and n - 1 are always online.

A path from 0 to n - 1 is valid if:

All intermediate nodes on the path are online.
The total recovery cost of all edges on the path does not exceed k.

For each valid path, define its score as the minimum edge-cost along that path.

Return the maximum path score (i.e., the largest minimum-edge cost) among all
valid paths. If no valid path exists, return -1.


Example 1:

Input: edges = [[0,1,5],[1,3,10],[0,2,3],[2,3,4]],
       online = [true,true,true,true], k = 10
Output: 3
Explanation:
The graph has two possible routes from node 0 to node 3:
1. Path 0 -> 1 -> 3
   Total cost = 5 + 10 = 15, which exceeds k (15 > 10), so this path is
   invalid.
2. Path 0 -> 2 -> 3
   Total cost = 3 + 4 = 7 <= k, so this path is valid.
   The minimum edge-cost along this path is min(3, 4) = 3.
There are no other valid paths. Hence, the maximum among all valid path-scores
is 3.

Example 2:

Input: edges = [[0,1,7],[1,4,5],[0,2,6],[2,3,6],[3,4,2],[2,4,6]],
       online = [true,true,true,false,true], k = 12
Output: 6
Explanation:
Node 3 is offline, so any path passing through 3 is invalid.
Consider the remaining routes from 0 to 4:
1. Path 0 -> 1 -> 4
   Total cost = 7 + 5 = 12 <= k, so this path is valid.
   The minimum edge-cost along this path is min(7, 5) = 5.
2. Path 0 -> 2 -> 3 -> 4
   Node 3 is offline, so this path is invalid regardless of cost.
3. Path 0 -> 2 -> 4
   Total cost = 6 + 6 = 12 <= k, so this path is valid.
   The minimum edge-cost along this path is min(6, 6) = 6.
Among the two valid paths, their scores are 5 and 6. Therefore, the answer
is 6.


Constraints:

n == online.length
2 <= n <= 5 * 10^4
0 <= m == edges.length <= min(10^5, n * (n - 1) / 2)
edges[i] = [ui, vi, costi]
0 <= ui, vi < n
ui != vi
0 <= costi <= 10^9
0 <= k <= 5 * 10^13
online[i] is either true or false, and both online[0] and online[n - 1] are
true.
The given graph is a directed acyclic graph.

"""

# V0
# IDEA : BINARY SEARCH THE SCORE + CHEAPEST PATH ON THE DAG
#
#   the two requirements pull in opposite directions: the score wants big
#   edges, the budget wants a small total. that is the classic signal to fix
#   the score and only check feasibility.
#
#   say we demand a score of at least `mid`. then every edge cheaper than
#   `mid` is simply unusable, and what is left is a plain question — is the
#   cheapest 0 -> n-1 path in the surviving graph within budget k? if yes,
#   that path's minimum edge is >= mid, so the answer is >= mid. if the
#   cheapest path already blows the budget, no other path can fit either.
#   so `feasible(mid)` is exactly "answer >= mid", which is monotone, and a
#   binary search over the distinct edge costs lands on the answer.
#
#   the graph is a DAG, so the cheapest path needs no dijkstra: relax the
#   edges once in topological order and every node is finalised when it is
#   reached. the topological order does not depend on `mid`, so it is built
#   once outside the search and each check costs a single O(n + m) sweep.
#
#   offline nodes are dropped up front by deleting every edge touching one —
#   that is enough, because a node only ever appears on a path as an
#   endpoint of some edge, and nodes 0 and n-1 are guaranteed online.
#
# time = O((n + m) * log m), space = O(n + m)
class Solution(object):
    def findMaxPathScore(self, edges, online, k):
        n = len(online)

        adj = [[] for _ in range(n)]
        indeg = [0] * n
        costs = set()
        for u, v, w in edges:
            if not online[u] or not online[v]:
                continue
            adj[u].append((v, w))
            indeg[v] += 1
            costs.add(w)

        if not costs:
            return -1

        # topological order of the surviving DAG (built once)
        order = []
        stack = [i for i in range(n) if indeg[i] == 0]
        while stack:
            u = stack.pop()
            order.append(u)
            for v, _ in adj[u]:
                indeg[v] -= 1
                if indeg[v] == 0:
                    stack.append(v)

        INF = float('inf')
        target = n - 1

        def feasible(mid):
            dist = [INF] * n
            dist[0] = 0
            for u in order:
                du = dist[u]
                if du > k:
                    continue
                for v, w in adj[u]:
                    if w >= mid and du + w < dist[v]:
                        dist[v] = du + w
            return dist[target] <= k

        cand = sorted(costs)
        if not feasible(cand[0]):
            return -1

        lo, hi = 0, len(cand) - 1
        while lo < hi:
            mid = (lo + hi + 1) // 2
            if feasible(cand[mid]):
                lo = mid
            else:
                hi = mid - 1
        return cand[lo]
