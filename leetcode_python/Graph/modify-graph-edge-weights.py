"""

2699. Modify Graph Edge Weights
Hard

You are given an undirected weighted connected graph containing n nodes labeled from 0 to n - 1, and an integer array edges where edges[i] = [ai, bi, wi] indicates that there is an edge between nodes ai and bi with weight wi.

Some edges have a weight of -1 (wi = -1), while others have a positive weight (wi > 0).

Your task is to modify all edges with a weight of -1 by assigning them positive integer values in the range [1, 2 * 10^9] so that the shortest distance between the nodes source and destination becomes equal to an integer target. If there are multiple modifications that make the shortest distance between source and destination equal to target, any of them will be considered correct.

Return an array containing all edges (even unmodified ones) in any order if it is possible to make the shortest distance from source to destination equal to target, or an empty array if it's impossible.

Note: You are not allowed to modify the weights of edges with initial positive weights.


Example 1:

Input: n = 5, edges = [[4,1,-1],[2,0,-1],[0,3,-1],[4,3,-1]], source = 0, destination = 1, target = 5
Output: [[4,1,1],[2,0,1],[0,3,3],[4,3,1]]
Explanation: The graph above shows a possible modification to the edges, making the distance from 0 to 1 equal to 5.

Example 2:

Input: n = 3, edges = [[0,1,-1],[0,2,5]], source = 0, destination = 2, target = 6
Output: []
Explanation: The graph above contains the initial edges. It is not possible to make the distance from 0 to 2 equal to 6 by modifying the edge with weight -1. So, an empty array is returned.

Example 3:

Input: n = 4, edges = [[1,0,4],[1,2,3],[2,3,5],[0,3,-1]], source = 0, destination = 2, target = 6
Output: [[1,0,4],[1,2,3],[2,3,5],[0,3,1]]
Explanation: The graph above shows a modified graph having the shortest distance from 0 to 2 as 6.


Constraints:

1 <= n <= 100
1 <= edges.length <= n * (n - 1) / 2
edges[i].length == 3
0 <= ai, bi < n
wi = -1 or 1 <= wi <= 10^7
ai != bi
0 <= source, destination < n
1 <= target <= 10^9
The graph is connected, and there are no self-loops or repeated edges

"""

# V0
# IDEA : DIJKSTRA, ACTIVATING THE -1 EDGES ONE BY ONE (greedy)
#
#   step 1 : run dijkstra using ONLY the fixed (positive) edges.
#            let d be dist(source, destination).
#            - d <  target -> impossible, a path shorter than target already
#                             exists and adding edges can only shorten more -> []
#            - d == target -> already done; every -1 edge gets the max weight
#                             2 * 10^9 so it can never create a shorter path
#            - d >  target -> we must shorten it with the -1 edges
#
#   step 2 : turn the -1 edges on one at a time, each with weight 1 (the most
#            shortening possible). after switching edge e on, rerun dijkstra:
#            - d still > target -> e alone is not enough; LEAVE it at 1 (that
#              is safe, it did not overshoot) and move to the next -1 edge
#            - d <= target -> the new shortest path must use e (it only became
#              shorter because of e), so pushing e up by (target - d) raises
#              that path to exactly target. every later -1 edge -> 2 * 10^9
#
#   NOTE : the distance decreases monotonically as we switch edges on, so the
#          first time it drops to <= target we can hit target EXACTLY by
#          inflating just that one edge. this is why the greedy is correct.
#   NOTE : 2 * 10^9 doubles as both "infinity" and the legal max weight, which
#          is exactly why target <= 10^9 can never route through such an edge.
#   NOTE : n <= 100 so the O(n^2) matrix dijkstra (no heap) is the fast choice
#          here; the matrix is mutated in place instead of being rebuilt.
#
# time = O(m * n^2), space = O(n^2)
class Solution(object):
    def modifiedGraphEdges(self, n, edges, source, destination, target):
        INF = 2 * 10 ** 9

        g = [[INF] * n for _ in range(n)]
        for a, b, w in edges:
            if w > 0:
                g[a][b] = g[b][a] = w

        def dijkstra():
            dist = [INF] * n
            dist[source] = 0
            visited = [False] * n
            for _ in range(n):
                k = -1
                for j in range(n):
                    if not visited[j] and (k == -1 or dist[j] < dist[k]):
                        k = j
                visited[k] = True
                base, row = dist[k], g[k]
                for j in range(n):
                    if base + row[j] < dist[j]:
                        dist[j] = base + row[j]
            return dist[destination]

        d = dijkstra()
        if d < target:
            return []

        done = (d == target)
        for e in edges:
            if e[2] > 0:
                continue
            if done:
                e[2] = INF
                continue
            a, b = e[0], e[1]
            e[2] = 1
            g[a][b] = g[b][a] = 1
            d = dijkstra()
            if d <= target:
                done = True
                e[2] += target - d
                g[a][b] = g[b][a] = e[2]
        return edges if done else []
