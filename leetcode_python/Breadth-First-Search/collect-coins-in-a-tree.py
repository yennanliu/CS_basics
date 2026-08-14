"""

2603. Collect Coins in a Tree
Hard

There exists an undirected and unrooted tree with n nodes indexed from 0 to n - 1. You are given an integer n and a 2D integer array edges of length n - 1, where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the tree. You are also given an array coins of size n where coins[i] can be either 0 or 1, where 1 indicates the presence of a coin in the vertex i.

Initially, you choose to start at any vertex in the tree. Then, you can perform the following operations any number of times:

- Collect all the coins that are at a distance of at most 2 from the current vertex, or
- Move to any adjacent vertex in the tree.

Find the minimum number of edges you need to go through to collect all the coins and go back to the initial vertex.

Note that if you pass an edge several times, you need to count it into the answer several times.


Example 1:

Input: coins = [1,0,0,0,0,1], edges = [[0,1],[1,2],[2,3],[3,4],[4,5]]
Output: 2
Explanation: Start at vertex 2, collect the coin at vertex 0, move to vertex 3, collect the coin at vertex 5 then move back to vertex 2.

Example 2:

Input: coins = [0,0,0,1,1,0,0,1], edges = [[0,1],[0,2],[1,3],[1,4],[2,5],[5,6],[5,7]]
Output: 2
Explanation: Start at vertex 0, collect the coins at vertices 4 and 3, move to vertex 2, collect the coin at vertex 7, then move back to vertex 0.


Constraints:

n == coins.length
1 <= n <= 3 * 10^4
0 <= coins[i] <= 1
edges.length == n - 1
edges[i].length == 2
0 <= ai, bi < n
ai != bi
edges represents a valid tree.

"""

from collections import deque

# V0
# IDEA : TOPOLOGICAL (LEAF) PRUNING x 2 ROUNDS
#
#   whatever route we walk, it is a closed walk over some connected subtree S,
#   and its length is exactly 2 * (number of edges of S) — every edge of a tree
#   walk is used once down and once back up.
#
#   so the task becomes: shrink the tree down to the smallest subtree that
#   still lets us see every coin from distance <= 2. two prunings do it:
#
#   1) repeatedly strip leaves that carry NO coin — nothing is lost by never
#      walking into them (a coin-free dead end). this is a topological peel
#      with a queue, exactly like Kahn's algorithm on degree-1 nodes.
#
#   2) now every leaf holds a coin. a coin sitting on a leaf can be collected
#      from 2 edges away, so strip the outermost leaf layer TWICE — but as two
#      *simultaneous* layer removals, not another cascading peel.
#
#   NOTE : the two layers must be removed level-by-level (snapshot all current
#          leaves, then delete them together). a cascading peel would eat far
#          too much of a long path.
#   NOTE : answer = 2 * (# edges whose BOTH endpoints survive).
#          if everything is pruned away, that count is 0 — correct, we never
#          have to move at all.
#   NOTE : n up to 3e4, so the peel is done iteratively (no recursion).
#
# time = O(n), space = O(n)
class Solution(object):
    def collectTheCoins(self, coins, edges):
        n = len(coins)
        g = [set() for _ in range(n)]
        for a, b in edges:
            g[a].add(b)
            g[b].add(a)

        # 1) cascade-remove coin-free leaves
        q = deque(i for i in range(n) if len(g[i]) == 1 and coins[i] == 0)
        while q:
            i = q.popleft()
            for j in g[i]:
                g[j].discard(i)
                if coins[j] == 0 and len(g[j]) == 1:
                    q.append(j)
            g[i].clear()

        # 2) peel exactly 2 whole leaf layers
        for _ in range(2):
            layer = [i for i in range(n) if len(g[i]) == 1]
            for i in layer:
                for j in g[i]:
                    g[j].discard(i)
                g[i].clear()

        res = 0
        for a, b in edges:
            if g[a] and g[b]:
                res += 2
        return res
