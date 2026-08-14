"""

2093. Minimum Cost to Reach City With Discounts
Medium
(premium / locked problem)

A series of highways connect n cities numbered from 0 to n - 1. You are given a 2D integer array highways where highways[i] = [city1_i, city2_i, toll_i] indicates that there is a highway that connects city1_i and city2_i, allowing a car to go from city1_i to city2_i and vice versa for a cost of toll_i.

You are also given an integer discounts which represents the number of discounts you have. You can use a discount to travel across the ith highway for a cost of toll_i / 2 (integer division). Each discount may only be used once, and you can only use at most one discount per highway.

Return the minimum total cost to go from city 0 to city n - 1, or -1 if it is not possible to go from city 0 to city n - 1.


Example 1:

Input: n = 5, highways = [[0,1,4],[2,1,3],[1,4,11],[3,2,3],[3,4,2]], discounts = 1
Output: 9
Explanation:
Go from 0 to 1 for a cost of 4.
Go from 1 to 4 and use a discount for a cost of 11 / 2 = 5.
The minimum cost to go from 0 to 4 is 4 + 5 = 9.

Example 2:

Input: n = 4, highways = [[1,3,17],[1,2,7],[3,2,5],[0,1,6],[3,0,20]], discounts = 20
Output: 8
Explanation:
Go from 0 to 1 and use a discount for a cost of 6 / 2 = 3.
Go from 1 to 2 and use a discount for a cost of 7 / 2 = 3.
The minimum cost to go from 0 to 2 is 3 + 3 = 6.

Example 3:

Input: n = 4, highways = [[0,1,3],[2,3,2]], discounts = 0
Output: -1
Explanation:
It is impossible to go from 0 to 3 so return -1.


Constraints:

2 <= n <= 1000
1 <= highways.length <= 1000
highways[i].length == 3
0 <= city1_i, city2_i <= n - 1
city1_i != city2_i
0 <= toll_i <= 10^5
0 <= discounts <= 500
There are no duplicate highways.

"""

# V0
# IDEA : DIJKSTRA ON THE EXPANDED STATE (city, discounts already used)
#
#   the state is not just "where am I" but "where am I AND how many discounts
#   have I burned". that graph has n * (discounts + 1) nodes, and from
#   (u, used) each highway offers two moves :
#       (v, used)      paying toll
#       (v, used + 1)  paying toll // 2   — only while used < discounts
#
#   run a plain Dijkstra over that; the first time the heap pops any state
#   with city == n - 1, that distance is the answer.
#
# time = O(E * D * log(V * D)), space = O(V * D)
import heapq
from collections import defaultdict


class Solution(object):
    def minimumCost(self, n, highways, discounts):
        g = defaultdict(list)
        for u, v, w in highways:
            g[u].append((v, w))
            g[v].append((u, w))

        INF = float('inf')
        best = [[INF] * (discounts + 1) for _ in range(n)]
        best[0][0] = 0
        pq = [(0, 0, 0)]   # (cost, city, discounts used)
        while pq:
            cost, u, used = heapq.heappop(pq)
            if u == n - 1:
                return cost
            if cost > best[u][used]:
                continue
            for v, w in g[u]:
                if cost + w < best[v][used]:
                    best[v][used] = cost + w
                    heapq.heappush(pq, (cost + w, v, used))
                if used < discounts and cost + w // 2 < best[v][used + 1]:
                    best[v][used + 1] = cost + w // 2
                    heapq.heappush(pq, (cost + w // 2, v, used + 1))
        return -1
