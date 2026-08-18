#---------------------------------------------------------------
# GRAPH (adjacency list)
#---------------------------------------------------------------
#
# A graph is a set of vertices plus a set of edges. Unlike a tree it
# may contain cycles and has no root.
#
#         0 --- 1 --- 3
#         |    /|     |
#         |   / |     |
#         2 -/  |     4 --- 5
#                \         /
#                 --------- 6
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


class Graph:
    """Undirected graph stored as an adjacency list."""

    def __init__(self, directed=False):
        self.directed = directed
        self.adjacent = {}

    def __str__(self):
        return "\n".join(
            "{} --> {}".format(node, " ".join(str(n) for n in sorted(neighbours)))
            for node, neighbours in sorted(self.adjacent.items(), key=lambda kv: str(kv[0]))
        )

    @property
    def vertex_count(self):
        return len(self.adjacent)

    @property
    def edge_count(self):
        total = sum(len(n) for n in self.adjacent.values())
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
        return self.adjacent.get(node, [])

    def has_edge(self, node1, node2):
        return node2 in self.adjacent.get(node1, [])

    def bfs(self, start):
        """Breadth-first: nearest vertices first, using a QUEUE."""
        if start not in self.adjacent:
            return []
        visited, order, queue = {start}, [], deque([start])
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
        order, visited = [], set()

        def walk(node):
            visited.add(node)
            order.append(node)
            for neighbour in self.adjacent.get(node, []):
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
        if start == end:
            return [start]
        previous, queue = {start: None}, deque([start])
        while queue:
            node = queue.popleft()
            for neighbour in self.adjacent[node]:
                if neighbour in previous:
                    continue
                previous[neighbour] = node
                if neighbour == end:                 # rebuild the path backwards
                    path = [end]
                    while previous[path[-1]] is not None:
                        path.append(previous[path[-1]])
                    return path[::-1]
                queue.append(neighbour)
        return None


if __name__ == "__main__":
    #    0 --- 1 --- 3
    #    |    /|     |
    #    2 --/ |     4 --- 5 --- 6
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

    # in a DIRECTED graph the edge goes one way only
    d = Graph(directed=True)
    d.add_edge("a", "b")
    assert d.has_edge("a", "b") and not d.has_edge("b", "a")
    assert d.edge_count == 1

    print(g)
    print("Success.")
