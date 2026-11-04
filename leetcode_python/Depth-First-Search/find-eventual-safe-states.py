"""

802. Find Eventual Safe States
Medium

There is a directed graph of n nodes with each node labeled from 0 to n - 1.
The graph is represented by a 0-indexed 2D integer array graph where graph[i]
is an integer array of nodes adjacent to node i, meaning there is an edge from
node i to each node in graph[i].

A node is a terminal node if there are no outgoing edges. A node is a safe
node if every possible path starting from that node leads to a terminal node
(or another safe node).

Return an array containing all the safe nodes of the graph. The answer should
be sorted in ascending order.

Example 1:

Illustration of graph (https://s3-lc-upload.s3.amazonaws.com/uploads/2018/03/17/picture1.png)

Input: graph = [[1,2],[2,3],[5],[0],[5],[],[]]
Output: [2,4,5,6]
Explanation: The given graph is shown above.
Nodes 5 and 6 are terminal nodes as there are no outgoing edges from either of them.
Every path starting at nodes 2, 4, 5, and 6 all lead to either node 5 or 6.

Example 2:

Input: graph = [[1,2,3,4],[1,2],[3,4],[0,4],[]]
Output: [4]
Explanation:
Only node 4 is a terminal node, and every path starting at node 4 leads to node 4.

Constraints:

n == graph.length
1 <= n <= 10^4
0 <= graph[i].length <= n
0 <= graph[i][j] <= n - 1
graph[i] is sorted in a strictly increasing order.
The graph may contain self-loops.
The number of edges in the graph will be in the range [1, 4 * 10^4].

"""

# V0
# IDEA : DFS WITH 3 COLOURS (cycle detection on a directed graph)
#
#   A node is UNSAFE exactly when some path from it reaches a cycle (a path
#   that never reaches a terminal node in a finite graph must loop). So run
#   cycle-detection DFS and keep every node whose DFS finished cleanly:
#
#     WHITE (0) = not visited yet
#     GRAY  (1) = on the current DFS path  -> meeting one again = a cycle
#     BLACK (2) = fully explored, proven SAFE
#
#   A node that hits a cycle is LEFT GRAY on purpose: GRAY then doubles as
#   "known unsafe", so a later DFS that reaches it also fails at once and no
#   node is explored twice.
#
#   e.g. graph = [[1,2],[2,3],[5],[0],[5],[],[]]
#        0 -> 1 -> 3 -> 0 (GRAY) -> cycle -> 0, 1, 3 unsafe
#        2 -> 5 (terminal) -> 2 safe; 4 -> 5 -> 4 safe -> [2,4,5,6]
#
# time = O(V + E), space = O(V)   # colour array + recursion stack
import sys


class Solution(object):
    def eventualSafeNodes(self, graph):
        """
        :type graph: List[List[int]]
        :rtype: List[int]
        """
        WHITE, GRAY, BLACK = 0, 1, 2
        n = len(graph)
        color = [WHITE] * n

        # a chain of 10^4 nodes is deeper than Python's default limit (1000)
        sys.setrecursionlimit(max(sys.getrecursionlimit(), n + 100))

        def is_safe(node):
            """Return True if every path from `node` ends at a terminal node."""
            if color[node] != WHITE:
                # BLACK = proven safe; GRAY = on a cycle or leads to one
                return color[node] == BLACK

            color[node] = GRAY
            for neighbour in graph[node]:
                if not is_safe(neighbour):
                    # NOTE !!! return WITHOUT resetting to WHITE: staying
                    #          GRAY marks this node unsafe for later calls
                    return False

            # every outgoing path ends safely
            color[node] = BLACK
            return True

        # check nodes in index order, so the result is already sorted
        safe_nodes = []
        for node in range(n):
            if is_safe(node):
                safe_nodes.append(node)
        return safe_nodes


# V1
# IDEA : TOPOLOGICAL SORT (KAHN'S BFS) ON THE REVERSED GRAPH
#
#   Terminal nodes (out-degree 0) are safe. A node becomes safe once ALL of
#   its outgoing edges point at safe nodes. So reverse every edge and peel
#   from the terminals inward: when a node is found safe, each node pointing
#   at it loses one "unresolved" outgoing edge; at 0 it is safe too.
#   Nodes on, or leading into, a cycle never drop to 0 and are never peeled.
#
#   e.g. graph = [[1,2],[2,3],[5],[0],[5],[],[]]
#        out-degree 0: {5, 6} -> peel 5 -> 2 and 4 reach 0 -> peel 2 -> 0
#        drops to 1 (still has edge to 1) -> stops; safe = {2,4,5,6}
#
#   Iterative, so no recursion-depth concern — a good one to offer when the
#   interviewer asks for a non-recursive version.
#
# time = O(V + E), space = O(V + E)   # the reversed adjacency list
from collections import deque


class Solution2(object):
    def eventualSafeNodes(self, graph):
        """
        :type graph: List[List[int]]
        :rtype: List[int]
        """
        n = len(graph)

        # reversed[v] = every node u with an edge u -> v
        reversed_graph = [[] for _ in range(n)]
        # out_degree[u] = outgoing edges of u not yet known to lead somewhere safe
        out_degree = [0] * n
        for node in range(n):
            out_degree[node] = len(graph[node])
            for neighbour in graph[node]:
                reversed_graph[neighbour].append(node)

        # start from the terminal nodes
        queue = deque()
        for node in range(n):
            if out_degree[node] == 0:
                queue.append(node)

        is_safe = [False] * n
        while queue:
            node = queue.popleft()
            is_safe[node] = True
            for predecessor in reversed_graph[node]:
                out_degree[predecessor] -= 1
                # NOTE !!! safe only once EVERY outgoing edge is resolved
                if out_degree[predecessor] == 0:
                    queue.append(predecessor)

        # collect in index order -> sorted
        safe_nodes = []
        for node in range(n):
            if is_safe[node]:
                safe_nodes.append(node)
        return safe_nodes
