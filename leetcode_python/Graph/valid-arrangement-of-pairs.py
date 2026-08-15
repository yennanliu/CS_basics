"""

2097. Valid Arrangement of Pairs
Hard

You are given a 0-indexed 2D integer array pairs where pairs[i] = [starti, endi]. An arrangement of pairs is valid if for every index i where 1 <= i < pairs.length, we have endi-1 == starti.

Return any valid arrangement of pairs.

Note: The inputs will be generated such that there exists a valid arrangement of pairs.


Example 1:

Input: pairs = [[5,1],[4,5],[11,9],[9,4]]
Output: [[11,9],[9,4],[4,5],[5,1]]
Explanation:
This is a valid arrangement since endi-1 always equals starti.
end0 = 9 == 9 = start1
end1 = 4 == 4 = start2
end2 = 5 == 5 = start3

Example 2:

Input: pairs = [[1,3],[3,2],[2,1]]
Output: [[1,3],[3,2],[2,1]]
Explanation:
This is a valid arrangement since endi-1 always equals starti.
end0 = 3 == 3 = start1
end1 = 2 == 2 = start2
The arrangements [[2,1],[1,3],[3,2]] and [[3,2],[2,1],[1,3]] are also valid.

Example 3:

Input: pairs = [[1,2],[1,3],[2,1]]
Output: [[1,2],[2,1],[1,3]]
Explanation:
This is a valid arrangement since endi-1 always equals starti.
end0 = 2 == 2 = start1
end1 = 1 == 1 = start2


Constraints:

1 <= pairs.length <= 10^5
pairs[i].length == 2
0 <= starti, endi <= 10^9
starti != endi
No two pairs are exactly the same.
There exists a valid arrangement of pairs.

"""

# V0
# IDEA : EULERIAN PATH (HIERHOLZER)
#
#   read each pair [a, b] as a DIRECTED EDGE a -> b. an arrangement that
#   chains end == next start is exactly a walk using every edge once, i.e.
#   an Eulerian path.
#
#   start vertex :
#       the one with  outdegree - indegree == 1  if it exists (path),
#       otherwise any vertex with an outgoing edge (the walk is a circuit).
#
#   Hierholzer, iteratively so 10^5 edges cannot blow the stack :
#       push start on a stack; while the top still has unused edges, walk
#       one and push the neighbour; when it runs dry, pop it into `route`.
#       `route` reversed is the Eulerian path of VERTICES, and consecutive
#       vertices are the pairs to output.
#
# time = O(V + E), space = O(V + E)
from collections import defaultdict


class Solution(object):
    def validArrangement(self, pairs):
        adj = defaultdict(list)
        degree = defaultdict(int)          # outdegree - indegree
        for a, b in pairs:
            adj[a].append(b)
            degree[a] += 1
            degree[b] -= 1

        start = pairs[0][0]
        for node, d in degree.items():
            if d == 1:
                start = node
                break

        route = []
        stack = [start]
        while stack:
            node = stack[-1]
            if adj[node]:
                stack.append(adj[node].pop())
            else:
                route.append(stack.pop())
        route.reverse()

        return [[route[i], route[i + 1]] for i in range(len(route) - 1)]
