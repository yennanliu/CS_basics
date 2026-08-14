"""

2479. Maximum XOR of Two Non-Overlapping Subtrees
Hard

There is an undirected tree with n nodes labeled from 0 to n - 1. You are given the integer n and a 2D integer array edges of length n - 1, where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the tree. The root of the tree is the node labeled 0.

Each node has an associated value. You are given an array values of length n, where values[i] is the value of the ith node.

Select any two non-overlapping subtrees. Your score is the bitwise XOR of the sum of the values within those subtrees.

Return the maximum possible score you can achieve. If it is impossible to find two nonoverlapping subtrees, return 0.

Note that:

The subtree of a node is the tree consisting of that node and all of its descendants.
Two subtrees are non-overlapping if they do not share any common node.


Example 1:

Input: n = 6, edges = [[0,1],[0,2],[1,3],[1,4],[2,5]], values = [2,8,3,6,2,5]
Output: 24
Explanation: Node 1's subtree has sum of values 16, while node 2's subtree has sum of values 8, so choosing these nodes will yield a score of 16 XOR 8 = 24. It can be proved that is the maximum possible score we can obtain.

Example 2:

Input: n = 3, edges = [[0,1],[1,2]], values = [4,6,1]
Output: 0
Explanation: There is no possible way to select two non-overlapping subtrees, so we just return 0.


Constraints:

2 <= n <= 5 * 10^4
edges.length == n - 1
0 <= ai, bi < n
values.length == n
1 <= values[i] <= 10^9
It is guaranteed that edges represents a valid tree.

"""

# V0
# IDEA : SUBTREE SUMS + BINARY TRIE, QUERY ON ENTRY / INSERT ON EXIT
#
#   two subtrees are non-overlapping iff neither root is an ancestor of the
#   other. walk the tree so that node v is QUERIED when we first reach it and
#   only INSERTED after its whole subtree is done. at query time the trie then
#   holds exactly the fully-finished subtrees -- none of them is an ancestor
#   (not inserted yet) nor a descendant (not visited yet) of v.
#   a binary trie over the 46-bit subtree sums answers "max xor with s[v]".
#   NOTE : n up to 5 * 10^4 and the tree may be a chain -> both passes are
#          written ITERATIVELY.
#
# time = O(n * B), space = O(n * B), B = 46 bits
class Solution(object):
    def maxXor(self, n, edges, values):
        g = [[] for _ in range(n)]
        for a, b in edges:
            g[a].append(b)
            g[b].append(a)

        # ---- pass 1 : subtree sums (iterative, children before parents)
        parent = [-1] * n
        order = [0]
        seen = [False] * n
        seen[0] = True
        i = 0
        while i < len(order):
            a = order[i]
            i += 1
            for b in g[a]:
                if not seen[b]:
                    seen[b] = True
                    parent[b] = a
                    order.append(b)

        s = values[:]
        for idx in range(len(order) - 1, 0, -1):
            v = order[idx]
            s[parent[v]] += s[v]

        # ---- pass 2 : pre-order query / post-order insert against a trie
        B = 46
        ch = [[0, 0]]                    # ch[node] = [child0, child1], 0 = empty

        def insert(x):
            cur = 0
            for i in range(B - 1, -1, -1):
                b = (x >> i) & 1
                if ch[cur][b] == 0:
                    ch.append([0, 0])
                    ch[cur][b] = len(ch) - 1
                cur = ch[cur][b]

        def query(x):
            if ch[0][0] == 0 and ch[0][1] == 0:
                return -1                # trie is empty
            cur = 0
            res = 0
            for i in range(B - 1, -1, -1):
                b = (x >> i) & 1
                if ch[cur][b ^ 1]:
                    res |= 1 << i
                    cur = ch[cur][b ^ 1]
                else:
                    cur = ch[cur][b]
            return res

        res = 0
        stack = [(0, -1, False)]
        while stack:
            v, fa, done = stack.pop()
            if done:
                insert(s[v])
                continue
            t = query(s[v])
            if t > res:
                res = t
            stack.append((v, fa, True))
            for b in g[v]:
                if b != fa:
                    stack.append((b, v, False))
        return res
