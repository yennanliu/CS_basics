"""

2359. Find Closest Node to Given Two Nodes
Medium

You are given a directed graph of n nodes numbered from 0 to n - 1, where each node has at most one outgoing edge.

The graph is represented with a given 0-indexed array edges of size n, indicating that there is a directed edge from node i to node edges[i]. If there is no outgoing edge from i, then edges[i] == -1.

You are also given two integers node1 and node2.

Return the index of the node that can be reached from both node1 and node2, such that the maximum between the distance from node1 to that node, and from node2 to that node is minimized. If there are multiple answers, return the node with the smallest index, and if no possible answer exists, return -1.

Note that edges may contain cycles.


Example 1:

Input: edges = [2,2,3,-1], node1 = 0, node2 = 1
Output: 2
Explanation: The distance from node 0 to node 2 is 1, and the distance from node 1 to node 2 is 1.
The maximum of those two distances is 1. It can be proven that we cannot get a node with a smaller maximum distance than 1, so we return node 2.

Example 2:

Input: edges = [1,2,-1], node1 = 0, node2 = 2
Output: 2
Explanation: The distance from node 0 to node 2 is 2, and the distance from node 2 to itself is 0.
The maximum of those two distances is 2. It can be proven that we cannot get a node with a smaller maximum distance than 2, so we return node 2.


Constraints:

n == edges.length
2 <= n <= 10^5
-1 <= edges[i] < n
edges[i] != i
0 <= node1, node2 < n

"""

# V0
# IDEA : FUNCTIONAL GRAPH WALK (out-degree <= 1 -> the reachable set is one chain)
#
#   Every node has at most one outgoing edge, so "BFS from node1" degenerates
#   into simply following edges[] until we fall off (-1) or revisit a node
#   (the chain ran into a cycle). That gives dist arrays d1 and d2 in O(n).
#
#   Then scan i = 0..n-1 and keep the first i minimising max(d1[i], d2[i]);
#   scanning left to right automatically breaks ties by smallest index.
#
#   NOTE : do the walk iteratively -- a chain can be 10^5 long.
#
# time = O(n), space = O(n)
class Solution(object):
    def closestMeetingNode(self, edges, node1, node2):
        n = len(edges)
        INF = float("inf")

        def walk(src):
            dist = [INF] * n
            d = 0
            cur = src
            while cur != -1 and dist[cur] == INF:
                dist[cur] = d
                d += 1
                cur = edges[cur]
            return dist

        d1 = walk(node1)
        d2 = walk(node2)

        res, best = -1, INF
        for i in range(n):
            t = d1[i] if d1[i] > d2[i] else d2[i]
            if t < best:
                best = t
                res = i
        return res
