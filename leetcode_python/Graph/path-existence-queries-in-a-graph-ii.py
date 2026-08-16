"""

3534. Path Existence Queries in a Graph II
Hard

You are given an integer n representing the number of nodes in a graph, labeled
from 0 to n - 1.

You are also given an integer array nums of length n and an integer maxDiff.

An undirected edge exists between nodes i and j if the absolute difference
between nums[i] and nums[j] is at most maxDiff (i.e., |nums[i] - nums[j]| <=
maxDiff).

You are also given a 2D integer array queries. For each queries[i] = [u_i, v_i],
find the minimum distance between nodes u_i and v_i_. If no path exists between
the two nodes, return -1 for that query.

Return an array answer, where answer[i] is the result of the i^th query.

Note: The edges between the nodes are unweighted.

Example 1:

Input: n = 5, nums = [1,8,3,4,2], maxDiff = 3, queries = [[0,3],[2,4]]

Output: [1,1]

Explanation:

The resulting graph is:

Query  | Shortest Path | Minimum Distance
-------+---------------+-----------------
[0, 3] | 0 -> 3        | 1
[2, 4] | 2 -> 4        | 1

Thus, the output is [1, 1].

Example 2:

Input: n = 5, nums = [5,3,1,9,10], maxDiff = 2, queries =
[[0,1],[0,2],[2,3],[4,3]]

Output: [1,2,-1,1]

Explanation:

The resulting graph is:

Query  | Shortest Path | Minimum Distance
-------+---------------+-----------------
[0, 1] | 0 -> 1        | 1
[0, 2] | 0 -> 1 -> 2   | 2
[2, 3] | None          | -1
[4, 3] | 3 -> 4        | 1

Thus, the output is [1, 2, -1, 1].

Example 3:

Input: n = 3, nums = [3,6,1], maxDiff = 1, queries = [[0,0],[0,1],[1,2]]

Output: [0,-1,-1]

Explanation:

There are no edges between any two nodes because:

Nodes 0 and 1: |nums[0] - nums[1]| = |3 - 6| = 3 > 1

Nodes 0 and 2: |nums[0] - nums[2]| = |3 - 1| = 2 > 1

Nodes 1 and 2: |nums[1] - nums[2]| = |6 - 1| = 5 > 1

Thus, no node can reach any other node, and the output is [0, -1, -1].

Constraints:

1 <= n == nums.length <= 10^5

0 <= nums[i] <= 10^5

0 <= maxDiff <= 10^5

1 <= queries.length <= 10^5

queries[i] == [u_i, v_i]

0 <= u_i, v_i < n

"""

# V0
# IDEA : SORT BY VALUE -> INTERVAL GRAPH -> GREEDY JUMPS VIA BINARY LIFTING
#
#   the edge rule only looks at values, so sort the nodes by value.  in that
#   order the neighbours of position p form a *contiguous* block: everything
#   from the first value >= nums[p] - maxDiff to the last value <= nums[p] +
#   maxDiff.  and the right end of that block is non-decreasing in p.
#
#   for such a graph the shortest path between two sorted positions a < b never
#   moves left, and the classic jump-game greedy is optimal: from p always hop
#   to far[p], the furthest position still within maxDiff.  the number of hops
#   is what the query asks for, and answering 10^5 queries one hop at a time is
#   too slow, so precompute the 2^j-th iterate of far -- binary lifting then
#   counts the hops in O(log n) by taking the largest jumps that still fall
#   short of b.
#
#   connectivity is even simpler: two nodes are joined iff no adjacent pair in
#   the sorted order has a gap larger than maxDiff between them, so a running
#   component id over the sorted values decides -1 in O(1).
#
# time = O((n + q) * log n), space = O(n * log n)
import bisect


class Solution(object):
    def pathExistenceQueries(self, n, nums, maxDiff, queries):
        idx = sorted(range(n), key=lambda i: nums[i])
        vals = [nums[i] for i in idx]
        pos = [0] * n                       # original node -> sorted position
        for p, i in enumerate(idx):
            pos[i] = p

        comp = [0] * n                      # connected component of each position
        for p in range(1, n):
            comp[p] = comp[p - 1] + (1 if vals[p] - vals[p - 1] > maxDiff else 0)

        far = [0] * n                       # furthest position reachable in one hop
        for p in range(n):
            far[p] = bisect.bisect_right(vals, vals[p] + maxDiff) - 1

        LOG = max(1, n.bit_length())
        up = [far]
        for _ in range(1, LOG):
            prev = up[-1]
            up.append([prev[x] for x in prev])

        res = []
        for u, v in queries:
            a, b = pos[u], pos[v]
            if a > b:
                a, b = b, a
            if a == b:
                res.append(0)
                continue
            if comp[a] != comp[b]:
                res.append(-1)
                continue
            steps = 0
            cur = a
            for j in range(LOG - 1, -1, -1):
                nxt = up[j][cur]
                if nxt < b:
                    cur = nxt
                    steps += 1 << j
            res.append(steps + 1)
        return res
