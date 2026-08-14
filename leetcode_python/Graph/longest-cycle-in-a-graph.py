"""

2360. Longest Cycle in a Graph
Hard

You are given a directed graph of n nodes numbered from 0 to n - 1, where each node has at most one outgoing edge.

The graph is represented with a given 0-indexed array edges of size n, indicating that there is a directed edge from node i to node edges[i]. If there is no outgoing edge from node i, then edges[i] == -1.

Return the length of the longest cycle in the graph. If no cycle exists, return -1.

A cycle is a path that starts and ends at the same node.


Example 1:

Input: edges = [3,3,4,2,3]
Output: 3
Explanation: The longest cycle in the graph is the cycle: 2 -> 4 -> 3 -> 2.
The length of this cycle is 3, so 3 is returned.

Example 2:

Input: edges = [2,-1,3,1]
Output: -1
Explanation: There are no cycles in this graph.


Constraints:

n == edges.length
2 <= n <= 10^5
-1 <= edges[i] < n
edges[i] != i

"""

# V0
# IDEA : FUNCTIONAL GRAPH + VISIT TIMESTAMPS (each node walked once overall)
#
#   Out-degree <= 1, so from any start the walk is a single chain that either
#   dies at -1 or hits an already-seen node.
#
#   Stamp each node with the "step counter" at the moment we visit it, plus the
#   id of the walk that visited it. When the walk re-enters a node j:
#     - if j belongs to THIS walk  -> we closed a cycle of length
#                                     step_now - stamp[j]
#     - if j belongs to an EARLIER walk -> that part was already processed, stop
#
#   NOTE : iterative -- a chain can be 10^5 nodes deep.
#
# time = O(n), space = O(n)
class Solution(object):
    def longestCycle(self, edges):
        n = len(edges)
        owner = [-1] * n     # which walk visited this node
        stamp = [0] * n      # step counter within that walk

        res = -1
        step = 0
        for i in range(n):
            if owner[i] != -1:
                continue
            j = i
            while j != -1:
                if owner[j] != -1:
                    if owner[j] == i:
                        length = step - stamp[j]
                        if length > res:
                            res = length
                    break
                owner[j] = i
                stamp[j] = step
                step += 1
                j = edges[j]
        return res
