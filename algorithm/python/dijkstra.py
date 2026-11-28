#---------------------------------------------------------------
# DIJKSTRA'S SHORTEST PATH
#---------------------------------------------------------------
#
# Scope: shortest paths from ONE source when edges have NON-NEGATIVE
#        weights. See bfs.py when every edge counts the same,
#        bellman_ford.py when weights can be negative, and
#        floyd_warshall.py for every pair of vertices at once.
#
#           4         1
#       A ----- B ------- D          shortest A -> D:
#       |       |         |            A -> C -> B -> D = 1 + 2 + 1 = 4
#     1 |     2 |         | 3          (not A -> B -> D = 5,
#       |       |         |             and not A -> C -> E -> D = 9)
#       C ------+         E
#       |                 |
#       +--------5--------+
#
# THE IDEA
#   Keep the best distance known so far for every vertex (the source
#   is 0, everything else infinity). Repeatedly:
#     1) take the unfinished vertex with the SMALLEST known distance --
#        that distance is now final
#     2) RELAX its edges: for each neighbour, check whether going
#        through this vertex is shorter than what the neighbour has
#
#       finalise   A=0    then C=1    then B=3    then D=4    then E=6
#       relaxing   B=4    B=3 (via C)  D=4
#                  C=1    E=6
#
# WHY STEP 1 IS SAFE (and why weights must be >= 0)
#   The closest unfinished vertex cannot be improved later: any other
#   route to it would first have to go through a vertex that is
#   already at least as far away, and then add MORE (non-negative)
#   weight. A negative edge breaks exactly that argument -- a longer
#   route could come back cheaper -- so with negative weights use
#   Bellman-Ford.
#
# "Take the smallest" is a job for a MIN-HEAP. heapq cannot lower the
# priority of an entry already inside it, so the standard trick is to
# push a NEW entry and skip the stale ones when they surface.
#
# Time  : V0 (heap)   O((V + E) log V)   -- the default
#         V1 (matrix) O(V^2)             -- better only on DENSE graphs
# Space : O(V + E)
#
# Used by: LC 743 Network Delay Time, LC 1631 Path With Minimum Effort,
#          LC 787 Cheapest Flights Within K Stops (a variant).
#
# References:
#   - https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/


import heapq

INF = float("inf")

# What dijkstra_path records as "reached from" for the SOURCE, which was
# reached from nowhere. A private object, so it can never be mistaken
# for a real vertex.
_NO_PREVIOUS = object()


# V0 : adjacency list + min-heap -- the one to write in an interview
def dijkstra(graph, src):
    """Shortest distance from `src` to every vertex.

    graph: {node: [(neighbour, weight), ...]}, weights >= 0.
    Returns {node: distance}; unreachable vertices map to infinity.
    """
    dist = {node: INF for node in graph}
    dist[src] = 0
    heap = [(0, src)]                # (distance from src, node), smallest first

    while heap:
        d, node = heapq.heappop(heap)
        if d > dist[node]:
            continue                 # stale entry: a shorter route was found meanwhile
        for neighbour, weight in graph[node]:
            if weight < 0:
                raise ValueError("Dijkstra needs non-negative weights")
            new_dist = d + weight
            if new_dist < dist.get(neighbour, INF):      # relax the edge
                dist[neighbour] = new_dist
                heapq.heappush(heap, (new_dist, neighbour))
    return dist


# V0-1 : the same loop, also recording HOW each vertex was reached, so
# the path itself can be rebuilt (not only its length).
def dijkstra_path(graph, src, dst):
    """Return (distance, path) for the shortest src -> dst route.

    (infinity, []) if dst cannot be reached.
    """
    dist = {node: INF for node in graph}
    dist[src] = 0
    came_from = {src: _NO_PREVIOUS}  # came_from[v] = the vertex before v on the best route
    heap = [(0, src)]

    while heap:
        d, node = heapq.heappop(heap)
        if d > dist[node]:
            continue
        if node == dst:
            break                    # dst is final: no need to finish the rest
        for neighbour, weight in graph[node]:
            new_dist = d + weight
            if new_dist < dist.get(neighbour, INF):
                dist[neighbour] = new_dist
                came_from[neighbour] = node
                heapq.heappush(heap, (new_dist, neighbour))

    if dist.get(dst, INF) == INF:
        return INF, []

    # follow the links backwards from dst, then reverse
    path = []
    node = dst
    while node is not _NO_PREVIOUS:
        path.append(node)
        node = came_from[node]
    path.reverse()
    return dist[dst], path


