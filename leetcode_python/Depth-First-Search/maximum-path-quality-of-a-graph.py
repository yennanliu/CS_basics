"""

2065. Maximum Path Quality of a Graph
Hard

There is an undirected graph with n nodes numbered from 0 to n - 1 (inclusive). You are given a 0-indexed integer array values where values[i] is the value of the ith node. You are also given a 0-indexed 2D integer array edges, where each edges[j] = [u_j, v_j, time_j] indicates that there is an undirected edge between the nodes u_j and v_j, and it takes time_j seconds to travel between the two nodes. Finally, you are given an integer maxTime.

A valid path in the graph is any path that starts at node 0, ends at node 0, and takes at most maxTime seconds to complete. You may visit the same node multiple times. The quality of a valid path is the sum of the values of the unique nodes visited in the path (each node's value is added at most once to the sum).

Return the maximum quality of a valid path.

Note: There are at most four edges connected to each node.


Example 1:

Input: values = [0,32,10,43], edges = [[0,1,10],[1,2,15],[0,3,10]], maxTime = 49
Output: 75
Explanation:
One possible path is 0 -> 1 -> 0 -> 3 -> 0. The total time taken is 10 + 10 + 10 + 10 = 40 <= 49.
The nodes visited are 0, 1, and 3, giving a maximal path quality of 0 + 32 + 43 = 75.

Example 2:

Input: values = [5,10,15,20], edges = [[0,1,10],[1,2,10],[0,3,10]], maxTime = 30
Output: 25
Explanation:
One possible path is 0 -> 3 -> 0. The total time taken is 10 + 10 = 20 <= 30.
The nodes visited are 0 and 3, giving a maximal path quality of 5 + 20 = 25.

Example 3:

Input: values = [1,2,3,4], edges = [[0,1,10],[1,2,11],[2,3,12],[1,3,13]], maxTime = 50
Output: 7
Explanation:
One possible path is 0 -> 1 -> 3 -> 1 -> 0. The total time taken is 10 + 13 + 13 + 10 = 46 <= 50.
The nodes visited are 0, 1, and 3, giving a maximal path quality of 1 + 2 + 4 = 7.


Constraints:

n == values.length
1 <= n <= 1000
0 <= values[i] <= 10^8
0 <= edges.length <= 2000
edges[j].length == 3
0 <= u_j < v_j <= n - 1
10 <= time_j, maxTime <= 100
All the pairs [u_j, v_j] are unique.
There are at most four edges connected to each node.

"""

# V0
# IDEA : EXHAUSTIVE DFS (the time budget bounds the walk length)
#
#   every edge costs >= 10 and maxTime <= 100, so a valid walk has at most
#   10 edges; each node has at most 4 neighbours -> at most 4^10 walks,
#   small enough to enumerate them all.
#
#   dfs(u, cost, value) walks the graph carrying the accumulated time and
#   the sum of the DISTINCT nodes seen so far. `vis` marks nodes already
#   counted: re-entering a visited node is allowed (it costs time) but adds
#   no value, so only the first entry flips vis and adds values[v].
#
#   NOTE : the answer is updated whenever we are standing on node 0, since
#          the path must both start and end there.
#
# time = O(n + m + 4^(maxTime/min_time)), space = O(n + m)
class Solution(object):
    def maximalPathQuality(self, values, edges, maxTime):
        n = len(values)
        g = [[] for _ in range(n)]
        for u, v, t in edges:
            g[u].append((v, t))
            g[v].append((u, t))

        vis = [False] * n
        vis[0] = True
        res = [0]

        def dfs(u, cost, value):
            if u == 0 and value > res[0]:
                res[0] = value
            for v, t in g[u]:
                if cost + t <= maxTime:
                    if vis[v]:
                        dfs(v, cost + t, value)
                    else:
                        vis[v] = True
                        dfs(v, cost + t, value + values[v])
                        vis[v] = False

        dfs(0, 0, values[0])
        return res[0]
