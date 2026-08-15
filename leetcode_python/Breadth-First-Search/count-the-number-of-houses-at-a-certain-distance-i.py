"""

3015. Count the Number of Houses at a Certain Distance I
Medium

You are given three positive integers n, x, and y.

In a city, there exist houses numbered 1 to n connected by n streets. There is a street connecting the house numbered i with the house numbered i + 1 for all 1 <= i <= n - 1 . An additional street connects the house numbered x with the house numbered y.

For each k, such that 1 <= k <= n, you need to find the number of pairs of houses (house1, house2) such that the minimum number of streets that need to be traveled to reach house2 from house1 is k.

Return a 1-indexed array result of length n where result[k] represents the total number of pairs of houses such that the minimum streets required to reach one house from the other is k.

Note that x and y can be equal.


Example 1:

Input: n = 3, x = 1, y = 3
Output: [6,0,0]
Explanation: Let's look at each pair of houses:
- For the pair (1, 2), we can go from house 1 to house 2 directly.
- For the pair (2, 1), we can go from house 2 to house 1 directly.
- For the pair (1, 3), we can go from house 1 to house 3 directly.
- For the pair (3, 1), we can go from house 3 to house 1 directly.
- For the pair (2, 3), we can go from house 2 to house 3 directly.
- For the pair (3, 2), we can go from house 3 to house 2 directly.

Example 2:

Input: n = 5, x = 2, y = 4
Output: [10,8,2,0,0]
Explanation: For each distance k the pairs are:
- For k == 1, the pairs are (1, 2), (2, 1), (2, 3), (3, 2), (2, 4), (4, 2), (3, 4), (4, 3), (4, 5), and (5, 4).
- For k == 2, the pairs are (1, 3), (3, 1), (1, 4), (4, 1), (2, 5), (5, 2), (3, 5), and (5, 3).
- For k == 3, the pairs are (1, 5) and (5, 1).
- For k == 4 and k == 5, there are no pairs.

Example 3:

Input: n = 4, x = 1, y = 1
Output: [6,4,2,0]
Explanation: For each distance k the pairs are:
- For k == 1, the pairs are (1, 2), (2, 1), (2, 3), (3, 2), (3, 4), and (4, 3).
- For k == 2, the pairs are (1, 3), (3, 1), (2, 4), and (4, 2).
- For k == 3, the pairs are (1, 4), and (4, 1).
- For k == 4, there are no pairs.


Constraints:

2 <= n <= 100
1 <= x, y <= n

"""

# V0
# IDEA : n <= 100 — JUST BFS FROM EVERY HOUSE
#
#   the graph is a path plus one extra edge, and all edges cost 1, so a BFS
#   from each house gives its shortest distance to every other house in O(n).
#
#   the answer counts ORDERED pairs, so every BFS result is tallied as is —
#   the pair (u, v) and (v, u) are both counted, once from each source.
#
#   NOTE : x == y (or |x - y| == 1) makes the extra street a duplicate, and
#          BFS handles that with no special case.
#
# time = O(n^2), space = O(n)
from collections import deque


class Solution(object):
    def countOfPairs(self, n, x, y):
        adj = [[] for _ in range(n + 1)]
        for i in range(1, n):
            adj[i].append(i + 1)
            adj[i + 1].append(i)
        if x != y:
            adj[x].append(y)
            adj[y].append(x)

        res = [0] * (n + 1)
        for src in range(1, n + 1):
            dist = [-1] * (n + 1)
            dist[src] = 0
            q = deque([src])
            while q:
                u = q.popleft()
                for v in adj[u]:
                    if dist[v] == -1:
                        dist[v] = dist[u] + 1
                        res[dist[v]] += 1
                        q.append(v)
        return res[1:]
