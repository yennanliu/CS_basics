"""

1938. Maximum Genetic Difference Query
Hard

There is a rooted tree consisting of n nodes numbered 0 to n - 1. Each node's number denotes its unique genetic value (i.e. the genetic value of node x is x). The genetic difference between two genetic values is defined as the bitwise-XOR of their values. You are given the integer array parents, where parents[i] is the parent for node i. If node x is the root of the tree, then parents[x] == -1.

You are also given the array queries where queries[i] = [nodei, vali]. For each query i, find the maximum genetic difference between vali and pi, where pi is the genetic value of any node that is on the path between nodei and the root (including nodei and the root). More formally, you want to maximize vali XOR pi.

Return an array ans where ans[i] is the answer to the ith query.


Example 1:

Input: parents = [-1,0,1,1], queries = [[0,2],[3,2],[2,5]]
Output: [2,3,7]
Explanation: The queries are processed as follows:
- [0,2]: The node with the maximum genetic difference is 0, with a difference of 2 XOR 0 = 2.
- [3,2]: The node with the maximum genetic difference is 1, with a difference of 2 XOR 1 = 3.
- [2,5]: The node with the maximum genetic difference is 2, with a difference of 5 XOR 2 = 7.

Example 2:

Input: parents = [3,7,-1,2,0,7,0,2], queries = [[4,6],[1,15],[0,5]]
Output: [6,14,7]
Explanation: The queries are processed as follows:
- [4,6]: The node with the maximum genetic difference is 0, with a difference of 6 XOR 0 = 6.
- [1,15]: The node with the maximum genetic difference is 1, with a difference of 15 XOR 1 = 14.
- [0,5]: The node with the maximum genetic difference is 2, with a difference of 5 XOR 2 = 7.


Constraints:

2 <= parents.length <= 10^5
0 <= parents[i] <= parents.length - 1 for every node i that is not the root.
parents[root] == -1
1 <= queries.length <= 3 * 10^4
0 <= nodei <= parents.length - 1
0 <= vali <= 2 * 10^5

"""

# V0
# IDEA : OFFLINE DFS + BINARY TRIE THAT HOLDS EXACTLY THE CURRENT ROOT PATH
#
#   a query at node u may only use values on the path root -> u. so run one DFS
#   over the tree and keep a bit-trie of the values currently on the stack :
#
#     entering u : insert u   (counter +1 along its 18-bit path)
#                  answer every query attached to u with a max-XOR walk
#     leaving  u : delete u   (counter -1) so siblings never see it
#
#   max-XOR walk : from the top bit down, greedily follow the OPPOSITE bit of
#   val whenever that branch still has a live counter; that sets the bit in the
#   answer, otherwise follow the same bit.
#
#   NOTE : reference counts (not booleans) are what make the delete correct
#          when several path values share a prefix.
#   NOTE : depth can be 10^5, so the DFS is ITERATIVE with enter/exit markers.
#
# time = O((n + q) * 18), space = O(n * 18)
class Solution(object):
    def maxGeneticDifference(self, parents, queries):
        BITS = 18                       # values fit in 2 * 10^5 < 2^18

        child = [[-1, -1]]              # trie: child[node][bit]
        cnt = [0]                       # how many live values pass this node

        def update(x, delta):
            cur = 0
            for b in range(BITS - 1, -1, -1):
                v = (x >> b) & 1
                if child[cur][v] == -1:
                    child.append([-1, -1])
                    cnt.append(0)
                    child[cur][v] = len(child) - 1
                cur = child[cur][v]
                cnt[cur] += delta

        def best_xor(x):
            cur = 0
            res = 0
            for b in range(BITS - 1, -1, -1):
                v = (x >> b) & 1
                nxt = child[cur][v ^ 1]
                if nxt != -1 and cnt[nxt] > 0:
                    res |= 1 << b
                    cur = nxt
                else:
                    cur = child[cur][v]
            return res

        n = len(parents)
        children = [[] for _ in range(n)]
        root = 0
        for i, p in enumerate(parents):
            if p == -1:
                root = i
            else:
                children[p].append(i)

        by_node = {}
        for qi in range(len(queries)):
            node, val = queries[qi]
            by_node.setdefault(node, []).append((qi, val))

        res = [0] * len(queries)
        stack = [(root, False)]
        while stack:
            u, leaving = stack.pop()
            if leaving:
                update(u, -1)
                continue
            update(u, 1)
            for qi, val in by_node.get(u, []):
                res[qi] = best_xor(val)
            stack.append((u, True))
            for v in children[u]:
                stack.append((v, False))
        return res
