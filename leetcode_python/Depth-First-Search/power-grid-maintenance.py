"""

3607. Power Grid Maintenance
Medium

You are given an integer c representing c power stations, each with a unique
identifier id from 1 to c (1-based indexing).

These stations are interconnected via n bidirectional cables, represented by a
2D array connections, where each element connections[i] = [ui, vi] indicates a
connection between station ui and station vi. Stations that are directly or
indirectly connected form a power grid.

Initially, all stations are online (operational).

You are also given a 2D array queries, where each query is one of the following
two types:

[1, x]: A maintenance check is requested for station x. If station x is online,
it resolves the check by itself. If station x is offline, the check is resolved
by the operational station with the smallest id in the same power grid as x. If
no operational station exists in that grid, return -1.

[2, x]: Station x goes offline (i.e., it becomes non-operational).

Return an array of integers representing the results of each query of type
[1, x] in the order they appear.

Note: The power grid preserves its structure; an offline (non-operational) node
remains part of its grid and taking it offline does not alter connectivity.


Example 1:

Input: c = 5, connections = [[1,2],[2,3],[3,4],[4,5]],
       queries = [[1,3],[2,1],[1,1],[2,2],[1,2]]
Output: [3,2,3]
Explanation:
Initially, all stations {1, 2, 3, 4, 5} are online and form a single power grid.
Query [1,3]: Station 3 is online, so the maintenance check is resolved by
station 3.
Query [2,1]: Station 1 goes offline. The remaining online stations are
{2, 3, 4, 5}.
Query [1,1]: Station 1 is offline, so the check is resolved by the operational
station with the smallest id among {2, 3, 4, 5}, which is station 2.
Query [2,2]: Station 2 goes offline. The remaining online stations are {3, 4, 5}.
Query [1,2]: Station 2 is offline, so the check is resolved by the operational
station with the smallest id among {3, 4, 5}, which is station 3.

Example 2:

Input: c = 3, connections = [], queries = [[1,1],[2,1],[1,1]]
Output: [1,-1]
Explanation:
There are no connections, so each station is its own isolated grid.
Query [1,1]: Station 1 is online in its isolated grid, so the maintenance check
is resolved by station 1.
Query [2,1]: Station 1 goes offline.
Query [1,1]: Station 1 is offline and there are no other stations in its grid,
so the result is -1.


Constraints:

1 <= c <= 10^5
0 <= n == connections.length <= min(10^5, c * (c - 1) / 2)
connections[i].length == 2
1 <= ui, vi <= c
ui != vi
1 <= queries.length <= 2 * 10^5
queries[i].length == 2
queries[i][0] is either 1 or 2.
1 <= queries[i][1] <= c
"""

# V0
# IDEA : UNION-FIND + PER-GRID MIN-HEAP WITH LAZY DELETION
#
#   the note in the statement is the whole trick: going offline never breaks a
#   cable, so the partition into grids is fixed for the entire run. that means
#   one pass of union-find up front tells us, once and for all, which grid each
#   station belongs to — the only thing that changes over time is the online
#   flag.
#
#   per grid we need "smallest online id", with deletions only (a station never
#   comes back). a min-heap of the grid's ids serves this perfectly: ids are
#   only ever removed, so once we pop an offline id off the top it can never be
#   needed again. that makes lazy deletion exact rather than merely convenient,
#   and amortises every id to a single push and a single pop.
#
#   the `[1, x]` answer is `x` itself whenever x is online — no heap work at
#   all — because x is trivially the smallest online id "in the same grid as x"
#   under the rule stated. otherwise we peel dead entries off that grid's heap.
#
# time = O((c + n + q) * log(c)), space = O(c)
import heapq


class Solution(object):
    def processQueries(self, c, connections, queries):
        parent = list(range(c + 1))

        def find(x):
            root = x
            while parent[root] != root:
                root = parent[root]
            while parent[x] != root:
                parent[x], x = root, parent[x]
            return root

        for u, v in connections:
            ru, rv = find(u), find(v)
            if ru != rv:
                parent[ru] = rv

        grid = [[] for _ in range(c + 1)]
        for i in range(1, c + 1):
            grid[find(i)].append(i)
        for i in range(1, c + 1):
            if grid[i]:
                heapq.heapify(grid[i])

        online = [True] * (c + 1)
        res = []
        for kind, x in queries:
            if kind == 2:
                online[x] = False
                continue
            if online[x]:
                res.append(x)
                continue
            heap = grid[find(x)]
            while heap and not online[heap[0]]:
                heapq.heappop(heap)
            res.append(heap[0] if heap else -1)
        return res
