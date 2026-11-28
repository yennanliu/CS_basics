#---------------------------------------------------------------
# GRAPH (adjacency list)
#---------------------------------------------------------------
#
# A graph is a set of vertices plus a set of edges. Unlike a tree it
# may contain cycles and has no root.
#
#     0 --- 1 --- 3
#     |    /      |
#     2 --/       4 --- 5 --- 6
#
# TWO WAYS TO STORE IT
#
#   adjacency LIST    {0: [1, 2], 1: [0, 2, 3], ...}
#     space O(V + E), listing a vertex's neighbours is O(deg(v))
#     -> the default for SPARSE graphs, which is nearly every
#        interview/LeetCode graph
#
#   adjacency MATRIX  matrix[i][j] == 1 when edge i-j exists
#     space O(V^2), edge lookup is O(1), neighbour listing is O(V)
#     -> only worth it for DENSE graphs or O(1) edge tests
#
# Time  : add_vertex / add_edge  O(1)
#         has_edge               O(deg(v))
#         bfs / dfs              O(V + E)
# Space : O(V + E)
#
# References:
#   - https://github.com/yennanliu/CS_basics/blob/master/data_structure/js/graph.js


from collections import deque


# What shortest_path records as "came from" for the START vertex, which
# came from nowhere. A private object rather than None, so that it can
# never be mistaken for a real vertex -- None is a legal vertex name.
_NO_PREVIOUS = object()


class Graph:
    """Adjacency-list graph: undirected by default, directed on request.

    `self.adjacent` maps every vertex to the list of its neighbours.
    """

    def __init__(self, directed=False):
        self.directed = directed
        self.adjacent = {}

    def __str__(self):
        """One line per vertex: `1 --> 0 2 3`."""
        lines = []
        for node in sorted(self.adjacent, key=str):
            neighbours = " ".join(str(n) for n in sorted(self.adjacent[node], key=str))
            lines.append("{} --> {}".format(node, neighbours))
        return "\n".join(lines)

    @property
    def vertex_count(self):
        return len(self.adjacent)

    @property
    def edge_count(self):
        """An undirected edge sits in TWO adjacency lists, so halve the total."""
        total = sum(len(neighbours) for neighbours in self.adjacent.values())
        return total if self.directed else total // 2

    def add_vertex(self, node):
        """Adding an isolated vertex is NOT the same as adding an edge."""
        self.adjacent.setdefault(node, [])

    def add_edge(self, node1, node2):
        """Connect two vertices, creating them if they do not exist yet."""
        self.add_vertex(node1)
        self.add_vertex(node2)
        self.adjacent[node1].append(node2)
        if not self.directed:            # undirected -> record BOTH directions
            self.adjacent[node2].append(node1)

    def neighbours(self, node):
        """The vertices one edge away from `node` (empty if it is unknown)."""
        return self.adjacent.get(node, [])

    def has_edge(self, node1, node2):
        """O(deg(node1)): scans one adjacency list."""
        return node2 in self.neighbours(node1)

    def bfs(self, start):
        """Breadth-first: nearest vertices first, using a QUEUE."""
        if start not in self.adjacent:
            return []
        visited = {start}
        order = []
        queue = deque([start])
        while queue:
            node = queue.popleft()
            order.append(node)
            for neighbour in self.adjacent[node]:
                if neighbour not in visited:
                    visited.add(neighbour)   # mark on ENQUEUE, or a vertex
                    queue.append(neighbour)  # can be queued twice
        return order

    def dfs(self, start):
        """Depth-first: follow one branch to the end, using recursion."""
        order = []
        visited = set()

        def walk(node):
            visited.add(node)
            order.append(node)
            for neighbour in self.adjacent[node]:
                if neighbour not in visited:
                    walk(neighbour)

        if start in self.adjacent:
            walk(start)
        return order

    def shortest_path(self, start, end):
        """Fewest EDGES from start to end (BFS), or None if unreachable.

        Only correct on an UNWEIGHTED graph -- with weights you need
        Dijkstra (see algorithm/python/dijkstra.py).
        """
        if start not in self.adjacent or end not in self.adjacent:
            return None

        # previous[v] = the vertex we reached v FROM. It doubles as the
        # visited set: a vertex with an entry has already been queued.
        #
        # "Have we reached `end`?" is asked as `end in previous` -- a
        # dictionary lookup, the same test the adjacency dict itself uses.
        previous = {start: _NO_PREVIOUS}
        queue = deque([start])
        while queue and end not in previous:
            node = queue.popleft()
            for neighbour in self.adjacent[node]:
                if neighbour not in previous:
                    previous[neighbour] = node
                    queue.append(neighbour)

        if end not in previous:              # ran out of vertices first
            return None
        return self._rebuild_path(previous, end)

    @staticmethod
    def _rebuild_path(previous, end):
        """Follow the `previous` links from the end back to the start.

        The walk ends at the _NO_PREVIOUS marker stored for the start.
        Testing for that marker (rather than `node == start`) never
        compares two vertices, so it is right for every vertex name --
        including None, and float("nan"), which is not equal to itself.
        """
        path = []
        node = end
        while node is not _NO_PREVIOUS:
            path.append(node)
            node = previous[node]
        path.reverse()                       # collected end -> start
        return path


if __name__ == "__main__":
    #    0 --- 1 --- 3
    #    |    /      |
    #    2 --/       4 --- 5 --- 6
    g = Graph()
    for edge in [("0", "1"), ("0", "2"), ("1", "2"), ("1", "3"),
                 ("3", "4"), ("4", "5"), ("5", "6")]:
        g.add_edge(*edge)

    assert g.vertex_count == 7
    assert g.edge_count == 7
    assert sorted(g.neighbours("1")) == ["0", "2", "3"]
    assert g.has_edge("0", "1") and g.has_edge("1", "0")   # undirected
    assert not g.has_edge("0", "6")

    assert g.bfs("0") == ["0", "1", "2", "3", "4", "5", "6"]
    assert g.dfs("0") == ["0", "1", "2", "3", "4", "5", "6"]

    assert g.shortest_path("0", "6") == ["0", "1", "3", "4", "5", "6"]
    assert g.shortest_path("0", "0") == ["0"]
    g.add_vertex("island")
    assert g.shortest_path("0", "island") is None

    # any hashable value can name a vertex -- even None
    odd = Graph()
    odd.add_edge("a", None)
    odd.add_edge(None, "b")
    assert odd.shortest_path("a", None) == ["a", None]
    assert odd.shortest_path("a", "b") == ["a", None, "b"]
    assert odd.shortest_path(None, "b") == [None, "b"]

    # ... or NaN, which is not even equal to itself
    nan = float("nan")
    odd.add_edge(nan, "b")
    assert odd.shortest_path(nan, "b") == [nan, "b"]
    assert odd.shortest_path(nan, "a")[1:] == ["b", None, "a"]
    assert odd.shortest_path("a", nan) == ["a", None, "b", nan]   # as a destination
    assert odd.shortest_path(nan, nan) == [nan]

    # in a DIRECTED graph the edge goes one way only
    d = Graph(directed=True)
    d.add_edge("a", "b")
    assert d.has_edge("a", "b") and not d.has_edge("b", "a")
    assert d.edge_count == 1

    print(g)
    print("Success.")
