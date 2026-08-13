"""

1059. All Paths from Source Lead to Destination
Medium

Given the edges of a directed graph where edges[i] = [ai, bi] indicates there is an
edge between nodes ai and bi, and two nodes source and destination of this graph,
determine whether or not all paths starting from source eventually, end at
destination, that is:

At least one path exists from the source node to the destination node
If a path exists from the source node to a node with no outgoing edges,
then that node is equal to destination.
The number of possible paths from source to destination is a finite number.

Return true if and only if all roads from source lead to destination.


Example 1:

Input: n = 3, edges = [[0,1],[0,2]], source = 0, destination = 2
Output: false
Explanation: It is possible to reach and get stuck on both node 1 and node 2.

Example 2:

Input: n = 4, edges = [[0,1],[0,3],[1,2],[2,1]], source = 0, destination = 3
Output: false
Explanation: We have two possibilities: to end at node 3, or to loop over node 1 and node 2 indefinitely.

Example 3:

Input: n = 4, edges = [[0,1],[0,2],[1,3],[2,3]], source = 0, destination = 3
Output: true


Constraints:

1 <= n <= 10^4
0 <= edges.length <= 10^4
edges.length == 2
0 <= ai, bi <= n - 1
0 <= source <= n - 1
0 <= destination <= n - 1
The given graph may have self-loops and parallel edges.

"""

# V0
# IDEA : DFS + 3 color (white / gray / black) cycle detection
#
#  state[i] = 0 : not visited
#  state[i] = 1 : on current DFS stack  -> seeing it again means a CYCLE -> False
#  state[i] = 2 : already proven "all paths from i reach destination"
#
#  a node with NO outgoing edge is good only if it IS the destination
#  the destination itself must have no outgoing edge (else we can walk away from it)
# time = O(V + E)
# space = O(V + E)
class Solution(object):
    def leadsToDestination(self, n, edges, source, destination):
        g = [[] for _ in range(n)]
        for a, b in edges:
            g[a].append(b)

        # if we can leave destination, some path does NOT end there
        if g[destination]:
            return False

        state = [0] * n

        # NOTE : recursive DFS, depth can reach n (10^4) -> see V1 for iterative
        def dfs(i):
            if state[i]:
                # 1 -> cycle (bad), 2 -> already validated (good)
                return state[i] == 2
            if not g[i]:
                return i == destination
            state[i] = 1
            for j in g[i]:
                if not dfs(j):
                    return False
            state[i] = 2
            return True

        return dfs(source)

# V1
# IDEA : same 3 color DFS, written ITERATIVELY (no recursion limit issue)
# time = O(V + E)
# space = O(V + E)
class Solution(object):
    def leadsToDestination(self, n, edges, source, destination):
        g = [[] for _ in range(n)]
        for a, b in edges:
            g[a].append(b)

        if g[destination]:
            return False

        state = [0] * n
        # each stack frame : (node, index of next child to visit)
        stack = [(source, 0)]

        while stack:
            node, idx = stack[-1]

            if idx == 0:
                if state[node] == 1:
                    return False        # back edge -> cycle
                if state[node] == 2:
                    stack.pop()
                    continue
                if not g[node]:
                    if node != destination:
                        return False
                    state[node] = 2
                    stack.pop()
                    continue
                state[node] = 1

            if idx < len(g[node]):
                stack[-1] = (node, idx + 1)
                stack.append((g[node][idx], 0))
            else:
                state[node] = 2
                stack.pop()

        return True
