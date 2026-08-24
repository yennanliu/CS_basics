"""

847. Shortest Path Visiting All Nodes
Hard

You have an undirected, connected graph of n nodes labeled from 0 to n - 1.
You are given an array graph where graph[i] is a list of all the nodes connected
with node i by an edge.

Return the length of the shortest path that visits every node. You may start and
stop at any node, you may revisit nodes multiple times, and you may reuse edges.


Example 1:

Input: graph = [[1,2,3],[0],[0],[0]]
Output: 4
Explanation: One possible path is [1,0,2,0,3]

Example 2:

Input: graph = [[1],[0,2,4],[1,3,4],[2],[1,2]]
Output: 4
Explanation: One possible path is [0,1,4,2,3]


Constraints:

n == graph.length
1 <= n <= 12
0 <= graph[i].length < n
graph[i] does not contain i.
If graph[a] contains b, then graph[b] contains a.
The input graph is always connected.

"""

# V0
# IDEA : BFS + BITMASK (multi-source, state = (node, visited set))
#
#   A plain "visited node" set is not enough because we are allowed to revisit
#   nodes. The real state is (current node, bitmask of nodes seen so far).
#   There are only n * 2^n <= 12 * 4096 such states, so BFS over them is cheap.
#
#   Every node can be the start, so we seed the queue with all n states
#   (i, 1 << i) at distance 0 -> the first time we pop a state whose mask is
#   full, that distance is the answer.
#
"""

DP def
    a plain "visited node" set is NOT enough - revisiting nodes is allowed.
    the real state is (current node, bitmask of nodes seen so far).

    dist[(node, mask)]: fewest steps to be standing at `node` having

                        already visited exactly the set `mask`

                        -> only n * 2^n <= 12 * 4096 such states

DP eq

     dist[(nxt, mask | (1 << nxt))] = dist[(node, mask)] + 1

        for every neighbour nxt of node


    -> e.g. every node can be the START, so seed all n states (i, 1 << i) at
              distance 0 - a multi-source BFS. since every edge costs 1, BFS
              order IS the DP order, so the first time a full-mask state is
              popped that distance is the answer

     ans = the first distance at which mask == (1 << n) - 1

"""
# time  = O(n^2 * 2^n)
# space = O(n * 2^n)
from collections import deque
class Solution(object):
    def shortestPathLength(self, graph):
        n = len(graph)
        full = (1 << n) - 1

        # multi-source: start from every node at once
        queue = deque((i, 1 << i) for i in range(n))
        visited = set((i, 1 << i) for i in range(n))

        steps = 0
        while queue:
            for _ in range(len(queue)):
                node, mask = queue.popleft()
                if mask == full:
                    return steps
                for nxt in graph[node]:
                    state = (nxt, mask | (1 << nxt))
                    if state not in visited:
                        visited.add(state)
                        queue.append(state)
            steps += 1

        return -1
