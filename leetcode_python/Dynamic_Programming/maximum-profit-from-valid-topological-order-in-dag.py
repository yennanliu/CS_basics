"""

3530. Maximum Profit from Valid Topological Order in DAG
Hard

You are given a Directed Acyclic Graph (DAG) with n nodes labeled from 0 to n -
1, represented by a 2D array edges, where edges[i] = [u_i, v_i] indicates a
directed edge from node u_i to v_i. Each node has an associated score given in
an array score, where score[i] represents the score of node i.

You must process the nodes in a valid topological order. Each node is assigned a
1-based position in the processing order.

The profit is calculated by summing up the product of each node's score and its
position in the ordering.

Return the maximum possible profit achievable with an optimal topological order.

A topological order of a DAG is a linear ordering of its nodes such that for
every directed edge u → v, node u comes before v in the ordering.

Example 1:

Input: n = 2, edges = [[0,1]], score = [2,3]

Output: 8

Explanation:

Node 1 depends on node 0, so a valid order is [0, 1].

Node | Processing Order | Score | Multiplier | Profit Calculation
-----+------------------+-------+------------+-------------------
   0 | 1st              |     2 |          1 | 2 x 1 = 2
   1 | 2nd              |     3 |          2 | 3 x 2 = 6

The maximum total profit achievable over all valid topological orders is 2 + 6 =
8.

Example 2:

Input: n = 3, edges = [[0,1],[0,2]], score = [1,6,3]

Output: 25

Explanation:

Nodes 1 and 2 depend on node 0, so the most optimal valid order is [0, 2, 1].

Node | Processing Order | Score | Multiplier | Profit Calculation
-----+------------------+-------+------------+-------------------
   0 | 1st              |     1 |          1 | 1 x 1 = 1
   2 | 2nd              |     3 |          2 | 3 x 2 = 6
   1 | 3rd              |     6 |          3 | 6 x 3 = 18

The maximum total profit achievable over all valid topological orders is 1 + 6 +
18 = 25.

Constraints:

1 <= n == score.length <= 22

1 <= score[i] <= 10^5

0 <= edges.length <= n * (n - 1) / 2

edges[i] == [u_i, v_i] denotes a directed edge from u_i to v_i.

0 <= u_i, v_i < n

u_i != v_i

The input graph is guaranteed to be a DAG.

There are no duplicate edges.

"""

# V0
# IDEA : BITMASK DP OVER PROCESSED SETS, WITH A MEET-IN-THE-MIDDLE READY TABLE
#
#   the only thing a partial ordering needs to remember is *which* nodes are
#   already processed -- the position of the next node is just popcount + 1,
#   and which nodes may come next depends on the set alone.  so dp[mask] = best
#   profit after processing exactly the nodes in mask, and n <= 22 makes the
#   2^n states affordable.
#
#   a node i may be placed next when mask contains every predecessor of i, i.e.
#   pred[i] is a subset of mask.  testing that for all i at every mask costs a
#   second factor of n, so instead split the 22 bits into two halves and
#   precompute, for each half-mask, which nodes have all their predecessors *in
#   that half* satisfied.  intersecting the two tables gives the ready set of
#   any full mask with a single AND, so the per-state overhead collapses to the
#   bits actually iterated.
#
#   masks are visited in increasing numeric order, which is a valid topological
#   order of the subset lattice because mask | bit is always larger than mask.
#
"""

DP def
    a partial ordering only needs to remember WHICH nodes are already
    processed - the position of the next node is just popcount(mask) + 1, and
    which nodes may come next depends on the set alone

    dp[mask]: best profit after processing EXACTLY the nodes in mask

              (n <= 22, so 2^n states are affordable)

DP eq

     node i may be placed next iff pred[i] is a SUBSET of mask, and it lands
     at position popcount(mask) + 1:

        dp[mask | (1 << i)] = max( dp[mask | (1<<i)],

                                   dp[mask] + score[i] * (popcount(mask) + 1) )


    -> e.g. the speed-up: testing "pred[i] subset of mask" for all i at every
              mask costs an extra factor n. instead SPLIT the 22 bits in half
              and precompute, per half-mask, which nodes have their
              predecessors in that half satisfied - intersecting the two
              tables gives the ready set with ONE AND

     masks are visited in increasing numeric order, which IS a topological
     order of the subset lattice (mask | bit > mask)

     ans = dp[(1 << n) - 1]

"""
# time = O(n * 2^n), space = O(2^n)
class Solution(object):
    def maxProfit(self, n, edges, score):
        pred = [0] * n
        for u, v in edges:
            pred[v] |= 1 << u

        half = n >> 1
        low_mask = (1 << half) - 1
        # ready_lo[a] : nodes whose low-half predecessors are all inside a
        ready_lo = [0] * (1 << half)
        for a in range(1 << half):
            r = 0
            for i in range(n):
                if not pred[i] & low_mask & ~a:
                    r |= 1 << i
            ready_lo[a] = r
        hi_bits = n - half
        ready_hi = [0] * (1 << hi_bits)
        for b in range(1 << hi_bits):
            r = 0
            shifted = b << half
            for i in range(n):
                if not pred[i] & ~low_mask & ~shifted:
                    r |= 1 << i
            ready_hi[b] = r

        full = (1 << n) - 1
        dp = [-1] * (1 << n)
        dp[0] = 0
        for mask in range(1 << n):
            cur = dp[mask]
            if cur < 0:
                continue
            pos = bin(mask).count("1") + 1
            avail = ready_lo[mask & low_mask] & ready_hi[mask >> half] & ~mask
            while avail:
                bit = avail & -avail
                avail ^= bit
                nxt = mask | bit
                cand = cur + pos * score[bit.bit_length() - 1]
                if cand > dp[nxt]:
                    dp[nxt] = cand
        return dp[full]
