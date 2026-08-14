"""

1857. Largest Color Value in a Directed Graph
Hard

There is a directed graph of n colored nodes and m edges. The nodes are numbered from 0 to n - 1.

You are given a string colors where colors[i] is a lowercase English letter representing the color of the ith node in this graph (0-indexed). You are also given a 2D array edges where edges[j] = [aj, bj] indicates that there is a directed edge from node aj to node bj.

A valid path in the graph is a sequence of nodes x1 -> x2 -> x3 -> ... -> xk such that there is a directed edge from xi to xi+1 for every 1 <= i < k. The color value of the path is the number of nodes that are colored the most frequently occurring color along that path.

Return the largest color value of any valid path in the given graph, or -1 if the graph contains a cycle.


Example 1:

Input: colors = "abaca", edges = [[0,1],[0,2],[2,3],[3,4]]
Output: 3
Explanation: The path 0 -> 2 -> 3 -> 4 contains 3 nodes that are colored "a".

Example 2:

Input: colors = "a", edges = [[0,0]]
Output: -1
Explanation: There is a cycle from 0 to 0.


Constraints:

n == colors.length
m == edges.length
1 <= n <= 10^5
0 <= m <= 10^5
colors consists of lowercase English letters.
0 <= aj, bj < n

"""

# V0
# IDEA : TOPOLOGICAL SORT (KAHN) + DP OVER 26 COLORS
#
#   dp[v][c] = max count of color c on any path that ENDS at node v.
#   process nodes in topological order, so every predecessor of v is
#   finalised before v is popped :
#     dp[v][c] = max(dp[u][c]) over all u -> v, then dp[v][colors[v]] += 1
#
#   answer = max over all dp[v][c].
#
#   NOTE : cycle detection is free -> if the number of popped nodes is less
#          than n, some nodes never reached in-degree 0, i.e. a cycle exists.
#
# time = O((n + m) * 26), space = O(n * 26)
from collections import deque
class Solution(object):
    def largestPathValue(self, colors, edges):
        n = len(colors)
        g = [[] for _ in range(n)]
        indeg = [0] * n
        for a, b in edges:
            g[a].append(b)
            indeg[b] += 1

        dp = [[0] * 26 for _ in range(n)]
        q = deque()
        for i in range(n):
            if indeg[i] == 0:
                q.append(i)
                dp[i][ord(colors[i]) - ord('a')] = 1

        seen = 0
        res = 0
        while q:
            u = q.popleft()
            seen += 1
            res = max(res, max(dp[u]))
            cu = dp[u]
            for v in g[u]:
                cv = dp[v]
                for c in range(26):
                    if cu[c] > cv[c]:
                        cv[c] = cu[c]
                indeg[v] -= 1
                if indeg[v] == 0:
                    cv[ord(colors[v]) - ord('a')] += 1
                    q.append(v)

        return -1 if seen < n else res
