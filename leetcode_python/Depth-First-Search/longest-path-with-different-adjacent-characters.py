"""

2246. Longest Path With Different Adjacent Characters
Hard

You are given a tree (i.e. a connected, undirected graph that has no cycles) rooted at node 0 consisting of n nodes numbered from 0 to n - 1. The tree is represented by a 0-indexed array parent of size n, where parent[i] is the parent of node i. Since node 0 is the root, parent[0] == -1.

You are also given a string s of length n, where s[i] is the character assigned to node i.

Return the length of the longest path in the tree such that no pair of adjacent nodes on the path have the same character assigned to them.


Example 1:

Input: parent = [-1,0,0,1,1,2], s = "abacbe"
Output: 3
Explanation: The longest path where each two adjacent nodes have different characters in the tree is the path: 0 -> 1 -> 3. The length of this path is 3, so 3 is returned.
It can be proven that there is no longer path that satisfies the conditions.

Example 2:

Input: parent = [-1,0,0,0], s = "aabc"
Output: 3
Explanation: The longest path where each two adjacent nodes have different characters is the path: 2 -> 0 -> 3. The length of this path is 3, so 3 is returned.


Constraints:

n == parent.length == s.length
1 <= n <= 10^5
0 <= parent[i] <= n - 1 for all i >= 1
parent[0] == -1
parent represents a valid tree.
s consists of only lowercase English letters.

"""

# V0
# IDEA : TREE DP — AT EACH NODE, JOIN ITS TWO BEST DOWNWARD CHAINS
#
#   down[u] = longest valid chain that STARTS at u and goes strictly down.
#   a child v may extend it only when s[v] != s[u], so
#       down[u] = 1 + max(down[v] over usable children, or 0)
#
#   the longest path THROUGH u bends at u and uses its two best usable
#   children :
#       1 + best1 + best2
#   tracking the max of that over all u answers the problem (every path has a
#   unique highest node).
#
#   NOTE : n is up to 10^5, so the traversal is ITERATIVE — process the nodes
#          in reverse BFS order, which guarantees children before parents and
#          avoids blowing the recursion limit.
#
# time = O(n), space = O(n)
class Solution(object):
    def longestPath(self, parent, s):
        n = len(parent)
        children = [[] for _ in range(n)]
        for i in range(1, n):
            children[parent[i]].append(i)

        # BFS order from the root; reversing it gives a valid post-order
        order = [0]
        qi = 0
        while qi < len(order):
            u = order[qi]
            qi += 1
            order.extend(children[u])

        down = [1] * n
        res = 1
        for u in reversed(order):
            best1 = best2 = 0
            for v in children[u]:
                if s[v] == s[u]:
                    continue
                d = down[v]
                if d > best1:
                    best1, best2 = d, best1
                elif d > best2:
                    best2 = d
            down[u] = 1 + best1
            res = max(res, 1 + best1 + best2)
        return res
