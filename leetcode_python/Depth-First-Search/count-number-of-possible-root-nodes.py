"""

2581. Count Number of Possible Root Nodes
Hard

Alice has an undirected tree with n nodes labeled from 0 to n - 1. The tree is represented as a
2D integer array edges of length n - 1 where edges[i] = [ai, bi] indicates that there is an edge
between nodes ai and bi in the tree.

Alice wants Bob to find the root of the tree. She allows Bob to make several guesses about her
tree. In one guess, he does the following:

Chooses two distinct integers u and v such that there exists an edge [u, v] in the tree.
He tells Alice that u is the parent of v in the tree.

Bob's guesses are represented by a 2D integer array guesses where guesses[j] = [uj, vj] indicates
Bob guessed uj to be the parent of vj.

Alice being lazy, does not reply to each of Bob's guesses, but just says that at least k of his
guesses are true.

Given the 2D integer arrays edges, guesses and the integer k, return the number of possible nodes
that can be the root of Alice's tree. If there is no such tree, return 0.


Example 1:

Input: edges = [[0,1],[1,2],[1,3],[4,2]], guesses = [[1,3],[0,1],[1,0],[2,4]], k = 3
Output: 3
Explanation:
Root = 0, correct guesses = [1,3], [0,1], [2,4]
Root = 1, correct guesses = [1,3], [1,0], [2,4]
Root = 2, correct guesses = [1,3], [1,0], [2,4]
Root = 3, correct guesses = [1,0], [2,4]
Root = 4, correct guesses = [1,3], [1,0]
Considering 0, 1, or 2 as root node leads to 3 correct guesses.

Example 2:

Input: edges = [[0,1],[1,2],[2,3],[3,4]], guesses = [[1,0],[3,4],[2,1],[3,2]], k = 1
Output: 5
Explanation:
Root = 0, correct guesses = [3,4]
Root = 1, correct guesses = [1,0], [3,4]
Root = 2, correct guesses = [1,0], [2,1], [3,4]
Root = 3, correct guesses = [1,0], [2,1], [3,2], [3,4]
Root = 4, correct guesses = [1,0], [2,1], [3,2]
Considering any node as root will give at least 1 correct guess.


Constraints:

edges.length == n - 1
2 <= n <= 10^5
1 <= guesses.length <= 10^5
0 <= ai, bi, uj, vj <= n - 1
ai != bi
uj != vj
edges represents a valid tree.
guesses[j] is an edge of the tree.
guesses is unique.
0 <= k <= guesses.length

"""

# V0
# IDEA : REROOTING TREE DP (2 passes, iterative)
#
#   score(r) = how many guesses (u, v) are "u is parent of v" when the tree is
#   rooted at r. Recomputing score for every r is O(n^2) — too slow. But moving
#   the root along ONE edge only flips the parent/child relation of that single
#   edge, everything else is untouched. So:
#
#       score(child) = score(parent)
#                      - ((parent, child) in guesses)     # this guess dies
#                      + ((child, parent) in guesses)     # this guess is born
#
#   pass 1 : root at node 0, count score(0) directly.
#   pass 2 : walk the tree from 0 and propagate the score with the delta above,
#            counting how many nodes reach score >= k.
#
#   NOTE : guesses are unique, so a plain SET of (u, v) tuples is enough — no
#          need to count multiplicities.
#
#   NOTE : n can be 10^5, so a recursive DFS would blow python's stack. Both
#          passes here are ITERATIVE: we compute one BFS order from node 0 with
#          its parent array, then just sweep that order (parents always come
#          before their children, so score(parent) is ready when we need it).
#
# time = O(n + m), space = O(n + m)   (m = len(guesses))
from collections import deque
class Solution(object):
    def rootCount(self, edges, guesses, k):
        n = len(edges) + 1
        g = [[] for _ in range(n)]
        for a, b in edges:
            g[a].append(b)
            g[b].append(a)

        gs = set()
        for u, v in guesses:
            gs.add((u, v))

        # BFS from node 0 -> visiting order + parent of each node
        parent = [-1] * n
        order = [0]
        visited = [False] * n
        visited[0] = True
        q = deque([0])
        while q:
            i = q.popleft()
            for j in g[i]:
                if not visited[j]:
                    visited[j] = True
                    parent[j] = i
                    order.append(j)
                    q.append(j)

        # pass 1 : score when the tree is rooted at node 0
        score = [0] * n
        base = 0
        for v in order[1:]:
            if (parent[v], v) in gs:
                base += 1
        score[0] = base

        # pass 2 : reroot along each edge (parents come first in `order`)
        res = 1 if score[0] >= k else 0
        for v in order[1:]:
            p = parent[v]
            s = score[p]
            if (p, v) in gs:
                s -= 1
            if (v, p) in gs:
                s += 1
            score[v] = s
            if s >= k:
                res += 1
        return res
