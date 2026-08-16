"""

3590. Kth Smallest Path XOR Sum
Hard

You are given an undirected tree rooted at node 0 with n nodes numbered from 0
to n - 1. Each node i has an integer value vals[i], and its parent is given by
par[i].

The path XOR sum from the root to a node u is defined as the bitwise XOR of all
vals[i] for nodes i on the path from the root node to node u, inclusive.

You are given a 2D integer array queries, where queries[j] = [u_j, k_j]. For
each query, find the k_j-th smallest distinct path XOR sum among all nodes in
the subtree rooted at u_j. If there are fewer than k_j distinct path XOR sums
in that subtree, the answer is -1.

Return an integer array where the j-th element is the answer to the j-th query.

In a rooted tree, the subtree of a node v includes v and all nodes whose path
to the root passes through v, that is, v and its descendants.


Example 1:

Input: par = [-1,0,0], vals = [1,1,1], queries = [[0,1],[0,2],[0,3]]
Output: [0,1,-1]
Explanation:

Path XORs:
Node 0: 1
Node 1: 1 XOR 1 = 0
Node 2: 1 XOR 1 = 0

Subtree of 0: Subtree rooted at node 0 includes nodes [0, 1, 2] with Path XORs
= [1, 0, 0]. The distinct XORs are [0, 1].

Queries:
queries[0] = [0, 1]: The 1st smallest distinct path XOR in the subtree of node
0 is 0.
queries[1] = [0, 2]: The 2nd smallest distinct path XOR in the subtree of node
0 is 1.
queries[2] = [0, 3]: Since there are only two distinct path XORs in this
subtree, the answer is -1.

Output: [0, 1, -1]

Example 2:

Input: par = [-1,0,1], vals = [5,2,7], queries = [[0,1],[1,2],[1,3],[2,1]]
Output: [0,7,-1,0]
Explanation:

Path XORs:
Node 0: 5
Node 1: 5 XOR 2 = 7
Node 2: 5 XOR 2 XOR 7 = 0

Subtrees and Distinct Path XORs:
Subtree of 0: Subtree rooted at node 0 includes nodes [0, 1, 2] with Path XORs
= [5, 7, 0]. The distinct XORs are [0, 5, 7].
Subtree of 1: Subtree rooted at node 1 includes nodes [1, 2] with Path XORs =
[7, 0]. The distinct XORs are [0, 7].
Subtree of 2: Subtree rooted at node 2 includes only node [2] with Path XOR =
[0]. The distinct XORs are [0].

Queries:
queries[0] = [0, 1]: The 1st smallest distinct path XOR in the subtree of node
0 is 0.
queries[1] = [1, 2]: The 2nd smallest distinct path XOR in the subtree of node
1 is 7.
queries[2] = [1, 3]: Since there are only two distinct path XORs, the answer is
-1.
queries[3] = [2, 1]: The 1st smallest distinct path XOR in the subtree of node
2 is 0.

Output: [0, 7, -1, 0]


Constraints:

1 <= n == vals.length <= 5 * 10^4
0 <= vals[i] <= 10^5
par.length == n
par[0] == -1
0 <= par[i] < n for i in [1, n - 1]
1 <= queries.length <= 5 * 10^4
queries[j] == [u_j, k_j]
0 <= u_j < n
1 <= k_j <= n
The input is generated such that the parent array par represents a valid tree.

"""

# V0
# IDEA : MERGEABLE BINARY TRIE OVER THE XOR VALUES, MERGED BOTTOM-UP
#
#   answering a query needs an order statistic over the distinct path xors of
#   one subtree, and a subtree's multiset is just the union of its children's
#   plus its own value. that suggests a structure that can be unioned cheaply
#   rather than rebuilt: a binary trie on the 17 bits of the value (vals[i] and
#   therefore every xor stay below 2^17), where each node stores how many
#   distinct values live beneath it.
#
#   the union is the segment-tree-merge trick. merging two tries walks only the
#   nodes they have in common and splices the rest in whole; every recursive
#   step permanently destroys one node, so the total work across the entire
#   traversal is bounded by the number of nodes ever created, O(n * 17), no
#   matter how lopsided the tree is. this is what makes it strictly better than
#   small-to-large, which would re-insert values repeatedly.
#
#   distinctness falls out for free at the bottom of the merge: when both sides
#   reach the same leaf they represent the same value, so one of them is simply
#   dropped and the count stays 1. every internal count is then recomputed from
#   its two children, which keeps the subtree counts exactly "distinct values".
#
#   with counts in place the k-th smallest is a descent from the root: the zero
#   branch covers the smaller half of the range, so if k fits inside its count
#   go left, otherwise subtract that count, set the bit and go right. answering
#   at the moment the merge for that node finishes means each trie is consumed
#   exactly once, so the queries are grouped by node and served offline.
#
#   the traversal itself is iterative, since a path-shaped tree of 5 * 10^4
#   nodes would blow the recursion limit.
#
# time = O((n + q) * log(max_val)), space = O(n * log(max_val))
from collections import defaultdict


class Solution(object):
    def kthSmallest(self, par, vals, queries):
        n = len(par)
        BITS = 17  # vals[i] <= 10^5 < 2^17, and xor keeps values in range

        children = [[] for _ in range(n)]
        for i in range(1, n):
            children[par[i]].append(i)

        # pre-order sweep gives every node's xor from the root
        xor = [0] * n
        xor[0] = vals[0]
        stack = [0]
        order = []
        while stack:
            x = stack.pop()
            order.append(x)
            for y in children[x]:
                xor[y] = xor[x] ^ vals[y]
                stack.append(y)

        # flat trie storage; node 0 is the shared null node with count 0
        cap = n * (BITS + 1) + 1
        zero = [0] * cap
        one = [0] * cap
        cnt = [0] * cap
        used = [1]

        def insert(v):
            free = used[0]
            root = free
            for b in range(BITS - 1, -1, -1):
                cnt[free] = 1
                nxt = free + 1
                if (v >> b) & 1:
                    one[free] = nxt
                else:
                    zero[free] = nxt
                free = nxt
            cnt[free] = 1
            used[0] = free + 1
            return root

        def merge(a, b, bit):
            if a == 0:
                return b
            if b == 0:
                return a
            if bit == 0:
                return a  # same leaf, same value: keep one copy
            zero[a] = merge(zero[a], zero[b], bit - 1)
            one[a] = merge(one[a], one[b], bit - 1)
            cnt[a] = cnt[zero[a]] + cnt[one[a]]
            return a

        def kth(root, k):
            if cnt[root] < k:
                return -1
            node = root
            res = 0
            for b in range(BITS - 1, -1, -1):
                lo = zero[node]
                c = cnt[lo]
                if k <= c:
                    node = lo
                else:
                    k -= c
                    res |= 1 << b
                    node = one[node]
            return res

        asked = defaultdict(list)
        for i, (u, k) in enumerate(queries):
            asked[u].append((k, i))

        ans = [0] * len(queries)
        root_of = [0] * n
        for x in reversed(order):  # reverse pre-order is a valid post-order
            r = insert(xor[x])
            for y in children[x]:
                r = merge(r, root_of[y], BITS)
                root_of[y] = 0
            root_of[x] = r
            for k, i in asked[x]:
                ans[i] = kth(r, k)
        return ans
