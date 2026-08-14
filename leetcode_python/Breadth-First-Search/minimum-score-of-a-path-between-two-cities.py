"""

2492. Minimum Score of a Path Between Two Cities
Medium

You are given a positive integer n representing n cities numbered from 1 to n. You are also given a 2D array roads where roads[i] = [ai, bi, distance_i] indicates that there is a bidirectional road between cities ai and bi with a distance equal to distance_i. The cities graph is not necessarily connected.

The score of a path between two cities is defined as the minimum distance of a road in this path.

Return the minimum possible score of a path between cities 1 and n.

Note:

A path is a sequence of roads between two cities.
It is allowed for a path to contain the same road multiple times, and you can visit cities 1 and n multiple times along the path.
The test cases are generated such that there is at least one path between 1 and n.


Example 1:

Input: n = 4, roads = [[1,2,9],[2,3,6],[2,4,5],[1,4,7]]
Output: 5
Explanation: The path from city 1 to 4 with the minimum score is: 1 -> 2 -> 4. The score of this path is min(9,5) = 5.
It can be shown that no other path has less score.

Example 2:

Input: n = 4, roads = [[1,2,2],[1,3,4],[3,4,7]]
Output: 2
Explanation: The path from city 1 to 4 with the minimum score is: 1 -> 2 -> 1 -> 3 -> 4. The score of this path is min(2,2,4,7) = 2.


Constraints:

2 <= n <= 10^5
1 <= roads.length <= 10^5
roads[i].length == 3
1 <= ai, bi <= n
ai != bi
1 <= distance_i <= 10^4
There are no repeated edges.
There is at least one path between 1 and n.

"""

# V0
# IDEA : ROADS MAY BE REUSED — SO EVERY EDGE IN 1's COMPONENT IS REACHABLE
#
#   because a path can revisit cities and roads freely, any edge in the
#   connected component containing city 1 can be detoured onto and back. so
#   the score is simply the SMALLEST edge weight in that component.
#
#   the guarantee that 1 and n are connected means n lives in the same
#   component, so no separate check is needed.
#
#   one BFS from city 1, tracking the minimum weight of every edge seen.
#
# time = O(V + E), space = O(V + E)
from collections import deque, defaultdict


class Solution(object):
    def minScore(self, n, roads):
        g = defaultdict(list)
        for a, b, w in roads:
            g[a].append((b, w))
            g[b].append((a, w))

        seen = [False] * (n + 1)
        seen[1] = True
        q = deque([1])
        res = float('inf')
        while q:
            u = q.popleft()
            for v, w in g[u]:
                res = min(res, w)
                if not seen[v]:
                    seen[v] = True
                    q.append(v)
        return res
