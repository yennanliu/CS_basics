"""

2673. Make Costs of Paths Equal in a Binary Tree
Medium

You are given an integer n representing the number of nodes in a perfect binary tree consisting of nodes numbered from 1 to n. The root of the tree is node 1 and each node i in the tree has two children where the left child is the node 2 * i and the right child is 2 * i + 1.

Each node in the tree also has a cost represented by a given 0-indexed integer array cost of size n where cost[i] is the cost of node i + 1. You are allowed to increment the cost of any node by 1 any number of times.

Return the minimum number of increments you need to make the cost of paths from the root to each leaf node equal.

Note:

A perfect binary tree is a tree where each node, except the leaf nodes, has exactly 2 children.
The cost of a path is the sum of costs of nodes in the path.


Example 1:

Input: n = 7, cost = [1,5,2,2,3,3,1]
Output: 6
Explanation: We can do the following increments:
- Increase the cost of node 4 one time.
- Increase the cost of node 3 three times.
- Increase the cost of node 7 two times.
Each path from the root to a leaf will have a total cost of 9.
The total increments we did is 1 + 3 + 2 = 6.
It can be shown that this is the minimum answer we can achieve.

Example 2:

Input: n = 3, cost = [5,3,3]
Output: 0
Explanation: The two paths already have equal total costs, so no increments are needed.


Constraints:

3 <= n <= 10^5
n + 1 is a power of 2
cost.length == n
1 <= cost[i] <= 10^4

"""

# V0
# IDEA : BOTTOM-UP GREEDY ON THE HEAP-INDEXED PERFECT TREE
#
#   root-to-leaf sums are all equal  <=>  for EVERY node, the two subtrees
#   hanging off it have equal "node -> leaf" depths. (If some node's two
#   sides differed, the two leaves under it would give different root sums.)
#
#   so process nodes bottom-up, and let h[v] = max cost of a path from v
#   down to a leaf, AFTER v's subtree has been fixed. For an internal node
#   v with children l and r:
#     - the cheapest way to equalize the two sides is to raise the smaller
#       side by exactly |h[l] - h[r]|, i.e. add that many increments; there
#       is no cheaper option because the deeper side can never be lowered.
#     - afterwards both sides equal max(h[l], h[r]), so
#       h[v] = cost[v] + max(h[l], h[r]).
#
#   NOTE : it is always optimal to spend the increments on the CHILD (one
#          bump lifts its whole subtree at once) rather than on individual
#          leaves -- which is why the answer is just the sum of |diff| over
#          all internal nodes, independent of which node we actually bump.
#
#   NOTE : heap indexing -- node v (1-based) has children 2v and 2v+1, and
#          its cost lives at cost[v-1]. In a perfect tree with n nodes the
#          internal nodes are exactly 1 .. n//2, and iterating v from n//2
#          DOWN to 1 visits every node after both of its children, so no
#          recursion is needed (n can be 10^5).
#
#   NOTE : we fold h[v] back into cost[v-1] in place, so cost doubles as
#          the h array -- work on a copy to avoid mutating the caller's list.
#
# time = O(n), space = O(n) for the working copy
class Solution(object):
    def minIncrements(self, n, cost):
        h = list(cost)
        res = 0
        for v in range(n // 2, 0, -1):
            l, r = h[2 * v - 1], h[2 * v]
            res += abs(l - r)
            h[v - 1] += l if l > r else r
        return res
