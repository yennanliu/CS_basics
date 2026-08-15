"""

3244. Shortest Distance After Road Addition Queries II
Hard

You are given an integer n and a 2D integer array queries.

There are n cities numbered from 0 to n - 1. Initially, there is a unidirectional road from city i to city i + 1 for all 0 <= i < n - 1.

queries[i] = [u_i, v_i] represents the addition of a new unidirectional road from city u_i to city v_i. After each query, you need to find the length of the shortest path from city 0 to city n - 1.

There are no two queries such that queries[i][0] < queries[j][0] < queries[i][1] < queries[j][1].

Return an array answer where for each i in the range [0, queries.length - 1], answer[i] is the length of the shortest path from city 0 to city n - 1.


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

3 <= n <= 10^5
1 <= queries.length <= 10^5
queries[i].length == 2
0 <= queries[i][0] < queries[i][1] < n
1 < queries[i][1] - queries[i][0]
There are no two queries such that i != j and queries[i][0] < queries[j][0] < queries[i][1] < queries[j][1].
There are no repeated roads among the queries.

"""

# V0
# IDEA : THE NON-CROSSING GUARANTEE MAKES THE SHORTCUTS *NEST*, SO JUST SPLICE
#
#   the promise that no two queries interleave (u1 < u2 < v1 < v2) means any
#   two shortcuts are either disjoint or fully nested. so a new road u -> v
#   swallows every city strictly between u and v that is still on the path,
#   and none of them can ever be needed again.
#
#   keep a `next` pointer per city describing the current best path. adding
#   u -> v walks from next[u] up to v, unlinking each city on the way and
#   shortening the answer by one per removal; then next[u] = v.
#
#   every city is removed at most once overall, so the whole run is linear
#   despite the inner walk.
#
# time = O(n + q), space = O(n)
class Solution(object):
    def shortestDistanceAfterQueries(self, n, queries):
        nxt = list(range(1, n + 1))          # nxt[i] = successor of i on the path
        length = n - 1                       # current shortest path length

        res = []
        for u, v in queries:
            # u already swallowed, or the path from u already reaches past v
            if nxt[u] == -1 or nxt[u] >= v:
                res.append(length)
                continue
            cur = nxt[u]
            while cur < v:                   # swallow everything strictly inside
                after = nxt[cur]
                nxt[cur] = -1                # detached, never revisited
                length -= 1
                cur = after
            nxt[u] = v
            res.append(length)
        return res
