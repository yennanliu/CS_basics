"""

2538. Difference Between Maximum and Minimum Price Sum
Hard

There exists an undirected and initially unrooted tree with n nodes indexed from 0 to n - 1. You are given the integer n and a 2D integer array edges of length n - 1, where edges[i] = [a_i, b_i] indicates that there is an edge between nodes a_i and b_i in the tree.

Each node has an associated price. You are given an integer array price, where price[i] is the price of the ith node.

The price sum of a given path is the sum of the prices of all nodes lying on that path.

The tree can be rooted at any node root of your choice. The incurred cost after choosing root is the difference between the maximum and minimum price sum amongst all paths starting at root.

Return the maximum possible cost amongst all possible root choices.


Example 1:

Input: n = 6, edges = [[0,1],[1,2],[1,3],[3,4],[3,5]], price = [9,8,7,6,10,5]
Output: 24
Explanation: The diagram above denotes the tree after rooting it at node 2. The first part (colored in red) shows the path with the maximum price sum. The second part (colored in blue) shows the path with the minimum price sum.
- The first path contains nodes [2,1,3,4]: the prices are [7,8,6,10], and the sum of the prices is 31.
- The second path contains the node [2] with the price [7].
The difference between the maximum and minimum price sum is 24. It can be proved that 24 is the maximum cost.

Example 2:

Input: n = 3, edges = [[0,1],[1,2]], price = [1,1,1]
Output: 2
Explanation: The diagram above denotes the tree after rooting it at node 0. The first part (colored in red) shows the path with the maximum price sum. The second part (colored in blue) shows the path with the minimum price sum.
- The first path contains nodes [0,1,2]: the prices are [1,1,1], and the sum of the prices is 3.
- The second path contains node [0] with a price [1].
The difference between the maximum and minimum price sum is 2. It can be proved that 2 is the maximum cost.


Constraints:

1 <= n <= 10^5
edges.length == n - 1
0 <= a_i, b_i <= n - 1
edges represents a valid tree.
price.length == n
1 <= price[i] <= 10^5

"""

# V0
# IDEA : TREE DP -- BEST PATH WITH ONE ENDPOINT'S PRICE DROPPED
#
#   all prices are POSITIVE, so the cheapest path starting at `root` is always
#   the single-node path `root` itself, worth price[root]. therefore
#
#       cost(root) = (max path sum from root) - price[root]
#
#   i.e. we want the maximum, over all paths in the tree, of
#   (path sum - price of ONE of its two endpoints). the dropped endpoint is
#   the root, and either end may play that role.
#
#   root the tree anywhere and let, for node i:
#       a[i] = best downward path from i, counting EVERY node (incl. the leaf)
#       b[i] = best downward path from i, EXCLUDING the bottom-most node
#   a leaf has a = price[i], b = 0.
#
#   at node i we merge children one by one; joining the branches seen so far
#   with a new child j gives a full path through i whose two halves are
#   (full, leaf-dropped) or (leaf-dropped, full) :
#       ans = max(ans, a + b[j], b + a[j])
#       a   = max(a, price[i] + a[j])
#       b   = max(b, price[i] + b[j])
#
#   NOTE : n can be 10^5 -> a recursive dfs blows python's stack, so the
#          traversal below is ITERATIVE (push order, then process reversed).
#   NOTE : initialising a = price[i] / b = 0 before the child loop is what lets
#          i itself be the dropped endpoint.
#
# time = O(n), space = O(n)
class Solution(object):
    def maxOutput(self, n, edges, price):
        g = [[] for _ in range(n)]
        for u, v in edges:
            g[u].append(v)
            g[v].append(u)

        # iterative dfs -> `order` is a valid pre-order, reversing it gives post-order
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

        a = [0] * n
        b = [0] * n
        ans = 0
        for u in reversed(order):
            best_full, best_cut = price[u], 0
            for v in g[u]:
                if v == parent[u]:
                    continue
                ans = max(ans, best_full + b[v], best_cut + a[v])
                best_full = max(best_full, price[u] + a[v])
                best_cut = max(best_cut, price[u] + b[v])
            a[u] = best_full
            b[u] = best_cut
        return ans