# V1 : adjacency matrix, no heap -- O(V^2)
#
# "Take the smallest" is a linear scan here. On a dense graph (E close
# to V^2) that beats the heap version's O(E log V).
class Graph:
    """Graph as a V x V matrix: graph[u][v] is the weight, 0 means no edge."""

    def __init__(self, vertices):
        self.V = vertices
        self.graph = [[0] * vertices for _ in range(vertices)]

    def _closest_unfinished(self, dist, finished):
        """The unfinished vertex with the smallest distance, or -1 if none is reachable."""
        best = -1
        for v in range(self.V):
            if not finished[v] and dist[v] < INF:
                if best == -1 or dist[v] < dist[best]:
                    best = v
        return best

    def dijkstra(self, src):
        """Shortest distance from src to every vertex, as a list."""
        dist = [INF] * self.V
        dist[src] = 0
        finished = [False] * self.V

        for _ in range(self.V):
            u = self._closest_unfinished(dist, finished)
            if u == -1:
                break                # everything still unfinished is unreachable
            finished[u] = True
            for v in range(self.V):
                weight = self.graph[u][v]
                if weight > 0 and not finished[v] and dist[u] + weight < dist[v]:
                    dist[v] = dist[u] + weight
        return dist


if __name__ == "__main__":
    # the graph from the header (undirected: each edge listed both ways)
    small = {
        "A": [("B", 4), ("C", 1)],
        "B": [("A", 4), ("C", 2), ("D", 1)],
        "C": [("A", 1), ("B", 2), ("E", 5)],
        "D": [("B", 1), ("E", 3)],
        "E": [("C", 5), ("D", 3)],
    }
    assert dijkstra(small, "A") == {"A": 0, "B": 3, "C": 1, "D": 4, "E": 6}
    assert dijkstra_path(small, "A", "D") == (4, ["A", "C", "B", "D"])
    assert dijkstra_path(small, "A", "A") == (0, ["A"])

    # the classic 9-vertex example, both representations
    matrix = [
        [0, 4, 0, 0, 0, 0, 0, 8, 0],
        [4, 0, 8, 0, 0, 0, 0, 11, 0],
        [0, 8, 0, 7, 0, 4, 0, 0, 2],
        [0, 0, 7, 0, 9, 14, 0, 0, 0],
        [0, 0, 0, 9, 0, 10, 0, 0, 0],
        [0, 0, 4, 14, 10, 0, 2, 0, 0],
        [0, 0, 0, 0, 0, 2, 0, 1, 6],
        [8, 11, 0, 0, 0, 0, 1, 0, 7],
        [0, 0, 2, 0, 0, 0, 6, 7, 0],
    ]
    expected = [0, 4, 12, 19, 21, 11, 9, 8, 14]

    g = Graph(9)
    g.graph = matrix
    assert g.dijkstra(0) == expected

    # the same graph as an adjacency list, built from the matrix
    adjacency = {u: [] for u in range(9)}
    for u in range(9):
        for v in range(9):
            if matrix[u][v] > 0:
                adjacency[u].append((v, matrix[u][v]))
    by_heap = dijkstra(adjacency, 0)
    assert [by_heap[v] for v in range(9)] == expected

    # an unreachable vertex stays at infinity -- in both versions
    island = Graph(3)
    island.graph = [[0, 5, 0], [5, 0, 0], [0, 0, 0]]
    assert island.dijkstra(0) == [0, 5, INF]
    assert dijkstra({0: [(1, 5)], 1: [(0, 5)], 2: []}, 0) == {0: 0, 1: 5, 2: INF}
    assert dijkstra_path({0: [(1, 5)], 1: [], 2: []}, 0, 2) == (INF, [])

    # a directed edge is followed one way only
    one_way = {"a": [("b", 1)], "b": []}
    assert dijkstra(one_way, "b") == {"a": INF, "b": 0}

    # a negative weight is refused, not silently mis-answered
    try:
        dijkstra({"a": [("b", -1)], "b": []}, "a")
        raise AssertionError("expected ValueError")
    except ValueError:
        pass

    # heap and matrix versions agree on random graphs
    import random

    random.seed(1)
    for _ in range(100):
        n = random.randint(1, 8)
        weights = [[0] * n for _ in range(n)]
        for u in range(n):
            for v in range(n):
                if u != v and random.random() < 0.4:
                    weights[u][v] = random.randint(1, 9)
        dense = Graph(n)
        dense.graph = weights
        sparse = {u: [(v, weights[u][v]) for v in range(n) if weights[u][v] > 0]
                  for u in range(n)}
        from_heap = dijkstra(sparse, 0)
        assert [from_heap[v] for v in range(n)] == dense.dijkstra(0)

    print("Success.")
