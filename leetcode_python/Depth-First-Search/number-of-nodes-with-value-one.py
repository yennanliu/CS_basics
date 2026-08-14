"""

2445. Number of Nodes With Value One
Medium

There is an undirected connected tree with n nodes labeled from 1 to n and n - 1 edges. You are given the integer n. The parent node of a node with a label v is the node with the label floor (v / 2). The root of the tree is the node with the label 1.

For example, if n = 7, then the node with the label 3 has the node with the label floor(3 / 2) = 1 as its parent, and the node with the label 7 has the node with the label floor(7 / 2) = 3 as its parent.

You are also given an integer array queries. Initially, every node has a value 0 on it. For each query queries[i], you should flip all values in the subtree of the node with the label queries[i].

Return the total number of nodes with the value 1 after processing all the queries.

Note that:

Flipping the value of a node means that the node with the value 0 becomes 1 and vice versa.
floor(x) is equivalent to rounding x down to the nearest integer.


Example 1:

Input: n = 5 , queries = [1,2,5]
Output: 3
Explanation: The diagram above shows the tree structure and its status after performing the queries. The blue node represents the value 0, and the red node represents the value 1.
After processing the queries, there are three red nodes (nodes with value 1): 1, 3, and 5.

Example 2:

Input: n = 3, queries = [2,3,3]
Output: 1
Explanation: The diagram above shows the tree structure and its status after performing the queries. The blue node represents the value 0, and the red node represents the value 1.
After processing the queries, there are one red node (node with value 1): 2.


Constraints:

1 <= n <= 10^5
1 <= queries.length <= 10^5
1 <= queries[i] <= n

"""

# V0
# IDEA : LAZY FLIP TAG + TOP-DOWN PROPAGATION (heap-indexed tree, parent = i // 2)
#
#   a query on node q flips every node in q's subtree, so put a "tag" of
#   parity 1 on q. two flips on the same node cancel -> only tag % 2 matters.
#
#   the final value of node i is the XOR of all tags on the path root..i,
#   which is exactly:  val[i] = tag[i] XOR val[i // 2]
#
#   because the labels are heap indices, i // 2 < i, so a single ascending
#   sweep i = 1..n computes every val[i] in order - no DFS, no recursion.
#
# time = O(n + q), space = O(n)
from collections import Counter
class Solution(object):
    def numberOfNodes(self, n, queries):
        tag = [0] * (n + 1)
        for q, c in Counter(queries).items():
            tag[q] = c & 1

        val = [0] * (n + 1)
        res = 0
        for i in range(1, n + 1):
            val[i] = tag[i] ^ (val[i >> 1] if i > 1 else 0)
            res += val[i]
        return res
