"""

1519. Number of Nodes in the Sub-Tree With the Same Label
Medium

You are given a tree (i.e. a connected, undirected graph that has no cycles) consisting of n nodes numbered from 0 to n - 1 and exactly n - 1 edges. The root of the tree is the node 0, and each node of the tree has a label which is a lower-case character given in the string labels (i.e. The node with the number i has the label labels[i]).

The edges array is given on the form edges[i] = [a_i, b_i], which means there is an edge between nodes a_i and b_i in the tree.

Return an array of size n where ans[i] is the number of nodes in the subtree of the ith node which have the same label as node i.

A subtree of a tree T is the tree consisting of a node in T and all of its descendant nodes.


Example 1:

Input: n = 7, edges = [[0,1],[0,2],[1,4],[1,5],[2,3],[2,6]], labels = "abaedcd"
Output: [2,1,1,1,1,1,1]
Explanation: Node 0 has label 'a' and its sub-tree has node 2 with label 'a' as well, thus the answer is 2. Notice that any node is part of its sub-tree.
Node 1 has a label 'b'. The sub-tree of node 1 contains nodes 1,4 and 5, as nodes 4 and 5 have different labels than node 1, the answer is just 1 (the node itself).

Example 2:

Input: n = 4, edges = [[0,1],[1,2],[0,3]], labels = "bbbb"
Output: [4,2,1,1]
Explanation: The sub-tree of node 2 contains only node 2, so the answer is 1.
The sub-tree of node 3 contains only node 3, so the answer is 1.
The sub-tree of node 1 contains nodes 1 and 2, both have label 'b', thus the answer is 2.
The sub-tree of node 0 contains nodes 0, 1, 2 and 3, all with label 'b', thus the answer is 4.

Example 3:

Input: n = 5, edges = [[0,1],[0,2],[1,3],[0,4]], labels = "aabab"
Output: [3,2,1,1,1]


Constraints:

1 <= n <= 10^5
edges.length == n - 1
edges[i].length == 2
0 <= a_i, b_i < n
a_i != b_i
labels.length == n
labels is consisting of only of lowercase English letters.

"""

# V0
# IDEA : POST-ORDER DFS, MERGING 26-SLOT LABEL COUNTERS UPWARDS
#
#   for every node keep a 26-length array of label counts of its subtree.
#   process nodes in reverse DFS pre-order (= a valid post-order), so all
#   children of a node are finished before the node itself :
#
#     cnt[u] = own label + sum of cnt[child]
#     res[u] = cnt[u][labels[u]]
#
#   NOTE : n can be 10^5 and the tree may be a single 10^5-long chain, so
#          the traversal is ITERATIVE — plain recursion would overflow the
#          Python stack.
#
# time = O(26 * n), space = O(26 * n)
class Solution(object):
    def countSubTrees(self, n, edges, labels):
        g = [[] for _ in range(n)]
        for a, b in edges:
            g[a].append(b)
            g[b].append(a)

        # iterative DFS -> pre-order list + parent of each node
        parent = [-1] * n
        order = []
        seen = [False] * n
        seen[0] = True
        stack = [0]
        while stack:
            u = stack.pop()
            order.append(u)
            for v in g[u]:
                if not seen[v]:
                    seen[v] = True
                    parent[v] = u
                    stack.append(v)

        res = [0] * n
        cnt = [None] * n
        for u in reversed(order):
            cur = cnt[u]
            if cur is None:
                cur = [0] * 26
            k = ord(labels[u]) - ord('a')
            cur[k] += 1
            res[u] = cur[k]

            p = parent[u]
            if p >= 0:
                if cnt[p] is None:
                    cnt[p] = [0] * 26
                pc = cnt[p]
                for i in range(26):
                    pc[i] += cur[i]
            cnt[u] = None   # free it, the parent already absorbed it
        return res
