"""

3544. Subtree Inversion Sum
Hard

You are given an undirected tree rooted at node 0, with n nodes numbered from 0
to n - 1. The tree is represented by a 2D integer array edges of length n - 1,
where edges[i] = [u_i, v_i] indicates an edge between nodes u_i and v_i.

You are also given an integer array nums of length n, where nums[i] represents
the value at node i, and an integer k.

You may perform inversion operations on a subset of nodes subject to the
following rules:

Subtree Inversion Operation:

When you invert a node, every value in the subtree rooted at that node is
multiplied by -1.

Distance Constraint on Inversions:

You may only invert a node if it is "sufficiently far" from any other inverted
node.

Specifically, if you invert two nodes a and b such that one is an ancestor of
the other (i.e., if LCA(a, b) = a or LCA(a, b) = b), then the distance (the
number of edges on the unique path between them) must be at least k.

Return the maximum possible sum of the tree's node values after applying
inversion operations.

Example 1:

Input: edges = [[0,1],[0,2],[1,3],[1,4],[2,5],[2,6]], nums = [4,-8,-6,3,7,-2,5],
k = 2

Output: 27

Explanation:

Apply inversion operations at nodes 0, 3, 4 and 6.

The final nums array is [-4, 8, 6, 3, 7, 2, 5], and the total sum is 27.

Example 2:

Input: edges = [[0,1],[1,2],[2,3],[3,4]], nums = [-1,3,-2,4,-5], k = 2

Output: 9

Explanation:

Apply the inversion operation at node 4.

The final nums array becomes [-1, 3, -2, 4, 5], and the total sum is 9.

Example 3:

Input: edges = [[0,1],[0,2]], nums = [0,-1,-2], k = 3

Output: 3

Explanation:

Apply inversion operations at nodes 1 and 2.

Constraints:

2 <= n <= 5 * 10^4

edges.length == n - 1

edges[i] = [u_i, v_i]

0 <= u_i, v_i < n

nums.length == n

-5 * 10^4 <= nums[i] <= 5 * 10^4

1 <= k <= 50

The input is generated such that edges represents a valid tree.

"""

# V0
# IDEA : TREE DP ON THE DISTANCE SINCE THE LAST INVERTED ANCESTOR,
#        CARRYING BOTH THE BEST AND THE WORST SUBTREE TOTAL
#
#   inverting a node flips its whole subtree, so a node's final contribution is
#   nums[v] times (-1)^(inverted nodes on the root-to-v path).  the distance
#   rule only ever compares two inverted nodes lying on one such path, so v may
#   be inverted exactly when the nearest inverted ancestor is >= k edges up.
#
#   that fixes the state: walk down carrying d, the distance to the closest
#   inverted ancestor, capped at k because everything >= k is equally "free".
#   the incoming sign looks like a second dimension -- and it nearly collapses,
#   since handing a subtree the opposite sign negates every achievable total.
#   but negating turns a maximum into a *minimum*, so the trick is not to drop
#   the sign but to carry the pair: best[v][d] and worst[v][d] under sign +1.
#   under sign -1 the best is simply -worst.
#
#   with AB[d] / AW[d] the summed best / worst of the children at distance d,
#       best[v][d] = nums[v] + AB[min(d+1, k)]           (leave v alone)
#       best[v][k] = max(that, -nums[v] - AW[1])         (invert v, now allowed)
#   and worst mirrors it with min and the roles of AB, AW swapped.  that
#   crossover is the whole point: an inverted subtree wants its children as bad
#   as possible under the un-inverted sign.
#
#   n reaches 5*10^4, so the traversal is an explicit post-order, not recursion.
#
"""

DP def
    inverting a node flips its whole subtree, so v's final contribution is
    nums[v] * (-1)^(inverted nodes on the root-to-v path). the distance rule
    only ever compares two inverted nodes on ONE such path, so v may be
    inverted exactly when the nearest inverted ancestor is >= k edges up.

    that fixes the state: walk down carrying d = distance to the closest
    inverted ancestor, CAPPED at k (anything >= k is equally "free").

    best[v][d]: MAX achievable total of subtree(v) under sign +1
    worst[v][d]: MIN achievable total of subtree(v) under sign +1

    -> under sign -1 the best is simply -worst, which is why the sign does
       NOT need its own dimension - but the PAIR must be carried

DP eq

     with AB[d] / AW[d] = the summed best / worst of the children at distance d:

        best[v][d] = nums[v] + AB[min(d+1, k)]              # leave v alone

        best[v][k] = max( that, -nums[v] - AW[1] )          # INVERT v (allowed)

     and worst mirrors it with min, and the roles of AB / AW SWAPPED


    -> e.g. that crossover is the whole point: an inverted subtree wants its
              children as BAD as possible under the un-inverted sign

     n reaches 5*10^4, so the traversal is an explicit POST-ORDER

     ans = best[root][k]

"""
# time = O(n * k), space = O(n * k)
class Solution(object):
    def subtreeInversionSum(self, edges, nums, k):
        n = len(nums)
        g = [[] for _ in range(n)]
        for u, v in edges:
            g[u].append(v)
            g[v].append(u)

        par = [-1] * n
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
                    par[v] = u
                    stack.append(v)

        accB = [None] * n                     # summed child bests, index 1..k
        accW = [None] * n                     # summed child worsts
        for v in range(n):
            accB[v] = [0] * (k + 1)
            accW[v] = [0] * (k + 1)

        answer = 0
        for v in reversed(order):             # children are finished first
            ab, aw = accB[v], accW[v]
            x = nums[v]
            best = [0] * (k + 1)
            worst = [0] * (k + 1)
            for d in range(1, k):
                best[d] = x + ab[d + 1]
                worst[d] = x + aw[d + 1]
            keep_b = x + ab[k]
            flip_b = -x - aw[1]
            best[k] = keep_b if keep_b > flip_b else flip_b
            keep_w = x + aw[k]
            flip_w = -x - ab[1]
            worst[k] = keep_w if keep_w < flip_w else flip_w

            p = par[v]
            if p < 0:
                answer = best[k]
            else:
                pb, pw = accB[p], accW[p]
                for d in range(1, k + 1):
                    pb[d] += best[d]
                    pw[d] += worst[d]
                accB[v] = accW[v] = None
        return answer
