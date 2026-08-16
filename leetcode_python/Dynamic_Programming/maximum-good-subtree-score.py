"""

3575. Maximum Good Subtree Score
Hard

You are given an undirected tree rooted at node 0 with n nodes numbered
from 0 to n - 1. Each node i has an integer value vals[i], and its parent is
given by par[i].

A subset of nodes within the subtree of a node is called good if every digit
from 0 to 9 appears at most once in the decimal representation of the values
of the selected nodes.

The score of a good subset is the sum of the values of its nodes.

Define an array maxScore of length n, where maxScore[u] represents the
maximum possible sum of values of a good subset of nodes that belong to the
subtree rooted at node u, including u itself and all its descendants.

Return the sum of all values in maxScore.

Since the answer may be large, return it modulo 10^9 + 7.


Example 1:

Input: vals = [2,3], par = [-1,0]
Output: 8
Explanation:
- The subtree rooted at node 0 includes nodes {0, 1}. The subset {2, 3} is
  good as the digits 2 and 3 appear only once. The score of this subset is
  2 + 3 = 5.
- The subtree rooted at node 1 includes only node {1}. The subset {3} is
  good. The score of this subset is 3.
- The maxScore array is [5, 3], and the sum of all values in maxScore is
  5 + 3 = 8. Thus, the answer is 8.

Example 2:

Input: vals = [1,5,2], par = [-1,0,0]
Output: 15
Explanation:
- The subtree rooted at node 0 includes nodes {0, 1, 2}. The subset
  {1, 5, 2} is good as the digits 1, 5 and 2 appear only once. The score of
  this subset is 1 + 5 + 2 = 8.
- The subtree rooted at node 1 includes only node {1}. The subset {5} is
  good. The score of this subset is 5.
- The subtree rooted at node 2 includes only node {2}. The subset {2} is
  good. The score of this subset is 2.
- The maxScore array is [8, 5, 2], and the sum of all values in maxScore is
  8 + 5 + 2 = 15. Thus, the answer is 15.

Example 3:

Input: vals = [34,1,2], par = [-1,0,1]
Output: 42
Explanation:
- The subtree rooted at node 0 includes nodes {0, 1, 2}. The subset
  {34, 1, 2} is good as the digits 3, 4, 1 and 2 appear only once. The score
  of this subset is 34 + 1 + 2 = 37.
- The subtree rooted at node 1 includes node {1, 2}. The subset {1, 2} is
  good as the digits 1 and 2 appear only once. The score of this subset is
  1 + 2 = 3.
- The subtree rooted at node 2 includes only node {2}. The subset {2} is
  good. The score of this subset is 2.
- The maxScore array is [37, 3, 2], and the sum of all values in maxScore is
  37 + 3 + 2 = 42. Thus, the answer is 42.

Example 4:

Input: vals = [3,22,5], par = [-1,0,1]
Output: 18
Explanation:
- The subtree rooted at node 0 includes nodes {0, 1, 2}. The subset
  {3, 22, 5} is not good, as digit 2 appears twice. Therefore, the subset
  {3, 5} is valid. The score of this subset is 3 + 5 = 8.
- The subtree rooted at node 1 includes nodes {1, 2}. The subset {22, 5} is
  not good, as digit 2 appears twice. Therefore, the subset {5} is valid.
  The score of this subset is 5.
- The subtree rooted at node 2 includes {2}. The subset {5} is good. The
  score of this subset is 5.
- The maxScore array is [8, 5, 5], and the sum of all values in maxScore is
  8 + 5 + 5 = 18. Thus, the answer is 18.


Constraints:

1 <= n == vals.length <= 500
1 <= vals[i] <= 10^9
par.length == n
par[0] == -1
0 <= par[i] < n for i in [1, n - 1]
The input is generated such that the parent array par represents a valid
tree.

"""

# V0
# IDEA : DIGIT-MASK KNAPSACK PER SUBTREE, REUSED VIA SMALL-TO-LARGE MERGING
#
#   a value is usable at all only if its own decimal digits are distinct;
#   otherwise it can never sit in a good subset. every usable value becomes
#   an item with a 10-bit digit mask, and a good subset is just a collection
#   of items with pairwise disjoint masks. so maxScore[u] is a 0/1 knapsack
#   over the items of subtree(u), with "weight" = mask and the constraint
#   being disjointness: dp[S] = best sum whose digits are exactly S, and an
#   item (m, v) updates dp[S | m] from dp[S] for every S disjoint from m.
#
#   the point that makes this cheap is that such a knapsack is *incremental
#   and order free* — adding items one at a time, in any order, gives the
#   same table. so a parent does not have to rebuild from scratch: it can
#   adopt the finished table of its largest child and then feed in only the
#   items of its remaining (smaller) children plus its own. that is the
#   standard small-to-large argument, and it bounds the number of item
#   insertions by O(n log n) instead of the O(n^2) of "one knapsack per
#   subtree" (a path-shaped tree is the case it rescues).
#
#   note we never have to delete a dominated item: two items with the same
#   mask collide, so a packing uses at most one of them and automatically
#   the larger. an empty subset is good, hence dp[0] = 0 and every
#   maxScore[u] is at least 0.
#
# time = O(n log n * 2^10), space = O(n * 2^10)
class Solution(object):
    def goodSubtreeSum(self, vals, par):
        MOD = 10 ** 9 + 7
        FULL = 1 << 10
        NEG = -1 << 60

        n = len(vals)
        kids = [[] for _ in range(n)]
        for i in range(n):
            if par[i] >= 0:
                kids[par[i]].append(i)

        # digit mask of each value, or -1 when a digit repeats
        masks = []
        for v in vals:
            m, x = 0, v
            while x:
                bit = 1 << (x % 10)
                if m & bit:
                    m = -1
                    break
                m |= bit
                x //= 10
            masks.append(m)

        # iterative dfs -> preorder plus subtree ranges
        order = []
        start = [0] * n
        stop = [0] * n
        stack = [(0, False)]
        while stack:
            u, done = stack.pop()
            if done:
                stop[u] = len(order)
                continue
            start[u] = len(order)
            order.append(u)
            stack.append((u, True))
            for c in kids[u]:
                stack.append((c, False))

        # complementary masks are shared by every insertion of the same mask
        free = {}

        def absorb(dp, w):
            m = masks[w]
            if m < 0:
                return
            v = vals[w]
            spots = free.get(m)
            if spots is None:
                spots = free[m] = [s for s in range(FULL) if not (s & m)]
            for s in spots:
                base = dp[s]
                if base >= 0 and base + v > dp[s | m]:
                    dp[s | m] = base + v

        tables = {}
        total = 0
        # a parent always appears before its descendants in preorder, so the
        # reversed order is a valid bottom-up sweep
        for u in reversed(order):
            heavy = -1
            for c in kids[u]:
                if heavy < 0 or stop[c] - start[c] > stop[heavy] - start[heavy]:
                    heavy = c

            if heavy < 0:
                dp = [NEG] * FULL
                dp[0] = 0
            else:
                dp = tables.pop(heavy)

            absorb(dp, u)
            for c in kids[u]:
                if c != heavy:
                    tables.pop(c, None)
                    for w in order[start[c]:stop[c]]:
                        absorb(dp, w)

            total += max(dp)
            tables[u] = dp

        return total % MOD
