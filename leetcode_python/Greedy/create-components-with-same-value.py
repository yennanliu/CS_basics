"""

2440. Create Components With Same Value
Hard

There is an undirected tree with n nodes labeled from 0 to n - 1.

You are given a 0-indexed integer array nums of length n where nums[i] represents the value of the ith node. You are also given a 2D integer array edges of length n - 1 where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the tree.

You are allowed to delete some edges, splitting the tree into multiple connected components. Let the value of a component be the sum of all nums[i] for which node i is in the component.

Return the maximum number of edges you can delete, such that every connected component in the tree has the same value.


Example 1:

Input: nums = [6,2,2,2,6], edges = [[0,1],[1,2],[1,3],[3,4]]
Output: 2
Explanation: The above figure shows how we can delete the edges [0,1] and [3,4]. The created components are nodes [0], [1,2,3] and [4]. The sum of the values in each component equals 6. It can be proven that no better deletion exists, so the answer is 2.

Example 2:

Input: nums = [2], edges = []
Output: 0
Explanation: There are no edges to be deleted.


Constraints:

1 <= n <= 2 * 10^4
nums.length == n
1 <= nums[i] <= 50
edges.length == n - 1
edges[i].length == 2
0 <= edges[i][0], edges[i][1] <= n - 1
edges represents a valid tree.

"""

# V0
# IDEA : ENUMERATE THE DIVISOR + GREEDY BOTTOM-UP PACKING
#
#   if the tree is cut into k components each of value t, then k * t = s
#   (s = total sum) -> t must be a divisor of s, and t >= max(nums).
#   so only the divisors of s are candidates; try the largest k first.
#
#   feasibility check for a given t : post-order the tree, accumulate each
#   subtree sum upwards.
#     - acc == t -> close this component right here, send 0 to the parent
#     - acc  > t -> impossible for this t
#     - acc  < t -> the component must extend through the parent edge
#   this is forced (no choice), hence greedy is exact. success iff the root
#   also closes at exactly t.
#
#   NOTE : n up to 2 * 10^4 -> the tree can be a 20000-long chain, so the
#          traversal is written ITERATIVELY (recursion would overflow).
#
# time = O(n * d(s)), d(s) = number of divisors of s
# space = O(n)
class Solution(object):
    def componentValue(self, nums, edges):
        n = len(nums)
        if n == 1:
            return 0

        g = [[] for _ in range(n)]
        for a, b in edges:
            g[a].append(b)
            g[b].append(a)

        # iterative DFS from 0 -> parent[] + a valid pre-order
        parent = [-1] * n
        order = []
        stack = [0]
        seen = [False] * n
        seen[0] = True
        while stack:
            u = stack.pop()
            order.append(u)
            for v in g[u]:
                if not seen[v]:
                    seen[v] = True
                    parent[v] = u
                    stack.append(v)

        s = sum(nums)
        mx = max(nums)

        def feasible(t):
            acc = nums[:]
            for i in reversed(order):          # children before parents
                if acc[i] > t:
                    return False
                if acc[i] == t:
                    acc[i] = 0                 # component closed
                elif i != 0:
                    acc[parent[i]] += acc[i]
                else:
                    return False               # root left with a partial piece
            return True

        for k in range(min(n, s // mx), 1, -1):
            if s % k == 0 and feasible(s // k):
                return k - 1
        return 0
