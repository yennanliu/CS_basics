"""

3067. Count Pairs of Connectable Servers in a Weighted Tree Network
Medium

You are given an unrooted weighted tree with n vertices representing servers numbered from 0 to n - 1, an array edges where edges[i] = [ai, bi, weighti] represents a bidirectional edge between vertices ai and bi of weight weighti. You are also given an integer signalSpeed.

Two servers a and b are connectable through a server c if:

a < b, a != c and b != c.
The distance from c to a is divisible by signalSpeed.
The distance from c to b is divisible by signalSpeed.
The path from c to a and the path from c to b do not share any edges.

Return an integer array count of length n where count[i] is the number of server pairs that are connectable through the server i.


Example 1:

Input: edges = [[0,1,1],[1,2,5],[2,3,13],[3,4,9],[4,5,2]], signalSpeed = 1
Output: [0,4,6,6,4,0]
Explanation: Since signalSpeed is 1, count[c] is equal to the number of pairs of paths that start at c and do not share any edges.
In the case of the given path graph, count[c] is equal to the number of servers to the left of c multiplied by the servers to the right of c.

Example 2:

Input: edges = [[0,6,3],[6,5,3],[0,3,1],[3,2,7],[3,1,6],[3,4,2]], signalSpeed = 3
Output: [2,0,0,0,0,0,2]
Explanation: Through server 0 that can be connected through the two paths (3, 6), (3, 5).
Through server 6 that can be connected through the two paths (0, 3), (0, 4).
It can be shown that no two servers are connectable through servers other than 0 and 6.


Constraints:

2 <= n <= 1000
edges.length == n - 1
edges[i].length == 3
0 <= ai, bi < n
edges[i] = [ai, bi, weighti]
1 <= weighti <= 10^6
1 <= signalSpeed <= 10^6
The input is generated such that edges represents a valid tree.

"""

# V0
# IDEA : ROOT AT EACH SERVER, COUNT PER BRANCH, MULTIPLY ACROSS BRANCHES
#
#   "the two paths share no edge" means a and b must leave c through
#   DIFFERENT neighbours — every subtree hanging off c is one branch.
#
#   so for a fixed c, walk each branch and count how many vertices sit at a
#   distance divisible by signalSpeed. if the branches yield c1, c2, ..., cm
#   such vertices, the connectable pairs through c number
#
#       sum over i < j of ci * cj
#
#   which a running total computes in one pass :  add prev_total * ci, then
#   prev_total += ci.
#
#   n <= 1000, so repeating the whole DFS from every c is O(n^2) — fine.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def countPairsOfConnectableServers(self, edges, signalSpeed):
        n = len(edges) + 1
        adj = [[] for _ in range(n)]
        for a, b, w in edges:
            adj[a].append((b, w))
            adj[b].append((a, w))

        res = [0] * n
        for c in range(n):
            seen_total = 0
            for nxt, w in adj[c]:
                # count vertices in this branch whose distance from c divides evenly
                cnt = 0
                stack = [(nxt, w, c)]
                while stack:
                    node, dist, parent = stack.pop()
                    if dist % signalSpeed == 0:
                        cnt += 1
                    for nb, nw in adj[node]:
                        if nb != parent:
                            stack.append((nb, dist + nw, node))
                res[c] += seen_total * cnt
                seen_total += cnt
        return res
