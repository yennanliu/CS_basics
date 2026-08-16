"""

3585. Find Weighted Median Node in Tree
Hard

You are given an integer n and an undirected, weighted tree rooted at node 0
with n nodes numbered from 0 to n - 1. This is represented by a 2D array edges
of length n - 1, where edges[i] = [u_i, v_i, w_i] indicates an edge from node
u_i to v_i with weight w_i.

The weighted median node is defined as the first node x on the path from u_i
to v_i such that the sum of edge weights from u_i to x is greater than or equal
to half of the total path weight.

You are given a 2D integer array queries. For each queries[j] = [u_j, v_j],
determine the weighted median node along the path from u_j to v_j.

Return an array ans, where ans[j] is the node index of the weighted median for
queries[j].


Example 1:

Input: n = 2, edges = [[0,1,7]], queries = [[1,0],[0,1]]
Output: [0,1]
Explanation:

Query [1, 0]: path 1 -> 0, edge weights [7], total path weight 7, half 3.5.
Sum from 1 -> 0 = 7 >= 3.5, median is node 0. Answer 0.

Query [0, 1]: path 0 -> 1, edge weights [7], total path weight 7, half 3.5.
Sum from 0 -> 1 = 7 >= 3.5, median is node 1. Answer 1.

Example 2:

Input: n = 3, edges = [[0,1,2],[2,0,4]], queries = [[0,1],[2,0],[1,2]]
Output: [1,0,2]
Explanation:

Query [0, 1]: path 0 -> 1, edge weights [2], total path weight 2, half 1.
Sum from 0 -> 1 = 2 >= 1, median is node 1. Answer 1.

Query [2, 0]: path 2 -> 0, edge weights [4], total path weight 4, half 2.
Sum from 2 -> 0 = 4 >= 2, median is node 0. Answer 0.

Query [1, 2]: path 1 -> 0 -> 2, edge weights [2, 4], total path weight 6,
half 3. Sum from 1 -> 0 = 2 < 3. Sum from 1 -> 2 = 2 + 4 = 6 >= 3, median is
node 2. Answer 2.

Example 3:

Input: n = 5, edges = [[0,1,2],[0,2,5],[1,3,1],[2,4,3]], queries = [[3,4],[1,2]]
Output: [2,2]
Explanation:

Query [3, 4]: path 3 -> 1 -> 0 -> 2 -> 4, edge weights [1, 2, 5, 3], total
path weight 11, half 5.5. Sum from 3 -> 1 = 1 < 5.5. Sum from 3 -> 0 =
1 + 2 = 3 < 5.5. Sum from 3 -> 2 = 1 + 2 + 5 = 8 >= 5.5, median is node 2.
Answer 2.

Query [1, 2]: path 1 -> 0 -> 2, edge weights [2, 5], total path weight 7,
half 3.5. Sum from 1 -> 0 = 2 < 3.5. Sum from 1 -> 2 = 2 + 5 = 7 >= 3.5,
median is node 2. Answer 2.


Constraints:

2 <= n <= 10^5
edges.length == n - 1
edges[i] == [u_i, v_i, w_i]
0 <= u_i, v_i < n
1 <= w_i <= 10^9
1 <= queries.length <= 10^5
queries[j] == [u_j, v_j]
0 <= u_j, v_j < n
The input is generated such that edges represents a valid tree.

"""

# V0
# IDEA : ROOT DISTANCES + LCA, THEN ONE BINARY-LIFTING JUMP PER QUERY
#
#   root the tree once and store dist[x], the weighted distance from node 0.
#   with l = lca(u, v) the path splits into an upward leg of length
#   A = dist[u] - dist[l] and a downward leg of length B = dist[v] - dist[l],
#   and the total path weight is A + B. all the comparisons below are doubled
#   so the "half" is exact integer arithmetic and never a float.
#
#   which leg holds the median is decided in O(1) by testing l itself: the
#   prefix from u down to l measures A, so l qualifies exactly when
#   2A >= A + B, i.e. when A >= B.
#
#   on the upward leg the prefix from u to an ancestor x is dist[u] - dist[x],
#   so the test 2*(dist[u] - dist[x]) >= A + B rewrites to
#   2*dist[x] <= 2*dist[l] + A - B. climbing from u the left side only shrinks,
#   so the failing nodes form an unbroken run starting at u; binary lifting
#   climbs to the last failing node and the answer is its parent. u itself
#   always fails here (as long as u != v), which is what guarantees that parent
#   exists.
#
#   on the downward leg the prefix from u to x is A + dist[x] - dist[l], and
#   the test becomes 2*dist[x] >= 2*dist[l] + B - A. now deeper nodes are the
#   ones that qualify, so climbing from v the qualifying nodes form an unbroken
#   run; the answer is the highest one still strictly below l. v itself always
#   qualifies, and l does not (that is exactly the A < B branch), so the climb
#   is well defined.
#
# time = O((n + q) * log n), space = O(n * log n)
class Solution(object):
    def findMedian(self, n, edges, queries):
        LOG = max(1, n.bit_length())

        g = [[] for _ in range(n)]
        for u, v, w in edges:
            g[u].append((v, w))
            g[v].append((u, w))

        parent = [-1] * n
        depth = [0] * n
        dist = [0] * n
        seen = [False] * n
        seen[0] = True
        stack = [0]
        while stack:
            x = stack.pop()
            for y, w in g[x]:
                if not seen[y]:
                    seen[y] = True
                    parent[y] = x
                    depth[y] = depth[x] + 1
                    dist[y] = dist[x] + w
                    stack.append(y)

        up = [parent]
        for _ in range(1, LOG):
            prev = up[-1]
            cur = [-1] * n
            for i in range(n):
                p = prev[i]
                if p != -1:
                    cur[i] = prev[p]
            up.append(cur)

        def lca(a, b):
            if depth[a] < depth[b]:
                a, b = b, a
            diff = depth[a] - depth[b]
            k = 0
            while diff:
                if diff & 1:
                    a = up[k][a]
                diff >>= 1
                k += 1
            if a == b:
                return a
            for k in range(LOG - 1, -1, -1):
                if up[k][a] != up[k][b]:
                    a = up[k][a]
                    b = up[k][b]
            return parent[a]

        ans = []
        for u, v in queries:
            if u == v:
                ans.append(u)
                continue

            l = lca(u, v)
            a = dist[u] - dist[l]
            b = dist[v] - dist[l]

            if a >= b:
                # median sits on the u -> l leg; climb past every failing node
                limit = 2 * dist[l] + a - b
                cur = u
                for k in range(LOG - 1, -1, -1):
                    p = up[k][cur]
                    if p != -1 and depth[p] >= depth[l] and 2 * dist[p] > limit:
                        cur = p
                ans.append(parent[cur])
            else:
                # median sits on the l -> v leg; climb while still qualifying
                limit = 2 * dist[l] + b - a
                cur = v
                for k in range(LOG - 1, -1, -1):
                    p = up[k][cur]
                    if p != -1 and depth[p] > depth[l] and 2 * dist[p] >= limit:
                        cur = p
                ans.append(cur)
        return ans
