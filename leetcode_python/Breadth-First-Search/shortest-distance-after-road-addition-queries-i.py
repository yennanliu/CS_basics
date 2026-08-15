"""

3243. Shortest Distance After Road Addition Queries I
Medium

You are given an integer n and a 2D integer array queries.

There are n cities numbered from 0 to n - 1. Initially, there is a unidirectional road from city i to city i + 1 for all 0 <= i < n - 1.

queries[i] = [u_i, v_i] represents the addition of a new unidirectional road from city u_i to city v_i. After each query, you need to find the length of the shortest path from city 0 to city n - 1.

Return an array answer where for each i in the range [0, queries.length - 1], answer[i] is the length of the shortest path from city 0 to city n - 1 after processing the first i + 1 queries.


Example 1:

Input: n = 5, queries = [[2,4],[0,2],[0,4]]
Output: [3,2,1]
Explanation:
After the addition of the road from 2 to 4, the length of the shortest path from 0 to 4 is 3.
After the addition of the road from 0 to 2, the length of the shortest path from 0 to 4 is 2.
After the addition of the road from 0 to 4, the length of the shortest path from 0 to 4 is 1.

Example 2:

Input: n = 4, queries = [[0,3],[0,2]]
Output: [1,1]
Explanation:
After the addition of the road from 0 to 3, the length of the shortest path from 0 to 3 is 1.
After the addition of the road from 0 to 2, the length of the shortest path remains 1.


Constraints:

3 <= n <= 500
1 <= queries.length <= 500
queries[i].length == 2
0 <= queries[i][0] < queries[i][1] < n
1 < queries[i][1] - queries[i][0]
There are no repeated roads among the queries.

"""

# V0
# IDEA : ALL EDGES COST 1 — SO ONE BFS PER QUERY IS ENOUGH AT THIS SIZE
#
#   the graph stays unweighted no matter how many shortcuts are added, so a
#   plain BFS from city 0 gives the distance to n-1.
#
#   n and the query count are both capped at 500, so re-running the BFS after
#   each addition is 500 * (500 + 1000) steps — well within budget. the
#   sequel (LC 3244) removes that slack and needs the non-crossing structure.
#
# time = O(q * (n + q)), space = O(n + q)
from collections import deque


class Solution(object):
    def shortestDistanceAfterQueries(self, n, queries):
        adj = [[] for _ in range(n)]
        for i in range(n - 1):
            adj[i].append(i + 1)

        res = []
        for u, v in queries:
            adj[u].append(v)
            dist = [-1] * n
            dist[0] = 0
            q = deque([0])
            while q:
                cur = q.popleft()
                if cur == n - 1:
                    break
                for nxt in adj[cur]:
                    if dist[nxt] == -1:
                        dist[nxt] = dist[cur] + 1
                        q.append(nxt)
            res.append(dist[n - 1])
        return res
