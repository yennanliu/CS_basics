"""

3543. Maximum Weighted K-Edge Path
Medium

You are given an integer n and a Directed Acyclic Graph (DAG) with n nodes
labeled from 0 to n - 1. This is represented by a 2D array edges, where edges[i]
= [u_i, v_i, w_i] indicates a directed edge from node u_i to v_i with weight
w_i.

You are also given two integers, k and t.

Your task is to determine the maximum possible sum of edge weights for any path
in the graph such that:

The path contains exactly k edges.

The total sum of edge weights in the path is strictly less than t.

Return the maximum possible sum of weights for such a path. If no such path
exists, return -1.

Example 1:

Input: n = 3, edges = [[0,1,1],[1,2,2]], k = 2, t = 4

Output: 3

Explanation:

The only path with k = 2 edges is 0 -> 1 -> 2 with weight 1 + 2 = 3 < t.

Thus, the maximum possible sum of weights less than t is 3.

Example 2:

Input: n = 3, edges = [[0,1,2],[0,2,3]], k = 1, t = 3

Output: 2

Explanation:

There are two paths with k = 1 edge:

0 -> 1 with weight 2 < t.

0 -> 2 with weight 3 = t, which is not strictly less than t.

Thus, the maximum possible sum of weights less than t is 2.

Example 3:

Input: n = 3, edges = [[0,1,6],[1,2,8]], k = 1, t = 6

Output: -1

Explanation:

There are two paths with k = 1 edge:

0 -> 1 with weight 6 = t, which is not strictly less than t.

1 -> 2 with weight 8 > t, which is not strictly less than t.

Since there is no path with sum of weights strictly less than t, the answer is
-1.

Constraints:

1 <= n <= 300

0 <= edges.length <= 300

edges[i] = [u_i, v_i, w_i]

0 <= u_i, v_i < n

u_i != v_i

1 <= w_i <= 10

0 <= k <= 300

1 <= t <= 600

The input graph is guaranteed to be a DAG.

There are no duplicate edges.

"""

# V0
# IDEA : LAYERED DP WHOSE STATE IS A BITSET OF THE ACHIEVABLE PATH SUMS
#
#   the path length is pinned at exactly k edges, so the natural DP walks in
#   layers: after s edges, what sums can a path ending at node v have?  the
#   answer is a *set*, not a single number, because a smaller sum now can still
#   beat a larger one later once the strict "< t" cap is applied.
#
#   every weight is positive, so any partial sum that has already reached t is
#   dead -- it can never come back down.  that caps every interesting sum at
#   t - 1 <= 599 values, small enough to keep the whole set as one Python
#   integer with bit w meaning "sum w is reachable".
#
#   relaxing an edge (u, v, w) is then a single shift: the sums at v gain
#   (bitset at u) << w, masked back down to below t.  one layer costs one shift
#   per edge, so the k layers cost O(k * E) big-integer operations, and the
#   answer is the highest bit still standing after k layers.
#
# time = O(k * E * t / 64), space = O(n * t / 64)
class Solution(object):
    def maxWeight(self, n, edges, k, t):
        mask = (1 << t) - 1
        cur = [1] * n                       # bit 0 set: a 0-edge path sums to 0
        for _ in range(k):
            nxt = [0] * n
            for u, v, w in edges:
                s = cur[u]
                if s:
                    nxt[v] |= (s << w) & mask
            cur = nxt

        total = 0
        for s in cur:
            total |= s
        return total.bit_length() - 1 if total else -1
