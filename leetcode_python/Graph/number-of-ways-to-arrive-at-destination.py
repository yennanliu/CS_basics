"""

1976. Number of Ways to Arrive at Destination
Medium

You are in a city that consists of n intersections numbered from 0 to n - 1 with bi-directional roads between some intersections. The inputs are generated such that you can reach any intersection from any other intersection and that there is at most one road between any two intersections.

You are given an integer n and a 2D integer array roads where roads[i] = [ui, vi, timei] means that there is a road between intersections ui and vi that takes timei minutes to travel. You want to know in how many ways you can travel from intersection 0 to intersection n - 1 in the shortest amount of time.

Return the number of ways you can arrive at your destination in the shortest amount of time. Since the answer may be large, return it modulo 10^9 + 7.


Example 1:

Input: n = 7, roads = [[0,6,7],[0,1,2],[1,2,3],[1,3,3],[6,3,3],[3,5,1],[6,5,1],[2,5,1],[0,4,5],[4,6,2]]
Output: 4
Explanation: The shortest amount of time it takes to go from intersection 0 to intersection 6 is 7 minutes.
The four ways to get there in 7 minutes are:
- 0 -> 6
- 0 -> 4 -> 6
- 0 -> 1 -> 2 -> 5 -> 6
- 0 -> 1 -> 3 -> 5 -> 6

Example 2:

Input: n = 2, roads = [[1,0,10]]
Output: 1
Explanation: There is only one way to go from intersection 0 to intersection 1, and it takes 10 minutes.


Constraints:

1 <= n <= 200
n - 1 <= roads.length <= n * (n - 1) / 2
roads[i].length == 3
0 <= ui, vi <= n - 1
1 <= timei <= 10^9
ui != vi
There is at most one road connecting any two intersections.
You can reach any intersection from any other intersection.

"""

# V0
# IDEA : DIJKSTRA + PATH COUNTING
#
#   run Dijkstra from node 0 while carrying a second array cnt[]:
#     - strictly shorter route found -> dist[v] = nd ; cnt[v] = cnt[u]
#     - equally short route found    -> cnt[v] += cnt[u]
#
#   this is valid because every weight is >= 1 (strictly positive), so any
#   predecessor u on a shortest path to v has dist[u] < dist[v] and is
#   therefore settled (its cnt is final) before v is ever popped.
#
#   NOTE : keep dist un-modded but reduce cnt mod 1e9+7.
#
# time = O(E log V), space = O(V + E)
import heapq
from collections import defaultdict
class Solution(object):
    def countPaths(self, n, roads):
        MOD = 10 ** 9 + 7
        g = defaultdict(list)
        for u, v, t in roads:
            g[u].append((v, t))
            g[v].append((u, t))

        INF = float("inf")
        dist = [INF] * n
        cnt = [0] * n
        dist[0] = 0
        cnt[0] = 1

        pq = [(0, 0)]
        while pq:
            d, u = heapq.heappop(pq)
            if d > dist[u]:
                continue            # stale entry
            for v, w in g[u]:
                nd = d + w
                if nd < dist[v]:
                    dist[v] = nd
                    cnt[v] = cnt[u]
                    heapq.heappush(pq, (nd, v))
                elif nd == dist[v]:
                    cnt[v] = (cnt[v] + cnt[u]) % MOD
        return cnt[n - 1] % MOD
