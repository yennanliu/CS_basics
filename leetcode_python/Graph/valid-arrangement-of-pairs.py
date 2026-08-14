"""

2097. Valid Arrangement of Pairs
Hard

You are given a 0-indexed 2D integer array pairs where pairs[i] = [start_i, end_i]. An arrangement of pairs is valid if for every index i where 1 <= i < pairs.length, we have end_(i-1) == start_i.

Return any valid arrangement of pairs.

Note: The inputs will be generated such that there exists a valid arrangement of pairs.


Example 1:

Input: pairs = [[5,1],[4,5],[11,9],[9,4]]
Output: [[11,9],[9,4],[4,5],[5,1]]
Explanation:
This is a valid arrangement since end_i-1 always equals start_i.
end_0 = 9 == 9 = start_1
end_1 = 4 == 4 = start_2
end_2 = 5 == 5 = start_3

Example 2:

Input: pairs = [[1,3],[3,2],[2,1]]
Output: [[1,3],[3,2],[2,1]]
Explanation:
This is a valid arrangement since end_i-1 always equals start_i.
end_0 = 3 == 3 = start_1
end_1 = 2 == 2 = start_2
The arrangements [[2,1],[1,3],[3,2]] and [[3,2],[2,1],[1,3]] are also valid.

Example 3:

Input: pairs = [[1,2],[1,3],[2,1]]
Output: [[1,2],[2,1],[1,3]]
Explanation:
This is a valid arrangement since end_i-1 always equals start_i.
end_0 = 2 == 2 = start_1
end_1 = 1 == 1 = start_2


Constraints:

1 <= pairs.length <= 10^5
pairs[i].length == 2
0 <= start_i, end_i <= 10^9
start_i != end_i
No two pairs are exactly the same.
There exists a valid arrangement of pairs.

"""

# V0
# IDEA : EULERIAN PATH (Hierholzer) — each pair is a DIRECTED EDGE
#
#   chaining the pairs so that every end meets the next start is exactly
#   walking every edge once : an Eulerian path.
#
#   start node : the vertex with  outdeg - indeg == 1  if one exists,
#                otherwise any vertex with an outgoing edge (the graph is a
#                closed circuit).
#
#   Hierholzer, iteratively to avoid recursion limits : push nodes on a
#   stack, greedily consume an unused outgoing edge, and when a node has
#   none left pop it into the route. reversing the route gives the node
#   order; zip consecutive nodes back into pairs.
#
# time = O(V + E), space = O(V + E)
from collections import defaultdict


class Solution(object):
    def validArrangement(self, pairs):
        g = defaultdict(list)
        outdeg = defaultdict(int)
        indeg = defaultdict(int)
        for u, v in pairs:
            g[u].append(v)
            outdeg[u] += 1
            indeg[v] += 1

        start = pairs[0][0]
        for node in outdeg:
            if outdeg[node] - indeg[node] == 1:
                start = node
                break

        route = []
        stack = [start]
        while stack:
            u = stack[-1]
            if g[u]:
                stack.append(g[u].pop())
            else:
                route.append(stack.pop())
        route.reverse()

        return [[route[i], route[i + 1]] for i in range(len(route) - 1)]
