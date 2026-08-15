"""

2714. Find Shortest Path with K Hops
Hard

You are given a positive integer n which is the number of nodes of a 0-indexed undirected weighted connected graph and a 0-indexed 2D array edges where edges[i] = [ui, vi, wi] indicates that there is an edge between nodes ui and vi with weight wi.

You are also given two nodes s and d, and a positive integer k, your task is to find the shortest path from s to d, but you can hop over at most k edges. In other words, make the weight of at most k edges 0 and then find the shortest path from s to d.

Return the length of the shortest path from s to d with the given condition.


Example 1:

Input: n = 4, edges = [[0,1,4],[0,2,2],[2,3,6]], s = 1, d = 3, k = 2
Output: 2
Explanation: In this example there is only one path from node 1 (the green node) to node 3 (the red node), which is (1->0->2->3) and the length of it is 4 + 2 + 6 = 12. Now we can make weight of two edges 0, we make weight of the blue edges 0, then we have 0 + 2 + 0 = 2. It can be shown that 2 is the minimum length of a path we can achieve with the given condition.

Example 2:

Input: n = 7, edges = [[3,1,9],[3,2,4],[4,0,9],[0,5,6],[3,6,2],[6,0,4],[1,2,4]], s = 4, d = 1, k = 2
Output: 6
Explanation: In this example there are 2 paths from node 4 (the green node) to node 1 (the red node), which are (4->0->6->3->2->1) and (4->0->6->3->1). The first one has the length 9 + 4 + 2 + 4 + 4 = 23, and the second one has the length 9 + 4 + 2 + 9 = 24. Now if we make weight of the blue edges 0, we get the shortest path with the length 0 + 4 + 2 + 0 = 6. It can be shown that 6 is the minimum length of a path we can achieve with the given condition.

Example 3:

Input: n = 5, edges = [[0,4,2],[0,1,3],[0,2,1],[2,1,4],[1,3,4],[3,4,7]], s = 2, d = 3, k = 1
Output: 3
Explanation: In this example there are 4 paths from node 2 (the green node) to node 3 (the red node), which are (2->1->3), (2->0->1->3), (2->1->0->4->3) and (2->0->4->3). The first two have the length 4 + 4 = 1 + 3 + 4 = 8, the third one has the length 4 + 3 + 2 + 7 = 16 and the last one has the length 1 + 2 + 7 = 10. Now if we make weight of the blue edge 0, we get the shortest path with the length 1 + 2 + 0 = 3. It can be shown that 3 is the minimum length of a path we can achieve with the given condition.


Constraints:

2 <= n <= 500
n - 1 <= edges.length <= min(10^4, n * (n - 1) / 2)
edges[i].length = 3
0 <= edges[i][0], edges[i][1] <= n - 1
1 <= edges[i][2] <= 10^6
0 <= s, d, k <= n - 1
s != d
The input is generated such that the graph is connected and has no repeated edges or self-loops

"""

import heapq

# V0
# IDEA : DIJKSTRA on the LAYERED STATE GRAPH (node, #hops used so far)
#
#   the choice "which edges do I zero out" cannot be made up front, so we fold
#   it into the state: dist[u][t] = cheapest way to reach node u having already
#   spent t of the k free hops. From a state (u, t) each incident edge (u,v,w)
#   offers two transitions:
#
#       pay the edge  ->  (v, t)     with cost dis + w
#       hop the edge  ->  (v, t + 1) with cost dis      (only if t < k)
#
#   all transition costs are >= 0, so plain Dijkstra over the n * (k + 1)
#   states is correct. The answer is min(dist[d][0..k]).
#
#   NOTE : spending FEWER hops is never forced to be better or worse, so we
#          must take the min over ALL t at the destination, not just t == k.
#   NOTE : the heap can hold stale entries (a state re-pushed with a smaller
#          key); skip a popped entry whose distance no longer matches the best
#          known one, otherwise each state gets expanded many times.
#   NOTE : the graph is UNDIRECTED - insert both directions.
#
# time = O(E * k * log(V * k)), space = O(n * k)
class Solution(object):
    def shortestPathWithHops(self, n, edges, s, d, k):
        g = [[] for _ in range(n)]
        for u, v, w in edges:
            g[u].append((v, w))
            g[v].append((u, w))

        INF = float('inf')
        dist = [[INF] * (k + 1) for _ in range(n)]
        dist[s][0] = 0
        pq = [(0, s, 0)]
        while pq:
            dis, u, t = heapq.heappop(pq)
            if dis > dist[u][t]:
                continue          # stale heap entry
            if u == d:
                return dis        # first pop of d is already optimal
            for v, w in g[u]:
                if dis + w < dist[v][t]:
                    dist[v][t] = dis + w
                    heapq.heappush(pq, (dis + w, v, t))
                if t < k and dis < dist[v][t + 1]:
                    dist[v][t + 1] = dis
                    heapq.heappush(pq, (dis, v, t + 1))
        return min(dist[d])
