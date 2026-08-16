"""

3425. Longest Special Path
Hard

You are given an undirected tree rooted at node 0 with n nodes numbered from 0
to n - 1, represented by a 2D array edges of length n - 1, where edges[i] =
[u_i, v_i, length_i] indicates an edge between nodes u_i and v_i with length
length_i. You are also given an integer array nums, where nums[i] represents the
value at node i.

A special path is defined as a downward path from an ancestor node to a
descendant node such that all the values of the nodes in that path are unique.

Note that a path may start and end at the same node.

Return an array result of size 2, where result[0] is the length of the longest
special path, and result[1] is the minimum number of nodes in all possible
longest special paths.

Example 1:

Input: edges = [[0,1,2],[1,2,3],[1,3,5],[1,4,4],[2,5,6]], nums = [2,1,2,1,3,1]

Output: [6,2]

Explanation:

In the image below, nodes are colored by their corresponding values in nums

The longest special paths are 2 -> 5 and 0 -> 1 -> 4, both having a length of 6.
The minimum number of nodes across all longest special paths is 2.

Example 2:

Input: edges = [[1,0,8]], nums = [2,2]

Output: [0,1]

Explanation:

The longest special paths are 0 and 1, both having a length of 0. The minimum
number of nodes across all longest special paths is 1.

Constraints:

2 <= n <= 5 * 10^4
edges.length == n - 1
edges[i].length == 3
0 <= u_i, v_i < n
1 <= length_i <= 10^3
nums.length == n
0 <= nums[i] <= 5 * 10^4
The input is generated such that edges represents a valid tree.

"""

# V0
# IDEA : DFS CARRYING A SLIDING WINDOW ALONG THE ROOT PATH
#
#   a special path is a *vertical* stretch of the current root-to-node path with
#   all values distinct — in other words, exactly the classic "longest substring
#   without repeats" window, but the string is the DFS stack instead of an
#   array.
#
#   keep, per value, a stack of the depths at which it occurs on the current
#   path.  entering a node of value v whose previous occurrence sits at depth d0
#   forbids every window that starts at or above d0, so the window's left edge
#   moves to max(left, d0 + 1).  the left edge never moves back while we descend
#   — it is restored on backtrack, which is what makes this a per-branch two
#   pointer rather than a global one.
#
#   edge lengths are >= 1, so the prefix distances along the path are strictly
#   increasing and the longest window ending at the current node is always the
#   widest legal one, i.e. the one starting exactly at the left edge.  that
#   makes one candidate per node enough; among candidates of equal length we
#   keep the fewest nodes.
#
#   the recursion is written with an explicit stack because n can be 5e4.
#
# time = O(n), space = O(n)
class Solution(object):
    def longestSpecialPath(self, edges, nums):
        n = len(nums)
        head = [-1] * n
        nxt = []
        to = []
        wt = []
        for u, v, w in edges:
            to.append(v); wt.append(w); nxt.append(head[u]); head[u] = len(to) - 1
            to.append(u); wt.append(w); nxt.append(head[v]); head[v] = len(to) - 1

        maxv = max(nums) if nums else 0
        occ = [[] for _ in range(maxv + 1)]
        dist = [0] * (n + 1)     # dist[d] = root distance of the node at depth d
        bestLen = 0
        bestNodes = 1

        # stack frames: (node, parent, depth, edge iterator index, saved left)
        stack = [(0, -1, 0, head[0], 0, True)]
        left = 0
        while stack:
            u, par, d, ei, savedLeft, first = stack.pop()
            if first:
                v = nums[u]
                prev = occ[v][-1] if occ[v] else -1
                occ[v].append(d)
                savedLeft = left
                if prev + 1 > left:
                    left = prev + 1
                ln = dist[d] - dist[left]
                cnt = d - left + 1
                if ln > bestLen:
                    bestLen = ln
                    bestNodes = cnt
                elif ln == bestLen and cnt < bestNodes:
                    bestNodes = cnt
            # walk the adjacency list one edge at a time
            while ei != -1 and to[ei] == par:
                ei = nxt[ei]
            if ei == -1:
                occ[nums[u]].pop()
                left = savedLeft
                continue
            stack.append((u, par, d, nxt[ei], savedLeft, False))
            c = to[ei]
            dist[d + 1] = dist[d] + wt[ei]
            stack.append((c, u, d + 1, head[c], 0, True))
        return [bestLen, bestNodes]
