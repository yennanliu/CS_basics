"""

3604. Minimum Time to Reach Destination in Directed Graph
Medium

You are given an integer n and a directed graph with n nodes labeled from 0
to n - 1. This is represented by a 2D array edges, where
edges[i] = [ui, vi, starti, endi] indicates an edge from node ui to vi that
can only be used at any integer time t such that starti <= t <= endi.

You start at node 0 at time 0.

In one unit of time, you can either:

Wait at your current node without moving, or
Travel along an outgoing edge from your current node if the current time t
satisfies starti <= t <= endi.

Return the minimum time required to reach node n - 1. If it is impossible,
return -1.


Example 1:

Input: n = 3, edges = [[0,1,0,1],[1,2,2,5]]
Output: 3
Explanation:
The optimal path is:
At time t = 0, take the edge (0 -> 1) which is available from 0 to 1. You
arrive at node 1 at time t = 1, then wait until t = 2.
At time t = 2, take the edge (1 -> 2) which is available from 2 to 5. You
arrive at node 2 at time 3.
Hence, the minimum time to reach node 2 is 3.

Example 2:

Input: n = 4, edges = [[0,1,0,3],[1,3,7,8],[0,2,1,5],[2,3,4,7]]
Output: 5
Explanation:
The optimal path is:
Wait at node 0 until time t = 1, then take the edge (0 -> 2) which is
available from 1 to 5. You arrive at node 2 at t = 2.
Wait at node 2 until time t = 4, then take the edge (2 -> 3) which is
available from 4 to 7. You arrive at node 3 at t = 5.
Hence, the minimum time to reach node 3 is 5.

Example 3:

Input: n = 3, edges = [[1,0,1,3],[1,2,3,5]]
Output: -1
Explanation:
Since there is no outgoing edge from node 0, it is impossible to reach node
2. Hence, the output is -1.


Constraints:

1 <= n <= 10^5
0 <= edges.length <= 10^5
edges[i] == [ui, vi, starti, endi]
0 <= ui, vi <= n - 1
ui != vi
0 <= starti <= endi <= 10^9

"""

# V0
# IDEA : DIJKSTRA ON EARLIEST ARRIVAL TIME
#
#   the key observation is that arriving at a node earlier is never worse.
#   waiting is always allowed and free, so any plan that works from node u
#   at time t also works from u at any time t' < t — you just idle until t.
#   that makes "earliest arrival time" a well-defined label per node, and
#   an optimal answer is built from optimal sub-answers.
#
#   with that, the relaxation for an edge (u -> v) usable in [s, e] from
#   arrival time t at u is: the earliest moment we may *depart* is
#   max(t, s), which is legal only while it stays <= e; departing then
#   lands us at v at max(t, s) + 1. this is a monotone, non-decreasing
#   edge rule (the new label is always > the old one), which is exactly
#   the condition dijkstra needs — so a min-heap over arrival times pops
#   each node with its final value.
#
#   note the availability window makes the effective edge weight depend on
#   the current time, so a plain BFS or a fixed-weight shortest path will
#   not do; but since waiting only ever costs time and never unlocks
#   anything an earlier departure could not reach, greedy still holds.
#
# time = O((V + E) * log(V)), space = O(V + E)
class Solution(object):
    def minTime(self, n, edges):
        import heapq

        g = [[] for _ in range(n)]
        for u, v, s, e in edges:
            g[u].append((v, s, e))

        INF = float("inf")
        dist = [INF] * n
        dist[0] = 0
        pq = [(0, 0)]
        while pq:
            t, u = heapq.heappop(pq)
            if t > dist[u]:
                continue
            if u == n - 1:
                return t
            for v, s, e in g[u]:
                if t > e:
                    continue
                nt = (s if s > t else t) + 1
                if nt < dist[v]:
                    dist[v] = nt
                    heapq.heappush(pq, (nt, v))

        return -1
