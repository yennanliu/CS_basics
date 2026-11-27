"""

1971. Find if Path Exists in Graph
Easy

There is a bi-directional graph with n vertices, where each vertex is labeled from
0 to n - 1 (inclusive). The edges in the graph are represented as a 2D integer
array edges, where each edges[i] = [ui, vi] denotes a bi-directional edge between
vertex ui and vertex vi. Every vertex pair is connected by at most one edge, and no
vertex has an edge to itself.

You want to determine if there is a valid path that exists from vertex source to
vertex destination.

Given edges and the integers n, source, and destination, return true if there is a
valid path from source to destination, or false otherwise.

Example 1:

Input: n = 3, edges = [[0,1],[1,2],[2,0]], source = 0, destination = 2

Output: true

Explanation: There are two paths from vertex 0 to vertex 2:
- 0 -> 1 -> 2
- 0 -> 2

Example 2:

Input: n = 6, edges = [[0,1],[0,2],[3,5],[5,4],[4,3]], source = 0, destination = 5

Output: false

Explanation: There is no path from vertex 0 to vertex 5.

Constraints:

1 <= n <= 2 * 10^5

0 <= edges.length <= 2 * 10^5

edges[i].length == 2

0 <= ui, vi <= n - 1

ui != vi

0 <= source, destination <= n - 1

There are no duplicate edges.

There are no self edges.

"""

# V0
# IDEA : BFS ON AN ADJACENCY LIST
#
#   "Is there a path?" = "is destination in source's connected component?"
#   BFS from source visits exactly that component, so we stop the moment we
#   pop destination, or answer False once the queue runs dry.
#
#   Edges are bi-directional -> add each edge in BOTH directions.
#   Mark a node visited when it is PUSHED (not when popped), so no node
#   enters the queue twice -> every node and edge is handled once.
#
#   e.g. n = 6, edges = [[0,1],[0,2],[3,5],[5,4],[4,3]], 0 -> 5
#        queue: [0] -> [1,2] -> [2] -> []  (5 never reached) -> False
#
# time = O(n + e), space = O(n + e)
from collections import defaultdict, deque


class Solution(object):
    def validPath(self, n, edges, source, destination):
        """
        :type n: int
        :type edges: List[List[int]]
        :type source: int
        :type destination: int
        :rtype: bool
        """
        # edge case: a node always reaches itself, even with no edges
        if source == destination:
            return True

        # node -> its neighbours; an undirected edge goes both ways
        graph = defaultdict(list)
        for u, v in edges:
            graph[u].append(v)
            graph[v].append(u)

        visited = set([source])
        queue = deque([source])

        while queue:
            node = queue.popleft()
            if node == destination:
                return True
            for neighbour in graph[node]:
                # NOTE !!! mark on push, or a node can be queued many times
                if neighbour not in visited:
                    visited.add(neighbour)
                    queue.append(neighbour)

        # the whole component of source was explored without meeting destination
        return False


# V1
# IDEA : DFS (ITERATIVE, EXPLICIT STACK)
#
#   Same reachability question, explored depth-first. The stack is explicit
#   on purpose: n can be 2 * 10^5, and a path graph that deep would blow
#   Python's recursion limit (~1000) in a recursive DFS.
#
# time = O(n + e), space = O(n + e)
class Solution2(object):
    def validPath(self, n, edges, source, destination):
        """
        :type n: int
        :type edges: List[List[int]]
        :type source: int
        :type destination: int
        :rtype: bool
        """
        if source == destination:
            return True

        graph = defaultdict(list)
        for u, v in edges:
            graph[u].append(v)
            graph[v].append(u)

        visited = set([source])
        stack = [source]

        while stack:
            node = stack.pop()
            if node == destination:
                return True
            for neighbour in graph[node]:
                if neighbour not in visited:
                    visited.add(neighbour)
                    stack.append(neighbour)

        return False


# V2
# IDEA : UNION FIND
#
#   Union the two ends of every edge; afterwards two nodes are connected
#   exactly when they share a root. No adjacency list is built, and it is
#   the version to reach for when many (source, destination) queries are
#   asked against the same edges (each query is then ~O(1)).
#
# time = O(n + e * a(n)), space = O(n)
class Solution3(object):
    def validPath(self, n, edges, source, destination):
        """
        :type n: int
        :type edges: List[List[int]]
        :type source: int
        :type destination: int
        :rtype: bool
        """
        # every node starts as its own set
        parent = list(range(n))
        rank = [0] * n

        def find(x):
            """Return the root of x's set, compressing the path on the way."""
            root = x
            while parent[root] != root:
                root = parent[root]
            # point every node on the path straight at the root
            while parent[x] != root:
                next_node = parent[x]
                parent[x] = root
                x = next_node
            return root

        def union(a, b):
            """Merge the sets of a and b (union by rank)."""
            root_a = find(a)
            root_b = find(b)
            if root_a == root_b:
                return
            if rank[root_a] < rank[root_b]:
                parent[root_a] = root_b
            elif rank[root_a] > rank[root_b]:
                parent[root_b] = root_a
            else:
                parent[root_b] = root_a
                rank[root_a] += 1

        for u, v in edges:
            union(u, v)

        return find(source) == find(destination)
